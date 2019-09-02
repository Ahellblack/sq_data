package com.siti.wisdomhydrologic.datepull.mapper;

import com.siti.wisdomhydrologic.datepull.entity.RealFiveminSensorDataEntity;
import org.apache.ibatis.annotations.Select;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

/**
 * Created by dc on 2018/12/18.
 */
public interface RealFiveminSensorDataMapper extends Mapper<RealFiveminSensorDataEntity> {

    @Select("select * from real_5min_sensor_data ")
    List<RealFiveminSensorDataEntity> selectAllFiveMinData();

   /* @Insert("<script>insert into real_5min_sensor_data values <foreach item=\"value\" " +
            "collection=\"fiveMinList\" open=\"(\" close=\")\" separator=\",\" >#{item}</foreach></script>")
    int batchInsert(@Param("fiveMinList")List<RealFiveminSensorDataEntity> fiveMinList);*/

}
