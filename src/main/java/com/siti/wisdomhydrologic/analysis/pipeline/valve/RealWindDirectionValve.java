package com.siti.wisdomhydrologic.analysis.pipeline.valve;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.siti.wisdomhydrologic.config.ConstantConfig;
import com.siti.wisdomhydrologic.analysis.entity.*;
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
public class RealWindDirectionValve implements Valve<RealVo, Real, WDEntity>, ApplicationContextAware {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private static ApplicationContext context = null;

    AbnormalDetailMapper abnormalDetailMapper = null;

    @Override
    public void beforeProcess(List<RealVo> realData) {
        logger.info( new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date())+"进入风向分析!");
        abnormalDetailMapper = getBean(AbnormalDetailMapper.class);

        //-------------------一天内的数据-----------------
        String before = LocalDateUtil
                .dateToLocalDateTime(realData.get(0).getTime()).minusHours(3)
                .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        List<Real> previousData = abnormalDetailMapper.selectBeforeFiveReal(before, ConstantConfig.WDS);
        //----------------------获取风向配置表---------------------------
        //获取潮位配置表
        Map<Integer, WDEntity> configMap = Optional.of(abnormalDetailMapper.fetchWD())
                .get()
                .stream()
                .collect(Collectors.toMap(WDEntity::getSensorCode, b -> b));
        doProcess(realData, previousData, configMap);
    }


    @Override
    public void doProcess(List<RealVo> realData, List<Real> previousData, Map<Integer, WDEntity> configMap) {
        try {
            //---------------已经存在入库得数据-----------------
            Map<String, Real> compareMap = new HashMap<>(3000);
            if (previousData.size() > 0) {
                compareMap = previousData.stream()
                        .collect(Collectors.toMap((real) -> real.getTime() + "," + real.getSensorCode()
                                , account -> account));
            }
            //--------------------筛选出实时数据-------------------------
            Map<Integer, RealVo> mapval = realData.stream().filter(e -> ((e.getSenId() % 100) == ConstantConfig.WDS))
                    .collect(Collectors.toMap(RealVo::getSenId, a -> a));
            //--------------------获取回归模型-------------------------
            List<RegressionEntity> rlists = abnormalDetailMapper.getRegression();

            //-------------------------------------------------------------
            final List[] exceptionContainer = {new ArrayList<AbnormalDetailEntity>()};
            String date = LocalDateUtil.dateToLocalDateTime(realData.get(0).getTime())
                    .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));  // 记录时间
            logger.info("风向数据预计到达数量" + configMap.size() + ",实际到达数量" + mapval.size());

            Iterator<Map.Entry<Integer, WDEntity>> iterator = configMap.entrySet().iterator();
            while (iterator.hasNext()) {
                Map.Entry<Integer, WDEntity> entry = iterator.next();

                try {
                    RealVo vo = mapval.get(entry.getKey());  // 元素数据
                    WDEntity config = entry.getValue();  // 元素配置

                    Boolean flag = false;
                    Integer sensorCode = entry.getKey();  // 记录传感器元素编号
                    String dataErrorCode = null;  // 记录异常错误编号
                    Double realvalue = vo == null ? -99 : vo.getFACTV();  // 记录当前元素的数值.-99表示中断异常时数值没有
                    String descStr = "RTSQ："+date+","+sensorCode+","+realvalue;
                    String savedStr = "";  // 用来记录错误原因，后续写入数据库中

                    //---------------------------中断分析-------------------------
                    if(!flag){
                        if (vo == null) {  // MQ数据包数据不存在
                            dataErrorCode = DataError.BREAK_WINDDIRECTION.getErrorCode();
                            flag = true;
                            descStr +="==>中断分析:数据发生中断！";
                            savedStr = "数据发生中断！";
                        }else{
                            descStr +="==>中断分析:通过！";
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
                                    if (Double.parseDouble(one.get("error_value").toString()) == realvalue) {
                                        dataErrorCode = one.get("error_code").toString();
                                        flag = true;
                                        descStr += "==>典型值分析:异常类型：" + dataErrorCode + "典型值配置：" + one.get("error_value");
                                        savedStr = "出现典型值"+one.get("error_value") +"!";
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
                    if(!flag){
                        if (realvalue < config.getLevelMin()) {
                            dataErrorCode = DataError.LESS_WINDDIRECTION.getErrorCode();
                            flag = true;
                            descStr += "==>极值分析:小于最小值:"+config.getLevelMin()+" !<"+realvalue+" <"+config.getLevelMax();
                            savedStr = "当前数值"+realvalue +",小于最小值配置"+config.getLevelMin()+"!";
                        } else if (realvalue > config.getLevelMax()) {
                            dataErrorCode = DataError.MORE_WINDDIRECTION.getErrorCode();
                            flag = true;
                            descStr += "==>极值分析得到:超过最大值:"+config.getLevelMin()+" <"+realvalue+" !<"+config.getLevelMax();
                            savedStr = "当前数值"+realvalue +",大于最大值配置"+config.getLevelMax()+"!";
                        }else{
                            descStr += "==>极值分析得到:通过！";
                        }
                    }

                    //---------------------------变化率分析-------------------------
                    if (!flag) {
                        if(null == config.getUpMax() || null == config.getDownMax()){
                            descStr +="==>变化率分析:缺少配置，跳过变化率分析！";
                        }else{
                            String before=LocalDateUtil
                                    .dateToLocalDateTime(realData.get(0).getTime()).minusMinutes(5)
                                    .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
                            Real real = compareMap.get( before + "," + sensorCode );
                            if (real != null) {
                                BigDecimal frant = BigDecimal.valueOf( real.getRealVal() );
                                BigDecimal end = BigDecimal.valueOf(realvalue);
                                double result  = end.subtract(frant).doubleValue();

                                if (result > config.getUpMax()) {
                                    dataErrorCode = DataError.UP_MAX_WINDDIRECTION.getErrorCode();
                                    flag = true;
                                    descStr+="==>变化率分析:上升值超过最大值配置！"+end+" -"+frant+" ="+result+" >"+config.getUpMax();
                                    savedStr = "当前上升值" + result + ",超过最大上升值" + config.getUpMax()+"!";
                                } else if(result < config.getDownMax()) {
                                    dataErrorCode = DataError.DOWN_MAX_WINDDIRECTION.getErrorCode();
                                    flag = true;
                                    descStr+="==>变化率分析:下降值超过最大值配置！"+end+" -"+frant+" ="+result+" <"+config.getDownMax();
                                    savedStr = "当前下降值" + result + ",超过最大下降值" + config.getDownMax()+"!";
                                }else{
                                    descStr += "==>变化率分析:通过！";
                                }
                            }else{
                                //为了防止初次启动数据无法查询的问题
                                descStr += "==>变化率分析:跳过分析！"+"时间为"+before+"的数据找不到！";
                            }
                        }
                    }

                    //------------------------------过程线分析----------------------
                    if (!flag) {
                        if (null == config.getDuration() || "".equals(config.getDuration())) {
                            descStr += "==>过程线分析:缺少配置，跳过过程线分析！";
                        } else if (config.getDuration() < 10) {
                            descStr += "==>过程线分析:配置时间过短，跳过过程线分析！";
                        } else {
                            String before = LocalDateUtil
                                    .dateToLocalDateTime(realData.get(0).getTime()).minusMinutes(config.getDuration())
                                    .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

                            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                            List<Real> realList = previousData.stream().filter(real -> real.getSensorCode() == sensorCode)
                                    .filter(real -> {
                                        try {
                                            return sdf.parse(real.getTime()).after(sdf.parse(before)) && sdf.parse(real.getTime()).before(sdf.parse(date));
                                        } catch (ParseException e) {
                                            e.printStackTrace();
                                        }
                                        return false;
                                    })
                                    .collect(Collectors.toList());

                            int needCount = config.getDuration() / 5;
                            if (((float) realList.size() / needCount) < 0.8) {
                                descStr += "==>过程线分析:缺少依赖数据，跳过过程线分析！";
                            } else {
                                Map<String, Double> map = realList.stream().collect(Collectors.toMap(Real::getTime, Real::getRealVal));

                                // 全部数据相等标志
                                if (realList.stream().allMatch(element -> element.getRealVal().equals(realvalue))) {
                                    flag = true;
                                    dataErrorCode = DataError.DURING_WINDDIRECTION.getErrorCode();
                                    descStr += "==>过程线分析:过程线异常！" + realList.size()+"次数据相等，"+map;
                                    savedStr = "当前连续"+realList.size()+"次数据相等"+"!";
                                } else {
                                    descStr += "==>过程线分析:过程线分析通过！";
                                }
                            }
                        }
                    }  // 过程线分析结束

                    //---------------------------回归模型分析-------------------------
                    if(!flag){
                        List<RegressionEntity> regressionFunc = rlists.stream()
                                .filter(object -> object.getSectionCode().equals(sensorCode))
                                .collect(Collectors.toList());

                        if (regressionFunc.size() <= 0) {  // 回归模型不存在
                            descStr +="==>回归模型分析:回归模型不存在，跳过分析！";
                        }
                        else{
                            RegressionEntity regressionEntity= regressionFunc.get(0);
                            Double arg0 = regressionEntity.getArg0();
                            Double arg1 = regressionEntity.getArg1();
                            Double redisualMax = regressionEntity.getAbResidualMax();
                            Integer sensorCode1 = regressionEntity.getRef1SectionCode();
                            Double value1 = compareMap.get( date + "," + sensorCode1 ).getRealVal();

                            if (regressionEntity.getRefNum() == 1){
                                if (null == arg0 || null == redisualMax || null == arg1 || null == value1 ){
                                    descStr +="==>回归模型分析:回归模型参数不全，跳过分析！"+regressionEntity.toString();
                                }
                                else {
                                    Double predictValue = value1 * arg1 + arg0;
                                    if ( Math.abs(predictValue - realvalue) > redisualMax){
                                        flag = true;
                                        descStr +="==>回归模型分析:残差过大，回归模型异常！"+
                                                "redisualMax < abs(predictValue - realvalue) = arg0 + value1 * arg1 " +
                                                redisualMax+" < abs(" + predictValue + " -" + realvalue + ") =" + arg0 + "+" + value1 + " *" + arg1;
                                        savedStr = "回归异常，预测值 "+predictValue +" 与实际值 " +realvalue + "相差超过" + redisualMax+"!";
                                    }
                                    else{
                                        descStr +=";回归模型分析得到==>残差正常，回归模型正常！"+
                                                "redisualMax < abs(predictValue - realvalue) = arg0 + value1 * arg1 " +
                                                redisualMax+" < abs(" + predictValue + " -" + realvalue + ") =" + arg0 + "+" + value1 + " *" + arg1;
                                    }
                                }
                            }
                            else if (regressionEntity.getRefNum() == 2){
                                Double arg2 = regressionEntity.getArg2();
                                Integer sensorCode2 = regressionEntity.getRef2SectionCode();
                                Double value2 = compareMap.get( date + "," + sensorCode2 ).getRealVal();

                                if (null == arg0 || null == redisualMax || null == arg1 || null == value1 ||
                                        null == arg2 || null == value2 ){
                                    descStr +=";回归模型分析得到==>回归模型参数不全，跳过分析！"+regressionEntity.toString();
                                }
                                else {
                                    Double predictValue = value1 * arg1 + value2 * arg2 + arg0;
                                    if (Math.abs(predictValue - realvalue) > redisualMax){
                                        flag = true;
                                        descStr +="==>回归模型分析:残差过大，回归模型异常！"+
                                                "redisualMax < abs(predictValue - realvalue) = arg0 + value1 * arg1 + value2 * arg2" +
                                                redisualMax+" < abs(" + predictValue + " -" + realvalue + ") =" + arg0 + "+" + value1 + " *" + arg1 + "+" + value2 + " *" + arg2;
                                        savedStr = "回归异常，预测值 "+predictValue +" 与实际值 " +realvalue + "相差超过" + redisualMax+"!";
                                    }
                                    else{
                                        descStr +="==>回归模型分析:残差正常，回归模型正常！"+
                                                "redisualMax < abs(predictValue - realvalue) = arg0 + value1 * arg1 + value2 * arg2" +
                                                redisualMax+" < abs(" + predictValue + " -" + realvalue + ") =" + arg0 + "+" + value1 + " *" + arg1 + "+" + value2 + " *" + arg2;
                                    }
                                }
                            }
                            else if (regressionEntity.getRefNum() == 3){
                                Double arg2 = regressionEntity.getArg2();
                                Double arg3 = regressionEntity.getArg3();
                                Integer sensorCode2 = regressionEntity.getRef2SectionCode();
                                Double value2 = compareMap.get( date + "," + sensorCode2 ).getRealVal();
                                Integer sensorCode3 = regressionEntity.getRef3SectionCode();
                                Double value3 = compareMap.get( date + "," + sensorCode3 ).getRealVal();

                                if (null == arg0 || null == redisualMax || null == arg1 || null == value1 ||
                                        null == arg2 || null == value2 || null == arg3 || null == value3){
                                    descStr +="==>回归模型分析:回归模型参数不全，跳过分析！"+regressionEntity.toString();
                                }
                                else {
                                    Double predictValue = arg0 + value1 * arg1 + value2 * arg2 + value3 * arg3;
                                    if (Math.abs(predictValue - realvalue) > redisualMax){
                                        flag = true;
                                        descStr +="==>回归模型分析:残差过大，回归模型异常！"+
                                                "redisualMax < abs(predictValue - realvalue) = arg0 + value1 * arg1 + value2 * arg2" +
                                                redisualMax+" < abs(" + predictValue + " -" + realvalue + ") =" + arg0 + "+" + value1 + " *" + arg1 + "+" + value2 + " *" + arg2 + "+" + value3 + " *" + arg3;
                                        savedStr = "回归异常，预测值 "+predictValue +" 与实际值 " +realvalue + "相差超过" + redisualMax+"!";
                                    }
                                    else{
                                        descStr +="==>回归模型分析:残差正常，回归模型正常！"+
                                                "redisualMax < abs(predictValue - realvalue) = arg0 + value1 * arg1 + value2 * arg2" +
                                                redisualMax+" < abs(" + predictValue + " -" + realvalue + ") =" + arg0 + "+" + value1 + " *" + arg1 + "+" + value2 + " *" + arg2 + "+" + value3 + " *" + arg3;
                                    }
                                }
                            }else{
                                descStr +="==>回归模型分析:回归模型参数不全，跳过分析！";
                            }

                            if (flag){
                                dataErrorCode = DataError.REGRESSION_EXCEPTION_WINDDIRECTION.getErrorCode();
                            }
                        }
                    }  // 回归模型分析结束

                    logger.info(descStr);
                    if(flag){  // 出现异常才往异常表添加数据
                        exceptionContainer[0].add( new AbnormalDetailEntity.builer()
                                .date(date)
                                .sensorCode( sensorCode )
                                .errorValue( realvalue )
                                .dataError(dataErrorCode)
                                .description(savedStr)
                                .build() );
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            if (exceptionContainer[0].size() > 0) {
                abnormalDetailMapper.insertFinal(exceptionContainer[0]);
                exceptionContainer[0] = null;
            }
        } catch (Exception e) {
            logger.error("RealWindDirectionValve异常：{}", e.getMessage());
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
