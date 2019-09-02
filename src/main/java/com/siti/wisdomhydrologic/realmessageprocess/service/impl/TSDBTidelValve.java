package com.siti.wisdomhydrologic.realmessageprocess.service.impl;

import com.siti.wisdomhydrologic.config.ConstantConfig;
import com.siti.wisdomhydrologic.datepull.vo.TSDBVo;
import com.siti.wisdomhydrologic.realmessageprocess.entity.AbnormalDetailEntity;
import com.siti.wisdomhydrologic.realmessageprocess.entity.TideLevelEntity;
import com.siti.wisdomhydrologic.realmessageprocess.mapper.AbnormalDetailMapper;
import com.siti.wisdomhydrologic.realmessageprocess.service.Valve;
import com.siti.wisdomhydrologic.util.DateTransform;
import com.siti.wisdomhydrologic.util.LocalDateUtil;
import com.siti.wisdomhydrologic.util.enumbean.DataError;
import com.siti.wisdomhydrologic.util.enumbean.EquimentError;
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
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * Created by DC on 2019/7/18.
 *
 * @data ${DATA}-9:54
 */
@Component
public class TSDBTidelValve implements Valve<TSDBVo, TideLevelEntity, AbnormalDetailEntity>, ApplicationContextAware {

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
        int com=ConstantConfig.TS;
        //获取潮位配置表
        Map<Integer, TideLevelEntity> tideLevelMap = Optional.of(abnormalDetailMapper.fetchAllT())
                .get()
                .stream()
                .collect(Collectors.toMap(TideLevelEntity::getSensorCode, b -> b));
        Map<Integer, TSDBVo> map = realList.stream()
                .filter(
                        e -> ((e.getSENID()%100)==com)
                ).collect(Collectors.toMap(TSDBVo::getSENID, Function.identity(),(oldData, newData) -> newData));
        doProcess(map, tideLevelMap);
    }

    @Override
    public void beforeProcess(List<TSDBVo> val, Map<Integer, AbnormalDetailEntity> compare) {

    }

    @Override
    public void doProcess(Map<Integer, TSDBVo> val, Map<Integer, TideLevelEntity> configMap) {
        if (val.size() > 0) {
            Map<Integer, TSDBVo> mapval = val;
            final List[] exceptionContainer = {new ArrayList<AbnormalDetailEntity>()};
            mapval.keySet().stream().forEach(e -> {
                TideLevelEntity config = configMap.get(e);
                if (config != null) {
                    final double[] doubles = {99999};
                    final double[] temp = {-99};
                    final int[] timelimit = {0};
                    TSDBVo vo = mapval.get(e);
                    double[] arrayV = {vo.getV0(), vo.getV1(), vo.getV2(), vo.getV3(), vo.getV4(), vo.getV5(),
                            vo.getV6(), vo.getV7(), vo.getV8(), vo.getV9(), vo.getV10(), vo.getV11()};
                    //中断次数
                    int limit = config.getInterruptLimit();
                    IntStream.range(0, 13 - limit).forEach(j -> {
                        final int[] flag = {0};
                        IntStream.range(j, j + limit).forEach(k -> {
                            if (arrayV[k] == -99) {
                                flag[0]++;
                       /* String date = LocalDateUtil.dateToLocalDateTime(vo.getTime())
                                .plusHours(-1)
                                .plusMinutes(k * 5)
                                .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
                        //存在设备异常
                        if (abnormalDetailMapper.selectRealExist(vo.getSENID(), date) > 0) {
                            exceptionContainer[0].add(new AbnormalDetailEntity.builer()
                                    .date(LocalDateUtil
                                            .dateToLocalDateTime(vo.getTime())
                                            .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")))
                                    .sensorCode(vo.getSENID())
                                    .dataError(DataError.RAIN_INTER.getErrorCode())
                                    .build());
                        }*/
                            }
                        });
                        if (flag[0] == limit) {
                            exceptionContainer[0].add(new AbnormalDetailEntity.builer()
                                    .date(LocalDateUtil
                                            .dateToLocalDateTime(vo.getTime())
                                            .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")))
                                    .sensorCode(vo.getSENID())
                                    .dataError(DataError.INTENT_T.getErrorCode())
                                    .build());
                        }
                    });
                    //数据不变的时长
                    IntStream.range(0, arrayV.length).forEach(k -> {
                        if (temp[0] == arrayV[k]) {
                            timelimit[0]++;
                            exceptionContainer[0].add(new AbnormalDetailEntity.builer()
                                    .date(LocalDateUtil
                                            .dateToLocalDateTime(vo.getTime())
                                            .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")))
                                    .sensorCode(vo.getSENID())
                                    .dataError(DataError.DURA_T.getErrorCode())
                                    .build());
                        } else {
                            temp[0] = arrayV[k];
                            timelimit[0] = 1;
                        }

                        if (arrayV[k] == -99) {
                            //实时数据不存在
                            String date = LocalDateUtil.dateToLocalDateTime(vo.getTime())
                                    .plusHours(-1)
                                    .plusMinutes(k * 5)
                                    .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
                            int flag = abnormalDetailMapper.selectRealExist(e, date);
                            if (flag < 1) {
                                exceptionContainer[0].add(new AbnormalDetailEntity.builer()
                                        .date(date)
                                        .sensorCode(e)
                                        .equipmentError(DataError.EQ_TIDE.getErrorCode())
                                        .build());
                            }
                        }
                        //5分钟
                        if (doubles[0] == 66666) {
                            doubles[0] = arrayV[k];
                        } else {
                            if (arrayV[k] > doubles[0]) {
                                if ((arrayV[k] - doubles[0]) > config.getUpMax()) {
                                    exceptionContainer[0].add(new AbnormalDetailEntity.builer()
                                            .date(LocalDateUtil
                                                    .dateToLocalDateTime(vo.getTime())
                                                    .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")))
                                            .sensorCode(vo.getSENID())
                                            .errorValue(arrayV[k])
                                            .dataError(DataError.CHANGE_BIG_T.getErrorCode())
                                            .build());
                                }
                            } else if (arrayV[k] < doubles[0]) {
                                if ((doubles[0] - arrayV[k]) > config.getBelowMin()) {
                                    exceptionContainer[0].add(new AbnormalDetailEntity.builer()
                                            .date(LocalDateUtil
                                                    .dateToLocalDateTime(vo.getTime())
                                                    .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")))
                                            .sensorCode(vo.getSENID()).sensorCode(vo.getSENID())
                                            .errorValue(arrayV[k])
                                            .dataError(DataError.CHANGE_SMALL_T.getErrorCode())
                                            .build());
                                }
                            }
                        }
                /*//最大上升 最大下降
                */
                /*if (doubles[0] == 99999) {
                    doubles[0] = arrayV[k];
                } else {
                    if ( arrayV[k] > doubles[0]) {
                        if ((arrayV[k] - doubles[0]) > config.getUpMax()) {
                            exceptionContainer[0].add(new AbnormalDetailEntity.builer()
                                    .date(LocalDateUtil
                                            .dateToLocalDateTime(vo.getTime())
                                            .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")))
                                    .sensorCode(vo.getSENID()).fiveBelow(0)
                                    .fiveAbove(0).hourBelow(0).hourAbove(0).dayBelow(0)
                                    .dayAbove(0).moreNear(0).lessNear(0).floatingUp(1)
                                    .floatingDown(0).keepTime(0).continueInterrupt(0)
                                    .errorValue(0).errorPeriod("").equipmentError("")
                                    .build());
                        }
                    } else if ( arrayV[k] < doubles[0]) {
                        if ((doubles[0] - arrayV[k]) > config.getBelowMin()) {
                            exceptionContainer[0].add(new AbnormalDetailEntity.builer()
                                    .date(LocalDateUtil
                                            .dateToLocalDateTime(vo.getTime())
                                            .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")))
                                    .sensorCode(vo.getSENID()).fiveBelow(0)
                                    .fiveAbove(0).hourBelow(0).hourAbove(0).dayBelow(0)
                                    .dayAbove(0).moreNear(0).lessNear(0).floatingUp(0)
                                    .floatingDown(1).keepTime(0).continueInterrupt(0)
                                    .errorValue(0).errorPeriod("").equipmentError("")
                                    .build());
                        }
                    }
                }*/
                    });
                }
            });
            if (exceptionContainer[0].size() > 0) {
                abnormalDetailMapper.insertFinal(exceptionContainer[0]);
                exceptionContainer[0] = null;
            }
        }
    }

    @Override
    public void doProcess(Map<Integer, TSDBVo> val, Map<Integer, TideLevelEntity> configMap, LocalDateTime time, Map<Integer, AbnormalDetailEntity> compare) {

    }

}
