package com.aizhixin.cloud.dataanalysis.zb.app.mananger;

import com.aizhixin.cloud.dataanalysis.common.PageData;
import com.aizhixin.cloud.dataanalysis.common.core.PageUtil;
import com.aizhixin.cloud.dataanalysis.zb.app.entity.StudentSemesterScoreIndex;
import com.aizhixin.cloud.dataanalysis.zb.app.respository.StudentSemesterScoreIndexRespository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@Transactional
public class StudentSemesterScoreIndexManager {
    @Autowired
    private EntityManager em;
    @Autowired
    private StudentSemesterScoreIndexRespository studentSemesterScoreIndexRespository;

    public StudentSemesterScoreIndex save(StudentSemesterScoreIndex entity) {
        return studentSemesterScoreIndexRespository.save(entity);
    }

    public List<StudentSemesterScoreIndex> save(List<StudentSemesterScoreIndex> entityList) {
        return studentSemesterScoreIndexRespository.save(entityList);
    }

    @Transactional(readOnly = true)
    public Page<StudentSemesterScoreIndex> findByXxdmAndXnAndXqmAndYxsh(Pageable pageable, String xxdm, String xn, String xq, String yxsh) {
        return studentSemesterScoreIndexRespository.findByXxdmAndXnAndXqmAndYxsh(pageable, xxdm, xn, xq, yxsh);
    }

    @Transactional(readOnly = true)
    public Page<StudentSemesterScoreIndex> findByXxdmAndXnAndXqmAndZyh(Pageable pageable, String xxdm, String xn, String xq, String yxsh, String zyh) {
        return studentSemesterScoreIndexRespository.findByXxdmAndXnAndXqmAndYxshAndZyh(pageable, xxdm, xn, xq, yxsh, zyh);
    }

    @Transactional(readOnly = true)
    public PageData<StudentSemesterScoreIndex> querySemesterScoreIndex(Pageable pageable, String xxdm, String xn, String xq, String collegeCode, String professionalCode, String nj, String name) {
        PageData<StudentSemesterScoreIndex> pd = new PageData<>();
        pd.getPage().setPageNumber(pageable.getPageNumber());
        pd.getPage().setPageSize(pageable.getPageSize());
        pd.getPage().setPageNumber(0);
        pd.getPage().setPageSize(0);
        Map<String, Object> condition = new HashMap<>();
        condition.put("xxdm", xxdm);
        condition.put("xn", xn);
        condition.put("xq", xq);
        StringBuilder chql = new StringBuilder(
                "SELECT COUNT(c.id) FROM com.aizhixin.cloud.dataanalysis.zb.app.entity.StudentSemesterScoreIndex c WHERE c.xxdm = :xxdm AND c.xn = :xn AND c.xqm = :xq");
        StringBuilder hql = new StringBuilder(
                "SELECT c FROM com.aizhixin.cloud.dataanalysis.zb.app.entity.StudentSemesterScoreIndex c WHERE c.xxdm = :xxdm AND c.xn = :xn AND c.xqm = :xq");
        if (!StringUtils.isEmpty(collegeCode)) {
            hql.append(" AND c.yxsh = :yxsh");
            chql.append(" AND c.yxsh = :yxsh");
            condition.put("yxsh", collegeCode);
        }
        if (!StringUtils.isEmpty(professionalCode)) {
            hql.append(" AND c.zyh = :zyh");
            chql.append(" AND o.zyh = :zyh");
            condition.put("zyh", professionalCode);
        }
        if (!StringUtils.isEmpty(nj)) {
            hql.append(" AND c.nj = :nj");
            chql.append(" AND c.nj = :nj");
            condition.put("nj", nj);
        }
        if (!StringUtils.isEmpty(name)) {
            hql.append(" AND (c.xm like :name OR c.xh like :name)");
            chql.append(" AND (c.xm like :name OR c.xh like :name)");
            condition.put("name", "%" + name + "%");
        }
        hql.append(" order by c.id");

        Query q = em.createQuery(chql.toString());
        for (Map.Entry<String, Object> e : condition.entrySet()) {
            q.setParameter(e.getKey(), e.getValue());
        }
        Long count = (Long) q.getSingleResult();
        if (count <= 0) {
            return pd;
        }
        TypedQuery<StudentSemesterScoreIndex> tq = em.createQuery(hql.toString(), StudentSemesterScoreIndex.class);
        for (Map.Entry<String, Object> e : condition.entrySet()) {
            tq.setParameter(e.getKey(), e.getValue());
        }
        tq.setFirstResult(pageable.getPageNumber() * pageable.getPageSize());
        tq.setMaxResults(pageable.getPageSize());

        pd.setData(tq.getResultList());
        pd.getPage().setTotalElements(count);
        pd.getPage().setPageNumber(pageable.getPageNumber());
        pd.getPage().setPageSize(pageable.getPageSize());
        pd.getPage().setTotalPages(PageUtil.cacalatePagesize(count, pd.getPage().getPageSize()));
        return pd;
    }
}
