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
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;


@Component
public class HourWindSpeedValve implements Valve<DayVo, WSEntity, Real>, ApplicationContextAware {

    private final Logger logger = LoggerFactory.getLogger( this.getClass() );

    private static ApplicationContext context = null;

    AbnormalDetailMapper abnormalDetailMapper = null;

    @Override
    public void beforeProcess(List<DayVo> realList, Map<String, Real> compare) {
        //getRegression
        DayVo one = realList.get( 0 );
        abnormalDetailMapper = getBean(AbnormalDetailMapper.class);

        int com = ConstantConfig.WSS;
        //----------------------获取配置表--------------------------------
        Map <Integer, WSEntity> WSS = Optional.of( abnormalDetailMapper.fetchWS() )
                .get()
                .stream()
                .collect( Collectors.toMap( WSEntity::getSensorCode, b -> b ) );
        //--------------------筛选出实时数据-----------------------------
        Map<Integer, DayVo> map = realList.stream()
                .filter(
                        e -> (((e.getSenId()) % 100) == com)
                ).collect(Collectors.toMap(DayVo::getSenId, Function.identity(), (oldData, newData) -> newData));

        doProcess(map, WSS, LocalDateUtil
                .dateToLocalDateTime(realList.get(0).getTime()),compare);
    }

    @Override
    public void doProcess(Map<Integer, DayVo> mapval, Map<Integer, WSEntity> configMap, LocalDateTime time
            , Map<String, Real> compare) {
        try {
            //-------------回归模型------------------------
            List<RegressionEntity> rlists = abnormalDetailMapper.getRegression();
            Map<Integer, RegressionEntity> regmap;
            RegressionEstimate estimate=  new  RegressionEstimate();
            estimate.initAlgorithm();
            if (rlists.size() > 0) {
                regmap = rlists.stream()
                        .collect(Collectors.toMap(RegressionEntity::getSectionCode,
                                Function.identity(), (oldData, newData) -> newData));

            } else {
                return;
            }
            //--------------------筛选出小时内相关-------------------------------
            Map <String, Real> maps = compare.keySet().stream().filter(
                    e -> (e.split( "," )[1].contains(  ConstantConfig.WSS+ "" ))
            ).collect( Collectors.toMap( e -> e, e -> compare.get( e ) ) );
            //-------------------------------------------------
            if (mapval.size() > 0) {
                final List[] exceptionContainer = {new ArrayList <AbnormalDetailEntity>()};
                configMap.keySet().stream().forEach( e -> {
                    DayVo vo = mapval.get( e );
                    boolean flag=false;
                    WSEntity config = configMap.get( e );
                    if (vo != null) {
                        //---------------------最大值，最小值---------------------------------
                        if (vo.getMinV() < config.getLevelMin()) {
                            exceptionContainer[0].add( new AbnormalDetailEntity.builer()
                                    .date( LocalDateUtil
                                            .dateToLocalDateTime( vo.getTime() )
                                            .format( DateTimeFormatter.ofPattern( "yyyy-MM-dd HH:mm:ss" ) ) )
                                    .sensorCode( vo.getSenId() )
                                    .errorValue( vo.getMinV() )
                                    .dataError( DataError.HOUR_LESS_WINDSPEED.getErrorCode() )
                                    .build());
                            flag=true;
                        } else if (vo.getMaxV() > config.getLevelMax()) {
                            exceptionContainer[0].add( new AbnormalDetailEntity.builer()
                                    .date( LocalDateUtil
                                            .dateToLocalDateTime( vo.getTime() )
                                            .format( DateTimeFormatter.ofPattern( "yyyy-MM-dd HH:mm:ss" ) ) )
                                    .sensorCode( vo.getSenId() )
                                    .errorValue( vo.getMaxV() )
                                    .dataError( DataError.HOUR_MORE_WINDSPEED.getErrorCode() )
                                    .build() );
                            flag=true;
                        }
                        //---------------------------------回归模型分析--------------------------------
                        if(!flag&&regmap.size()>0){
                            RegressionEntity regConfig= regmap.get( e );
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
                        //雨量无数据
                        if(maps.size()==0){
                            //其他测站有数据
                            if(compare.size()>0){
                                exceptionContainer[0].add( new AbnormalDetailEntity.builer()
                                        .date( time.format( DateTimeFormatter.ofPattern( "yyyy-MM-dd HH:mm:ss" ) ) )
                                        .sensorCode( config.getSensorCode() )
                                        .dataError( DataError.CAL_EXCEPTION.getErrorCode() )
                                        .build() );
                            }else{
                                exceptionContainer[0].add( new AbnormalDetailEntity.builer()
                                        .date( time.format( DateTimeFormatter.ofPattern( "yyyy-MM-dd HH:mm:ss" ) ) )
                                        .sensorCode( config.getSensorCode() )
                                        .dataError( DataError.WRONG_CONFIG.getErrorCode() )
                                        .build() );
                            }
                        }else{
                                exceptionContainer[0].add( new AbnormalDetailEntity.builer()
                                        .date( time.format( DateTimeFormatter.ofPattern( "yyyy-MM-dd HH:mm:ss" ) ) )
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
            logger.error( "HourWindSpeedValve异常：{}", e.getMessage() );
        }
    }

    @Override
    public void doProcess(Map<Integer, DayVo> mapval, Map<Integer, WSEntity> configMap) {

    }

    public static <T> T getBean(Class<T> requiredType) {
        return context.getBean(requiredType);
    }
    @Override
    public void beforeProcess(List<DayVo> val) {
    }
    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        context = applicationContext;
    }

}
