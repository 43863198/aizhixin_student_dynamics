package com.aizhixin.cloud.dataanalysis.zb.manager;

import com.aizhixin.cloud.dataanalysis.common.util.DateUtil;
import com.aizhixin.cloud.dataanalysis.zb.dto.AnalysisBasezbDTO;
import com.aizhixin.cloud.dataanalysis.zb.dto.SchoolCalendarDTO;
import com.aizhixin.cloud.dataanalysis.zb.dto.ZxrsDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
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
public class AnalysisIndexManager {

    public static String SQL_SCHOOL_CALENDAR = "SELECT  TEACHER_YEAR as XN, SEMESTER as XQ, START_TIME as KSRQ, WEEK as ZS, END_TIME as JSRQ FROM t_school_calendar WHERE ORG_ID = ?";

    public static String SQL_DC_SCHOOL = "SELECT d.teacher_year AS XN, d.semester AS XQ, cs.TYPE AS KSLX, sum(if(cs.SCORE > 0,1,0)) AS CKRC, null AS P_BH, cs.ORG_ID AS BH," +
            "SUM(cs.SCORE) AS ZF, MAX(cs.SCORE) as GF,SUM(if(cs.SCORE >= (if(cs.TYPE = '大学英语三级考试',60,425 )),1,0)) AS TGRC," +
            "SUM(if(x.XB='男',1,0)) AS NRC, SUM(if(x.XB='男',cs.SCORE,0)) AS NZF,SUM(if(x.XB='女',1,0)) AS VRC,SUM(if(x.XB='女',cs.SCORE,0)) AS VZF," +
            "SUM(if(x.XB='男',(if(cs.SCORE >= (if(cs.TYPE = '大学英语三级考试',60,425 )),1,0)),0)) AS NTGRC," +
            "SUM(if(x.XB='女',(if(cs.SCORE >= (if(cs.TYPE = '大学英语三级考试',60,425 )),1,0)),0)) AS VTGRC " +
            "FROM t_cet_score cs,t_xsjbxx x,t_school_calendar d " +
            "WHERE cs.TYPE LIKE '%大学英语%' AND cs.SCORE > 0 AND cs.JOB_NUMBER = x.XH AND cs.EXAMINATION_DATE BETWEEN d.start_time and d.end_time and  cs.ORG_ID=? " +
            "GROUP BY cs.ORG_ID,cs.TYPE,d.teacher_year, d.semester ORDER BY cs.EXAMINATION_DATE";

    public static String SQL_DC_COLLEGE = "SELECT d.teacher_year AS XN, d.semester AS XQ, cs.TYPE AS KSLX, sum(if(cs.SCORE > 0,1,0)) AS CKRC, cs.ORG_ID AS P_BH, x.YXSH AS BH," +
            "SUM(cs.SCORE) AS ZF, MAX(cs.SCORE) as GF,SUM(if(cs.SCORE >= (if(cs.TYPE = '大学英语三级考试',60,425 )),1,0)) AS TGRC," +
            "SUM(if(x.XB='男',1,0)) AS NRC, SUM(if(x.XB='男',cs.SCORE,0)) AS NZF,SUM(if(x.XB='女',1,0)) AS VRC,SUM(if(x.XB='女',cs.SCORE,0)) AS VZF," +
            "SUM(if(x.XB='男',(if(cs.SCORE >= (if(cs.TYPE = '大学英语三级考试',60,425 )),1,0)),0)) AS NTGRC," +
            "SUM(if(x.XB='女',(if(cs.SCORE >= (if(cs.TYPE = '大学英语三级考试',60,425 )),1,0)),0)) AS VTGRC " +
            "FROM t_cet_score cs,t_xsjbxx x,t_school_calendar d " +
            "WHERE cs.TYPE LIKE '%大学英语%' AND cs.SCORE > 0 AND cs.JOB_NUMBER = x.XH AND cs.EXAMINATION_DATE BETWEEN d.start_time and d.end_time and  cs.ORG_ID=? " +
            "GROUP BY cs.ORG_ID,cs.TYPE,d.teacher_year, d.semester, x.YXSH ORDER BY cs.EXAMINATION_DATE";

    public static String SQL_DC_PROFESSIONAL = "SELECT d.teacher_year AS XN, d.semester AS XQ, cs.TYPE AS KSLX, sum(if(cs.SCORE > 0,1,0)) AS CKRC, x.YXSH AS P_BH, x.ZYH AS BH," +
            "SUM(cs.SCORE) AS ZF, MAX(cs.SCORE) as GF,SUM(if(cs.SCORE >= (if(cs.TYPE = '大学英语三级考试',60,425 )),1,0)) AS TGRC," +
            "SUM(if(x.XB='男',1,0)) AS NRC, SUM(if(x.XB='男',cs.SCORE,0)) AS NZF,SUM(if(x.XB='女',1,0)) AS VRC,SUM(if(x.XB='女',cs.SCORE,0)) AS VZF," +
            "SUM(if(x.XB='男',(if(cs.SCORE >= (if(cs.TYPE = '大学英语三级考试',60,425 )),1,0)),0)) AS NTGRC," +
            "SUM(if(x.XB='女',(if(cs.SCORE >= (if(cs.TYPE = '大学英语三级考试',60,425 )),1,0)),0)) AS VTGRC " +
            "FROM t_cet_score cs,t_xsjbxx x,t_school_calendar d " +
            "WHERE cs.TYPE LIKE '%大学英语%' AND cs.SCORE > 0 AND cs.JOB_NUMBER = x.XH AND cs.EXAMINATION_DATE BETWEEN d.start_time and d.end_time and  cs.ORG_ID=? " +
            "GROUP BY cs.ORG_ID,cs.TYPE,d.teacher_year, d.semester, x.YXSH, x.ZYH ORDER BY cs.EXAMINATION_DATE";


    public static String SQL_DC_CLASSES = "SELECT d.teacher_year AS XN, d.semester AS XQ, cs.TYPE AS KSLX, sum(if(cs.SCORE > 0,1,0)) AS CKRC, x.ZYH AS P_BH, x.BJMC AS BH," +
            "SUM(cs.SCORE) AS ZF, MAX(cs.SCORE) as GF,SUM(if(cs.SCORE >= (if(cs.TYPE = '大学英语三级考试',60,425 )),1,0)) AS TGRC," +
            "SUM(if(x.XB='男',1,0)) AS NRC, SUM(if(x.XB='男',cs.SCORE,0)) AS NZF,SUM(if(x.XB='女',1,0)) AS VRC,SUM(if(x.XB='女',cs.SCORE,0)) AS VZF," +
            "SUM(if(x.XB='男',(if(cs.SCORE >= (if(cs.TYPE = '大学英语三级考试',60,425 )),1,0)),0)) AS NTGRC," +
            "SUM(if(x.XB='女',(if(cs.SCORE >= (if(cs.TYPE = '大学英语三级考试',60,425 )),1,0)),0)) AS VTGRC " +
            "FROM t_cet_score cs,t_xsjbxx x,t_school_calendar d " +
            "WHERE cs.TYPE LIKE '%大学英语%' AND cs.SCORE > 0 AND cs.JOB_NUMBER = x.XH AND cs.EXAMINATION_DATE BETWEEN d.start_time and d.end_time and  cs.ORG_ID=? " +
            "GROUP BY cs.ORG_ID,cs.TYPE,d.teacher_year, d.semester, x.YXSH, x.ZYH, x.BJMC ORDER BY cs.EXAMINATION_DATE";

    public static String SQL_SCHOOL_RS = "SELECT count(ss.XH) AS ZXRS FROM t_xsjbxx ss WHERE ss.XXID = ?  AND ss.DQZT NOT IN ('02','04','16') AND ss.RXNY<=? AND ss.YBYNY>=?";
    public static String SQL_COLLEGE_RS = "SELECT ss.YXSH AS BH, count(ss.XH) AS ZXRS FROM t_xsjbxx ss WHERE ss.XXID = ?  AND ss.DQZT NOT IN ('02','04','16') AND ss.RXNY<=? AND ss.YBYNY>=? GROUP BY ss.YXSH";
    public static String SQL_PROFESSIONAL_RS = "SELECT ss.ZYH AS BH, count(ss.XH) AS ZXRS FROM t_xsjbxx ss WHERE ss.XXID = ?  AND ss.DQZT NOT IN ('02','04','16') AND ss.RXNY<=? AND ss.YBYNY>=? GROUP BY ss.ZYH";
    public static String SQL_CLASSES_RS = "SELECT ss.BJMC AS BH, count(ss.XH) AS ZXRS FROM t_xsjbxx ss WHERE ss.XXID = ?  AND ss.DQZT NOT IN ('02','04','16') AND ss.RXNY<=? AND ss.YBYNY>=? GROUP BY ss.BJMC";

    public static String SQL_LJ_SCHOOL = "SELECT d.teacher_year AS XN, d.semester AS XQ, null AS P_BH, x.XXID AS BH, c.TYPE AS KSLX, " +
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
             "GROUP BY x.XXID, d.TEACHER_YEAR, d.SEMESTER, c.TYPE ";

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

    public static String SQL_INSERT_JCZB = "INSERT INTO t_zb_djksjc (XN, XQM, XXDM, KSLX, DHLJ, P_BH, BH, ZXRS, CKRC, ZF, GF, TGZF, TGRC, NRC, NZF, VRC, VZF, NTGRC, VTGRC) VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Transactional(readOnly = true)
    public List<AnalysisBasezbDTO> queryJczb(String sql, Long orgId) {
        return jdbcTemplate.query(sql.toString(), new Object[]{orgId}, new int [] {Types.BIGINT}, new RowMapper<AnalysisBasezbDTO>() {
            public AnalysisBasezbDTO mapRow(ResultSet rs, int rowNum) throws SQLException {
                return new AnalysisBasezbDTO (rs.getString("XN"), rs.getString("XQ"),
                        rs.getString("KSLX"), rs.getString("P_BH"),rs.getString("BH"),
                        rs.getLong("CKRC"), rs.getDouble("ZF"),
                        rs.getDouble("GF"), rs.getLong("TGRC"), rs.getLong("NRC"),
                        rs.getDouble("NZF"), rs.getLong("VRC"), rs.getDouble("VZF"),
                        rs.getLong("NTGRC"), rs.getLong("VTGRC"));
            }
        });
    }

    @Transactional(readOnly = true)
    public List<AnalysisBasezbDTO> queryLjJczb(String sql, Long orgId, Date date) {
        String start = DateUtil.formatYearMonth(date);
        return jdbcTemplate.query(sql.toString(), new Object[]{orgId, date, start, date}, new int [] {Types.BIGINT, Types.DATE, Types.VARCHAR, Types.DATE}, new RowMapper<AnalysisBasezbDTO>() {
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

    @Transactional(readOnly = true)
    public List<SchoolCalendarDTO> querySchoolCalendar(String sql, Long orgId) {
        return jdbcTemplate.query(sql.toString(), new Object[]{orgId}, new int [] {Types.BIGINT}, new RowMapper<SchoolCalendarDTO>() {
            public SchoolCalendarDTO mapRow(ResultSet rs, int rowNum) throws SQLException {
                return new SchoolCalendarDTO (rs.getString("XN"), rs.getString("XQ"),
                        rs.getDate("KSRQ"), rs.getDate("JSRQ"),rs.getInt("ZS"));
            }
        });
    }

    @Transactional(readOnly = true)
    public Long queryAllZxrs(String sql, Long orgId, Date date) {
        return jdbcTemplate.queryForObject(sql, new Object[]{orgId, date}, new int [] {Types.BIGINT, Types.VARCHAR, Types.DATE}, Long.class);
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

    public void saveJczb(List<AnalysisBasezbDTO> list) {
        jdbcTemplate.batchUpdate(SQL_INSERT_JCZB, new BatchPreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement preparedStatement, int i) throws SQLException {
                //(XN, XQM, XXDM, KSLX, DHLJ, P_BH, BH, ZXRS, CKRC, ZF, GF, TGZF, TGRC, NRC, NZF, VRC, VZF, NTGRC, VTGRC)
                AnalysisBasezbDTO d = list.get(i);
                String tmp = null;
                String tmp2 = null;
                if ("春".equals(d.getXq())) {
                    tmp = (new Integer(d.getXn()) - 1) + "-" + d.getXn();
                    tmp2 = "2";
                } else {
                    tmp = d.getXn() + "-" + (new Integer(d.getXn()) + 1);
                    tmp2 = "1";
                }
                preparedStatement.setString(1, tmp);//XN
                preparedStatement.setString(2, tmp2);//XQ
                preparedStatement.setString(3, d.getXxdm());//XXDM
                if(d.getKslx().indexOf("四级") > 0) {
                    tmp = "4";
                } else if(d.getKslx().indexOf("六级") > 0) {
                    tmp = "6";
                } else if(d.getKslx().indexOf("三级") > 0) {
                    tmp = "3";
                } else {
                    tmp = "0";
                }
                preparedStatement.setString(4, tmp);//KSLX
                preparedStatement.setString(5, d.getDhlj());//DHLJ
                preparedStatement.setString(6, d.getPbh());//P_BH
                preparedStatement.setString(7, d.getBh());//BH
                if (null == d.getZxrs()) {
                    preparedStatement.setNull(8, Types.BIGINT);//ZXRS
                } else {
                    preparedStatement.setLong(8, d.getZxrs());//ZXRS
                }
                preparedStatement.setLong(9, d.getCkrs());//CKRC
                preparedStatement.setDouble(10, d.getZf());//ZF
                if (null == d.getGf()) {
                    preparedStatement.setNull(11, Types.DOUBLE);//GF
                } else {
                    preparedStatement.setDouble(11, d.getGf());//GF
                }
                if (null == d.getTgzf()) {
                    preparedStatement.setNull(12, Types.DOUBLE);//TGZF
                } else {
                    preparedStatement.setDouble(12, d.getTgzf());//TGZF
                }
                preparedStatement.setLong(13, d.getTgrc());//TGRC
                preparedStatement.setLong(14, d.getNrc());//NRC
                preparedStatement.setDouble(15, d.getNzf());//NZF
                preparedStatement.setLong(16, d.getVrc());//VRC
                preparedStatement.setDouble(17, d.getVzf());//VZF
                preparedStatement.setLong(18, d.getNtgrc());//NTGRC
                preparedStatement.setLong(19, d.getVtgrc());//VTGRC
            }

            @Override
            public int getBatchSize() {
                return list.size();
            }
        });
    }
}
