package com.aizhixin.cloud.dataanalysis.analysis.service;

import com.aizhixin.cloud.dataanalysis.analysis.constant.DataType;
import com.aizhixin.cloud.dataanalysis.analysis.dto.TeacherYearSemesterDTO;
import com.aizhixin.cloud.dataanalysis.analysis.dto.TeacherlYearData;
import com.aizhixin.cloud.dataanalysis.analysis.entity.SchoolYearTerm;
import com.aizhixin.cloud.dataanalysis.analysis.respository.SchoolYearTermResposotory;
import com.aizhixin.cloud.dataanalysis.analysis.vo.ExamArrangeVO;
import org.apache.commons.lang.StringUtils;
import org.hibernate.SQLQuery;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.*;

@Component
@Transactional
public class SchoolYearTermService {
    @Autowired
    private SchoolYearTermResposotory schoolYearTermResposotory;
    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Autowired
    private EntityManager em;

    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

    public void initSchoolYearTerm(Long orgId) {
        schoolYearTermResposotory.deleteByOrgId(orgId);
        for (DataType dataType : DataType.values()) {
            List<SchoolYearTerm> list = this.getSchoolYearTermByType(orgId, dataType);
            schoolYearTermResposotory.save(list);
        }

    }
    public void deleteSchoolYearTerm(Long orgId,String dataType) {
        schoolYearTermResposotory.deleteByOrgIdAndDataType(orgId, dataType);
    }

    public void saveSchoolYearTerm(Set<SchoolYearTerm> schoolYearTerms) {
        for(SchoolYearTerm yt: schoolYearTerms){
            if(null!=yt.getSemester()&&null!=yt.getTeacherYear()) {
                yt.setDataType(DataType.t_teaching_score_statistics.getIndex() + "");
                this.deleteSchoolYearTerm(yt.getOrgId(), yt.getDataType());
            }
        }
        schoolYearTermResposotory.save(schoolYearTerms);
    }


    public List<TeacherlYearData> getSchoolYearTerm(Long orgId,Integer type) {
        RowMapper<TeacherlYearData> rowMapper=new RowMapper<TeacherlYearData>() {
            @Override
            public TeacherlYearData mapRow(ResultSet rs, int i) throws SQLException {
                TeacherlYearData teacherlYearData=new TeacherlYearData();
                teacherlYearData.setTeacherYear(rs.getInt("TEACHER_YEAR"));
                teacherlYearData.setSemester(rs.getInt("SEMESTER"));
                return teacherlYearData;
            }
        };
        List<TeacherlYearData> result = new ArrayList();
        String sql="SELECT DISTINCT SEMESTER ,TEACHER_YEAR  FROM t_school_year_term  where ORG_ID=" + orgId + " and data_type="+type+" ORDER BY TEACHER_YEAR DESC,SEMESTER DESC";
        result=jdbcTemplate.query(sql,rowMapper);
        return result;
    }

    public List<SchoolYearTerm> getSchoolYearTermByType(Long orgId, DataType dataType) {
        String tableName = dataType.getType(dataType.getIndex());
        String type = dataType.getIndex() + "";
        RowMapper<SchoolYearTerm> rowMapper = new RowMapper<SchoolYearTerm>() {
            @Override
            public SchoolYearTerm mapRow(ResultSet rs, int i) throws SQLException {
                SchoolYearTerm schoolYearTerm = new SchoolYearTerm();
                schoolYearTerm.setOrgId(orgId);
                schoolYearTerm.setDataType(type);
                schoolYearTerm.setTeacherYear(rs.getInt("TEACHER_YEAR"));
                schoolYearTerm.setSemester(rs.getInt("SEMESTER"));
                return schoolYearTerm;
            }
        };
        List<SchoolYearTerm> list = new ArrayList<>();

        String sql = "SELECT DISTINCT SEMESTER ,TEACHER_YEAR  FROM " + tableName + "  where ORG_ID=" + orgId + " ORDER BY TEACHER_YEAR DESC,SEMESTER DESC";
        try {
            list = jdbcTemplate.query(sql, rowMapper);
        } catch (EmptyResultDataAccessException emptyResultDataAccessException) {
            return null;
        }

        return list;
    }


    public Map<String, Object> getTeacherYearSemester(Long orgId) {
        Map<String, Object> result = new HashMap<>();
        List<TeacherYearSemesterDTO> tysList = new ArrayList<>();
        try {
            StringBuilder sql = new StringBuilder("SELECT  TEACHER_YEAR as teacherYear, SEMESTER as semester FROM t_school_calendar WHERE 1=1");
            if (null != orgId) {
                sql.append(" AND ORG_ID = " + orgId + "");
            }
            sql.append(" ORDER BY TEACHER_YEAR DESC, SEMESTER DESC");
            Query sq = em.createNativeQuery(sql.toString());
            sq.unwrap(SQLQuery.class).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
            List<Object> res = sq.getResultList();
            for (Object obj : res) {
                Map row = (Map) obj;
                TeacherYearSemesterDTO tys = new TeacherYearSemesterDTO();
                if (null != row.get("teacherYear")) {
                    tys.setTeacherYear(row.get("teacherYear").toString());
                    if (null != row.get("semester")) {
                        tys.setSemester(row.get("semester").toString());
                    }
                    tysList.add(tys);
                }
            }
            result.put("success", true);
            result.put("data", tysList);
            return result;
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", "获取学年学期失败！");
            return result;
        }
    }


    public Map<String, Object> getSchoolCalendar(Long orgId, String teacherYear, String semester) {
        Map<String, Object> result = new HashMap<>();
        List<TeacherYearSemesterDTO> tysList = new ArrayList<>();
        Map<String, Object> condition = new HashMap<>();
        try {
            StringBuilder sql = new StringBuilder("SELECT  TEACHER_YEAR as teacherYear, SEMESTER as semester, START_TIME as startTime, WEEK as week, END_TIME as endTime FROM t_school_calendar WHERE 1=1");
            if (null != orgId) {
                sql.append(" and ORG_ID = :orgId");
                condition.put("orgId", orgId);
            }
            if (!StringUtils.isBlank(teacherYear)) {
                sql.append(" and TEACHER_YEAR = :teacherYear");
                condition.put("teacherYear", teacherYear);
            }
            if (!StringUtils.isBlank(semester)) {
                sql.append(" and SEMESTER = :semester");
                condition.put("semester", semester);
            }
            Query sq = em.createNativeQuery(sql.toString());
            sq.unwrap(SQLQuery.class).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
            for (Map.Entry<String, Object> e : condition.entrySet()) {
                sq.setParameter(e.getKey(), e.getValue());
            }
            List<Object> res = sq.getResultList();
            for (Object obj : res) {
                Map row = (Map) obj;
                TeacherYearSemesterDTO tys = new TeacherYearSemesterDTO();
                if (null != row.get("teacherYear")) {
                    tys.setTeacherYear(row.get("teacherYear").toString());
                    if (null != row.get("semester")) {
                        tys.setSemester(row.get("semester").toString());
                        if(null!=row.get("startTime")){
                            tys.setStartTime(row.get("startTime").toString());
                        }
                        if(null!=row.get("week")){
                            tys.setWeek(Integer.valueOf(row.get("week").toString()));
                        }
                        if(null!=row.get("endTime")){
                            tys.setEndTime(row.get("endTime").toString());
                        }
                    }
                    tysList.add(tys);
                }
            }
            result.put("success", true);
            result.put("data", tysList);
            return result;
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", "获取学年学期失败！");
            return result;
        }
    }

    public Map<String, Object> getCurrentSchoolCalendar(Long orgId) {
        Map<String, Object> result = new HashMap<>();
        List<TeacherYearSemesterDTO> tysList = new ArrayList<>();
        Map<String, Object> condition = new HashMap<>();
        try {
            //StringBuilder sql = new StringBuilder("SELECT  TEACHER_YEAR as teacherYear, SEMESTER as semester, START_TIME as startTime, (SELECT TIMESTAMPDIFF(WEEK,START_TIME,CURDATE())) as week, END_TIME as endTime FROM t_school_calendar WHERE 1=1");
            StringBuilder sql = new StringBuilder("SELECT  TEACHER_YEAR as teacherYear, SEMESTER as semester, START_TIME as startTime, WEEK as week, END_TIME as endTime,CURDATE() as currentTime FROM t_school_calendar WHERE 1=1");
            if (null != orgId) {
                sql.append(" AND ORG_ID = :orgId");
                condition.put("orgId", orgId);
            }
            sql.append(" AND CURDATE() BETWEEN START_TIME AND END_TIME");

            Query sq = em.createNativeQuery(sql.toString());
            sq.unwrap(SQLQuery.class).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
            for (Map.Entry<String, Object> e : condition.entrySet()) {
                sq.setParameter(e.getKey(), e.getValue());
            }
            List<Object> res = sq.getResultList();
            for (Object obj : res) {
                Map row = (Map) obj;
                TeacherYearSemesterDTO tys = new TeacherYearSemesterDTO();
                if (null != row.get("teacherYear")) {
                    tys.setTeacherYear(row.get("teacherYear").toString());
                    if (null != row.get("semester")) {
                        tys.setSemester(row.get("semester").toString());
                        if(null!=row.get("startTime")){
                            tys.setStartTime(row.get("startTime").toString());
                        }
                        if(null!=row.get("week")){
                            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                            tys.setWeek(calcWeekOffset(sdf.parse(row.get("startTime").toString()),sdf.parse(row.get("currentTime").toString())) + 1);
                        }
                        if(null!=row.get("endTime")){
                            tys.setEndTime(row.get("endTime").toString());
                        }
                    }
                    tysList.add(tys);
                }
            }
            result.put("success", true);
            result.put("data", tysList);
            return result;
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", "获取学当前年学期失败！");
            return result;
        }
    }

    public int calcDayOffset(Date date1, Date date2) {
        Calendar cal1 = Calendar.getInstance();
        cal1.setTime(date1);

        Calendar cal2 = Calendar.getInstance();
        cal2.setTime(date2);
        int day1 = cal1.get(Calendar.DAY_OF_YEAR);
        int day2 = cal2.get(Calendar.DAY_OF_YEAR);

        int year1 = cal1.get(Calendar.YEAR);
        int year2 = cal2.get(Calendar.YEAR);
        if (year1 != year2) {  //同一年
            int timeDistance = 0;
            for (int i = year1; i < year2; i++) {
                if (i % 4 == 0 && i % 100 != 0 || i % 400 == 0) {  //闰年
                    timeDistance += 366;
                } else {  //不是闰年

                    timeDistance += 365;
                }
            }
            return timeDistance + (day2 - day1);
        } else { //不同年
            return day2 - day1;
        }
    }

    /**
     * date2比date1多的周数
     * @param startTime
     * @param endTime
     * @return
     */
    public int calcWeekOffset(Date startTime, Date endTime) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(startTime);
        int dayOfWeek = cal.get(Calendar.DAY_OF_WEEK);
        dayOfWeek = dayOfWeek - 1;
        if (dayOfWeek == 0) dayOfWeek = 7;

        int dayOffset = calcDayOffset(startTime, endTime);

        int weekOffset = dayOffset / 7;
        int a;
        if (dayOffset > 0) {
            a = (dayOffset % 7 + dayOfWeek > 7) ? 1 : 0;
        } else {
            a = (dayOfWeek + dayOffset % 7 < 1) ? -1 : 0;
        }
        weekOffset = weekOffset + a;
        return weekOffset;
    }




    public Map<String, Object> getYearSemesterWeek(Long orgId) {
        Map<String, Object> result = new HashMap<>();
        String dateTime = "";
        try {
            Map<String, Object> current = this.getCurrentSchoolCalendar(orgId);
            if(null!=current){
                if(Boolean.valueOf(current.get("success").toString())){
                    List<TeacherYearSemesterDTO> time = (List<TeacherYearSemesterDTO>)current.get("data");
                    String[] weekDaysName = { "星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六" };
                    String[] weekDaysCode = { "0", "1", "2", "3", "4", "5", "6" };
                    Calendar calendar = Calendar.getInstance();
                    calendar.setTime(new Date());
                    int intWeek = calendar.get(Calendar.DAY_OF_WEEK) - 1;
                    String day =  weekDaysName[intWeek];
                    if(time.size()>0){
                        TeacherYearSemesterDTO  currentTime = time.get(0);
                        String year = currentTime.getTeacherYear();
                        String semester = currentTime.getSemester();
                        int week = currentTime.getWeek();
                        dateTime = year+"年"+semester+"季学期第"+week+"周 "+day;
                    }else {
                        int year = calendar.get(Calendar.YEAR);
                        int month = calendar.get(Calendar.MONTH)+1;
                        int da = calendar.get(Calendar.DAY_OF_MONTH);
                        dateTime = year+"年"+month+"月"+da+"日 "+day;
                    }
                }
            }
            result.put("success", true);
            result.put("data", dateTime);
            return result;
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", "获取学当前年学期失败！");
            return result;
        }
    }




}
