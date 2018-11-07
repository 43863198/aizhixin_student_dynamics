package com.aizhixin.cloud.dataanalysis.dd.rollcall.manager;

import com.aizhixin.cloud.dataanalysis.dd.rollcall.dto.PeriodDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.Types;
import java.util.List;

@Component
@Slf4j
public class PeriodManager {
    @Value("${dl.org.back.dbname}")
    private String orgDatabaseName;
    @Autowired
    private JdbcTemplate jdbcTemplate;

    public List<PeriodDTO> findAllPeroidByOrgId(Long orgId) {
        String sql = "SELECT p.id AS ID, p.`NO` AS PNO, p.START_TIME, p.END_TIME FROM #orgdatabase#.t_period p WHERE p.ORG_ID=? AND p.DELETE_FLAG=0 ORDER BY p.`NO`".replaceAll("#orgdatabase#", orgDatabaseName);
        log.info("Peroid query params: orgId:{},native SQL:{}", orgId, sql);
        return jdbcTemplate.query(sql,
                new Object[]{orgId},
                new int [] {Types.BIGINT},
                (ResultSet rs, int rowNum) -> new PeriodDTO(
                        rs.getLong("ID"),
                        rs.getInt("PNO"), rs.getString("START_TIME"),
                        rs.getString("END_TIME"))
        );
    }
}
