package com.aizhixin.cloud.dataanalysis.common.constant;

public class AlertTypeConstant {

	// 预警规则开启状态(10:启用 ;20:关闭；)
	public final static int SETUP_ALERTSETTING = 10;

	// 预警规则开启状态(10:启用 ;20:关闭；)
	public final static int CLOSE_ALERTSETTING = 20;

	// 状态(10:处理中；20：已处理)
	public final static int ALERT_IN_PROCESS = 10;

	// 状态(10:处理中；20：已处理)
	public final static int ALERT_PROCESSED = 20;

	// 预警等级：红色预警
	public final static int WARN_STATE_RED_ALERT = 1;

	// 预警等级：橙色预警
	public final static int WARN_STATE_ORANGE_ALERT = 2;

	// 预警等级：黄色预警
	public final static int WARN_STATE_YELLOW_ALERT = 3;

	//大于等于
	public final static String EQUAL_OR_GREATER_THAN = ">=";

	//大于
	public final static String GREATER_THAN = ">";

	//等于
	public final static String EQUAL = "=";

	//小于等于
	public final static String EQUAL_OR_LESS_THAN = "<=";
	
	//小于
	public final static String LESS_THAN = "<";
}
