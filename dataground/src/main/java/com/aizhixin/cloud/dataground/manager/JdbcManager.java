package com.aizhixin.cloud.dataground.manager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@Transactional
public class JdbcManager {
    @Autowired
    private JdbcTemplate template;
    public void execute(String sql) {
        template.execute(sql);
    }
}
