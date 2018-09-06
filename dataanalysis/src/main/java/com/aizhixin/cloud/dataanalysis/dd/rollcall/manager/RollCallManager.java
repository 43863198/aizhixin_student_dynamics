package com.aizhixin.cloud.dataanalysis.dd.rollcall.manager;

import com.aizhixin.cloud.dataanalysis.dd.rollcall.vo.SchoolWeekRollcallScreenVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class RollCallManager {
    /**
     * 最近14天的考勤数据
     */
    private String SQL_DD_LASTEST14DAY_ROLLCALL = "SELECT   DATE_FORMAT(v.CREATED_DATE, '%Y-%m-%d') AS dateday,  COUNT(*) AS total,  SUM(IF(v.`TYPE` = 1, 1, 0)) AS normal," +
            "SUM(IF(v.`TYPE` = 3, 1, 0)) AS later,  SUM(IF(v.`TYPE` = 4, 1, 0)) AS askForLeave," +
            "SUM(IF(v.`TYPE` = 5, 1, 0)) AS leaveEarly,  SUM(IF(v.`TYPE` = 2, 1, 0)) AS truant " +
            "FROM  #database#.dd_rollcall v " +
            "WHERE v.org_id = ? AND v.CREATED_DATE BETWEEN ? AND ? " +
            "GROUP BY  dateday " +
            "ORDER BY dateday ";
    private String ddDatabaseName;
    @Autowired
    private JdbcTemplate jdbcTemplate;


    public List<SchoolWeekRollcallScreenVO> queryLastTwoWeekRollcall(Long orgId, String start, String end) {
        String ylsj = "2018-06-04,8434,7654,43,180,1,547\n" +
                "2018-06-05,8625,7486,46,324,2,735\n" +
                "2018-06-06,8303,7492,35,269,2,505\n" +
                "2018-06-07,6172,5669,48,129,0,296\n" +
                "2018-06-08,3380,2856,22,164,2,336\n" +
                "2018-06-09,172,169,0,3,0,0\n" +
                "2018-06-11,7826,7064,55,128,1,522\n" +
                "2018-06-12,8987,8127,59,113,5,683\n" +
                "2018-06-13,8070,7390,43,165,2,470\n" +
                "2018-06-14,6639,6083,47,94,2,413\n" +
                "2018-06-15,3023,2583,7,128,0,305\n" +
                "2018-06-16,3,3,0,0,0,0";
        List<SchoolWeekRollcallScreenVO> list = new ArrayList<>();
        String[] rr = ylsj.split("\\n");
        if (rr.length > 0) {
            for (String t : rr) {
                String[] fs = t.split(",");
                if (7 == fs.length) {
                    SchoolWeekRollcallScreenVO v = new SchoolWeekRollcallScreenVO(fs[0], new Integer(fs[1]), new Integer(fs[2]), new Integer(fs[3]), new Integer(fs[4]), new Integer(fs[5]), new Integer(fs[6]));
                    list.add(v);
                }
            }
        }
        return list;
    }
}
