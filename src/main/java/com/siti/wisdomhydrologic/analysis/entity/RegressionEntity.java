package com.siti.wisdomhydrologic.analysis.entity;

import javax.persistence.Table;

/**
 * Created by DC on 2019/8/26.
 *  回归模型表
 * @data ${DATA}-11:07
 */
@Table(name="config_regression_function")
public class RegressionEntity {
    private int sectionCode;
    private int refNum;

    private double arg0;

    private int ref1SectionCode;
    private int ref1SectionName;
    private double arg1;

    private int ref2SectionCode;
    private int ref2SectionName;
    private double arg2;

    private int ref3SectionCode;
    private int ref3SectionName;
    private double arg3;

    private double abResidualMax;

    public int getSectionCode() {
        return sectionCode;
    }

    public void setSectionCode(int sectionCode) {
        this.sectionCode = sectionCode;
    }

    public int getRefNum() {
        return refNum;
    }

    public void setRefNum(int refNum) {
        this.refNum = refNum;
    }

    public double getArg0() {
        return arg0;
    }

    public void setArg0(double arg0) {
        this.arg0 = arg0;
    }

    public int getRef1SectionCode() {
        return ref1SectionCode;
    }

    public void setRef1SectionCode(int ref1SectionCode) {
        this.ref1SectionCode = ref1SectionCode;
    }

    public int getRef1SectionName() {
        return ref1SectionName;
    }

    public void setRef1SectionName(int ref1SectionName) {
        this.ref1SectionName = ref1SectionName;
    }

    public double getArg1() {
        return arg1;
    }

    public void setArg1(double arg1) {
        this.arg1 = arg1;
    }

    public int getRef2SectionCode() {
        return ref2SectionCode;
    }

    public void setRef2SectionCode(int ref2SectionCode) {
        this.ref2SectionCode = ref2SectionCode;
    }

    public int getRef2SectionName() {
        return ref2SectionName;
    }

    public void setRef2SectionName(int ref2SectionName) {
        this.ref2SectionName = ref2SectionName;
    }

    public double getArg2() {
        return arg2;
    }

    public void setArg2(double arg2) {
        this.arg2 = arg2;
    }

    public int getRef3SectionCode() {
        return ref3SectionCode;
    }

    public void setRef3SectionCode(int ref3SectionCode) {
        this.ref3SectionCode = ref3SectionCode;
    }

    public int getRef3SectionName() {
        return ref3SectionName;
    }

    public void setRef3SectionName(int ref3SectionName) {
        this.ref3SectionName = ref3SectionName;
    }

    public double getArg3() {
        return arg3;
    }

    public void setArg3(double arg3) {
        this.arg3 = arg3;
    }

    public double getAbResidualMax() {
        return abResidualMax;
    }

    public void setAbResidualMax(double abResidualMax) {
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
