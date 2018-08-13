package com.aizhixin.cloud.dataanalysis.zb.manager;

import com.aizhixin.cloud.dataanalysis.common.util.DateUtil;
import com.aizhixin.cloud.dataanalysis.zb.dto.AnalysisBasezbDTO;
import com.aizhixin.cloud.dataanalysis.zb.dto.SchoolCalendarDTO;
import com.aizhixin.cloud.dataanalysis.zb.dto.ZxrsDTO;
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
public class CetLjIndexAnalysisManager {
    public static String SQL_SCHOOL_RS = "SELECT ss.XXID AS BH, ss.XB, count(ss.XH) AS ZXRS FROM t_xsjbxx ss WHERE ss.XXID = ?  AND ss.DQZT NOT IN ('02','04','16') AND ss.RXNY<=? AND ss.YBYNY>=? GROUP BY ss.XXID, ss.XB";
    public static String SQL_COLLEGE_RS = "SELECT ss.YXSH AS BH, ss.XB, count(ss.XH) AS ZXRS FROM t_xsjbxx ss WHERE ss.XXID = ?  AND ss.DQZT NOT IN ('02','04','16') AND ss.RXNY<=? AND ss.YBYNY>=? GROUP BY ss.YXSH, ss.XB";
    public static String SQL_PROFESSIONAL_RS = "SELECT ss.ZYH AS BH, ss.XB, count(ss.XH) AS ZXRS FROM t_xsjbxx ss WHERE ss.XXID = ?  AND ss.DQZT NOT IN ('02','04','16') AND ss.RXNY<=? AND ss.YBYNY>=? GROUP BY ss.ZYH, ss.XB";
    public static String SQL_CLASSES_RS = "SELECT ss.BJMC AS BH, ss.XB, count(ss.XH) AS ZXRS FROM t_xsjbxx ss WHERE ss.XXID = ?  AND ss.DQZT NOT IN ('02','04','16') AND ss.RXNY<=? AND ss.YBYNY>=? GROUP BY ss.BJMC, ss.XB";

    public static String SQL_CET_ALL_XN_XQ = "SELECT XN, XQM FROM t_b_djksxx WHERE XXDM = ? GROUP BY XN,XQM ORDER BY XN DESC, XQM DESC;";

    public static String SQL_LJ_SCHOOL = "SELECT NULL AS P_BH, d.XXID AS BH, c.KSLX," +
            "COUNT(d.XH) AS CKRC," +
            "SUM(c.CJ) AS ZF," +
            "SUM(IF(c.CJ >= IF(c.KSLX='3',60,425), 1, 0)) AS TGRC," +
            "SUM(IF(c.CJ >= IF(c.KSLX='3',60,425), c.CJ, 0)) AS TGZF," +
            "SUM(IF(c.XBM = '1', 1, 0)) AS NRC," +
            "SUM(IF(c.XBM = '1', c.CJ, 0)) AS NZF," +
            "SUM(IF(c.XBM = '2', 1, 0)) AS VRC," +
            "SUM(IF(c.XBM = '2', c.CJ, 0)) AS VZF," +
            "SUM(IF(c.XBM = '1', IF(c.CJ >= IF(c.KSLX='3',60,425), 1, 0), 0)) AS NTGRC," +
            "SUM(IF(c.XBM = '2', IF(c.CJ >= IF(c.KSLX='3',60,425), 1, 0), 0)) AS VTGRC " +
            "FROM t_xsjbxx d," +
            "(" +
            "SELECT c.KSLX, c.XBM, c.XH, MAX(c.CJ) AS CJ " +
            "FROM t_b_djksxx c, t_xsjbxx x " +
            "WHERE c.KSLX in ('3', '4', '6')  AND c.XXDM=? AND x.RXNY<= ? AND x.YBYNY >= ? " +
            "AND x.XH = c.XH " +
            "GROUP BY c.KSLX,c.XH, c.XBM " +
            ") c " +
            "WHERE " +
            "d.XH=c.XH AND d.XXID=? AND d.RXNY<= ? AND d.YBYNY >= ? " +
            "GROUP BY d.XXID,  c.KSLX ";

    public static String SQL_LJ_COLLEGE = "SELECT d.XXID AS P_BH, d.YXSH AS BH, c.KSLX," +
            "COUNT(d.XH) AS CKRC," +
            "SUM(c.CJ) AS ZF," +
            "SUM(IF(c.CJ >= IF(c.KSLX='3',60,425), 1, 0)) AS TGRC," +
            "SUM(IF(c.CJ >= IF(c.KSLX='3',60,425), c.CJ, 0)) AS TGZF," +
            "SUM(IF(c.XBM = '1', 1, 0)) AS NRC," +
            "SUM(IF(c.XBM = '1', c.CJ, 0)) AS NZF," +
            "SUM(IF(c.XBM = '2', 1, 0)) AS VRC," +
            "SUM(IF(c.XBM = '2', c.CJ, 0)) AS VZF," +
            "SUM(IF(c.XBM = '1', IF(c.CJ >= IF(c.KSLX='3',60,425), 1, 0), 0)) AS NTGRC," +
            "SUM(IF(c.XBM = '2', IF(c.CJ >= IF(c.KSLX='3',60,425), 1, 0), 0)) AS VTGRC " +
            "FROM t_xsjbxx d," +
            "(" +
            "SELECT c.KSLX, c.XBM, c.XH, MAX(c.CJ) AS CJ " +
            "FROM t_b_djksxx c, t_xsjbxx x " +
            "WHERE c.KSLX in ('3', '4', '6')  AND c.XXDM=? AND x.RXNY<= ? AND x.YBYNY >= ? " +
            "AND x.XH = c.XH " +
            "GROUP BY c.KSLX,c.XH, c.XBM " +
            ") c " +
            "WHERE " +
            "d.XH=c.XH AND d.XXID=? AND d.RXNY<= ? AND d.YBYNY >= ? " +
            "GROUP BY d.XXID,  c.KSLX, d.YXSH ";

    public static String SQL_LJ_PROFESSIONAL = "SELECT d.YXSH AS P_BH, d.ZYH AS BH, c.KSLX," +
            "COUNT(d.XH) AS CKRC," +
            "SUM(c.CJ) AS ZF," +
            "SUM(IF(c.CJ >= IF(c.KSLX='3',60,425), 1, 0)) AS TGRC," +
            "SUM(IF(c.CJ >= IF(c.KSLX='3',60,425), c.CJ, 0)) AS TGZF," +
            "SUM(IF(c.XBM = '1', 1, 0)) AS NRC," +
            "SUM(IF(c.XBM = '1', c.CJ, 0)) AS NZF," +
            "SUM(IF(c.XBM = '2', 1, 0)) AS VRC," +
            "SUM(IF(c.XBM = '2', c.CJ, 0)) AS VZF," +
            "SUM(IF(c.XBM = '1', IF(c.CJ >= IF(c.KSLX='3',60,425), 1, 0), 0)) AS NTGRC," +
            "SUM(IF(c.XBM = '2', IF(c.CJ >= IF(c.KSLX='3',60,425), 1, 0), 0)) AS VTGRC " +
            "FROM t_xsjbxx d," +
            "(" +
            "SELECT c.KSLX, c.XBM, c.XH, MAX(c.CJ) AS CJ " +
            "FROM t_b_djksxx c, t_xsjbxx x " +
            "WHERE c.KSLX in ('3', '4', '6')  AND c.XXDM=? AND x.RXNY<= ? AND x.YBYNY >= ? " +
            "AND x.XH = c.XH " +
            "GROUP BY c.KSLX,c.XH, c.XBM " +
            ") c " +
            "WHERE " +
            "d.XH=c.XH AND d.XXID=? AND d.RXNY<= ? AND d.YBYNY >= ? " +
            "GROUP BY d.XXID,  c.KSLX, d.YXSH, d.ZYH ";

    public static String SQL_LJ_CLASSES = "SELECT d.ZYH AS P_BH, d.BJMC AS BH, c.KSLX," +
            "COUNT(d.XH) AS CKRC," +
            "SUM(c.CJ) AS ZF," +
            "SUM(IF(c.CJ >= IF(c.KSLX='3',60,425), 1, 0)) AS TGRC," +
            "SUM(IF(c.CJ >= IF(c.KSLX='3',60,425), c.CJ, 0)) AS TGZF," +
            "SUM(IF(c.XBM = '1', 1, 0)) AS NRC," +
            "SUM(IF(c.XBM = '1', c.CJ, 0)) AS NZF," +
            "SUM(IF(c.XBM = '2', 1, 0)) AS VRC," +
            "SUM(IF(c.XBM = '2', c.CJ, 0)) AS VZF," +
            "SUM(IF(c.XBM = '1', IF(c.CJ >= IF(c.KSLX='3',60,425), 1, 0), 0)) AS NTGRC," +
            "SUM(IF(c.XBM = '2', IF(c.CJ >= IF(c.KSLX='3',60,425), 1, 0), 0)) AS VTGRC " +
            "FROM t_xsjbxx d," +
            "(" +
            "SELECT c.KSLX, c.XBM, c.XH, MAX(c.CJ) AS CJ " +
            "FROM t_b_djksxx c, t_xsjbxx x " +
            "WHERE c.KSLX in ('3', '4', '6')  AND c.XXDM=? AND x.RXNY<= ? AND x.YBYNY >= ? " +
            "AND x.XH = c.XH " +
            "GROUP BY c.KSLX,c.XH, c.XBM " +
            ") c " +
            "WHERE " +
            "d.XH=c.XH AND d.XXID=? AND d.RXNY<= ? AND d.YBYNY >= ? " +
            "GROUP BY d.XXID,  c.KSLX, d.YXSH, d.ZYH, d.BJMC ";

    public static String SQL_INSERT_JCZB = "INSERT INTO t_zb_djksjc (XN, XQM, XXDM, KSLX, DHLJ, P_BH, BH, ZXRS, NZXRS, VZXRS, CKRC, ZF, GF, TGZF, TGRC, NRC, NZF, VRC, VZF, NTGRC, VTGRC) VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
    public static String SQL_DELETE_ALL_NOT_NEW_SHOOL_LJ = "DELETE FROM t_zb_djksjc WHERE XN IS NOT NULL AND DHLJ='2' AND XXDM=?";
    public static String SQL_DELETE_ALL_NEW_SHOOL_LJ = "DELETE FROM t_zb_djksjc WHERE DHLJ='2' AND XN IS NULL AND XXDM=?";

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Transactional(readOnly = true)
    public List<AnalysisBasezbDTO> queryLjJczb(String sql, Long orgId, Date date) {
        return queryLjJczb(sql, orgId, date, date);
    }

    @Transactional(readOnly = true)
    public List<AnalysisBasezbDTO> queryLjJczb(String sql, Long orgId, Date s, Date e) {
        String start = DateUtil.formatYearMonth(e);
        return jdbcTemplate.query(sql.toString(),
                new Object[]{orgId, start, s, orgId, start, s},
                new int [] {Types.BIGINT, Types.VARCHAR, Types.DATE, Types.BIGINT, Types.VARCHAR, Types.DATE},
                (ResultSet rs, int rowNum) -> new AnalysisBasezbDTO (
                        null, null,
                        rs.getString("KSLX"), rs.getString("P_BH"),rs.getString("BH"),
                        rs.getLong("CKRC"), rs.getDouble("ZF"),
                        null , rs.getDouble("TGZF"), rs.getLong("TGRC"), rs.getLong("NRC"),
                        rs.getDouble("NZF"), rs.getLong("VRC"), rs.getDouble("VZF"),
                        rs.getLong("NTGRC"), rs.getLong("VTGRC")));
    }

    public void deleteHistory(String sql, String xxdm) {
        jdbcTemplate.update(sql, new Object[]{ xxdm}, new int[] {Types.VARCHAR});
    }


    @Transactional(readOnly = true)
    public List<ZxrsDTO> querySubZxrs(String sql, Long orgId, Date s, Date e) {
        String start = DateUtil.formatYearMonth(e);
        return jdbcTemplate.query(sql.toString(), new Object[]{orgId, start, s}, new int [] {Types.BIGINT, Types.VARCHAR, Types.DATE},
                (ResultSet rs, int rowNum) -> new ZxrsDTO (rs.getString("BH"), rs.getString("XB"), rs.getLong("ZXRS"))
        );
    }

    @Transactional(readOnly = true)
    public List<SchoolCalendarDTO> queryCetXnxqAndDate(String xxdm) {
        return jdbcTemplate.query(SQL_CET_ALL_XN_XQ,
                new Object[]{xxdm},
                new int [] {Types.VARCHAR},
                (ResultSet rs, int rowNum) -> new SchoolCalendarDTO (rs.getString("XN"), rs.getString("XQM")));
    }

    public void saveJczb(List<AnalysisBasezbDTO> list) {
        jdbcTemplate.batchUpdate(SQL_INSERT_JCZB, new BatchPreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement preparedStatement, int i) throws SQLException {
                //(XN, XQM, XXDM, KSLX, DHLJ, P_BH, BH, ZXRS, CKRC, ZF, GF, TGZF, TGRC, NRC, NZF, VRC, VZF, NTGRC, VTGRC)
                AnalysisBasezbDTO d = list.get(i);
                if (null == d.getXn()) {
                    preparedStatement.setNull(1, Types.VARCHAR);//XN
                } else {
                    preparedStatement.setString(1, d.getXn());//XN
                }
                if (null == d.getXq()) {
                    preparedStatement.setNull(2, Types.VARCHAR);//XQ
                } else {
                    preparedStatement.setString(2, d.getXq());//XQ
                }
                preparedStatement.setString(3, d.getXxdm());//XXDM
                preparedStatement.setString(4, d.getKslx());//KSLX
                preparedStatement.setString(5, d.getDhlj());//DHLJ
                if (null != d.getPbh()) {
                    preparedStatement.setString(6, d.getPbh());//P_BH
                } else {
                    preparedStatement.setNull(6, Types.VARCHAR);//P_BH
                }
                preparedStatement.setString(7, d.getBh());//BH
                if (null == d.getZxrs()) {
                    preparedStatement.setNull(8, Types.BIGINT);//ZXRS
                } else {
                    preparedStatement.setLong(8, d.getZxrs());//ZXRS
                }
                if (null == d.getNzxrs()) {
                    preparedStatement.setNull(9, Types.BIGINT);//NZXRS
                } else {
                    preparedStatement.setLong(9, d.getNzxrs());//NZXRS
                }
                if (null == d.getVzxrs()) {
                    preparedStatement.setNull(10, Types.BIGINT);//VZXRS
                } else {
                    preparedStatement.setLong(10, d.getVzxrs());//VZXRS
                }
                preparedStatement.setLong(11, d.getCkrs());//CKRC
                preparedStatement.setDouble(12, d.getZf());//ZF
                if (null == d.getGf()) {
                    preparedStatement.setNull(13, Types.DOUBLE);//GF
                } else {
                    preparedStatement.setDouble(13, d.getGf());//GF
                }
                if (null == d.getTgzf()) {
                    preparedStatement.setNull(14, Types.DOUBLE);//TGZF
                } else {
                    preparedStatement.setDouble(14, d.getTgzf());//TGZF
                }
                preparedStatement.setLong(15, d.getTgrc());//TGRC
                preparedStatement.setLong(16, d.getNrc());//NRC
                preparedStatement.setDouble(17, d.getNzf());//NZF
                preparedStatement.setLong(18, d.getVrc());//VRC
                preparedStatement.setDouble(19, d.getVzf());//VZF
                preparedStatement.setLong(20, d.getNtgrc());//NTGRC
                preparedStatement.setLong(21, d.getVtgrc());//VTGRC
            }

            @Override
            public int getBatchSize() {
                return list.size();
            }
        });
    }
}
