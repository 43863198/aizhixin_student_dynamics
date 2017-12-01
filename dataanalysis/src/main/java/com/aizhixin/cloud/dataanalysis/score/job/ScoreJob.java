package com.aizhixin.cloud.dataanalysis.score.job;

import java.math.BigDecimal;
import java.math.RoundingMode;
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

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.aizhixin.cloud.dataanalysis.alertinformation.entity.WarningInformation;
import com.aizhixin.cloud.dataanalysis.alertinformation.service.AlertWarningInformationService;
import com.aizhixin.cloud.dataanalysis.common.constant.AlertTypeConstant;
import com.aizhixin.cloud.dataanalysis.common.constant.DataValidity;
import com.aizhixin.cloud.dataanalysis.common.constant.ScoreConstant;
import com.aizhixin.cloud.dataanalysis.common.constant.WarningType;
import com.aizhixin.cloud.dataanalysis.common.util.DateUtil;
import com.aizhixin.cloud.dataanalysis.score.domain.ScoreDomain;
import com.aizhixin.cloud.dataanalysis.score.mongoEntity.MakeUpScoreCount;
import com.aizhixin.cloud.dataanalysis.score.mongoEntity.Score;
import com.aizhixin.cloud.dataanalysis.score.mongoEntity.ScoreFluctuateCount;
import com.aizhixin.cloud.dataanalysis.score.mongoEntity.TotalScoreCount;
import com.aizhixin.cloud.dataanalysis.score.mongoRespository.MakeUpScoreCountMongoRespository;
import com.aizhixin.cloud.dataanalysis.score.mongoRespository.ScoreFluctuateCountMongoRespository;
import com.aizhixin.cloud.dataanalysis.score.mongoRespository.ScoreMongoRespository;
import com.aizhixin.cloud.dataanalysis.score.mongoRespository.TotalScoreCountMongoRespository;
import com.aizhixin.cloud.dataanalysis.score.service.ScoreService;
import com.aizhixin.cloud.dataanalysis.setup.entity.AlarmRule;
import com.aizhixin.cloud.dataanalysis.setup.entity.AlarmSettings;
import com.aizhixin.cloud.dataanalysis.setup.service.AlarmRuleService;
import com.aizhixin.cloud.dataanalysis.setup.service.AlarmSettingsService;
import com.aizhixin.cloud.dataanalysis.setup.service.ProcessingModeService;

@Component
public class ScoreJob {

	@Autowired
	private ScoreService scoreService;
	@Autowired
	private AlarmRuleService alarmRuleService;
	@Autowired
	private AlarmSettingsService alarmSettingsService;
	@Autowired
	private TotalScoreCountMongoRespository totalScoreCountMongoRespository;
	@Autowired
	private MakeUpScoreCountMongoRespository makeUpScoreCountMongoRespository;
	@Autowired
	private ScoreMongoRespository scoreMongoRespository;
	@Autowired
	private AlertWarningInformationService alertWarningInformationService;
	@Autowired
	private ProcessingModeService processingModeService;
	@Autowired
	private ScoreFluctuateCountMongoRespository scoreFluctuateCountMongoRespository;

	
	/**
	 * 统计mongo里的相邻学期平均绩点
	 */
	public void scoreFluctuateCountJob() {

		// 获取预警配置
		List<AlarmSettings> settingsList = alarmSettingsService
				.getAlarmSettingsByType(WarningType.PerformanceFluctuation.toString());
		if (null != settingsList && settingsList.size() > 0) {
			
			Calendar c = Calendar.getInstance();
			//当前年份
			int schoolYear = c.get(Calendar.YEAR);
			//当前月份
			int month = c.get(Calendar.MONTH);
			//当前学期编号
			int semester = 2;
			if( month > 2 && month < 9){
				semester = 1;
			}
			//上学期编号
			int secondSchoolYear = schoolYear;
			int secondSemester = 1;
			if(semester == 1){
				secondSemester = 2;
				secondSchoolYear = schoolYear-1;
			}
			
			//上上学期编号
			int firstSchoolYear = secondSchoolYear;
			int firstSemester = 1;
			if(secondSemester == 1){
				firstSemester = 2;
				firstSchoolYear = secondSchoolYear-1;
			}
			
			HashMap<Long, Long> alarmMap = new HashMap<Long,Long>();
			// 按orgId归类告警等级阀值
			for (AlarmSettings settings : settingsList) {

				Long orgId = settings.getOrgId();
				alarmMap.put(orgId, orgId);
			}

			//清除之前总评成绩不及格统计数据
//			totalScoreCountMongoRespository.deleteAll();
			Iterator iter = alarmMap.entrySet().iterator();
			while (iter.hasNext()) {

				List<ScoreFluctuateCount> scoreFluctuateCountList = new ArrayList<ScoreFluctuateCount>();
				HashMap<String,ScoreFluctuateCount> scoreFluctuateCountMap = new HashMap<String,ScoreFluctuateCount>();
				Map.Entry entry = (Map.Entry) iter.next();
				Long orgId = (Long) entry.getKey();

				List<Score> secondScoreList = scoreMongoRespository.findAllByTotalScoreGreaterThanEqualAndSchoolYearAndSemesterAndOrgIdAndExamType(ScoreConstant.PASS_SCORE_LINE, secondSchoolYear, secondSemester, orgId,ScoreConstant.EXAM_TYPE_COURSE);
				List<Score> firstScoreList = scoreMongoRespository.findAllByTotalScoreGreaterThanEqualAndSchoolYearAndSemesterAndOrgIdAndExamType(ScoreConstant.PASS_SCORE_LINE, firstSchoolYear, firstSemester, orgId,ScoreConstant.EXAM_TYPE_COURSE);
				HashMap<String,List<Score>> secondUserScoreMap = new HashMap<String,List<Score>>();
				HashMap<String,List<Score>> firstUserScoreMap = new HashMap<String,List<Score>>();
				//按学号分组学生上学期成绩信息
				for(Score score :secondScoreList){
					List<Score> scoreList = secondUserScoreMap.get(score.getJobNum());
					if(null == scoreList){
						scoreList = new ArrayList<Score>();
						scoreList.add(score);
						secondUserScoreMap.put(score.getJobNum(), scoreList);
					}else{
						scoreList.add(score);
						secondUserScoreMap.put(score.getJobNum(), scoreList);
					}
				}
				//按学号分组学生上上学期成绩信息
				for(Score score : firstScoreList){
					List<Score> scoreList = firstUserScoreMap.get(score.getJobNum());
					if(null == scoreList){
						scoreList = new ArrayList<Score>();
						scoreList.add(score);
						firstUserScoreMap.put(score.getJobNum(), scoreList);
					}else{
						scoreList.add(score);
						firstUserScoreMap.put(score.getJobNum(), scoreList);
					}
				}
				
				Iterator secondIter = secondUserScoreMap.entrySet().iterator();
				while (secondIter.hasNext()) {
					Map.Entry secondEntry = (Map.Entry) secondIter.next();
					String jobNum = (String)secondEntry.getKey();
					List<Score> userScoreList = (List<Score>)secondEntry.getValue();
					ScoreFluctuateCount scoreDomain = setSecondScoreDomainInfor(userScoreList,orgId,secondSchoolYear,secondSemester);
					if(null != scoreDomain){
						scoreFluctuateCountMap.put(jobNum, scoreDomain);
					}
				}
				
				Iterator firstIter = firstUserScoreMap.entrySet().iterator();
				while (firstIter.hasNext()) {
					Map.Entry firstEntry = (Map.Entry) firstIter.next();
					String jobNum = (String)firstEntry.getKey();
					List<Score> userScoreList = (List<Score>)firstEntry.getValue();
					ScoreFluctuateCount scoreDomain = scoreFluctuateCountMap.get(jobNum);
					if(null != scoreDomain){
					 scoreDomain = setFirstScoreDomainInfor(userScoreList, orgId, firstSchoolYear, firstSemester, scoreDomain);
					 if(!StringUtils.isEmpty(scoreDomain.getFirstAvgradePoint())){
						 scoreFluctuateCountMap.put(jobNum, scoreDomain);
					 }else{
						 scoreFluctuateCountMap.remove(jobNum);
					 }
					}
				}
				
				Iterator fluctuateCountIter = scoreFluctuateCountMap.entrySet().iterator();
				while (fluctuateCountIter.hasNext()) {
					Map.Entry fluctuateCountEntry = (Map.Entry) fluctuateCountIter.next();
					ScoreFluctuateCount scoreFluctuateCount = (ScoreFluctuateCount)fluctuateCountEntry.getValue();
					scoreFluctuateCountList.add(scoreFluctuateCount);
				}
				
				scoreFluctuateCountMongoRespository.save(scoreFluctuateCountList);
			}
		}
	}
	
	private ScoreFluctuateCount setSecondScoreDomainInfor(List<Score> userScoreList,Long orgId,int secondSchoolYear,int secondSemester) {
		
		ScoreFluctuateCount scoreDomain = new ScoreFluctuateCount();
		
			//保存不重复的考试课程编号
			HashMap<String,String> courseMap =  new HashMap<String,String>();
			BigDecimal totalScores = new BigDecimal(0);
			BigDecimal totalGradePoint = new BigDecimal(0);
			for(Score score : userScoreList){
				
				if(StringUtils.isEmpty(scoreDomain.getJobNum())){
					//复制用户基本信息
					scoreDomain.setClassId(score.getClassId());
					scoreDomain.setClassName(score.getClassName());
					scoreDomain.setCollegeId(score.getCollegeId());
					scoreDomain.setCollegeName(score.getCollegeName());
					scoreDomain.setJobNum(score.getJobNum());
					scoreDomain.setOrgId(orgId);
					scoreDomain.setProfessionalId(score.getProfessionalId());
					scoreDomain.setProfessionalName(score.getProfessionalName());
					scoreDomain.setUserId(score.getUserId());
					scoreDomain.setUserName(score.getUserName());
					scoreDomain.setUserPhoto(score.getUserPhoto());
					scoreDomain.setUserPhone(score.getUserPhone());
				}
				courseMap.put(score.getScheduleId(), score.getScheduleId());
				if(!StringUtils.isEmpty(score.getTotalScore())){
					totalScores = totalScores.add(new BigDecimal(score.getTotalScore()));
					if(!StringUtils.isEmpty(score.getGradePoint())){
						totalGradePoint = totalGradePoint.add(new BigDecimal(score.getGradePoint()));
					}
				}
			}
			if(courseMap.size() > 0){
				BigDecimal avgradePoint = new BigDecimal(totalGradePoint.doubleValue()/courseMap.size());
				scoreDomain.setSecondAvgradePoint(avgradePoint.setScale(2, RoundingMode.HALF_UP).toString());
				scoreDomain.setSecondSchoolYear(secondSchoolYear);
				scoreDomain.setSecondSemester(secondSemester);
				scoreDomain.setSecondTotalCourseNums(courseMap.size());
				scoreDomain.setSecondTotalGradePoint(totalGradePoint.toString());
				scoreDomain.setSecondTotalScores(totalScores.toString());
			}else{
				return null;
			}
			
			return scoreDomain;
	}
	
	/**
	 * 最近学期的相邻学期成绩信息获取
	 * 
	 */
	private ScoreFluctuateCount setFirstScoreDomainInfor(List<Score> userScoreList,Long orgId,int firstSchoolYear,int firstSemester,ScoreFluctuateCount scoreDomain) {
		
			//保存不重复的考试课程编号
			HashMap<String,String> courseMap =  new HashMap<String,String>();
			BigDecimal totalScores = new BigDecimal(0);
			BigDecimal totalGradePoint = new BigDecimal(0);
			for(Score score : userScoreList){
				courseMap.put(score.getScheduleId(), score.getScheduleId());
				
				if(!StringUtils.isEmpty(score.getTotalScore())){
					totalScores = totalScores.add(new BigDecimal(score.getTotalScore()));
					if(!StringUtils.isEmpty(score.getGradePoint())){
						totalGradePoint = totalGradePoint.add(new BigDecimal(score.getGradePoint()));
					}
				}
			}
			if(courseMap.size() > 0){
				BigDecimal avgradePoint = new BigDecimal(totalGradePoint.doubleValue()/courseMap.size());
				scoreDomain.setFirstAvgradePoint(avgradePoint.setScale(2, RoundingMode.HALF_UP).toString());
				scoreDomain.setFirstSchoolYear(firstSchoolYear);
				scoreDomain.setFirstSemester(firstSemester);
				scoreDomain.setFirstTotalCourseNums(courseMap.size());
				scoreDomain.setFirstTotalGradePoint(totalGradePoint.toString());
				scoreDomain.setFirstTotalScores(totalScores.toString());
			}
			
			return scoreDomain;
	}
	
	
	/**
	 * 成绩波动预警定时任务
	 */
	public void scoreFluctuateJob() {

		// 获取成绩波动预警配置
		List<AlarmSettings> settingsList = alarmSettingsService
				.getAlarmSettingsByType(WarningType.PerformanceFluctuation
						.toString());
		if (null != settingsList && settingsList.size() > 0) {
			
			HashMap<Long, ArrayList<AlarmSettings>> alarmMap = new HashMap<Long, ArrayList<AlarmSettings>>();
			Set<String> warnRuleIdList = new HashSet<String>();
			Set<String> warnSettingsIdList = new HashSet<String>();
			// 按orgId归类告警等级阀值
			for (AlarmSettings settings : settingsList) {

				warnSettingsIdList.add(settings.getId());
				Long orgId = settings.getOrgId();

				String[] warmRuleIds = settings.getRuleSet().split(",");
				for (String warmRuleId : warmRuleIds) {
					if (!StringUtils.isEmpty(warmRuleId)) {
						warnRuleIdList.add(warmRuleId);
					}
				}
				if (null != alarmMap.get(orgId)) {
					ArrayList<AlarmSettings> alarmList = alarmMap.get(orgId);
					alarmList.add(settings);
				} else {
					ArrayList<AlarmSettings> alarmList = new ArrayList<AlarmSettings>();
					alarmList.add(settings);
					alarmMap.put(orgId, alarmList);
				}
			}
			// 预警规则获取
			HashMap<String, AlarmRule> alarmRuleMap = new HashMap<String, AlarmRule>();
			List<AlarmRule> alarmList = alarmRuleService
					.getAlarmRuleByIds(warnRuleIdList);
			for (AlarmRule alarmRule : alarmList) {
				alarmRuleMap.put(alarmRule.getId(), alarmRule);
			}

			Iterator iter = alarmMap.entrySet().iterator();
		while (iter.hasNext()) {
			Map.Entry entry = (Map.Entry) iter.next();
			Long orgId = (Long) entry.getKey();
			ArrayList<AlarmSettings> val = (ArrayList<AlarmSettings>) entry
					.getValue();
			
			ArrayList<WarningInformation> alertInforList = new ArrayList<WarningInformation>();
			//获取成绩波动数据
			List<ScoreFluctuateCount> scoreFluctuateCountList = scoreFluctuateCountMongoRespository.findAllByOrgId(orgId);
			if (null != scoreFluctuateCountList && scoreFluctuateCountList.size() > 0) {
				for (ScoreFluctuateCount scoreFluctuateCount : scoreFluctuateCountList) {
					for (AlarmSettings alarmSettings : val) {
						AlarmRule alarmRule = alarmRuleMap
								.get(alarmSettings.getRuleSet());
						if (null != alarmRule) {
							 float result = Float.parseFloat(scoreFluctuateCount.getSecondAvgradePoint())-Float.parseFloat(scoreFluctuateCount.getFirstAvgradePoint());
							 //上学期平均绩点小于上上学期平均绩点时
							 if(result < 0){
								 result = Math.abs(result);
								 if (result >= Float.parseFloat(String.valueOf(alarmRule.getRightParameter()))) {
										WarningInformation alertInfor = new WarningInformation();
										String alertId = UUID.randomUUID()
												.toString();
										alertInfor.setId(alertId);
										alertInfor.setDefendantId(scoreFluctuateCount
												.getUserId());
										alertInfor.setName(scoreFluctuateCount
												.getUserName());
										alertInfor.setJobNumber(scoreFluctuateCount
												.getJobNum());
										alertInfor.setCollogeId(scoreFluctuateCount
												.getCollegeId());
										alertInfor.setCollogeName(scoreFluctuateCount
												.getCollegeName());
										alertInfor.setClassId(scoreFluctuateCount
												.getClassId());
										alertInfor.setClassName(scoreFluctuateCount
												.getClassName());
										alertInfor
												.setProfessionalId(scoreFluctuateCount
														.getProfessionalId());
										alertInfor
												.setProfessionalName(scoreFluctuateCount
														.getProfessionalName());
										alertInfor.setWarningLevel(alarmSettings
												.getWarningLevel());
										alertInfor
												.setWarningState(AlertTypeConstant.ALERT_IN_PROCESS);
										alertInfor.setAlarmSettingsId(alarmSettings
												.getId());
										alertInfor
												.setWarningType(WarningType.PerformanceFluctuation
														.toString());
										alertInfor.setWarningTime(new Date());
										alertInfor.setPhone(scoreFluctuateCount.getUserPhone());
										alertInfor.setOrgId(alarmRule.getOrgId());
										alertInforList.add(alertInfor);

										break;
									} else {
										continue;
									}
							 }
						}
					}
				}
			}
			if (!alertInforList.isEmpty()) {
				alertWarningInformationService.save(alertInforList);
			}
		}
		}

	}
	
	/**
	 * 统计mongo里的上学期不及格成绩汇总到
	 */
	public void totalScoreCountJob() {

		// 获取预警配置
		List<AlarmSettings> settingsList = alarmSettingsService
				.getAlarmSettingsByType(WarningType.TotalAchievement.toString());
		if (null != settingsList && settingsList.size() > 0) {
			
			Calendar c = Calendar.getInstance();
			//当前年份
			int schoolYear = c.get(Calendar.YEAR);
			//当前月份
			int month = c.get(Calendar.MONTH);
			//当前学期编号
			int semester = 2;
			if( month > 2 && month < 9){
				semester = 1;
			}
			//上学期编号
			int lastSemester = 1;
			if(semester == 1){
				lastSemester = 2;
				schoolYear = schoolYear-1;
			}
			
			HashMap<Long, Long> alarmMap = new HashMap<Long,Long>();
			// 按orgId归类告警等级阀值
			for (AlarmSettings settings : settingsList) {

				Long orgId = settings.getOrgId();
				alarmMap.put(orgId, orgId);
			}

			//清除之前总评成绩不及格统计数据
//			totalScoreCountMongoRespository.deleteAll();
			Iterator iter = alarmMap.entrySet().iterator();
			while (iter.hasNext()) {

				List<TotalScoreCount> totalScoreCountList = new ArrayList<TotalScoreCount>();
				HashMap<String,TotalScoreCount> totalScoreCountMap = new HashMap<String,TotalScoreCount>();
				Map.Entry entry = (Map.Entry) iter.next();
				Long orgId = (Long) entry.getKey();

				List<Score> scoreList = scoreMongoRespository.findAllByTotalScoreLessThanAndSchoolYearAndSemesterAndOrgIdAndExamType(ScoreConstant.PASS_SCORE_LINE, schoolYear, lastSemester, orgId,ScoreConstant.EXAM_TYPE_COURSE);
				for(Score score : scoreList){
					TotalScoreCount totalScoreCount = totalScoreCountMap.get(score.getJobNum());
					if(null == totalScoreCount){
						totalScoreCount = new TotalScoreCount();
						totalScoreCount.setClassId(score.getClassId());
						totalScoreCount.setClassName(score.getClassName());
						totalScoreCount.setCollegeId(score.getCollegeId());
						totalScoreCount.setCollegeName(score.getCollegeName());
						totalScoreCount.setGrade(score.getGrade());
						totalScoreCount.setJobNum(score.getJobNum());
						totalScoreCount.setOrgId(orgId);
						totalScoreCount.setProfessionalId(score.getProfessionalId());
						totalScoreCount.setProfessionalName(score.getProfessionalName());
						totalScoreCount.setSchoolYear(schoolYear);
						totalScoreCount.setSemester(lastSemester);
						totalScoreCount.setUserName(score.getUserName());
						totalScoreCount.setUserPhone(score.getUserPhone());
						totalScoreCount.setFailCourseNum(1);
						if(!StringUtils.isEmpty(score.getCourseType()) && ScoreConstant.REQUIRED_COURSE.equals(score.getCourseType())){
							totalScoreCount.setFailRequiredCourseNum(1);
							totalScoreCount.setRequireCreditCount(score.getCredit());
						}
						totalScoreCountMap.put(score.getJobNum(), totalScoreCount);
					}else{
						if(!StringUtils.isEmpty(score.getCourseType()) && ScoreConstant.REQUIRED_COURSE.equals(score.getCourseType())){
							totalScoreCount.setFailRequiredCourseNum(totalScoreCount.getFailRequiredCourseNum()+1);
							totalScoreCount.setRequireCreditCount(totalScoreCount.getRequireCreditCount()+score.getCredit());
						}
						totalScoreCount.setFailCourseNum(totalScoreCount.getFailCourseNum()+1);
						totalScoreCountMap.put(score.getJobNum(), totalScoreCount);
					}
				}
				
				Iterator totalScoreiter = totalScoreCountMap.entrySet().iterator();
				while (totalScoreiter.hasNext()) {
					Map.Entry totalScoreEntry = (Map.Entry) totalScoreiter.next();
					TotalScoreCount totalScoreCount = (TotalScoreCount)totalScoreEntry.getValue();
					if(totalScoreCount.getFailCourseNum() > 0){
						totalScoreCountList.add(totalScoreCount);
					}
				}
				
				totalScoreCountMongoRespository.save(totalScoreCountList);
			}
		}
	}

	/**
	 * 总评成绩预警
	 */
	public void totalScoreJob() {

		// 获取预警配置
		List<AlarmSettings> settingsList = alarmSettingsService
				.getAlarmSettingsByType(WarningType.TotalAchievement.toString());
		if (null != settingsList && settingsList.size() > 0) {
			
			Calendar c = Calendar.getInstance();
			//当前年份
			int schoolYear = c.get(Calendar.YEAR);
			//当前月份
			int month = c.get(Calendar.MONTH);
			//当前学期编号
			int semester = 2;
			if( month > 2 && month < 9){
				semester = 1;
			}
			
			//上学期编号
			int lastSemester = 1;
			if(semester == 1){
				lastSemester = 2;
				schoolYear = schoolYear-1;
			}
			
			HashMap<Long, ArrayList<AlarmSettings>> alarmMap = new HashMap<Long, ArrayList<AlarmSettings>>();
			Set<String> warnRuleIdList = new HashSet<String>();
			Set<String> warnSettingsIdList = new HashSet<String>();
			// 按orgId归类告警等级阀值
			for (AlarmSettings settings : settingsList) {

				warnSettingsIdList.add(settings.getId());
				Long orgId = settings.getOrgId();

				String[] warmRuleIds = settings.getRuleSet().split(",");
				for (String warmRuleId : warmRuleIds) {
					if (!StringUtils.isEmpty(warmRuleId)) {
						warnRuleIdList.add(warmRuleId);
					}
				}
				if (null != alarmMap.get(orgId)) {
					ArrayList<AlarmSettings> alarmList = alarmMap.get(orgId);
					alarmList.add(settings);
				} else {
					ArrayList<AlarmSettings> alarmList = new ArrayList<AlarmSettings>();
					alarmList.add(settings);
					alarmMap.put(orgId, alarmList);
				}
			}
			// 预警规则获取
			HashMap<String, AlarmRule> alarmRuleMap = new HashMap<String, AlarmRule>();
			List<AlarmRule> alarmList = alarmRuleService
					.getAlarmRuleByIds(warnRuleIdList);
			for (AlarmRule alarmRule : alarmList) {
				alarmRuleMap.put(alarmRule.getId(), alarmRule);
			}

			Iterator iter = alarmMap.entrySet().iterator();
			while (iter.hasNext()) {

				//更新预警集合
				ArrayList<WarningInformation> alertInforList = new ArrayList<WarningInformation>();
				// 定时任务产生的新的预警数据
				HashMap<String, WarningInformation> warnMap = new HashMap<String, WarningInformation>();
				Map.Entry entry = (Map.Entry) iter.next();
				Long orgId = (Long) entry.getKey();
				ArrayList<AlarmSettings> val = (ArrayList<AlarmSettings>) entry
						.getValue();

				// 预警处理配置获取
//				HashMap<String, ProcessingMode> processingModeMap = new HashMap<String, ProcessingMode>();
//				List<ProcessingMode> processingModeList = processingModeService
//						.getProcessingModeBywarningTypeId(orgId,
//								WarningType.TotalAchievement.toString());
				// 按orgId查询未报到的学生信息
				List<TotalScoreCount> totalScoreCountList = totalScoreCountMongoRespository.findAllBySchoolYearAndSemesterAndOrgId(schoolYear, lastSemester, orgId);
				
				if (null != totalScoreCountList && totalScoreCountList.size() > 0) {
					Date today = new Date();
					for (TotalScoreCount totalScoreCount : totalScoreCountList) {
						for (AlarmSettings alarmSettings : val) {
							AlarmRule alarmRule = alarmRuleMap
									.get(alarmSettings.getRuleSet());
							if (null != alarmRule) {
								if (totalScoreCount.getFailCourseNum() >= alarmRule.getRightParameter()) {
									WarningInformation alertInfor = new WarningInformation();
									String alertId = UUID.randomUUID()
											.toString();
									alertInfor.setId(alertId);
									alertInfor.setDefendantId(totalScoreCount
											.getUserId());
									alertInfor.setName(totalScoreCount
											.getUserName());
									alertInfor.setJobNumber(totalScoreCount
											.getJobNum());
									alertInfor.setCollogeId(totalScoreCount
											.getCollegeId());
									alertInfor.setCollogeName(totalScoreCount
											.getCollegeName());
									alertInfor.setClassId(totalScoreCount
											.getClassId());
									alertInfor.setClassName(totalScoreCount
											.getClassName());
									alertInfor
											.setProfessionalId(totalScoreCount
													.getProfessionalId());
									alertInfor
											.setProfessionalName(totalScoreCount
													.getProfessionalName());
									alertInfor.setTeachingYear(String.valueOf(totalScoreCount
											.getSchoolYear()));
									alertInfor.setWarningLevel(alarmSettings
											.getWarningLevel());
									alertInfor
											.setWarningState(AlertTypeConstant.ALERT_IN_PROCESS);
									alertInfor.setAlarmSettingsId(alarmSettings
											.getId());
									alertInfor
											.setWarningType(WarningType.TotalAchievement
													.toString());
									alertInfor.setWarningTime(new Date());
									alertInfor.setPhone(totalScoreCount.getUserPhone());
									alertInfor.setOrgId(alarmRule.getOrgId());
									alertInforList.add(alertInfor);

									break;
								} else {
									continue;
								}
							}
						}
					}
				}
				if (!alertInforList.isEmpty()) {
					alertWarningInformationService.save(alertInforList);
				}
			}
		}
	}
	
	/**
	 * 修读异常预警
	 */
	public void attendAbnormalJob() {

		// 获取预警配置
		List<AlarmSettings> settingsList = alarmSettingsService
				.getAlarmSettingsByType(WarningType.AttendAbnormal.toString());
		if (null != settingsList && settingsList.size() > 0) {
			
			Calendar c = Calendar.getInstance();
			//当前年份
			int schoolYear = c.get(Calendar.YEAR);
			//当前月份
			int month = c.get(Calendar.MONTH);
			//当前学期编号
			int semester = 2;
			if( month > 2 && month < 9){
				semester = 1;
			}
			
			//上学期编号
			int lastSemester = 1;
			if(semester == 1){
				lastSemester = 2;
				schoolYear = schoolYear-1;
			}
			
			HashMap<Long, ArrayList<AlarmSettings>> alarmMap = new HashMap<Long, ArrayList<AlarmSettings>>();
			Set<String> warnRuleIdList = new HashSet<String>();
			Set<String> warnSettingsIdList = new HashSet<String>();
			// 按orgId归类告警等级阀值
			for (AlarmSettings settings : settingsList) {

				warnSettingsIdList.add(settings.getId());
				Long orgId = settings.getOrgId();

				String[] warmRuleIds = settings.getRuleSet().split(",");
				for (String warmRuleId : warmRuleIds) {
					if (!StringUtils.isEmpty(warmRuleId)) {
						warnRuleIdList.add(warmRuleId);
					}
				}
				if (null != alarmMap.get(orgId)) {
					ArrayList<AlarmSettings> alarmList = alarmMap.get(orgId);
					alarmList.add(settings);
				} else {
					ArrayList<AlarmSettings> alarmList = new ArrayList<AlarmSettings>();
					alarmList.add(settings);
					alarmMap.put(orgId, alarmList);
				}
			}
			// 预警规则获取
			HashMap<String, AlarmRule> alarmRuleMap = new HashMap<String, AlarmRule>();
			List<AlarmRule> alarmList = alarmRuleService
					.getAlarmRuleByIds(warnRuleIdList);
			for (AlarmRule alarmRule : alarmList) {
				alarmRuleMap.put(alarmRule.getId(), alarmRule);
			}

			Iterator iter = alarmMap.entrySet().iterator();
			while (iter.hasNext()) {

				//更新预警集合
				ArrayList<WarningInformation> alertInforList = new ArrayList<WarningInformation>();
				// 定时任务产生的新的预警数据
				HashMap<String, WarningInformation> warnMap = new HashMap<String, WarningInformation>();
				Map.Entry entry = (Map.Entry) iter.next();
				Long orgId = (Long) entry.getKey();
				ArrayList<AlarmSettings> val = (ArrayList<AlarmSettings>) entry
						.getValue();

				// 预警处理配置获取
//				HashMap<String, ProcessingMode> processingModeMap = new HashMap<String, ProcessingMode>();
//				List<ProcessingMode> processingModeList = processingModeService
//						.getProcessingModeBywarningTypeId(orgId,
//								WarningType.TotalAchievement.toString());
				// 按orgId查询未报到的学生信息
				List<TotalScoreCount> totalScoreCountList = totalScoreCountMongoRespository.findAllBySchoolYearAndSemesterAndOrgId(schoolYear, lastSemester, orgId);
				
				if (null != totalScoreCountList && totalScoreCountList.size() > 0) {
					Date today = new Date();
					for (TotalScoreCount totalScoreCount : totalScoreCountList) {
						for (AlarmSettings alarmSettings : val) {
							AlarmRule alarmRule = alarmRuleMap
									.get(alarmSettings.getRuleSet());
							if (null != alarmRule) {
								if (Float.parseFloat(totalScoreCount.getRequireCreditCount()) >= Float.parseFloat(String.valueOf(alarmRule.getRightParameter()))) {
									WarningInformation alertInfor = new WarningInformation();
									String alertId = UUID.randomUUID()
											.toString();
									alertInfor.setId(alertId);
									alertInfor.setDefendantId(totalScoreCount
											.getUserId());
									alertInfor.setName(totalScoreCount
											.getUserName());
									alertInfor.setJobNumber(totalScoreCount
											.getJobNum());
									alertInfor.setCollogeId(totalScoreCount
											.getCollegeId());
									alertInfor.setCollogeName(totalScoreCount
											.getCollegeName());
									alertInfor.setClassId(totalScoreCount
											.getClassId());
									alertInfor.setClassName(totalScoreCount
											.getClassName());
									alertInfor
											.setProfessionalId(totalScoreCount
													.getProfessionalId());
									alertInfor
											.setProfessionalName(totalScoreCount
													.getProfessionalName());
									alertInfor.setTeachingYear(String.valueOf(totalScoreCount
											.getSchoolYear()));
									alertInfor.setWarningLevel(alarmSettings
											.getWarningLevel());
									alertInfor
											.setWarningState(AlertTypeConstant.ALERT_IN_PROCESS);
									alertInfor.setAlarmSettingsId(alarmSettings
											.getId());
									alertInfor
											.setWarningType(WarningType.AttendAbnormal
													.toString());
									alertInfor.setWarningTime(new Date());
									alertInfor.setPhone(totalScoreCount.getUserPhone());
									alertInfor.setOrgId(alarmRule.getOrgId());
									alertInforList.add(alertInfor);

									break;
								} else {
									continue;
								}
							}
						}
					}
				}
				if (!alertInforList.isEmpty()) {
					alertWarningInformationService.save(alertInforList);
				}
			}
		}
	}
	
	
	/**
	 * 统计mongo里的补考不及格成绩汇总到
	 */
	public void makeUpScoreCountJob() {

		// 获取预警配置
		List<AlarmSettings> settingsList = alarmSettingsService
				.getAlarmSettingsByType(WarningType.SupplementAchievement.toString());
		if (null != settingsList && settingsList.size() > 0) {
			
			Calendar c = Calendar.getInstance();
			//当前年份
			int endYear = c.get(Calendar.YEAR);
			//前三年
			int beginYear = endYear-3;
			
			HashMap<Long, Long> alarmMap = new HashMap<Long,Long>();
			// 按orgId归类告警等级阀值
			for (AlarmSettings settings : settingsList) {

				Long orgId = settings.getOrgId();
				alarmMap.put(orgId, orgId);
			}

			//清除之前补考统计数据
//			makeUpScoreCountMongoRespository.deleteAll();
			Iterator iter = alarmMap.entrySet().iterator();
			while (iter.hasNext()) {

				List<MakeUpScoreCount> makeUpScoreCountList = new ArrayList<MakeUpScoreCount>();
				HashMap<String,MakeUpScoreCount> makeUpScoreCountMap = new HashMap<String,MakeUpScoreCount>();
				Map.Entry entry = (Map.Entry) iter.next();
				Long orgId = (Long) entry.getKey();

				HashMap<String,HashMap<String,ArrayList<Score>>> scoreMap = new HashMap<String,HashMap<String,ArrayList<Score>>>();
				List<Score> scoreList = scoreMongoRespository.findAllBySchoolYearGreaterThanEqualAndOrgId(beginYear,orgId);
				for(Score score : scoreList){
					HashMap<String,ArrayList<Score>> userScoreMap = scoreMap.get(score.getJobNum());
					if(null == userScoreMap){
						ArrayList<Score> userScoreList = new ArrayList<Score>();
						userScoreList.add(score);
						userScoreMap = new HashMap<String,ArrayList<Score>>();
						userScoreMap.put(score.getScheduleId(), userScoreList);
						scoreMap.put(score.getJobNum(), userScoreMap);
					}else{
						ArrayList<Score> userScoreList =  userScoreMap.get(score.getScheduleId());
						if(null == userScoreList){
							userScoreList = new ArrayList<Score>();
							userScoreList.add(score);
							userScoreMap.put(score.getScheduleId(), userScoreList);
							scoreMap.put(score.getJobNum(), userScoreMap);
						}else{
							userScoreList.add(score);
							userScoreMap.put(score.getScheduleId(), userScoreList);
							scoreMap.put(score.getJobNum(), userScoreMap);
						}
					}
				}
				
				Iterator scoreiter = scoreMap.entrySet().iterator();
				while (scoreiter.hasNext()) {
					Map.Entry scoreEntry = (Map.Entry) scoreiter.next();
					HashMap<String,ArrayList<Score>> courseScoreMap =(HashMap<String,ArrayList<Score>>) scoreEntry.getValue();
					Iterator courseScoreiter = courseScoreMap.entrySet().iterator();
					while (courseScoreiter.hasNext()) {
						Map.Entry courseScoreEntry = (Map.Entry) courseScoreiter.next();
						ArrayList<Score> courseScoreList =(ArrayList<Score>) courseScoreEntry.getValue();
						float totalGradePoint = 0.0f;
						for(Score score :courseScoreList){
							if(!StringUtils.isEmpty(score.getGradePoint())){
								totalGradePoint += Float.parseFloat(score.getGradePoint());
							}
						}
						if(totalGradePoint < 1.0f){
							Score score = courseScoreList.get(0);
							MakeUpScoreCount makeUpScoreCount = makeUpScoreCountMap.get(score.getJobNum());
							if(null == makeUpScoreCount){
								makeUpScoreCount = new MakeUpScoreCount();
								makeUpScoreCount.setClassId(score.getClassId());
								makeUpScoreCount.setClassName(score.getClassName());
								makeUpScoreCount.setCollegeId(score.getCollegeId());
								makeUpScoreCount.setCollegeName(score.getCollegeName());
								makeUpScoreCount.setJobNum(score.getJobNum());
								makeUpScoreCount.setOrgId(orgId);
								makeUpScoreCount.setProfessionalId(score.getProfessionalId());
								makeUpScoreCount.setProfessionalName(score.getProfessionalName());
								makeUpScoreCount.setUserName(score.getUserName());
								makeUpScoreCount.setUserPhone(score.getUserPhone());
								makeUpScoreCount.setFailCourseNum(1);
								makeUpScoreCountMap.put(score.getJobNum(), makeUpScoreCount);
							}else{
								makeUpScoreCount.setFailCourseNum(makeUpScoreCount.getFailCourseNum()+1);
								makeUpScoreCountMap.put(score.getJobNum(), makeUpScoreCount);
							}
							
						}
					}
				}
				
				Iterator makeUpScoreCountiter = makeUpScoreCountMap.entrySet().iterator();
				while (makeUpScoreCountiter.hasNext()) {
					Map.Entry makeUpScoreCountEntry = (Map.Entry) makeUpScoreCountiter.next();
					MakeUpScoreCount makeUpScoreCount = (MakeUpScoreCount)makeUpScoreCountEntry.getValue();
					if(makeUpScoreCount.getFailCourseNum() > 0){
						makeUpScoreCountList.add(makeUpScoreCount);
					}
				}
				
				makeUpScoreCountMongoRespository.save(makeUpScoreCountList);
			}
		}
	}

	/**
	 * 总评成绩预警
	 */
	public void makeUpScoreJob() {

		// 获取预警配置
		List<AlarmSettings> settingsList = alarmSettingsService
				.getAlarmSettingsByType(WarningType.SupplementAchievement.toString());
		if (null != settingsList && settingsList.size() > 0) {
			
			
			HashMap<Long, ArrayList<AlarmSettings>> alarmMap = new HashMap<Long, ArrayList<AlarmSettings>>();
			Set<String> warnRuleIdList = new HashSet<String>();
			Set<String> warnSettingsIdList = new HashSet<String>();
			// 按orgId归类告警等级阀值
			for (AlarmSettings settings : settingsList) {

				warnSettingsIdList.add(settings.getId());
				Long orgId = settings.getOrgId();

				String[] warmRuleIds = settings.getRuleSet().split(",");
				for (String warmRuleId : warmRuleIds) {
					if (!StringUtils.isEmpty(warmRuleId)) {
						warnRuleIdList.add(warmRuleId);
					}
				}
				if (null != alarmMap.get(orgId)) {
					ArrayList<AlarmSettings> alarmList = alarmMap.get(orgId);
					alarmList.add(settings);
				} else {
					ArrayList<AlarmSettings> alarmList = new ArrayList<AlarmSettings>();
					alarmList.add(settings);
					alarmMap.put(orgId, alarmList);
				}
			}
			// 预警规则获取
			HashMap<String, AlarmRule> alarmRuleMap = new HashMap<String, AlarmRule>();
			List<AlarmRule> alarmList = alarmRuleService
					.getAlarmRuleByIds(warnRuleIdList);
			for (AlarmRule alarmRule : alarmList) {
				alarmRuleMap.put(alarmRule.getId(), alarmRule);
			}

			Iterator iter = alarmMap.entrySet().iterator();
			while (iter.hasNext()) {

				//更新预警集合
				ArrayList<WarningInformation> alertInforList = new ArrayList<WarningInformation>();
				// 定时任务产生的新的预警数据
				HashMap<String, WarningInformation> warnMap = new HashMap<String, WarningInformation>();
				Map.Entry entry = (Map.Entry) iter.next();
				Long orgId = (Long) entry.getKey();
				ArrayList<AlarmSettings> val = (ArrayList<AlarmSettings>) entry
						.getValue();

				// 预警处理配置获取
//				HashMap<String, ProcessingMode> processingModeMap = new HashMap<String, ProcessingMode>();
//				List<ProcessingMode> processingModeList = processingModeService
//						.getProcessingModeBywarningTypeId(orgId,
//								WarningType.TotalAchievement.toString());
				// 按orgId查询未报到的学生信息
				List<MakeUpScoreCount> makeUpScoreCountList = makeUpScoreCountMongoRespository.findAllByOrgId(orgId);
				
				if (null != makeUpScoreCountList && makeUpScoreCountList.size() > 0) {
					for (MakeUpScoreCount makeUpScoreCount : makeUpScoreCountList) {
						for (AlarmSettings alarmSettings : val) {
							AlarmRule alarmRule = alarmRuleMap
									.get(alarmSettings.getRuleSet());
							if (null != alarmRule) {
								if (makeUpScoreCount.getFailCourseNum() >= alarmRule.getRightParameter()) {
									WarningInformation alertInfor = new WarningInformation();
									String alertId = UUID.randomUUID()
											.toString();
									alertInfor.setId(alertId);
									alertInfor.setDefendantId(makeUpScoreCount
											.getUserId());
									alertInfor.setName(makeUpScoreCount
											.getUserName());
									alertInfor.setJobNumber(makeUpScoreCount
											.getJobNum());
									alertInfor.setCollogeId(makeUpScoreCount
											.getCollegeId());
									alertInfor.setCollogeName(makeUpScoreCount
											.getCollegeName());
									alertInfor.setClassId(makeUpScoreCount
											.getClassId());
									alertInfor.setClassName(makeUpScoreCount
											.getClassName());
									alertInfor
											.setProfessionalId(makeUpScoreCount
													.getProfessionalId());
									alertInfor
											.setProfessionalName(makeUpScoreCount
													.getProfessionalName());
									alertInfor.setWarningLevel(alarmSettings
											.getWarningLevel());
									alertInfor
											.setWarningState(AlertTypeConstant.ALERT_IN_PROCESS);
									alertInfor.setAlarmSettingsId(alarmSettings
											.getId());
									alertInfor
											.setWarningType(WarningType.SupplementAchievement
													.toString());
									alertInfor.setWarningTime(new Date());
									alertInfor.setPhone(makeUpScoreCount.getUserPhone());
									alertInfor.setOrgId(alarmRule.getOrgId());
									alertInforList.add(alertInfor);

									break;
								} else {
									continue;
								}
							}
						}
					}
				}
				if (!alertInforList.isEmpty()) {
					alertWarningInformationService.save(alertInforList);
				}
			}
		}
	}
	
	
	/**
	 * 英语四级考试成绩未通过预警
	 */
	public void cet4ScoreJob() {

		// 获取预警配置
		List<AlarmSettings> settingsList = alarmSettingsService
				.getAlarmSettingsByType(WarningType.Cet.toString());
		if (null != settingsList && settingsList.size() > 0) {
			
			Calendar c = Calendar.getInstance();
			//当前年份
			int nowYear = c.get(Calendar.YEAR);
			String grade2 = String.valueOf(nowYear-1);
			String grade3 = String.valueOf(nowYear-2);
			String grade4 = String.valueOf(nowYear-3);
			String[] grades = new String[]{grade2,grade3,grade4};
			HashMap<String,Integer> gradeMap = new HashMap<String,Integer>();
			gradeMap.put(grade2,2);
			gradeMap.put(grade3,3);
			gradeMap.put(grade4,4);
			
			HashMap<Long, ArrayList<AlarmSettings>> alarmMap = new HashMap<Long, ArrayList<AlarmSettings>>();
			Set<String> warnRuleIdList = new HashSet<String>();
			Set<String> warnSettingsIdList = new HashSet<String>();
			// 按orgId归类告警等级阀值
			for (AlarmSettings settings : settingsList) {

				warnSettingsIdList.add(settings.getId());
				Long orgId = settings.getOrgId();

				String[] warmRuleIds = settings.getRuleSet().split(",");
				for (String warmRuleId : warmRuleIds) {
					if (!StringUtils.isEmpty(warmRuleId)) {
						warnRuleIdList.add(warmRuleId);
					}
				}
				if (null != alarmMap.get(orgId)) {
					ArrayList<AlarmSettings> alarmList = alarmMap.get(orgId);
					alarmList.add(settings);
				} else {
					ArrayList<AlarmSettings> alarmList = new ArrayList<AlarmSettings>();
					alarmList.add(settings);
					alarmMap.put(orgId, alarmList);
				}
			}
			// 预警规则获取
			HashMap<String, AlarmRule> alarmRuleMap = new HashMap<String, AlarmRule>();
			List<AlarmRule> alarmList = alarmRuleService
					.getAlarmRuleByIds(warnRuleIdList);
			for (AlarmRule alarmRule : alarmList) {
				alarmRuleMap.put(alarmRule.getId(), alarmRule);
			}

			Iterator iter = alarmMap.entrySet().iterator();
			while (iter.hasNext()) {

				//更新预警集合
				ArrayList<WarningInformation> alertInforList = new ArrayList<WarningInformation>();
				Map.Entry entry = (Map.Entry) iter.next();
				Long orgId = (Long) entry.getKey();
				ArrayList<AlarmSettings> val = (ArrayList<AlarmSettings>) entry
						.getValue();

				// 预警处理配置获取
//				HashMap<String, ProcessingMode> processingModeMap = new HashMap<String, ProcessingMode>();
//				List<ProcessingMode> processingModeList = processingModeService
//						.getProcessingModeBywarningTypeId(orgId,
//								WarningType.TotalAchievement.toString());
				// 按orgId查询成绩信息		
				List<Score> scoreList = scoreMongoRespository.findAllByGradeInAndOrgIdAndExamType( grades, orgId, ScoreConstant.EXAM_TYPE_CET4);
				HashMap<String,Score> userScoreMap = new HashMap<String,Score>();
				//去重后的英语四级不合格成绩集合
				List<Score> alarmScoreList = new ArrayList<Score>();
				
				for(Score score : scoreList){
					if(StringUtils.isEmpty(score.getTotalScore())){
						continue;
					}
					Score tmpScore = userScoreMap.get(score.getJobNum());
					if(null != tmpScore){
						if(Float.parseFloat(score.getTotalScore()) > Float.parseFloat(tmpScore.getTotalScore())){
							userScoreMap.put(score.getJobNum(), score);
						}
					}else{
						userScoreMap.put(score.getJobNum(), score);
					}
				}
				
				Iterator userScoreMapIter = userScoreMap.entrySet().iterator();
				while (userScoreMapIter.hasNext()) {
					Map.Entry userScoreEntry = (Map.Entry) userScoreMapIter.next();
					Score score = (Score) userScoreEntry
							.getValue();
					
					if(Float.parseFloat(score.getTotalScore()) < Float.parseFloat(ScoreConstant.CET_PASS_SCORE_LINE)){
						alarmScoreList.add(score);
					}
				}
				
				if (null != alarmScoreList && alarmScoreList.size() > 0) {
					for (Score score : alarmScoreList) {
						for (AlarmSettings alarmSettings : val) {
							AlarmRule alarmRule = alarmRuleMap
									.get(alarmSettings.getRuleSet());
							if (null != alarmRule) {
								int grade = 0;
								if(null != gradeMap.get(score.getGrade())){
									grade = gradeMap.get(score.getGrade()).intValue();
								}
								if (grade == alarmRule.getRightParameter()) {
									WarningInformation alertInfor = new WarningInformation();
									String alertId = UUID.randomUUID()
											.toString();
									alertInfor.setId(alertId);
									alertInfor.setDefendantId(score
											.getUserId());
									alertInfor.setName(score
											.getUserName());
									alertInfor.setJobNumber(score
											.getJobNum());
									alertInfor.setCollogeId(score
											.getCollegeId());
									alertInfor.setCollogeName(score
											.getCollegeName());
									alertInfor.setClassId(score
											.getClassId());
									alertInfor.setClassName(score
											.getClassName());
									alertInfor
											.setProfessionalId(score
													.getProfessionalId());
									alertInfor
											.setProfessionalName(score
													.getProfessionalName());
									alertInfor.setWarningLevel(alarmSettings
											.getWarningLevel());
									alertInfor
											.setWarningState(AlertTypeConstant.ALERT_IN_PROCESS);
									alertInfor.setAlarmSettingsId(alarmSettings
											.getId());
									alertInfor
											.setWarningType(WarningType.Cet
													.toString());
									alertInfor.setWarningTime(new Date());
									alertInfor.setPhone(score.getUserPhone());
									alertInfor.setOrgId(alarmRule.getOrgId());
									alertInforList.add(alertInfor);

									break;
								} else {
									continue;
								}
							}
						}
					}
				}
				if (!alertInforList.isEmpty()) {
					alertWarningInformationService.save(alertInforList);
				}
			}
		}
	}
}
