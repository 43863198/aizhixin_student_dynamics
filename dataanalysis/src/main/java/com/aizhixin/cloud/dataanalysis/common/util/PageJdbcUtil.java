
package com.aizhixin.cloud.dataanalysis.common.util;

import com.aizhixin.cloud.dataanalysis.common.PageData;
import com.aizhixin.cloud.dataanalysis.common.PageDomain;
import com.aizhixin.cloud.dataanalysis.common.core.ApiReturnConstants;
import com.aizhixin.cloud.dataanalysis.common.core.PaginationCore;
import com.aizhixin.cloud.dataanalysis.common.domain.SortDTO;
import org.apache.commons.collections.map.HashedMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Map;


@Repository
public class PageJdbcUtil {
    private static Logger log = LoggerFactory.getLogger(PageJdbcUtil.class);
    @Autowired
    private JdbcTemplate jdbcTemplate;


    public <T> Map<String, Object> getPageInfor(Integer pageSize, Integer offset, RowMapper<T> rowMapper,
                                                List<SortDTO> sort, String querySql, String countSql) {
        Map<String, Object> r = new HashedMap();
        if (pageSize == null || pageSize <= 0){
            pageSize = PaginationCore.DEFAULT_LIMIT;
        }
        if (offset == null || offset <= 0){
            offset = PaginationCore.DEFAULT_OFFSET;
        }

        Long totalCount = jdbcTemplate.queryForObject(
                countSql, Long.class);
        String orderBy = "";
        // sort不为空时按页面输入排序操作
        if (sort != null) {
            orderBy = getOrderByStatement(sort);
        } else {

        }
        querySql = querySql + orderBy;
        int start = (offset - 1) * pageSize;
        querySql += " limit " + start + " , " + pageSize;
        log.debug("find page = {} " + querySql);
        List<T> data = jdbcTemplate.query(querySql, rowMapper);
        log.debug("data size : {} ", data.size());

        int pageCount = totalCount == 0 ? 1 : (int) Math
                .ceil((double) totalCount / (double) pageSize);
        PageDomain p = new PageDomain();
        p.setPageNumber(offset);
        p.setPageSize(pageSize);
        p.setTotalElements(totalCount);
        p.setTotalPages(pageCount);
        r.put(ApiReturnConstants.PAGE, p);
        r.put(ApiReturnConstants.DATA, data);
        return r;
    }

    public <T> PageData<T> getPageInfor2(Integer pageSize, Integer offset, RowMapper<T> rowMapper,
                                         List<SortDTO> sort, String querySql, String countSql) {

        if (pageSize == null || pageSize <= 0) {
            pageSize = PaginationCore.DEFAULT_LIMIT;
        }
        if (offset == null || offset <= 0) {
            offset = PaginationCore.DEFAULT_OFFSET;
        }
        Long totalCount = jdbcTemplate.queryForObject(countSql, Long.class);
        String orderBy = "";
        // sort不为空时按页面输入排序操作
        if (sort != null) {
            orderBy = getOrderByStatement(sort);
        }
        querySql = querySql + orderBy;
        int start = (offset - 1) * pageSize;
        querySql += " limit " + start + ", " + pageSize;
        log.debug("find page = {} " + querySql);
        List<T> data = jdbcTemplate.query(querySql, rowMapper);
        log.debug("data size : {} ", data.size());

        int pageCount = totalCount == 0 ? 1 : (int) Math.ceil((double) totalCount / (double) pageSize);
        PageDomain p = new PageDomain();
        p.setPageNumber(offset);
        p.setPageSize(pageSize);
        p.setTotalElements(totalCount);
        p.setTotalPages(pageCount);
        PageData<T> pageData = new PageData();
        pageData.setPage(p);
        pageData.setData(data);
        return pageData;
    }

    private String getOrderByStatement(List<SortDTO> sorts) {
        if (sorts == null) {
            return "";
        } else {
            String s = "";
            log.debug("sorts size : {} ", sorts.size());
            for (int i = 0; i < sorts.size(); i++) {
                SortDTO sdto = sorts.get(i);
                if (sdto.getAsc()) {
                    s += sdto.getKey() + " ASC";
                } else {
                    s += sdto.getKey() + " DESC";
                }
                if (i < sorts.size() - 1) {
                    s += " , ";
                }
            }
            if (!StringUtils.isEmpty(s)) {
                s = " ORDER BY " + s;
            }
            return s;
        }
    }

    public <T> List<T> getInfo(String sql, RowMapper<T> rowMapper) {

        log.debug("getInfo sql = {} " + sql);
        List<T> data = jdbcTemplate.query(sql, rowMapper);
        log.debug("getInfo data size : {} ", data.size());

        return data;
    }

    public Long getCount(String sql) {
        Long totalCount = jdbcTemplate.queryForObject(
                sql, Long.class);
        return totalCount;
    }
}
