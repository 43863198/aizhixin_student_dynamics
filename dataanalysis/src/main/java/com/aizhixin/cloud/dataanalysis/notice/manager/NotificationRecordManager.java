package com.aizhixin.cloud.dataanalysis.notice.manager;

import com.aizhixin.cloud.dataanalysis.common.PageData;
import com.aizhixin.cloud.dataanalysis.common.core.PageUtil;
import com.aizhixin.cloud.dataanalysis.common.util.DateUtil;
import com.aizhixin.cloud.dataanalysis.notice.entity.NotificationRecord;
import com.aizhixin.cloud.dataanalysis.notice.repository.NotificationRecordRepository;
import com.aizhixin.cloud.dataanalysis.notice.vo.NotificationRecordVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@Transactional
public class NotificationRecordManager {
    @Autowired
    private EntityManager em;

    @Autowired
    private NotificationRecordRepository repository;

    /**
     * 保存实体
     * @param entity 实体
     * @return	实体
     */
    public NotificationRecord save(NotificationRecord entity) {
        return repository.save(entity);
    }

    /**
     * 保存实体
     * @param entityList 实体
     * @return	实体
     */
    public List<NotificationRecord> save(List<NotificationRecord> entityList) {
        return repository.save(entityList);
    }

    public PageData<NotificationRecordVO> list (Pageable pageable, Long orgId, String collegeName, Integer rs, String receiver, Date startDate, Date endDate) {
        PageData<NotificationRecordVO> page = new PageData<>();
        page.getPage().setPageNumber(pageable.getPageNumber() + 1);
        page.getPage().setPageSize(pageable.getPageSize());
        Map<String, Object> condition = new HashMap<>();
        StringBuilder chql = new StringBuilder(
                "select count(t.id) from com.aizhixin.cloud.dataanalysis.notice.entity.NotificationRecord t where t.orgId = :orgId");
        StringBuilder hql = new StringBuilder(
                "select new com.aizhixin.cloud.dataanalysis.notice.vo.NotificationRecordVO(t.id, t.sendTime, t.lastAccessTime, t.receiverName, t.receiverCode, t.receiverPhone, t.collegeName, t.rs, t.failMsg, t.content) from com.aizhixin.cloud.dataanalysis.notice.entity.NotificationRecord t where t.orgId = :orgId");

        condition.put("orgId", orgId);

        if (!StringUtils.isEmpty(collegeName)) {
            hql.append(" and t.collegeName = :collegeName");
            chql.append(" and t.collegeName = :collegeName");
            condition.put("collegeName", collegeName);
        }
        if (null != rs) {
            hql.append(" and t.rs = :rs");
            chql.append(" and t.rs = :rs");
            condition.put("rs", rs);
        }
        if (!StringUtils.isEmpty(receiver)) {
            hql.append(" and (t.receiverName like :receiver or t.receiverCode like :receiver)");
            chql.append(" and (t.receiverName like :receiver or t.receiverCode like :receiver)");
            condition.put("receiver", "%" + receiver + "%");
        }
        if (null != startDate) {
            hql.append(" and t.sendTime >= :startDate");
            chql.append(" and t.sendTime >= :startDate");
            condition.put("startDate", startDate);
        }
        if (null != endDate) {
            endDate = DateUtil.nextDate(endDate);
            hql.append(" and t.sendTime < :endDate");
            chql.append(" and t.sendTime < :endDate");
            condition.put("endDate", endDate);
        }
        hql.append(" order by t.sendTime DESC");

        Query q = em.createQuery(chql.toString());
        for (Map.Entry<String, Object> e : condition.entrySet()) {
            q.setParameter(e.getKey(), e.getValue());
        }
        Long count = (Long) q.getSingleResult();
        if (count <= 0) {
            return page;
        }
        TypedQuery<NotificationRecordVO> tq = em.createQuery(hql.toString(), NotificationRecordVO.class);
        for (Map.Entry<String, Object> e : condition.entrySet()) {
            tq.setParameter(e.getKey(), e.getValue());
        }
        tq.setFirstResult(pageable.getPageNumber() * pageable.getPageSize());
        tq.setMaxResults(pageable.getPageSize());

        page.setData(tq.getResultList());
        page.getPage().setTotalElements(count);
        page.getPage().setTotalPages(PageUtil.cacalatePagesize(count, page.getPage().getPageSize()));
        return page;
    }
}
