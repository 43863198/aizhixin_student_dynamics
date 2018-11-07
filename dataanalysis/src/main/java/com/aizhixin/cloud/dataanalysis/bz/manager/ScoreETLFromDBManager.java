package com.aizhixin.cloud.dataanalysis.bz.manager;

import com.aizhixin.cloud.dataanalysis.bz.entity.StandardScore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
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

    public static String SQL_ETL_DB_SRC_XN_XQ = "SELECT c.XN, c.XQM, x.YXSH, x.ZYH, x.BJMC, x.XH, x.XM, x.NJ, c.JXBH, c.KCH, c.XKSX, c.RKJSGH, c.KSRQ, c.KSFSM, c.KSXZM, c.DJLKSCJ, c.JD, c.KCCJ, c.PSCJ " +
            "FROM t_xscjxx c, t_xsjbxx x  " +
            "WHERE c.XH=x.XH AND x.XXID=? AND c.XN=? AND c.XQM=?";


    @Autowired
    private JdbcTemplate jdbcTemplate;

    private StandardScore createBean(String xxdm, ResultSet rs) throws SQLException {
        StandardScore s = new StandardScore ();
        s.setXxdm(xxdm);
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

    @Transactional(readOnly = true)
    public List<StandardScore> queryDBData(String sql, Long orgId, int ps, int pz) {
        return jdbcTemplate.query(
                sql,
                new Object[]{orgId, ps, pz},
                new int [] {Types.BIGINT, Types.INTEGER, Types.INTEGER},
                (ResultSet rs, int rowNum) -> createBean(orgId.toString(), rs));
    }

    @Transactional(readOnly = true)
    public List<StandardScore> queryOldSemesterDBData(Long orgId, String xn, String xq) {
        return jdbcTemplate.query(SQL_ETL_DB_SRC_XN_XQ,
                new Object[]{orgId, xn, xq},
                new int [] {Types.BIGINT, Types.VARCHAR, Types.VARCHAR},
                (ResultSet rs, int rowNum) -> createBean(orgId.toString(), rs)
        );
    }


    private StandardScore createChangjiangBean(String xxdm, ResultSet rs) throws SQLException {
        StandardScore s = new StandardScore ();
        s.setXxdm(xxdm);
        s.setXn(rs.getString("XN"));
        s.setXqm("" + rs.getInt("XQ"));
        s.setYxsh(rs.getString("XYDM"));
        s.setZyh(rs.getString("ZYM"));
        s.setBh(rs.getString("BJDM"));
        s.setXh(rs.getString("XH"));
        s.setXm(rs.getString("XM"));
        s.setNj("" + rs.getInt("SZNJ"));
        s.setJxbh(rs.getString("XKKH"));
        s.setKch(rs.getString("KCDM"));
        s.setKclbm("1");//全必修
        s.setXf(new Double(rs.getString("XF")));

        s.setBfcj(rs.getDouble("FSLKSCJ"));
        if (s.getBfcj() < 60) {
            if ("合格".equals(rs.getString("BKCJ"))) {
                s.setJd(1.0);
                s.setBfcj(60.0);

            } else {
                s.setJd(0.0);
            }
        } else {
            s.setJd((s.getBfcj() - 50)/10);
        }
        s.setKscj(s.getBfcj());
        return s;
    }

    @Transactional(readOnly = true)
    public List<StandardScore> queryChangjiangSemesterDBData(final String xxdm, final String xn, final Integer xq) {
        String sql = "SELECT  XM,XH,SZNJ,BJDM,ZYM,XYDM,XN,XQ,KCDM,XF,FSLKSCJ,BKCJ,XKKH FROM v_cjb WHERE XN=? AND XQ=?";
        return jdbcTemplate.query(sql,
                new Object[]{xn, xq},
                new int [] {Types.VARCHAR, Types.INTEGER},
                (ResultSet rs, int rowNum) -> createChangjiangBean(xxdm, rs)
        );
    }
}
