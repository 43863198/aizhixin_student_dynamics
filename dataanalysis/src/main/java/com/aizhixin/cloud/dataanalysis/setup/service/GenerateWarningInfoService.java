package com.aizhixin.cloud.dataanalysis.setup.service;

import com.aizhixin.cloud.dataanalysis.alertinformation.entity.WarningInformation;
import com.aizhixin.cloud.dataanalysis.alertinformation.service.AlertWarningInformationService;
import com.aizhixin.cloud.dataanalysis.common.service.DistributeLock;
import com.aizhixin.cloud.dataanalysis.common.util.RestUtil;
import com.aizhixin.cloud.dataanalysis.etl.score.service.StandardScoreSemesterIndexService;
import com.aizhixin.cloud.dataanalysis.etl.study.service.StudyExceptionIndexService;
import com.aizhixin.cloud.dataanalysis.rollCall.job.RollCallJob;
import com.aizhixin.cloud.dataanalysis.score.job.ScoreJob;
import com.aizhixin.cloud.dataanalysis.setup.dto.StudentAlertMsgDTO;
import com.aizhixin.cloud.dataanalysis.setup.entity.*;
import com.aizhixin.cloud.dataanalysis.studentRegister.job.StudentRegisterJob;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.SQLQuery;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.*;

/**
 * @author: Created by jianwei.wu
 * @E-mail: wujianwei@aizhixin.com
 * @Date: 2018-05-21
 */
@Service
@Slf4j
public class GenerateWarningInfoService {
    @Autowired
    private WarningTypeService warningTypeService;
    @Autowired
    private AlarmSettingsService alarmSettingsService;
    @Autowired
    private RuleParameterService ruleParameterService;
    @Autowired
    private StudentRegisterJob studentRegisterJob;
    @Autowired
    private RollCallJob rollCallJob;
    @Autowired
    private ScoreJob scoreJob;
    @Autowired
    private AlertWarningInformationService warningInformationService;
    @Autowired
    private RuleService ruleService;
    @Autowired
    private DistributeLock distributeLock;
    @Autowired
    private EntityManager em;
    @Autowired
    private StudyExceptionIndexService studyExceptionIndexService;
    @Autowired
    private StandardScoreSemesterIndexService standardScoreSemesterIndexService;
    @Autowired
    private  AlarmCreateRecordService alarmCreateRecordService;
    @Autowired
    private RestUtil restUtil;
    @Value("${dl.dledu.back.host}")
    private String zhixinUrl;

    public void warningJob() {
        Calendar c = Calendar.getInstance();
        // 当前年份
        int year = c.get(Calendar.YEAR);
        // 当前月份
        int month = c.get(Calendar.MONTH) + 1;
        // 当前学期编号
        String semester = "秋";
        if (month > 1 && month < 9) {
            semester = "春";
        }
        if (month == 1) {
            year = year - 1;
        }
        String teachYear = year + "";
        //获取的预警类型
        List<WarningType> warningTypeList = warningTypeService.getAllWarningTypeList();
        if (null != warningTypeList && warningTypeList.size() > 0) {
            for (WarningType wt : warningTypeList) {
                if (wt.getSetupCloseFlag() == 10) {
                    this.warningInfo(wt.getOrgId(), wt.getType(), teachYear, semester, 10, null, null);
                }
            }
        }
    }

    public void enerateWarningInfo(Long orgId, String warningType, Integer overOrAdd, Long userId, String userName) {
        Calendar c = Calendar.getInstance();
        // 当前年份
        int year = c.get(Calendar.YEAR);
        // 当前月份
        int month = c.get(Calendar.MONTH) + 1;
        //当前日期
        int day = c.get(Calendar.DAY_OF_MONTH);
        String date = year + "-" + month + "-" + day;
        Map map = getXQAndXN(orgId, date);
        String teachYear;
        String semester;
        if (null != map) {
            teachYear = map.get("teacherYear").toString();
            semester = map.get("semester").toString();
        } else {
            semester = "秋";
            if (month > 3 && month < 9) {
                semester = "春";
            } else {
                year = year - 1;
            }
            teachYear = year + "";
        }
        warningInfo(orgId, warningType, teachYear, semester, overOrAdd, userId, userName);

    }

    public Map getXQAndXN(Long orgId, String date) {
        Map<String, Object> result = new HashMap<>();
        Map<String, Object> condition = new HashMap<>();
        try {
            StringBuilder sql = new StringBuilder("SELECT  TEACHER_YEAR as teacherYear, SEMESTER as semester FROM t_school_calendar WHERE 1=1");
            if (null != orgId) {
                sql.append(" and ORG_ID = :orgId");
                condition.put("orgId", orgId);
            }
            sql.append(" AND '" + date + "' BETWEEN START_TIME AND END_TIME");
            Query sq = em.createNativeQuery(sql.toString());
            sq.unwrap(SQLQuery.class).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
            for (Map.Entry<String, Object> e : condition.entrySet()) {
                sq.setParameter(e.getKey(), e.getValue());
            }
            Map res = (Map) sq.getSingleResult();
            return res;
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", "获取学当前年学期失败！");
            return new HashMap<>();
        }

    }

    @Async
    public void warningInfo(Long orgId, String type, String schoolYear, String semester, Integer overOrAdd, Long userId, String userName) {
//        StringBuilder path = new StringBuilder("/warningInfo");
//        path.append("/").append(type);
//        if (distributeLock.getLock(path)) {
        try {
            List<AlarmSettings> alarmSettingsList = alarmSettingsService.getAlarmSettingsByOrgIdAndWarningType(orgId, type);
            HashMap<Integer, List<WarningInformation>> restHasMap = new HashMap<>();
            for (AlarmSettings as : alarmSettingsList) {
                if (null != as && as.getSetupCloseFlag() == 10) {
                    String rules = as.getRuleSet();
                    String[] ruleSetIds;
                    if (rules.indexOf(",") != -1) {
                        ruleSetIds = rules.split(",");
                    } else {
                        ruleSetIds = new String[]{rules};
                    }
                    List<WarningInformation> gradeList = new ArrayList<>();
                    ArrayList<WarningInformation> registerList = null;
                    ArrayList<WarningInformation> absenteeismList = null;
                    List<WarningInformation> performanceFluctuationList = null;
                    List<WarningInformation> supplementAchievementList = null;
                    List<WarningInformation> totalAchievementList = null;
                    List<WarningInformation> cetList = null;
                    List<WarningInformation> attendAbnormalList = null;
                    List<WarningInformation> leaveSchoolList = null;
                    if ("AttendAbnormal".equals(type)) {
                        attendAbnormalList = studyExceptionIndexService.generalAlertInfo(orgId, schoolYear, semester, as);
                        log.info("Generator study exception index count({})", (null == attendAbnormalList ? 0 : attendAbnormalList.size()));
                    }  else if("PerformanceFluctuation".equals(type)) {
                        performanceFluctuationList = standardScoreSemesterIndexService.generalStudentSemesterScoreBdAlertInfo(orgId, schoolYear, semester, as);
                        log.info("Generator score PerformanceFluctuation index count({})", (null == performanceFluctuationList ? 0 : performanceFluctuationList.size()));
                    } else {
                        for (String ruleSetId : ruleSetIds) {
                            if (!StringUtils.isEmpty(ruleSetId)) {
                                RuleParameter rp = ruleParameterService.findById(ruleSetId);
                                if (null != rp) {
                                    Rule rule = null;
                                    List<Rule> ruleList = ruleService.getByName(rp.getRuleName());
                                    if (null != ruleList && ruleList.size() > 0) {
                                        rule = ruleList.get(0);
                                    }
                                    if (null != rule) {
//                                        if (rule.getName().equals("A")) { //迎新
//                                            WarningType wt = warningTypeService.getWarningTypeByOrgIdAndType(orgId, type);
//                                            Date startTime = wt.getStartTime();
//                                            registerList = studentRegisterJob.studenteRegisterJob(orgId, schoolYear, semester, rp.getId(), startTime);
//                                        }
//                                        if (rule.getName().equals("D")) { //旷课
//                                            absenteeismList = rollCallJob.rollCallJob(orgId, schoolYear, semester, rp.getId());
//                                        }
//                                        if (rule.getName().equals("G")) {
//                                            performanceFluctuationList = scoreJob.scoreFluctuateJob(orgId, schoolYear, semester, rp.getId());
//                                        }
                                        if (rule.getName().equals("F")) {
                                            supplementAchievementList = scoreJob.makeUpScoreJob(orgId, schoolYear, semester, rp.getId());
                                        }
                                        if (rule.getName().equals("E")) {
                                            totalAchievementList = scoreJob.failScoreCountJob(orgId, schoolYear, semester, rp.getId());
                                        }
                                        if (rule.getName().equals("B")) {
                                            leaveSchoolList = scoreJob.dropOutJob(orgId, schoolYear, semester, rp.getId());
                                        }
                                        if (rule.getName().equals("H")) {
                                            cetList = scoreJob.cet4ScoreJob(orgId, schoolYear, semester, rp.getId());
                                        }
//                                            if (rule.getName().equals("C")) {
//                                                attendAbnormalList = scoreJob.attendAbnormalJob(orgId, schoolYear, semester, rp.getId());
//                                            }
                                    }
                                }
                            }
                        }
                    }
                    Set<String> studentJobNumberSet = new HashSet<>();
                    if (as.getRelationship().equals("与")) {
                        if (null != registerList && registerList.size() > 0) {
                            if (studentJobNumberSet.isEmpty()) {
                                for (WarningInformation wi : registerList) {
                                    wi.setAlarmSettingsId(as.getId());
                                    wi.setWarningLevel(as.getWarningLevel());
                                    studentJobNumberSet.add(wi.getJobNumber());
                                }
                                gradeList.addAll(registerList);
                            } else {
                                List<WarningInformation> addList = new ArrayList<>();
                                for (WarningInformation gwi : gradeList) {
                                    gwi.setAlarmSettingsId(as.getId());
                                    gwi.setWarningLevel(as.getWarningLevel());
                                    for (WarningInformation wi : registerList) {
                                        if (gwi.getJobNumber().equals(wi.getJobNumber())) {
                                            gwi.setWarningCondition(gwi.getWarningCondition() + "且" + wi.getWarningCondition());
                                            gwi.setWarningSource(gwi.getWarningSource() + ";" + wi.getWarningSource());
                                            addList.add(gwi);
                                            break;
                                        }
                                    }
                                }
                                gradeList = addList;
                            }
                        }
                        if (null != absenteeismList && absenteeismList.size() > 0) {
                            if (studentJobNumberSet.isEmpty()) {
                                for (WarningInformation wi : absenteeismList) {
                                    wi.setAlarmSettingsId(as.getId());
                                    wi.setWarningLevel(as.getWarningLevel());
                                    studentJobNumberSet.add(wi.getJobNumber());
                                }
                                gradeList.addAll(absenteeismList);
                            } else {
                                List<WarningInformation> addList = new ArrayList<>();
                                for (WarningInformation gwi : gradeList) {
                                    gwi.setAlarmSettingsId(as.getId());
                                    gwi.setWarningLevel(as.getWarningLevel());
                                    for (WarningInformation wi : absenteeismList) {
                                        if (gwi.getJobNumber().equals(wi.getJobNumber())) {
                                            gwi.setWarningCondition(gwi.getWarningCondition() + "且" + wi.getWarningCondition());
                                            gwi.setWarningSource(gwi.getWarningSource() + ";" + wi.getWarningSource());
                                            addList.add(gwi);
                                            break;
                                        }
                                    }
                                }
                                gradeList = addList;
                            }
                        }
                        if (null != performanceFluctuationList && performanceFluctuationList.size() > 0) {
                            if (studentJobNumberSet.isEmpty()) {
                                for (WarningInformation wi : performanceFluctuationList) {
                                    wi.setAlarmSettingsId(as.getId());
                                    wi.setWarningLevel(as.getWarningLevel());
                                    studentJobNumberSet.add(wi.getJobNumber());
                                }
                                gradeList.addAll(performanceFluctuationList);
                            } else {
                                List<WarningInformation> addList = new ArrayList<>();
                                for (WarningInformation gwi : gradeList) {
                                    for (WarningInformation wi : performanceFluctuationList) {
                                        gwi.setAlarmSettingsId(as.getId());
                                        gwi.setWarningLevel(as.getWarningLevel());
                                        if (gwi.getJobNumber().equals(wi.getJobNumber())) {
                                            gwi.setWarningCondition(gwi.getWarningCondition() + "且" + wi.getWarningCondition());
                                            gwi.setWarningSource(gwi.getWarningSource() + ";" + wi.getWarningSource());
                                            addList.add(gwi);
                                            break;
                                        }
                                    }
                                }
                                gradeList = addList;
                            }
                        }
                        if (null != supplementAchievementList && supplementAchievementList.size() > 0) {
                            if (studentJobNumberSet.isEmpty()) {
                                for (WarningInformation wi : supplementAchievementList) {
                                    wi.setAlarmSettingsId(as.getId());
                                    wi.setWarningLevel(as.getWarningLevel());
                                    studentJobNumberSet.add(wi.getJobNumber());
                                }
                                gradeList.addAll(supplementAchievementList);
                            } else {
                                List<WarningInformation> addList = new ArrayList<>();
                                for (WarningInformation gwi : gradeList) {
                                    gwi.setAlarmSettingsId(as.getId());
                                    gwi.setWarningLevel(as.getWarningLevel());
                                    for (WarningInformation wi : supplementAchievementList) {
                                        if (gwi.getJobNumber().equals(wi.getJobNumber())) {
                                            gwi.setWarningCondition(gwi.getWarningCondition() + "且" + wi.getWarningCondition());
                                            gwi.setWarningSource(gwi.getWarningSource() + ";" + wi.getWarningSource());
                                            addList.add(gwi);
                                            break;
                                        }
                                    }
                                }
                                gradeList = addList;
                            }
                        }
                        if (null != totalAchievementList && totalAchievementList.size() > 0) {
                            if (studentJobNumberSet.isEmpty()) {
                                for (WarningInformation wi : totalAchievementList) {
                                    wi.setAlarmSettingsId(as.getId());
                                    wi.setWarningLevel(as.getWarningLevel());
                                    studentJobNumberSet.add(wi.getJobNumber());
                                }
                                gradeList.addAll(totalAchievementList);
                            } else {
                                List<WarningInformation> addList = new ArrayList<>();
                                for (WarningInformation gwi : gradeList) {
                                    gwi.setAlarmSettingsId(as.getId());
                                    gwi.setWarningLevel(as.getWarningLevel());
                                    for (WarningInformation wi : totalAchievementList) {
                                        if (gwi.getJobNumber().equals(wi.getJobNumber())) {
                                            gwi.setWarningCondition(gwi.getWarningCondition() + "且" + wi.getWarningCondition());
                                            gwi.setWarningSource(gwi.getWarningSource() + ";" + wi.getWarningSource());
                                            addList.add(gwi);
                                            break;
                                        }
                                    }
                                }
                                gradeList = addList;
                            }
                        }
                        if (null != leaveSchoolList && leaveSchoolList.size() > 0) {
                            if (studentJobNumberSet.isEmpty()) {
                                for (WarningInformation wi : leaveSchoolList) {
                                    wi.setAlarmSettingsId(as.getId());
                                    wi.setWarningLevel(as.getWarningLevel());
                                    studentJobNumberSet.add(wi.getJobNumber());
                                }
                                gradeList.addAll(leaveSchoolList);
                            } else {
                                List<WarningInformation> addList = new ArrayList<>();
                                for (WarningInformation gwi : gradeList) {
                                    gwi.setAlarmSettingsId(as.getId());
                                    gwi.setWarningLevel(as.getWarningLevel());
                                    for (WarningInformation wi : leaveSchoolList) {
                                        if (gwi.getJobNumber().equals(wi.getJobNumber())) {
                                            gwi.setWarningCondition(gwi.getWarningCondition() + "且" + wi.getWarningCondition());
                                            gwi.setWarningSource(gwi.getWarningSource() + ";" + wi.getWarningSource());
                                            addList.add(gwi);
                                            break;
                                        }
                                    }
                                }
                                gradeList = addList;
                            }
                        }
                        if (null != cetList && cetList.size() > 0) {
                            if (studentJobNumberSet.isEmpty()) {
                                for (WarningInformation wi : cetList) {
                                    wi.setAlarmSettingsId(as.getId());
                                    wi.setWarningLevel(as.getWarningLevel());
                                    studentJobNumberSet.add(wi.getJobNumber());
                                }
                                gradeList.addAll(cetList);
                            } else {
                                List<WarningInformation> addList = new ArrayList<>();
                                for (WarningInformation gwi : gradeList) {
                                    gwi.setAlarmSettingsId(as.getId());
                                    gwi.setWarningLevel(as.getWarningLevel());
                                    for (WarningInformation wi : cetList) {
                                        if (gwi.getJobNumber().equals(wi.getJobNumber())) {
                                            gwi.setWarningCondition(gwi.getWarningCondition() + "且" + wi.getWarningCondition());
                                            gwi.setWarningSource(gwi.getWarningSource() + ";" + wi.getWarningSource());
                                            addList.add(gwi);
                                            break;
                                        }
                                    }
                                }
                                gradeList = addList;
                            }
                        }

                        if (null != attendAbnormalList && attendAbnormalList.size() > 0) {
                            if (studentJobNumberSet.isEmpty()) {
                                for (WarningInformation wi : attendAbnormalList) {
                                    wi.setAlarmSettingsId(as.getId());
                                    wi.setWarningLevel(as.getWarningLevel());
                                    studentJobNumberSet.add(wi.getJobNumber());
                                }
                                gradeList.addAll(attendAbnormalList);
                            } else {
                                List<WarningInformation> addList = new ArrayList<>();
                                for (WarningInformation gwi : gradeList) {
                                    gwi.setAlarmSettingsId(as.getId());
                                    gwi.setWarningLevel(as.getWarningLevel());
                                    for (WarningInformation wi : attendAbnormalList) {
                                        if (gwi.getJobNumber().equals(wi.getJobNumber())) {
                                            gwi.setWarningCondition(gwi.getWarningCondition() + "且" + wi.getWarningCondition());
                                            gwi.setWarningSource(gwi.getWarningSource() + ";" + wi.getWarningSource());
                                            addList.add(gwi);
                                            break;
                                        }
                                    }
                                }
                                gradeList = addList;
                            }
                        }
                    } else {
                        if (null != registerList && registerList.size() > 0) {
                            if (studentJobNumberSet.isEmpty()) {
                                for (WarningInformation wi : registerList) {
                                    wi.setAlarmSettingsId(as.getId());
                                    wi.setWarningLevel(as.getWarningLevel());
                                    studentJobNumberSet.add(wi.getJobNumber());
                                }
                                gradeList.addAll(registerList);
                            } else {
                                List<WarningInformation> addList = new ArrayList<>();
                                for (WarningInformation wi : registerList) {
                                    wi.setAlarmSettingsId(as.getId());
                                    wi.setWarningLevel(as.getWarningLevel());
                                    if (studentJobNumberSet.contains(wi.getJobNumber())) {
                                        for (WarningInformation gwi : gradeList) {
                                            if (gwi.getJobNumber().equals(wi.getJobNumber())) {
                                                gwi.setWarningCondition(gwi.getWarningCondition() + "且" + wi.getWarningCondition());
                                                gwi.setWarningSource(gwi.getWarningSource() + ";" + wi.getWarningSource());
                                                addList.add(gwi);
                                                break;
                                            }
                                        }
                                    } else {
                                        studentJobNumberSet.add(wi.getJobNumber());
                                        gradeList.add(wi);
                                    }
                                }
                            }
                        }

                        if (null != absenteeismList && absenteeismList.size() > 0) {
                            if (studentJobNumberSet.isEmpty()) {
                                for (WarningInformation wi : absenteeismList) {
                                    wi.setAlarmSettingsId(as.getId());
                                    wi.setWarningLevel(as.getWarningLevel());
                                    studentJobNumberSet.add(wi.getJobNumber());
                                }
                                gradeList.addAll(absenteeismList);
                            } else {
                                List<WarningInformation> addList = new ArrayList<>();
                                for (WarningInformation wi : absenteeismList) {
                                    wi.setAlarmSettingsId(as.getId());
                                    wi.setWarningLevel(as.getWarningLevel());
                                    if (studentJobNumberSet.contains(wi.getJobNumber())) {
                                        for (WarningInformation gwi : gradeList) {
                                            if (gwi.getJobNumber().equals(wi.getJobNumber())) {
                                                gwi.setWarningCondition(gwi.getWarningCondition() + "且" + wi.getWarningCondition());
                                                gwi.setWarningSource(gwi.getWarningSource() + ";" + wi.getWarningSource());
                                                addList.add(gwi);
                                                break;
                                            }
                                        }
                                    } else {
                                        studentJobNumberSet.add(wi.getJobNumber());
                                        gradeList.add(wi);
                                    }
                                }
                            }
                        }

                        if (null != performanceFluctuationList && performanceFluctuationList.size() > 0) {
                            if (studentJobNumberSet.isEmpty()) {
                                for (WarningInformation wi : performanceFluctuationList) {
                                    wi.setAlarmSettingsId(as.getId());
                                    wi.setWarningLevel(as.getWarningLevel());
                                    studentJobNumberSet.add(wi.getJobNumber());
                                }
                                gradeList.addAll(performanceFluctuationList);
                            } else {
                                List<WarningInformation> addList = new ArrayList<>();
                                for (WarningInformation wi : performanceFluctuationList) {
                                    wi.setAlarmSettingsId(as.getId());
                                    wi.setWarningLevel(as.getWarningLevel());
                                    if (studentJobNumberSet.contains(wi.getJobNumber())) {
                                        for (WarningInformation gwi : gradeList) {
                                            if (gwi.getJobNumber().equals(wi.getJobNumber())) {
                                                gwi.setWarningCondition(gwi.getWarningCondition() + "且" + wi.getWarningCondition());
                                                gwi.setWarningSource(gwi.getWarningSource() + ";" + wi.getWarningSource());
                                                addList.add(gwi);
                                                break;
                                            }
                                        }
                                    } else {
                                        studentJobNumberSet.add(wi.getJobNumber());
                                        gradeList.add(wi);
                                    }
                                }
                            }
                        }

                        if (null != supplementAchievementList && supplementAchievementList.size() > 0) {
                            if (studentJobNumberSet.isEmpty()) {
                                for (WarningInformation wi : supplementAchievementList) {
                                    wi.setAlarmSettingsId(as.getId());
                                    wi.setWarningLevel(as.getWarningLevel());
                                    studentJobNumberSet.add(wi.getJobNumber());
                                }
                                gradeList.addAll(supplementAchievementList);
                            } else {
                                List<WarningInformation> addList = new ArrayList<>();
                                for (WarningInformation wi : supplementAchievementList) {
                                    wi.setAlarmSettingsId(as.getId());
                                    wi.setWarningLevel(as.getWarningLevel());
                                    if (studentJobNumberSet.contains(wi.getJobNumber())) {
                                        for (WarningInformation gwi : gradeList) {
                                            if (gwi.getJobNumber().equals(wi.getJobNumber())) {
                                                gwi.setWarningCondition(gwi.getWarningCondition() + "且" + wi.getWarningCondition());
                                                gwi.setWarningSource(gwi.getWarningSource() + ";" + wi.getWarningSource());
                                                addList.add(gwi);
                                                break;
                                            }
                                        }
                                    } else {
                                        studentJobNumberSet.add(wi.getJobNumber());
                                        gradeList.add(wi);
                                    }
                                }
                            }
                        }


                        if (null != totalAchievementList && totalAchievementList.size() > 0) {
                            if (studentJobNumberSet.isEmpty()) {
                                for (WarningInformation wi : totalAchievementList) {
                                    wi.setAlarmSettingsId(as.getId());
                                    wi.setWarningLevel(as.getWarningLevel());
                                    studentJobNumberSet.add(wi.getJobNumber());
                                }
                                gradeList.addAll(totalAchievementList);
                            } else {
                                List<WarningInformation> addList = new ArrayList<>();
                                for (WarningInformation wi : totalAchievementList) {
                                    wi.setAlarmSettingsId(as.getId());
                                    wi.setWarningLevel(as.getWarningLevel());
                                    if (studentJobNumberSet.contains(wi.getJobNumber())) {
                                        for (WarningInformation gwi : gradeList) {
                                            if (gwi.getJobNumber().equals(wi.getJobNumber())) {
                                                gwi.setWarningCondition(gwi.getWarningCondition() + "且" + wi.getWarningCondition());
                                                gwi.setWarningSource(gwi.getWarningSource() + ";" + wi.getWarningSource());
                                                addList.add(gwi);
                                                break;
                                            }
                                        }
                                    } else {
                                        studentJobNumberSet.add(wi.getJobNumber());
                                        gradeList.add(wi);
                                    }
                                }
                            }
                        }

                        if (null != leaveSchoolList && leaveSchoolList.size() > 0) {
                            if (studentJobNumberSet.isEmpty()) {
                                for (WarningInformation wi : leaveSchoolList) {
                                    wi.setAlarmSettingsId(as.getId());
                                    wi.setWarningLevel(as.getWarningLevel());
                                    studentJobNumberSet.add(wi.getJobNumber());
                                }
                                gradeList.addAll(leaveSchoolList);
                            } else {
                                List<WarningInformation> addList = new ArrayList<>();
                                for (WarningInformation wi : leaveSchoolList) {
                                    wi.setAlarmSettingsId(as.getId());
                                    wi.setWarningLevel(as.getWarningLevel());
                                    if (studentJobNumberSet.contains(wi.getJobNumber())) {
                                        for (WarningInformation gwi : gradeList) {
                                            if (gwi.getJobNumber().equals(wi.getJobNumber())) {
                                                gwi.setWarningCondition(gwi.getWarningCondition() + "且" + wi.getWarningCondition());
                                                gwi.setWarningSource(gwi.getWarningSource() + ";" + wi.getWarningSource());
                                                addList.add(gwi);
                                                break;
                                            }
                                        }
                                    } else {
                                        studentJobNumberSet.add(wi.getJobNumber());
                                        gradeList.add(wi);
                                    }
                                }
                            }
                        }

                        if (null != cetList && cetList.size() > 0) {
                            if (studentJobNumberSet.isEmpty()) {
                                for (WarningInformation wi : cetList) {
                                    wi.setAlarmSettingsId(as.getId());
                                    wi.setWarningLevel(as.getWarningLevel());
                                    studentJobNumberSet.add(wi.getJobNumber());
                                }
                                gradeList.addAll(cetList);
                            } else {
                                List<WarningInformation> addList = new ArrayList<>();
                                for (WarningInformation wi : cetList) {
                                    wi.setAlarmSettingsId(as.getId());
                                    wi.setWarningLevel(as.getWarningLevel());
                                    if (studentJobNumberSet.contains(wi.getJobNumber())) {
                                        for (WarningInformation gwi : gradeList) {
                                            if (gwi.getJobNumber().equals(wi.getJobNumber())) {
                                                gwi.setWarningCondition(gwi.getWarningCondition() + "且" + wi.getWarningCondition());
                                                gwi.setWarningSource(gwi.getWarningSource() + ";" + wi.getWarningSource());
                                                addList.add(gwi);
                                                break;
                                            }
                                        }
                                    } else {
                                        studentJobNumberSet.add(wi.getJobNumber());
                                        gradeList.add(wi);
                                    }
                                }
                            }
                        }
                        if (null != attendAbnormalList && attendAbnormalList.size() > 0) {
                            if (studentJobNumberSet.isEmpty()) {
                                for (WarningInformation wi : attendAbnormalList) {
                                    wi.setAlarmSettingsId(as.getId());
                                    wi.setWarningLevel(as.getWarningLevel());
                                    studentJobNumberSet.add(wi.getJobNumber());
                                }
                                gradeList.addAll(attendAbnormalList);
                            } else {
                                List<WarningInformation> addList = new ArrayList<>();
                                for (WarningInformation wi : attendAbnormalList) {
                                    wi.setAlarmSettingsId(as.getId());
                                    wi.setWarningLevel(as.getWarningLevel());
                                    if (studentJobNumberSet.contains(wi.getJobNumber())) {
                                        for (WarningInformation gwi : gradeList) {
                                            if (gwi.getJobNumber().equals(wi.getJobNumber())) {
                                                gwi.setWarningCondition(gwi.getWarningCondition() + "且" + wi.getWarningCondition());
                                                gwi.setWarningSource(gwi.getWarningSource() + ";" + wi.getWarningSource());
                                                addList.add(gwi);
                                                break;
                                            }
                                        }
                                    } else {
                                        studentJobNumberSet.add(wi.getJobNumber());
                                        gradeList.add(wi);
                                    }
                                }
                            }
                        }
                    }

                    restHasMap.put(as.getWarningLevel(), gradeList);
                }
            }

            WarningType warningType = warningTypeService.getWarningTypeByOrgIdAndType(orgId, type);
            //按照预警等级去重
            LinkedList<WarningInformation> resList = new LinkedList<>();
            Set<String> jobNumber = new HashSet<>();
            int r = 0, o = 0, y = 0;//累计新生成的数据的条数
            List<StudentAlertMsgDTO> dxList = new ArrayList<>();
            if (restHasMap.containsKey(1)) {
                for (WarningInformation w : restHasMap.get(1)) {
                    w.setWarningLevel(1);
                    w.setWarningType(type);
                    jobNumber.add(w.getJobNumber());
                    resList.add(w);
                    addStudentAlertMsgContent(dxList, null != warningType ? warningType.getWarningName() : "", w, 1);
                    r++;
                }
            }
            if (restHasMap.containsKey(2)) {
                for (WarningInformation w : restHasMap.get(2)) {
                    if (!jobNumber.contains(w.getJobNumber())) {
                        w.setWarningLevel(2);
                        w.setWarningType(type);
                        jobNumber.add(w.getJobNumber());
                        resList.add(w);
                        addStudentAlertMsgContent(dxList, null != warningType ? warningType.getWarningName() : "", w, 2);
                        o++;
                    }
                }
            }
            if (restHasMap.containsKey(3)) {
                for (WarningInformation w : restHasMap.get(3)) {
                    if (!jobNumber.contains(w.getJobNumber())) {
                        w.setWarningLevel(3);
                        w.setWarningType(type);
                        jobNumber.add(w.getJobNumber());
                        resList.add(w);
                        addStudentAlertMsgContent(dxList, null != warningType ? warningType.getWarningName() : "", w, 3);
                        y++;
                    }
                }
            }
            AlarmCreateRecord alertLog = new AlarmCreateRecord();
            alertLog.setOrgId(orgId);
            alertLog.setAlarmType(type);//翻译成汉字
            if (null != warningType) {
                alertLog.setAlarmType(warningType.getWarningName());
            }
            alertLog.setCreatedBy(userName);
            alertLog.setCreatedId(userId);
            alertLog.setAddRedNum(r);
            alertLog.setAddOrgNum(o);
            alertLog.setAddYelloNum(y);
            alertLog.setCreatedDate(new Date());

            alertLog.setDeleteRedNum(0);
            alertLog.setDeleteOrgNum(0);
            alertLog.setDeleteYelloNum(0);

            if (null == overOrAdd || 10 == overOrAdd) {
                alertLog.setDeleteRedNum(warningInformationService.countByOrgIdAndTeacherYearAndSemesterAndWarningTypeAndWarningLevel(orgId, type, schoolYear, semester, 1).intValue());
                alertLog.setDeleteOrgNum(warningInformationService.countByOrgIdAndTeacherYearAndSemesterAndWarningTypeAndWarningLevel(orgId, type, schoolYear, semester, 2).intValue());
                alertLog.setDeleteYelloNum(warningInformationService.countByOrgIdAndTeacherYearAndSemesterAndWarningTypeAndWarningLevel(orgId, type, schoolYear, semester, 3).intValue());

                warningInformationService.deleteWarningInformation(orgId, type, schoolYear, semester);
            }
            warningInformationService.save(resList);
            alarmCreateRecordService.save(alertLog);
            if (dxList.size() > 0) {
                log.info("有效手机号码数据:{}", dxList.size());
                //发送短信
                for (StudentAlertMsgDTO sm : dxList) {
                    try {
                        restUtil.post(zhixinUrl + "/api/web/v1/msg/send?phone=" + sm.getPhone() + "&msg=" + sm.getContent(), null);
                        log.info("给[{}]电话号码 发送告警短信:[{}]成功", sm.getPhone(), sm.getContent());
                    } catch (Exception e) {
                        log.info("给[{}]电话号码 发送告警短信:[{}]失败", sm.getPhone(), sm.getContent());
                    }
                }
            } else {
                log.info("没有有效手机号码数据");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
//        finally{
//            distributeLock.delete(path);
//        }
//    }
    }

    private void addStudentAlertMsgContent(List<StudentAlertMsgDTO> list, String warningTypeName, WarningInformation w, int alertLevel) {
        if (!StringUtils.isEmpty(w.getPhone()) && 11 == w.getPhone().length() && w.getPhone().startsWith("1") && org.apache.commons.lang.StringUtils.isNumeric(w.getPhone())) {//电话号码有值并且是11位，以1开始，并且全部是数字
            StudentAlertMsgDTO d = new StudentAlertMsgDTO();
            StringBuilder sb = new StringBuilder();
            sb.append(w.getName()).append("同学，本学期有一条");
            switch (alertLevel) {
                case 1: sb.append("红");
                break;
                case 2:sb.append("橙");
                    break;
                case 3:sb.append("黄");
                    break;
                    default:
            }
            sb.append("色").append(warningTypeName).append("通知到你，预警内容[");
            sb.append(w.getWarningStandard());
            sb.append("]。希望你认真查找原因，努力学习，以便顺利完成学业。【桂工教务处】");
            d.setPhone(w.getPhone());
            d.setContent(sb.toString());
            list.add(d);
        }
    }
}
