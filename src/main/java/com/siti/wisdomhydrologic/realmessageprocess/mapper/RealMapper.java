package com.siti.wisdomhydrologic.realmessageprocess.mapper;

import com.siti.wisdomhydrologic.realmessageprocess.entity.AbnormalDetailEntity;
import com.siti.wisdomhydrologic.realmessageprocess.vo.RealVo;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import tk.mybatis.mapper.common.Mapper;

import java.util.Date;
import java.util.List;

/**
 * Created by DC on 2019/7/19.
 * @data ${DATA}-18:03
 */
public interface RealMapper extends Mapper<RealVo> {
   /* private Integer senId;
    private Date Time;//
    private Integer FACTV;
    private Integer IFCH;
    private Integer CYCLE;
    private Integer STATE;
    private Integer TS;*/

    @Insert("<script>INSERT INTO `real` (`sensor_code`,`time`,`real_val`,`modified`,`cycle`,`state`,`ts`)\n" +
            "VALUES <foreach collection=\"list\" index=\"index\" item=\"vo\" separator=\",\">" +
            "( #{vo.senId},#{vo.Time}" +
            ",#{vo.FACTV},#{vo.IFCH},#{vo.CYCLE},#{vo.STATE},#{vo.TS})" +
            "</foreach> on duplicate key update sensor_code=values(sensor_code)," +
            "time=values(time)</script>\n")
    int insertReal(@Param("list") List<RealVo> list);


    @Insert("<script>INSERT INTO `real` (`sensor_code`,`time`,`real_val`,`modified`,`cycle`,`state`,`ts`)\n" +
            "VALUES ( #{vo.senId},#{vo.Time}" +
            ",#{vo.FACTV},#{vo.IFCH},#{vo.CYCLE},#{vo.STATE},#{vo.TS})</script>\n")
    int insertReald(@Param("vo") RealVo list);
}
