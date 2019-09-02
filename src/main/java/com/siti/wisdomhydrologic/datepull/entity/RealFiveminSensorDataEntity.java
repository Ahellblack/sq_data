package com.siti.wisdomhydrologic.datepull.entity;

import javax.persistence.Id;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by dc on 2019/6/9 0009.
 */
@Table(name = "real_5min_sensor_data")
public class RealFiveminSensorDataEntity {

    @Id
    private Integer id;
    //传感器编号
    private String sensor_code;
    //传感器数值采集时间
    private Date sensor_data_time;
    //数据采集值
    private BigDecimal sensor_data_value0;

    private BigDecimal sensor_data_value1;

    private BigDecimal sensor_data_value2;

    private BigDecimal sensor_data_value3;

    private BigDecimal sensor_data_value4;

    private BigDecimal sensor_data_value5;

    private BigDecimal sensor_data_value6;

    private BigDecimal sensor_data_value7;

    private BigDecimal sensor_data_value8;

    private BigDecimal sensor_data_value9;

    private BigDecimal sensor_data_value10;

    private BigDecimal sensor_data_value11;

    private BigDecimal sensor_data_value12;
    //数据值状态
    private int sensor_data_value_status0;

    private int sensor_data_value_status1;

    private int sensor_data_value_status2;

    private int sensor_data_value_status3;

    private int sensor_data_value_status4;

    private int sensor_data_value_status5;

    private int sensor_data_value_status6;

    private int sensor_data_value_status7;

    private int sensor_data_value_status8;

    private int sensor_data_value_status9;

    private int sensor_data_value_status10;

    private int sensor_data_value_status11;
    //传感器类型ID
    private int sensor_type_id;
    //传感器类型名称
    private String sensor_type_name;
    //传感器所属测站编号
    private String station_code;
    //传感器所属测站名称
    private String station_name;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getSensor_code() {
        return sensor_code;
    }

    public void setSensor_code(String sensor_code) {
        this.sensor_code = sensor_code;
    }

    public Date getSensor_data_time() {
        return sensor_data_time;
    }

    public void setSensor_data_time(Date sensor_data_time) {
        this.sensor_data_time = sensor_data_time;
    }

    public BigDecimal getSensor_data_value0() {
        return sensor_data_value0;
    }

    public void setSensor_data_value0(BigDecimal sensor_data_value0) {
        this.sensor_data_value0 = sensor_data_value0;
    }

    public BigDecimal getSensor_data_value1() {
        return sensor_data_value1;
    }

    public void setSensor_data_value1(BigDecimal sensor_data_value1) {
        this.sensor_data_value1 = sensor_data_value1;
    }

    public BigDecimal getSensor_data_value2() {
        return sensor_data_value2;
    }

    public void setSensor_data_value2(BigDecimal sensor_data_value2) {
        this.sensor_data_value2 = sensor_data_value2;
    }

    public BigDecimal getSensor_data_value3() {
        return sensor_data_value3;
    }

    public void setSensor_data_value3(BigDecimal sensor_data_value3) {
        this.sensor_data_value3 = sensor_data_value3;
    }

    public BigDecimal getSensor_data_value4() {
        return sensor_data_value4;
    }

    public void setSensor_data_value4(BigDecimal sensor_data_value4) {
        this.sensor_data_value4 = sensor_data_value4;
    }

    public BigDecimal getSensor_data_value5() {
        return sensor_data_value5;
    }

    public void setSensor_data_value5(BigDecimal sensor_data_value5) {
        this.sensor_data_value5 = sensor_data_value5;
    }

    public BigDecimal getSensor_data_value6() {
        return sensor_data_value6;
    }

    public void setSensor_data_value6(BigDecimal sensor_data_value6) {
        this.sensor_data_value6 = sensor_data_value6;
    }

    public BigDecimal getSensor_data_value7() {
        return sensor_data_value7;
    }

    public void setSensor_data_value7(BigDecimal sensor_data_value7) {
        this.sensor_data_value7 = sensor_data_value7;
    }

    public BigDecimal getSensor_data_value8() {
        return sensor_data_value8;
    }

    public void setSensor_data_value8(BigDecimal sensor_data_value8) {
        this.sensor_data_value8 = sensor_data_value8;
    }

    public BigDecimal getSensor_data_value9() {
        return sensor_data_value9;
    }

    public void setSensor_data_value9(BigDecimal sensor_data_value9) {
        this.sensor_data_value9 = sensor_data_value9;
    }

    public BigDecimal getSensor_data_value10() {
        return sensor_data_value10;
    }

    public void setSensor_data_value10(BigDecimal sensor_data_value10) {
        this.sensor_data_value10 = sensor_data_value10;
    }

    public BigDecimal getSensor_data_value11() {
        return sensor_data_value11;
    }

    public void setSensor_data_value11(BigDecimal sensor_data_value11) {
        this.sensor_data_value11 = sensor_data_value11;
    }

    public BigDecimal getSensor_data_value12() {
        return sensor_data_value12;
    }

    public void setSensor_data_value12(BigDecimal sensor_data_value12) {
        this.sensor_data_value12 = sensor_data_value12;
    }

    public int getSensor_data_value_status0() {
        return sensor_data_value_status0;
    }

    public void setSensor_data_value_status0(int sensor_data_value_status0) {
        this.sensor_data_value_status0 = sensor_data_value_status0;
    }

    public int getSensor_data_value_status1() {
        return sensor_data_value_status1;
    }

    public void setSensor_data_value_status1(int sensor_data_value_status1) {
        this.sensor_data_value_status1 = sensor_data_value_status1;
    }

    public int getSensor_data_value_status2() {
        return sensor_data_value_status2;
    }

    public void setSensor_data_value_status2(int sensor_data_value_status2) {
        this.sensor_data_value_status2 = sensor_data_value_status2;
    }

    public int getSensor_data_value_status3() {
        return sensor_data_value_status3;
    }

    public void setSensor_data_value_status3(int sensor_data_value_status3) {
        this.sensor_data_value_status3 = sensor_data_value_status3;
    }

    public int getSensor_data_value_status4() {
        return sensor_data_value_status4;
    }

    public void setSensor_data_value_status4(int sensor_data_value_status4) {
        this.sensor_data_value_status4 = sensor_data_value_status4;
    }

    public int getSensor_data_value_status5() {
        return sensor_data_value_status5;
    }

    public void setSensor_data_value_status5(int sensor_data_value_status5) {
        this.sensor_data_value_status5 = sensor_data_value_status5;
    }

    public int getSensor_data_value_status6() {
        return sensor_data_value_status6;
    }

    public void setSensor_data_value_status6(int sensor_data_value_status6) {
        this.sensor_data_value_status6 = sensor_data_value_status6;
    }

    public int getSensor_data_value_status7() {
        return sensor_data_value_status7;
    }

    public void setSensor_data_value_status7(int sensor_data_value_status7) {
        this.sensor_data_value_status7 = sensor_data_value_status7;
    }

    public int getSensor_data_value_status8() {
        return sensor_data_value_status8;
    }

    public void setSensor_data_value_status8(int sensor_data_value_status8) {
        this.sensor_data_value_status8 = sensor_data_value_status8;
    }

    public int getSensor_data_value_status9() {
        return sensor_data_value_status9;
    }

    public void setSensor_data_value_status9(int sensor_data_value_status9) {
        this.sensor_data_value_status9 = sensor_data_value_status9;
    }

    public int getSensor_data_value_status10() {
        return sensor_data_value_status10;
    }

    public void setSensor_data_value_status10(int sensor_data_value_status10) {
        this.sensor_data_value_status10 = sensor_data_value_status10;
    }

    public int getSensor_data_value_status11() {
        return sensor_data_value_status11;
    }

    public void setSensor_data_value_status11(int sensor_data_value_status11) {
        this.sensor_data_value_status11 = sensor_data_value_status11;
    }

    public int getSensor_type_id() {
        return sensor_type_id;
    }

    public void setSensor_type_id(int sensor_type_id) {
        this.sensor_type_id = sensor_type_id;
    }

    public String getSensor_type_name() {
        return sensor_type_name;
    }

    public void setSensor_type_name(String sensor_type_name) {
        this.sensor_type_name = sensor_type_name;
    }

    public String getStation_code() {
        return station_code;
    }

    public void setStation_code(String station_code) {
        this.station_code = station_code;
    }

    public String getStation_name() {
        return station_name;
    }

    public void setStation_name(String station_name) {
        this.station_name = station_name;
    }
}
