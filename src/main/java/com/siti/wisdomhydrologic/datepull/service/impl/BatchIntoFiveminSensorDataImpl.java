package com.siti.wisdomhydrologic.datepull.service.impl;

import com.siti.wisdomhydrologic.datepull.entity.RealFiveminSensorDataEntity;
import com.siti.wisdomhydrologic.datepull.mapper.RealFiveminSensorDataMapper;
import com.siti.wisdomhydrologic.datepull.service.BatchIntoFiveminSensorData;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by DC on 2019/6/19.
 *
 * @data ${DATA}-13:27
 */
@Component
public class BatchIntoFiveminSensorDataImpl implements BatchIntoFiveminSensorData{
    @Resource
    RealFiveminSensorDataMapper realFiveminSensorDataMapper;

   /* @Override
    public int batchInsert(List<RealFiveminSensorDataEntity> fiveMinList) {
        return realFiveminSensorDataMapper.batchInsert(fiveMinList);
    }*/

    @Override
    public List<RealFiveminSensorDataEntity> selectAllFiveMinData() {
        return realFiveminSensorDataMapper.selectAllFiveMinData();
    }
}
