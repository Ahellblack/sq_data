package com.siti.wisdomhydrologic.datepull.service;

import com.siti.wisdomhydrologic.datepull.entity.RealFiveminSensorDataEntity;

import java.util.List;

/**
 * @author dc
 *
 */
public interface BatchIntoFiveminSensorData {

   /* int batchInsert(List<RealFiveminSensorDataEntity> fiveMinList);*/

    List<RealFiveminSensorDataEntity> selectAllFiveMinData();

}
