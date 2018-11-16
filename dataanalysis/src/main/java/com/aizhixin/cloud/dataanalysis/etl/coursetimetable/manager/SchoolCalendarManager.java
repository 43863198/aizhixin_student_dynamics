package com.aizhixin.cloud.dataanalysis.etl.coursetimetable.manager;


import com.aizhixin.cloud.dataanalysis.etl.cet.dto.SchoolCalendarDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Transactional
@Component
public class SchoolCalendarManager {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    /**
     * 查询桂理校历
     */
    @Transactional(readOnly = true)
    public List<SchoolCalendarDTO> queryGuiLiCalendar() {
        String sql = "SELECT XN, XQ, KSSJ, ZZS FROM t_glut_dc_xlxx ORDER BY CONCAT(XN,XQ) DESC";
        return jdbcTemplate.query(sql.toString(),
                (ResultSet rs, int rowNum) -> new SchoolCalendarDTO(
                        rs.getString("XN"), rs.getString("XQ"),
                        rs.getDate("KSSJ"), rs.getInt("ZZS")
                )
        );
    }

    public void cleanCalendar() {
        jdbcTemplate.execute("TRUNCATE t_school_calendar");
    }

    public  void writeGuiliCalendar(List<SchoolCalendarDTO> list, Long orgId) {
        String sql = "INSERT INTO t_school_calendar (ORG_ID, TEACHER_YEAR, SEMESTER, START_TIME, END_TIME, WEEK) VALUES (?, ?, ?, ?, ?, ?)";

        jdbcTemplate.batchUpdate(sql, new BatchPreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement preparedStatement, int i) throws SQLException {
                //ORG_ID, TEACHER_YEAR, SEMESTER, START_TIME, END_TIME, WEEK
                SchoolCalendarDTO d = list.get(i);
                preparedStatement.setLong(1, orgId);//ORG_ID
                preparedStatement.setString(2, d.getXn());
                preparedStatement.setString(3, d.getXq());
                preparedStatement.setDate(4, new java.sql.Date(d.getKsrq().getTime()));
                preparedStatement.setDate(5, new java.sql.Date(d.getJsrq().getTime()));
                preparedStatement.setInt(6, d.getZs());
            }

            @Override
            public int getBatchSize() {
                return list.size();
            }
        });
    }
}
