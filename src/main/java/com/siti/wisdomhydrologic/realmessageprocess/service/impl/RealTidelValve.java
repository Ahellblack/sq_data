package com.siti.wisdomhydrologic.realmessageprocess.service.impl;

import com.siti.wisdomhydrologic.config.ConstantConfig;
import com.siti.wisdomhydrologic.realmessageprocess.entity.AbnormalDetailEntity;
import com.siti.wisdomhydrologic.realmessageprocess.entity.RainfallEntity;
import com.siti.wisdomhydrologic.realmessageprocess.entity.Real;
import com.siti.wisdomhydrologic.realmessageprocess.entity.TideLevelEntity;
import com.siti.wisdomhydrologic.realmessageprocess.mapper.AbnormalDetailMapper;
import com.siti.wisdomhydrologic.realmessageprocess.service.Valve;
import com.siti.wisdomhydrologic.realmessageprocess.vo.RealVo;
import com.siti.wisdomhydrologic.util.DateTransform;
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
import java.util.concurrent.BlockingQueue;
import java.util.stream.Collectors;

/**
 * Created by DC on 2019/7/18.
 *
 * @data ${DATA}-9:54
 */
@Component
public  class RealTidelValve implements Valve<RealVo,TideLevelEntity,Real>,ApplicationContextAware {

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
        //获取潮位配置表
        Map<Integer, TideLevelEntity> tideLevelMap = Optional.of(abnormalDetailMapper.fetchAllT())
                .get()
                .stream()
                .collect(Collectors.toMap(TideLevelEntity::getSensorCode, b -> b));
        Map<Integer, RealVo> map = realList.stream()
                .filter(
                        e -> ((e.getSenId()%100)==ConstantConfig.TS)
                ).collect(Collectors.toMap(RealVo::getSenId, a -> a));
        doProcess(map,tideLevelMap, LocalDateUtil
                .dateToLocalDateTime(realList.get(0).getTime()),compare);
    }

    @Override
    public void doProcess(Map<Integer, RealVo> mapval, Map<Integer, TideLevelEntity> configMap) {}

    @Override
    public void doProcess(Map<Integer, RealVo> mapval, Map<Integer, TideLevelEntity> configMap, LocalDateTime time,
                          final Map<Integer, Real> finalCompareMap) {
        final List[] exceptionContainer = {new ArrayList<AbnormalDetailEntity>()};
        configMap.keySet().stream().forEach(e -> {
            // 最大值最小值比较
            RealVo vo=mapval.get(e);
            TideLevelEntity config = configMap.get(e);
            if(vo!=null) {
                double realvalue= mapval.get(e).getFACTV();
                double max = config.getLevelMax();
                double min = config.getLevelMin();
                if (realvalue < min) {
                    exceptionContainer[0].add(new AbnormalDetailEntity.builer()
                            .date(LocalDateUtil
                                    .dateToLocalDateTime(vo.getTime())
                                    .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")))
                            .sensorCode(vo.getSenId())  .sensorCode(vo.getSenId())
                            .errorValue(realvalue)
                            .dataError(DataError.LESS_SMALL_T.getErrorCode())
                            .build());
                } else if (realvalue > max) {
                    exceptionContainer[0].add(new AbnormalDetailEntity.builer()
                            .date(LocalDateUtil
                                    .dateToLocalDateTime(vo.getTime())
                                    .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")))
                            .sensorCode(vo.getSenId())  .sensorCode(vo.getSenId())
                            .errorValue(realvalue)
                            .dataError(DataError.MORE_BIG_T.getErrorCode())
                            .build());
                }

            }else{
                //实时数据丢失
                int[] array={71,72,73,75,81,83,84,85,86,89};
                int diff=e- e.intValue()%100;
                int flag=0;
                for(int one=0;one<array.length;one++){
                    if(mapval.containsKey(diff+array[one])){
                        return;
                    }
                    flag++;
                }
                String now= time .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
                if(flag==array.length){
                    //均不存在 diff+89；判断电压
                    int code=(config.getSensorCode() -
                            config.getSensorCode() % 100 + 89);
                    Real ele =finalCompareMap.get(code);
                    if(ele!=null){
                        //测站上传故障
                        exceptionContainer[0].add(new AbnormalDetailEntity.builer()
                                .date(now)
                                .errorPeriod(now)
                                .sensorCode(config.getSensorCode())
                                .equipmentError(DataError.EQ_UPLOAD.getErrorCode())
                                .build());
                    }else{
                        //断电故障
                        exceptionContainer[0].add(new AbnormalDetailEntity.builer()
                                .date(now)
                                .sensorCode(config.getSensorCode())
                                .errorPeriod(now)
                                .equipmentError(DataError.EQ_ELESHUTDOWN.getErrorCode())
                                .build());
                    }
                }else{
                    //测站上传故障

                    exceptionContainer[0].add(new AbnormalDetailEntity.builer()
                            .date(now)
                            .errorPeriod(now)
                            .sensorCode(config.getSensorCode())
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