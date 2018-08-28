package com.aizhixin.cloud.dataanalysis.etl.cet.manager;

import com.aizhixin.cloud.dataanalysis.common.util.DateUtil;
import com.aizhixin.cloud.dataanalysis.etl.cet.dto.NjAnalysiszbDTO;
import com.aizhixin.cloud.dataanalysis.etl.cet.dto.NjZxrsDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.Date;
import java.util.List;

@Transactional
@Component
public class CetLjGradeManager {
    public static String SQL_SCHOOL_RS = "SELECT ss.XXID AS BH, ss.NJ, count(ss.XH) AS ZXRS FROM t_xsjbxx ss WHERE ss.XXID = ?  AND ss.DQZT NOT IN ('02','04','16') AND ss.RXNY<=? AND ss.YBYNY>=? GROUP BY ss.NJ";
    public static String SQL_COLLEGE_RS = "SELECT ss.YXSH AS BH, ss.NJ, count(ss.XH) AS ZXRS FROM t_xsjbxx ss WHERE ss.XXID = ?  AND ss.DQZT NOT IN ('02','04','16') AND ss.RXNY<=? AND ss.YBYNY>=? GROUP BY ss.YXSH, ss.NJ";
    public static String SQL_PROFESSIONAL_RS = "SELECT ss.ZYH AS BH, ss.NJ, count(ss.XH) AS ZXRS FROM t_xsjbxx ss WHERE ss.XXID = ?  AND ss.DQZT NOT IN ('02','04','16') AND ss.RXNY<=? AND ss.YBYNY>=? GROUP BY ss.YXSH, ss.ZYH, ss.NJ";
    public static String SQL_CLASSES_RS = "SELECT ss.BJMC AS BH, ss.NJ, count(ss.XH) AS ZXRS FROM t_xsjbxx ss WHERE ss.XXID = ?  AND ss.DQZT NOT IN ('02','04','16') AND ss.RXNY<=? AND ss.YBYNY>=? GROUP BY ss.YXSH, ss.ZYH, ss.BJMC";

    public static String SQL_LJ_NJ_SCHOOL = "SELECT c.KSLX, NULL AS 'P_BH', x.XXID AS BH, c.NJ, COUNT(x.XH) AS CKRC, SUM(c.CJ) AS ZF, SUM(IF(c.CJ >= IF(c.KSLX='3', 60, 425), 1, 0)) as TGRC " +
            "FROM t_xsjbxx x, (" +
            "SELECT d.NJ, d.KSLX, d.XH, MAX(d.CJ) AS CJ " +
            "FROM t_b_djksxx d,t_xsjbxx s " +
            "WHERE d.XH=s.XH AND s.XXID=? AND s.RXNY<=? AND s.YBYNY>=? " +
            "GROUP BY d.KSLX,d.XH " +
            ") c " +
            "WHERE x.XH=c.XH AND x.XXID=? AND x.RXNY<=? AND x.YBYNY>=? " +
            "GROUP BY c.KSLX, x.NJ ORDER BY c.KSLX";

    public static String SQL_LJ_NJ_COLLEGE = "SELECT c.KSLX, x.XXID AS 'P_BH', x.YXSH AS BH, c.NJ, COUNT(x.XH) AS CKRC, SUM(c.CJ) AS ZF, SUM(IF(c.CJ >= IF(c.KSLX='3', 60, 425), 1, 0)) as TGRC " +
            "FROM t_xsjbxx x, (" +
            "SELECT d.NJ, d.KSLX, d.XH, MAX(d.CJ) AS CJ " +
            "FROM t_b_djksxx d,t_xsjbxx s " +
            "WHERE d.XH=s.XH AND s.XXID=? AND s.RXNY<=? AND s.YBYNY>=? " +
            "GROUP BY d.NJ, d.KSLX,d.XH " +
            ") c " +
            "WHERE x.XH=c.XH AND x.XXID=? AND x.RXNY<=? AND x.YBYNY>=? " +
            "GROUP BY c.KSLX, x.NJ, x.YXSH ORDER BY c.KSLX";

    public static String SQL_LJ_NJ_PROFESSIONAL = "SELECT c.KSLX, x.YXSH AS 'P_BH', x.ZYH AS BH, c.NJ, COUNT(x.XH) AS CKRC, SUM(c.CJ) AS ZF, SUM(IF(c.CJ >= IF(c.KSLX='3', 60, 425), 1, 0)) as TGRC " +
            "FROM t_xsjbxx x, (" +
            "SELECT d.NJ, d.KSLX, d.XH, MAX(d.CJ) AS CJ " +
            "FROM t_b_djksxx d,t_xsjbxx s " +
            "WHERE d.XH=s.XH AND s.XXID=? AND s.RXNY<=? AND s.YBYNY>=? " +
            "GROUP BY d.NJ, d.KSLX,d.XH " +
            ") c " +
            "WHERE x.XH=c.XH AND x.XXID=? AND x.RXNY<=? AND x.YBYNY>=? " +
            "GROUP BY c.KSLX, x.NJ, x.YXSH, x.ZYH ORDER BY c.KSLX";

    public static String SQL_LJ_NJ_CLASSES = "SELECT c.KSLX, x.ZYH AS 'P_BH', x.BJMC AS BH, c.NJ, COUNT(x.XH) AS CKRC, SUM(c.CJ) AS ZF, SUM(IF(c.CJ >= IF(c.KSLX='3', 60, 425), 1, 0)) as TGRC " +
            "FROM t_xsjbxx x, (" +
            "SELECT d.NJ, d.KSLX, d.XH, MAX(d.CJ) AS CJ " +
            "FROM t_b_djksxx d,t_xsjbxx s " +
            "WHERE d.XH=s.XH AND s.XXID=? AND s.RXNY<=? AND s.YBYNY>=? " +
            "GROUP BY d.NJ, d.KSLX,d.XH " +
            ") c " +
            "WHERE x.XH=c.XH AND x.XXID=? AND x.RXNY<=? AND x.YBYNY>=? " +
            "GROUP BY c.KSLX, x.NJ, x.YXSH, x.ZYH, x.BJMC ORDER BY c.KSLX";

    public static String SQL_INSERT_NJZB = "INSERT INTO t_zb_djksnj (XN, XQM, XXDM, KSLX, DHLJ, P_BH, BH, NJ, ZXRS, CKRC, ZF, TGRC) VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
    public static String SQL_DELETE_LJ_NEWEST_NJZB = "DELETE FROM t_zb_djksnj WHERE XN IS NULL AND DHLJ='2' AND XXDM=?";
    public static String SQL_DELETE_LJ_NJZB = "DELETE FROM t_zb_djksnj WHERE XN IS NOT NULL AND DHLJ='2' AND XXDM=?";

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Transactional(readOnly = true)
    public List<NjAnalysiszbDTO> queryNjLjzb(String sql, Long orgId, Date s, Date e) {
        String start = DateUtil.formatYearMonth(e);
        return jdbcTemplate.query(sql.toString(),
                new Object[]{orgId, start, s, orgId, start, s},
                new int [] {Types.BIGINT, Types.VARCHAR, Types.DATE, Types.BIGINT, Types.VARCHAR, Types.DATE},
                (ResultSet rs, int rowNum) -> new NjAnalysiszbDTO( rs.getString("KSLX"),
                        rs.getString("P_BH"),rs.getString("BH"),
                        rs.getString("NJ"), rs.getLong("CKRC"),
                        rs.getDouble("ZF"), rs.getLong("TGRC"))
        );
    }
    public void deleteHistory(String sql, String xxdm) {
        jdbcTemplate.update(sql, new Object[]{ xxdm}, new int[] {Types.VARCHAR});
    }


    @Transactional(readOnly = true)
    public List<NjZxrsDTO> querySubZxrs(String sql, Long orgId, Date s, Date e) {
        String start = DateUtil.formatYearMonth(e);
        return jdbcTemplate.query(sql.toString(), new Object[]{orgId, start, s}, new int [] {Types.BIGINT, Types.VARCHAR, Types.DATE},
                (ResultSet rs, int rowNum) -> new NjZxrsDTO (rs.getString("BH"), rs.getString("NJ"), rs.getLong("ZXRS"))
        );
    }


    public void saveNjzb(final List<NjAnalysiszbDTO> list) {
        jdbcTemplate.batchUpdate(SQL_INSERT_NJZB, new BatchPreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement preparedStatement, int i) throws SQLException {
                //(XN, XQM, XXDM, KSLX, DHLJ, P_BH, BH, NJ, ZXRS, CKRC, ZF, TGRC)
                NjAnalysiszbDTO d = list.get(i);

                if (null != d.getNj()) {
                    preparedStatement.setString(1, d.getXn());//XN
                } else {
                    preparedStatement.setNull(1, Types.VARCHAR);//XN
                }
                if (null != d.getNj()) {
                    preparedStatement.setString(2, d.getXq());//XQ
                } else {
                    preparedStatement.setNull(2, Types.VARCHAR);//XQ
                }
                preparedStatement.setString(3, d.getXxdm());//XXDM
                preparedStatement.setString(4, d.getKslx());//KSLX
                preparedStatement.setString(5, d.getDhlj());//DHLJ
                preparedStatement.setString(6, d.getPbh());//P_BH
                preparedStatement.setString(7, d.getBh());//BH
                if (null != d.getNj()) {
                    preparedStatement.setString(8, d.getNj());//NJ
                } else {
                    preparedStatement.setNull(8, Types.VARCHAR);
                }
                if (null == d.getZxrs()) {
                    preparedStatement.setNull(9, Types.BIGINT);//ZXRS
                } else {
                    preparedStatement.setLong(9, d.getZxrs());//ZXRS
                }
                preparedStatement.setLong(10, d.getCkrs());//CKRC
                preparedStatement.setDouble(11, d.getZf());//ZF
                preparedStatement.setLong(12, d.getTgrc());//TGRC
            }

            @Override
            public int getBatchSize() {
                return list.size();
            }
        });
    }
}
