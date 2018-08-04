package com.aizhixin.cloud.dataanalysis.bz.manager;

import com.aizhixin.cloud.dataanalysis.bz.entity.StandardScore;
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
public class ScoreETLFromDBManager {

    public static String SQL_ETL_DB_SRC = "SELECT c.XN, c.XQM, x.YXSH, x.ZYH, x.BJMC, x.XH, x.XM, x.NJ, c.JXBH, c.KCH, c.XKSX, c.RKJSGH, c.KSRQ, c.KSFSM, c.KSXZM, c.DJLKSCJ, c.JD, c.KCCJ, c.PSCJ " +
            "FROM t_xscjxx c, t_xsjbxx x  " +
            "WHERE c.XH=x.XH AND x.XXID=? LIMIT ?, ?";

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Transactional(readOnly = true)
    public List<StandardScore> queryDBData(String sql, Long orgId, int ps, int pz) {
        return jdbcTemplate.query(sql.toString(), new Object[]{orgId, ps, pz}, new int [] {Types.BIGINT, Types.INTEGER, Types.INTEGER}, new RowMapper<StandardScore>() {
            public StandardScore mapRow(ResultSet rs, int rowNum) throws SQLException {
                StandardScore s = new StandardScore ();
                s.setXxdm(orgId.toString());
                String t1=rs.getString("XN"), t2 = rs.getString("XQM");
                if (null != t2) {
                    if ("1".equals(t2)) {
                        s.setXqm("2");
                        s.setXn((Integer.valueOf(t1) - 1) + "-" + t1);
                    } else if ("2".equals(t2)) {
                        s.setXqm("1");
                        s.setXn(t1 + "-" + (Integer.valueOf(t1) + 1));
                    }
                }
                s.setYxsh(rs.getString("YXSH"));
                s.setZyh(rs.getString("ZYH"));
                s.setBh(rs.getString("BJMC"));
                s.setXh(rs.getString("XH"));
                s.setXm(rs.getString("XM"));
                t1 = rs.getString("NJ");
                if (null != t1 && t1.endsWith("级") && t1.length() >= 4) {
                    s.setNj(t1.substring(0, 4));
                }
                s.setJxbh(rs.getString("JXBH"));
                s.setKch(rs.getString("KCH"));
                t1 = rs.getString("XKSX");
                if (null != t1) {
                    if ("必修".equals(t1)) {
                        s.setKclbm("1");
                    } else if ("限选".equals(t1)) {
                        s.setKclbm("2");
                    }
                }
                s.setJsgh(rs.getString("RKJSGH"));
                s.setKsrq(rs.getDate("KSRQ"));
                s.setKsfsm(rs.getString("KSFSM"));
                s.setKsxzm(rs.getString("KSXZM"));
                t1 = rs.getString("DJLKSCJ");
                if (null != t1) {
                    s.setCjlx("3");
                    s.setDjcj(t1);
                } else {
                    s.setCjlx("1");
                }
                s.setJd(rs.getDouble("JD"));
                s.setKscj(rs.getDouble("KCCJ"));
                s.setBfcj(s.getKscj());
                s.setPscj(rs.getDouble("PSCJ"));
                return s;
            }
        });
    }
}
