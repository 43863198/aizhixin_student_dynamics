package com.aizhixin.cloud.dataanalysis.monitor.job;

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
import com.aizhixin.cloud.dataanalysis.analysis.entity.SchoolStatistics;
import com.aizhixin.cloud.dataanalysis.common.constant.AlertTypeConstant;
import com.aizhixin.cloud.dataanalysis.common.constant.DataValidity;
import com.aizhixin.cloud.dataanalysis.common.constant.RollCallConstant;
import com.aizhixin.cloud.dataanalysis.common.constant.WarningTypeConstant;
import com.aizhixin.cloud.dataanalysis.common.util.DateUtil;
import com.aizhixin.cloud.dataanalysis.common.util.RestUtil;
import com.aizhixin.cloud.dataanalysis.monitor.entity.AbnormalAttendanceStatistics;
import com.aizhixin.cloud.dataanalysis.monitor.service.AbnormalAttendanceStatisticsService;
import com.aizhixin.cloud.dataanalysis.rollCall.mongoEntity.RollCall;
import com.aizhixin.cloud.dataanalysis.rollCall.mongoEntity.RollCallCount;
import com.aizhixin.cloud.dataanalysis.rollCall.mongoRespository.RollCallCountMongoRespository;
import com.aizhixin.cloud.dataanalysis.rollCall.mongoRespository.RollCallMongoRespository;
import com.aizhixin.cloud.dataanalysis.setup.service.AlarmRuleService;
import com.aizhixin.cloud.dataanalysis.setup.service.AlarmSettingsService;
import com.aizhixin.cloud.dataanalysis.setup.service.ProcessingModeService;

import org.apache.log4j.Logger;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.aizhixin.cloud.dataanalysis.setup.entity.AlarmRule;
import com.aizhixin.cloud.dataanalysis.setup.entity.AlarmSettings;
import com.aizhixin.cloud.dataanalysis.setup.entity.ProcessingMode;
import com.mongodb.AggregationOutput;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.mongodb.util.JSON;

@Component
public class RollCallDayJob {

	public volatile static boolean flag = true;

	private Logger logger = Logger.getLogger(this.getClass());
	@Autowired
	private RollCallMongoRespository rollCallMongoRespository;
	@Autowired
	private MongoTemplate mongoTemplate;
	@Autowired
	private AbnormalAttendanceStatisticsService abnormalAttendanceStatisticsService;

//	 @Scheduled(cron = "0 0/1 * * * ?")
	public void rollCallDayCountJob() {

		HashMap<Long, AbnormalAttendanceStatistics> resultMap = getDayRollCallCount();
		if (null != resultMap && resultMap.size() > 0) {
			Iterator iter = resultMap.entrySet().iterator();
			List<AbnormalAttendanceStatistics> resultList = new ArrayList<AbnormalAttendanceStatistics>();
			while (iter.hasNext()) {

				Map.Entry entry = (Map.Entry) iter.next();
				AbnormalAttendanceStatistics val = (AbnormalAttendanceStatistics) entry
						.getValue();
				resultList.add(val);
			}
			if(!resultList.isEmpty()){
				abnormalAttendanceStatisticsService.saveList(resultList);
			}
		}
	}

	private HashMap<Long, AbnormalAttendanceStatistics> getDayRollCallCount() {

		Calendar c = Calendar.getInstance();
		// 当前年份
		int schoolYear = c.get(Calendar.YEAR);
		// 当前月份
		int month = c.get(Calendar.MONTH);
		// 当前学期编号
		int semester = 2;
		if (month > 2 && month < 9) {
			semester = 1;
		}
		if (month <= 2) {
			semester = 1;
			schoolYear = schoolYear - 1;
		}

		String groupStr1 = "{ $group: {\"_id\": {  \"orgId\": \"$orgId\" ,\"jobNum\" : \"$jobNum\",\"rollCallResult\" : \"$rollCallResult\" } } }";
		DBObject group1 = (DBObject) JSON.parse(groupStr1);
		String groupStr2 = "{ $group: {\"_id\": { \"orgId\": \"$_id.orgId\",\"rollCallResult\" : \"$_id.rollCallResult\"}, count : { $sum : 1 }  } }";
		DBObject group2 = (DBObject) JSON.parse(groupStr2);
		String matchStr = "{$match :{\"schoolYear\":" + schoolYear
				+ ",\"semester\":" + semester + "}}";
		DBObject match = (DBObject) JSON.parse(matchStr);
		String projectStr = "{ $project : {\"_id\": 0, \"orgId\" : \"$_id.orgId\",\"rollCallResult\" : \"$_id.rollCallResult\", \"count\" : 1}}";
		DBObject project = (DBObject) JSON.parse(projectStr);
		AggregationOutput output = mongoTemplate.getCollection("RollCall")
				.aggregate(match, group1, group2, project);

		HashMap<Long, AbnormalAttendanceStatistics> resultMap = new HashMap<Long, AbnormalAttendanceStatistics>();
		for (Iterator<DBObject> it = output.results().iterator(); it.hasNext();) {
			BasicDBObject dbo = (BasicDBObject) it.next();
			long orgId = (Long) dbo.get("orgId");
			int countNum = (Integer) dbo.get("count");
			Integer rollCallResult = (Integer) dbo.get("rollCallResult");
			AbnormalAttendanceStatistics abnormalAttendanceStatistics = resultMap
					.get(orgId);
			if (null != abnormalAttendanceStatistics) {
				if (null != rollCallResult && rollCallResult.intValue() == 2) {
					abnormalAttendanceStatistics
							.setAbsenteeismNum(rollCallResult.intValue());
				}
				if (null != rollCallResult && rollCallResult.intValue() == 3) {
					abnormalAttendanceStatistics.setLateNum(rollCallResult
							.intValue());
				}
				if (null != rollCallResult && rollCallResult.intValue() == 5) {
					abnormalAttendanceStatistics
							.setLeaveEarlyNum(rollCallResult.intValue());
				}
				if (null != rollCallResult && rollCallResult.intValue() == 4) {
					abnormalAttendanceStatistics.setLeaveNum(rollCallResult
							.intValue());
				}
				resultMap.put(orgId, abnormalAttendanceStatistics);
			} else {
				abnormalAttendanceStatistics = new AbnormalAttendanceStatistics();
				abnormalAttendanceStatistics.setOrgId(orgId);
				abnormalAttendanceStatistics.setStatisticalTime(DateUtil
						.getCurrentTime(DateUtil.FORMAT_SHORT));
				if (null != rollCallResult && rollCallResult.intValue() == 2) {
					abnormalAttendanceStatistics
							.setAbsenteeismNum(rollCallResult.intValue());
				}
				if (null != rollCallResult && rollCallResult.intValue() == 3) {
					abnormalAttendanceStatistics.setLateNum(rollCallResult
							.intValue());
				}
				if (null != rollCallResult && rollCallResult.intValue() == 5) {
					abnormalAttendanceStatistics
							.setLeaveEarlyNum(rollCallResult.intValue());
				}
				if (null != rollCallResult && rollCallResult.intValue() == 4) {
					abnormalAttendanceStatistics.setLeaveNum(rollCallResult
							.intValue());
				}
				resultMap.put(orgId, abnormalAttendanceStatistics);
			}
		}

		return resultMap;
	}
}
