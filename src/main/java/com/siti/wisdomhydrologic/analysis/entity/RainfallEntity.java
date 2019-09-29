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
    int id;
    int sensorCode;//模块
    String sensorName;//站点名称
    int interruptLimit;//中断次数
    double maxDayLevel;//一天最高雨量
    double minDayLevel;//一天最低雨量
    double maxHourLevel;//小时最高雨量
    double minHourLevel;//小时最低雨量
    double maxFiveLevel;//5分钟最高雨量
    double minFiveLevel;//5分钟最低雨量
    String nearbySensorCode;//附近3个传感器nearby_sensor_code
    double nearbyRate;//高于附近平均值的比例
    Date createTime;
    String exceptionValue;

    public String getExceptionValue() {
        return exceptionValue;
    }

    public void setExceptionValue(String exceptionValue) {
        this.exceptionValue = exceptionValue;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getSensorCode() {
        return sensorCode;
    }

    public void setSensorCode(int sensorCode) {
        this.sensorCode = sensorCode;
    }

    public String getSensorName() {
        return sensorName;
    }

    public void setSensorName(String sensorName) {
        this.sensorName = sensorName;
    }

    public int getInterruptLimit() {
        return interruptLimit;
    }

    public void setInterruptLimit(int interruptLimit) {
        this.interruptLimit = interruptLimit;
    }

    public double getMaxDayLevel() {
        return maxDayLevel;
    }

    public void setMaxDayLevel(double maxDayLevel) {
        this.maxDayLevel = maxDayLevel;
    }

    public double getMinDayLevel() {
        return minDayLevel;
    }

    public void setMinDayLevel(double minDayLevel) {
        this.minDayLevel = minDayLevel;
    }

    public double getMaxHourLevel() {
        return maxHourLevel;
    }

    public void setMaxHourLevel(double maxHourLevel) {
        this.maxHourLevel = maxHourLevel;
    }

    public double getMinHourLevel() {
        return minHourLevel;
    }

    public void setMinHourLevel(double minHourLevel) {
        this.minHourLevel = minHourLevel;
    }

    public double getMaxFiveLevel() {
        return maxFiveLevel;
    }

    public void setMaxFiveLevel(double maxFiveLevel) {
        this.maxFiveLevel = maxFiveLevel;
    }

    public double getMinFiveLevel() {
        return minFiveLevel;
    }

    public void setMinFiveLevel(double minFiveLevel) {
        this.minFiveLevel = minFiveLevel;
    }

    public String getNearbySensorCode() {
        return nearbySensorCode;
    }

    public void setNearbySensorCode(String nearbySensorCode) {
        this.nearbySensorCode = nearbySensorCode;
    }

    public double getNearbyRate() {
        return nearbyRate;
    }

    public void setNearbyRate(double nearbyRate) {
        this.nearbyRate = nearbyRate;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}
