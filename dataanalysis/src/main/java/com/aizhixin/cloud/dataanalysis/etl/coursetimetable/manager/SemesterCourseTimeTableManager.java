package com.aizhixin.cloud.dataanalysis.etl.coursetimetable.manager;


import com.aizhixin.cloud.dataanalysis.etl.coursetimetable.dto.CourseTimeTableDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.sql.ResultSet;
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
}
