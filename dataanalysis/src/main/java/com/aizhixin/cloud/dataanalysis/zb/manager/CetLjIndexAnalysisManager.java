package com.aizhixin.cloud.dataanalysis.zb.manager;

import com.aizhixin.cloud.dataanalysis.common.util.DateUtil;
import com.aizhixin.cloud.dataanalysis.zb.dto.AnalysisBasezbDTO;
import com.aizhixin.cloud.dataanalysis.zb.dto.ZxrsDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.Date;
import java.util.List;

@Transactional
@Component
public class CetLjIndexAnalysisManager {
    public static String SQL_SCHOOL_RS = "SELECT count(ss.XH) AS ZXRS FROM t_xsjbxx ss WHERE ss.XXID = ?  AND ss.DQZT NOT IN ('02','04','16') AND ss.RXNY<=? AND ss.YBYNY>=?";
    public static String SQL_COLLEGE_RS = "SELECT ss.YXSH AS BH, count(ss.XH) AS ZXRS FROM t_xsjbxx ss WHERE ss.XXID = ?  AND ss.DQZT NOT IN ('02','04','16') AND ss.RXNY<=? AND ss.YBYNY>=? GROUP BY ss.YXSH";
    public static String SQL_PROFESSIONAL_RS = "SELECT ss.ZYH AS BH, count(ss.XH) AS ZXRS FROM t_xsjbxx ss WHERE ss.XXID = ?  AND ss.DQZT NOT IN ('02','04','16') AND ss.RXNY<=? AND ss.YBYNY>=? GROUP BY ss.ZYH";
    public static String SQL_CLASSES_RS = "SELECT ss.BJMC AS BH, count(ss.XH) AS ZXRS FROM t_xsjbxx ss WHERE ss.XXID = ?  AND ss.DQZT NOT IN ('02','04','16') AND ss.RXNY<=? AND ss.YBYNY>=? GROUP BY ss.BJMC";

    public static String SQL_LJ_SCHOOL = "SELECT NULL AS XN, NULL AS XQ, NULL AS P_BH, d.XXID AS BH, c.KSLX," +
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

    public static String SQL_LJ_COLLEGE = "SELECT d.teacher_year AS XN, d.semester AS XQ, x.XXID AS P_BH, x.YXSH AS BH, c.TYPE AS KSLX, " +
            "SUM(IF(c.max > 0, 1, 0)) AS CKRC, " +
            "SUM(c.max) AS ZF, " +
            "SUM(if(c.max >= (if(c.TYPE = '大学英语三级考试',60,425 )),c.max,0)) AS TGZF, " +
            "SUM(if(c.max >= (if(c.TYPE = '大学英语三级考试',60,425 )),1,0)) AS TGRC, " +
            "SUM(if(x.XB='男',1,0)) AS NRC, " +
            "SUM(if(x.XB='男',c.max,0)) AS NZF, " +
            "SUM(if(x.XB='女',1,0)) AS VRC, " +
            "SUM(if(x.XB='女',c.max,0)) AS VZF, " +
            "SUM(if(x.XB='男',(if(c.max >= (if(c.TYPE = '大学英语三级考试',60,425 )),1,0)),0))  AS NTGRC, " +
            "SUM(if(x.XB='女',(if(c.max >= (if(c.TYPE = '大学英语三级考试',60,425 )),1,0)),0))AS VTGRC " +
            "FROM t_xsjbxx x, ( " +
            "SELECT cs.JOB_NUMBER AS xh, cs.TYPE, MAX(cs.SCORE) AS max " +
            "FROM t_cet_score cs " +
            "WHERE cs.TYPE LIKE '%大学英语%' AND cs.SCORE>0 " +
            "GROUP BY cs.TYPE, cs.JOB_NUMBER ) c, t_school_calendar d " +
            "WHERE  x.XXID = ? AND x.XH = c.xh " +
            "AND ? BETWEEN d.start_time and d.end_time " +
            "AND x.RXNY >= ? AND x.YBYNY <=? " +
            "GROUP BY x.XXID, d.TEACHER_YEAR, d.SEMESTER, c.TYPE, x.YXSH ";

    public static String SQL_LJ_PROFESSIONAL = "SELECT d.teacher_year AS XN, d.semester AS XQ, x.YXSH AS P_BH, x.ZYH AS BH, c.TYPE AS KSLX, " +
            "SUM(IF(c.max > 0, 1, 0)) AS CKRC, " +
            "SUM(c.max) AS ZF, " +
            "SUM(if(c.max >= (if(c.TYPE = '大学英语三级考试',60,425 )),c.max,0)) AS TGZF, " +
            "SUM(if(c.max >= (if(c.TYPE = '大学英语三级考试',60,425 )),1,0)) AS TGRC, " +
            "SUM(if(x.XB='男',1,0)) AS NRC, " +
            "SUM(if(x.XB='男',c.max,0)) AS NZF, " +
            "SUM(if(x.XB='女',1,0)) AS VRC, " +
            "SUM(if(x.XB='女',c.max,0)) AS VZF, " +
            "SUM(if(x.XB='男',(if(c.max >= (if(c.TYPE = '大学英语三级考试',60,425 )),1,0)),0))  AS NTGRC, " +
            "SUM(if(x.XB='女',(if(c.max >= (if(c.TYPE = '大学英语三级考试',60,425 )),1,0)),0))AS VTGRC " +
            "FROM t_xsjbxx x, ( " +
            "SELECT cs.JOB_NUMBER AS xh, cs.TYPE, MAX(cs.SCORE) AS max " +
            "FROM t_cet_score cs " +
            "WHERE cs.TYPE LIKE '%大学英语%' AND cs.SCORE>0 " +
            "GROUP BY cs.TYPE, cs.JOB_NUMBER ) c, t_school_calendar d " +
            "WHERE  x.XXID = ? AND x.XH = c.xh " +
            "AND ? BETWEEN d.start_time and d.end_time " +
            "AND x.RXNY >= ? AND x.YBYNY <=? " +
            "GROUP BY x.XXID, d.TEACHER_YEAR, d.SEMESTER, c.TYPE, x.YXSH, x.ZYH ";

    public static String SQL_LJ_CLASSES = "SELECT d.teacher_year AS XN, d.semester AS XQ, x.ZYH AS P_BH, x.BJMC AS BH, c.TYPE AS KSLX, " +
            "SUM(IF(c.max > 0, 1, 0)) AS CKRC, " +
            "SUM(c.max) AS ZF, " +
            "SUM(if(c.max >= (if(c.TYPE = '大学英语三级考试',60,425 )),c.max,0)) AS TGZF, " +
            "SUM(if(c.max >= (if(c.TYPE = '大学英语三级考试',60,425 )),1,0)) AS TGRC, " +
            "SUM(if(x.XB='男',1,0)) AS NRC, " +
            "SUM(if(x.XB='男',c.max,0)) AS NZF, " +
            "SUM(if(x.XB='女',1,0)) AS VRC, " +
            "SUM(if(x.XB='女',c.max,0)) AS VZF, " +
            "SUM(if(x.XB='男',(if(c.max >= (if(c.TYPE = '大学英语三级考试',60,425 )),1,0)),0))  AS NTGRC, " +
            "SUM(if(x.XB='女',(if(c.max >= (if(c.TYPE = '大学英语三级考试',60,425 )),1,0)),0))AS VTGRC " +
            "FROM t_xsjbxx x, ( " +
            "SELECT cs.JOB_NUMBER AS xh, cs.TYPE, MAX(cs.SCORE) AS max " +
            "FROM t_cet_score cs " +
            "WHERE cs.TYPE LIKE '%大学英语%' AND cs.SCORE>0 " +
            "GROUP BY cs.TYPE, cs.JOB_NUMBER ) c, t_school_calendar d " +
            "WHERE  x.XXID = ? AND x.XH = c.xh " +
            "AND ? BETWEEN d.start_time and d.end_time " +
            "AND  x.RXNY >= ? AND x.YBYNY <=? " +
            "GROUP BY x.XXID, d.TEACHER_YEAR, d.SEMESTER, c.TYPE, x.ZYH, x.BJMC ";

//    public static String SQL_INSERT_JCZB = "INSERT INTO t_zb_djksjc (XN, XQM, XXDM, KSLX, DHLJ, P_BH, BH, ZXRS, CKRC, ZF, GF, TGZF, TGRC, NRC, NZF, VRC, VZF, NTGRC, VTGRC) VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
    public static String SQL_DELETE_NEW_SHOOL_LJ = "DELETE FROM t_zb_djksjc WHERE DHLJ='2' AND XN IS NULL AND XQ IS NULL AND XXDM=?";

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Transactional(readOnly = true)
    public List<AnalysisBasezbDTO> queryLjJczb(String sql, Long orgId, Date date) {
        String start = DateUtil.formatYearMonth(date);
        return jdbcTemplate.query(sql.toString(), new Object[]{orgId, start, date, orgId, start, date}, new int [] {Types.BIGINT, Types.VARCHAR, Types.DATE, Types.BIGINT, Types.VARCHAR, Types.DATE}, new RowMapper<AnalysisBasezbDTO>() {
            public AnalysisBasezbDTO mapRow(ResultSet rs, int rowNum) throws SQLException {
                return new AnalysisBasezbDTO (rs.getString("XN"), rs.getString("XQ"),
                        rs.getString("KSLX"), rs.getString("P_BH"),rs.getString("BH"),
                        rs.getLong("CKRC"), rs.getDouble("ZF"),
                        null , rs.getDouble("TGZF"), rs.getLong("TGRC"), rs.getLong("NRC"),
                        rs.getDouble("NZF"), rs.getLong("VRC"), rs.getDouble("VZF"),
                        rs.getLong("NTGRC"), rs.getLong("VTGRC"));
            }
        });
    }

    public void deleteHistory(String sql, String xxdm) {
        jdbcTemplate.update(sql, new Object[]{ xxdm}, new int[] {Types.VARCHAR});
    }

    @Transactional(readOnly = true)
    public Long queryAllZxrs(String sql, Long orgId, Date date) {
        String start = DateUtil.formatYearMonth(date);
        return jdbcTemplate.queryForObject(sql, new Object[]{orgId, start, date}, new int [] {Types.BIGINT, Types.VARCHAR, Types.DATE}, Long.class);
    }

    @Transactional(readOnly = true)
    public List<ZxrsDTO> querySubZxrs(String sql, Long orgId, Date date) {
        String start = DateUtil.formatYearMonth(date);
        return jdbcTemplate.query(sql.toString(), new Object[]{orgId, start, date}, new int [] {Types.BIGINT, Types.VARCHAR, Types.DATE}, new RowMapper<ZxrsDTO>() {
            public ZxrsDTO mapRow(ResultSet rs, int rowNum) throws SQLException {
                return new ZxrsDTO (rs.getString("BH"), rs.getLong("ZXRS"));
            }
        });
    }
}
