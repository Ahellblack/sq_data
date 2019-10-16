package com.siti.wisdomhydrologic.analysis.entity;

import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

/**
 * Created by DC on 2019/7/19.
 *
 * @data ${DATA}-15:32
 */
@Table(name = "abnormal_rainfall")
public class RainfallEntity {
    @Id
    Integer id;
    Integer sensorCode;//模块
    String sensorName;//站点名称
    Integer interruptLimit;//中断次数
    Double maxDayLevel;//一天最高雨量
    Double minDayLevel;//一天最低雨量
    Double maxHourLevel;//小时最高雨量
    Double minHourLevel;//小时最低雨量
    Double maxFiveLevel;//5分钟最高雨量
    Double minFiveLevel;//5分钟最低雨量
    String nearbySensorCode;//附近3个传感器nearby_sensor_code
    Double nearbyRate;//高于附近平均值的比例
    Date createTime;
    String exceptionValue;

    @Override
    public String toString() {
        return "RainfallEntity{" +
                "id=" + id +
                ", sensorCode=" + sensorCode +
                ", sensorName='" + sensorName + '\'' +
                ", interruptLimit=" + interruptLimit +
                ", maxDayLevel=" + maxDayLevel +
                ", minDayLevel=" + minDayLevel +
                ", maxHourLevel=" + maxHourLevel +
                ", minHourLevel=" + minHourLevel +
                ", maxFiveLevel=" + maxFiveLevel +
                ", minFiveLevel=" + minFiveLevel +
                ", nearbySensorCode='" + nearbySensorCode + '\'' +
                ", nearbyRate=" + nearbyRate +
                ", createTime=" + createTime +
                ", exceptionValue='" + exceptionValue + '\'' +
                '}';
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getSensorCode() {
        return sensorCode;
    }

    public void setSensorCode(Integer sensorCode) {
        this.sensorCode = sensorCode;
    }

    public String getSensorName() {
        return sensorName;
    }

    public void setSensorName(String sensorName) {
        this.sensorName = sensorName;
    }

    public Integer getInterruptLimit() {
        return interruptLimit;
    }

    public void setInterruptLimit(Integer interruptLimit) {
        this.interruptLimit = interruptLimit;
    }

    public Double getMaxDayLevel() {
        return maxDayLevel;
    }

    public void setMaxDayLevel(Double maxDayLevel) {
        this.maxDayLevel = maxDayLevel;
    }

    public Double getMinDayLevel() {
        return minDayLevel;
    }

    public void setMinDayLevel(Double minDayLevel) {
        this.minDayLevel = minDayLevel;
    }

    public Double getMaxHourLevel() {
        return maxHourLevel;
    }

    public void setMaxHourLevel(Double maxHourLevel) {
        this.maxHourLevel = maxHourLevel;
    }

    public Double getMinHourLevel() {
        return minHourLevel;
    }

    public void setMinHourLevel(Double minHourLevel) {
        this.minHourLevel = minHourLevel;
    }

    public Double getMaxFiveLevel() {
        return maxFiveLevel;
    }

    public void setMaxFiveLevel(Double maxFiveLevel) {
        this.maxFiveLevel = maxFiveLevel;
    }

    public Double getMinFiveLevel() {
        return minFiveLevel;
    }

    public void setMinFiveLevel(Double minFiveLevel) {
        this.minFiveLevel = minFiveLevel;
    }

    public String getNearbySensorCode() {
        return nearbySensorCode;
    }

    public void setNearbySensorCode(String nearbySensorCode) {
        this.nearbySensorCode = nearbySensorCode;
    }

    public Double getNearbyRate() {
        return nearbyRate;
    }

    public void setNearbyRate(Double nearbyRate) {
        this.nearbyRate = nearbyRate;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getExceptionValue() {
        return exceptionValue;
    }

    public void setExceptionValue(String exceptionValue) {
        this.exceptionValue = exceptionValue;
    }
}
