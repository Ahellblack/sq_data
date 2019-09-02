package com.siti.wisdomhydrologic.realmessageprocess.entity;

import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

/**
 * Created by DC on 2019/7/24.
 *
 * @data ${DATA}-15:11
 */
@Table(name="real")
public class Real {
    @Id
    private int sensorCode;//sensor_code
    @Id
    private Date time;

    private Double realVal;

    private Integer modified;

    private Integer cycle;

    private Integer state;

    private Integer ts;

    public int getSensorCode() {
        return sensorCode;
    }

    public void setSensorCode(int sensorCode) {
        this.sensorCode = sensorCode;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public Double getRealVal() {
        return realVal;
    }

    public void setRealVal(Double realVal) {
        this.realVal = realVal;
    }

    public Integer getModified() {
        return modified;
    }

    public void setModified(Integer modified) {
        this.modified = modified;
    }

    public Integer getCycle() {
        return cycle;
    }

    public void setCycle(Integer cycle) {
        this.cycle = cycle;
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    public Integer getTs() {
        return ts;
    }

    public void setTs(Integer ts) {
        this.ts = ts;
    }
}
