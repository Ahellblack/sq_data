package com.siti.wisdomhydrologic.realmessageprocess.service.impl;

import com.google.common.collect.Maps;
import com.siti.wisdomhydrologic.config.ConstantConfig;
import com.siti.wisdomhydrologic.datepull.vo.DayVo;
import com.siti.wisdomhydrologic.realmessageprocess.entity.AbnormalDetailEntity;
import com.siti.wisdomhydrologic.realmessageprocess.entity.RainfallEntity;
import com.siti.wisdomhydrologic.realmessageprocess.entity.TideLevelEntity;
import com.siti.wisdomhydrologic.realmessageprocess.entity.WaterLevelEntity;
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
import java.util.*;
import java.util.concurrent.BlockingQueue;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Created by DC on 2019/7/18.
 *
 * @data ${DATA}-9:54
 */
@Component
public class DayRainfallValve implements ApplicationContextAware,Valve<DayVo,RainfallEntity,AbnormalDetailEntity> {

    private static ApplicationContext context = null;

    AbnormalDetailMapper abnormalDetailMapper=null;

    public static <T> T getBean(Class<T> requiredType) {
        return context.getBean(requiredType);
    }

    @Override
    public void beforeProcess(List<DayVo> realList) {
        abnormalDetailMapper = getBean(AbnormalDetailMapper.class);
        //获取雨量配置表
        Map<Integer, RainfallEntity> rainfallMap = Optional.of(abnormalDetailMapper.fetchAllR())
                .get()
                .stream()
                .collect(Collectors.toMap(RainfallEntity::getSensorCode, a -> a));
        Map<Integer, DayVo> map = realList.stream()
                .filter(
                        e -> ((e.getSenId()%100)==ConstantConfig.RS)
                ).collect(Collectors.toMap(DayVo::getSenId, Function.identity(),(oldData, newData) -> newData));
        doProcess( map, rainfallMap);
    }

    @Override
    public void beforeProcess(List<DayVo> val, Map<Integer, AbnormalDetailEntity> compare) {
    }

    @Override
    public void doProcess(Map<Integer, DayVo> mapval, Map<Integer, RainfallEntity> configMap) {
        final List[] exceptionContainer = {new ArrayList<AbnormalDetailEntity>()};
        mapval.keySet().stream().forEach(e -> {
            RainfallEntity config = configMap.get(e);
            if(config!=null){
            DayVo vo = mapval.get(e);
            double daymax = config.getMaxDayLevel();
            double daymin = config.getMinDayLevel();
            if(daymax>vo.getMaxV()){
                exceptionContainer[0].add(new AbnormalDetailEntity.builer()
                        .date(LocalDateUtil
                                .dateToLocalDateTime(vo.getTime())
                                .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")))
                        .sensorCode(vo.getSenId())
                        .errorValue(vo.getMaxV())
                        .dataError(DataError.DAY_MORE_R.getErrorCode())
                        .build());
            }
            if(daymin<vo.getMinV()){
                exceptionContainer[0].add(new AbnormalDetailEntity.builer()
                        .date(LocalDateUtil
                                .dateToLocalDateTime(vo.getTime())
                                .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")))
                        .sensorCode(vo.getSenId())
                        .errorValue(vo.getMaxV())
                        .dataError(DataError.DAY_LESS_R.getErrorCode())
                        .build());
            }
        }});
        if (exceptionContainer[0].size() > 0) {
            abnormalDetailMapper.insertFinal(exceptionContainer[0]);
            exceptionContainer[0] = null;
        }
    }

    @Override
    public void doProcess(Map<Integer, DayVo> val, Map<Integer, RainfallEntity> configMap, LocalDateTime time, Map<Integer, AbnormalDetailEntity> compare) {

    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        context = applicationContext;
    }
}
