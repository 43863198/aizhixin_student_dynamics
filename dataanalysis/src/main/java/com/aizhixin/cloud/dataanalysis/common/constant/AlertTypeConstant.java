package com.aizhixin.cloud.dataanalysis.common.constant;

public class AlertTypeConstant {

	//预警规则开启状态(10:启用 ;20:关闭；)
	public final static int SETUP_ALERTSETTING = 10;
	
	//预警规则开启状态(10:启用 ;20:关闭；)
	public final static int CLOSE_ALERTSETTING = 20;
	
	
	//状态(10:告警中；20：已处理；30：处理中；40:取消)
	public final static int ALERT_PENDING = 10;
	
	//状态(10:告警中；20：已处理；30：处理中；40:取消)
	public final static int ALERT_PROCESSED = 20;
	
	//状态(10:告警中；20：已处理；30：处理中；40:取消)
	public final static int ALERT_IN_PROCESS= 30;
	
	//状态(10:告警中；20：已处理；30：处理中；40:取消)
	public final static int ALERT_IN_CANCEL = 40;
}
