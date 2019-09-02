package com.siti.wisdomhydrologic.util.enumbean;

/**
 * @author DC
 */
public enum DataError {

	MORE_BIG_T("data_11"),
	LESS_SMALL_T("data_12"),
	CHANGE_BIG_T("data_13"),
	CHANGE_SMALL_T("data_14"),
	DURA_T("data_77"),
	//CHANGE_SMALL_T("data_14"),
	INTENT_T("data_78"),
	FIVE_MORE_R("data_3"),
	FIVE_LESS_R("data_72"),
	HOUR_LESS_R("data_73"),
	HOUR_MORE_R("data_4"),
	DAY_LESS_R("data_74"),
	DAY_MORE_R("data_5"),
	RAIN_INTER("data_75"),
	MORENEAR_R("data_76"),

	MORE_BIG_WL("data_7"),
	LESS_SMALL_WL("data_8"),
	CHANGE_BIG_WL("data_9"),
	CHANGE_SMALL_WL("data_10"),
	WS_INTER_WL("data_70"),
	WS_DURA_WL("data_71"),
	//data_15
	MORE_BIG("data_15"),
	LESS_SMALL("data_16"),
	CHANGE_BIG("data_17"),
	CHANGE_SMALL("data_18"),
	WS_INTER("data_32"),
	WS_DURA("data_33"),

	MORE_BIG_WD("data_34"),
	LESS_SMALL_WD("data_35"),
	CHANGE_BIG_WD("data_36"),
	CHANGE_SMALL_WD("data_37"),
	WS_INTER_WD("data_38"),
	WS_DURA_WD("data_39"),

	MORE_BIG_AT("data_40"),
	LESS_SMALL_AT("data_41"),
	CHANGE_BIG_AT("data_42"),
	CHANGE_SMALL_AT("data_43"),
	WS_INTER_AT("data_44"),
	WS_DURA_AT("data_45"),

	MORE_BIG_AP("data_46"),
	LESS_SMALL_AP("data_47"),
	CHANGE_BIG_AP("data_48"),
	CHANGE_SMALL_AP("data_49"),
	WS_INTER_AP("data_50"),
	WS_DURA_AP("data_51"),

	MORE_BIG_FV("data_52"),
	LESS_SMALL_FV("data_53"),
	CHANGE_BIG_FV("data_54"),
	CHANGE_SMALL_FV("data_55"),
	WS_INTER_FV("data_56"),
	WS_DURA_FV("data_57"),

	MORE_BIG_E("data_58"),
	LESS_SMALL_E("data_59"),
	CHANGE_BIG_E("data_60"),
	CHANGE_SMALL_E("data_61"),
	WS_INTER_E("data_62"),
	WS_DURA_E("data_63"),

	MORE_BIG_Y("data_64"),
	LESS_SMALL_Y("data_65"),
	CHANGE_BIG_Y("data_66"),
	CHANGE_SMALL_Y("data_67"),
	WS_INTER_Y("data_68"),
	WS_DURA_Y("data_69"),
	EQ_RAIN("eq_5"),
	EQ_TIDE("eq_6"),
	EQ_WATER("eq_7"),
	INTENT_WATER("data_79"),
	INTENT_WATER_UPMAX("data_80"),
	EQ_UPLOAD("eq_15"),
	regression_md("md_1"),
	regression_NOTFOUND("md_2"),
	EQ_ELESHUTDOWN("eq_16");
	private String errorCode;

	DataError(String errorCode){
		this.errorCode = errorCode;
	}
	
	public String getErrorCode() {
		return errorCode;
	}

}
