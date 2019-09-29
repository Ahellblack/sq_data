package com.siti.wisdomhydrologic.util.enumbean;

/**
 * @author DC
 */
public enum DataError {

	MORE_TIDE("data_010","潮位大于历史最大值"),
	LESS_TIDE("data_018","潮位小于历史最小值"),
	UP_MAX_TIDE("data_026","潮位上升超过历史上升最大值"),
	DOWN_MAX_TIDE("data_034","潮位下降超过历史下降最大值"),
	DURING_TIDE("data_042","潮位数据不变时长过长"),
	BREAK_TIDE("data_78","潮位中断"),

	MORE_RainFall("data_016","雨量大于历史最大值"),
	LESS_RainFall("data_024","雨量小于历史最小值"),
	HOUR_LESS_RainFall("data_074","小时雨量小于历史最小值"),
	HOUR_MORE_RainFall("data_066","小时雨量大于历史最大值"),
	BREAK_RainFall("data_008","雨量数据中断"),
	MORENEAR_RainFall("data_76","大于附近均值低于附近值"),

	MORE_WATERLEVEL("data_009","水位大于历史最大值"),
	LESS_WATERLEVEL("data_017","水位小于历史最小值"),
	UP_MAX_WATERLEVEL("data_025","水位上升超过历史上升最大值"),
	DOWN_MAX_WATERLEVEL("data_033","水位下降超过历史下降最大值"),
	BREAK_WATERLEVEL("data_001","水位数据中断"),
	HOUR_BREAK_WATERLEVEL("data_001","水位数据中断"),
	DURING_WATERLEVEL("data_041","水位数据不变时长过长"),
	MORE_WINDSPEED("data_011","风速大于历史最大值"),
	LESS_WINDSPEED("data_019","风速小于历史最小值"),
	UP_MAX_WINDSPEED("data_027","风速上升超过历史上升最大值"),
	DOWN_MAX_WINDSPEED("data_035","风速下降超过历史下降最大值"),
	BREAK_WINDSPEED("data_003","风速数据中断"),
	DURING_WINDSPEED("data_043","风速数据不变时长过长"),

	MORE_WINDDIRECTION("data_012","风向大于历史最大值"),
	LESS_WINDDIRECTION("data_020","风向小于历史最小值"),
	UP_MAX_WINDDIRECTION("data_028","风向上升超过历史上升最大值"),
	DOWN_MAX_WINDDIRECTION("data_036","风向下降超过历史下降最大值"),
	BREAK_WINDDIRECTION("data_004","风向数据中断"),
	DURING_WINDDIRECTION("data_044","风向数据不变时长过长"),

	MORE_AIRTEMPRATURE("data_014","气温大于历史最大值"),
	LESS_AIRTEMPRATURE("data_022","气温小于历史最小值"),
	UP_MAX_AIRTEMPRATURE("data_030","气温上升超过历史上升最大值"),
	DOWN_MAX_AIRTEMPRATURE("data_038","气温下降超过历史下降最大值"),
	BREAK_AIRTEMPRATURE("data_006","气温数据不变时长过长"),
	DURING_AIRTEMPRATURE("data_046","气温持续时间"),

	MORE_AirPress("data_013","气压大于历史最大值"),
	LESS_AirPress("data_021","气压小于历史最小值"),
	UP_MAX_AirPress("data_029","气压上升超过历史上升最大值"),
	DOWN_MAX_AirPress("data_037","气压下降超过历史下降最大值"),
	BREAK_AirPress("data_005","气压数据中断"),
	DURING_AirPress("data_045","气压数据不变时长过长"),

	MORE_FlowVelocity("data_015","流速大于历史最大值"),
	LESS_FlowVelocity("data_023","流速小于历史最小值"),
	UP_MAX_FlowVelocity("data_031","流速上升超过历史上升最大值"),
	DOWN_MAX_FlowVelocity("data_039","流速下降超过历史下降最大值"),
	BREAK_FlowVelocity("data_007","流速数据中断"),
	DURING_FlowVelocity("data_047","流速数据不变时长过长"),

	MORE_BIG_E("data_58","电压超过极值"),
	LESS_SMALL_E("data_59","电压低于极值"),
	CHANGE_BIG_E("data_60","电压变化量超过极值"),
	CHANGE_SMALL_E("data_61","电压变化量低于极值"),
	WS_INTER_E("data_62","电压出现中断"),
	WS_DURA_E("data_63","电压持续时间"), //

	MORE_BIG_Y("data_015","流速大于历史最大值"),
	LESS_SMALL_Y("data_023","流速小于历史最小值"),
	CHANGE_BIG_Y("data_031","流速上升超过历史上升最大值"),
	CHANGE_SMALL_Y("data_039","流速下降超过历史下降最大值"),
	WS_INTER_Y("data_007","流速数据中断"),
	WS_DURA_Y("data_047","流速数据不变时长过长"),

	EQ_RAIN("eq_5","数据计算服务异常"),  // 测站异常先屏蔽，不管什么传感器数据，都是使用同一个计算服务器，均改为 se_001 数据计算服务器故障
	EQ_TIDE("eq_6","潮位计算服务异常"),
	EQ_WATER("eq_7","水位计算服务异常"),
	INTENT_WATER("data_79","水位中断"), // 和 WS_INTER_WL 有什么区分
	INTENT_WATER_UPMAX("data_80",""),// 和 INTENT_WATER_UPMAX 有什么区分
	EQ_UPLOAD("eq_019","测站上传故障"),  //
	regression_md("md_1","回归模型异常"),  // 拆分出来，雨量、水位、潮位、风速、风向、流速、气温、气压等 data_075-data_082
	regression_NOTFOUND("md_2","模型数据丢失"), // 模型缺失的话，不用进行回归模型的判断
	EQ_ELESHUTDOWN("eq_020","断电故障");  //


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
