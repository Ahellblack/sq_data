package com.siti.wisdomhydrologic.analysis.pipeline.regression;

import com.siti.wisdomhydrologic.config.ConstantConfig;
import com.siti.wisdomhydrologic.analysis.entity.AbnormalDetailEntity;
import com.siti.wisdomhydrologic.analysis.entity.RegressionEntity;
import com.siti.wisdomhydrologic.analysis.vo.DayVo;
import com.siti.wisdomhydrologic.util.LocalDateUtil;
import com.siti.wisdomhydrologic.util.enumbean.DataError;

import java.time.format.DateTimeFormatter;
import java.util.Map;

/**
 * Created by DC on 2019/8/26.
 *
 * @data ${DATA}-13:55
 */
public class ArgsOneAlgorithm implements Algorithm <DayVo, RegressionEntity, AbnormalDetailEntity> {

    @Override
    public AbnormalDetailEntity calculate(DayVo vo, Map <Integer, DayVo> data, RegressionEntity config) {
        //------------------判断是否为雨量 取值V--其他取值avg------------------
        if (vo.getSenId() % 100 == ConstantConfig.RS) {
            if (data.containsKey( config.getRef1SectionCode() )) {
                if (Math.abs( vo.getV() -
                        (config.getArg1() * (data.get( config.getRef1SectionCode() ).getV()) + config.getArg0()) )
                        > config.getAbResidualMax()) {
                    return new AbnormalDetailEntity.builer()
                            .date( LocalDateUtil
                                    .dateToLocalDateTime( vo.getTime() )
                                    .format( DateTimeFormatter.ofPattern( "yyyy-MM-dd HH:mm:ss" )))
                            .sensorCode(vo.getSenId())
                            .errorValue(vo.getMaxV())
                            .dataError( DataError.REGRESSION_EXCEPTION.getErrorCode() )
                            .build();
                }
            } else {
                return new AbnormalDetailEntity.builer()
                        .date( LocalDateUtil
                                .dateToLocalDateTime( vo.getTime() )
                                .format( DateTimeFormatter.ofPattern( "yyyy-MM-dd HH:mm:ss" ) ) )
                        .sensorCode( vo.getSenId() )
                        .errorValue( vo.getMaxV() )
                        .dataError( DataError.REGRESSION_NOT_FOUND.getErrorCode() )
                        .build();
            }
        } else {
            if (data.containsKey( config.getRef1SectionCode() )) {
                if (Math.abs( vo.getAvgV() -
                        (config.getArg1() * (data.get( config.getRef1SectionCode() ).getAvgV()) + config.getArg0()) )
                        > config.getAbResidualMax()) {
                    return new AbnormalDetailEntity.builer()
                            .date( LocalDateUtil
                                    .dateToLocalDateTime( vo.getTime() )
                                    .format( DateTimeFormatter.ofPattern( "yyyy-MM-dd HH:mm:ss" ) ) )
                            .sensorCode( vo.getSenId() )
                            .errorValue( vo.getMaxV() )
                            .dataError( DataError.REGRESSION_EXCEPTION.getErrorCode() )
                            .build();
                }
            } else {
                return new AbnormalDetailEntity.builer()
                        .date( LocalDateUtil
                                .dateToLocalDateTime( vo.getTime() )
                                .format( DateTimeFormatter.ofPattern( "yyyy-MM-dd HH:mm:ss" ) ) )
                        .sensorCode( vo.getSenId() )
                        .errorValue( vo.getMaxV() )
                        .dataError( DataError.REGRESSION_NOT_FOUND.getErrorCode() )
                        .build();
            }
        }
        return null;
    }
}
