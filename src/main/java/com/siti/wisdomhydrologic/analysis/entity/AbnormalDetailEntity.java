package com.siti.wisdomhydrologic.analysis.entity;

import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

/**
 * Created by DC on 2019/7/19.
 *
 * @data ${DATA}-18:04
 */
@Table(name = "abnormal_detail")
public class AbnormalDetailEntity {
    @Id
    int id;
    String date;
    int sensorCode;
    double errorValue;
    String dataError;
    String description;
    Date createTime;

    private AbnormalDetailEntity(builer builer){
        this. date=builer.date;
        this. sensorCode=builer.sensorCode;
        this. errorValue=builer.errorValue;
        this. dataError=builer.dataError;
        this. description=builer.description;
        this. createTime=builer.createTime;
        //java 构建器
    }

    public static class builer{
        String date;
        int sensorCode;
        double errorValue;
        String dataError;
        String description;
        Date createTime;
        public AbnormalDetailEntity build(){
            return new AbnormalDetailEntity(this);
        }

        public builer date(String date) {
            this.date = date;
            return this;
        }

        public builer dataError(String dataError) {
            this.dataError = dataError;
            return this;
        }

        public builer errorValue(double errorValue) {
            this.errorValue = errorValue;
            return this;
        }

        public builer description(String description) {
            this.description = description;
            return this;
        }

        public builer createTime(Date createTime) {
            this.createTime = createTime;
            return this;
        }

        public builer sensorCode(Integer sensorCode) {
            this.sensorCode = sensorCode;
            return this;
        }
    }

    public String getDataError() {
        return dataError;
    }

    public void setDataError(String dataError) {
        this.dataError = dataError;
    }


    public double getErrorValue() {
        return errorValue;
    }

    public void setErrorValue(double errorValue) {
        this.errorValue = errorValue;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getSensorCode() {
        return sensorCode;
    }

    public void setSensorCode(int sensorCode) {
        this.sensorCode = sensorCode;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
