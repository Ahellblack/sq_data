package com.siti.wisdomhydrologic.realmessageprocess.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.siti.wisdomhydrologic.config.ConstantConfig;
import com.siti.wisdomhydrologic.realmessageprocess.entity.AbnormalDetailEntity;
import com.siti.wisdomhydrologic.realmessageprocess.entity.ELEEntity;
import com.siti.wisdomhydrologic.realmessageprocess.entity.Real;
import com.siti.wisdomhydrologic.realmessageprocess.mapper.AbnormalDetailMapper;
import com.siti.wisdomhydrologic.realmessageprocess.service.Valve;
import com.siti.wisdomhydrologic.realmessageprocess.vo.RealVo;
import com.siti.wisdomhydrologic.util.LocalDateUtil;
import com.siti.wisdomhydrologic.util.enumbean.DataError;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Created by DC on 2019/7/18.
 *
 * @data ${DATA}-9:54
 */
@Component
public  class RealEleValve implements Valve<RealVo,ELEEntity,Real>,ApplicationContextAware {

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        context = applicationContext;
    }

    private static ApplicationContext context = null;

    AbnormalDetailMapper abnormalDetailMapper = null;

    public static <T> T getBean(Class<T> requiredType) {
        return context.getBean(requiredType);
    }

    @Override
    public void beforeProcess(List<RealVo> val) {

    }

    @Override
    public void beforeProcess(List<RealVo> realList,Map<Integer,Real> compare) {
        abnormalDetailMapper = getBean(AbnormalDetailMapper.class);
        //获取at
        Map<Integer, ELEEntity> config = Optional.of(abnormalDetailMapper.fetchAllELE())
                .get()
                .stream()
                .collect(Collectors.toMap(ELEEntity::getSensorCode, b -> b));
        Map<Integer, RealVo> map = realList.stream()
                .filter(
                        e -> ((e.getSenId()%100)==ConstantConfig.ES)
                ).collect(Collectors.toMap(RealVo::getSenId, a -> a));
        doProcess(map, config, LocalDateUtil
                .dateToLocalDateTime(realList.get(0).getTime()),compare);    }

    @Override
    public void doProcess(Map<Integer, RealVo> mapval, Map<Integer, ELEEntity> configMap) {

    }

    @Override
    public void doProcess(Map<Integer, RealVo> mapval, Map<Integer, ELEEntity> configMap, LocalDateTime time, Map<Integer, Real> compare) {
        final List[] exceptionContainer = {new ArrayList<AbnormalDetailEntity>()};
        configMap.keySet().stream().forEach(e -> {
            //        最大值最小值比较
            ELEEntity config = configMap.get(e);
            RealVo vo=mapval.get(e);
            if(vo!=null) {
                double realvalue= mapval.get(e).getFACTV();
                double max = config.getLevelMax();
                double min = config.getLevelMin();
                if (realvalue < min) {
                    exceptionContainer[0].add(new AbnormalDetailEntity.builer()
                            .date(LocalDateUtil
                                    .dateToLocalDateTime(vo.getTime())
                                    .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")))
                            .sensorCode(vo.getSenId())
                            .errorValue(realvalue)
                            .dataError(DataError.LESS_SMALL_E.getErrorCode())
                            .build());
                } else if (realvalue > max) {
                    exceptionContainer[0].add(new AbnormalDetailEntity.builer()
                            .date(LocalDateUtil
                                    .dateToLocalDateTime(vo.getTime())
                                    .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")))
                            .sensorCode(vo.getSenId())
                            .errorValue(realvalue)
                            .dataError(DataError.MORE_BIG_E.getErrorCode())
                            .build());
                }

                String JsonConfig = config.getExceptionValue();
                if (!JsonConfig.equals("") && JsonConfig != null) {
                    JSONArray array = JSONArray.parseArray(JsonConfig);
                    for (int i = 0; i < array.size(); i++) {
                        JSONObject one = (JSONObject) array.get(i);
                        if (Double.parseDouble(one.get("error_value").toString())>=realvalue) {
                            String date = LocalDateUtil
                                    .dateToLocalDateTime(vo.getTime())
                                    .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
                            //TODO
                            exceptionContainer[0].add(new AbnormalDetailEntity.builer()
                                    .date(date)
                                    .errorPeriod(date)
                                    .equipmentError(one.get("error_code").toString())
                                    .build());
                        }
                    }
                }
            }else{
                //实时数据丢失 71 72  73 75 81 83 84 85 86 89
                int[] array = {71, 72, 73, 75, 81, 83, 84, 85, 86, 89};
                int diff = e - e.intValue() % 100;
                int flag = 0;
                for (int one = 0; one < array.length; one++) {
                    if (mapval.containsKey(diff + array[one])) {
                        return;
                    }
                    flag++;
                }
                String now= time .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
                if (flag == array.length) {
                    //均不存在 diff+89；判断电压
                    int code=(config.getSensorCode() -
                            config.getSensorCode() % 100 + 89);
                    Real ele =compare.get(code);
                    if (ele != null) {
                        //测站上传故障
                        exceptionContainer[0].add(new AbnormalDetailEntity.builer()
                                .date(now)
                                .sensorCode(config.getSensorCode())
                                .errorPeriod(now)
                                .equipmentError(DataError.EQ_UPLOAD.getErrorCode())
                                .build());
                    } else {
                        //断电故障
                        exceptionContainer[0].add(new AbnormalDetailEntity.builer()
                                .date(now)
                                .sensorCode(config.getSensorCode())
                                .errorPeriod(now)
                                .equipmentError(DataError.EQ_ELESHUTDOWN.getErrorCode())
                                .build());
                    }
                } else {
                    //测站上传故障
                    exceptionContainer[0].add(new AbnormalDetailEntity.builer()
                            .date(now)
                            .sensorCode(config.getSensorCode())
                            .errorPeriod(now)
                            .equipmentError(DataError.EQ_UPLOAD.getErrorCode())
                            .build());
                }
            }
        });
        if (exceptionContainer[0].size() > 0) {
            abnormalDetailMapper.insertFinal(exceptionContainer[0]);
            exceptionContainer[0] = null;
        }
    }


}
