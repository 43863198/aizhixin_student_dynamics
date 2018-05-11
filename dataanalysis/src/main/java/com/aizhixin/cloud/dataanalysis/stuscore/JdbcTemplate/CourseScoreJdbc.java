package com.aizhixin.cloud.dataanalysis.stuscore.JdbcTemplate;

import com.aizhixin.cloud.dataanalysis.common.PageData;
import com.aizhixin.cloud.dataanalysis.common.domain.SortDTO;
import com.aizhixin.cloud.dataanalysis.common.util.PageJdbcUtil;
import com.aizhixin.cloud.dataanalysis.stuscore.domain.CourseScoreDomain;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Component
public class CourseScoreJdbc {

    @Autowired
    private PageJdbcUtil pageJdbcUtil;

    public PageData<CourseScoreDomain> getByStu(Long orgId, String jobNum, String courseName, Integer pageNumber, Integer pageSize) {
        String querySql = "SELECT tx.XSXM,tx.XH,tx.XN,tx.XKSX,tc.CREDIT,tx.KHM,tx.JSXM,tx.DJLKSCJ,tx.KCCJ,tx.PSCJ,tx.FSLKSCJ,tx.JD FROM t_xscjxx tx LEFT JOIN t_course tc ON tc.COURSE_NUMBER=tx.KCH WHERE tx.XH='" + jobNum + "'";
        String countSql = "SELECT count(*) FROM t_xscjxx tx WHERE tx.XH='" + jobNum + "'";
        if (StringUtils.isNotEmpty(courseName)) {
            querySql += " AND tx.KHM LIKE '%" + courseName + "%'";
            countSql += " AND tx.KHM LIKE '%" + courseName + "%'";
        }
        RowMapper<CourseScoreDomain> rowMapper = new RowMapper<CourseScoreDomain>() {
            @Override
            public CourseScoreDomain mapRow(ResultSet resultSet, int i) throws SQLException {
                CourseScoreDomain d = new CourseScoreDomain();
                d.setName(resultSet.getString("XSXM"));
                d.setJobNum(resultSet.getString("XH"));
                d.setTeachingYear(resultSet.getInt("XN"));
                d.setXksx(resultSet.getString("XKSX"));
                d.setCourseCredit(resultSet.getFloat("CREDIT"));
                d.setCourseName(resultSet.getString("KHM"));
                d.setCourseTeacher(resultSet.getString("JSXM"));
                d.setDjlkscj(resultSet.getString("DJLKSCJ"));
                d.setKccj(resultSet.getFloat("KCCJ"));
                d.setPscj(resultSet.getFloat("PSCJ"));
                d.setFslkscj(resultSet.getFloat("FSLKSCJ"));
                d.setJd(resultSet.getFloat("JD"));
                return d;
            }
        };
        List<SortDTO> sort = new ArrayList<SortDTO>();
        SortDTO dto = new SortDTO();
        dto.setKey("CJLRRQ");
        dto.setAsc(false);
        sort.add(dto);
        return pageJdbcUtil.getPageInfor2(pageSize, pageNumber, rowMapper, sort, querySql, countSql);
    }
}
