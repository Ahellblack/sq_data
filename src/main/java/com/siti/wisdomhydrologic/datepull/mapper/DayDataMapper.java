package com.siti.wisdomhydrologic.datepull.mapper;

import com.siti.wisdomhydrologic.datepull.entity.ConfigSensorSectionModule;
import com.siti.wisdomhydrologic.datepull.vo.DayVo;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

/**
 * Created by dell on 2019/7/18.
 */
public interface DayDataMapper {

    @Insert("<script>" +
            "INSERT INTO `${database}` " +
            "( `sensor_code`, `sensor_data_value`, `sensor_type_id`, `sensor_type_name`, " +
            "`sensor_data_unit`, `sensor_data_upload_time`, `sensor_avg_data`, " +
            "`sensor_max_data`, `sensor_max_data_time`, `sensor_min_data`, `sensor_min_data_time`," +
            " `sensor_data_value_flag`, `sensor_avg_data_value_flag`, `sensor_max_data_value_flag`," +
            " `sensor_min_data_value_flag`, `station_code`, `station_name`) VALUES" +
            "<foreach collection=\"dayList\" item=\"item\" separator=\",\">" +
            " (#{item.senId},#{item.v},#{item.sensorTypeId},#{item.sensorTypeName},#{item.sensorDataUnit}," +
            "#{item.time},#{item.avgV},#{item.maxV},#{item.maxT},#{item.minV},#{item.minT}," +
            "#{item.s},#{item.avgS},#{item.maxS},#{item.minS},#{item.stationId},#{item.stationName})" +
            "</foreach></script>")
    int addDayData(@Param("database")String database,@Param("dayList") List<DayVo> dayList) ;

    @Insert("<script>" +
            "INSERT INTO `history_day_sensor_data` " +
            "( `sensor_code`, `sensor_data_value`, `sensor_type_id`, `sensor_type_name`, " +
            "`sensor_data_unit`, `sensor_data_upload_time`, `sensor_avg_data`, " +
            "`sensor_max_data`, `sensor_max_data_time`, `sensor_min_data`, `sensor_min_data_time`," +
            " `sensor_data_value_flag`, `sensor_avg_data_value_flag`, `sensor_max_data_value_flag`," +
            " `sensor_min_data_value_flag`, `station_code`, `station_name`) VALUES" +
            "<foreach collection=\"dayList\" item=\"item\" separator=\",\">" +
            " (#{item.senId},#{item.v},#{item.sensorTypeId},#{item.sensorTypeName},#{item.sensorDataUnit}," +
            "#{item.time},#{item.avgV},#{item.maxV},#{item.maxT},#{item.minV},#{item.minT}," +
            "#{item.s},#{item.avgS},#{item.maxS},#{item.minS},#{item.stationId},#{item.stationName})" +
            "</foreach></script>")
    int addTestDayData(@Param("dayList") List<DayVo> dayList) ;

    @Update("CREATE TABLE if not exists `${database}`" +
            "(`sensor_code` int(10) DEFAULT NULL COMMENT '传感器编号', " +
            "`sensor_data_value` decimal(12,4) DEFAULT NULL COMMENT '传感器采集数值',  " +
            "`sensor_type_id` int(11) DEFAULT NULL COMMENT '传感器类型ID',  " +
            "`sensor_type_name` varchar(50) COLLATE utf8_bin DEFAULT NULL COMMENT '传感器类型名称', " +
            " `sensor_data_unit` varchar(10) COLLATE utf8_bin DEFAULT NULL COMMENT '数值单位', " +
            " `sensor_data_upload_time` varchar(20) COLLATE utf8_bin DEFAULT NULL COMMENT '数据采集时间'," +
            "  `sensor_avg_data` decimal(12,4) DEFAULT NULL COMMENT '为空是否异常', " +
            " `sensor_max_data` decimal(12,4) DEFAULT NULL COMMENT '小时最大值', " +
            " `sensor_max_data_time` varchar(20) COLLATE utf8_bin DEFAULT NULL COMMENT '小时最大值发生时间', " +
            " `sensor_min_data` decimal(12,4) DEFAULT NULL COMMENT '小时最小值', " +
            " `sensor_min_data_time` varchar(20) COLLATE utf8_bin DEFAULT NULL COMMENT '小时最小值发生时间'," +
            "  `sensor_data_value_flag` tinyint(1) DEFAULT NULL COMMENT '整点数值标识：0无效 1 直接采集 2 计算得来 3 人工修改 4 外系统得到',  " +
            "`sensor_avg_data_value_flag` tinyint(1) DEFAULT NULL COMMENT '平均值标识 整点值标识'," +
            "  `sensor_max_data_value_flag` tinyint(1) DEFAULT NULL COMMENT '最大值标识：同整点值标识',  " +
            "`sensor_min_data_value_flag` tinyint(1) DEFAULT NULL COMMENT '最小值标识', " +
            " `calculate_span_hour` tinyint(1) DEFAULT NULL COMMENT '计算时段', " +
            " `station_code` int(11) DEFAULT NULL COMMENT '传感器所属测站编号'," +
            "  `station_name` varchar(50) COLLATE utf8_bin DEFAULT NULL COMMENT '传感器所属测站名称') " +
            "ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;")
    int buildDayBase(@Param("database") String dateBaseName);



    @Select("Select * from config_sensor_section_module")
    List<ConfigSensorSectionModule> getStation();


    @Insert("<script>" +
            "INSERT INTO `wisdomhydrologic`.`${datebase}` " +
            "(`sensor_code`, `sensor_data_value`, `sensor_type_id`, `sensor_type_name`, `sensor_data_unit`," +
            " `sensor_data_upload_time`,`sensor_avg_data`, `sensor_max_data`, `sensor_max_data_time`, `sensor_min_data`, `sensor_min_data_time`, " +
            "`sensor_data_value_flag`, `sensor_avg_data_value_flag`, `sensor_max_data_value_flag`, `sensor_min_data_value_flag`," +
            "  `station_code`, `station_name`)  VALUES" +
            "<foreach collection=\"hourList\" item=\"item\" separator=\",\">" +
            " (#{item.senId},#{item.v},#{item.sensorTypeId},#{item.sensorTypeName},#{item.sensorDataUnit}," +
            "#{item.time},#{item.avgV},#{item.maxV},#{item.maxT},#{item.minV},#{item.minT}," +
            "#{item.s},#{item.avgS},#{item.maxS},#{item.minS},#{item.stationId},#{item.stationName})" +
            "</foreach></script>")
    int addHourData(@Param("datebase")String datebase,@Param("hourList") List<DayVo> hourVo);

    @Update("CREATE TABLE if not exists `${database}`" +
            "(`sensor_code` int(10) DEFAULT NULL COMMENT '传感器编号', " +
            "`sensor_data_value` decimal(12,4) DEFAULT NULL COMMENT '传感器采集数值',  " +
            "`sensor_type_id` int(11) DEFAULT NULL COMMENT '传感器类型ID',  " +
            "`sensor_type_name` varchar(50) COLLATE utf8_bin DEFAULT NULL COMMENT '传感器类型名称', " +
            " `sensor_data_unit` varchar(10) COLLATE utf8_bin DEFAULT NULL COMMENT '数值单位', " +
            " `sensor_data_upload_time` varchar(20) COLLATE utf8_bin DEFAULT NULL COMMENT '数据采集时间'," +
            "  `sensor_avg_data` decimal(12,4) DEFAULT NULL COMMENT '为空是否异常', " +
            " `sensor_max_data` decimal(12,4) DEFAULT NULL COMMENT '小时最大值', " +
            " `sensor_max_data_time` varchar(20) COLLATE utf8_bin DEFAULT NULL COMMENT '小时最大值发生时间', " +
            " `sensor_min_data` decimal(12,4) DEFAULT NULL COMMENT '小时最小值', " +
            " `sensor_min_data_time` varchar(20) COLLATE utf8_bin DEFAULT NULL COMMENT '小时最小值发生时间'," +
            "  `sensor_data_value_flag` tinyint(1) DEFAULT NULL COMMENT '整点数值标识：0无效 1 直接采集 2 计算得来 3 人工修改 4 外系统得到',  " +
            "`sensor_avg_data_value_flag` tinyint(1) DEFAULT NULL COMMENT '平均值标识 整点值标识'," +
            "  `sensor_max_data_value_flag` tinyint(1) DEFAULT NULL COMMENT '最大值标识：同整点值标识',  " +
            "`sensor_min_data_value_flag` tinyint(1) DEFAULT NULL COMMENT '最小值标识', " +
            " `calculate_span_hour` tinyint(1) DEFAULT NULL COMMENT '计算时段', " +
            " `station_code` int(11) DEFAULT NULL COMMENT '传感器所属测站编号'," +
            "  `station_name` varchar(50) COLLATE utf8_bin DEFAULT NULL COMMENT '传感器所属测站名称') " +
            "ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;")
    int buildHourBase(@Param("database") String dateBaseName);


    @Insert("<script>" +
            "INSERT INTO `wisdomhydrologic`.`history_hour_sensor_data` " +
            "(`sensor_code`, `sensor_data_value`, `sensor_type_id`, `sensor_type_name`, `sensor_data_unit`," +
            " `sensor_data_upload_time`,`sensor_avg_data`, `sensor_max_data`, `sensor_max_data_time`, `sensor_min_data`, `sensor_min_data_time`, " +
            "`sensor_data_value_flag`, `sensor_avg_data_value_flag`, `sensor_max_data_value_flag`, `sensor_min_data_value_flag`," +
            "  `station_code`, `station_name`)  VALUES" +
            "<foreach collection=\"hourList\" item=\"item\" separator=\",\">" +
            " (#{item.senId},#{item.v},#{item.sensorTypeId},#{item.sensorTypeName},#{item.sensorDataUnit}," +
            "#{item.time},#{item.avgV},#{item.maxV},#{item.maxT},#{item.minV},#{item.minT}," +
            "#{item.s},#{item.avgS},#{item.maxS},#{item.minS},#{item.stationId},#{item.stationName})" +
            "</foreach></script>")
    int addTestHourData(@Param("hourList") List<DayVo> hourVo);

}
