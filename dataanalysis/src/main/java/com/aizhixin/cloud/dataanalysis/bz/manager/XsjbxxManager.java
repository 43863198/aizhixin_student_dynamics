package com.aizhixin.cloud.dataanalysis.bz.manager;

import com.aizhixin.cloud.dataanalysis.analysis.dto.CodeNameCountDTO;
import com.aizhixin.cloud.dataanalysis.common.util.DateUtil;
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
public class XsjbxxManager {


    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Transactional(readOnly = true)
    public List<CodeNameCountDTO> queryYxsZxrs(Long orgId) {
        String sql = " select YXSH, YXSMC, count(*) count from t_xsjbxx where XXID=? and RXNY <= ? and YBYNY >=? group by YXSH, YXSMC";
        Date d = new Date();
        String yyyyMM = DateUtil.format(d, "yyyyMM");
        return jdbcTemplate.query(sql.toString(), new Object[]{orgId, yyyyMM, d}, new int [] {Types.BIGINT, Types.VARCHAR, Types.DATE},
            (ResultSet rs, int rowNum) ->
                new CodeNameCountDTO (rs.getString("YXSH"), rs.getString("YXSMC"),rs.getLong("count"))
            );
    }
}
