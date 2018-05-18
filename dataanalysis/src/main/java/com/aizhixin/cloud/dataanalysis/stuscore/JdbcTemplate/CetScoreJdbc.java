package com.aizhixin.cloud.dataanalysis.stuscore.JdbcTemplate;

import com.aizhixin.cloud.dataanalysis.common.PageData;
import com.aizhixin.cloud.dataanalysis.common.domain.SortDTO;
import com.aizhixin.cloud.dataanalysis.common.util.PageJdbcUtil;
import com.aizhixin.cloud.dataanalysis.stuscore.domain.CetScoreDomain;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Component
public class CetScoreJdbc {
    @Autowired
    private PageJdbcUtil pageJdbcUtil;

    public PageData<CetScoreDomain> getByStu(Long orgId, String jobNum, Integer pageNumber, Integer pageSize) {
        String querySql = "SELECT ts.`NAME`,tcs.JOB_NUMBER,tc.TEACHER_YEAR,tc.SEMESTER,tcs.TYPE,tcs.SCORE, tcs.EXAMINATION_DATE FROM t_cet_score tcs LEFT JOIN t_student ts ON ts.JOB_NUMBER=tcs.JOB_NUMBER AND ts.ORG_ID=tcs.ORG_ID LEFT JOIN t_school_calendar tc ON tc.ORG_ID=tcs.ORG_ID AND tcs.EXAMINATION_DATE> tc.START_TIME AND tcs.EXAMINATION_DATE< tc.END_TIME WHERE tcs.ORG_ID='" + orgId + "' AND tcs.JOB_NUMBER='" + jobNum + "'";
        String countSql = "SELECT COUNT(*) FROM t_cet_score tcs LEFT JOIN t_student ts ON ts.JOB_NUMBER=tcs.JOB_NUMBER AND ts.ORG_ID=tcs.ORG_ID LEFT JOIN t_school_calendar tc ON tc.ORG_ID=tcs.ORG_ID AND tcs.EXAMINATION_DATE> tc.START_TIME AND tcs.EXAMINATION_DATE< tc.END_TIME WHERE tcs.ORG_ID='" + orgId + "' AND tcs.JOB_NUMBER='" + jobNum + "'";
        RowMapper<CetScoreDomain> rowMapper = new RowMapper<CetScoreDomain>() {
            @Override
            public CetScoreDomain mapRow(ResultSet resultSet, int i) throws SQLException {
                CetScoreDomain d = new CetScoreDomain();
                d.setName(resultSet.getString("NAME"));
                d.setJobNum(resultSet.getString("JOB_NUMBER"));
                d.setTeachingYear(resultSet.getInt("TEACHER_YEAR"));
                d.setSemester(resultSet.getString("SEMESTER"));
                d.setType(resultSet.getString("TYPE"));
                d.setScore(resultSet.getString("SCORE"));
                d.setDate(resultSet.getString("EXAMINATION_DATE"));
                return d;
            }
        };
        List<SortDTO> sort = new ArrayList<SortDTO>();
        SortDTO dto = new SortDTO();
        dto.setKey("EXAMINATION_DATE");
        dto.setAsc(false);
        sort.add(dto);
        return pageJdbcUtil.getPageInfor2(pageSize, pageNumber, rowMapper, sort, querySql, countSql);
    }
}
