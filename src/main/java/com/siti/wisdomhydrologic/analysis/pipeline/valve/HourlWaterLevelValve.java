package com.siti.wisdomhydrologic.analysis.pipeline.valve;

import com.siti.wisdomhydrologic.analysis.entity.*;
import com.siti.wisdomhydrologic.analysis.mapper.AbnormalDetailMapper;
import com.siti.wisdomhydrologic.analysis.pipeline.Valve;
import com.siti.wisdomhydrologic.analysis.pipeline.regression.RegressionEstimate;
import com.siti.wisdomhydrologic.analysis.vo.DayVo;
import com.siti.wisdomhydrologic.config.ConstantConfig;
import com.siti.wisdomhydrologic.util.LocalDateUtil;
import com.siti.wisdomhydrologic.util.enumbean.DataError;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;


@Component
public class HourlWaterLevelValve implements Valve<DayVo, Real,WaterLevelEntity>, ApplicationContextAware {

    private final Logger logger = LoggerFactory.getLogger( this.getClass() );

    private static ApplicationContext context = null;

    AbnormalDetailMapper abnormalDetailMapper = null;

    @Override
    public void beforeProcess(List<DayVo> realData) {
        //getRegression
        abnormalDetailMapper = getBean(AbnormalDetailMapper.class);
        //----------------------获取雨量配置表--------------------------------
        Map <Integer, WaterLevelEntity> configMap = Optional.of( abnormalDetailMapper.fetchAllW() )
                .get()
                .stream()
                .collect(Collectors.toMap(WaterLevelEntity::getSensorCode, a -> a));
        //-------------------3小时内的数据-----------------
        String before=LocalDateUtil
                .dateToLocalDateTime(realData.get(0).getTime()).minusHours(3)
                .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        List<Real> previousData = abnormalDetailMapper.selectBeforeFiveReal(before,ConstantConfig.WS);
        doProcess( realData,previousData, configMap );
    }

    @Override
    public void doProcess(List <DayVo> realData, List<Real> previousData, Map <Integer, WaterLevelEntity> configMap) {
        //---------------查询出得数据-----------------
        Map<String, Real> compareMap=new HashMap<>(3000);
        if (previousData.size() > 0) {
            compareMap = previousData.stream()
                    .collect(Collectors.toMap((real)->real.getTime().toString()+","+real.getSensorCode()
                            ,account -> account));
        }
        //--------------------筛选出mq实时数据-------------------------
        Map<Integer, DayVo> mapval = realData.stream().filter(e -> ((e.getSenId() % 100) == ConstantConfig.WS))
                .collect(Collectors.toMap(DayVo::getSenId, a -> a));
        Map<String, Real> finalCompareMap = compareMap;
        try {
            //-------------回归模型------------------------
            List<RegressionEntity> rlists = abnormalDetailMapper.getRegression();
            Map<Integer, RegressionEntity> regmap = new HashMap<>(0);
            RegressionEstimate estimate=  new  RegressionEstimate();
            estimate.initAlgorithm();
            if (rlists.size() > 0) {
                regmap = rlists.stream()
                        .collect(Collectors.toMap(RegressionEntity::getSectionCode,
                                Function.identity(), (oldData, newData) -> newData));

            }

            //-------------------------------------------------
            if (mapval.size() > 0) {
                final List[] exceptionContainer = {new ArrayList <AbnormalDetailEntity>()};
                Map<Integer, RegressionEntity> finalRegmap = regmap;
                configMap.keySet().stream().forEach(e -> {
                    DayVo vo = mapval.get( e );
                    boolean flag=false;
                    WaterLevelEntity config = configMap.get( e );
                    if (vo != null) {
                        //---------------------最大值，最小值---------------------------------
                        if (vo.getMinV() < config.getLevelMin()) {
                            exceptionContainer[0].add( new AbnormalDetailEntity.builer()
                                    .date( LocalDateUtil
                                            .dateToLocalDateTime( vo.getTime() )
                                            .format( DateTimeFormatter.ofPattern( "yyyy-MM-dd HH:mm:ss" ) ) )
                                    .sensorCode( vo.getSenId() )
                                    .errorValue( vo.getMinV() )
                                    .dataError( DataError.HOUR_LESS_WATERLEVEL.getErrorCode() )
                                    .build());
                            flag=true;
                        } else if (vo.getMaxV() > config.getLevelMax()) {
                            exceptionContainer[0].add( new AbnormalDetailEntity.builer()
                                    .date( LocalDateUtil
                                            .dateToLocalDateTime( vo.getTime() )
                                            .format( DateTimeFormatter.ofPattern( "yyyy-MM-dd HH:mm:ss" ) ) )
                                    .sensorCode( vo.getSenId() )
                                    .errorValue( vo.getMaxV() )
                                    .dataError( DataError.HOUR_MORE_WATERLEVEL.getErrorCode() )
                                    .build() );
                            flag=true;
                        }
                        //---------------------------------回归模型分析--------------------------------
                        if(!flag&& finalRegmap.size()>0){
                            RegressionEntity regConfig= finalRegmap.get( e );
                            if(regConfig!=null) {
                                estimate.chooseAlgorithm( regConfig.getRefNum() );
                                AbnormalDetailEntity abnormal = estimate.compute( vo, mapval, regConfig );
                                if (abnormal != null) {
                                    exceptionContainer[0].add(abnormal);
                                }
                            }
                        }
                    } else {
                        //---------------------------小时雨量不存在-------------------------
                        if(finalCompareMap.size()==0){
                            //其他测站有数据
                            if(previousData.size()>0){
                                String date = LocalDateUtil
                                        .dateToLocalDateTime(realData.get(0).getTime())
                                        .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
                                exceptionContainer[0].add( new AbnormalDetailEntity.builer()
                                        .date( date)
                                        .sensorCode( config.getSensorCode() )
                                        .dataError( DataError.CAL_EXCEPTION.getErrorCode() )
                                        .build() );
                            }else{
                                String date = LocalDateUtil
                                        .dateToLocalDateTime(realData.get(0).getTime())
                                        .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
                                exceptionContainer[0].add( new AbnormalDetailEntity.builer()
                                        .date( date)
                                        .sensorCode( config.getSensorCode() )
                                        .dataError( DataError.WRONG_CONFIG.getErrorCode() )
                                        .build() );
                            }
                        }else{
                            String date = LocalDateUtil
                                    .dateToLocalDateTime(realData.get(0).getTime())
                                    .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
                            exceptionContainer[0].add( new AbnormalDetailEntity.builer()
                                    .date( date)
                                        .sensorCode( config.getSensorCode() )
                                        .dataError( DataError.CAL_EXCEPTION.getErrorCode() )
                                        .build() );
                        }
                    }
                } );
                if (exceptionContainer[0].size() > 0) {
                    abnormalDetailMapper.insertFinal( exceptionContainer[0] );
                    exceptionContainer[0] = null;
                }
            }
        }catch (Exception e){
            logger.error( "HourWaterLevelValve异常：{}", e.getMessage() );

        }
    }

    public static <T> T getBean(Class<T> requiredType) {
        return context.getBean(requiredType);
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        context = applicationContext;
    }

}
