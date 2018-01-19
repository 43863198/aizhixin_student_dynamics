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

import com.aizhixin.cloud.dataanalysis.common.util.TermConversion;
import com.aizhixin.cloud.dataanalysis.setup.entity.WarningType;
import com.aizhixin.cloud.dataanalysis.setup.service.WarningTypeService;
import com.mongodb.BasicDBObject;
import net.sf.json.JSONObject;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.aizhixin.cloud.dataanalysis.alertinformation.entity.WarningInformation;
import com.aizhixin.cloud.dataanalysis.alertinformation.service.AlertWarningInformationService;
import com.aizhixin.cloud.dataanalysis.analysis.entity.TeachingScoreDetails;
import com.aizhixin.cloud.dataanalysis.analysis.service.TeachingScoreService;
import com.aizhixin.cloud.dataanalysis.common.constant.AlertTypeConstant;
import com.aizhixin.cloud.dataanalysis.common.constant.DataValidity;
import com.aizhixin.cloud.dataanalysis.common.constant.ScoreConstant;
import com.aizhixin.cloud.dataanalysis.common.constant.WarningTypeConstant;
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

	private Logger logger = Logger.getLogger(this.getClass());
	@Autowired
	private ScoreService scoreService;
	@Autowired
	private AlarmRuleService alarmRuleService;
	@Autowired
	@Lazy
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
	@Autowired
	private TeachingScoreService teachingScoreService;
	@Autowired
	private TermConversion termConversion;
	@Autowired
	private WarningTypeService warningTypeService;

	/**
	 * 统计mongo里的相邻学期平均绩点
	 */
	public void scoreFluctuateCountJob() {

		// 获取预警配置
		List<AlarmSettings> settingsList = alarmSettingsService
				.getAlarmSettingsByType(WarningTypeConstant.PerformanceFluctuation
						.toString());
		if (null != settingsList && settingsList.size() > 0) {

			Calendar c = Calendar.getInstance();
			// 当前年份
			int schoolYear = c.get(Calendar.YEAR);
			// 当前月份
			int month = c.get(Calendar.MONTH)+1;
			// 当前学期编号
			int semester = 2;
			if (month > 1 && month < 9) {
				semester = 1;
			}
			if(month == 1 ){
				schoolYear = schoolYear - 1;
			}
			// 上学期编号
			int secondSchoolYear = schoolYear;
			int secondSemester = 1;
			if (semester == 1) {
				secondSemester = 2;
				secondSchoolYear = schoolYear - 1;
			}

			// 上上学期编号
			int firstSchoolYear = secondSchoolYear;
			int firstSemester = 1;
			if (secondSemester == 1) {
				firstSemester = 2;
				firstSchoolYear = secondSchoolYear - 1;
			}

			HashMap<Long, Long> alarmMap = new HashMap<Long, Long>();
			// 按orgId归类告警等级阀值
			for (AlarmSettings settings : settingsList) {

				Long orgId = settings.getOrgId();
				alarmMap.put(orgId, orgId);
			}

			// 清除之前总评成绩不及格统计数据
			scoreFluctuateCountMongoRespository.deleteAll();
			Iterator iter = alarmMap.entrySet().iterator();
			while (iter.hasNext()) {

				List<ScoreFluctuateCount> scoreFluctuateCountList = new ArrayList<ScoreFluctuateCount>();
				HashMap<String, ScoreFluctuateCount> scoreFluctuateCountMap = new HashMap<String, ScoreFluctuateCount>();
				Map.Entry entry = (Map.Entry) iter.next();
				Long orgId = (Long) entry.getKey();

				List<Score> secondScoreList = scoreMongoRespository
						.findAllByTotalScoreGreaterThanEqualAndSchoolYearAndSemesterAndOrgIdAndExamType(
								ScoreConstant.PASS_SCORE_LINE,
								secondSchoolYear, secondSemester, orgId,
								ScoreConstant.EXAM_TYPE_COURSE);
				List<Score> firstScoreList = scoreMongoRespository
						.findAllByTotalScoreGreaterThanEqualAndSchoolYearAndSemesterAndOrgIdAndExamType(
								ScoreConstant.PASS_SCORE_LINE, firstSchoolYear,
								firstSemester, orgId,
								ScoreConstant.EXAM_TYPE_COURSE);
				HashMap<String, List<Score>> secondUserScoreMap = new HashMap<String, List<Score>>();
				HashMap<String, List<Score>> firstUserScoreMap = new HashMap<String, List<Score>>();
				// 按学号分组学生上学期成绩信息
				for (Score score : secondScoreList) {
					List<Score> scoreList = secondUserScoreMap.get(score
							.getJobNum());
					if (null == scoreList) {
						scoreList = new ArrayList<Score>();
						scoreList.add(score);
							secondUserScoreMap.put(score.getJobNum(), scoreList);
					} else {
						scoreList.add(score);
						secondUserScoreMap.put(score.getJobNum(), scoreList);
					}
				}
				// 按学号分组学生上上学期成绩信息
				for (Score score : firstScoreList) {
					List<Score> scoreList = firstUserScoreMap.get(score
							.getJobNum());

					if (null == scoreList) {
						scoreList = new ArrayList<Score>();
						scoreList.add(score);
						firstUserScoreMap.put(score.getJobNum(), scoreList);
					} else {
						scoreList.add(score);
						firstUserScoreMap.put(score.getJobNum(), scoreList);
					}
				}

				Iterator secondIter = secondUserScoreMap.entrySet().iterator();
				while (secondIter.hasNext()) {
					Map.Entry secondEntry = (Map.Entry) secondIter.next();
					String jobNum = (String) secondEntry.getKey();
					List<Score> userScoreList = (List<Score>) secondEntry
							.getValue();
					ScoreFluctuateCount scoreDomain = setSecondScoreDomainInfor(
							userScoreList, orgId, secondSchoolYear,
							secondSemester);
					if (null != scoreDomain) {
						scoreFluctuateCountMap.put(jobNum, scoreDomain);
					}
				}

				Iterator firstIter = firstUserScoreMap.entrySet().iterator();
				while (firstIter.hasNext()) {
					Map.Entry firstEntry = (Map.Entry) firstIter.next();
					String jobNum = (String) firstEntry.getKey();
					List<Score> userScoreList = (List<Score>) firstEntry
							.getValue();
					ScoreFluctuateCount scoreDomain = scoreFluctuateCountMap
							.get(jobNum);
					if (null != scoreDomain) {
						scoreDomain = setFirstScoreDomainInfor(userScoreList,
								orgId, firstSchoolYear, firstSemester,
								scoreDomain);
						if (!StringUtils.isEmpty(scoreDomain
								.getSecondAvgradePoint())
								&& !StringUtils.isEmpty(scoreDomain
										.getFirstAvgradePoint())) {
							scoreFluctuateCountMap.put(jobNum, scoreDomain);
						} else {
							scoreFluctuateCountMap.remove(jobNum);
						}
					}
				}

				Iterator fluctuateCountIter = scoreFluctuateCountMap.entrySet()
						.iterator();
				while (fluctuateCountIter.hasNext()) {
					Map.Entry fluctuateCountEntry = (Map.Entry) fluctuateCountIter
							.next();
					ScoreFluctuateCount scoreFluctuateCount = (ScoreFluctuateCount) fluctuateCountEntry
							.getValue();
					scoreFluctuateCountList.add(scoreFluctuateCount);
				}
				scoreFluctuateCountMongoRespository
						.save(scoreFluctuateCountList);
			}
		}
	}

	private ScoreFluctuateCount setSecondScoreDomainInfor(
			List<Score> userScoreList, Long orgId, int secondSchoolYear,
			int secondSemester) {

		ScoreFluctuateCount scoreDomain = new ScoreFluctuateCount();

		// 保存不重复的考试课程编号
		HashMap<String, String> courseMap = new HashMap<String, String>();
		BigDecimal totalScores = new BigDecimal(0);
		BigDecimal totalGradePoint = new BigDecimal(0);
		for (Score score : userScoreList) {

			if (StringUtils.isEmpty(scoreDomain.getJobNum())) {
				// 复制用户基本信息
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
				StringBuilder  dataSource = new StringBuilder("");
				dataSource.append("【KCH:" + score.getScheduleId() + ";");
				dataSource.append("KCMC:" + score.getCourseName() + ";");
				dataSource.append("XF:" + score.getCredit() + "】 ");
				scoreDomain.setDataSource(dataSource.toString());
			}
			courseMap.put(score.getScheduleId(), score.getScheduleId());
			if (!StringUtils.isEmpty(score.getTotalScore())) {
				totalScores = totalScores.add(new BigDecimal(score
						.getTotalScore()));
				if (!StringUtils.isEmpty(score.getGradePoint())) {
					totalGradePoint = totalGradePoint.add(new BigDecimal(score
							.getGradePoint()));
				}
			}
		}
		if (courseMap.size() > 0) {
			BigDecimal avgradePoint = new BigDecimal(
					totalGradePoint.doubleValue() / courseMap.size());
			scoreDomain.setSecondAvgradePoint(avgradePoint.setScale(2,
					RoundingMode.HALF_UP).toString());
			scoreDomain.setSecondSchoolYear(secondSchoolYear);
			scoreDomain.setSecondSemester(secondSemester);
			scoreDomain.setSecondTotalCourseNums(courseMap.size());
			scoreDomain.setSecondTotalGradePoint(totalGradePoint.toString());
			scoreDomain.setSecondTotalScores(totalScores.toString());
		} else {
			return null;
		}

		return scoreDomain;
	}

	/**
	 * 最近学期的相邻学期成绩信息获取
	 * 
	 */
	private ScoreFluctuateCount setFirstScoreDomainInfor(
			List<Score> userScoreList, Long orgId, int firstSchoolYear,
			int firstSemester, ScoreFluctuateCount scoreDomain) {

		// 保存不重复的考试课程编号
		HashMap<String, String> courseMap = new HashMap<String, String>();
		BigDecimal totalScores = new BigDecimal(0);
		BigDecimal totalGradePoint = new BigDecimal(0);
		for (Score score : userScoreList) {
			courseMap.put(score.getScheduleId(), score.getScheduleId());

			if (!StringUtils.isEmpty(score.getTotalScore())) {
				totalScores = totalScores.add(new BigDecimal(score
						.getTotalScore()));
				if (!StringUtils.isEmpty(score.getGradePoint())) {
					totalGradePoint = totalGradePoint.add(new BigDecimal(score
							.getGradePoint()));
				}
			}
		}
		if (courseMap.size() > 0) {
			BigDecimal avgradePoint = new BigDecimal(
					totalGradePoint.doubleValue() / courseMap.size());
			scoreDomain.setFirstAvgradePoint(avgradePoint.setScale(2,
					RoundingMode.HALF_UP).toString());
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

		// 获取的预警类型
		List<WarningType> warningTypeList = warningTypeService.getAllWarningTypeList();

		//已经开启次预警类型的组织
		Set<Long> orgIdSet = new HashSet<>();
		for (WarningType wt : warningTypeList) {
			if (wt.getSetupCloseFlag() == 10) {
				orgIdSet.add(wt.getOrgId());
			}
		}

		// 获取成绩波动预警配置
		List<AlarmSettings> settingsList = alarmSettingsService
				.getAlarmSettingsByType(WarningTypeConstant.PerformanceFluctuation
						.toString());
		if (null != settingsList && settingsList.size() > 0) {

			Calendar c = Calendar.getInstance();
			// 当前年份
			int schoolYear = c.get(Calendar.YEAR);
			// 当前月份
			int month = c.get(Calendar.MONTH) + 1;
			// 当前学期编号
			int semester = 2;
			if (month > 1 && month < 9) {
				semester = 1;
			}
			if (month == 1) {
				schoolYear = schoolYear - 1;
			}
			HashMap<Long, ArrayList<AlarmSettings>> alarmMap = new HashMap<Long, ArrayList<AlarmSettings>>();
			Set<String> warnRuleIdList = new HashSet<String>();
			Set<String> warnSettingsIdList = new HashSet<String>();
			// 按orgId归类告警等级阀值
			for (AlarmSettings settings : settingsList) {
				if (orgIdSet.contains(settings.getOrgId())&&settings.getSetupCloseFlag()==10) {
					warnSettingsIdList.add(settings.getId());
					Long orgId = settings.getOrgId();

					if (StringUtils.isEmpty(settings.getRuleSet())) {
						continue;
					}
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
			}
			// 预警规则获取
			HashMap<String, List<AlarmRule>> alarmRuleMap = new HashMap<String, List<AlarmRule>>();
			List<AlarmRule> alarmList = alarmRuleService
					.getAlarmRuleByIds(warnRuleIdList);
			for (AlarmRule alarmRule : alarmList) {
				// 成绩波动只取第一条规则
				if (alarmRule.getSerialNumber() == 1) {

					if (null != alarmRuleMap
							.get(alarmRule.getAlarmSettingsId())) {
						List<AlarmRule> ruleList = alarmRuleMap.get(alarmRule
								.getAlarmSettingsId());
						ruleList.add(alarmRule);
						alarmRuleMap.put(alarmRule.getAlarmSettingsId(),
								ruleList);
					} else {
						List<AlarmRule> ruleList = new ArrayList<AlarmRule>();
						ruleList.add(alarmRule);
						alarmRuleMap.put(alarmRule.getAlarmSettingsId(),
								ruleList);
					}

				}
			}

			Iterator iter = alarmMap.entrySet().iterator();
			while (iter.hasNext()) {
				Map.Entry entry = (Map.Entry) iter.next();
				Long orgId = (Long) entry.getKey();
				ArrayList<AlarmSettings> val = (ArrayList<AlarmSettings>) entry
						.getValue();

				//删除已生成的预警信息
				alertWarningInformationService.deleteWarningInformation(orgId,WarningTypeConstant.PerformanceFluctuation.toString(),schoolYear,semester);

				ArrayList<WarningInformation> alertInforList = new ArrayList<WarningInformation>();
				// 获取成绩波动数据
				List<ScoreFluctuateCount> scoreFluctuateCountList = scoreFluctuateCountMongoRespository
						.findAllByOrgId(orgId);
				if (null != scoreFluctuateCountList
						&& scoreFluctuateCountList.size() > 0) {
					for (ScoreFluctuateCount scoreFluctuateCount : scoreFluctuateCountList) {
						for (AlarmSettings alarmSettings : val) {
							List<AlarmRule> alarmRuleList = alarmRuleMap
									.get(alarmSettings.getId());
							if (null != alarmRuleList
									&& !alarmRuleList.isEmpty()) {

								for (AlarmRule alarmRule : alarmRuleList) {
									float result = 0L;
									if (!StringUtils
											.isEmpty(scoreFluctuateCount
													.getSecondAvgradePoint())
											&& !StringUtils
											.isEmpty(scoreFluctuateCount
													.getFirstAvgradePoint())) {
										result = Float
												.parseFloat(scoreFluctuateCount
														.getSecondAvgradePoint())
												- Float.parseFloat(scoreFluctuateCount
												.getFirstAvgradePoint());
									}
									// 上学期平均绩点小于上上学期平均绩点时
									if (result < 0) {
										result = Math.abs(result);
										if (result >= Float.parseFloat(String
												.valueOf(alarmRule
														.getRightParameter()))) {
											WarningInformation alertInfor = new WarningInformation();
											String alertId = UUID.randomUUID()
													.toString();
											alertInfor.setId(alertId);
											alertInfor
													.setDefendantId(scoreFluctuateCount
															.getUserId());
											alertInfor
													.setName(scoreFluctuateCount
															.getUserName());
											alertInfor
													.setJobNumber(scoreFluctuateCount
															.getJobNum());
											alertInfor
													.setCollogeId(scoreFluctuateCount
															.getCollegeId());
											alertInfor
													.setCollogeName(scoreFluctuateCount
															.getCollegeName());
											alertInfor
													.setClassId(scoreFluctuateCount
															.getClassId());
											alertInfor
													.setClassName(scoreFluctuateCount
															.getClassName());
											alertInfor
													.setProfessionalId(scoreFluctuateCount
															.getProfessionalId());
											alertInfor
													.setProfessionalName(scoreFluctuateCount
															.getProfessionalName());
											alertInfor
													.setWarningLevel(alarmSettings
															.getWarningLevel());
											alertInfor
													.setWarningState(AlertTypeConstant.ALERT_IN_PROCESS);
											alertInfor
													.setAlarmSettingsId(alarmSettings
															.getId());
											alertInfor.setSemester(semester);
											alertInfor
													.setTeacherYear(schoolYear);
											alertInfor
													.setWarningType(WarningTypeConstant.PerformanceFluctuation
															.toString());
											alertInfor
													.setWarningCondition(termConversion.getSemester(schoolYear, semester, 1).get("schoolYear") + "年第" + termConversion.getSemester(schoolYear, semester, 1).get("semester") + "学期平均绩点为："
															+ scoreFluctuateCount
															.getSecondAvgradePoint() +
															"," + termConversion.getSemester(schoolYear, semester, 2).get("schoolYear") + "年第" + termConversion.getSemester(schoolYear, semester, 2).get("semester") + "学期平均绩点为："
															+ scoreFluctuateCount
															.getFirstAvgradePoint()
															+ ",平均绩点下降："
															+ new BigDecimal(
															result)
															.setScale(
																	2,
																	RoundingMode.HALF_UP)
															.toString());
											alertInfor
													.setWarningTime(new Date());
											alertInfor
													.setPhone(scoreFluctuateCount
															.getUserPhone());
											alertInfor.setOrgId(alarmRule
													.getOrgId());
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
				.getAlarmSettingsByType(WarningTypeConstant.TotalAchievement
						.toString());
		if (null != settingsList && settingsList.size() > 0) {

			Calendar c = Calendar.getInstance();
			// 当前年份
			int schoolYear = c.get(Calendar.YEAR);
			// 当前月份
			int month = c.get(Calendar.MONTH)+1;
			// 当前学期编号
			int semester = 2;
			if (month > 1 && month < 9) {
				semester = 1;
			}
			if(month == 1 ){
				schoolYear = schoolYear - 1;
			}
			// 上学期编号
			int lastSemester = 1;
			if (semester == 1) {
				lastSemester = 2;
				schoolYear = schoolYear - 1;
			}

			HashMap<Long, Long> alarmMap = new HashMap<Long, Long>();
			// 按orgId归类告警等级阀值
			for (AlarmSettings settings : settingsList) {

				Long orgId = settings.getOrgId();
				alarmMap.put(orgId, orgId);
			}

			// 清除之前总评成绩不及格统计数据
			totalScoreCountMongoRespository.deleteAll();
			Iterator iter = alarmMap.entrySet().iterator();
			while (iter.hasNext()) {

				List<TotalScoreCount> totalScoreCountList = new ArrayList<TotalScoreCount>();
				HashMap<String, TotalScoreCount> totalScoreCountMap = new HashMap<String, TotalScoreCount>();
				Map.Entry entry = (Map.Entry) iter.next();
				Long orgId = (Long) entry.getKey();

				List<Score> scoreList = scoreMongoRespository
						.findAllByGradePointAndSchoolYearAndSemesterAndOrgIdAndExamType(
								0, schoolYear, lastSemester, orgId,
								ScoreConstant.EXAM_TYPE_COURSE);
				for (Score score : scoreList) {
					TotalScoreCount totalScoreCount = totalScoreCountMap
							.get(score.getJobNum());
					if (null == totalScoreCount) {
						totalScoreCount = new TotalScoreCount();
						StringBuilder  dataSource = new StringBuilder("");
						dataSource.append("【KCH:" + score.getScheduleId() + ";");
						dataSource.append("KCMC:" + score.getCourseName() + ";");
						dataSource.append("XF:" + score.getCredit() + "】 ");
						totalScoreCount.setDataSource(dataSource.toString());
						Set<String> scheduleIdSet = new HashSet<>();
						scheduleIdSet.add(score.getScheduleId());
						totalScoreCount.setScheduleIdList(scheduleIdSet);
						totalScoreCount.setClassId(score.getClassId());
						totalScoreCount.setClassName(score.getClassName());
						totalScoreCount.setCollegeId(score.getCollegeId());
						totalScoreCount.setCollegeName(score.getCollegeName());
						totalScoreCount.setGrade(score.getGrade());
						totalScoreCount.setJobNum(score.getJobNum());
						totalScoreCount.setOrgId(orgId);
						totalScoreCount.setProfessionalId(score
								.getProfessionalId());
						totalScoreCount.setProfessionalName(score
								.getProfessionalName());
						totalScoreCount.setSchoolYear(schoolYear);
						totalScoreCount.setSemester(lastSemester);
						totalScoreCount.setUserName(score.getUserName());
						totalScoreCount.setUserId(score.getUserId());
						totalScoreCount.setUserPhone(score.getUserPhone());
						totalScoreCount.setFailCourseNum(1);
						if (!StringUtils.isEmpty(score.getCourseType())
								&& ScoreConstant.REQUIRED_COURSE.equals(score
										.getCourseType())) {
							totalScoreCount.setFailRequiredCourseNum(1);
							totalScoreCount.setRequireCreditCount(score
									.getCredit());
						}
						totalScoreCountMap.put(score.getJobNum(),
								totalScoreCount);
					} else {
						if (!totalScoreCount.getScheduleIdList().contains(score.getScheduleId())) {
							totalScoreCount.getScheduleIdList().add(score.getScheduleId());
							StringBuilder dataSource = new StringBuilder("");
							dataSource.append("【KCH:" + score.getScheduleId() + ";");
							dataSource.append("KCMC:" + score.getCourseName() + ";");
							dataSource.append("XF:" + score.getCredit() + "】 ");
							totalScoreCount.setDataSource(totalScoreCount.getDataSource() + dataSource);
							if (!StringUtils.isEmpty(score.getCourseType())
									&& ScoreConstant.REQUIRED_COURSE.equals(score
									.getCourseType())) {
								totalScoreCount
										.setFailRequiredCourseNum(totalScoreCount
												.getFailRequiredCourseNum() + 1);
								if (StringUtils.isEmpty(totalScoreCount
										.getRequireCreditCount())) {
									totalScoreCount.setRequireCreditCount(score
											.getCredit());
								} else {
									BigDecimal totalCredits = new BigDecimal(
											totalScoreCount.getRequireCreditCount());
									totalCredits = totalCredits.add(new BigDecimal(
											score.getCredit()));
									totalScoreCount.setRequireCreditCount(Float
											.valueOf(totalCredits.setScale(2,
													RoundingMode.HALF_UP)
													.toString()));
								}
							}
							totalScoreCount.setFailCourseNum(totalScoreCount
									.getFailCourseNum() + 1);
							totalScoreCountMap.put(score.getJobNum(),
									totalScoreCount);
						}
					}
				}

				Iterator totalScoreiter = totalScoreCountMap.entrySet()
						.iterator();
				while (totalScoreiter.hasNext()) {
					Map.Entry totalScoreEntry = (Map.Entry) totalScoreiter
							.next();
					TotalScoreCount totalScoreCount = (TotalScoreCount) totalScoreEntry
							.getValue();
					if (totalScoreCount.getFailCourseNum() > 0) {
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

		// 获取的预警类型
		List<WarningType> warningTypeList = warningTypeService.getAllWarningTypeList();

		//已经开启次预警类型的组织
		Set<Long> orgIdSet = new HashSet<>();
		for(WarningType wt : warningTypeList){
			if(wt.getSetupCloseFlag()==10){
				orgIdSet.add(wt.getOrgId());
			}
		}

		// 获取预警配置
		List<AlarmSettings> settingsList = alarmSettingsService
				.getAlarmSettingsByType(WarningTypeConstant.TotalAchievement
						.toString());
		if (null != settingsList && settingsList.size() > 0) {

			Calendar c = Calendar.getInstance();
			// 当前年份
			int schoolYear = c.get(Calendar.YEAR);
			// 当前月份
			int month = c.get(Calendar.MONTH)+1;
			// 当前学期编号
			int semester = 2;
			if (month > 1 && month < 9) {
				semester = 1;
			}
			if(month == 1 ){
				schoolYear = schoolYear - 1;
			}
			// 上学期编号
			int lastSemester = 1;
			if (semester == 1) {
				lastSemester = 2;
				schoolYear = schoolYear - 1;
			}

			HashMap<Long, ArrayList<AlarmSettings>> alarmMap = new HashMap<Long, ArrayList<AlarmSettings>>();
			Set<String> warnRuleIdList = new HashSet<String>();
			Set<String> warnSettingsIdList = new HashSet<String>();
			// 按orgId归类告警等级阀值
			for (AlarmSettings settings : settingsList) {
				if(orgIdSet.contains(settings.getOrgId())&&settings.getSetupCloseFlag()==10) {
					warnSettingsIdList.add(settings.getId());
					Long orgId = settings.getOrgId();

					if (StringUtils.isEmpty(settings.getRuleSet())) {
						continue;
					}
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
			}
			// 预警规则获取
			HashMap<String, List<AlarmRule>> alarmRuleMap = new HashMap<String, List<AlarmRule>>();
			List<AlarmRule> alarmList = alarmRuleService
					.getAlarmRuleByIds(warnRuleIdList);
			for (AlarmRule alarmRule : alarmList) {
				// 总评成绩只取第一条规则
				if (alarmRule.getSerialNumber() == 1) {
					if (null != alarmRuleMap
							.get(alarmRule.getAlarmSettingsId())) {
						List<AlarmRule> ruleList = alarmRuleMap.get(alarmRule
								.getAlarmSettingsId());
						ruleList.add(alarmRule);
						alarmRuleMap.put(alarmRule.getAlarmSettingsId(),
								ruleList);
					} else {
						List<AlarmRule> ruleList = new ArrayList<AlarmRule>();
						ruleList.add(alarmRule);
						alarmRuleMap.put(alarmRule.getAlarmSettingsId(),
								ruleList);
					}
				}
			}

			Iterator iter = alarmMap.entrySet().iterator();
			while (iter.hasNext()) {

				// 更新预警集合
				ArrayList<WarningInformation> alertInforList = new ArrayList<WarningInformation>();
				// 定时任务产生的新的预警数据
				HashMap<String, WarningInformation> warnMap = new HashMap<String, WarningInformation>();
				Map.Entry entry = (Map.Entry) iter.next();
				Long orgId = (Long) entry.getKey();
				ArrayList<AlarmSettings> val = (ArrayList<AlarmSettings>) entry
						.getValue();

				//删除已生成的预警信息
				alertWarningInformationService.deleteWarningInformation(orgId,WarningTypeConstant.TotalAchievement.toString(),schoolYear,semester);


				// 预警处理配置获取
				// HashMap<String, ProcessingMode> processingModeMap = new
				// HashMap<String, ProcessingMode>();
				// List<ProcessingMode> processingModeList =
				// processingModeService
				// .getProcessingModeBywarningTypeId(orgId,
				// WarningType.TotalAchievement.toString());
				// 按orgId查询未报到的学生信息
				List<TotalScoreCount> totalScoreCountList = totalScoreCountMongoRespository
						.findAllBySchoolYearAndSemesterAndOrgId(schoolYear,
								lastSemester, orgId);

				if (null != totalScoreCountList
						&& totalScoreCountList.size() > 0) {
//					Date today = new Date();
					for (TotalScoreCount totalScoreCount : totalScoreCountList) {
						for (AlarmSettings alarmSettings : val) {

								List<AlarmRule> alarmRuleList = alarmRuleMap
										.get(alarmSettings.getId());
								if (null != alarmRuleList
										&& !alarmRuleList.isEmpty()) {

									for (AlarmRule alarmRule : alarmRuleList) {

										if (alarmRule.getSerialNumber() != 1) {
											continue;
										}
										if (totalScoreCount.getFailCourseNum() >= Float
												.parseFloat(alarmRule
														.getRightParameter())) {
											WarningInformation alertInfor = new WarningInformation();
											String alertId = UUID.randomUUID()
													.toString();
											alertInfor.setId(alertId);
											alertInfor
													.setDefendantId(totalScoreCount
															.getUserId());
											alertInfor.setName(totalScoreCount
													.getUserName());
											alertInfor.setJobNumber(totalScoreCount
													.getJobNum());
											alertInfor.setCollogeId(totalScoreCount
													.getCollegeId());
											alertInfor
													.setCollogeName(totalScoreCount
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
											alertInfor
													.setTeacherYear(totalScoreCount
															.getSchoolYear());
											alertInfor
													.setWarningLevel(alarmSettings
															.getWarningLevel());
											alertInfor
													.setWarningState(AlertTypeConstant.ALERT_IN_PROCESS);
											alertInfor
													.setAlarmSettingsId(alarmSettings
															.getId());
											alertInfor
													.setWarningType(WarningTypeConstant.TotalAchievement
															.toString());
											alertInfor.setWarningTime(new Date());
											alertInfor.setSemester(semester);
											alertInfor.setTeacherYear(schoolYear);
											if (null != totalScoreCount.getDataSource() && totalScoreCount.getDataSource().length() > 0) {
												alertInfor.setWarningSource(totalScoreCount.getDataSource().substring(0, totalScoreCount.getDataSource().length() - 1));
											}
											alertInfor
													.setWarningCondition(termConversion.getSemester(schoolYear, semester, 1).get("schoolYear") + "年第" + termConversion.getSemester(schoolYear, semester, 1).get("semester") + "学期必修课不及格课程数:"
															+ totalScoreCount
															.getFailCourseNum());
											alertInfor.setPhone(totalScoreCount
													.getUserPhone());
											alertInfor.setOrgId(alarmRule
													.getOrgId());
											alertInforList.add(alertInfor);
											warnMap.put(totalScoreCount
													.getJobNum(), alertInfor);
											break;
										} else {
											continue;
										}
									}

							}
						}
					}
				}
				
				//上学期每个学生的成绩数据明细
				List<TeachingScoreDetails> scoreDetailsList = teachingScoreService.findAllByTeacherYearAndSemesterAndDeleteFlagAndOrgId(schoolYear, lastSemester, DataValidity.VALID.getState(), orgId);
				if(null != scoreDetailsList && !scoreDetailsList.isEmpty()){
					for(TeachingScoreDetails scoreDetails :scoreDetailsList){
						for (AlarmSettings alarmSettings : val) {
								List<AlarmRule> alarmRuleList = alarmRuleMap
										.get(alarmSettings.getId());
								if (null != alarmRuleList
										&& !alarmRuleList.isEmpty()) {

									for (AlarmRule alarmRule : alarmRuleList) {

										if (alarmRule.getSerialNumber() != 2) {
											continue;
										}
										if (scoreDetails.getAvgGPA() <= Float
												.parseFloat(alarmRule
														.getRightParameter())) {
											WarningInformation alertInfor = new WarningInformation();
											String alertId = UUID.randomUUID()
													.toString();
											alertInfor.setId(alertId);
											alertInfor
													.setDefendantId(scoreDetails
															.getUserId());
											alertInfor.setName(scoreDetails
													.getUserName());
											alertInfor.setJobNumber(scoreDetails
													.getJobNum());
											alertInfor.setCollogeId(scoreDetails
													.getCollegeId());
											alertInfor
													.setCollogeName(scoreDetails
															.getCollegeName());
											alertInfor.setClassId(scoreDetails
													.getClassId());
											alertInfor.setClassName(scoreDetails
													.getClassName());
											alertInfor
													.setProfessionalId(scoreDetails
															.getProfessionalId());
											alertInfor
													.setProfessionalName(scoreDetails
															.getProfessionalName());
											alertInfor
													.setTeacherYear(scoreDetails
															.getTeacherYear());
											alertInfor
													.setWarningLevel(alarmSettings
															.getWarningLevel());
											alertInfor
													.setWarningState(AlertTypeConstant.ALERT_IN_PROCESS);
											alertInfor
													.setAlarmSettingsId(alarmSettings
															.getId());
											alertInfor
													.setWarningType(WarningTypeConstant.TotalAchievement
															.toString());
											alertInfor.setWarningTime(new Date());
											alertInfor.setSemester(semester);
											alertInfor.setTeacherYear(schoolYear);
											alertInfor
													.setWarningCondition(termConversion.getSemester(schoolYear, semester, 1).get("schoolYear") + "年第" + termConversion.getSemester(schoolYear, semester, 1).get("semester") + "学期平均学分绩点:"
															+ scoreDetails
															.getAvgGPA());
											alertInfor.setOrgId(alarmRule
													.getOrgId());

											if (null != warnMap.get(scoreDetails
													.getJobNum())) {
												WarningInformation alertInfor2 = warnMap.get(scoreDetails
														.getJobNum());
												if (alertInfor2.getWarningLevel() > alertInfor.getWarningLevel()) {
													continue;
												}
												if (alertInfor2.getWarningLevel() == alertInfor.getWarningLevel()) {
													alertInfor.setWarningCondition(alertInfor.getWarningCondition() + "; " + alertInfor2.getWarningCondition());
													alertInforList.remove(alertInfor2);
													alertInforList.add(alertInfor);
												}
												if (alertInfor2.getWarningLevel() < alertInfor.getWarningLevel()) {
													alertInforList.remove(alertInfor2);
													alertInforList.add(alertInfor);
												}
											} else {
												alertInforList.add(alertInfor);
											}

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
	 * 修读异常预警
	 */
	public void attendAbnormalJob() {

		// 获取的预警类型
		List<WarningType> warningTypeList = warningTypeService.getAllWarningTypeList();

		//已经开启次预警类型的组织
		Set<Long> orgIdSet = new HashSet<>();
		for(WarningType wt : warningTypeList){
			if(wt.getSetupCloseFlag()==10){
				orgIdSet.add(wt.getOrgId());
			}
		}

		// 获取预警配置
		List<AlarmSettings> settingsList = alarmSettingsService
				.getAlarmSettingsByType(WarningTypeConstant.AttendAbnormal
						.toString());
		if (null != settingsList && settingsList.size() > 0) {

			Calendar c = Calendar.getInstance();
			// 当前年份
			int schoolYear = c.get(Calendar.YEAR);
			// 当前月份
			int month = c.get(Calendar.MONTH)+1;
			// 当前学期编号
			int semester = 2;
			if (month > 1 && month < 9) {
				semester = 1;
			}
			if(month == 1 ){
				schoolYear = schoolYear - 1;
			}

			// 上学期编号
			int lastSemester = 1;
			if (semester == 1) {
				lastSemester = 2;
				schoolYear = schoolYear - 1;
			}

			HashMap<Long, ArrayList<AlarmSettings>> alarmMap = new HashMap<Long, ArrayList<AlarmSettings>>();
			Set<String> warnRuleIdList = new HashSet<String>();
			Set<String> warnSettingsIdList = new HashSet<String>();
			// 按orgId归类告警等级阀值
			for (AlarmSettings settings : settingsList) {
				if(orgIdSet.contains(settings.getOrgId())&&settings.getSetupCloseFlag()==10) {
					warnSettingsIdList.add(settings.getId());
					Long orgId = settings.getOrgId();

					if (StringUtils.isEmpty(settings.getRuleSet())) {
						continue;
					}
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

				// 更新预警集合
				ArrayList<WarningInformation> alertInforList = new ArrayList<WarningInformation>();
				// 定时任务产生的新的预警数据
				HashMap<String, WarningInformation> warnMap = new HashMap<String, WarningInformation>();
				Map.Entry entry = (Map.Entry) iter.next();
				Long orgId = (Long) entry.getKey();
				ArrayList<AlarmSettings> val = (ArrayList<AlarmSettings>) entry
						.getValue();

				//删除已生成的预警信息
				alertWarningInformationService.deleteWarningInformation(orgId,WarningTypeConstant.AttendAbnormal.toString(),schoolYear,semester);

				// 预警处理配置获取
				// HashMap<String, ProcessingMode> processingModeMap = new
				// HashMap<String, ProcessingMode>();
				// List<ProcessingMode> processingModeList =
				// processingModeService
				// .getProcessingModeBywarningTypeId(orgId,
				// WarningType.TotalAchievement.toString());
				// 按orgId查询未报到的学生信息
				List<TotalScoreCount> totalScoreCountList = totalScoreCountMongoRespository
						.findAllBySchoolYearAndSemesterAndOrgId(schoolYear,
								lastSemester, orgId);

				if (null != totalScoreCountList
						&& totalScoreCountList.size() > 0) {
					Date today = new Date();
					for (TotalScoreCount totalScoreCount : totalScoreCountList) {
						for (AlarmSettings alarmSettings : val) {
								AlarmRule alarmRule = alarmRuleMap
										.get(alarmSettings.getRuleSet());
								if (null != alarmRule) {
									if (StringUtils.isEmpty(totalScoreCount
											.getRequireCreditCount())) {
										continue;
									}
									if (totalScoreCount.getRequireCreditCount() >= Float
											.parseFloat(String.valueOf(alarmRule
													.getRightParameter()))) {
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
										alertInfor.setTeacherYear(totalScoreCount
												.getSchoolYear());
										alertInfor.setWarningLevel(alarmSettings
												.getWarningLevel());
										alertInfor
												.setWarningState(AlertTypeConstant.ALERT_IN_PROCESS);
										alertInfor.setAlarmSettingsId(alarmSettings
												.getId());
										alertInfor
												.setWarningType(WarningTypeConstant.AttendAbnormal
														.toString());
										alertInfor.setWarningTime(new Date());
										alertInfor
												.setWarningCondition(termConversion.getSemester(schoolYear, semester, 1).get("schoolYear") + "年第" + termConversion.getSemester(schoolYear, semester, 1).get("semester") + "学期不及格必修课程学分为："
														+ totalScoreCount
														.getRequireCreditCount());
										alertInfor.setPhone(totalScoreCount
												.getUserPhone());
										alertInfor.setSemester(semester);
										alertInfor.setTeacherYear(schoolYear);
										alertInfor.setOrgId(alarmRule.getOrgId());
										alertInforList.add(alertInfor);
										if (null != totalScoreCount.getDataSource() && totalScoreCount.getDataSource().length() > 0) {
											alertInfor.setWarningSource(totalScoreCount.getDataSource().substring(0, totalScoreCount.getDataSource().length() - 1));
										}

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
				.getAlarmSettingsByType(WarningTypeConstant.SupplementAchievement
						.toString());
		if (null != settingsList && settingsList.size() > 0) {

			Calendar c = Calendar.getInstance();
			// 当前年份
			int endYear = c.get(Calendar.YEAR);
			// 前三年
			int beginYear = endYear - 3;

			HashMap<Long, Long> alarmMap = new HashMap<Long, Long>();
			// 按orgId归类告警等级阀值
			for (AlarmSettings settings : settingsList) {

				Long orgId = settings.getOrgId();
				alarmMap.put(orgId, orgId);
			}

			// 清除之前补考统计数据
			makeUpScoreCountMongoRespository.deleteAll();
			Iterator iter = alarmMap.entrySet().iterator();
			while (iter.hasNext()) {

				List<MakeUpScoreCount> makeUpScoreCountList = new ArrayList<MakeUpScoreCount>();
				HashMap<String, MakeUpScoreCount> makeUpScoreCountMap = new HashMap<String, MakeUpScoreCount>();
				Map.Entry entry = (Map.Entry) iter.next();
				Long orgId = (Long) entry.getKey();

				HashMap<String, HashMap<String, ArrayList<Score>>> scoreMap = new HashMap<String, HashMap<String, ArrayList<Score>>>();
				List<Score> scoreList = scoreMongoRespository
						.findAllBySchoolYearGreaterThanEqualAndOrgIdAndExamType(
								beginYear, orgId,
								ScoreConstant.EXAM_TYPE_COURSE);
				List<String> sourceList = new ArrayList<>();
				for (Score score : scoreList) {
					HashMap<String, ArrayList<Score>> userScoreMap = scoreMap
							.get(score.getJobNum());
					if (null == userScoreMap) {
						ArrayList<Score> userScoreList = new ArrayList<Score>();
						userScoreList.add(score);
						userScoreMap = new HashMap<String, ArrayList<Score>>();
						userScoreMap.put(score.getScheduleId(), userScoreList);
						scoreMap.put(score.getJobNum(), userScoreMap);
					} else {
						ArrayList<Score> userScoreList = userScoreMap.get(score
								.getScheduleId());
						if (null == userScoreList) {
							userScoreList = new ArrayList<Score>();
							userScoreList.add(score);
							userScoreMap.put(score.getScheduleId(),
									userScoreList);
							scoreMap.put(score.getJobNum(), userScoreMap);
						} else {
							userScoreList.add(score);
							userScoreMap.put(score.getScheduleId(),
									userScoreList);
							scoreMap.put(score.getJobNum(), userScoreMap);
						}
					}
				}

				Iterator scoreiter = scoreMap.entrySet().iterator();
				while (scoreiter.hasNext()) {
					Map.Entry scoreEntry = (Map.Entry) scoreiter.next();
					HashMap<String, ArrayList<Score>> courseScoreMap = (HashMap<String, ArrayList<Score>>) scoreEntry
							.getValue();
					Iterator courseScoreiter = courseScoreMap.entrySet()
							.iterator();
					while (courseScoreiter.hasNext()) {
						Map.Entry courseScoreEntry = (Map.Entry) courseScoreiter
								.next();
						ArrayList<Score> courseScoreList = (ArrayList<Score>) courseScoreEntry
								.getValue();
						float totalGradePoint = 0.0f;
						for (Score score : courseScoreList) {
							if (!StringUtils.isEmpty(score.getGradePoint())) {
								totalGradePoint += score.getGradePoint();
							}
						}
						if (totalGradePoint < 1.0f) {
							Score score = courseScoreList.get(0);
							MakeUpScoreCount makeUpScoreCount = makeUpScoreCountMap
									.get(score.getJobNum());
							if (null == makeUpScoreCount) {
								makeUpScoreCount = new MakeUpScoreCount();
								StringBuilder  dataSource = new StringBuilder("");
								dataSource.append("【KCH:" + score.getScheduleId() + ";");
								dataSource.append("KCMC:" + score.getCourseName() + ";");
								dataSource.append("XF:" + score.getCredit() + "】 ");
								makeUpScoreCount.setDataSource(dataSource.toString());
								makeUpScoreCount.setUserId(score.getUserId());
								makeUpScoreCount.setUserName(score.getUserName());
								makeUpScoreCount.setClassId(score.getClassId());
								makeUpScoreCount.setClassName(score
										.getClassName());
								makeUpScoreCount.setCollegeId(score
										.getCollegeId());
								makeUpScoreCount.setCollegeName(score
										.getCollegeName());
								makeUpScoreCount.setJobNum(score.getJobNum());
								makeUpScoreCount.setOrgId(orgId);
								makeUpScoreCount.setProfessionalId(score
										.getProfessionalId());
								makeUpScoreCount.setProfessionalName(score
										.getProfessionalName());
								makeUpScoreCount.setUserName(score
										.getUserName());
								makeUpScoreCount.setUserPhone(score
										.getUserPhone());
								makeUpScoreCount.setFailCourseNum(1);
								makeUpScoreCount.setFailCourseCredit(score
										.getCredit());
								makeUpScoreCount.setDataSource(dataSource.substring(0,dataSource.length()-1));
								makeUpScoreCountMap.put(score.getJobNum(),
										makeUpScoreCount);
							} else {
								StringBuilder  dataSource = new StringBuilder("");
								dataSource.append("【KCH:" + score.getScheduleId() + ";");
								dataSource.append("KCMC:" + score.getCourseName() + ";");
								dataSource.append("XF:" + score.getCredit() + "】 ");
								makeUpScoreCount.setDataSource(makeUpScoreCount.getDataSource()+dataSource);
								makeUpScoreCount
										.setFailCourseNum(makeUpScoreCount
												.getFailCourseNum() + 1);
								float totalCredit = score.getCredit()
										+ makeUpScoreCount
												.getFailCourseCredit();
								makeUpScoreCount
										.setFailCourseCredit(totalCredit);
								makeUpScoreCountMap.put(score.getJobNum(),
										makeUpScoreCount);
							}

						}
					}
				}

				Iterator makeUpScoreCountiter = makeUpScoreCountMap.entrySet()
						.iterator();
				while (makeUpScoreCountiter.hasNext()) {
					Map.Entry makeUpScoreCountEntry = (Map.Entry) makeUpScoreCountiter
							.next();
					MakeUpScoreCount makeUpScoreCount = (MakeUpScoreCount) makeUpScoreCountEntry
							.getValue();
					if (makeUpScoreCount.getFailCourseNum() > 0) {
						makeUpScoreCountList.add(makeUpScoreCount);
					}
				}

				makeUpScoreCountMongoRespository.save(makeUpScoreCountList);
			}
		}
	}
    //补考成绩预警定时任务
	public void makeUpScoreJob() {

		// 获取的预警类型
		List<WarningType> warningTypeList = warningTypeService.getAllWarningTypeList();

		//已经开启次预警类型的组织
		Set<Long> orgIdSet = new HashSet<>();
		for(WarningType wt : warningTypeList){
			if(wt.getSetupCloseFlag()==10){
				orgIdSet.add(wt.getOrgId());
			}
		}

		// 获取预警配置
		List<AlarmSettings> settingsList = alarmSettingsService
				.getAlarmSettingsByType(WarningTypeConstant.SupplementAchievement
						.toString());
		if (null != settingsList && settingsList.size() > 0) {

			Calendar c = Calendar.getInstance();
			// 当前年份
			int schoolYear = c.get(Calendar.YEAR);
			// 当前月份
			int month = c.get(Calendar.MONTH)+1;
			// 当前学期编号
			int semester = 2;
			if (month > 1 && month < 9) {
				semester = 1;
			}
			if(month == 1 ){
				schoolYear = schoolYear - 1;
			}
			HashMap<Long, ArrayList<AlarmSettings>> alarmMap = new HashMap<Long, ArrayList<AlarmSettings>>();
			Set<String> warnRuleIdList = new HashSet<String>();
			Set<String> warnSettingsIdList = new HashSet<String>();
			// 按orgId归类告警等级阀值
			for (AlarmSettings settings : settingsList) {
				if(orgIdSet.contains(settings.getOrgId())&&settings.getSetupCloseFlag()==10) {
					warnSettingsIdList.add(settings.getId());
					Long orgId = settings.getOrgId();

					if (StringUtils.isEmpty(settings.getRuleSet())) {
						continue;
					}
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

				// 更新预警集合
				ArrayList<WarningInformation> alertInforList = new ArrayList<WarningInformation>();
				// 定时任务产生的新的预警数据
				HashMap<String, WarningInformation> warnMap = new HashMap<String, WarningInformation>();
				Map.Entry entry = (Map.Entry) iter.next();
				Long orgId = (Long) entry.getKey();
				ArrayList<AlarmSettings> val = (ArrayList<AlarmSettings>) entry
						.getValue();

				//删除已生成的预警信息
				alertWarningInformationService.deleteWarningInformation(orgId,WarningTypeConstant.SupplementAchievement.toString(),schoolYear,semester);


				// 预警处理配置获取
				// HashMap<String, ProcessingMode> processingModeMap = new
				// HashMap<String, ProcessingMode>();
				// List<ProcessingMode> processingModeList =
				// processingModeService
				// .getProcessingModeBywarningTypeId(orgId,
				// WarningType.TotalAchievement.toString());
				// 按orgId查询未报到的学生信息
				List<MakeUpScoreCount> makeUpScoreCountList = makeUpScoreCountMongoRespository
						.findAllByOrgId(orgId);

				if (null != makeUpScoreCountList
						&& makeUpScoreCountList.size() > 0) {
					for (MakeUpScoreCount makeUpScoreCount : makeUpScoreCountList) {
						for (AlarmSettings alarmSettings : val) {
								AlarmRule alarmRule = alarmRuleMap
										.get(alarmSettings.getRuleSet());
								if (null != alarmRule) {
									if (makeUpScoreCount.getFailCourseNum() >= Float
											.parseFloat(alarmRule
													.getRightParameter())) {
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
												.setWarningType(WarningTypeConstant.SupplementAchievement
														.toString());
										alertInfor
												.setWarningCondition("补考后" + termConversion.getSemester(schoolYear, semester, 1).get("schoolYear") + "年第" + termConversion.getSemester(schoolYear, semester, 1).get("semester") + "学期总评成绩不及格课程数:"
														+ makeUpScoreCount
														.getFailCourseNum());
										alertInfor.setSemester(semester);
										alertInfor.setTeacherYear(schoolYear);
										alertInfor.setWarningTime(new Date());
										alertInfor.setPhone(makeUpScoreCount
												.getUserPhone());
										alertInfor.setOrgId(alarmRule.getOrgId());
										alertInforList.add(alertInfor);
										if (null != makeUpScoreCount.getDataSource() && makeUpScoreCount.getDataSource().length() > 0) {
											alertInfor.setWarningSource(makeUpScoreCount.getDataSource().substring(0, makeUpScoreCount.getDataSource().length() - 1));
										}

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

		// 获取的预警类型
		List<WarningType> warningTypeList = warningTypeService.getAllWarningTypeList();

		//已经开启次预警类型的组织
		Set<Long> orgIdSet = new HashSet<>();
		for(WarningType wt : warningTypeList){
			if(wt.getSetupCloseFlag()==10){
				orgIdSet.add(wt.getOrgId());
			}
		}

		// 获取预警配置
		List<AlarmSettings> settingsList = alarmSettingsService
				.getAlarmSettingsByType(WarningTypeConstant.Cet.toString());
		if (null != settingsList && settingsList.size() > 0) {

			Calendar c = Calendar.getInstance();
			// 当前年份
			int schoolYear = c.get(Calendar.YEAR);
			// 当前月份
			int month = c.get(Calendar.MONTH)+1;
			// 当前学期编号
			int semester = 2;
			if (month > 1 && month < 9) {
				semester = 1;
			}
			if(month == 1 ){
				schoolYear = schoolYear - 1;
			}
			int nowYear = c.get(Calendar.YEAR);
			String grade2 = String.valueOf(nowYear - 1);
			String grade3 = String.valueOf(nowYear - 2);
			String grade4 = String.valueOf(nowYear - 3);
			String[] grades = new String[] { grade2, grade3, grade4 };
			HashMap<String, Integer> gradeMap = new HashMap<String, Integer>();
			gradeMap.put(grade2, 2);
			gradeMap.put(grade3, 3);
			gradeMap.put(grade4, 4);

			HashMap<Long, ArrayList<AlarmSettings>> alarmMap = new HashMap<Long, ArrayList<AlarmSettings>>();
			Set<String> warnRuleIdList = new HashSet<String>();
			Set<String> warnSettingsIdList = new HashSet<String>();
			// 按orgId归类告警等级阀值
			for (AlarmSettings settings : settingsList) {
				if(orgIdSet.contains(settings.getOrgId())&&settings.getSetupCloseFlag()==10) {
					warnSettingsIdList.add(settings.getId());
					Long orgId = settings.getOrgId();

					if (StringUtils.isEmpty(settings.getRuleSet())) {
						continue;
					}
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
			}
			// 预警规则获取
			HashMap<String, AlarmRule> alarmRuleMap = new HashMap<String, AlarmRule>();
			List<AlarmRule> alarmList = alarmRuleService
					.getAlarmRuleByIds(warnRuleIdList);
			if (null != alarmList) {
				logger.debug("alarmList.size()----------" + alarmList.size());
			}
			for (AlarmRule alarmRule : alarmList) {
				alarmRuleMap.put(alarmRule.getAlarmSettingsId(), alarmRule);
			}

			Iterator iter = alarmMap.entrySet().iterator();
			while (iter.hasNext()) {

				// 更新预警集合
				ArrayList<WarningInformation> alertInforList = new ArrayList<WarningInformation>();
				Map.Entry entry = (Map.Entry) iter.next();
				Long orgId = (Long) entry.getKey();
				ArrayList<AlarmSettings> val = (ArrayList<AlarmSettings>) entry
						.getValue();

				//删除已生成的预警信息
				alertWarningInformationService.deleteWarningInformation(orgId,WarningTypeConstant.Cet.toString(),schoolYear,semester);


				// 预警处理配置获取
				// HashMap<String, ProcessingMode> processingModeMap = new
				// HashMap<String, ProcessingMode>();
				// List<ProcessingMode> processingModeList =
				// processingModeService
				// .getProcessingModeBywarningTypeId(orgId,
				// WarningType.TotalAchievement.toString());
				// 按orgId查询成绩信息
				List<Score> scoreList = scoreMongoRespository
						.findAllByGradeInAndOrgIdAndExamType(grades, orgId,
								ScoreConstant.EXAM_TYPE_CET4);
				if (null != scoreList) {
					logger.debug("scoreList.size()---------" + scoreList.size());
				}
				HashMap<String, Score> userScoreMap = new HashMap<String, Score>();
				// 去重后的英语四级不合格成绩集合
				List<Score> alarmScoreList = new ArrayList<Score>();

				for (Score score : scoreList) {
					if (StringUtils.isEmpty(score.getTotalScore())) {
						continue;
					}
					Score tmpScore = userScoreMap.get(score.getJobNum());
					if (null != tmpScore) {
						if (score.getTotalScore() > tmpScore.getTotalScore()) {
							userScoreMap.put(score.getJobNum(), score);
						}
					} else {
						userScoreMap.put(score.getJobNum(), score);
					}
				}

				Iterator userScoreMapIter = userScoreMap.entrySet().iterator();
				while (userScoreMapIter.hasNext()) {
					Map.Entry userScoreEntry = (Map.Entry) userScoreMapIter
							.next();
					Score score = (Score) userScoreEntry.getValue();

					if (score.getTotalScore() < ScoreConstant.CET_PASS_SCORE_LINE) {
						alarmScoreList.add(score);
					}
				}

				if (null != alarmScoreList && alarmScoreList.size() > 0) {
					for (Score score : alarmScoreList) {
						for (AlarmSettings alarmSettings : val) {
								AlarmRule alarmRule = alarmRuleMap
										.get(alarmSettings.getId());
								if (null != alarmRule) {
									int grade = 0;
									if (null != gradeMap.get(score.getGrade())) {
										grade = gradeMap.get(score.getGrade())
												.intValue();
									}
									if (grade >= Float.parseFloat(alarmRule
											.getRightParameter())) {
										WarningInformation alertInfor = new WarningInformation();
										String alertId = UUID.randomUUID()
												.toString();
										alertInfor.setId(alertId);
										alertInfor
												.setDefendantId(score.getUserId());
										alertInfor.setName(score.getUserName());
										alertInfor.setJobNumber(score.getJobNum());
										alertInfor.setCollogeId(score
												.getCollegeId());
										alertInfor.setCollogeName(score
												.getCollegeName());
										alertInfor.setClassId(score.getClassId());
										alertInfor.setClassName(score
												.getClassName());
										alertInfor.setProfessionalId(score
												.getProfessionalId());
										alertInfor.setProfessionalName(score
												.getProfessionalName());
										alertInfor.setWarningLevel(alarmSettings
												.getWarningLevel());
										alertInfor
												.setWarningState(AlertTypeConstant.ALERT_IN_PROCESS);
										alertInfor.setAlarmSettingsId(alarmSettings
												.getId());
										alertInfor
												.setWarningType(WarningTypeConstant.Cet
														.toString());
										alertInfor.setWarningCondition("第" + grade
												+ "学年仍未通过英语四级考试");
										alertInfor.setSemester(semester);
										alertInfor.setTeacherYear(schoolYear);
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

	/**
	 * 退学预警
	 */
	public void dropOutJob() {

		// 获取的预警类型
		List<WarningType> warningTypeList = warningTypeService.getAllWarningTypeList();

		//已经开启次预警类型的组织
		Set<Long> orgIdSet = new HashSet<>();
		for(WarningType wt : warningTypeList){
			if(wt.getSetupCloseFlag()==10){
				orgIdSet.add(wt.getOrgId());
			}
		}

		// 获取预警配置
		List<AlarmSettings> settingsList = alarmSettingsService
				.getAlarmSettingsByType(WarningTypeConstant.LeaveSchool
						.toString());
		if (null != settingsList && settingsList.size() > 0) {

//			Calendar c = Calendar.getInstance();
//			int month = c.get(Calendar.MONTH)+1;
//			// 当前学期编号
//			int semester = 2;
//			if (month > 1 && month < 9) {
//				semester = 1;
//			}
//			// 当前年份
//			int endYear = c.get(Calendar.YEAR);
//			// 前三年
//			int beginYear = endYear - 3;


			Calendar c = Calendar.getInstance();
			// 当前年份
			int schoolYear = c.get(Calendar.YEAR);
			// 当前月份
			int month = c.get(Calendar.MONTH)+1;
			// 当前学期编号
			int semester = 2;
			if (month > 1 && month < 9) {
				semester = 1;
			}
			if(month == 1 ){
				schoolYear = schoolYear - 1;
			}


			HashMap<Long, ArrayList<AlarmSettings>> alarmMap = new HashMap<Long, ArrayList<AlarmSettings>>();
			Set<String> warnRuleIdList = new HashSet<String>();
			Set<String> warnSettingsIdList = new HashSet<String>();
			// 按orgId归类告警等级阀值
			for (AlarmSettings settings : settingsList) {
				if(orgIdSet.contains(settings.getOrgId())&&settings.getSetupCloseFlag()==10) {
					warnSettingsIdList.add(settings.getId());
					Long orgId = settings.getOrgId();

					if (StringUtils.isEmpty(settings.getRuleSet())) {
						continue;
					}
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

				// 更新预警集合
				ArrayList<WarningInformation> alertInforList = new ArrayList<WarningInformation>();
				Map.Entry entry = (Map.Entry) iter.next();
				Long orgId = (Long) entry.getKey();
				ArrayList<AlarmSettings> val = (ArrayList<AlarmSettings>) entry
						.getValue();

				//删除已生成的预警信息
				alertWarningInformationService.deleteWarningInformation(orgId,WarningTypeConstant.LeaveSchool.toString(),schoolYear,semester);

				List<MakeUpScoreCount> makeUpScoreCountList = makeUpScoreCountMongoRespository
						.findAllByOrgId(orgId);

				if (null != makeUpScoreCountList
						&& makeUpScoreCountList.size() > 0) {
					for (MakeUpScoreCount makeUpScoreCount : makeUpScoreCountList) {
						for (AlarmSettings alarmSettings : val) {
								AlarmRule alarmRule = alarmRuleMap
										.get(alarmSettings.getRuleSet());
								if (null != alarmRule) {
									if (makeUpScoreCount.getFailCourseCredit() >= Float
											.parseFloat(alarmRule
													.getRightParameter())) {
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
												.setWarningType(WarningTypeConstant.LeaveSchool
														.toString());
										BigDecimal failCourseCredit = new BigDecimal(
												makeUpScoreCount
														.getFailCourseCredit());
										alertInfor
												.setWarningCondition("不及格必修课和选修课累计学分:"
														+ failCourseCredit
														.setScale(
																2,
																RoundingMode.HALF_UP)
														.toString());
										alertInfor.setWarningTime(new Date());
										alertInfor.setPhone(makeUpScoreCount
												.getUserPhone());
										alertInfor.setSemester(semester);
										alertInfor.setTeacherYear(schoolYear);
										alertInfor.setOrgId(alarmRule.getOrgId());
										alertInforList.add(alertInfor);

										if (null != makeUpScoreCount.getDataSource() && makeUpScoreCount.getDataSource().length() > 0) {
											alertInfor.setWarningSource(makeUpScoreCount.getDataSource().substring(0, makeUpScoreCount.getDataSource().length() - 1));
										}

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
