package com.siti.wisdomhydrologic.realmessageprocess.service.impl;

import com.siti.wisdomhydrologic.datepull.vo.DayVo;
import com.siti.wisdomhydrologic.realmessageprocess.entity.AbnormalDetailEntity;
import com.siti.wisdomhydrologic.realmessageprocess.entity.RegressionEntity;
import com.siti.wisdomhydrologic.realmessageprocess.service.AbstractRegressionEstimate;
import java.util.Map;

/**
 * Created by DC on 2019/8/26.
 *
 * @data ${DATA}-15:04
 */
public class RegressionEstimate extends AbstractRegressionEstimate {

    @Override
    public AbnormalDetailEntity compute(DayVo vo, Map<Integer, DayVo> data, RegressionEntity config) {
        return (AbnormalDetailEntity) algorithm.calculate(vo,data,config);
    }
}
