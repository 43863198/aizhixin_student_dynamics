package com.aizhixin.cloud.dataanalysis.zb.manager;

import com.aizhixin.cloud.dataanalysis.zb.app.vo.EnglishLevelBigScreenVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

@Component
@Transactional
public class IndexAnalysisAppManager {

    private final  static String SQL_DJ_DP_TGL = "SELECT XN,XQM,KSLX,ZXRS, CKRC,TGRC FROM t_zb_djksjc WHERE XXDM=? and BH=? and DHLJ=? AND KSLX=?  AND XN = (SELECT max(XN) FROM t_zb_djksjc WHERE KSLX=?) ORDER BY XQM DESC LIMIT 1";

    @Autowired
    private JdbcTemplate jdbcTemplate;


    @Transactional (readOnly = true)
    public List<EnglishLevelBigScreenVO> getNewLevelTestBigScreenPass(Long orgId) {
        List<EnglishLevelBigScreenVO> rsList = new ArrayList<>();
        List<EnglishLevelBigScreenVO> list =  jdbcTemplate.query(SQL_DJ_DP_TGL, new Object[]{orgId.toString(), orgId.toString(), "1", "3", "3"}, new int [] {Types.VARCHAR, Types.VARCHAR, Types.VARCHAR, Types.VARCHAR, Types.VARCHAR}, new RowMapper<EnglishLevelBigScreenVO>() {
            public EnglishLevelBigScreenVO mapRow(ResultSet rs, int rowNum) throws SQLException {
                return new EnglishLevelBigScreenVO (rs.getString("XN"), rs.getString("XQM"),
                        rs.getString("KSLX"), rs.getLong("ZXRS"),
                        rs.getLong("CKRC"), rs.getLong("TGRC"));
            }
        });
        if (null != list && list.size() > 0) {
            rsList.add(list.get(0));
        }

        list =  jdbcTemplate.query(SQL_DJ_DP_TGL, new Object[]{orgId.toString(), orgId.toString(), "1", "4", "4"}, new int [] {Types.VARCHAR, Types.VARCHAR, Types.VARCHAR, Types.VARCHAR, Types.VARCHAR}, new RowMapper<EnglishLevelBigScreenVO>() {
            public EnglishLevelBigScreenVO mapRow(ResultSet rs, int rowNum) throws SQLException {
                return new EnglishLevelBigScreenVO (rs.getString("XN"), rs.getString("XQM"),
                        rs.getString("KSLX"), rs.getLong("ZXRS"),
                        rs.getLong("CKRC"), rs.getLong("TGRC"));
            }
        });
        if (null != list && list.size() > 0) {
            rsList.add(list.get(0));
        }

        list =  jdbcTemplate.query(SQL_DJ_DP_TGL, new Object[]{orgId.toString(), orgId.toString(), "1", "6", "6"}, new int [] {Types.VARCHAR, Types.VARCHAR, Types.VARCHAR, Types.VARCHAR, Types.VARCHAR}, new RowMapper<EnglishLevelBigScreenVO>() {
            public EnglishLevelBigScreenVO mapRow(ResultSet rs, int rowNum) throws SQLException {
                return new EnglishLevelBigScreenVO (rs.getString("XN"), rs.getString("XQM"),
                        rs.getString("KSLX"), rs.getLong("ZXRS"),
                        rs.getLong("CKRC"), rs.getLong("TGRC"));
            }
        });
        if (null != list && list.size() > 0) {
            rsList.add(list.get(0));
        }
        return rsList;
    }
}
