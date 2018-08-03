package com.aizhixin.cloud.dataanalysis.bz.manager;

import com.aizhixin.cloud.dataanalysis.bz.dto.CetEtlDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.List;

@Transactional
@Component
public class CetETLFromDBManager {

    public static String SQL_ETL_DB_SRC = "SELECT x.YXSH, x.ZYH, x.BJMC, x.XH, x.XM, x.XB, x.NJ, x.SFZH, x.CSRQ,c.EXAMINATION_DATE AS KSRQ, c.TYPE AS KSLX, c.SCORE AS CJ " +
            "FROM t_cet_score c, t_xsjbxx x " +
            "WHERE c.SCORE>0 AND c.TYPE LIKE '%大学英语%' AND c.JOB_NUMBER=x.XH AND x.XXID=? limit ?, ?";

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Transactional(readOnly = true)
    public List<CetEtlDTO> queryDcJczb(String sql, Long orgId, int pn, int pz) {
        return jdbcTemplate.query(sql.toString(), new Object[]{orgId, pn, pz}, new int [] {Types.BIGINT, Types.INTEGER, Types.INTEGER}, new RowMapper<CetEtlDTO>() {
            public CetEtlDTO mapRow(ResultSet rs, int rowNum) throws SQLException {
                return new CetEtlDTO (rs.getString("YXSH"), rs.getString("ZYH"),rs.getString("BJMC"),
                        rs.getString("XH"), rs.getString("XM"),rs.getString("XB"),
                        rs.getString("NJ"), rs.getString("SFZH"),
                        rs.getDate("CSRQ"), rs.getString("KSRQ"), rs.getString("KSLX"),
                        rs.getDouble("CJ"));
            }
        });
    }
}
