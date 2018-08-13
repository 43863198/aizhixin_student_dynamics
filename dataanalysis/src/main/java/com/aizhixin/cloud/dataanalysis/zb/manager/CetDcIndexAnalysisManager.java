package com.aizhixin.cloud.dataanalysis.zb.manager;

import com.aizhixin.cloud.dataanalysis.zb.dto.AnalysisBasezbDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.sql.ResultSet;
import java.sql.Types;
import java.util.List;

@Transactional
@Component
public class CetDcIndexAnalysisManager {

    public static String SQL_DC_SCHOOL = "SELECT " +
            "x.XN, x.XQM, x.KSLX,null AS P_BH, x.XXDM AS BH," +
            "COUNT(x.XH) AS CKRC,SUM(x.CJ) AS ZF, MAX(x.CJ) AS  GF, " +
            "SUM(IF(x.CJ >= IF(x.KSLX = '3', 60, 425), 1, 0)) AS TGRC, " +
            "SUM(IF(x.XBM = '1', 1, 0)) AS NRC, SUM(IF(x.XBM = '1', x.CJ, 0)) AS NZF," +
            "SUM(IF(x.XBM = '2', 1, 0)) AS VRC, SUM(IF(x.XBM = '2', x.CJ, 0)) AS VZF," +
            "SUM(IF(x.XBM = '1', IF(x.CJ >= IF(x.KSLX = '3', 60, 425), 1, 0), 0)) AS NTGRC," +
            "SUM(IF(x.XBM = '2', IF(x.CJ >= IF(x.KSLX = '3', 60, 425), 1, 0), 0)) AS VTGRC " +
            "FROM t_b_djksxx x " +
            "WHERE x.XXDM=? " +
            "GROUP BY x.KSLX,x.XN, x.XQM, x.XXDM";

    public static String SQL_DC_COLLEGE = "SELECT " +
            "x.XN, x.XQM, x.KSLX,x.XXDM AS P_BH, x.YXSH AS BH," +
            "COUNT(x.XH) AS CKRC,SUM(x.CJ) AS ZF, MAX(x.CJ) AS  GF, " +
            "SUM(IF(x.CJ >= IF(x.KSLX = '3', 60, 425), 1, 0)) AS TGRC, " +
            "SUM(IF(x.XBM = '1', 1, 0)) AS NRC, SUM(IF(x.XBM = '1', x.CJ, 0)) AS NZF," +
            "SUM(IF(x.XBM = '2', 1, 0)) AS VRC, SUM(IF(x.XBM = '2', x.CJ, 0)) AS VZF," +
            "SUM(IF(x.XBM = '1', IF(x.CJ >= IF(x.KSLX = '3', 60, 425), 1, 0), 0)) AS NTGRC," +
            "SUM(IF(x.XBM = '2', IF(x.CJ >= IF(x.KSLX = '3', 60, 425), 1, 0), 0)) AS VTGRC " +
            "FROM t_b_djksxx x " +
            "WHERE x.XXDM=? " +
            "GROUP BY x.KSLX,x.XN, x.XQM, x.XXDM, x.YXSH";

    public static String SQL_DC_PROFESSIONAL = "SELECT " +
            "x.XN, x.XQM, x.KSLX,x.YXSH AS P_BH, x.ZYH AS BH," +
            "COUNT(x.XH) AS CKRC,SUM(x.CJ) AS ZF, MAX(x.CJ) AS  GF, " +
            "SUM(IF(x.CJ >= IF(x.KSLX = '3', 60, 425), 1, 0)) AS TGRC, " +
            "SUM(IF(x.XBM = '1', 1, 0)) AS NRC, SUM(IF(x.XBM = '1', x.CJ, 0)) AS NZF," +
            "SUM(IF(x.XBM = '2', 1, 0)) AS VRC, SUM(IF(x.XBM = '2', x.CJ, 0)) AS VZF," +
            "SUM(IF(x.XBM = '1', IF(x.CJ >= IF(x.KSLX = '3', 60, 425), 1, 0), 0)) AS NTGRC," +
            "SUM(IF(x.XBM = '2', IF(x.CJ >= IF(x.KSLX = '3', 60, 425), 1, 0), 0)) AS VTGRC " +
            "FROM t_b_djksxx x " +
            "WHERE x.XXDM=? " +
            "GROUP BY x.KSLX,x.XN, x.XQM, x.YXSH, x.ZYH";

    public static String SQL_DC_CLASSES = "SELECT " +
            "x.XN, x.XQM, x.KSLX,x.ZYH AS P_BH, x.BH AS BH," +
            "COUNT(x.XH) AS CKRC,SUM(x.CJ) AS ZF, MAX(x.CJ) AS  GF, " +
            "SUM(IF(x.CJ >= IF(x.KSLX = '3', 60, 425), 1, 0)) AS TGRC, " +
            "SUM(IF(x.XBM = '1', 1, 0)) AS NRC, SUM(IF(x.XBM = '1', x.CJ, 0)) AS NZF," +
            "SUM(IF(x.XBM = '2', 1, 0)) AS VRC, SUM(IF(x.XBM = '2', x.CJ, 0)) AS VZF," +
            "SUM(IF(x.XBM = '1', IF(x.CJ >= IF(x.KSLX = '3', 60, 425), 1, 0), 0)) AS NTGRC," +
            "SUM(IF(x.XBM = '2', IF(x.CJ >= IF(x.KSLX = '3', 60, 425), 1, 0), 0)) AS VTGRC " +
            "FROM t_b_djksxx x " +
            "WHERE x.XXDM=? " +
            "GROUP BY x.KSLX,x.XN, x.XQM, x.YXSH, x.ZYH, x.BH";

    public static String SQL_DELETE_ALL_SHOOL_DC = "DELETE FROM t_zb_djksjc WHERE DHLJ='1' AND XXDM=?";

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Transactional(readOnly = true)
    public List<AnalysisBasezbDTO> queryDcJczb(String sql, String xxdm) {
        return jdbcTemplate.query(sql.toString(),
                new Object[]{xxdm},
                new int [] {Types.VARCHAR},
                (ResultSet rs, int rowNum) -> new AnalysisBasezbDTO (
                        rs.getString("XN"), rs.getString("XQM"),
                        rs.getString("KSLX"), rs.getString("P_BH"),rs.getString("BH"),
                        rs.getLong("CKRC"), rs.getDouble("ZF"),
                        rs.getDouble("GF") , null, rs.getLong("TGRC"), rs.getLong("NRC"),
                        rs.getDouble("NZF"), rs.getLong("VRC"), rs.getDouble("VZF"),
                        rs.getLong("NTGRC"), rs.getLong("VTGRC")));
    }

    public void deleteHistory(String sql, String xxdm) {
        jdbcTemplate.update(sql, new Object[]{ xxdm}, new int[] {Types.VARCHAR});
    }
}
