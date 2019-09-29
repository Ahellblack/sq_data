package com.siti.wisdomhydrologic.analysis.pipeline.valve;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.siti.wisdomhydrologic.config.ConstantConfig;
import com.siti.wisdomhydrologic.analysis.entity.AbnormalDetailEntity;
import com.siti.wisdomhydrologic.analysis.entity.Real;
import com.siti.wisdomhydrologic.analysis.entity.WSEntity;
import com.siti.wisdomhydrologic.analysis.mapper.AbnormalDetailMapper;
import com.siti.wisdomhydrologic.analysis.pipeline.Valve;
import com.siti.wisdomhydrologic.analysis.vo.RealVo;
import com.siti.wisdomhydrologic.util.LocalDateUtil;
import com.siti.wisdomhydrologic.util.enumbean.DataError;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
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
public class RealWindSpeedValve implements Valve <RealVo, WSEntity, Real>, ApplicationContextAware {

    private final org.slf4j.Logger logger = LoggerFactory.getLogger( this.getClass() );

    private static ApplicationContext context = null;

    AbnormalDetailMapper abnormalDetailMapper = null;

    @Override
    public void beforeProcess(List <RealVo> realList, Map <String, Real> compare) {

        abnormalDetailMapper = getBean( AbnormalDetailMapper.class );
        RealVo one = realList.get( 0 );
        //-----------获取风速度配置表---------------
        Map <Integer, WSEntity> ws = Optional.of( abnormalDetailMapper.fetchWS() )
                .get()
                .stream()
                .collect( Collectors.toMap( WSEntity::getSensorCode, b -> b ) );
        //------------------篩選出风速实时数据-------------------
        Map <Integer, RealVo> map = realList.stream()
                .filter(
                        e -> ((e.getSenId() % 100) == ConstantConfig.WSS)
                ).collect( Collectors.toMap( RealVo::getSenId, a -> a ) );
        //------------------筛选出风速历史数据---------
        Map <String, Real> maps = compare.keySet().stream().filter(
                e -> (e.split( "," )[1].contains( one.getSenId() % 100 + "" ))
        ).collect( Collectors.toMap( e -> e, e -> compare.get( e ) ) );
        doProcess( map, ws, LocalDateUtil
                .dateToLocalDateTime( realList.get( 0 ).getTime() ), maps );
    }


    @Override
    public void doProcess(Map <Integer, RealVo> mapval, Map <Integer, WSEntity> configMap, LocalDateTime time, Map <String, Real> finalCompareMap) {
        try {
            final List[] exceptionContainer = {new ArrayList <AbnormalDetailEntity>()};
            if (mapval.size() > 0) {
                configMap.keySet().stream().forEach( e -> {
                    //---------------最大值最小值比较-----------------
                    WSEntity config = configMap.get( e );
                    RealVo vo = mapval.get( e );
                    boolean flag = false;
                    if (vo != null) {
                        double realvalue = mapval.get( e ).getFACTV();
                        if (realvalue < config.getLevelMin()) {
                            exceptionContainer[0].add( new AbnormalDetailEntity.builer()
                                    .date( LocalDateUtil
                                            .dateToLocalDateTime( vo.getTime() )
                                            .format( DateTimeFormatter.ofPattern( "yyyy-MM-dd HH:mm:ss" ) ) )
                                    .sensorCode( vo.getSenId() )
                                    .errorValue( realvalue )
                                    .dataError( DataError.LESS_WINDSPEED.getErrorCode() )
                                    .build() );
                            flag = true;
                        } else if (realvalue > config.getLevelMax()) {
                            exceptionContainer[0].add( new AbnormalDetailEntity.builer()
                                    .date( LocalDateUtil
                                            .dateToLocalDateTime( vo.getTime() )
                                            .format( DateTimeFormatter.ofPattern( "yyyy-MM-dd HH:mm:ss" ) ) )
                                    .sensorCode( vo.getSenId() )
                                    .errorValue( realvalue )
                                    .dataError( DataError.MORE_WINDSPEED.getErrorCode() )
                                    .build() );
                            flag = true;
                        }
                        //---------------------------变化率分析-------------------------
                        if (!flag) {
                            Real real = finalCompareMap.get( vo.getTime().toString() + "," + e );
                            if (real != null) {
                                BigDecimal frant = BigDecimal.valueOf( real.getRealVal() );
                                BigDecimal end = BigDecimal.valueOf( vo.getFACTV() );
                                if (frant.subtract( end ).doubleValue() > config.getUpMax()) {
                                    exceptionContainer[0].add( new AbnormalDetailEntity.builer()
                                            .date( LocalDateUtil
                                                    .dateToLocalDateTime( vo.getTime() )
                                                    .format( DateTimeFormatter.ofPattern( "yyyy-MM-dd HH:mm:ss" ) ) )
                                            .sensorCode( vo.getSenId() )
                                            .errorValue( vo.getFACTV() )
                                            .dataError( DataError.UP_MAX_WINDSPEED.getErrorCode() )
                                            .build() );
                                    flag = true;
                                } else {
                                    if (frant.subtract( end ).doubleValue() < config.getBelowMin()) {
                                        exceptionContainer[0].add( new AbnormalDetailEntity.builer()
                                                .date( LocalDateUtil
                                                        .dateToLocalDateTime( vo.getTime() )
                                                        .format( DateTimeFormatter.ofPattern( "yyyy-MM-dd HH:mm:ss" ) ) )
                                                .sensorCode( vo.getSenId() )
                                                .errorValue( vo.getFACTV() )
                                                .dataError( DataError.DOWN_MAX_WINDSPEED.getErrorCode() )
                                                .build() );
                                        flag = true;
                                    }
                                }
                            }
                        }
                        //------------------------------过程线分析----------------------
                        if (!flag) {
                            List <Double> continueData = finalCompareMap.entrySet()
                                    .stream().map( f -> f.getValue().getRealVal() )
                                    .collect( Collectors.toList() );
                            double[] compare = {999};
                            int[] times = {0};
                            continueData.stream().forEach( k -> {
                                if (k != compare[0]) {
                                    compare[0] = k;
                                } else {
                                    times[0]++;
                                }
                            } );
                            if (config.getDuration() / 5 <= times[0]) {
                                exceptionContainer[0].add( new AbnormalDetailEntity.builer()
                                        .date( LocalDateUtil
                                                .dateToLocalDateTime( vo.getTime() )
                                                .format( DateTimeFormatter.ofPattern( "yyyy-MM-dd HH:mm:ss" ) ) )
                                        .sensorCode( vo.getSenId() )
                                        .dataError( DataError.DURING_WINDSPEED.getErrorCode() )
                                        .build() );
                            }
                            flag = true;
                        }
                        //-----------------------------典型值分析---------------------
                        if (flag) {
                            String JsonConfig = config.getExceptionValue();
                            if (!JsonConfig.equals( "" ) && JsonConfig != null) {
                                JSONArray array = JSONArray.parseArray( JsonConfig );
                                IntStream.range( 0, array.size() ).forEach( i -> {
                                    JSONObject one = (JSONObject) array.get( i );
                                    if (Double.parseDouble( one.get( "error_value" ).toString() ) == realvalue) {
                                        String date = LocalDateUtil
                                                .dateToLocalDateTime( vo.getTime() )
                                                .format( DateTimeFormatter.ofPattern( "yyyy-MM-dd HH:mm:ss" ) );
                                        //TODO
                                        exceptionContainer[0].add( new AbnormalDetailEntity.builer()
                                                .date( date )
                                                .dataError( date )
                                                .equipmentError( one.get( "error_code" ).toString() )
                                                .build() );

                                    }
                                } );
                            }
                        }
                    } else {
                        //---------------------------风向不存在-------------------------
                        String date = time
                                .format( DateTimeFormatter.ofPattern( "yyyy-MM-dd HH:mm:ss" ) );
                        exceptionContainer[0].add( new AbnormalDetailEntity.builer()
                                .date( date )
                                .sensorCode( config.getSensorCode() )
                                .dataError( DataError.BREAK_WINDSPEED.getErrorCode() )
                                .build() );
                    }
                } );
                if (exceptionContainer[0].size() > 0) {
                    abnormalDetailMapper.insertFinal( exceptionContainer[0] );
                    exceptionContainer[0] = null;
                }
            }
        } catch (Exception e) {
            logger.error( "RealWindSpeedValve异常：{}", e.getMessage() );

        }
    }


    public static <T> T getBean(Class <T> requiredType) {
        return context.getBean( requiredType );
    }

    @Override
    public void beforeProcess(List <RealVo> val) {

    }

    @Override
    public void doProcess(Map <Integer, RealVo> mapval, Map <Integer, WSEntity> configMap) {

    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        context = applicationContext;
    }

}