package com.siti.wisdomhydrologic.analysis.service.impl;


import com.siti.wisdomhydrologic.analysis.entity.DayEntity;
import com.siti.wisdomhydrologic.analysis.mapper.DayMapper;
import com.siti.wisdomhydrologic.analysis.service.DayToDB;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by DC on 2019/7/7.
 *
 * @data ${DATA}-9:45
 */
@Component
public class DayToDBImpl implements DayToDB {
    @Resource
    DayMapper dayMapper;
    @Override
    public int batchInsert(List<DayEntity> entityList) {
        return dayMapper.batchInsert(entityList);
    }
}
