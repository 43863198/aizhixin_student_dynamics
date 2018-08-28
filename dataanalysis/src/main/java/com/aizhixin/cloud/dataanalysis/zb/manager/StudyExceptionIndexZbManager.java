package com.aizhixin.cloud.dataanalysis.zb.manager;

import com.aizhixin.cloud.dataanalysis.common.util.DateUtil;
import com.aizhixin.cloud.dataanalysis.zb.app.entity.StudyExceptionIndex;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.sql.ResultSet;
import java.sql.Types;
import java.util.Date;
import java.util.List;

@Transactional
@Component
public class StudyExceptionIndexZbManager {
    public static String SQL_XDYC_COLLEGE = "SELECT x.XH, x.XM, x.BJMC, x.ZYH, x.ZYMC, x.YXSH, x.YXSMC, " +
            "COUNT(p.KCH) AS KCS," +
            "SUM(IF(p.XDZT=100, 1, 0)) AS LXKCS," +
            "SUM(IF(p.XDZT!=100, 1, 0)) AS TGKCS," +
            "SUM(p.XF) AS XF," +
            "SUM(IF(p.XDZT=100, p.XF, 0)) AS LXXF," +
            "SUM(IF(p.XDZT!=100, p.XF, 0)) AS TGXF," +
            "SUM(IF(p.KCLB='1', IF(p.XDZT=100, p.XF, 0), 0)) AS BXBJGXF, " +
            "GROUP_CONCAT(IF(p.XDZT=100,CONCAT(';课程号:', p.KCH, ',课程名:', p.KCMC, ',学分:', p.XF), '')) AS LXKCNR," +
            "GROUP_CONCAT(IF(p.KCLB='1', CONCAT(';课程号:', p.KCH, ',课程名:', p.KCMC, ',学分:', p.XF), ''))  AS BXBJGKCNR " +
            "FROM t_xsjbxx x LEFT JOIN t_xspyjh p ON x.XH=p.XH " +
            "WHERE x.XXID=? AND x.YXSH=? AND p.XXDM=? AND p.YXSH=? AND x.RXNY<=? AND ? <= x.YBYNY " +
            "GROUP BY x.XH, x.XM, x.BJMC, x.ZYH, x.ZYMC, x.YXSH, x.YXSMC";

    public static String SQL_ZXXS_YXSH = "SELECT DISTINCT x.YXSH FROM t_xsjbxx x WHERE x.XXID=? AND x.RXNY<=? AND ? <= x.YBYNY";

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Transactional(readOnly = true)
    public List<String> queryAllYxsh(Long orgId, Date date) {
        String start = DateUtil.formatYearMonth(date);
        return jdbcTemplate.query(SQL_ZXXS_YXSH,
                new Object[]{orgId, start, date},
                new int [] {Types.BIGINT, Types.VARCHAR, Types.DATE},
                (ResultSet rs, int rowNum) -> rs.getString("YXSH"));
    }

    @Transactional(readOnly = true)
    public List<StudyExceptionIndex> queryXyxdyczb(Long orgId, String yxsh, Date date) {
        String start = DateUtil.formatYearMonth(date);
        return jdbcTemplate.query(SQL_XDYC_COLLEGE,
                new Object[]{orgId, yxsh, orgId.toString(), yxsh, start, date},
                new int [] {Types.BIGINT, Types.VARCHAR, Types.VARCHAR, Types.VARCHAR, Types.VARCHAR, Types.DATE},
                (ResultSet rs, int rowNum) -> new StudyExceptionIndex(
                        rs.getString("XH"), rs.getString("XM"),
                        rs.getString("BJMC"), rs.getString("ZYH"),rs.getString("ZYMC"),
                        rs.getString("YXSH"), rs.getString("YXSMC"),
                        rs.getLong("KCS"), rs.getLong("LXKCS"), rs.getLong("TGKCS"),
                        rs.getDouble("XF"), rs.getDouble("LXXF"), rs.getDouble("TGXF"),
                        rs.getDouble("BXBJGXF"), rs.getString("LXKCNR"), rs.getString("BXBJGKCNR")));
    }
}
