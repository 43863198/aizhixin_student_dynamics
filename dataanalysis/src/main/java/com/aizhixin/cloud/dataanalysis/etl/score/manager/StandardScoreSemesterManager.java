package com.aizhixin.cloud.dataanalysis.etl.score.manager;

import com.aizhixin.cloud.dataanalysis.zb.app.entity.StudentSemesterScoreIndex;
import com.aizhixin.cloud.dataanalysis.etl.score.dto.StudentSemesterScoreIndexDTO;
import com.aizhixin.cloud.dataanalysis.etl.cet.dto.XnXqDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.sql.ResultSet;
import java.sql.Types;
import java.util.List;

@Transactional
@Component
public class StandardScoreSemesterManager {
    public static String SQL_GPA = "SELECT j.YXSH, j.ZYH, j.BH, j.XH, j.XM, j.NJ,  ROUND(SUM(j.XF*j.JD)/SUM(j.XF),2) AS GPA " +
            "FROM (" +
            "SELECT c.XN, c.XQM, c.YXSH, c.ZYH, c.BH, c.XH, c.XM, c.NJ, MAX(c.JD) AS JD,c.XF " +
            "FROM t_b_xscjxx c " +
            "WHERE c.KCLBM='1' AND c.XXDM=? AND c.XN=? AND c.XQM=? " +
            "GROUP BY " +
            "c.XN, c.XQM, c.YXSH, c.ZYH, c.BH, c.XH, c.XM, c.NJ, c.KCH,c.XF " +
            "ORDER BY c.XH, c.KCH " +
            ") j " +
            "GROUP BY j.YXSH, j.ZYH, j.BH, j.XH, j.XM, j.NJ ";

    public static String SQL_SEMESTER_ZB = "SELECT m.XH,  " +
            "COUNT(m.XH) AS CKKM," +
            "SUM(IF(m.CJ<60,1,0)) AS BJGKCS," +
            "SUM(IF(m.CJ<60, m.XF, 0)) AS BJGZXF " +
            "FROM (" +
            "SELECT c.XH, c.XF, MAX(c.BFCJ) AS CJ " +
            "FROM t_b_xscjxx c " +
            "WHERE c.XXDM=? AND c.XN=? AND c.XQM=? " +
            "GROUP BY c.XH, c.KCH, c.XF " +
            "ORDER BY c.XH, c.KCH " +
            ") m " +
            "GROUP BY m.XH";

    public static String SQL_ALL_SCORE_SEMESTER = "SELECT c.XN, c.XQM FROM  t_b_xscjxx c WHERE c.XXDM=? GROUP BY c.XN, c.XQM ORDER BY c.XN DESC, c.XQM DESC";

    public static String SQL_DELETE_XN_XQ = "DELETE FROM  T_ZB_XSXQCJ  WHERE XXDM=? AND XN=? AND XQM=?";

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Transactional(readOnly = true)
    public List<StudentSemesterScoreIndex> queryStudentSemsterGpaIndex(String xxdm, String xn, String xq) {
        return jdbcTemplate.query(SQL_GPA,
                new Object[]{xxdm, xn, xq},
                new int [] {Types.VARCHAR, Types.VARCHAR, Types.VARCHAR},
                (ResultSet rs, int rowNum) -> new StudentSemesterScoreIndex( rs.getString("YXSH"),
                        rs.getString("ZYH"),rs.getString("BH"),
                        rs.getString("XH"), rs.getString("XM"),
                        rs.getString("NJ"), rs.getDouble("GPA"))
        );
    }

    @Transactional(readOnly = true)
    public List<StudentSemesterScoreIndexDTO> queryStudentSemsterIndex(String xxdm, String xn, String xq) {
        return jdbcTemplate.query(SQL_SEMESTER_ZB,
                new Object[]{xxdm, xn, xq},
                new int [] {Types.VARCHAR, Types.VARCHAR, Types.VARCHAR},
                (ResultSet rs, int rowNum) -> new StudentSemesterScoreIndexDTO(
                        rs.getString("XH"), rs.getLong("CKKM"),
                        rs.getLong("BJGKCS"), rs.getDouble("BJGZXF"))
        );
    }

    @Transactional(readOnly = true)
    public List<XnXqDTO> queryAllYearAndSemester(String xxdm) {
        return jdbcTemplate.query(SQL_ALL_SCORE_SEMESTER,
                new Object[]{xxdm},
                new int [] {Types.VARCHAR},
                (ResultSet rs, int rowNum) -> new XnXqDTO(rs.getString("XN"),
                        rs.getString("XQM"))
        );
    }

    public void deleteXnXqIndex(String xxdm, String xn, String xq) {
        jdbcTemplate.update(SQL_DELETE_XN_XQ,
                new Object[]{xxdm, xn, xq},
                new int [] {Types.VARCHAR, Types.VARCHAR, Types.VARCHAR});
    }
}