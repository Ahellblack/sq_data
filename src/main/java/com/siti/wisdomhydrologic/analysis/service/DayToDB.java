package com.siti.wisdomhydrologic.analysis.service;


import com.siti.wisdomhydrologic.analysis.entity.DayEntity;

import java.util.List;

/**
 * Created by DC on 2019/7/7.
 *
 * @data ${DATA}-9:44
 */
public interface DayToDB {

    int batchInsert(List<DayEntity> entityList);
}
