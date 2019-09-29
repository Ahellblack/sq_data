/*
package com.siti.wisdomhydrologic.analysis.pipeline.valve;

import com.siti.wisdomhydrologic.config.ConstantConfig;
import com.siti.wisdomhydrologic.analysis.entity.AbnormalDetailEntity;
import com.siti.wisdomhydrologic.analysis.entity.RainfallEntity;
import com.siti.wisdomhydrologic.analysis.mapper.AbnormalDetailMapper;
import com.siti.wisdomhydrologic.analysis.pipeline.Valve;

import com.siti.wisdomhydrologic.analysis.vo.TSDBVo;
import com.siti.wisdomhydrologic.util.LocalDateUtil;
import com.siti.wisdomhydrologic.util.enumbean.DataError;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

*/
/**
 * Created by DC on 2019/7/18.
 *
 * @data ${DATA}-9:54
 *//*

@Component
public class TSDBRainfallValve implements Valve<TSDBVo,RainfallEntity,AbnormalDetailEntity>,ApplicationContextAware {

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
    public void beforeProcess(List<TSDBVo> realList) {
        abnormalDetailMapper = getBean(AbnormalDetailMapper.class);
        //获取雨量配置表
       int com= ConstantConfig.RS;
        Map<Integer, RainfallEntity> rainfallMap = Optional.of(abnormalDetailMapper.fetchAllR())
                .get()
                .stream()
                .collect(Collectors.toMap(RainfallEntity::getSensorCode, a -> a));
        Map<Integer, TSDBVo> map = realList.stream()
                .filter(
                        e -> (((e.getSENID())%100)==com)
                ).collect(Collectors.toMap(TSDBVo::getSENID, Function.identity(),(oldData,newData) -> newData));
        doProcess( map, rainfallMap);
    }

    @Override
    public void beforeProcess(List<TSDBVo> val, Map<Integer, AbnormalDetailEntity> compare) {

    }



    @Override
    public void doProcess(Map<Integer, TSDBVo> mapval,Map<Integer, RainfallEntity> configMap) {
        if (mapval.size() > 0) {
            final List[] exceptionContainer = {new ArrayList<AbnormalDetailEntity>()};
            mapval.keySet().stream().forEach(e -> {
                RainfallEntity config = configMap.get(e);
                if (config != null) {
                    TSDBVo vo = mapval.get(e);
                    if (vo != null) {
                        //中断次数
                        int limit = config.getInterruptLimit();
                        //一个小时最大最小值 不就是5分钟最大值最小值
                        double hourmax = config.getMaxHourLevel();
                        double hourmin = config.getMinHourLevel();
                        double[] arrayV = {vo.getV0(), vo.getV1(), vo.getV2(), vo.getV3(), vo.getV4(), vo.getV5(),
                                vo.getV6(), vo.getV7(), vo.getV8(), vo.getV9(), vo.getV10(), vo.getV11()};
                        final double[] maxmin = {0, 0};
                        IntStream.range(0, arrayV.length).forEach(i -> {
                            if (arrayV[i] == -99) {
                                //实时数据不存在
                                String date = LocalDateUtil.dateToLocalDateTime(vo.getTime())
                                        .plusHours(-1)
                                        .plusMinutes(i * 5)
                                        .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
                                int flag = abnormalDetailMapper.selectRealExist(e+"", date);
                                if (flag < 1) {
                                    exceptionContainer[0].add(new AbnormalDetailEntity.builer()
                                            .date(date)
                                            .sensorCode(e)
                                            .equipmentError(DataError.EQ_RAIN.getErrorCode())
                                            .build());
                                }
                            }
                        });

                        //中断
                        IntStream.range(0, 13 - limit).forEach(j -> {
                            final int[] flag = {0};
                            IntStream.range(j, j + limit).forEach(k -> {
                                if (arrayV[k] == -99) {
                                    flag[0]++;
                                }
                            });
                            if (flag[0] == limit) {
                                exceptionContainer[0].add(new AbnormalDetailEntity.builer()
                                        .date(LocalDateUtil
                                                .dateToLocalDateTime(vo.getTime())
                                                .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")))
                                        .sensorCode(vo.getSENID())
                                        .dataError(DataError.RAIN_INTER.getErrorCode())
                                        .build());
                            }
                        });
                    }
                }
            });
            if (exceptionContainer[0].size() > 0) {
                abnormalDetailMapper.insertFinal(exceptionContainer[0]);
                exceptionContainer[0] = null;
            }
        }

    }

    @Override
    public void doProcess(Map<Integer, TSDBVo> val, Map<Integer, RainfallEntity> configMap, LocalDateTime time, Map<Integer, AbnormalDetailEntity> compare) {

    }


}
*/
