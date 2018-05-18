package com.aizhixin.cloud.dataanalysis.alertinformation.JdbcTemplate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class StudentJdbc {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public Map<String, Object> findOne(String xh, Long xxid) {
        if (xxid == null) {
            xxid = 0L;
        }
        String sql = "SELECT * FROM t_xsjbxx WHERE XH='" + xh + "' AND XXID='" + xxid.intValue() + "'";
        return jdbcTemplate.queryForMap(sql);
    }
}
