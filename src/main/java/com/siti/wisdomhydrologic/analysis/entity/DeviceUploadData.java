package com.siti.wisdomhydrologic.analysis.entity;


public class DeviceUploadData {
    private String devid;  // 设备编号, eNET 创建的的唯一识别号
    private int sn; // 数据包发送的序列号
    private float temperature;  // 空气温度，单位℃；浮点数字格式，2 位小数
    private float humidity;  // 空气湿度，单位 %RH；浮点数字 格式，2 位小数
    private short bat;  // 终端当前电量百分比，整数
    private short mc;  // 门磁状态
    private int report_period;  // 上传周期
    private String upload_time; // 数据上传时间
    private String raw_data;  // 原始Json数据转为String格式

    @Override
    public String toString() {
        return "DeviceUploadData{" +
                " devid='" + devid + '\'' +
                ", sn=" + sn +
                ", temperature=" + temperature +
                ", humidity=" + humidity +
                ", bat=" + bat +
                ", mc=" + mc +
                ", report_period=" + report_period +
                ", upload_time='" + upload_time + '\'' +
                ", raw_data='" + raw_data + '\'' +
                '}';
    }

    public int getReport_period() {
        return report_period;
    }

    public void setReport_period(int report_period) {
        this.report_period = report_period;
    }

    public int getSn() {
        return sn;
    }

    public void setSn(int sn) {
        this.sn = sn;
    }

    public String getUpload_time() {
        return upload_time;
    }

    public void setUpload_time(String upload_time) {
        this.upload_time = upload_time;
    }

    public String getDevid() {
        return devid;
    }

    public void setDevid(String devid) {
        this.devid = devid;
    }

    public float getTemperature() {
        return temperature;
    }

    public void setTemperature(float temperature) {
        this.temperature = temperature;
    }

    public float getHumidity() {
        return humidity;
    }

    public void setHumidity(float humidity) {
        this.humidity = humidity;
    }

    public short getBat() {
        return bat;
    }

    public void setBat(short bat) {
        this.bat = bat;
    }

    public short getMc() {
        return mc;
    }

    public void setMc(short mc) {
        this.mc = mc;
    }

    public String getRaw_data() {
        return raw_data;
    }

    public void setRaw_data(String raw_data) {
        this.raw_data = raw_data;
    }
}


