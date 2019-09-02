package com.siti.wisdomhydrologic.datepull.entity;

public class ConfigSensorSectionModule {

  private int sectionCode;
  private String sectionName;
  private int sensorCode;
  private String sensorName;
  private int stationCode;
  private String stationName;
  private String sectionDataUnit;
  private String sectionStatus;


  public int getSectionCode() {
    return sectionCode;
  }

  public void setSectionCode(int sectionCode) {
    this.sectionCode = sectionCode;
  }

  public String getSectionName() {
    return sectionName;
  }

  public void setSectionName(String sectionName) {
    this.sectionName = sectionName;
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

  public int getStationCode() {
    return stationCode;
  }

  public void setStationCode(int stationCode) {
    this.stationCode = stationCode;
  }

  public String getStationName() {
    return stationName;
  }

  public void setStationName(String stationName) {
    this.stationName = stationName;
  }


  public String getSectionDataUnit() {
    return sectionDataUnit;
  }

  public void setSectionDataUnit(String sectionDataUnit) {
    this.sectionDataUnit = sectionDataUnit;
  }


  public String getSectionStatus() {
    return sectionStatus;
  }

  public void setSectionStatus(String sectionStatus) {
    this.sectionStatus = sectionStatus;
  }

  @Override
  public String toString() {
    return "ConfigSensorSectionModule{" + "sectionCode=" + sectionCode + ", sectionName='" + sectionName + '\'' + ", sensorCode=" + sensorCode + ", sensorName='" + sensorName + '\'' + ", stationCode=" + stationCode + ", stationName='" + stationName + '\'' + ", sectionDataUnit='" + sectionDataUnit + '\'' + ", sectionStatus='" + sectionStatus + '\'' + '}';
  }
}
