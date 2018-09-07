package com.aizhixin.cloud.dataanalysis.simulate.service;

import com.aizhixin.cloud.dataanalysis.analysis.dto.TeacherYearSemesterDTO;
import com.aizhixin.cloud.dataanalysis.analysis.entity.AttendanceStatistics;
import com.aizhixin.cloud.dataanalysis.analysis.respository.AttendanceStatisticsRepository;
import com.aizhixin.cloud.dataanalysis.analysis.service.SchoolYearTermService;
import com.aizhixin.cloud.dataanalysis.bz.manager.CetStandardScoreManager;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.hibernate.SQLQuery;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
@Slf4j
public class AddAttendanceDataService {
    @Autowired
    private EntityManager em;
    @Autowired
    private CetStandardScoreManager cetStandardScoreManager;
    @Autowired
    private SchoolYearTermService schoolYearTermService;
    @Autowired
    private AttendanceStatisticsRepository attendanceStatisticsRepository;

    public Map<String, Object> addDate(Long orgId, String collegeCode, String professionCode,String className, String xnxq) {
        Map<String, Object> result = new HashMap<>();
        Map<String, Object> condition = new HashMap<>();
        List<AttendanceStatistics> resultList = new ArrayList<>();
        try {
            int index = xnxq.lastIndexOf("-");
            //标准格式学期学年 格式如:2017-2018-2
            String t = null;
            String s = null;

            //转换为学校日历表中的学年学期 格式如:2018年春
            String st = null;
            String sj = null;

            //转换为课程计划表里的学年学期 格式如:2018-1
            String tt = null;
            String ss = null;

            if (index > 0) {
                t = xnxq.substring(0, index);
                s = xnxq.substring(index + 1);
                if ("2".equals(s)) {
                    tt = t.split("-")[1];
                    ss = "1";
                    st = xnxq.substring(0, index).split("-")[1];
                    sj = "春";
                } else {
                    tt = t.split("-")[0];
                    ss = "2";
                    st = xnxq.substring(0, index).split("-")[0];
                    sj = "秋";
                }
            }

            if(StringUtils.isEmpty(professionCode) && StringUtils.isEmpty(className)){
                attendanceStatisticsRepository.deleteByXxdmAndYxsh(orgId,collegeCode);
            }else if(StringUtils.isEmpty(className)){
                attendanceStatisticsRepository.deleteByXxdmAndYxshAndZyh(orgId,collegeCode,professionCode);
            }else{
               attendanceStatisticsRepository.deleteByXxdmAndYxshAndZyhAndBjmc(orgId,collegeCode,professionCode,className);
            }


            Date start = null;
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Map<String, Object> schoolCalendar = schoolYearTermService.getSchoolCalendar(orgId, st, sj);
            if (null != schoolCalendar) {
                if (null != schoolCalendar.get("success") && Boolean.parseBoolean(schoolCalendar.get("success").toString())) {
                    List<TeacherYearSemesterDTO> tysList = (List<TeacherYearSemesterDTO>) schoolCalendar.get("data");
                    if (tysList.size() > 0) {
                        Date date = sdf.parse(tysList.get(0).getStartTime());
                        start = date;
                    }
                }
            }
            String weekly = null;
            String curricula_time = null;

            StringBuilder sql = new StringBuilder("SELECT " +
                    "tct.TEACHING_CLASS_NUMBER as TEACHING_CLASS_NUMBER,tct.CURRICULA_TIME as CURRICULA_TIME,tct.WEEKLY as WEEKLY,tct.COURSE_NATURE as COURSE_NATURE,tct.COURSE_TABLE_NUMBER as COURSE_TABLE_NUMBER," +
                    "tct.SET_UP_UNIT as SET_UP_UNIT,tct.TEACHER_NAME as TEACHER_NAME,tct.TEACHER_JOB_NUMBER as TEACHER_JOB_NUMBER," +
                    "xs.XXID as XXID,xs.XH as XH,xs.XM as XM,xs.YXSH as YXSH,xs.YXSMC as YXSMC,xs.ZYH as ZYH,xs.ZYMC as ZYMC,xs.BH as BH,xs.BJMC as BJMC " +
                    "FROM t_course_timetable tct," +
                    "(SELECT tx.XXID as XXID, tx.XH as XH,tx.XM as XM,tx.YXSH as YXSH,tx.YXSMC as YXSMC,tx.ZYH as ZYH,tx.ZYMC as ZYMC,tx.BH as BH,tx.BJMC as BJMC " +
                    "FROM t_xsjbxx  tx where tx.XXID = :orgId and tx.YXSH=:collegeCode and tx.ZYH=:professionCode and tx.BJMC=:className) xs " +
                    "where LOCATE(xs.BJMC,tct.CLASS_NAME) > 0 and tct.SCHOOL_YEAR=:teacherYear " +
                    "and tct.SEMESTER_NUMBER=:semester and tct.TEACHER_NAME IS NOT NULL and  tct.TEACHER_NAME <>'' and tct.CURRICULA_TIME <>'  -'");
            condition.put("orgId", orgId);
            condition.put("collegeCode", collegeCode);
            if(StringUtils.isBlank(professionCode)){
                sql = new StringBuilder(sql.toString().replace("and tx.ZYH=:professionCode",""));
            }else{
                condition.put("professionCode", professionCode);
            }

            if(StringUtils.isBlank(className)){
                sql = new StringBuilder(sql.toString().replace("and tx.BJMC=:className",""));
            }else{
                condition.put("className", className);
            }

            condition.put("teacherYear", tt);
            condition.put("semester", ss);
            Query sq = em.createNativeQuery(sql.toString());
            sq.unwrap(SQLQuery.class).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
            for (Map.Entry<String, Object> e : condition.entrySet()) {
                sq.setParameter(e.getKey(), e.getValue());
            }
            List<Object> res = sq.getResultList();
            if (null != res) {
                for (Object row : res) {
                    Map map = (Map) row;
                    AttendanceStatistics attendanceStatistics = new AttendanceStatistics();
                    attendanceStatistics.setXn(t);
                    attendanceStatistics.setXqm(s);
                    if (null != map.get("XXID")) {
                        attendanceStatistics.setXxdm(Long.parseLong(map.get("XXID").toString()));
                    }
                    if (null != map.get("CURRICULA_TIME")) {
                        curricula_time = map.get("CURRICULA_TIME").toString();
                        attendanceStatistics.setKkrq(map.get("CURRICULA_TIME").toString());
                    }
                    if (null != map.get("WEEKLY")) {
                        weekly = map.get("WEEKLY").toString();
                        attendanceStatistics.setSkzc(map.get("WEEKLY").toString());
                    }
                    if (null != map.get("COURSE_NATURE")) {
                        attendanceStatistics.setKcxz(map.get("COURSE_NATURE").toString());
                    }
                    if (null != map.get("SET_UP_UNIT")) {
                        attendanceStatistics.setDwh(map.get("SET_UP_UNIT").toString());
                        Map collegeMap = cetStandardScoreManager.getCollegeName(map.get("SET_UP_UNIT").toString());
                        attendanceStatistics.setDwmc(collegeMap.get("simple_name") != null ? collegeMap.get("simple_name").toString() : collegeMap.get("name").toString());
                    }
                    if (null != map.get("TEACHER_NAME")) {
                        attendanceStatistics.setJsxm(map.get("TEACHER_NAME").toString());
                    }
                    if (null != map.get("TEACHER_JOB_NUMBER")) {
                        attendanceStatistics.setJsgh(map.get("TEACHER_JOB_NUMBER").toString());
                    }
                    if (null != map.get("XH")) {
                        attendanceStatistics.setXh(map.get("XH").toString());
                    }
                    if (null != map.get("XM")) {
                        attendanceStatistics.setXm(map.get("XM").toString());
                    }
                    if (null != map.get("YXSH")) {
                        attendanceStatistics.setYxsh(map.get("YXSH").toString());
                    }
                    if (null != map.get("YXSMC")) {
                        attendanceStatistics.setYxmc(map.get("YXSMC").toString());
                    }
                    if (null != map.get("ZYH")) {
                        attendanceStatistics.setZyh(map.get("ZYH").toString());
                    }
                    if (null != map.get("ZYMC")) {
                        attendanceStatistics.setZymc(map.get("ZYMC").toString());
                    }
                    if (null != map.get("BH")) {
                        attendanceStatistics.setBh(map.get("BH").toString());
                    }
                    if (null != map.get("BJMC")) {
                        attendanceStatistics.setBjmc(map.get("BJMC").toString());
                    }
                    if (null != map.get("COURSE_TABLE_NUMBER")) {
                        attendanceStatistics.setKch(map.get("COURSE_TABLE_NUMBER").toString());
                    }
                    StringBuilder csql = new StringBuilder("select DISTINCT(COURSE_NAME) from t_teaching_class where LOCATE('" + map.get("TEACHING_CLASS_NUMBER").toString() + "',TEACHING_CLASS_NUMBER) > 0");
                    Query csq = em.createNativeQuery(csql.toString());
                    csq.unwrap(SQLQuery.class).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
                    Object cres = csq.getSingleResult();
                    Map cresMap = (Map) cres;
                    if (null != cresMap.get("COURSE_NAME")) {
                        attendanceStatistics.setKcmc(cresMap.get("COURSE_NAME").toString());
                    }
                    List<String> list = generateKqrq(weekly, curricula_time, start);
                    for (String date : list) {
                        AttendanceStatistics attendanceStatistics2 = (AttendanceStatistics) attendanceStatistics.clone();
                        attendanceStatistics2.setKqrq(date);
                        attendanceStatistics2.setKqjg(new Random().nextInt(4) + 1 + "");
                        resultList.add(attendanceStatistics2);
                    }
                }
            }
            log.info("生成{}条考勤数据", resultList.size());
            attendanceStatisticsRepository.save(resultList);
            result.put("success", true);
            result.put("message", "生成考勤数据成功！");
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            log.info("生成考勤数据异常:{}", e.getStackTrace());
            result.put("success", false);
            result.put("message", "生成考勤数据异常！");
            return result;
        }

    }

    public List<String> generateKqrq(String weekly, String curricula_time, Date start) {
        List<String> list = new ArrayList<>();
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            String[] curricula_time_array = curricula_time.split(" ");
            String xq = curricula_time_array[0];
            String kqrq;
            Calendar calendar = Calendar.getInstance();
            weekly = weekly.replace("周", "").replace("第", "");
            if (StringUtils.isNumeric(weekly)) {
                calendar.setTime(start);
                calendar.add(Calendar.WEEK_OF_YEAR, Integer.parseInt(weekly));
                calendar.set(Calendar.DAY_OF_WEEK, getXq(xq));
                kqrq = sdf.format(calendar.getTime());
                list.add(kqrq);
            } else if (weekly.indexOf("双") > 0) {
                weekly = weekly.replace("双", "");
                String[] doubleweek = weekly.split("-");
                Integer doubleStart = Integer.parseInt(doubleweek[0]);
                Integer doubleEnd = Integer.parseInt(doubleweek[1]);
                while (doubleStart <= doubleEnd) {
                    calendar.setTime(start);
                    calendar.add(Calendar.WEEK_OF_YEAR, doubleStart);
                    calendar.set(Calendar.DAY_OF_WEEK, getXq(xq));
                    kqrq = sdf.format(calendar.getTime());
                    list.add(kqrq);
                    doubleStart += 2;
                }
            } else if (weekly.indexOf(",") > 0) {
                String[] week = weekly.split(",");
                for (int i = 0; i < week.length; i++) {
                    Integer weekStart = Integer.parseInt(week[i].split("-")[0]);
                    Integer weekEnd = Integer.parseInt(week[i].split("-")[1]);
                    while (weekStart <= weekEnd) {
                        calendar.setTime(start);
                        calendar.add(Calendar.WEEK_OF_YEAR, weekStart);
                        calendar.set(Calendar.DAY_OF_WEEK, getXq(xq));
                        kqrq = sdf.format(calendar.getTime());
                        list.add(kqrq);
                        weekStart += 1;
                    }
                }
            } else if (weekly.indexOf("单") > 0) {
                weekly = weekly.replace("单", "");
                String[] doubleweek = weekly.split("-");
                Integer doubleStart = Integer.parseInt(doubleweek[0]);
                Integer doubleEnd = Integer.parseInt(doubleweek[1]);
                while (doubleStart <= doubleEnd) {
                    calendar.setTime(start);
                    calendar.add(Calendar.WEEK_OF_YEAR, doubleStart);
                    calendar.set(Calendar.DAY_OF_WEEK, getXq(xq));
                    kqrq = sdf.format(calendar.getTime());
                    list.add(kqrq);
                    doubleStart += 2;
                }
            } else {
                String[] doubleweek = weekly.split("-");
                Integer doubleStart = Integer.parseInt(doubleweek[0]);
                Integer doubleEnd = Integer.parseInt(doubleweek[1]);
                while (doubleStart <= doubleEnd) {
                    calendar.setTime(start);
                    calendar.add(Calendar.WEEK_OF_YEAR, doubleStart);
                    calendar.set(Calendar.DAY_OF_WEEK, getXq(xq));
                    kqrq = sdf.format(calendar.getTime());
                    list.add(kqrq);
                    doubleStart += 1;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.info("生成考勤日期异常:{}", e.getStackTrace());
        }
        return list;
    }

    public Integer getXq(String xq) {
        Integer result = 0;
        switch (xq) {
            case "1":
                result = Calendar.MONTH;
                break;
            case "2":
                result = Calendar.TUESDAY;
                break;
            case "3":
                result = Calendar.WEDNESDAY;
                break;
            case "4":
                result = Calendar.THURSDAY;
                break;
            case "5":
                result = Calendar.FRIDAY;
                break;
            case "6":
                result = Calendar.SATURDAY;
                break;
            case "7":
                result = Calendar.SUNDAY;
                break;
            default:

        }
        return result;
    }
}
