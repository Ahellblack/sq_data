package com.siti.wisdomhydrologic.analysis.mapper;

import com.siti.wisdomhydrologic.analysis.vo.RealVo;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

/**
 * Created by DC on 2019/7/19.
 * @data ${DATA}-18:03
 */
public interface RealMapper extends Mapper<RealVo> {


    @Insert("<script>INSERT ignore INTO `real` (`sensor_code`,`time`,`real_val`,`modified`,`cycle`,`state`,`ts`) " +
            "VALUES <foreach collection=\"list\" index=\"index\" item=\"vo\" separator=\",\">" +
            "( #{vo.senId},#{vo.Time}" +
            ",#{vo.FACTV},#{vo.IFCH},#{vo.CYCLE},#{vo.STATE},#{vo.TS})" +
            "</foreach> on duplicate key update sensor_code=values(sensor_code)," +
            "time=values(time)</script> ")
    int insertReal(@Param("list") List<RealVo> list);

    @Insert("<script>INSERT ignore INTO `${table}` (`sensor_code`,`time`,`real_val`,`modified`,`cycle`,`state`,`ts`) " +
            "VALUES <foreach collection=\"list\" index=\"index\" item=\"vo\" separator=\",\">" +
            "( #{vo.senId},#{vo.Time}" +
            ",#{vo.FACTV},#{vo.IFCH},#{vo.CYCLE},#{vo.STATE},#{vo.TS})" +
            "</foreach> on duplicate key update sensor_code=values(sensor_code)," +
            "time=values(time)</script> ")
    int insertHistroy(@Param("list")List<RealVo> list,@Param("table") String table);

    @Delete("DELETE from `real` where time < #{oldWeekTime}")
    void deleteOldTime(@Param("oldWeekTime") String oldWeekTime);

    @Update(" CREATE TABLE if not exists `${database}` ( " +
            "  `sensor_code` varchar(10) COLLATE utf8_bin NOT NULL, " +
            "  `time` varchar(100) COLLATE utf8_bin NOT NULL, " +
            "  `real_val` decimal(14,2) DEFAULT NULL, " +
            "  `modified` tinyint(4) DEFAULT NULL COMMENT '0未处理 1已经处理', " +
            "  `cycle` decimal(11,0) DEFAULT NULL, " +
            "  `state` int(14) DEFAULT NULL, " +
            "  `ts` int(14) DEFAULT NULL, " +
            "  PRIMARY KEY (`sensor_code`,`time`), " +
            "  KEY `timeindex` (`time`) " +
            ") ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;")
    void buildTable(@Param("database") String datavase);



}
