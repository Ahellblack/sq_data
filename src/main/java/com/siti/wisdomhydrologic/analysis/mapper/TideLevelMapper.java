package com.siti.wisdomhydrologic.analysis.mapper;

import com.siti.wisdomhydrologic.analysis.entity.TideLevelEntity;
import org.apache.ibatis.annotations.Select;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

/**
 * Created by DC on 2019/7/19.
 *
 * @data ${DATA}-15:44
 */
public interface TideLevelMapper extends Mapper<TideLevelEntity> {

    @Select("select * from abnormal_tide_level")
    List<TideLevelEntity> fetchAll();
}
