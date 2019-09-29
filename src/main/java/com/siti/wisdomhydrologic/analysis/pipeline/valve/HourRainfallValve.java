package com.siti.wisdomhydrologic.analysis.pipeline.valve;

import com.siti.wisdomhydrologic.config.ConstantConfig;
import com.siti.wisdomhydrologic.analysis.entity.AbnormalDetailEntity;
import com.siti.wisdomhydrologic.analysis.entity.RainfallEntity;
import com.siti.wisdomhydrologic.analysis.mapper.AbnormalDetailMapper;
import com.siti.wisdomhydrologic.analysis.pipeline.Valve;
import com.siti.wisdomhydrologic.analysis.vo.DayVo;
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
public class HourRainfallValve implements Valve<DayVo, RainfallEntity, AbnormalDetailEntity>, ApplicationContextAware {

    private final Logger logger = LoggerFactory.getLogger( this.getClass() );

    private static ApplicationContext context = null;

    AbnormalDetailMapper abnormalDetailMapper = null;

    @Override
    public void beforeProcess(List<DayVo> realList) {
        //getRegression
        abnormalDetailMapper = getBean(AbnormalDetailMapper.class);
        //获取雨量配置表
        int com = ConstantConfig.RS;
        Map<Integer, RainfallEntity> rainfallMap = Optional.of(abnormalDetailMapper.fetchAllR())
                .get()
                .stream()
                .collect(Collectors.toMap(RainfallEntity::getSensorCode, a -> a));
        Map<Integer, DayVo> map = realList.stream()
                .filter(
                        e -> (((e.getSenId()) % 100) == com)
                ).collect(Collectors.toMap(DayVo::getSenId, Function.identity(), (oldData, newData) -> newData));
        doProcess(map, rainfallMap);
    }


    @Override
    public void doProcess(Map<Integer, DayVo> mapval, Map<Integer, RainfallEntity> configMap, LocalDateTime time
            , Map<String, AbnormalDetailEntity> compare) {
        try {
            if (mapval.size() > 0) {
                final List[] exceptionContainer = {new ArrayList <AbnormalDetailEntity>()};
                configMap.keySet().stream().forEach( e -> {
                    DayVo vo = mapval.get( e );
                    RainfallEntity config = configMap.get( e );
                    if (vo != null) {
                        //一个小时最大最小值
                        double hourmax = config.getMaxHourLevel();
                        double hourmin = config.getMinHourLevel();
                        //5分钟最大值，最小值
                        if (vo.getMinV() < hourmin) {
                            exceptionContainer[0].add( new AbnormalDetailEntity.builer()
                                    .date( LocalDateUtil
                                            .dateToLocalDateTime( vo.getTime() )
                                            .format( DateTimeFormatter.ofPattern( "yyyy-MM-dd HH:mm:ss" ) ) )
                                    .sensorCode( vo.getSenId() )
                                    .errorValue( vo.getMinV() )
                                    .dataError( DataError.HOUR_LESS_RainFall.getErrorCode() )
                                    .build() );
                        } else if (vo.getMaxV() > hourmax) {
                            exceptionContainer[0].add( new AbnormalDetailEntity.builer()
                                    .date( LocalDateUtil
                                            .dateToLocalDateTime( vo.getTime() )
                                            .format( DateTimeFormatter.ofPattern( "yyyy-MM-dd HH:mm:ss" ) ) )
                                    .sensorCode( vo.getSenId() )
                                    .errorValue( vo.getMaxV() )
                                    .dataError( DataError.HOUR_MORE_RainFall.getErrorCode() )
                                    .build() );
                        }
                    } else {
                        //---------------------------小时雨量不存在-------------------------
                        String date = time
                                .format( DateTimeFormatter.ofPattern( "yyyy-MM-dd HH:mm:ss" ) );
                        exceptionContainer[0].add( new AbnormalDetailEntity.builer()
                                .date( date )
                                .sensorCode( config.getSensorCode() )
                                .dataError( DataError.HOUR_BREAK_WATERLEVEL.getErrorCode() )
                                .build() );
                    }
                } );
                if (exceptionContainer[0].size() > 0) {
                    abnormalDetailMapper.insertFinal( exceptionContainer[0] );
                    exceptionContainer[0] = null;
                }
            }
        }catch (Exception e){
            logger.error( "HourRainfallValve异常：{}", e.getMessage() );

        }
    }

    @Override
    public void doProcess(Map<Integer, DayVo> mapval, Map<Integer, RainfallEntity> configMap) {

    }

    public static <T> T getBean(Class<T> requiredType) {
        return context.getBean(requiredType);
    }
    @Override
    public void beforeProcess(List<DayVo> val, Map<String, AbnormalDetailEntity> compare) {
    }
    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        context = applicationContext;
    }

}
