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
            StringBuilder sql = new StringBuilder("SELECT  TEACHER_YEAR as teacherYear, SEMESTER as semester, START_TIME as startTime, WEEK as week FROM t_school_calendar WHERE 1=1");
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
                            if(null!=row.get("week")){
                                tys.setWeek(Integer.valueOf(row.get("week").toString()));
                            }
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

}
