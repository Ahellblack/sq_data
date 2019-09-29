package com.siti.wisdomhydrologic.analysis.pipeline.valve;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.siti.wisdomhydrologic.config.ConstantConfig;
import com.siti.wisdomhydrologic.analysis.entity.AbnormalDetailEntity;
import com.siti.wisdomhydrologic.analysis.entity.RainfallEntity;
import com.siti.wisdomhydrologic.analysis.entity.Real;
import com.siti.wisdomhydrologic.analysis.mapper.AbnormalDetailMapper;
import com.siti.wisdomhydrologic.analysis.pipeline.Valve;
import com.siti.wisdomhydrologic.analysis.vo.RealVo;
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

    private final Logger logger = LoggerFactory.getLogger( this.getClass() );


    @Override
    public void beforeProcess(List<RealVo> realList, Map<String, Real> compare) {
        RealVo one = realList.get( 0 );

        abnormalDetailMapper = getBean(AbnormalDetailMapper.class);
        //------------获取雨量配置表--------------
        Map<Integer, RainfallEntity> rainfallMap = Optional.of(abnormalDetailMapper.fetchAllR()).get().stream()
                .collect(Collectors.toMap(RainfallEntity::getSensorCode, a -> a));
        //--------------------筛选出雨量实时数据-------------------------
        Map<Integer, RealVo> map = realList.stream().filter(e -> ((e.getSenId() % 100) == ConstantConfig.RS))
                .collect(Collectors.toMap(RealVo::getSenId, a -> a));
        //---------------------筛选出雨量历史数据---------
        Map <String, Real> maps = compare.keySet().stream().filter(
                e -> (e.split( "," )[1].contains( one.getSenId() % 100 + "" ))
        ).collect( Collectors.toMap( e -> e, e -> compare.get( e ) ) );
        doProcess(map, rainfallMap, LocalDateUtil.dateToLocalDateTime(realList.get(0).getTime()), maps);
    }

    @Override
    public void doProcess(Map<Integer, RealVo> mapval, Map<Integer, RainfallEntity> configMap, LocalDateTime time,
                          final Map<String, Real> finalCompareMap) {
        try {
            final List[] exceptionContainer = {new ArrayList <AbnormalDetailEntity>()};
            configMap.keySet().stream().forEach( e -> {
                RealVo vo = mapval.get( e );
                boolean flag=false;
                RainfallEntity rainfallEntity = configMap.get( e );
                if (vo != null) {
                    if (finalCompareMap != null) {
                        if (finalCompareMap.size() > 0 && finalCompareMap.get( e ) != null) {
                            double realvalue = (vo.getFACTV() - finalCompareMap.get( e ).getRealVal());
                            if (realvalue < rainfallEntity.getMinFiveLevel()) {
                                exceptionContainer[0].add
                                        ( new AbnormalDetailEntity
                                                .builer()
                                                .date( LocalDateUtil.dateToLocalDateTime( vo.getTime() )
                                                        .format( DateTimeFormatter.ofPattern( "yyyy-MM-dd HH:mm:ss" ) ) )
                                                .sensorCode( vo.getSenId() )
                                                .errorValue( realvalue )
                                                .dataError( DataError.MORE_RainFall.getErrorCode() )
                                                .build() );
                                flag=true;
                            } else if (realvalue > rainfallEntity.getMaxFiveLevel()) {
                                exceptionContainer[0].add( new AbnormalDetailEntity.builer()
                                        .date( LocalDateUtil.dateToLocalDateTime( vo.getTime() )
                                                .format( DateTimeFormatter.ofPattern( "yyyy-MM-dd HH:mm:ss" ) ) )
                                        .sensorCode( vo.getSenId() ).errorValue( realvalue )
                                        .dataError( DataError.LESS_RainFall.getErrorCode() ).build() );
                                flag=true;
                            }
                            if (rainfallEntity.getNearbySensorCode() != null && realvalue != 0 && realvalue != 0.5) {
                                //附近三个点位
                                String[] sendorcodeArr = rainfallEntity.getNearbySensorCode().split( "," );
                                final double[] calval = {0};
                                final double[] num = {0};
                                IntStream.range( 0, sendorcodeArr.length ).forEach( i -> {
                                    int key = Integer.parseInt( sendorcodeArr[i] );
                                    if (mapval.containsKey( key ) && finalCompareMap.containsKey( key )) {
                                        calval[0] = calval[0] + mapval.get( key ).getFACTV() - finalCompareMap.get( key ).getRealVal();
                                        num[0]++;
                                    }
                                } );
                                if (num[0] > 0) {
                                    double avgRate = (calval[0] / num[0]);
                                    double diff = (realvalue - avgRate) >= 0 ? (realvalue - avgRate) : (avgRate - realvalue);
                                    double calRate = diff / avgRate;

                                    if (diff / avgRate > rainfallEntity.getNearbyRate()) {
                                        exceptionContainer[0].add
                                                ( new AbnormalDetailEntity.builer()
                                                        .date( LocalDateUtil.dateToLocalDateTime( vo.getTime() )
                                                                .format( DateTimeFormatter.ofPattern( "yyyy-MM-dd HH:mm:ss" ) ) )
                                                        .sensorCode( vo.getSenId() ).errorValue( realvalue )
                                                        .dataError( DataError.MORENEAR_RainFall.getErrorCode() )
                                                        .build() );
                                    }
                                }
                            }
                            //-----------------------------典型值分析---------------------
                            if (flag) {
                                String JsonConfig = rainfallEntity.getExceptionValue();
                                if (!JsonConfig.equals( "" ) && JsonConfig != null) {
                                    JSONArray array = JSONArray.parseArray( JsonConfig );
                                    IntStream.range( 0, array.size() ).forEach( i -> {
                                        JSONObject one = (JSONObject) array.get( i );
                                        if (Double.parseDouble( one.get( "error_value" ).toString() ) == realvalue) {
                                            String date = LocalDateUtil
                                                    .dateToLocalDateTime( vo.getTime() )
                                                    .format( DateTimeFormatter.ofPattern( "yyyy-MM-dd HH:mm:ss" ) );
                                            exceptionContainer[0].add( new AbnormalDetailEntity.builer()
                                                    .date( date )
                                                    .sensorCode( rainfallEntity.getSensorCode() )
                                                    .dataError( one.get( "error_code" ).toString() )
                                                    .build() );
                                        }
                                    } );
                                }
                            }
                        }
                    }
                } else {
                    //---------------------------不存在-------------------------
                    String date = time
                            .format( DateTimeFormatter.ofPattern( "yyyy-MM-dd HH:mm:ss" ) );
                    exceptionContainer[0].add( new AbnormalDetailEntity.builer()
                            .date( date )
                            .sensorCode( rainfallEntity.getSensorCode() )
                            .dataError( DataError.BREAK_RainFall.getErrorCode() )
                            .build() );

                }
            } );
            if (exceptionContainer[0].size() > 0) {
                abnormalDetailMapper.insertFinal( exceptionContainer[0] );
                exceptionContainer[0] = null;
            }
        }catch (Exception e){
            logger.error( "RealRainfallValve异常：{}", e.getMessage() );
        }
    }
    public static <T> T getBean(Class<T> requiredType) {
        return context.getBean(requiredType);
    }

    @Override
    public void beforeProcess(List<RealVo> val) {

    }
    @Override
    public void doProcess(Map<Integer, RealVo> mapval, Map<Integer, RainfallEntity> configMap) {
    }


    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        context = applicationContext;
    }

}