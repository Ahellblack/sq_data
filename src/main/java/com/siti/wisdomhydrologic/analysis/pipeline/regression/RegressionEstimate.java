package com.siti.wisdomhydrologic.analysis.pipeline.regression;

import com.siti.wisdomhydrologic.analysis.entity.AbnormalDetailEntity;
import com.siti.wisdomhydrologic.analysis.entity.RegressionEntity;
import com.siti.wisdomhydrologic.analysis.vo.DayVo;

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
