package com.siti.wisdomhydrologic.datepull.service.impl;

import com.siti.wisdomhydrologic.datepull.entity.DayEntity;
import com.siti.wisdomhydrologic.datepull.mapper.DayMapper;
import com.siti.wisdomhydrologic.datepull.service.DayToDB;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by DC on 2019/7/7.
 *
 * @data ${DATA}-9:45
 */
@Component
public class DayToDBImpl implements DayToDB{
    @Resource
    DayMapper dayMapper;
    @Override
    public int batchInsert(List<DayEntity> entityList) {
        return dayMapper.batchInsert(entityList);
    }
}
