package com.siti.wisdomhydrologic.analysis.mapper;

import com.siti.wisdomhydrologic.analysis.entity.*;
import com.siti.wisdomhydrologic.analysis.vo.RealVo;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

/**
 * Created by DC on 2019/7/19.
 *
 * @data ${DATA}-18:03
 */
public interface AbnormalDetailMapper extends Mapper<AbnormalDetailEntity> {

    @Insert("<script>" +
            "replace into abnormal_detail(date,sensor_code,day_above,day_below,hour_above,hour_below," +
            "five_below,five_above,more_near,less_near,floating_up,floating_down,keeptime,continue_interrupt)\n" +
            "values <foreach collection=\"list\" index=\"index\" item=\"AbnormalDetailEntity\" separator=\",\">" +
            "( #{AbnormalDetailEntity.date},#{AbnormalDetailEntity.sensorCode},#{AbnormalDetailEntity.dayAbove}," +
            "#{AbnormalDetailEntity.dayBelow},#{AbnormalDetailEntity.hourAbove}," +
            "#{AbnormalDetailEntity.hourBelow},#{AbnormalDetailEntity.fiveBelow},#{AbnormalDetailEntity.fiveAbove}," +
            "#{AbnormalDetailEntity.moreNear},#{AbnormalDetailEntity.lessNear},#{AbnormalDetailEntity.floatingUp}," +
            "#{AbnormalDetailEntity.floatingDown},#{AbnormalDetailEntity.keepTime},#{AbnormalDetailEntity.continueInterrupt})" +
            "</foreach></script>\n")
    int insertAndUpdate(@Param("list") List<AbnormalDetailEntity> list);

    @Select("<script>select sensor_model_type from config_sensor_database where sensor_code=#{sensor_code}  </script>")
    String getSensorModelType(@Param("sensor_code") String sensor_code);

    @Insert("<script>" +
            "insert ignore into abnormal_detail(date,sensor_code,data_error,error_period,error_value,equipment_error)\n" +
            "values <foreach collection=\"list\" index=\"index\" item=\"AbnormalDetailEntity\" separator=\",\">" +
            "( #{AbnormalDetailEntity.date},#{AbnormalDetailEntity.sensorCode},#{AbnormalDetailEntity.dataError},#{AbnormalDetailEntity.errorPeriod}" +
            ",#{AbnormalDetailEntity.errorValue},#{AbnormalDetailEntity.equipmentError})" +
            "</foreach></script>\n")
    int insertFinal(@Param("list") List<AbnormalDetailEntity> list);

    @Insert("<script>" +
            "replace into abnormal_detail(date,sensor_code,day_above,day_below,hour_above,hour_below," +
            "five_below,five_above,more_near,less_near,floating_up,floating_down,keeptime,continue_interrupt,error_period,equipment_error," +
            "error_value)\n" +
            "values <foreach collection=\"list\" index=\"index\" item=\"AbnormalDetailEntity\" separator=\",\">" +
            "( #{AbnormalDetailEntity.date},#{AbnormalDetailEntity.sensorCode},#{AbnormalDetailEntity.dayAbove}," +
            "#{AbnormalDetailEntity.dayBelow},#{AbnormalDetailEntity.hourAbove}," +
            "#{AbnormalDetailEntity.hourBelow},#{AbnormalDetailEntity.fiveBelow},#{AbnormalDetailEntity.fiveAbove}," +
            "#{AbnormalDetailEntity.moreNear},#{AbnormalDetailEntity.lessNear},#{AbnormalDetailEntity.floatingUp}," +
            "#{AbnormalDetailEntity.floatingDown},#{AbnormalDetailEntity.keepTime},#{AbnormalDetailEntity.continueInterrupt}" +
            ",#{AbnormalDetailEntity.errorPeriod},#{AbnormalDetailEntity.equipmentError},#{AbnormalDetailEntity.errorValue})" +
            "</foreach></script>\n")
    int insertFinalUpdate(@Param("list") List<AbnormalDetailEntity> list);

    @Select("<script>select * from real  where sensor_code = #{sensorcode} and time=#{time}</script>\n")
    Real selectEle(@Param("sensorcode") int sensorcode, @Param("time") String time);

    @Select("select * from abnormal_water_level")
    List<WaterLevelEntity> fetchAllW();

    @Select("select * from abnormal_flow_velocity")
    List<FVYEntity> fetchAllFVY();

    @Select("select * from abnormal_flow_velocity")
    List<FVEntity> fetchAllFV();

    @Select("select * from abnormal_tide_level")
    List<TideLevelEntity> fetchAllT();

    @Select("select * from abnormal_air_temperature")
    List<ATEntity> fetchAllAT();

    @Select("select * from abnormal_air_pressure")
    List<APEntity> fetchAllAP();

    @Select("select * from abnormal_wind_speed")
    List<WSEntity> fetchWS();

    @Select("select * from abnormal_wind_direction")
    List<WDEntity> fetchWD();

    @Select("select * from abnormal_rainfall")
    List<RainfallEntity> fetchAllR();

    @Select("select * from abnormal_electric")
    List<ELEEntity> fetchAllELE();

    @Select("<script>SELECT * FROM `real` where time = #{time} </script>\n")
    List<Real> selectBefore5Ele( @Param("time") String time);

    @Select("<script>select * from config_regression_function</script>\n")
    List<RegressionEntity> getRegression();


    @Select("<script>select * from real  where sensor_code = #{sensorcode} and time=DATE_ADD(#{time},INTERVAL -5 MINUTE)</script>\n")
    RealVo select5Ele(@Param("sensorcode") String sensorcode, @Param("time") String time);


    @Select("<script>select count(1) from real where sensor_code=#{sensorCode} and time=#{time}</script>")
    int selectRealExist2(@Param("sensorCode") Integer sensorCode, @Param("time") String time);

    @Select("<script>SELECT count(1) FROM `real` where sensor_code=#{sensorCode} and time=#{time}</script>")
    int selectRealExist(@Param("sensorCode") String sensorCode, @Param("time") String time);

    @Select("<script>SELECT sensor_code,time,real_val FROM `real` where  time>#{time}</script>")
    List<Real> selectBeforeFiveReal(@Param("time") String time);

    @Insert("<script>" +
            "insert into abnormal_detail(date,sensor_code,five_below,five_above,more_near,less_near,error_value)\n" +
            "values <foreach collection=\"list\" index=\"index\" item=\"AbnormalDetailEntity\" separator=\",\">" +
            "( #{AbnormalDetailEntity.date},#{AbnormalDetailEntity.sensorCode},#{AbnormalDetailEntity.fiveBelow},#{AbnormalDetailEntity.fiveAbove}," +
            "#{AbnormalDetailEntity.moreNear},#{AbnormalDetailEntity.lessNear},#{AbnormalDetailEntity.errorValue})" +
            "</foreach> on duplicate key update five_below=values(five_below)," +
            "five_above=values(five_above),more_near=values(more_near),less_near" +
            "=values(less_near)</script>\n")
    int insertRain(@Param("list") List<AbnormalDetailEntity> list);

    @Insert("<script>" +
            "insert into abnormal_detail(date,sensor_code,hour_above,hour_below,continue_interrupt,error_value)\n" +
            "values <foreach collection=\"list\" index=\"index\" item=\"AbnormalDetailEntity\" separator=\",\">" +
            "( #{AbnormalDetailEntity.date},#{AbnormalDetailEntity.sensorCode},#{AbnormalDetailEntity.hourAbove}," +
            "#{AbnormalDetailEntity.hourBelow},#{AbnormalDetailEntity.continueInterrupt},#{AbnormalDetailEntity.errorValue})" +
            "</foreach> on duplicate key update hour_above=values(hour_above),hour_below=values(hour_below)" +
            ",continue_interrupt=values(continue_interrupt)</script>\n")
    int insertTSDBRain(@Param("list") List<AbnormalDetailEntity> list);

    @Insert("<script>" +
            "insert into abnormal_detail(date,sensor_code," +
            "five_below,five_above,floating_up,floating_down,error_value)\n" +
            "values <foreach collection=\"list\" index=\"index\" item=\"AbnormalDetailEntity\" separator=\",\">" +
            "( #{AbnormalDetailEntity.date},#{AbnormalDetailEntity.sensorCode},#{AbnormalDetailEntity.fiveBelow}" +
            ",#{AbnormalDetailEntity.fiveAbove},#{AbnormalDetailEntity.floatingUp},#{AbnormalDetailEntity.floatingDown}" +
            ",#{AbnormalDetailEntity.errorValue})" +
            "</foreach> on duplicate key update five_below=values(five_below)," +
            "five_above=values(five_above),floating_up=values(floating_up)," +
            "floating_down=values(floating_down)</script>\n")
    int insertTide(@Param("list") List<AbnormalDetailEntity> list);

    @Insert("<script>" +
            "insert into abnormal_detail(date,sensor_code,five_below,five_above,error_value)\n" +
            "values <foreach collection=\"list\" index=\"index\" item=\"AbnormalDetailEntity\" separator=\",\">" +
            "( #{AbnormalDetailEntity.date},#{AbnormalDetailEntity.sensorCode},#{AbnormalDetailEntity.fiveBelow}" +
            ",#{AbnormalDetailEntity.fiveAbove},#{AbnormalDetailEntity.errorValue})" +
            "</foreach> on duplicate key update five_below=values(five_below)," +
            "five_above=values(five_above)</script>\n")
    int insertWater(@Param("list") List<AbnormalDetailEntity> list);

    @Insert("<script>" +
            "insert into abnormal_detail(date,sensor_code,day_above,day_below,error_value)\n" +
            "values <foreach collection=\"list\" index=\"index\" item=\"AbnormalDetailEntity\" separator=\",\">" +
            "( #{AbnormalDetailEntity.date},#{AbnormalDetailEntity.sensorCode},#{AbnormalDetailEntity.dayAbove}," +
            "#{AbnormalDetailEntity.dayBelow},#{AbnormalDetailEntity.errorValue})" +
            "</foreach> on duplicate key update day_above=values(day_above),day_below=values(day_below)</script>\n")
    int insertDayRain(@Param("list") List<AbnormalDetailEntity> list);

    @Insert("<script>" +
            "insert into abnormal_detail(date,sensor_code,floating_up,floating_down,keeptime,continue_interrupt,error_value)\n" +
            "values <foreach collection=\"list\" index=\"index\" item=\"AbnormalDetailEntity\" separator=\",\">" +
            "( #{AbnormalDetailEntity.date},#{AbnormalDetailEntity.sensorCode},#{AbnormalDetailEntity.floatingUp}," +
            "#{AbnormalDetailEntity.floatingDown},#{AbnormalDetailEntity.keepTime},#{AbnormalDetailEntity.continueInterrupt}" +
            ",#{AbnormalDetailEntity.errorValue})" +
            "</foreach> on duplicate key update floating_up=values(floating_up),floating_down=values(floating_down)" +
            ",keeptime=values(keeptime),floating_down=values(floating_down)</script>\n")
    int insertTSDBTide(@Param("list") List<AbnormalDetailEntity> list);

    @Insert("<script>" +
            "insert into abnormal_detail(date,sensor_code,floating_up,floating_down,keeptime,continue_interrupt,error_value)\n" +
            "values <foreach collection=\"list\" index=\"index\" item=\"AbnormalDetailEntity\" separator=\",\">" +
            "( #{AbnormalDetailEntity.date},#{AbnormalDetailEntity.sensorCode},#{AbnormalDetailEntity.floatingUp}," +
            "#{AbnormalDetailEntity.floatingDown},#{AbnormalDetailEntity.keepTime}," +
            "#{AbnormalDetailEntity.continueInterrupt},#{AbnormalDetailEntity.errorValue})" +
            "</foreach> on duplicate key update floating_up=values(floating_up),floating_down=values(floating_down)" +
            ",keeptime=values(keeptime),continue_interrupt=values(continue_interrupt)</script>\n")
    int insertTSDVBWater(@Param("list") List<AbnormalDetailEntity> list);


}
