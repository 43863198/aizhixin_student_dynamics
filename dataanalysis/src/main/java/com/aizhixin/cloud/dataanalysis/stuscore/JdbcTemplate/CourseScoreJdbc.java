package com.aizhixin.cloud.dataanalysis.stuscore.JdbcTemplate;

import com.aizhixin.cloud.dataanalysis.alertinformation.dto.WarningDetailsDTO;
import com.aizhixin.cloud.dataanalysis.common.PageData;
import com.aizhixin.cloud.dataanalysis.common.domain.SortDTO;
import com.aizhixin.cloud.dataanalysis.common.util.PageJdbcUtil;
import com.aizhixin.cloud.dataanalysis.stuscore.domain.CourseScoreDomain;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Component
public class CourseScoreJdbc {

    @Autowired
    private PageJdbcUtil pageJdbcUtil;

    public PageData<CourseScoreDomain> getByStu(Long orgId, String jobNum, String courseName, Integer pageNumber, Integer pageSize){
        String querySql = "";
        String countSql = "";
        RowMapper<CourseScoreDomain> rowMapper = new RowMapper<CourseScoreDomain>() {
            @Override
            public CourseScoreDomain mapRow(ResultSet resultSet, int i) throws SQLException {
                return null;
            }
        };
        List<SortDTO> sort = new ArrayList<SortDTO>();
        SortDTO dto = new SortDTO();
        dto.setKey("WARNING_TIME");
        dto.setAsc(false);
        return pageJdbcUtil.getPageInfor2(pageSize, pageNumber, rowMapper, sort, querySql, countSql);
    }
}
