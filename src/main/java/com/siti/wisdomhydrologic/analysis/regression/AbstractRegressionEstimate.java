package com.siti.wisdomhydrologic.analysis.regression;

import com.siti.wisdomhydrologic.analysis.entity.AbnormalDetailEntity;
import com.siti.wisdomhydrologic.analysis.entity.RegressionEntity;
import com.siti.wisdomhydrologic.analysis.vo.DayVo;

import java.util.Map;

/**
 * Created by DC on 2019/8/26.
 *
 * @data ${DATA}-13:33
 */
public abstract class AbstractRegressionEstimate {

    public Algorithm algorithm;

    private Algorithm[] algorithmArray;

    public void chooseAlgorithm(int num){
        switch (num){
            case 1: algorithm=algorithmArray[0]; break;
            case 2: algorithm=algorithmArray[1];break;
            case 3:  algorithm=algorithmArray[2];break;
        }
    }

    private static class InnerClass{
        private static Algorithm[] algorithmArray={new ArgsOneAlgorithm()
                ,new ArgsTwoAlgorithm(),new ArgsThreeAlgorithm()};
    }

    public void initAlgorithm() {
        this.algorithmArray = InnerClass.algorithmArray;
    }

    public abstract AbnormalDetailEntity compute(DayVo vo, Map<Integer, DayVo> data, RegressionEntity config);
}
