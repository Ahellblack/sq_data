package com.siti.wisdomhydrologic.analysis.pipeline.valve;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.siti.wisdomhydrologic.analysis.entity.*;
import com.siti.wisdomhydrologic.config.ConstantConfig;
import com.siti.wisdomhydrologic.analysis.mapper.AbnormalDetailMapper;
import com.siti.wisdomhydrologic.analysis.pipeline.Valve;
import com.siti.wisdomhydrologic.analysis.vo.RealVo;
import com.siti.wisdomhydrologic.util.LocalDateUtil;
import com.siti.wisdomhydrologic.util.enumbean.DataError;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * Created by DC on 2019/7/18.
 *
 * @data ${DATA}-9:54
 */
@Component
public class RealRainfallValve implements Valve<RealVo, Real, RainfallEntity>, ApplicationContextAware {

    private static ApplicationContext context = null;

    AbnormalDetailMapper abnormalDetailMapper = null;

    private final Logger logger = LoggerFactory.getLogger(this.getClass());


    @Override
    public void beforeProcess(List<RealVo> realData) {
        abnormalDetailMapper = getBean(AbnormalDetailMapper.class);
        //-------------------3小时内的数据-----------------
        String before = LocalDateUtil
                .dateToLocalDateTime(realData.get(0).getTime()).minusHours(3)
                .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        List<Real> previousData = abnormalDetailMapper.selectBeforeFiveReal(before, ConstantConfig.RS);
        //------------获取雨量配置表--------------
        Map<Integer, RainfallEntity> configMap = Optional.of(abnormalDetailMapper.fetchAllR()).get().stream()
                .collect(Collectors.toMap(RainfallEntity::getSensorCode, a -> a));
        doProcess(realData, previousData, configMap);
    }

    @Override
    public void doProcess(List<RealVo> realData, List<Real> previousData, Map<Integer, RainfallEntity> configMap) {
        try {
            //---------------已经存在入库得数据-----------------
            Map<String, Real> compareMap = new HashMap<>(3000);
            if (previousData.size() > 0) {
                compareMap = previousData.stream()
                        .collect(Collectors.toMap((real) -> real.getTime() + "," + real.getSensorCode()
                                , account -> account));
            }
            //--------------------筛选出雨量实时数据-------------------------
            Map<Integer, RealVo> mapval = realData.stream().filter(e -> ((e.getSenId() % 100) == ConstantConfig.RS))
                    .collect(Collectors.toMap(RealVo::getSenId, a -> a));

            //--------------------获取回归模型-------------------------
            List<RegressionEntity> rlists = abnormalDetailMapper.getRegression();

            //-------------------------------------------------------------
            final List[] exceptionContainer = {new ArrayList<AbnormalDetailEntity>()};
            String date = LocalDateUtil.dateToLocalDateTime(realData.get(0).getTime())
                    .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));  // 记录时间
            logger.info("雨量数据预计到达数量" + configMap.size() + ",实际到达数量" + mapval.size());

            Iterator<Map.Entry<Integer, RainfallEntity>> iterator = configMap.entrySet().iterator();
            while (iterator.hasNext()) {
                Map.Entry<Integer, RainfallEntity> entry = iterator.next();

                RealVo vo = mapval.get(entry.getKey());  // 元素数据
                RainfallEntity config = entry.getValue();  // 元素配置

                Boolean flag = false;
                Integer sensorCode = entry.getKey();  // 记录传感器元素编号
                String dataErrorCode = null;  // 记录异常错误编号
                Double realvalue = vo == null ? -99 : vo.getFACTV();  // 记录当前元素的数值.-99表示中断异常时数值没有
                String descStr = "RTSQ：" + date + "," + sensorCode + "," + realvalue;


                // 雨量元素的数值为本次减前一次，需要过去前一次的值
                String before = LocalDateUtil
                        .dateToLocalDateTime(realData.get(0).getTime()).minusMinutes(5)
                        .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
                Real lastReal = compareMap.get(before + "," + sensorCode);

                //---------------------------中断分析-------------------------
                if (!flag) {
                    if (vo == null) {  // MQ数据包数据不存在
                        dataErrorCode = DataError.BREAK_RainFall.getErrorCode();
                        flag = true;
                        descStr += "==>中断分析:数据发生中断！";
                    } else {
                        descStr += "==>中断分析:通过！";
                    }
                }

                if (!flag) { // 判断前一次雨量数据是否存在
                    if (lastReal == null) {
                        descStr += "时间为" + before + "," + sensorCode + "的数据缺失，跳过后续分析！";
                        flag = true;
                        dataErrorCode = null;
                    } else {
                        realvalue = vo.getFACTV() - lastReal.getRealVal();
                        descStr += "实际值" + lastReal.getRealVal() + "-" + vo.getFACTV() + "=" + realvalue;
                    }
                }

                //-----------------------------典型值分析---------------------
                if (!flag) { //
                    String JsonConfig = config.getExceptionValue();
                    if (null != JsonConfig && !"".equals(JsonConfig)) {
                        Iterator<Object> jsonIterator = null;

                        try {
                            jsonIterator = JSONArray.parseArray(JsonConfig).iterator();

                            while (jsonIterator.hasNext()) {
                                JSONObject one = (JSONObject) jsonIterator.next();
                                if (Double.parseDouble(one.get("error_value").toString()) == vo.getFACTV()) {
                                    dataErrorCode = one.get("error_code").toString();
                                    flag = true;
                                    descStr += "==>典型值分析:异常类型：" + dataErrorCode + "典型值配置：" + one.get("error_value");
                                    break;
                                }
                            }
                            if (flag == false) {
                                descStr += "==>典型值分析:通过!";
                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    } else {
                        // 典型值配置没有
                        descStr += "==>典型值分析:典型值无配置,典型值分析跳过!";
                    }
                }

                //---------------------------极值分析-------------------------
                if (!flag) {
                    if (lastReal == null) {
                        descStr += "==>雨量" + before + "," + sensorCode + "的数据找不到，跳过分析！";
                    } else {
                        Double rainFallValue = vo.getFACTV() - lastReal.getRealVal();
                        if (rainFallValue < config.getMinFiveLevel()) {
                            dataErrorCode = DataError.LESS_RainFall.getErrorCode();
                            flag = true;
                            descStr += "==>极值分析:小于最小值:" + config.getMinFiveLevel() + " !<" + rainFallValue + " <" + config.getMaxFiveLevel();
                        } else if (rainFallValue > config.getMaxFiveLevel()) {
                            dataErrorCode = DataError.MORE_RainFall.getErrorCode();
                            flag = true;
                            descStr += "==>极值分析得到:超过最大值:" + config.getMinFiveLevel() + " <" + rainFallValue + " !<" + config.getMaxFiveLevel();
                        } else {
                            descStr += "==>极值分析得到:通过！";
                        }
                    }
                }

                //--------------------邻近测站分析-----------------------------------
                if (!flag) {
                    String nearBySensorCode = config.getNearbySensorCode();
                    Double nearByRate = config.getNearbyRate();
                    if (!"".equals(nearBySensorCode) || null != nearByRate) {
                        String lastTime = LocalDateUtil
                                .dateToLocalDateTime(realData.get(0).getTime()).minusMinutes(5)
                                .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
                        List<String> sendorcodeArr = Arrays.asList(nearBySensorCode.split(","));
                        Double validSumValue = new Double(0);
                        int validNum = 0;

                        for (String code : sendorcodeArr) {
                            if (null == mapval.get(Integer.parseInt(code))) {
                                descStr += "," + code + "," + date + "数据不齐全";
                            } else if (null == compareMap.get(lastTime + "," + code)) {
                                descStr += "," + code + "," + lastTime + "数据不齐全";
                            } else {
                                Double end = mapval.get(Integer.parseInt(code)).getFACTV();
                                Double front = compareMap.get(lastTime + "," + code).getRealVal();
                                validSumValue = validSumValue + end - front;
                                validNum++;
                                descStr += "," + code + "->" + end + "-" + front + "=" + (end - front);
                            }
                        }

                        // 判断依赖数据是否大部分存在
                        if ((float) (validNum / sendorcodeArr.size()) >= 0.5) {
                            double avgValue = (validSumValue / validNum);

                            if (avgValue == 0 && realvalue == 0) {
                                descStr += "==>邻近测站分析:通过！";
                            } else if (avgValue != 0 && realvalue == 0) {
                                flag = true;
                                dataErrorCode = DataError.LESSNEAR_RainFall.getErrorCode();
                                descStr += "==>邻近测站分析:异常！本身无雨，周围有雨！";
                            } else if (avgValue == 0 && realvalue != 0) {
                                flag = true;
                                dataErrorCode = DataError.MORENEAR_RainFall.getErrorCode();
                                descStr += "==>邻近测站分析:异常！本身有雨，周围无雨！";
                            } else {  // 本身和周围均值均不为0
                                double diff = realvalue - avgValue;
                                if (diff == 0) {
                                    descStr += "==>邻近测站分析:分析通过！";
                                } else if (diff < 0) {
                                    if (Math.abs(diff) / avgValue > config.getNearbyRate()) {
                                        flag = true;
                                        dataErrorCode = DataError.LESSNEAR_RainFall.getErrorCode();
                                        descStr += "==>邻近测站分析:低于周围均值" + config.getNearbyRate() + "！";
                                    } else {
                                        descStr += "==>邻近测站分析:分析通过！";
                                    }
                                } else { // diff > 0
                                    if (diff / avgValue > config.getNearbyRate()) {
                                        flag = true;
                                        dataErrorCode = DataError.MORENEAR_RainFall.getErrorCode();
                                        descStr += "==>邻近测站分析:高于周围均值" + config.getNearbyRate() + "！";
                                    } else {
                                        descStr += "==>邻近测站分析:分析通过！";
                                    }
                                }
                            }
                        } else {
                            descStr += "==>邻近测站分析:" + validNum + "/" + sendorcodeArr.size() + "缺失依赖过多,跳过分析!";
                        }
                    } else {
                        descStr += "==>邻近测站分析:邻近测站无配置,分析跳过!";
                    }
                }
//                // 雨量暂时不分析回归模型
//                //---------------------------回归模型分析-------------------------
//                if(!flag){
//                    List<RegressionEntity> regressionFunc = rlists.stream()
//                            .filter(object -> object.getSectionCode().equals(sensorCode))
//                            .collect(Collectors.toList());
//
//                    if (regressionFunc.size() <= 0) {  // 回归模型不存在
//                        descStr +="==>回归模型分析:回归模型不存在，跳过分析！";
//                    }
//                    else{
//                        RegressionEntity regressionEntity= regressionFunc.get(0);
//                        Double arg0 = regressionEntity.getArg0();
//                        Double arg1 = regressionEntity.getArg1();
//                        Double redisualMax = regressionEntity.getAbResidualMax();
//                        Integer sensorCode1 = regressionEntity.getRef1SectionCode();
//                        Double value1 = compareMap.get( date + "," + sensorCode1 ).getRealVal();
//
//                        if (regressionEntity.getRefNum() == 1){
//                            if (null == arg0 || null == redisualMax || null == arg1 || null == value1 ){
//                                descStr +="==>回归模型分析:回归模型参数不全，跳过分析！"+regressionEntity.toString();
//                            }
//                            else {
//                                Double predictValue = value1 * arg1 + arg0;
//                                if (Math.abs(predictValue - realvalue)  > redisualMax){
//                                    flag = true;
//                                    descStr +="==>回归模型分析:残差过大，回归模型异常！"+
//                                            "redisualMax < abs(predictValue - realvalue) = arg0 + value1 * arg1 " +
//                                            redisualMax+" < abs(" + predictValue + " -" + realvalue + ") =" + arg0 + "+" + value1 + " *" + arg1;
//                                }
//                                else{
//                                    descStr +=";回归模型分析得到==>残差正常，回归模型正常！"+
//                                            "redisualMax < abs(predictValue - realvalue) = arg0 + value1 * arg1 " +
//                                            redisualMax+" < abs(" + predictValue + " -" + realvalue + ") =" + arg0 + "+" + value1 + " *" + arg1;
//                                }
//                            }
//                        }
//                        else if (regressionEntity.getRefNum() == 2){
//                            Double arg2 = regressionEntity.getArg2();
//                            Integer sensorCode2 = regressionEntity.getRef2SectionCode();
//                            Double value2 = compareMap.get( date + "," + sensorCode2 ).getRealVal();
//
//                            if (null == arg0 || null == redisualMax || null == arg1 || null == value1 ||
//                                    null == arg2 || null == value2 ){
//                                descStr +=";回归模型分析得到==>回归模型参数不全，跳过分析！"+regressionEntity.toString();
//                            }
//                            else {
//                                Double predictValue = value1 * arg1 + value2 * arg2 + arg0;
//                                if (Math.abs(predictValue - realvalue) > redisualMax){
//                                    flag = true;
//                                    descStr +="==>回归模型分析:残差过大，回归模型异常！"+
//                                            "redisualMax < abs(predictValue - realvalue) = arg0 + value1 * arg1 + value2 * arg2" +
//                                            redisualMax+" < abs(" + predictValue + " -" + realvalue + ") =" + arg0 + "+" + value1 + " *" + arg1 + "+" + value2 + " *" + arg2;
//                                }
//                                else{
//                                    descStr +="==>回归模型分析:残差正常，回归模型正常！"+
//                                            "redisualMax < abs(predictValue - realvalue) = arg0 + value1 * arg1 + value2 * arg2" +
//                                            redisualMax+" < abs(" + predictValue + " -" + realvalue + ") =" + arg0 + "+" + value1 + " *" + arg1 + "+" + value2 + " *" + arg2;
//                                }
//                            }
//                        }
//                        else if (regressionEntity.getRefNum() == 3){
//                            Double arg2 = regressionEntity.getArg2();
//                            Double arg3 = regressionEntity.getArg3();
//                            Integer sensorCode2 = regressionEntity.getRef2SectionCode();
//                            Double value2 = compareMap.get( date + "," + sensorCode2 ).getRealVal();
//                            Integer sensorCode3 = regressionEntity.getRef3SectionCode();
//                            Double value3 = compareMap.get( date + "," + sensorCode3 ).getRealVal();
//
//                            if (null == arg0 || null == redisualMax || null == arg1 || null == value1 ||
//                                    null == arg2 || null == value2 || null == arg3 || null == value3){
//                                descStr +="==>回归模型分析:回归模型参数不全，跳过分析！"+regressionEntity.toString();
//                            }
//                            else {
//                                Double predictValue = arg0 + value1 * arg1 + value2 * arg2 + value3 * arg3;
//                                if (Math.abs(predictValue - realvalue) > redisualMax){
//                                    flag = true;
//                                    descStr +="==>回归模型分析:残差过大，回归模型异常！"+
//                                            "redisualMax < abs(predictValue - realvalue) = arg0 + value1 * arg1 + value2 * arg2" +
//                                            redisualMax+" < abs(" + predictValue + " -" + realvalue + ") =" + arg0 + "+" + value1 + " *" + arg1 + "+" + value2 + " *" + arg2 + "+" + value3 + " *" + arg3;
//                                }
//                                else{
//                                    descStr +="==>回归模型分析:残差正常，回归模型正常！"+
//                                            "redisualMax < abs(predictValue - realvalue) = arg0 + value1 * arg1 + value2 * arg2" +
//                                            redisualMax+" < abs(" + predictValue + " -" + realvalue + ") =" + arg0 + "+" + value1 + " *" + arg1 + "+" + value2 + " *" + arg2 + "+" + value3 + " *" + arg3;
//                                }
//                            }
//                        }else{
//                            descStr +="==>回归模型分析:回归模型参数不全，跳过分析！";
//                        }
//
//                        if (flag){
//                            dataErrorCode = DataError.REGRESSION_EXCEPTION_WATERLEVEL.getErrorCode();
//                        }
//                    }
//                }  // 回归模型分析结束


                System.out.println(descStr);
                if (flag && null != dataErrorCode) {  // 出现异常才往异常表添加数据
                    exceptionContainer[0].add(new AbnormalDetailEntity.builer()
                            .date(date)
                            .sensorCode(sensorCode)
                            .errorValue(realvalue)
                            .dataError(dataErrorCode)
                            .description(descStr)
                            .description(descStr)
                            .build());
                }
            }

            if (exceptionContainer[0].size() > 0) {
                abnormalDetailMapper.insertFinal(exceptionContainer[0]);
                exceptionContainer[0] = null;
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("RealRainfallValve异常：{}", e.getMessage());
        }
    }

    public static <T> T getBean(Class<T> requiredType) {
        return context.getBean(requiredType);
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        context = applicationContext;
    }

}
