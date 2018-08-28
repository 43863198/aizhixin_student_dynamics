package com.aizhixin.cloud.dataanalysis.etl.cet.manager;

import com.aizhixin.cloud.dataanalysis.etl.cet.dto.NjAnalysiszbDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.sql.ResultSet;
import java.sql.Types;
import java.util.List;

@Transactional
@Component
public class CetDcGradeManager {
    public static String SQL_DC_NJ_SCHOOL = "SELECT s.XN, s.XQM, s.KSLX," +
            "null AS P_BH, s.XXDM AS BH, s.NJ, " +
            "COUNT(s.XH) AS CKRC, SUM(s.CJ) AS ZF, " +
            "SUM(IF(s.CJ >= IF(s.KSLX='3', 60, 425), 1, 0)) as TGRC " +
            "FROM t_b_djksxx s " +
            "WHERE s.XXDM = ? " +
            "GROUP BY s.XN, s.XQM, s.KSLX, s.NJ";

    public static String SQL_DC_NJ_COLLEGE = "SELECT s.XN, s.XQM, s.KSLX," +
            "s.XXDM AS P_BH, s.YXSH AS BH, s.NJ, " +
            "COUNT(s.XH) AS CKRC, SUM(s.CJ) AS ZF, " +
            "SUM(IF(s.CJ >= IF(s.KSLX='3', 60, 425), 1, 0)) as TGRC " +
            "FROM t_b_djksxx s " +
            "WHERE s.XXDM = ?  " +
            "GROUP BY s.XN, s.XQM, s.KSLX, s.NJ, s.YXSH";

    public static String SQL_DC_NJ_PROFESSIONAL = "SELECT s.XN, s.XQM, s.KSLX," +
            "s.YXSH AS P_BH, s.ZYH AS BH, s.NJ, " +
            "COUNT(s.XH) AS CKRC, SUM(s.CJ) AS ZF, " +
            "SUM(IF(s.CJ >= IF(s.KSLX='3', 60, 425), 1, 0)) as TGRC " +
            "FROM t_b_djksxx s " +
            "WHERE s.XXDM = ?  " +
            "GROUP BY s.XN, s.XQM, s.KSLX, s.NJ, s.YXSH, s.ZYH";

    public static String SQL_DC_NJ_CLASSES = "SELECT s.XN, s.XQM, s.KSLX," +
            "s.ZYH AS P_BH, s.BH AS BH, s.NJ, " +
            "COUNT(s.XH) AS CKRC, SUM(s.CJ) AS ZF, " +
            "SUM(IF(s.CJ >= IF(s.KSLX='3', 60, 425), 1, 0)) as TGRC " +
            "FROM t_b_djksxx s " +
            "WHERE s.XXDM = ? " +
            "GROUP BY s.XN, s.XQM, s.KSLX, s.NJ, s.YXSH, s.ZYH, s.BH";

    public static String SQL_DELETE_DC_NJZB = "DELETE FROM t_zb_djksnj WHERE  DHLJ='1' AND XXDM=?";

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Transactional(readOnly = true)
    public List<NjAnalysiszbDTO> queryNjDczb(String sql, String xxdm) {
        return jdbcTemplate.query(sql.toString(),
                new Object[]{xxdm},
                new int [] {Types.VARCHAR},
                (ResultSet rs, int rowNum) -> new NjAnalysiszbDTO(rs.getString("XN"), rs.getString("XQM"), rs.getString("KSLX"),
                        rs.getString("P_BH"),rs.getString("BH"),
                        rs.getString("NJ"), rs.getLong("CKRC"),
                        rs.getDouble("ZF"), rs.getLong("TGRC"))
        );
    }
}
