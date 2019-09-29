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
public class RealWindDirectionValve implements Valve <RealVo, WDEntity, Real>, ApplicationContextAware {

    private final Logger logger = LoggerFactory.getLogger( this.getClass() );

    private static ApplicationContext context = null;

    AbnormalDetailMapper abnormalDetailMapper = null;

    @Override
    public void beforeProcess(List <RealVo> realList, Map <String, Real> compare) {
        RealVo one = realList.get( 0 );
        //----------------------获取风向配置表---------------------------
        abnormalDetailMapper = getBean( AbnormalDetailMapper.class );
        //获取潮位配置表
        Map <Integer, WDEntity> tideLevelMap = Optional.of( abnormalDetailMapper.fetchWD() )
                .get()
                .stream()
                .collect( Collectors.toMap( WDEntity::getSensorCode, b -> b ) );
        //-----------------------获取风向事实数据----------------------
        Map <Integer, RealVo> map = realList.stream()
                .filter(
                        e -> ((e.getSenId() % 100) == ConstantConfig.WDS)
                ).collect( Collectors.toMap( RealVo::getSenId, a -> a ) );
        //---------------------筛选出风向历史数据---------
        Map <String, Real> maps = compare.keySet().stream().filter(
                e -> (e.split( "," )[1].contains( one.getSenId() % 100 + "" ))
        ).collect( Collectors.toMap( e -> e, e -> compare.get( e ) ) );
        doProcess( map, tideLevelMap, LocalDateUtil
                .dateToLocalDateTime( realList.get( 0 ).getTime() ), maps );
    }


    @Override
    public void doProcess(Map <Integer, RealVo> mapval, Map <Integer, WDEntity> configMap, LocalDateTime time, final Map <String, Real> finalCompareMap) {
        try {
            final List[] exceptionContainer = {new ArrayList <AbnormalDetailEntity>()};
            configMap.keySet().stream().forEach( e -> {
                //---------------最大值最小值比较----------------
                WDEntity config = configMap.get( e );
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
                                .dataError( DataError.LESS_WINDDIRECTION.getErrorCode() )
                                .errorValue( realvalue )
                                .build() );
                    } else if (realvalue > config.getLevelMax()) {
                        exceptionContainer[0].add( new AbnormalDetailEntity.builer()
                                .date( LocalDateUtil
                                        .dateToLocalDateTime( vo.getTime() )
                                        .format( DateTimeFormatter.ofPattern( "yyyy-MM-dd HH:mm:ss" ) ) )
                                .sensorCode( vo.getSenId() )
                                .errorValue( realvalue )
                                .dataError( DataError.MORE_WINDDIRECTION.getErrorCode() )
                                .build() );
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
                                        .dataError( DataError.UP_MAX_WINDDIRECTION.getErrorCode() )
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
                                            .dataError( DataError.DOWN_MAX_WINDDIRECTION.getErrorCode() )
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
                        double[] compare={999};
                        int[] times={0};
                        continueData.stream().forEach(k->{
                            if(k!=compare[0]){
                                compare[0]=k;
                            }else{
                                times[0]++;
                            }
                        });
                        if(config.getDuration() / 5<=times[0]){
                            exceptionContainer[0].add(new AbnormalDetailEntity.builer()
                                    .date(LocalDateUtil
                                            .dateToLocalDateTime(vo.getTime())
                                            .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")))
                                    .sensorCode(vo.getSenId())
                                    .dataError(DataError.DURING_WINDDIRECTION.getErrorCode())
                                    .build());
                        }
                        flag=true;
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
                    //---------------------------风向不存在-------------------------
                    String date = time
                            .format( DateTimeFormatter.ofPattern( "yyyy-MM-dd HH:mm:ss" ) );
                    exceptionContainer[0].add( new AbnormalDetailEntity.builer()
                            .date( date )
                            .sensorCode( config.getSensorCode() )
                            .dataError( DataError.BREAK_WINDDIRECTION.getErrorCode() )
                            .build() );
                }
            } );
            if (exceptionContainer[0].size() > 0) {
                abnormalDetailMapper.insertFinal( exceptionContainer[0] );
                exceptionContainer[0] = null;
            }
        }catch (Exception e){
            logger.error( "RealWindDirectionValve异常：{}", e.getMessage() );
        }
    }

    public static <T> T getBean(Class <T> requiredType) {
        return context.getBean( requiredType );
    }

    @Override
    public void beforeProcess(List <RealVo> val) {

    }

    @Override
    public void doProcess(Map <Integer, RealVo> mapval, Map <Integer, WDEntity> configMap) {

    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        context = applicationContext;
    }

}