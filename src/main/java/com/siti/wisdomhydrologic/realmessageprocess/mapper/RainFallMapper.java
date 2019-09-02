package com.siti.wisdomhydrologic.realmessageprocess.mapper;

import com.siti.wisdomhydrologic.realmessageprocess.entity.RainfallEntity;
import org.apache.ibatis.annotations.Select;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

/**
 * Created by DC on 2019/7/19.
 *
 * @data ${DATA}-15:16
 */
public interface RainFallMapper extends Mapper<RainfallEntity> {

    @Select("select * from abnormal_rainfall")
    List<RainfallEntity> fetchAll();

}
