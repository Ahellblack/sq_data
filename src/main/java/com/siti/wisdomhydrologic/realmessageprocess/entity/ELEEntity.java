package com.siti.wisdomhydrologic.realmessageprocess.entity;

import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Created by DC on 2019/7/19.
 *
 * @data ${DATA}-15:18
 */
@Table(name = "abnormal_electric")
public class ELEEntity {
    @Id
    int id;
    int sensorCode;//模块
    String sensorName;//'站点名称',
    int interruptLimit;//中断次数
    double levelMax;//最高水位
    double levelMin;//最低水位
    double upMax;//最高上涨量
    double belowMin;//最高降低量
    int duration;//准许水位不变的最高时长
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

    public double getLevelMax() {
        return levelMax;
    }

    public void setLevelMax(double levelMax) {
        this.levelMax = levelMax;
    }

    public double getLevelMin() {
        return levelMin;
    }

    public void setLevelMin(double levelMin) {
        this.levelMin = levelMin;
    }

    public double getUpMax() {
        return upMax;
    }

    public void setUpMax(double upMax) {
        this.upMax = upMax;
    }

    public double getBelowMin() {
        return belowMin;
    }

    public void setBelowMin(double belowMin) {
        this.belowMin = belowMin;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

}
