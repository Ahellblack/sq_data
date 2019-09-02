package com.siti.wisdomhydrologic.datepull.entity;

import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;

/**
 * Created by DC on 2019/7/6.
 *
 * @data ${DATA}-15:57
 */
@Table(name = "daydb")
public class DayEntity implements Serializable {

    public DayEntity(){

    }
    @Id
    private int senId;
    private Date time;
    private String v;
    private String avgV;
    private String maxV;
    private String maxT;
    private String minV;
    private String minT;
    private String s;
    private String avgS;
    private String maxS;
    private String minS;

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

    public String getV() {
        return v;
    }

    public void setV(String v) {
        this.v = v;
    }

    public String getAvgV() {
        return avgV;
    }

    public void setAvgV(String avgV) {
        this.avgV = avgV;
    }

    public String getMaxV() {
        return maxV;
    }

    public void setMaxV(String maxV) {
        this.maxV = maxV;
    }


    public String getMaxT() {
        return maxT;
    }

    public void setMaxT(String maxT) {
        this.maxT = maxT;
    }

    public String getMinV() {
        return minV;
    }

    public void setMinV(String minV) {
        this.minV = minV;
    }

    public String getMinT() {
        return minT;
    }

    public void setMinT(String minT) {
        this.minT = minT;
    }

    public String getS() {
        return s;
    }

    public void setS(String s) {
        this.s = s;
    }

    public String getAvgS() {
        return avgS;
    }

    public void setAvgS(String avgS) {
        this.avgS = avgS;
    }

    public String getMaxS() {
        return maxS;
    }

    public void setMaxS(String maxS) {
        this.maxS = maxS;
    }

    public String getMinS() {
        return minS;
    }

    public void setMinS(String minS) {
        this.minS = minS;
    }

    @Override
    public String toString() {
        return "DayEntity{" +
                "senId=" + senId +
                ", time=" + time +
                ", v='" + v + '\'' +
                ", avgV='" + avgV + '\'' +
                ", maxV='" + maxV + '\'' +
                ", maxT='" + maxT + '\'' +
                ", minV='" + minV + '\'' +
                ", minT='" + minT + '\'' +
                ", s='" + s + '\'' +
                ", avgS='" + avgS + '\'' +
                ", maxS='" + maxS + '\'' +
                ", minS='" + minS + '\'' +
                '}';
    }

}
