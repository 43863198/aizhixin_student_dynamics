package com.aizhixin.cloud.dataanalysis.analysis.service;

import com.aizhixin.cloud.dataanalysis.analysis.constant.DataType;
import com.aizhixin.cloud.dataanalysis.analysis.dto.TeacherlYearData;
import com.aizhixin.cloud.dataanalysis.analysis.entity.SchoolYearTerm;
import com.aizhixin.cloud.dataanalysis.analysis.respository.SchoolYearTermResposotory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Component
@Transactional
public class SchoolYearTermService {
    @Autowired
    private SchoolYearTermResposotory schoolYearTermResposotory;
    @Autowired
    private JdbcTemplate jdbcTemplate;

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
}
