package com.siti.wisdomhydrologic.datepull.mapper;

import com.siti.wisdomhydrologic.datepull.entity.DayEntity;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

/**
 * Created by DC on 2019/7/6.
 *
 * @data ${DATA}-15:56
 */
public interface DayMapper extends Mapper<DayEntity> {

    @Insert("<script>insert into daydb04_10 values <foreach item=\"item\" " +
            "collection=\"dayList\"  separator=\",\" >(#{item.senId},#{item.time}" +
            ",#{item.v},#{item.avgV},#{item.maxV},#{item.maxT},#{item.minV}," +
            "#{item.minT},#{item.s},#{item.avgS},#{item.maxS},#{item.minS})</foreach></script>")
    int batchInsert(@Param("dayList")List<DayEntity> dayList);

}

