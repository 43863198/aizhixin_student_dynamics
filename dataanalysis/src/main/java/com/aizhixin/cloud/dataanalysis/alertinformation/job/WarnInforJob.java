package com.aizhixin.cloud.dataanalysis.alertinformation.job;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import com.aizhixin.cloud.dataanalysis.alertinformation.entity.WarningInformation;
import com.aizhixin.cloud.dataanalysis.alertinformation.repository.AlertWarningInformationRepository;
import com.aizhixin.cloud.dataanalysis.alertinformation.service.AlertWarningInformationService;
import com.aizhixin.cloud.dataanalysis.alertinformation.service.PushMessageService;
import com.aizhixin.cloud.dataanalysis.common.constant.AlertTypeConstant;
import com.aizhixin.cloud.dataanalysis.common.constant.DataValidity;
import com.aizhixin.cloud.dataanalysis.common.constant.RollCallConstant;
import com.aizhixin.cloud.dataanalysis.common.constant.WarningTypeConstant;
import com.aizhixin.cloud.dataanalysis.common.util.DateUtil;
import com.aizhixin.cloud.dataanalysis.common.util.RestUtil;
import com.aizhixin.cloud.dataanalysis.rollCall.mongoEntity.RollCall;
import com.aizhixin.cloud.dataanalysis.rollCall.mongoEntity.RollCallCount;
import com.aizhixin.cloud.dataanalysis.rollCall.mongoRespository.RollCallCountMongoRespository;
import com.aizhixin.cloud.dataanalysis.rollCall.mongoRespository.RollCallMongoRespository;
import com.aizhixin.cloud.dataanalysis.setup.service.AlarmSettingsService;
import com.aizhixin.cloud.dataanalysis.setup.service.ProcessingModeService;
import com.aizhixin.cloud.dataanalysis.setup.service.WarningTypeService;

import org.apache.log4j.Logger;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.aizhixin.cloud.dataanalysis.setup.domain.WarningTypeDomain;
import com.aizhixin.cloud.dataanalysis.setup.entity.AlarmSettings;
import com.aizhixin.cloud.dataanalysis.setup.entity.ProcessingMode;

@Component
public class WarnInforJob {

	private Logger logger = Logger.getLogger(this.getClass());
	@Autowired
	private AlertWarningInformationService alertWarningInformationService;
	@Autowired
	private WarningTypeService warningTypeService;
    @Autowired
	private PushMessageService pushMessageService;
	public void updateWarnStateJob() {
		logger.debug("黄色预警自动处理任务开始执行......");
		List<WarningTypeDomain> orgIdList = warningTypeService.getAllOrgId();
		if (null != orgIdList && orgIdList.size() > 0) {
			HashSet<Long> orgIds = new HashSet<Long>();
			for (int i = 0; i < orgIdList.size(); i++) {
				orgIds.add(orgIdList.get(i).getOrgId());
			}
			Long result = alertWarningInformationService.countyWarningLevel(
					AlertTypeConstant.ALERT_IN_PROCESS,
					AlertTypeConstant.WARN_STATE_YELLOW_ALERT);
			if (result.longValue() > 0) {

				List<WarningInformation>warningInformationList=alertWarningInformationService.findWarningInfoByWarningLevel(
						AlertTypeConstant.ALERT_IN_PROCESS,
						AlertTypeConstant.WARN_STATE_YELLOW_ALERT,
						orgIds);
						alertWarningInformationService
								.updateWarningStateByWarningLevel(
										AlertTypeConstant.ALERT_PROCESSED,
										AlertTypeConstant.WARN_STATE_YELLOW_ALERT,
										orgIds);
				pushMessageService.createPushMessage(warningInformationList);
			}
		}
		logger.debug("黄色预警自动处理任务执行结束......");
	}

}
