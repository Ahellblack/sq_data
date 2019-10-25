package com.siti.wisdomhydrologic.analysis.mapper;

import com.siti.wisdomhydrologic.analysis.entity.DeviceUploadData;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

public interface DeviceUploadDataMapper extends Mapper<DeviceUploadData> {
    @Select("select * from device_upload_data")
    List<DeviceUploadData> getAll();

    @Insert("INSERT INTO `device_upload_data` (`devid`, `sn`, `temperature`, " +
            "`humidity`, `bat`, `mc`, `report_period`, `upload_time`, `raw_data`) VALUES (" +
            "#{obj.devid}, " +
            "#{obj.sn}, " +
            "#{obj.temperature}, " +
            "#{obj.humidity}, " +
            "#{obj.bat}," +
            "#{obj.mc}, " +
            "#{obj.report_period}, "+
            "#{obj.upload_time}," +
            "#{obj.raw_data});")
    int insert(@Param("obj")DeviceUploadData deviceUploadData);
}
