package com.siti.wisdomhydrologic.analysis.entity;

import javax.persistence.Table;

/**
 * Created by DC on 2019/8/26.
 *  回归模型表
 * @data ${DATA}-11:07
 */
@Table(name="config_regression_function")
public class RegressionEntity {
    private Integer sectionCode;
    private String sectionName;
    private Integer refNum;

    private Double arg0;

    private Integer ref1SectionCode;
    private String ref1SectionName;
    private Double arg1;

    private Integer ref2SectionCode;
    private String ref2SectionName;
    private Double arg2;

    private Integer ref3SectionCode;
    private String ref3SectionName;
    private Double arg3;

    private Double abResidualMax;

    @Override
    public String toString() {
        return "RegressionEntity{" +
                "sectionCode=" + sectionCode +
                ", refNum=" + refNum +
                ", arg0=" + arg0 +
                ", ref1SectionCode=" + ref1SectionCode +
                ", ref1SectionName=" + ref1SectionName +
                ", arg1=" + arg1 +
                ", ref2SectionCode=" + ref2SectionCode +
                ", ref2SectionName=" + ref2SectionName +
                ", arg2=" + arg2 +
                ", ref3SectionCode=" + ref3SectionCode +
                ", ref3SectionName=" + ref3SectionName +
                ", arg3=" + arg3 +
                ", abResidualMax=" + abResidualMax +
                '}';
    }

    public Integer getSectionCode() {
        return sectionCode;
    }

    public void setSectionCode(Integer sectionCode) {
        this.sectionCode = sectionCode;
    }

    public String getSectionName() {
        return sectionName;
    }

    public void setSectionName(String sectionName) {
        this.sectionName = sectionName;
    }

    public Integer getRefNum() {
        return refNum;
    }

    public void setRefNum(Integer refNum) {
        this.refNum = refNum;
    }

    public Double getArg0() {
        return arg0;
    }

    public void setArg0(Double arg0) {
        this.arg0 = arg0;
    }

    public Integer getRef1SectionCode() {
        return ref1SectionCode;
    }

    public void setRef1SectionCode(Integer ref1SectionCode) {
        this.ref1SectionCode = ref1SectionCode;
    }

    public String getRef1SectionName() {
        return ref1SectionName;
    }

    public void setRef1SectionName(String ref1SectionName) {
        this.ref1SectionName = ref1SectionName;
    }

    public Double getArg1() {
        return arg1;
    }

    public void setArg1(Double arg1) {
        this.arg1 = arg1;
    }

    public Integer getRef2SectionCode() {
        return ref2SectionCode;
    }

    public void setRef2SectionCode(Integer ref2SectionCode) {
        this.ref2SectionCode = ref2SectionCode;
    }

    public String getRef2SectionName() {
        return ref2SectionName;
    }

    public void setRef2SectionName(String ref2SectionName) {
        this.ref2SectionName = ref2SectionName;
    }

    public Double getArg2() {
        return arg2;
    }

    public void setArg2(Double arg2) {
        this.arg2 = arg2;
    }

    public Integer getRef3SectionCode() {
        return ref3SectionCode;
    }

    public void setRef3SectionCode(Integer ref3SectionCode) {
        this.ref3SectionCode = ref3SectionCode;
    }

    public String getRef3SectionName() {
        return ref3SectionName;
    }

    public void setRef3SectionName(String ref3SectionName) {
        this.ref3SectionName = ref3SectionName;
    }

    public Double getArg3() {
        return arg3;
    }

    public void setArg3(Double arg3) {
        this.arg3 = arg3;
    }

    public Double getAbResidualMax() {
        return abResidualMax;
    }

    public void setAbResidualMax(Double abResidualMax) {
        this.abResidualMax = abResidualMax;
    }
}
/*
 `section_code` int(10) NOT NULL COMMENT '因变量元素编号',
         `section_name` varchar(20) COLLATE utf8_bin DEFAULT NULL,
         `ref_num` tinyint(5) NOT NULL COMMENT '自变量个数',
         `arg0` decimal(10,4) NOT NULL COMMENT '参数0',
         `ref1_section_code` int(10) NOT NULL COMMENT '自变量1元素编号',
         `ref1_section_name` varchar(20) COLLATE utf8_bin DEFAULT NULL,
         `arg1` decimal(10,4) NOT NULL COMMENT '自变量1的参数',
         `ref2_section_code` int(10) DEFAULT NULL COMMENT '自变量2元素编号',
         `ref2_section_name` varchar(20) COLLATE utf8_bin DEFAULT NULL,
         `arg2` decimal(10,4) DEFAULT NULL COMMENT '自变量2的参数',
         `ref3_section_code` int(10) DEFAULT NULL COMMENT '自变量3元素编号',
         `ref3_section_name` varchar(20) COLLATE utf8_bin DEFAULT NULL,
         `arg3` decimal(10,4) DEFAULT NULL COMMENT '自变量3的参数',
         `ab_residual_max` decimal(10,4) NOT NULL COMMENT '残差绝对值上限',*/
