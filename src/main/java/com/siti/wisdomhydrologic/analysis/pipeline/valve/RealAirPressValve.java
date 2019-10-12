package com.siti.wisdomhydrologic.analysis.pipeline.valve;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.siti.wisdomhydrologic.config.ConstantConfig;
import com.siti.wisdomhydrologic.analysis.entity.*;
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

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * Created by DC on 2019/7/18.
 *
 * @data ${DATA}-9:54
 */
@Component
public  class RealAirPressValve implements Valve<RealVo,Real,APEntity>,ApplicationContextAware {

    private final Logger logger = LoggerFactory.getLogger( this.getClass() );

    private static ApplicationContext context = null;

    AbnormalDetailMapper abnormalDetailMapper = null;


    @Override
    public void beforeProcess(List <RealVo> realData) {
        abnormalDetailMapper = getBean(AbnormalDetailMapper.class);
        //-------------------一天内的数据-----------------
        String before=LocalDateUtil
                .dateToLocalDateTime(realData.get(0).getTime()).minusHours(3)
                .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        List<Real> previousData = abnormalDetailMapper.selectBeforeFiveReal(before,ConstantConfig.WAP);
        //----------------------获取气压配置表---------------------------
        Map<Integer, APEntity> configMap = Optional.of(abnormalDetailMapper.fetchAllAP())
                .get()
                .stream()
                .collect(Collectors.toMap(APEntity::getSensorCode, b -> b));
        doProcess( realData,previousData, configMap );
    }


    @Override
    public void doProcess(List <RealVo> realData,List<Real> previousData,Map <Integer, APEntity> configMap) {
        try {
            //---------------已经存在入库得数据-----------------
            Map<String, Real> compareMap=new HashMap<>(3000);
            if (previousData.size() > 0) {
                compareMap = previousData.stream()
                        .collect(Collectors.toMap((real)->real.getTime().toString()+","+real.getSensorCode()
                                ,account -> account));
            }
            //--------------------筛选出雨量实时数据-------------------------
            Map<Integer, RealVo> mapval = realData.stream().filter(e -> ((e.getSenId() % 100) == ConstantConfig.WAP))
                    .collect(Collectors.toMap(RealVo::getSenId, a -> a));
            //-------------------------------------------------------------
            final List[] exceptionContainer = {new ArrayList <AbnormalDetailEntity>()};
            Map<String, Real> finalCompareMap = compareMap;
            configMap.keySet().stream().forEach( e -> {
                // ------------------------最大值最小值比较------------------------
                APEntity config = configMap.get( e );
                RealVo vo = mapval.get( e );
                if (vo != null) {
                    boolean flag = false;
                    double realvalue = mapval.get( e ).getFACTV();
                    if (realvalue < config.getLevelMin()) {
                        exceptionContainer[0].add( new AbnormalDetailEntity.builer()
                                .date( LocalDateUtil
                                        .dateToLocalDateTime( vo.getTime() )
                                        .format( DateTimeFormatter.ofPattern( "yyyy-MM-dd HH:mm:ss" ) ) )
                                .sensorCode( vo.getSenId() )
                                .errorValue( realvalue )
                                .dataError( DataError.LESS_AirPress.getErrorCode() )
                                .build() );
                        flag = true;
                    } else if (realvalue > config.getLevelMax()) {
                        exceptionContainer[0].add( new AbnormalDetailEntity.builer()
                                .date( LocalDateUtil
                                        .dateToLocalDateTime( vo.getTime() )
                                        .format( DateTimeFormatter.ofPattern( "yyyy-MM-dd HH:mm:ss" ) ) )
                                .sensorCode( vo.getSenId() )
                                .errorValue( realvalue )
                                .dataError( DataError.MORE_AirPress.getErrorCode() )
                                .build() );
                        flag = true;
                    }
                    //---------------------------变化率分析-------------------------
                    if (!flag) {
                        String before=LocalDateUtil
                                .dateToLocalDateTime(realData.get(0).getTime()).minusMinutes(5)
                                .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
                        Real real = finalCompareMap.get( before + "," + e );
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
                                        .dataError( DataError.UP_MAX_AirPress.getErrorCode() )
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
                                            .dataError( DataError.DOWN_MAX_AirPress.getErrorCode() )
                                            .build() );
                                    flag = true;
                                }
                            }
                        }
                    }
                    //------------------------------过程线分析----------------------
                    if (!flag) {
                        String before=LocalDateUtil
                                .dateToLocalDateTime(realData.get(0).getTime()).minusMinutes(5)
                                .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
                        Real real = finalCompareMap.get( before + "," + e );
                        if(real!=null) {
                            int times = config.getDuration() / 5;
                            try {
                                List<Real> durList = previousData.subList(0, times);
                                List<Double> continueData = durList
                                        .stream().map(f -> f.getRealVal())
                                        .collect(Collectors.toList());
                                double[] compare = {999};
                                int[] time = {0};
                                continueData.stream().forEach(k -> {
                                    if (k != compare[0]) {
                                        compare[0] = k;
                                        time[0] = 0;
                                    } else {
                                        time[0]++;
                                    }
                                });

                                if (config.getDuration() / 5 == time[0]+1) {
                                    exceptionContainer[0].add(new AbnormalDetailEntity.builer()
                                            .date(LocalDateUtil
                                                    .dateToLocalDateTime(vo.getTime())
                                                    .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")))
                                            .sensorCode(vo.getSenId())
                                            .dataError(DataError.DURING_AirPress.getErrorCode())
                                            .build());
                                }
                            }catch (Exception e1){
                                logger.error("DURING_AirPress过程线分析异常");
                            }
                            flag = true;
                        }else{
                            //排除系统重启
                        }
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
                                    exceptionContainer[0].add( new AbnormalDetailEntity.builer()
                                            .date( date )
                                            .sensorCode( config.getSensorCode() )
                                            .dataError( one.get( "error_code" ).toString() )
                                            .build() );
                                }
                            } );
                        }
                    }
                } else {
                    //---------------------------气压不存在-------------------------
                    String date = LocalDateUtil
                            .dateToLocalDateTime(realData.get(0).getTime())
                            .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
                    exceptionContainer[0].add( new AbnormalDetailEntity.builer()
                            .date( date )
                            .sensorCode( config.getSensorCode() )
                            .dataError( DataError.BREAK_AirPress.getErrorCode() )
                            .build() );
                }
            } );
            if (exceptionContainer[0].size() > 0) {
                abnormalDetailMapper.insertFinal( exceptionContainer[0] );
                exceptionContainer[0] = null;
            }
        }catch (Exception e){
            logger.error( "RealAirPressValve异常：{}", e.getMessage() );
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
