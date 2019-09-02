package com.siti.wisdomhydrologic.datepull.vo;

/**
 * Created by dell on 2019/7/19.
 */
public class StationVo {

    private int senId;
    private String sensorName;
    private int telemeteringId;
    private int stationCode;
    private String stationName;
    private int stationId;
    private String riverwayName;
/**
 * 传感器类型表字段
 * */
    private int sensorTypeId;
    private String sensorTypeName;

    public int getSenId() {
        return senId;
    }

    public void setSenId(int senId) {
        this.senId = senId;
    }

    public String getSensorName() {
        return sensorName;
    }

    public void setSensorName(String sensorName) {
        this.sensorName = sensorName;
    }

    public int getSensorTypeId() {
        return sensorTypeId;
    }

    public void setSensorTypeId(int sensorTypeId) {
        this.sensorTypeId = sensorTypeId;
    }

    public String getSensorTypeName() {
        return sensorTypeName;
    }

    public void setSensorTypeName(String sensorTypeName) {
        this.sensorTypeName = sensorTypeName;
    }

    public int getTelemeteringId() {
        return telemeteringId;
    }

    public void setTelemeteringId(int telemeteringId) {
        this.telemeteringId = telemeteringId;
    }

    public int getStationCode() {
        return stationCode;
    }

    public void setStationCode(int stationCode) {
        this.stationCode = stationCode;
    }

    public String getStationName() {
        return stationName;
    }

    public void setStationName(String stationName) {
        this.stationName = stationName;
    }

    public int getStationId() {
        return stationId;
    }

    public void setStationId(int stationId) {
        this.stationId = stationId;
    }

    public String getRiverwayName() {
        return riverwayName;
    }

    public void setRiverwayName(String riverwayName) {
        this.riverwayName = riverwayName;
    }

    @Override
    public String toString() {
        return "StationVo{" + "senId=" + senId + ", sensorName='" + sensorName + '\'' + ", sensorTypeId=" + sensorTypeId + ", sensorTypeName='" + sensorTypeName + '\'' + ", telemeteringId=" + telemeteringId + ", stationCode=" + stationCode + ", stationName='" + stationName + '\'' + ", stationId=" + stationId + ", riverwayName='" + riverwayName + '\'' + '}';
    }
}
