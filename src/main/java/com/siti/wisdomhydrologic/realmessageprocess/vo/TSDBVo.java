package com.siti.wisdomhydrologic.realmessageprocess.vo;

import java.util.Date;

/**
 * Created by dell on 2019/7/16.
 */
public class TSDBVo {

    Integer SENID;
    Date Time;
    double V0;
    double V1;
    double V2;
    double V3;
    double V4;
    double V5;
    double V6;
    double V7;
    double V8;
    double V9;
    double V10;
    double V11;
    double S0;
    double S1;
    double S2;
    double S3;
    double S4;
    double S5;
    double S6;
    double S7;
    double S8;
    double S9;
    double S10;
    double S11;
    /**
     * 为mq传入是做判断丢包添加的字段
     * maxBatch;currentBatch;status;sumSize;currentSize;
     * */
    private int maxBatch;
    private int currentBatch;
    private int status;
    private int sumSize;
    private int currentSize;

    /**
     * 添加测站的id,name
     * */
    private String stationName;
    private int stationId;
    /**
     * 添加数据单位
     * */
    private int sensorTypeId;
    private String sensorTypeName;
    private String sensorDataUnit;

    public Integer getSENID() {
        return SENID;
    }

    public void setSENID(Integer SENID) {
        this.SENID = SENID;
    }

    public Date getTime() {
        return Time;
    }

    public void setTime(Date time) {
        Time = time;
    }

    public double getV0() {
        return V0;
    }

    public void setV0(double v0) {
        V0 = v0;
    }

    public double getV1() {
        return V1;
    }

    public void setV1(double v1) {
        V1 = v1;
    }

    public double getV2() {
        return V2;
    }

    public void setV2(double v2) {
        V2 = v2;
    }

    public double getV3() {
        return V3;
    }

    public void setV3(double v3) {
        V3 = v3;
    }

    public double getV4() {
        return V4;
    }

    public void setV4(double v4) {
        V4 = v4;
    }

    public double getV5() {
        return V5;
    }

    public void setV5(double v5) {
        V5 = v5;
    }

    public double getV6() {
        return V6;
    }

    public void setV6(double v6) {
        V6 = v6;
    }

    public double getV7() {
        return V7;
    }

    public void setV7(double v7) {
        V7 = v7;
    }

    public double getV8() {
        return V8;
    }

    public void setV8(double v8) {
        V8 = v8;
    }

    public double getV9() {
        return V9;
    }

    public void setV9(double v9) {
        V9 = v9;
    }

    public double getV10() {
        return V10;
    }

    public void setV10(double v10) {
        V10 = v10;
    }

    public double getV11() {
        return V11;
    }

    public void setV11(double v11) {
        V11 = v11;
    }

    public double getS0() {
        return S0;
    }

    public void setS0(double s0) {
        S0 = s0;
    }

    public double getS1() {
        return S1;
    }

    public void setS1(double s1) {
        S1 = s1;
    }

    public double getS2() {
        return S2;
    }

    public void setS2(double s2) {
        S2 = s2;
    }

    public double getS3() {
        return S3;
    }

    public void setS3(double s3) {
        S3 = s3;
    }

    public double getS4() {
        return S4;
    }

    public void setS4(double s4) {
        S4 = s4;
    }

    public double getS5() {
        return S5;
    }

    public void setS5(double s5) {
        S5 = s5;
    }

    public double getS6() {
        return S6;
    }

    public void setS6(double s6) {
        S6 = s6;
    }

    public double getS7() {
        return S7;
    }

    public void setS7(double s7) {
        S7 = s7;
    }

    public double getS8() {
        return S8;
    }

    public void setS8(double s8) {
        S8 = s8;
    }

    public double getS9() {
        return S9;
    }

    public void setS9(double s9) {
        S9 = s9;
    }

    public double getS10() {
        return S10;
    }

    public void setS10(double s10) {
        S10 = s10;
    }

    public double getS11() {
        return S11;
    }

    public void setS11(double s11) {
        S11 = s11;
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
}
