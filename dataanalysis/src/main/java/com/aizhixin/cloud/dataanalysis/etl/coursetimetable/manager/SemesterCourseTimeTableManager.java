package com.aizhixin.cloud.dataanalysis.etl.coursetimetable.manager;


import com.aizhixin.cloud.dataanalysis.etl.coursetimetable.dto.CourseTimeTableDTO;
import com.aizhixin.cloud.dataanalysis.etl.coursetimetable.dto.CourseTimeTableOutDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.List;

@Transactional
@Component
public class SemesterCourseTimeTableManager {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    /**
     * 查询桂理排课数据（一次处理一个学期）
     */
    @Transactional(readOnly = true)
    public List<CourseTimeTableDTO> queryGuiLiPksj(String xn, String xq) {
        String sql = "SELECT ORG_ID, COURSE_TABLE_NUMBER,TEACHING_CLASS_NUMBER, TEACHING_CLASS_NAME, WEEKLY, CURRICULA_TIME " +
                "FROM t_course_timetable  WHERE PLACE IS NOT NULL AND CURRICULA_TIME IS NOT NULL AND SCHOOL_YEAR=? AND SEMESTER_NUMBER=?";
        return jdbcTemplate.query(sql.toString(),
                new Object[]{xn, xq},
                new int [] {Types.VARCHAR, Types.VARCHAR},
                (ResultSet rs, int rowNum) -> new CourseTimeTableDTO (
                        rs.getLong("ORG_ID"), rs.getString("COURSE_TABLE_NUMBER"), rs.getString("TEACHING_CLASS_NUMBER"),
                        rs.getString("TEACHING_CLASS_NAME"), rs.getString("WEEKLY"), rs.getString("CURRICULA_TIME")
                )
        );
    }

    public void  cleanPksj() {
        jdbcTemplate.execute("TRUNCATE t_curriculum_schedule");
    }

    public  void writeGuiliPksj(List<CourseTimeTableOutDTO> list) {
        String sql = "INSERT INTO t_curriculum_schedule (ORG_ID, TEACHING_CLASS_NUMBER, TEACHING_CLASS_NAME, START_WEEK, END_WEEK, DAY_OF_THE_WEEK, START_PERIOD, PERIOD_NUM) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        jdbcTemplate.batchUpdate(sql, new BatchPreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement preparedStatement, int i) throws SQLException {
                //(XN,XQM,XXDM,P_BH,BH,CKRS,CKRC,BXCKRC,BXBJGRC,KCS,JDZF,CJZF)
                CourseTimeTableOutDTO d = list.get(i);
                preparedStatement.setLong(1, d.getOrgId());//ORG_ID
                preparedStatement.setString(2, d.getJxbh());
                preparedStatement.setString(3, d.getJxbmc());
                preparedStatement.setInt(4, d.getQsz());
                preparedStatement.setInt(5, d.getJsz());
                preparedStatement.setInt(6, d.getXqj());
                preparedStatement.setInt(7, d.getDjj());
                preparedStatement.setInt(8, d.getCxj());
            }

            @Override
            public int getBatchSize() {
                return list.size();
            }
        });
    }
}
