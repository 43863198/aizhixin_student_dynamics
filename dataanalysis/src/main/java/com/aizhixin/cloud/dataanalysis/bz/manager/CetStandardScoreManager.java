package com.aizhixin.cloud.dataanalysis.bz.manager;

import com.aizhixin.cloud.dataanalysis.analysis.vo.CetDetailVO;
import com.aizhixin.cloud.dataanalysis.bz.entity.CetStandardScore;
import com.aizhixin.cloud.dataanalysis.bz.repository.CetStandardScoreRepository;
import com.aizhixin.cloud.dataanalysis.bz.vo.CetTopVo;
import com.aizhixin.cloud.dataanalysis.common.PageData;
import com.aizhixin.cloud.dataanalysis.common.core.PageUtil;
import org.apache.commons.lang.StringUtils;
import org.hibernate.SQLQuery;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.*;

@Component
@Transactional
public class CetStandardScoreManager {
    @Autowired
    private CetStandardScoreRepository cetStandardScoreRepository;

    @Autowired
    private EntityManager em;
    @Autowired
    private JdbcTemplate jdbcTemplate;

    public void save(List<CetStandardScore> entitys) {
        cetStandardScoreRepository.save(entitys);
    }

    public List<CetStandardScore> getCetScore(Long orgId, String jobNum, Integer pageNumber, Integer pageSize) {
        Pageable pageable = new PageRequest(pageNumber - 1, pageSize);
        CetStandardScore cetStandardScore = new CetStandardScore();
        cetStandardScore.setXh(jobNum);
        cetStandardScore.setXxdm(orgId.toString());
        Example<CetStandardScore> example = Example.of(cetStandardScore);
        List<CetStandardScore> list = cetStandardScoreRepository.findAll(example, pageable).getContent();
        if (null != list && list.size() > 0) {
            return list;
        }
        return new ArrayList<>();
    }

    public List<CetTopVo> getTop(Long orgId, String cetType, String teacherYear,String semester) {
        List<CetTopVo> listVo = new ArrayList<>();
        Pageable pageable = new PageRequest(0, 10, Sort.Direction.DESC, "cj");
        CetStandardScore cetStandardScore = new CetStandardScore();
        cetStandardScore.setXxdm(orgId.toString());
        cetStandardScore.setXn(teacherYear);
        cetStandardScore.setXqm(semester);
        cetStandardScore.setKslx(cetType);
        Example<CetStandardScore> example = Example.of(cetStandardScore);
        Page<CetStandardScore> page = cetStandardScoreRepository.findAll(example, pageable);
        List<CetStandardScore> list = page.getContent();
        for (CetStandardScore temp : list) {
            CetTopVo cetTopVo = new CetTopVo();
            cetTopVo.setJobNumber(temp.getXh());

            Map map = getCollegeName(temp.getYxsh());

            cetTopVo.setName(map.get("simple_name") != null ? map.get("simple_name").toString() : "");
            cetTopVo.setCollegeName(map.get("name") != null ? map.get("name").toString() : "");

            cetTopVo.setProfessionName(getProfessionName(temp.getZyh()));
            cetTopVo.setClassName(temp.getBh());
            cetTopVo.setGrade(temp.getNj());
            cetTopVo.setMaxScore(temp.getCj().intValue() + "");

            listVo.add(cetTopVo);
        }
        return listVo;
    }


    public Map getCollegeName(String collegeCode) {
        StringBuilder sql = new StringBuilder("select COMPANY_NAME as name,SIMPLE_NAME as simple_name from t_department where COMPANY_NUMBER = :collegeCode");
        Query query = em.createNativeQuery(sql.toString());
        query.unwrap(SQLQuery.class).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
        query.setParameter("collegeCode", collegeCode);
        Map map = (Map) query.getSingleResult();
        return map;
    }

    public String getProfessionName(String professionCode) {

        StringBuilder sql = new StringBuilder("select NAME as name from t_profession where CODE = :professionCode");
        Query query = em.createNativeQuery(sql.toString());
        query.unwrap(SQLQuery.class).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
        query.setParameter("professionCode", professionCode);
        List list =  query.getResultList();
        if(null != list && list.size() > 0){
            Map map = (Map)list.get(0);
            return map.get("name") != null ? map.get("name").toString() : "";
        }else {
            return "";
        }
    }

    public PageData<CetDetailVO> getDetailList(Long orgId, String cetType, String collegeCode, String professionCode, String className, Integer scoreSeg, Integer pageNumber, Integer pageSize) {
       PageData<CetDetailVO> p = new PageData<>();
       Map<String, Object> condition = new HashMap<>();
        StringBuilder sql = new StringBuilder("select c.XH,c.XM,c.BH,c.ZYH,c.YXSH,c.NJ,max(c.CJ) as CJ from t_b_djksxx c left join t_xsjbxx s ON c.XH=s.XH WHERE ");
        StringBuilder cql = new StringBuilder("select COUNT(DISTINCT c.xh) as count from t_b_djksxx c left join t_xsjbxx s ON c.XH=s.XH WHERE ");
        Calendar c = Calendar.getInstance();
        int month = c.get(Calendar.MONTH) + 1;
        int year = c.get(Calendar.YEAR);
        sql.append(" s.RXNY <='").append(year);
        if(month < 10) {
            sql.append("0");
        }
        sql.append(month).append("' AND s.YBYNY >= :currdate");

        cql.append(" s.RXNY <='").append(year);
        if(month < 10) {
            cql.append("0");
        }
        cql.append(month).append("' AND s.YBYNY >= :currdate");
        condition.put("currdate", new Date());
        if (null != orgId) {
            sql.append(" and c.XXDM = :orgId");
            cql.append(" and c.XXDM = :orgId");
            sql.append(" and s.XXID = :orgId");
            cql.append(" and s.XXID = :orgId");
            condition.put("orgId", orgId);
        }
        if (!StringUtils.isBlank(cetType)) {
            sql.append(" and c.KSLX = :cetType");
            cql.append(" and c.KSLX = :cetType");
            condition.put("cetType", cetType);
        }
        if (!StringUtils.isBlank(collegeCode)) {
            sql.append(" and c.YXSH = :collegeCode");
            cql.append(" and c.YXSH = :collegeCode");
            sql.append(" and s.YXSH = :collegeCode");
            cql.append(" and s.YXSH = :collegeCode");
            condition.put("collegeCode", collegeCode);
        }
        if (!StringUtils.isBlank(professionCode)) {
            sql.append(" and c.ZYH = :professionCode");
            cql.append(" and c.ZYH = :professionCode");
            sql.append(" and s.ZYH = :professionCode");
            cql.append(" and s.ZYH = :professionCode");
            condition.put("professionCode", professionCode);
        }
        if (!StringUtils.isBlank(className)) {
            sql.append(" and c.BH = :className");
            cql.append(" and c.BH = :className");
            sql.append(" and s.BJMC = :className");
            cql.append(" and s.BJMC = :className");
            condition.put("className", className);
        }
        if (!StringUtils.isBlank(cetType)) {
            if (!"3".equals(cetType)) {
                if (null != scoreSeg && scoreSeg >= 1 && scoreSeg <= 4) {
                    switch (scoreSeg) {
                        case 1:
                            sql.append(" and c.CJ < 390 and c.CJ > 0");
                            cql.append(" and c.CJ < 390 and c.CJ > 0");
                            break;
                        case 2:
                            sql.append(" and c.CJ < 425 and c.CJ >= 390");
                            cql.append(" and c.CJ < 425 and c.CJ >= 390");
                            break;
                        case 3:
                            sql.append(" and c.CJ <= 550 and c.CJ >= 425");
                            cql.append(" and c.CJ <= 550 and c.CJ >= 425");
                            break;
                        case 4:
                            sql.append(" and c.CJ > 550");
                            cql.append(" and c.CJ > 550");
                            break;
                        default:
                    }
                }
            } else {
                if (null != scoreSeg) {
                    if (1 == scoreSeg) {
                        sql.append(" and c.CJ < 60");
                        cql.append(" and c.CJ < 60");
                    } else if (2 == scoreSeg) {
                        sql.append(" and c.CJ >= 60");
                        cql.append(" and c.CJ >= 60");
                    }
                }
            }
        }

        sql.append(" group by XH");
        Query sq = em.createNativeQuery(sql.toString());
        Query csq = em.createNativeQuery(cql.toString());
        sq.unwrap(SQLQuery.class).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
        csq.unwrap(SQLQuery.class).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
        for (Map.Entry<String, Object> e : condition.entrySet()) {
            sq.setParameter(e.getKey(), e.getValue());
            csq.setParameter(e.getKey(), e.getValue());
        }
        Long count = Long.parseLong(((Map)csq.getSingleResult()).get("count").toString());

        if (null == pageNumber || pageNumber < 1) {
            pageNumber = 1;
        }
        if (null == pageSize) {
            pageSize = 20;
        }
        sq.setFirstResult((pageNumber - 1) * pageSize);
        sq.setMaxResults(pageSize);
        List<Object> res = sq.getResultList();
        List<CetDetailVO> sList = new ArrayList<>();
        for (Object obj : res) {
            Map row = (Map) obj;
            CetDetailVO s = new CetDetailVO();
            if (null != row.get("XH")) {
                s.setJobNumber(row.get("XH").toString());
            }
            if (null != row.get("XM")) {
                s.setName(row.get("XM").toString());
            }
            if (null != row.get("YXSH")) {
               Map map =  getCollegeName(row.get("YXSH").toString());
                s.setCollegeName(map.get("simple_name") != null ? map.get("simple_name").toString() : map.get("name").toString());
            }
            if (null != row.get("ZYH")) {
                s.setProfessionName(getProfessionName(row.get("ZYH").toString()));
            }
            if (null != row.get("BH")) {
                s.setClassName(row.get("BH").toString());
            }
            if (null != row.get("NJ")) {
                s.setGrade(row.get("NJ").toString());
            }
            if (null != row.get("CJ") && !row.get("CJ").equals("")) {
                if (Float.valueOf(row.get("CJ").toString()) > 0) {
                    s.setScore(Math.round(Float.valueOf(row.get("CJ").toString())) + "");
                } else {
                    s.setScore(0 + "");
                }
            }

            sList.add(s);
        }
        p.setData(sList);
        p.getPage().setPageNumber(pageNumber);
        p.getPage().setPageSize(pageSize);
        p.getPage().setTotalElements(count);
        p.getPage().setTotalPages(PageUtil.cacalatePagesize(count, p.getPage().getPageSize()));

        return p;
    }
}
