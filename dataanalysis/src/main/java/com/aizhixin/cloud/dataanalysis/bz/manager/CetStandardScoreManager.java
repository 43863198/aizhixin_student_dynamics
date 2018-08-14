package com.aizhixin.cloud.dataanalysis.bz.manager;

import com.aizhixin.cloud.dataanalysis.bz.entity.CetStandardScore;
import com.aizhixin.cloud.dataanalysis.bz.repository.CetStandardScoreRepository;
import com.aizhixin.cloud.dataanalysis.bz.vo.CetTopVo;
import org.hibernate.SQLQuery;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@Transactional
public class CetStandardScoreManager {
    @Autowired
    private CetStandardScoreRepository cetStandardScoreRepository;

    @Autowired
    private EntityManager em;

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
        return new ArrayList<CetStandardScore>();
    }

    public Map<String, Object> getTop(Long orgId, String cetType, String teacherYear, String semester) {
        Map<String,Object> resultMap = new HashMap<>();
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

            cetTopVo.setName(map.get("simple_name") != null?map.get("simple_name").toString():"");
            cetTopVo.setCollegeName(map.get("name") != null?map.get("name").toString():"");

            cetTopVo.setProfessionName(getProfessionName(temp.getZyh()));
            cetTopVo.setClassName(temp.getBh());
            cetTopVo.setGrade(temp.getNj());
            cetTopVo.setMaxScore(temp.getCj().intValue() + "");

            listVo.add(cetTopVo);
        }
        resultMap.put("data",listVo);
        return resultMap;
    }

    public Map getCollegeName(String collegeCode) {
        StringBuilder sql = new StringBuilder("select COMPANY_NAME as name,SIMPLE_NAME as simple_name from t_department where COMPANY_NUMBER = :collegeCode");
        Query query = em.createNativeQuery(sql.toString());
        query.unwrap(SQLQuery.class).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
        query.setParameter("collegeCode", collegeCode);
        Map map = (Map) query.getSingleResult();
        return  map;
    }

    public String getProfessionName(String professionCode){

        StringBuilder sql =new StringBuilder("select NAME as name from t_profession where CODE = :professionCode") ;
        Query query = em.createNativeQuery(sql.toString());
        query.unwrap(SQLQuery.class).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
        query.setParameter("professionCode", professionCode);
        Map map = (Map) query.getSingleResult();
        return  map.get("name") != null ? map.get("name").toString():"";
    }
}
