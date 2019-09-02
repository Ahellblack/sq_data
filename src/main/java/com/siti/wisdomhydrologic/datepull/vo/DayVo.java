package com.siti.wisdomhydrologic.datepull.vo;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by DC on 2019/7/6.
 *
 * @data ${DATA}-15:57
 */
public class DayVo implements Serializable {

    public DayVo() {

    }

    private int senId;
    private double v;
    /**
     * 添加数据单位
     */
    private int sensorTypeId;
    private String sensorTypeName;
    private String sensorDataUnit;
    private Date time;
    private double avgV;
    private double maxV;
    private String maxT;
    private double minV;
    private String minT;
    private int s;
    private int avgS;
    private int maxS;
    private int minS;
    /**
     * 添加测站的id,name
     */
    private String stationName;
    private int stationId;
    /**
     * 为mq传入是做判断丢包添加的字段
     * maxBatch;currentBatch;status;sumSize;currentSize;
     */
    private int maxBatch;
    private int currentBatch;
    private int status;
    private int sumSize;
    private int currentSize;


    public int getSenId() {
        return senId;
    }

    public void setSenId(int senId) {
        this.senId = senId;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public double getV() {
        return v;
    }

    public void setV(double v) {
        this.v = v;
    }

    public double getAvgV() {
        return avgV;
    }

    public void setAvgV(double avgV) {
        this.avgV = avgV;
    }

    public double getMaxV() {
        return maxV;
    }

    public void setMaxV(double maxV) {
        this.maxV = maxV;
    }

    public String getMaxT() {
        return maxT;
    }

    public void setMaxT(String maxT) {
        this.maxT = maxT;
    }

    public double getMinV() {
        return minV;
    }

    public void setMinV(double minV) {
        this.minV = minV;
    }

    public String getMinT() {
        return minT;
    }

    public void setMinT(String minT) {
        this.minT = minT;
    }

    public int getS() {
        return s;
    }

    public void setS(int s) {
        this.s = s;
    }

    public int getAvgS() {
        return avgS;
    }

    public void setAvgS(int avgS) {
        this.avgS = avgS;
    }

    public int getMaxS() {
        return maxS;
    }

    public void setMaxS(int maxS) {
        this.maxS = maxS;
    }

    public int getMinS() {
        return minS;
    }

    public void setMinS(int minS) {
        this.minS = minS;
    }

    public int getMaxBatch() {
        return maxBatch;
    }

    public void setMaxBatch(int maxBatch) {
        this.maxBatch = maxBatch;
    }

    public int getCurrentBatch() {
        return currentBatch;
    }

    public void setCurrentBatch(int currentBatch) {
        this.currentBatch = currentBatch;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getSumSize() {
        return sumSize;
    }

    public void setSumSize(int sumSize) {
        this.sumSize = sumSize;
    }

    public int getCurrentSize() {
        return currentSize;
    }

    public void setCurrentSize(int currentSize) {
        this.currentSize = currentSize;
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

    public String getSensorDataUnit() {
        return sensorDataUnit;
    }

    public void setSensorDataUnit(String sensorDataUnit) {
        this.sensorDataUnit = sensorDataUnit;
    }

    @Override
    public String toString() {
        return "DayVo{" + "senId=" + senId + ", time='" + time + '\'' + ", v=" + v + ", avgV=" + avgV + ", maxV=" + maxV + ", maxT='" + maxT + '\'' + ", minV=" + minV + ", minT='" + minT + '\'' + ", s=" + s + ", avgS=" + avgS + ", maxS=" + maxS + ", minS=" + minS + ", maxBatch=" + maxBatch + ", currentBatch=" + currentBatch + ", status=" + status + ", sumSize=" + sumSize + ", currentSize=" + currentSize + ", stationName='" + stationName + '\'' + ", stationId=" + stationId + ", sensorTypeId=" + sensorTypeId + ", sensorTypeName='" + sensorTypeName + '\'' + ", sensorDataUnit='" + sensorDataUnit + '\'' + '}';
    }
}
