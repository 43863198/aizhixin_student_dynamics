package com.aizhixin.cloud.datainstall.db.manager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
public class JdbcManager {

    @Autowired
    private JdbcTemplate template;

    public List<Map<String, Object>> query(String sql) {
        return template.queryForList(sql);
    }
}
