package com.aizhixin.cloud.dataanalysis.etl.study.manager;

import com.aizhixin.cloud.dataanalysis.etl.study.dto.EtlStudendStudyPlanDTO;
import com.aizhixin.cloud.dataanalysis.etl.study.dto.EtlStudentStudyPlanXdztDTO;
import com.aizhixin.cloud.dataanalysis.etl.study.dto.EtlStudyTeachingPlanDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Component
@Transactional
public class EtlStudyExceptionManager {
    private static final String SQL_ZYPYJH = "INSERT INTO t_zypyjh (ZYH, KKDW, XN, XQM, NJ, ND, JHH, KCH) " +
            "SELECT " +
            "c.ZYH," +
            "c.YXSH AS KKDW," +
            "IF(c.XQM='2',CONCAT(c.GRADE,'-', c.GRADE + 1), CONCAT(c.GRADE - 1,'-', c.GRADE)) AS XN," +
            "IF(c.XQM='1', '2', '1') AS XQM," +
            "c.GRADE AS NJ," +
            "c.NJ AS ND," +
            "c.JXJHH," +
            "c.KCH " +
            "from ( " +
            "SELECT " +
            "z.ZYH, k.YXSH, z.JXJHH, z.NJ AS GRADE, k.XQM, k.NJ, k.KCH " +
            "FROM " +
            "t_zyjxjh z, t_zykcjh k " +
            "WHERE z.JXJHH=k.JXJHH AND z.NJ>='2014' " +
            "ORDER BY z.ZYH, z.JXJHH, k.KCH, z.NJ, k.NJ, k.XQM " +
            ") c";
//    UPDATE t_zypyjh p SET p.KCMC=(SELECT COURSE_NAME FROM t_course WHERE COURSE_NUMBER=p.KCH);
//    UPDATE t_zypyjh p SET p.XF=(SELECT CREDIT FROM t_course WHERE COURSE_NUMBER=p.KCH);

    private static String SQL_SEE_REPEAT = "SELECT XN, XQM, ZYH, NJ," +
            "COUNT(DISTINCT JHH) as CT " +
            "FROM t_zypyjh " +
            "GROUP BY XN, XQM, ZYH, NJ " +
            "HAVING COUNT(DISTINCT JHH) > 1";
    private static String SQL_REPEAT_COURSE = "select XN, XQM, ZYH, NJ,JHH,COUNT(*) AS CT from t_zypyjh where XN=? AND XQM=? AND ZYH=? AND NJ=? GROUP BY  XN, XQM, ZYH, NJ,JHH";

    private static String SQL_DELETE_REPEAT_DATA = "delete from t_zypyjh where XN=? AND XQM=? AND ZYH=? AND NJ=? AND JHH=?";

    private static String SQL_JXJH = "SELECT ZYH,NJ from t_zypyjh WHERE XN = ? AND XQM = ? AND XXDM = ? GROUP BY ZYH,NJ";

    private static String SQL_JXJH_KC = "SELECT KCH, KCMC, XF from t_zypyjh WHERE ZYH = ? AND NJ = ? AND XN=? AND XQM=? AND XXDM=?";

    private static String SQL_XSJB_XX = "SELECT XH, XM, BJMC, YXSH FROM t_xsjbxx WHERE ZYH=? AND NJ=? AND XXID=?";

    public static String SQL_INSERT_XSPYJH = "INSERT INTO t_xspyjh (XN, XQM, XH, XM, NJ, BJMC, ZYH, YXSH, XXDM, KCH, KCMC, XF, XDZT, JD) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

    public static String SQL_XSPYJH_YXSH = "SELECT DISTINCT XH FROM t_xspyjh WHERE XXDM=? AND YXSH=? AND XDZT != 10 AND XN <= ?";
    public static String SQL_XSPYJH = "SELECT DISTINCT XH FROM t_xspyjh WHERE XXDM=? AND XDZT != 10 AND XN <= ?";
    private static String SQL_XS_KCCJ = "SELECT  XH, KCH, MAX(BFCJ) AS CJ, MAX(JD) AS JD FROM t_b_xscjxx WHERE XXDM=? AND XH=? GROUP BY XH, KCH HAVING MAX(BFCJ) >= 60";
    private static String SQL_XS_PYJH_XH_KCH = "SELECT ID, XH, KCH FROM t_xspyjh WHERE XH = :xh AND KCH in (:kchs) AND XDZT != 10 AND XXDM = :xxdm";
    private static String SQL_XS_PYJH_UPDATE_XDZT = "UPDATE t_xspyjh SET XDZT=?, JD=? WHERE ID=?";
    @Autowired
    private JdbcTemplate jdbcTemplate;


    /**
     * 查询重复的专业培养计划
     */
    @Transactional(readOnly = true)
    public List<EtlStudyTeachingPlanDTO> queryRepeatZypyjh() {
        return jdbcTemplate.query(SQL_SEE_REPEAT,
                (ResultSet rs, int rowNum) -> new EtlStudyTeachingPlanDTO(rs.getString("XN"), rs.getString("XQM"), rs.getString("ZYH"),
                        rs.getString("NJ"), rs.getLong("CT"))
        );
    }

    /**
     * 查询重复的计划号及课程数量
     */
    @Transactional(readOnly = true)
    public List<EtlStudyTeachingPlanDTO> queryRepeatZypyjhJHH(EtlStudyTeachingPlanDTO d) {
        return jdbcTemplate.query(SQL_REPEAT_COURSE,
                new Object[]{d.getXn(), d.getXqm(), d.getZyh(), d.getNj()},
                new int [] {Types.VARCHAR, Types.VARCHAR, Types.VARCHAR, Types.VARCHAR},
                (ResultSet rs, int rowNum) -> new EtlStudyTeachingPlanDTO(rs.getString("XN"), rs.getString("XQM"), rs.getString("ZYH"),
                        rs.getString("NJ"), rs.getString("JHH"), rs.getLong("CT"))
        );
    }

    /**
     * 删除重复的计划号
     */
    public void deleteRepeatData(EtlStudyTeachingPlanDTO d) {
        jdbcTemplate.update(SQL_DELETE_REPEAT_DATA,
                new Object[]{d.getXn(), d.getXqm(), d.getZyh(), d.getNj(), d.getJhh()},
                new int [] {Types.VARCHAR, Types.VARCHAR, Types.VARCHAR, Types.VARCHAR, Types.VARCHAR}
        );
    }



    /**
     * 查询特定学期的专业培养计划
     */
    @Transactional(readOnly = true)
    public List<EtlStudendStudyPlanDTO> queryXnXqZypyjh(String xn, String xq, String xxdm) {
        return jdbcTemplate.query(SQL_JXJH,
                new Object[]{xn, xq, xxdm},
                new int [] {Types.VARCHAR, Types.VARCHAR, Types.VARCHAR},
                (ResultSet rs, int rowNum) -> new EtlStudendStudyPlanDTO(rs.getString("ZYH"), rs.getString("NJ"))
        );
    }

    /**
     * 查询特定学期的专业年级对应的课程信息
     */
    @Transactional(readOnly = true)
    public List<EtlStudendStudyPlanDTO> queryXnXqZyhNjCourse(String xn, String xq, String xxdm, String zyh, String nj) {
        return jdbcTemplate.query(SQL_JXJH_KC,
                new Object[]{zyh, nj, xn, xq, xxdm},
                new int [] {Types.VARCHAR, Types.VARCHAR, Types.VARCHAR, Types.VARCHAR, Types.VARCHAR},
                (ResultSet rs, int rowNum) -> new EtlStudendStudyPlanDTO(rs.getString("KCH"), rs.getString("KCMC"), rs.getDouble("XF"))
        );
    }

    /**
     * 查询专业年级的学生信息
     */
    @Transactional(readOnly = true)
    public List<EtlStudendStudyPlanDTO> queryXsxx(String zyh, String nj, Long xxid) {
        return jdbcTemplate.query(SQL_XSJB_XX,
                new Object[]{zyh, nj, xxid},
                new int [] {Types.VARCHAR, Types.VARCHAR, Types.BIGINT},
                (ResultSet rs, int rowNum) -> new EtlStudendStudyPlanDTO(rs.getString("XH"), rs.getString("XM"), rs.getString("BJMC"), rs.getString("YXSH"))
        );
    }

    public void saveXspyjh(List<EtlStudendStudyPlanDTO> list) {
        jdbcTemplate.batchUpdate(SQL_INSERT_XSPYJH, new BatchPreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement preparedStatement, int i) throws SQLException {
                //(XN, XQM, XH, XM, NJ, BJMC, ZYH, YXSH, XXDM, KCH, KCMC, XF, XDZT, JD)
                EtlStudendStudyPlanDTO d = list.get(i);
                preparedStatement.setString(1, d.getXn());//XN
                preparedStatement.setString(2, d.getXqm());//XQ
                preparedStatement.setString(3, d.getXh());//XH
                preparedStatement.setString(4, d.getXm());//XM
                preparedStatement.setString(5, d.getNj());//NJ
                preparedStatement.setString(6, d.getBjmc());//BJMC
                preparedStatement.setString(7, d.getZyh());//ZYH
                preparedStatement.setString(8, d.getYxsh());//YXSH
                preparedStatement.setString(9, d.getXxdm());//XXDM
                preparedStatement.setString(10, d.getKch());//KCH
                preparedStatement.setString(11, d.getKcmc());//KCMC
                preparedStatement.setDouble(12, d.getXf());//XF
                preparedStatement.setInt(13, 100);//XDZT
                preparedStatement.setDouble(14, 0);//JD
            }

            @Override
            public int getBatchSize() {
                return list.size();
            }
        });
    }

    /**
     * 查询特定截止日期特定学年的学院的学生培养计划的学生学号
     */
    @Transactional(readOnly = true)
    public List<String> queryXspyjhXh(String xxdm, String yxsh, String xn) {
        if (StringUtils.isEmpty(yxsh)) {
            return jdbcTemplate.query(SQL_XSPYJH,
                    new Object[]{xxdm, xn},
                    new int[]{Types.VARCHAR, Types.VARCHAR},
                    (ResultSet rs, int rowNum) -> rs.getString("XH")
            );
        } else {
            return jdbcTemplate.query(SQL_XSPYJH_YXSH,
                    new Object[]{xxdm, yxsh, xn},
                    new int[]{Types.VARCHAR, Types.VARCHAR, Types.VARCHAR},
                    (ResultSet rs, int rowNum) -> rs.getString("XH")
            );
        }
    }


    /**
     * 查询学生的所有课程的最大成绩及几点
     */
    @Transactional(readOnly = true)
    public List<EtlStudentStudyPlanXdztDTO> queryXskccj(String xxdm, String xh) {
        return jdbcTemplate.query(SQL_XS_KCCJ,
                new Object[]{xxdm, xh},
                new int [] {Types.VARCHAR, Types.VARCHAR},
                (ResultSet rs, int rowNum) -> new EtlStudentStudyPlanXdztDTO(rs.getString("XH"), rs.getString("KCH"), rs.getDouble("CJ"), rs.getDouble("JD"))
        );
    }


    /**
     * 查询学生对应课程的培养计划
     */
    @Transactional(readOnly = true)
    public List<EtlStudentStudyPlanXdztDTO> queryXsKcPyjh(String xh, Set<String> kchs, String xxdm) {
        NamedParameterJdbcTemplate template = new NamedParameterJdbcTemplate(jdbcTemplate);
        Map<String, Object> params = new HashMap<>();
        params.put("xh", xh);
        params.put("kchs", kchs);
        params.put("xxdm", xxdm);
        return template.query(SQL_XS_PYJH_XH_KCH, params, (ResultSet rs, int rowNum) -> new EtlStudentStudyPlanXdztDTO(rs.getLong("ID"), rs.getString("XH"), rs.getString("KCH")));
//        return jdbcTemplate.query(SQL_XS_PYJH_XH_KCH,
//                new Object[]{xh, kchs, xxdm},
//                new int [] {Types.VARCHAR , Types.VARCHAR, Types.VARCHAR},
//                (ResultSet rs, int rowNum) -> new EtlStudentStudyPlanXdztDTO(rs.getLong("ID"), rs.getString("XH"), rs.getString("KCH"))
//        );
    }

    /**
     * 更新学生的学习计划
     */
    public void updateXsStudyPlan(EtlStudentStudyPlanXdztDTO d) {
        jdbcTemplate.update(SQL_XS_PYJH_UPDATE_XDZT,
                new Object[]{d.getXdzt(), d.getJd(), d.getId()},
                new int [] {Types.INTEGER, Types.DOUBLE, Types.BIGINT}
        );
    }
}
