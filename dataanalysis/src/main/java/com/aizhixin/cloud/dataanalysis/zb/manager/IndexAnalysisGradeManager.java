package com.aizhixin.cloud.dataanalysis.zb.manager;

import com.aizhixin.cloud.dataanalysis.common.util.DateUtil;
import com.aizhixin.cloud.dataanalysis.zb.dto.NjAnalysiszbDTO;
import com.aizhixin.cloud.dataanalysis.zb.dto.NjZxrsDTO;
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
public class IndexAnalysisGradeManager {

    public static String SQL_DC_NJ_SCHOOL = "SELECT d.teacher_year AS XN, d.semester AS XQ, cs.TYPE AS KSLX, null as P_BH, cs.ORG_ID as BH, x.NJ, " +
            "SUM(if(cs.SCORE > 0,1,0)) as CKRC, " +
            "SUM(cs.SCORE) as ZF, " +
            "SUM(if(SCORE >= (if(TYPE = '大学英语三级考试',60,425 )),1,0)) as TGRC " +
            "FROM t_cet_score cs,t_xsjbxx x,t_school_calendar d " +
            "WHERE cs.TYPE LIKE '%大学英语%' AND cs.SCORE > 0 AND cs.JOB_NUMBER = x.XH AND cs.EXAMINATION_DATE BETWEEN d.start_time and d.end_time and cs.ORG_ID=? " +
            "GROUP BY cs.ORG_ID,cs.TYPE,d.teacher_year, d.semester, x.NJ " +
            "ORDER BY cs.EXAMINATION_DATE";

    public static String SQL_DC_NJ_COLLEGE = "SELECT d.teacher_year AS XN, d.semester AS XQ, cs.TYPE AS KSLX, cs.ORG_ID as P_BH, x.YXSH  as BH, x.NJ, " +
            "SUM(if(cs.SCORE > 0,1,0)) as CKRC, " +
            "SUM(cs.SCORE) as ZF, " +
            "SUM(if(SCORE >= (if(TYPE = '大学英语三级考试',60,425 )),1,0)) as TGRC " +
            "FROM t_cet_score cs,t_xsjbxx x,t_school_calendar d " +
            "WHERE cs.TYPE LIKE '%大学英语%' AND cs.SCORE > 0 AND cs.JOB_NUMBER = x.XH AND cs.EXAMINATION_DATE BETWEEN d.start_time and d.end_time and cs.ORG_ID=?  " +
            "GROUP BY cs.ORG_ID,cs.TYPE,d.teacher_year, d.semester, x.YXSH, x.NJ " +
            "ORDER BY cs.EXAMINATION_DATE";

    public static String SQL_DC_NJ_PROFESSIONAL = "SELECT d.teacher_year AS XN, d.semester AS XQ, cs.TYPE AS KSLX, x.YXSH as P_BH, x.ZYH as BH, x.NJ, " +
            "SUM(if(cs.SCORE > 0,1,0)) as CKRC, " +
            "SUM(cs.SCORE) as ZF, " +
            "SUM(if(SCORE >= (if(TYPE = '大学英语三级考试',60,425 )),1,0)) as TGRC " +
            "FROM t_cet_score cs,t_xsjbxx x,t_school_calendar d " +
            "WHERE cs.TYPE LIKE '%大学英语%' AND cs.SCORE > 0 AND cs.JOB_NUMBER = x.XH AND cs.EXAMINATION_DATE BETWEEN d.start_time and d.end_time and cs.ORG_ID=?  " +
            "GROUP BY cs.ORG_ID,cs.TYPE,d.teacher_year, d.semester, x.YXSH, x.ZYH, x.NJ " +
            "ORDER BY cs.EXAMINATION_DATE";


    public static String SQL_DC_NJ_CLASSES = "SELECT d.teacher_year AS XN, d.semester AS XQ, cs.TYPE AS KSLX, x.ZYH as P_BH, x.BJMC as BH, x.NJ, " +
            "SUM(if(cs.SCORE > 0,1,0)) as CKRC, " +
            "SUM(cs.SCORE) as ZF, " +
            "SUM(if(SCORE >= (if(TYPE = '大学英语三级考试',60,425 )),1,0)) as TGRC " +
            "FROM t_cet_score cs,t_xsjbxx x,t_school_calendar d " +
            "WHERE cs.TYPE LIKE '%大学英语%' AND cs.SCORE > 0 AND cs.JOB_NUMBER = x.XH AND cs.EXAMINATION_DATE BETWEEN d.start_time and d.end_time and cs.ORG_ID=?  " +
            "GROUP BY cs.ORG_ID,cs.TYPE,d.teacher_year, d.semester, x.YXSH, x.ZYH, x.BJMC, x.NJ " +
            "ORDER BY cs.EXAMINATION_DATE";

    public static String SQL_SCHOOL_RS = "SELECT null AS BH, ss.NJ, count(ss.XH) AS ZXRS FROM t_xsjbxx ss WHERE ss.XXID = ?  AND ss.DQZT NOT IN ('02','04','16') AND ss.RXNY<=? AND ss.YBYNY>=? GROUP BY ss.NJ";
    public static String SQL_COLLEGE_RS = "SELECT ss.YXSH AS BH, ss.NJ, count(ss.XH) AS ZXRS FROM t_xsjbxx ss WHERE ss.XXID = ?  AND ss.DQZT NOT IN ('02','04','16') AND ss.RXNY<=? AND ss.YBYNY>=? GROUP BY ss.YXSH, ss.NJ";
    public static String SQL_PROFESSIONAL_RS = "SELECT ss.ZYH AS BH, ss.NJ, count(ss.XH) AS ZXRS FROM t_xsjbxx ss WHERE ss.XXID = ?  AND ss.DQZT NOT IN ('02','04','16') AND ss.RXNY<=? AND ss.YBYNY>=? GROUP BY ss.YXSH, ss.ZYH, ss.NJ";
    public static String SQL_CLASSES_RS = "SELECT ss.BJMC AS BH, ss.NJ, count(ss.XH) AS ZXRS FROM t_xsjbxx ss WHERE ss.XXID = ?  AND ss.DQZT NOT IN ('02','04','16') AND ss.RXNY<=? AND ss.YBYNY>=? GROUP BY ss.YXSH, ss.ZYH, ss.BJMC, ss.NJ";

    public static String SQL_LJ_NJ_SCHOOL = "SELECT  d.teacher_year AS XN, d.semester AS XQ, c.TYPE AS KSLX, null AS P_BH, x.XXID AS BH, x.NJ AS NJ, " +
            "COUNT(x.xh) as CKRC, " +
            "SUM(c.SCORE) as ZF, " +
            "SUM(if(c.SCORE >= (if(c.TYPE = '大学英语三级考试',60,425 )),1,0)) AS TGRC " +
            "FROM t_xsjbxx AS x , ( " +
            "SELECT cs.JOB_NUMBER AS JOB_NUMBER, cs.TYPE, MAX(cs.SCORE) AS SCORE " +
            "FROM t_cet_score cs " +
            "WHERE cs.TYPE LIKE '%大学英语%' AND cs.SCORE > 0 " +
            "GROUP BY cs.JOB_NUMBER, cs.TYPE " +
            ") c , t_school_calendar d " +
            "WHERE x.XXID=? AND x.xh=c.JOB_NUMBER " +
            "AND ? BETWEEN d.start_time and d.end_time " +
            "AND  x.YBYNY>= ? AND x.RXNY<= ? " +
            "GROUP BY x.XXID, d.TEACHER_YEAR, d.SEMESTER, c.TYPE, x.NJ";

    public static String SQL_LJ_NJ_COLLEGE = "SELECT  d.teacher_year AS XN, d.semester AS XQ, c.TYPE AS KSLX, x.XXID AS P_BH, x.YXSH AS BH, x.NJ AS NJ, " +
            "COUNT(x.xh) as CKRC, " +
            "SUM(c.SCORE) as ZF, " +
            "SUM(if(c.SCORE >= (if(c.TYPE = '大学英语三级考试',60,425 )),1,0)) AS TGRC " +
            "FROM t_xsjbxx AS x , ( " +
            "SELECT cs.JOB_NUMBER AS JOB_NUMBER, cs.TYPE, MAX(cs.SCORE) AS SCORE " +
            "FROM t_cet_score cs " +
            "WHERE cs.TYPE LIKE '%大学英语%' AND cs.SCORE > 0 " +
            "GROUP BY cs.JOB_NUMBER, cs.TYPE " +
            ") c , t_school_calendar d " +
            "WHERE x.XXID=? AND x.xh=c.JOB_NUMBER " +
            "AND ? BETWEEN d.start_time and d.end_time " +
            "AND  x.YBYNY>= ? AND x.RXNY<= ? " +
            "GROUP BY x.XXID, d.TEACHER_YEAR, d.SEMESTER, c.TYPE, x.YXSH, x.NJ";

    public static String SQL_LJ_NJ_PROFESSIONAL = "SELECT  d.teacher_year AS XN, d.semester AS XQ, c.TYPE AS KSLX, x.YXSH AS P_BH, x.ZYH AS BH, x.NJ AS NJ, " +
            "COUNT(x.xh) as CKRC, " +
            "SUM(c.SCORE) as ZF, " +
            "SUM(if(c.SCORE >= (if(c.TYPE = '大学英语三级考试',60,425 )),1,0)) AS TGRC " +
            "FROM t_xsjbxx AS x , ( " +
            "SELECT cs.JOB_NUMBER AS JOB_NUMBER, cs.TYPE, MAX(cs.SCORE) AS SCORE " +
            "FROM t_cet_score cs " +
            "WHERE cs.TYPE LIKE '%大学英语%' AND cs.SCORE > 0 " +
            "GROUP BY cs.JOB_NUMBER, cs.TYPE " +
            ") c , t_school_calendar d " +
            "WHERE x.XXID=? AND x.xh=c.JOB_NUMBER " +
            "AND ? BETWEEN d.start_time and d.end_time " +
            "AND  x.YBYNY>= ? AND x.RXNY<= ? " +
            "GROUP BY x.XXID, d.TEACHER_YEAR, d.SEMESTER, c.TYPE, x.YXSH, x.ZYH, x.NJ";

    public static String SQL_LJ_NJ_CLASSES = "SELECT  d.teacher_year AS XN, d.semester AS XQ, c.TYPE AS KSLX, x.ZYH AS P_BH, x.BJMC AS BH, x.NJ AS NJ, " +
            "COUNT(x.xh) as CKRC, " +
            "SUM(c.SCORE) as ZF, " +
            "SUM(if(c.SCORE >= (if(c.TYPE = '大学英语三级考试',60,425 )),1,0)) AS TGRC " +
            "FROM t_xsjbxx AS x , ( " +
            "SELECT cs.JOB_NUMBER AS JOB_NUMBER, cs.TYPE, MAX(cs.SCORE) AS SCORE " +
            "FROM t_cet_score cs " +
            "WHERE cs.TYPE LIKE '%大学英语%' AND cs.SCORE > 0 " +
            "GROUP BY cs.JOB_NUMBER, cs.TYPE " +
            ") c , t_school_calendar d " +
            "WHERE x.XXID=? AND x.xh=c.JOB_NUMBER " +
            "AND ? BETWEEN d.start_time and d.end_time " +
            "AND  x.YBYNY>= ? AND x.RXNY<= ? " +
            "GROUP BY x.XXID, d.TEACHER_YEAR, d.SEMESTER, c.TYPE, x.YXSH, x.ZYH, x.BJMC, x.NJ";

    public static String SQL_INSERT_NJZB = "INSERT INTO t_zb_djksnj (XN, XQM, XXDM, KSLX, DHLJ, P_BH, BH, NJ, ZXRS, CKRC, ZF, TGRC) VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Transactional(readOnly = true)
    public List<NjAnalysiszbDTO> queryNjDczb(String sql, Long orgId) {
        return jdbcTemplate.query(sql.toString(), new Object[]{orgId}, new int [] {Types.BIGINT}, new RowMapper<NjAnalysiszbDTO>() {
            public NjAnalysiszbDTO mapRow(ResultSet rs, int rowNum) throws SQLException {
                return new NjAnalysiszbDTO(rs.getString("XN"), rs.getString("XQ"),
                        rs.getString("KSLX"), rs.getString("P_BH"),rs.getString("BH"),
                        rs.getString("NJ"), rs.getLong("CKRC"), rs.getDouble("ZF"),
                         rs.getLong("TGRC"));
            }
        });
    }


    @Transactional(readOnly = true)
    public List<NjAnalysiszbDTO> queryNjLjzb(String sql, Long orgId, Date date) {
        String start = DateUtil.formatYearMonth(date);
        return jdbcTemplate.query(sql.toString(), new Object[]{orgId, date, date, start}, new int [] {Types.BIGINT, Types.DATE, Types.DATE, Types.VARCHAR}, new RowMapper<NjAnalysiszbDTO>() {
            public NjAnalysiszbDTO mapRow(ResultSet rs, int rowNum) throws SQLException {
                return new NjAnalysiszbDTO(rs.getString("XN"), rs.getString("XQ"),
                        rs.getString("KSLX"), rs.getString("P_BH"),rs.getString("BH"),
                        rs.getString("NJ"), rs.getLong("CKRC"), rs.getDouble("ZF"),
                        rs.getLong("TGRC"));
            }
        });
    }

    @Transactional(readOnly = true)
    public List<NjZxrsDTO> querySubZxrs(String sql, Long orgId, Date date) {
        String start = DateUtil.formatYearMonth(date);
        return jdbcTemplate.query(sql.toString(), new Object[]{orgId, start, date}, new int [] {Types.BIGINT, Types.VARCHAR, Types.DATE}, new RowMapper<NjZxrsDTO>() {
            public NjZxrsDTO mapRow(ResultSet rs, int rowNum) throws SQLException {
                return new NjZxrsDTO(rs.getString("BH"), rs.getString("NJ"), rs.getLong("ZXRS"));
            }
        });
    }

    public void saveNjzb(final List<NjAnalysiszbDTO> list) {
        jdbcTemplate.batchUpdate(SQL_INSERT_NJZB, new BatchPreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement preparedStatement, int i) throws SQLException {
                //(XN, XQM, XXDM, KSLX, DHLJ, P_BH, BH, NJ, ZXRS, CKRC, ZF, TGRC)
                NjAnalysiszbDTO d = list.get(i);
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
                if (null != d.getNj() && d.getNj().endsWith("级") && d.getNj().length() >= 4) {
                    preparedStatement.setString(8, d.getNj().substring(0, 4));//NJ
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
