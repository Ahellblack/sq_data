package com.siti.wisdomhydrologic.analysis.entity;

import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

/**
 * Created by DC on 2019/7/19.
 *
 * @data ${DATA}-15:18
 */
@Table(name = "abnormal_water_level")
public class WaterLevelEntity {
    @Id
    Integer id;
    Integer sensorCode;//模块
    String sensorName;//'站点名称',
    Integer interruptLimit;//中断次数
    Double levelMax;//最高水位
    Double levelMin;//最低水位
    Double upMax;//最高上涨量
    Double downMax;//最高降低量
    Integer duration;//准许水位不变的最高时长
    String exceptionValue;

    @Override
    public String toString() {
        return "WaterLevelEntity{" +
                "id=" + id +
                ", sensorCode=" + sensorCode +
                ", sensorName='" + sensorName + '\'' +
                ", interruptLimit=" + interruptLimit +
                ", levelMax=" + levelMax +
                ", levelMin=" + levelMin +
                ", upMax=" + upMax +
                ", downMax=" + downMax +
                ", duration=" + duration +
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

    public Double getLevelMax() {
        return levelMax;
    }

    public void setLevelMax(Double levelMax) {
        this.levelMax = levelMax;
    }

    public Double getLevelMin() {
        return levelMin;
    }

    public void setLevelMin(Double levelMin) {
        this.levelMin = levelMin;
    }

    public Double getUpMax() {
        return upMax;
    }

    public void setUpMax(Double upMax) {
        this.upMax = upMax;
    }

    public Double getDownMax() {
        return downMax;
    }

    public void setDownMax(Double downMax) {
        this.downMax = downMax;
    }

    public Integer getDuration() {
        return duration;
    }

    public void setDuration(Integer duration) {
        this.duration = duration;
    }

    public String getExceptionValue() {
        return exceptionValue;
    }

    public void setExceptionValue(String exceptionValue) {
        this.exceptionValue = exceptionValue;
    }
}
