package com.aizhixin.cloud.dataanalysis.dd.rollcall.manager;

import com.aizhixin.cloud.dataanalysis.common.PageData;
import com.aizhixin.cloud.dataanalysis.common.core.PageUtil;
import com.aizhixin.cloud.dataanalysis.common.util.DateUtil;
import com.aizhixin.cloud.dataanalysis.dd.rollcall.entity.Lastest3RollcallAlert;
import com.aizhixin.cloud.dataanalysis.dd.rollcall.vo.Lastest3RollcallAlertVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import java.util.*;

@Component
public class Lastest3RollcallAlertManager {
    @Autowired
    private EntityManager em;
//    @Autowired
//    private Lastest3RollcallAlertRepository lastest3RollcallAlertRepository;

    public PageData<Lastest3RollcallAlertVO> findLastest3RollcallAlert (Long orgId, Long collegeId, String name, Date start, Date end, Integer pageIndex, Integer pageSize) {
        PageData<Lastest3RollcallAlertVO> pageData = new PageData<>();
        if (null == pageIndex || pageIndex < 1) {
            pageIndex = 1;
        }
        if (null == pageSize || pageSize <= 0) {
            pageSize = 20;
        }
        pageData.getPage().setPageNumber(pageIndex);
        pageData.getPage().setPageSize(pageSize);
        pageData.getPage().setTotalElements(0L);
        pageData.getPage().setTotalPages(1);

        if (null == orgId || orgId <= 0) {
            return pageData;
        }

        StringBuilder sql = new StringBuilder(" FROM  com.aizhixin.cloud.dataanalysis.dd.rollcall.entity.Lastest3RollcallAlert t WHERE t.orgId = :orgId");
        StringBuilder sqlc = new StringBuilder("SELECT COUNT(*) FROM  com.aizhixin.cloud.dataanalysis.dd.rollcall.entity.Lastest3RollcallAlert t WHERE t.orgId = :orgId");
        Map<String, Object> params = new HashMap<>();
        params.put("orgId", orgId);

        Date cur = new Date();
        if (null == start) {
            start = DateUtil.afterNDay(cur, -7);
            start =  DateUtil.getZerotime(start);
        } else {
            start = DateUtil.getZerotime(start);
        }
        if (null == end) {
            end = DateUtil.afterNDay(cur, 1);
            end =  DateUtil.getZerotime(end);
        } else {
            end = DateUtil.afterNDay(end, 1);
            end = DateUtil.getZerotime(end);
        }
        params.put("start", start);
        params.put("end", end);
        sql.append(" AND t.calDate BETWEEN :start AND :end");
        sqlc.append(" AND t.calDate BETWEEN :start AND :end");

        if (null !=  collegeId && collegeId > 0) {
            params.put("collegeId", collegeId);
            sql.append(" AND t.collegeId = :collegeId");
            sqlc.append(" AND t.collegeId = :collegeId");
        }
        if (!StringUtils.isEmpty(name)) {
            params.put("name", "%" + name + "%");
            sql.append(" AND (t.studentNo LIKE :name OR t.studentName LIKE :name)");
            sqlc.append(" AND (t.studentNo LIKE :name OR t.studentName LIKE :name)");
        }
        Query q = em.createQuery(sqlc.toString());
        for (Map.Entry<String, Object> e : params.entrySet()) {
            q.setParameter(e.getKey(), e.getValue());
        }
        Long count = (Long) q.getSingleResult();
        pageData.getPage().setTotalElements(count);
        pageData.getPage().setTotalPages(PageUtil.cacalatePagesize(count, pageSize));
        if (count <= 0) {
            return pageData;
        }

        TypedQuery<Lastest3RollcallAlert> tq = em.createQuery(sql.toString(), Lastest3RollcallAlert.class);
        for (Map.Entry<String, Object> e : params.entrySet()) {
            tq.setParameter(e.getKey(), e.getValue());
        }
        tq.setFirstResult((pageIndex - 1) *  pageSize);
        tq.setMaxResults(pageSize);
        List<Lastest3RollcallAlertVO> list = new ArrayList<>();
        for (Lastest3RollcallAlert d : tq.getResultList()) {
            list.add(new Lastest3RollcallAlertVO(d.getStudentNo(), d.getStudentName(), d.getClassesName(), d.getProfessionalName(), d.getCollegeName(), d.getDateRange(), d.getShouldCount(), d.getNormal(), d.getDkl()));
        }
        pageData.setData(list);

        return pageData;
    }
}
