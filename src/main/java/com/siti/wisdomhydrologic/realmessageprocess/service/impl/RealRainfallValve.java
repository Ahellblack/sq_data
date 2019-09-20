package com.siti.wisdomhydrologic.realmessageprocess.service.impl;

import com.siti.wisdomhydrologic.config.ConstantConfig;
import com.siti.wisdomhydrologic.realmessageprocess.entity.AbnormalDetailEntity;
import com.siti.wisdomhydrologic.realmessageprocess.entity.RainfallEntity;
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
import java.util.stream.IntStream;

/**
 * Created by DC on 2019/7/18.
 *
 * @data ${DATA}-9:54
 */
@Component
public class RealRainfallValve implements Valve<RealVo, RainfallEntity, Real>, ApplicationContextAware {

    private static ApplicationContext context = null;

    AbnormalDetailMapper abnormalDetailMapper = null;

    public static <T> T getBean(Class<T> requiredType) {
        return context.getBean(requiredType);
    }

    @Override
    public void beforeProcess(List<RealVo> val) {

    }

    @Override
    public void beforeProcess(List<RealVo> realList, Map<Integer, Real> compare) {
        abnormalDetailMapper = getBean(AbnormalDetailMapper.class);
        //获取雨量配置表
        Map<Integer, RainfallEntity> rainfallMap = Optional.of(abnormalDetailMapper.fetchAllR()).get().stream().collect(Collectors.toMap(RainfallEntity::getSensorCode, a -> a));
        Map<Integer, RealVo> map = realList.stream().filter(e -> ((e.getSenId() % 100) == ConstantConfig.RS)).collect(Collectors.toMap(RealVo::getSenId, a -> a));
        doProcess(map, rainfallMap, LocalDateUtil.dateToLocalDateTime(realList.get(0).getTime()), compare);
    }

    @Override
    public void doProcess(Map<Integer, RealVo> mapval, Map<Integer, RainfallEntity> configMap) {
    }

    @Override
    public void doProcess(Map<Integer, RealVo> mapval, Map<Integer, RainfallEntity> configMap, LocalDateTime times, final Map<Integer, Real> finalCompareMap) {
        final List[] exceptionContainer = {new ArrayList<AbnormalDetailEntity>()};
        configMap.keySet().stream().forEach(e -> {
            RealVo vo = mapval.get(e);
            RainfallEntity rainfallEntity = configMap.get(e);
            if (vo != null) {
                if (finalCompareMap != null) {
                    if (finalCompareMap.size() > 0 && finalCompareMap.get(e) != null) {
                        double realvalue = (vo.getFACTV() - finalCompareMap.get(e).getRealVal());
                        double max = rainfallEntity.getMaxFiveLevel();
                        double min = rainfallEntity.getMinFiveLevel();
                        if (realvalue < min) {
                            exceptionContainer[0].add
                                    (new AbnormalDetailEntity
                                            .builer()
                                            .date(LocalDateUtil.dateToLocalDateTime(vo.getTime())
                                            .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")))
                                            .sensorCode(vo.getSenId())
                                            .errorValue(realvalue)
                                            .dataError(DataError.FIVE_LESS_R.getErrorCode())
                                            .build());
                        } else if (realvalue > max) {
                            exceptionContainer[0].add(new AbnormalDetailEntity.builer().date(LocalDateUtil.dateToLocalDateTime(vo.getTime()).format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))).sensorCode(vo.getSenId()).errorValue(realvalue).dataError(DataError.FIVE_MORE_R.getErrorCode()).build());
                        }
                        if (rainfallEntity.getNearbySensorCode() != null&&realvalue!=0&&realvalue!=0.5) {
                            //附近三个点位
                            String[] sendorcodeArr = rainfallEntity.getNearbySensorCode().split(",");
                            final double[] calval = {0};
                            final double[] num = {0};
                            IntStream.range(0, sendorcodeArr.length).forEach(i -> {
                                int key = Integer.parseInt(sendorcodeArr[i]);
                                if (mapval.containsKey(key) && finalCompareMap.containsKey(key)) {
                                    calval[0] = calval[0] + mapval.get(key).getFACTV() - finalCompareMap.get(key).getRealVal();
                                    num[0]++;
                                }
                            });
                            if (num[0] > 0) {
                                double avgRate = (calval[0] / num[0]);
                                double diff = (realvalue - avgRate) >= 0 ? (realvalue - avgRate) : (avgRate - realvalue);
                                double calRate=diff / avgRate;

                                if (diff / avgRate > rainfallEntity.getNearbyRate()) {
                                    exceptionContainer[0].add
                                            (new AbnormalDetailEntity.builer()
                                                    .date(LocalDateUtil.dateToLocalDateTime(vo.getTime())
                                                            .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")))
                                                    .sensorCode(vo.getSenId()).errorValue(realvalue)
                                                    .dataError(DataError.MORENEAR_R.getErrorCode())
                                                    .build());
                                }
                            }
                        }
                    }
                }
            } else {
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
                String now = times.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
                if (flag == array.length) {
                    //均不存在 diff+89；判断电压
                    int code = (rainfallEntity.getSensorCode() - rainfallEntity.getSensorCode() % 100 + 89);
                        if (finalCompareMap.containsKey(code)) {
                            //测站上传故障
                            exceptionContainer[0].add(new AbnormalDetailEntity.builer().date(now).sensorCode(rainfallEntity.getSensorCode()).errorPeriod(now).equipmentError(DataError.EQ_UPLOAD.getErrorCode()).build());
                        } else {
                            //断电故障
                            exceptionContainer[0].add(new AbnormalDetailEntity.builer().date(now).sensorCode(rainfallEntity.getSensorCode()).errorPeriod(now).equipmentError(DataError.EQ_ELESHUTDOWN.getErrorCode()).build());
                        }
                } else {
                    //测站上传故障
                    exceptionContainer[0].
                            add(new AbnormalDetailEntity.builer()
                                    .date(now)
                                    .sensorCode(rainfallEntity.getSensorCode())
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

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        context = applicationContext;
    }

}
