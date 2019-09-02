package com.siti.wisdomhydrologic.datepull.mapper;

import com.siti.wisdomhydrologic.datepull.vo.StationVo;
import com.siti.wisdomhydrologic.datepull.vo.TSDBVo;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

/**
 * Created by dell on 2019/7/23.
 */
public interface TSDBMapper {



    @Insert("<script>INSERT INTO `${database}` " +
            "(`sensor_code`,`sensor_data_time`, `sensor_data_value0`,`sensor_data_value1`,`sensor_data_value2`,`sensor_data_value3`,`sensor_data_value4`,`sensor_data_value5`,`sensor_data_value6`,`sensor_data_value7`,`sensor_data_value8`,`sensor_data_value9`,`sensor_data_value10`,`sensor_data_value11`," +
            "`sensor_data_value_status0`,`sensor_data_value_status1`,`sensor_data_value_status2`,`sensor_data_value_status3`,`sensor_data_value_status4`,`sensor_data_value_status5`,`sensor_data_value_status6`,`sensor_data_value_status7`,`sensor_data_value_status8`,`sensor_data_value_status9`,`sensor_data_value_status10`,`sensor_data_value_status11`,`sensor_type_id`,`sensor_type_name`,`station_code`,`station_name`) " +
            "VALUES <foreach collection=\"tsdblist\" item=\"item\" separator=\",\">" +
            "(#{item.SENID},#{item.Time},#{item.V0},#{item.V1},#{item.V2},#{item.V3},#{item.V4},#{item.V5},#{item.V6},#{item.V7},#{item.V8},#{item.V9},#{item.V10},#{item.V11}," +
            "#{item.S0},#{item.S1},#{item.S2},#{item.S3},#{item.S4},#{item.S5},#{item.S6},#{item.S7},#{item.S8},#{item.S9},#{item.S10},#{item.S11}," +
            "#{item.sensorTypeId},#{item.sensorTypeName},#{item.stationId},#{item.stationName})</foreach></script>")
    int insertTSDB(@Param("database")String database,@Param("tsdblist") List<TSDBVo> list);

    @Update("CREATE TABLE if not exists `${database}` " +
            "(`sensor_code` varchar(10) COLLATE utf8_bin NOT NULL COMMENT '传感器编号'," +
            " `sensor_data_time` varchar(20) COLLATE utf8_bin NOT NULL COMMENT '传感器数值采集时间'," +
            " `sensor_data_value0` decimal(12,4) DEFAULT NULL COMMENT '第1个五分钟数据采集值'," +
            " `sensor_data_value1` decimal(12,4) DEFAULT NULL COMMENT '第1个五分钟数据采集值'," +
            " `sensor_data_value2` decimal(12,4) DEFAULT NULL COMMENT '第1个五分钟数据采集值'," +
            " `sensor_data_value3` decimal(12,4) DEFAULT NULL COMMENT '第1个五分钟数据采集值', " +
            "`sensor_data_value4` decimal(12,4) DEFAULT NULL COMMENT '第1个五分钟数据采集值', " +
            "`sensor_data_value5` decimal(12,4) DEFAULT NULL COMMENT '第1个五分钟数据采集值', " +
            "`sensor_data_value6` decimal(12,4) DEFAULT NULL COMMENT '第1个五分钟数据采集值', " +
            "`sensor_data_value7` decimal(12,4) DEFAULT NULL COMMENT '第1个五分钟数据采集值', " +
            "`sensor_data_value8` decimal(12,4) DEFAULT NULL COMMENT '第1个五分钟数据采集值', " +
            "`sensor_data_value9` decimal(12,4) DEFAULT NULL COMMENT '第1个五分钟数据采集值', " +
            "`sensor_data_value10` decimal(12,4) DEFAULT NULL COMMENT '第1个五分钟数据采集值', " +
            "`sensor_data_value11` decimal(12,4) NOT NULL COMMENT '第1个五分钟数据采集值', " +
            "`sensor_data_value_status0` tinyint(1) DEFAULT NULL COMMENT '第1个五分钟数据值状态：0 无效；1直接采集；2计算得来；3人工修改；4外系统得到', " +
            "`sensor_data_value_status1` tinyint(1) DEFAULT NULL COMMENT '第1个五分钟数据值状态：0 无效；1直接采集；2计算得来；3人工修改；4外系统得到', `sensor_data_value_status2` tinyint(1) DEFAULT NULL COMMENT '第1个五分钟数据值状态：0 无效；1直接采集；2计算得来；3人工修改；4外系统得到'," +
            " `sensor_data_value_status3` tinyint(1) DEFAULT NULL COMMENT '第1个五分钟数据值状态：0 无效；1直接采集；2计算得来；3人工修改；4外系统得到', `sensor_data_value_status4` tinyint(1) DEFAULT NULL COMMENT '第1个五分钟数据值状态：0 无效；1直接采集；2计算得来；3人工修改；4外系统得到', `sensor_data_value_status5` tinyint(1) DEFAULT NULL COMMENT '第1个五分钟数据值状态：0 无效；1直接采集；2计算得来；3人工修改；4外系统得到', `sensor_data_value_status6` tinyint(1) DEFAULT NULL COMMENT '第1个五分钟数据值状态：0 无效；1直接采集；2计算得来；3人工修改；4外系统得到', `sensor_data_value_status7` tinyint(1) DEFAULT NULL COMMENT '第1个五分钟数据值状态：0 无效；1直接采集；2计算得来；3人工修改；4外系统得到', `sensor_data_value_status8` tinyint(1) DEFAULT NULL COMMENT '第1个五分钟数据值状态：0 无效；1直接采集；2计算得来；3人工修改；4外系统得到', `sensor_data_value_status9` tinyint(1) DEFAULT NULL COMMENT '第1个五分钟数据值状态：0 无效；1直接采集；2计算得来；3人工修改；4外系统得到', `sensor_data_value_status10` tinyint(1) DEFAULT NULL COMMENT '第1个五分钟数据值状态：0 无效；1直接采集；2计算得来；3人工修改；4外系统得到', `sensor_data_value_status11` tinyint(1) DEFAULT NULL COMMENT '第1个五分钟数据值状态：0 无效；1直接采集；2计算得来；3人工修改；4外系统得到', `sensor_type_id` int(11) NOT NULL COMMENT '传感器类型ID', `sensor_type_name` varchar(50) COLLATE utf8_bin DEFAULT NULL COMMENT '传感器类型名称', `station_code` varchar(10) COLLATE utf8_bin DEFAULT NULL COMMENT '传感器所属测站编号', `station_name` varchar(50) COLLATE utf8_bin DEFAULT NULL COMMENT '传感器所属测站名称'\n" + ") " +
            "ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;")
    void buildDatabase(@Param("database")String datavase);


    @Select("Select * from config_sensor_station_comparison")
    List<StationVo> getStation();

}
