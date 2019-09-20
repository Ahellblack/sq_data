package com.siti.wisdomhydrologic.util.enumbean;

/**
 * @author DC
 */
public enum DataError {

	MORE_BIG_T("data_11","潮位超过极值"),
	LESS_SMALL_T("data_12","潮位低于极值"),
	CHANGE_BIG_T("data_13","潮位变化量超过极值"),
	CHANGE_SMALL_T("data_14","潮位变化量低于极值"),
	DURA_T("data_77","数据不变得时长"),
	//CHANGE_SMALL_T("data_14"),
	INTENT_T("data_78","最大中断次数"),
	FIVE_MORE_R("data_3","5分钟雨量数据超出阈值"),
	FIVE_LESS_R("data_72","5分钟雨量低于阈值"),
	HOUR_LESS_R("data_73","小时雨量低于阈值"),
	HOUR_MORE_R("data_4","小时雨量数据超出阈值"),
	DAY_LESS_R("data_74","天雨量低于阈值"),
	DAY_MORE_R("data_5","日雨量数据超出阈值"),
	RAIN_INTER("data_75","雨量中断次数"),
	MORENEAR_R("data_76","大于附近均值?低于附近值？"),

	MORE_BIG_WL("data_7","水位超过极值"),
	LESS_SMALL_WL("data_8","水位低于极值"),
	CHANGE_BIG_WL("data_9","水位变化量超过最大值"),
	CHANGE_SMALL_WL("data_10","水位变化量低于极值"),
	WS_INTER_WL("data_70",""),//
	WS_DURA_WL("data_71",""),//
	//data_15
	MORE_BIG("data_15","风速超过极值"),
	LESS_SMALL("data_16","风速低于极值"),
	CHANGE_BIG("data_17","风速变化量超过极值"),
	CHANGE_SMALL("data_18","风速变化量低于极值"),
	WS_INTER("data_32","风速出现中断"),
	WS_DURA("data_33","风速一致持续时间"),

	MORE_BIG_WD("data_34","风向超过极值"),
	LESS_SMALL_WD("data_35","风向低于极值"),
	CHANGE_BIG_WD("data_36","风向变化量超过极值"),
	CHANGE_SMALL_WD("data_37","风向变化量低于极值"),
	WS_INTER_WD("data_38","风向出现中断"),
	WS_DURA_WD("data_39","风向持续时间"),

	MORE_BIG_AT("data_40","气温超过极值"),
	LESS_SMALL_AT("data_41","气温低于极值"),
	CHANGE_BIG_AT("data_42","气温变化量超过极值"),
	CHANGE_SMALL_AT("data_43","气温变化量低于极值"),
	WS_INTER_AT("data_44","气温出现中断"),
	WS_DURA_AT("data_45","气温持续时间"),

	MORE_BIG_AP("data_46","气压超过极值"),
	LESS_SMALL_AP("data_47","气压低于极值"),
	CHANGE_BIG_AP("data_48","气压变化量超过极值"),
	CHANGE_SMALL_AP("data_49","气压变化量低于极值"),
	WS_INTER_AP("data_50","气压出现中断"),
	WS_DURA_AP("data_51","气压持续时间"),

	MORE_BIG_FV("data_52","流速超过极值"),
	LESS_SMALL_FV("data_53","流速低于极值"),
	CHANGE_BIG_FV("data_54","流速变化量超过极值"),
	CHANGE_SMALL_FV("data_55","流速变化量低于极值"),
	WS_INTER_FV("data_56","流速出现中断"),
	WS_DURA_FV("data_57","流速持续时间"),

	MORE_BIG_E("data_58","电压超过极值"),
	LESS_SMALL_E("data_59","电压低于极值"),
	CHANGE_BIG_E("data_60","电压变化量超过极值"),
	CHANGE_SMALL_E("data_61","电压变化量低于极值"),
	WS_INTER_E("data_62","电压出现中断"),
	WS_DURA_E("data_63","电压持续时间"),

	MORE_BIG_Y("data_64","流速Y变化量超过极值"),
	LESS_SMALL_Y("data_65","流速Y变化量低于极值"),
	CHANGE_BIG_Y("data_66","流速Y变化量超过极值"),
	CHANGE_SMALL_Y("data_67","流速Y变化量低于极值"),
	WS_INTER_Y("data_68","流速Y出现中断"),
	WS_DURA_Y("data_69","流速Y持续时间"),
	EQ_RAIN("eq_5","数据计算服务异常"),
	EQ_TIDE("eq_6","潮位计算服务异常"),
	EQ_WATER("eq_7","水位计算服务异常"),
	INTENT_WATER("data_79","水位中断"),
	INTENT_WATER_UPMAX("data_80",""),//
	EQ_UPLOAD("eq_15","测站上传故障"),
	regression_md("md_1","回归模型异常"),
	regression_NOTFOUND("md_2","模型数据丢失"),
	EQ_ELESHUTDOWN("eq_16","断电故障");
	private String errorCode;
	private String desc;

	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	DataError(String errorCode,String desc){
		this.errorCode = errorCode;
		this.desc=desc;
	}
	
	public String getErrorCode() {
		return errorCode;
	}

}
