package com.siti.wisdomhydrologic.realmessageprocess.service.impl;

import com.siti.wisdomhydrologic.config.ConstantConfig;
import com.siti.wisdomhydrologic.datepull.vo.DayVo;
import com.siti.wisdomhydrologic.realmessageprocess.entity.AbnormalDetailEntity;
import com.siti.wisdomhydrologic.realmessageprocess.entity.RainfallEntity;
import com.siti.wisdomhydrologic.realmessageprocess.entity.RegressionEntity;
import com.siti.wisdomhydrologic.realmessageprocess.mapper.AbnormalDetailMapper;
import com.siti.wisdomhydrologic.realmessageprocess.service.Valve;
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
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Created by DC on 2019/7/18.
 * @data ${DATA}-9:54
 */
@Component
public class HourRegressionValve implements Valve<DayVo, RegressionEntity, AbnormalDetailEntity>, ApplicationContextAware {

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
    public void beforeProcess(List<DayVo> realList) {
        //getRegression
        abnormalDetailMapper = getBean(AbnormalDetailMapper.class);
        List<RegressionEntity> rlists = abnormalDetailMapper.getRegression();
        Map<Integer, RegressionEntity> regmap;
        if (rlists.size() > 0) {
            regmap = rlists.stream()
                    .collect(Collectors.toMap(RegressionEntity::getSectionCode,
                            Function.identity(), (oldData, newData) -> newData));
        } else {
            return;
        }
        Map<Integer, DayVo> map = realList.stream()
                .collect(Collectors.toMap(DayVo::getSenId, Function.identity(), (oldData, newData) -> newData));
        doProcess(map, regmap);
    }

    @Override
    public void beforeProcess(List<DayVo> val, Map<Integer, AbnormalDetailEntity> compare) {

    }

    @Override
    public void doProcess(Map<Integer, DayVo> data, Map<Integer, RegressionEntity> configMap) {
        if (data.size() > 0) {
            final List[] exceptionContainer = {new ArrayList<AbnormalDetailEntity>()};
            RegressionEstimate estimate=  new  RegressionEstimate();
            estimate.initAlgorithm();
            configMap.keySet().stream().forEach(e -> {
                DayVo vo = data.get(e);
                if (vo != null) {
                    RegressionEntity config =  configMap.get(e);
                    estimate.chooseAlgorithm(config.getRefNum());
                    estimate.compute(vo,data,config);
                }
            });
            if (exceptionContainer[0].size() > 0) {
                abnormalDetailMapper.insertFinal(exceptionContainer[0]);
                exceptionContainer[0] = null;
            }
        }
        ;

    }

    @Override
    public void doProcess(Map<Integer, DayVo> val, Map<Integer, RegressionEntity> configMap, LocalDateTime time, Map<Integer, AbnormalDetailEntity> compare) {

    }

}
