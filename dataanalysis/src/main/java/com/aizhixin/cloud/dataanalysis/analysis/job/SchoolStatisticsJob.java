//package com.aizhixin.cloud.dataanalysis.analysis.job;
//
//import java.util.ArrayList;
//import java.util.Calendar;
//import java.util.Date;
//import java.util.HashMap;
//import java.util.HashSet;
//import java.util.Iterator;
//import java.util.List;
//import java.util.Map;
//import java.util.Set;
//import java.util.UUID;
//
//import com.aizhixin.cloud.dataanalysis.alertinformation.entity.WarningInformation;
//import com.aizhixin.cloud.dataanalysis.alertinformation.repository.AlertWarningInformationRepository;
//import com.aizhixin.cloud.dataanalysis.alertinformation.service.AlertWarningInformationService;
//import com.aizhixin.cloud.dataanalysis.alertinformation.service.OperaionRecordService;
//import com.aizhixin.cloud.dataanalysis.common.constant.AlertTypeConstant;
//import com.aizhixin.cloud.dataanalysis.common.constant.DataValidity;
//import com.aizhixin.cloud.dataanalysis.common.constant.RollCallConstant;
//import com.aizhixin.cloud.dataanalysis.common.constant.WarningType;
//import com.aizhixin.cloud.dataanalysis.common.util.DateUtil;
//import com.aizhixin.cloud.dataanalysis.common.util.RestUtil;
//import com.aizhixin.cloud.dataanalysis.rollCall.mongoEntity.RollCall;
//import com.aizhixin.cloud.dataanalysis.rollCall.mongoEntity.RollCallCount;
//import com.aizhixin.cloud.dataanalysis.setup.service.AlarmRuleService;
//import com.aizhixin.cloud.dataanalysis.setup.service.AlarmSettingsService;
//import com.aizhixin.cloud.dataanalysis.setup.service.ProcessingModeService;
//
//import org.apache.log4j.Logger;
//
//import net.sf.json.JSONArray;
//import net.sf.json.JSONObject;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.scheduling.annotation.Scheduled;
//import org.springframework.stereotype.Component;
//import org.springframework.util.StringUtils;
//
//import com.aizhixin.cloud.dataanalysis.setup.entity.AlarmRule;
//import com.aizhixin.cloud.dataanalysis.setup.entity.AlarmSettings;
//import com.aizhixin.cloud.dataanalysis.setup.entity.ProcessingMode;
//import com.aizhixin.cloud.dataanalysis.studentRegister.mongoEntity.StudentRegister;
//import com.aizhixin.cloud.dataanalysis.studentRegister.mongoRespository.StudentRegisterMongoRespository;
//
//@Component
//public class SchoolStatisticsJob {
//
//	public volatile static boolean flag = true;
//
//	private Logger logger = Logger.getLogger(this.getClass());
//	@Autowired
//	private AlarmSettingsService alarmSettingsService;
//	@Autowired
//	private AlarmRuleService alarmRuleService;
//	@Autowired
//	private StudentRegisterMongoRespository stuRegisterMongoRespository;
//
//	public void rollCallCountJob() {
//
//		// 获取预警配置
//		List<AlarmSettings> settingsList = alarmSettingsService
//				.getAlarmSettingsByType(WarningType.Absenteeism.toString());
//		if (null != settingsList && settingsList.size() > 0) {
//			
//			Calendar c = Calendar.getInstance();
//			//当前年份
//			int schoolYear = c.get(Calendar.YEAR);
//			//当前月份
//			int month = c.get(Calendar.MONTH);
//			//当前学期编号
//			int semester = 2;
//			if( month > 2 && month < 9){
//				semester = 1;
//			}
//			
//			HashMap<Long, Long> alarmMap = new HashMap<Long,Long>();
//			// 按orgId归类告警等级阀值
//			for (AlarmSettings settings : settingsList) {
//
//				Long orgId = settings.getOrgId();
//				alarmMap.put(orgId, orgId);
//			}
//
//			//清除之前考勤统计数据
////			rollCallCountMongoRespository.deleteAll();
//			Iterator iter = alarmMap.entrySet().iterator();
//			while (iter.hasNext()) {
//
//				List<RollCallCount> rollCallCountList = new ArrayList<RollCallCount>();
//				HashMap<String,RollCallCount> rollCallCountMap = new HashMap<String,RollCallCount>();
//				Map.Entry entry = (Map.Entry) iter.next();
//				Long orgId = (Long) entry.getKey();
//
//				List<RollCall> rollCallList = rollCallMongoRespository.findAllBySchoolYearAndSemesterAndOrgIdAndRollCallResult(schoolYear, semester, orgId,RollCallConstant.OUT_SCHOOL);
//				for(RollCall rollCall : rollCallList){
//					RollCallCount rollCallCount = rollCallCountMap.get(rollCall.getJobNum());
//					if(null == rollCallCount){
//						rollCallCount = new RollCallCount();
//						rollCallCount.setClassId(rollCall.getClassId());
//						rollCallCount.setClassName(rollCall.getClassName());
//						rollCallCount.setCollegeId(rollCall.getCollegeId());
//						rollCallCount.setCollegeName(rollCall.getCollegeName());
//						rollCallCount.setGrade(rollCall.getGrade());
//						rollCallCount.setJobNum(rollCall.getJobNum());
//						rollCallCount.setOrgId(orgId);
//						rollCallCount.setProfessionalId(rollCall.getProfessionalId());
//						rollCallCount.setProfessionalName(rollCall.getProfessionalName());
//						rollCallCount.setSchoolYear(schoolYear);
//						rollCallCount.setSemester(semester);
//						rollCallCount.setUserId(rollCall.getUserId());
//						rollCallCount.setUserPhone(rollCall.getUserPhone());
//						rollCallCount.setUserName(rollCall.getUserName());
//						if(RollCallConstant.OUT_SCHOOL == rollCall.getRollCallResult()){
//							rollCallCount.setOutSchoolTimes(1);
//						}
//						rollCallCountMap.put(rollCall.getJobNum(), rollCallCount);
//					}else{
//						if(RollCallConstant.OUT_SCHOOL == rollCall.getRollCallResult()){
//							rollCallCount.setOutSchoolTimes(rollCallCount.getOutSchoolTimes()+1);
//							rollCallCountMap.put(rollCall.getJobNum(), rollCallCount);
//						}
//					}
//				}
//				
//				Iterator rollCalliter = rollCallCountMap.entrySet().iterator();
//				while (rollCalliter.hasNext()) {
//					Map.Entry rollCallentry = (Map.Entry) rollCalliter.next();
//					RollCallCount rollCallCount = (RollCallCount)rollCallentry.getValue();
//					if(rollCallCount.getOutSchoolTimes() > 0){
//						rollCallCountList.add(rollCallCount);
//					}
//				}
//				
//				rollCallCountMongoRespository.save(rollCallCountList);
//			}
//		}
//	}
//
//
//}
