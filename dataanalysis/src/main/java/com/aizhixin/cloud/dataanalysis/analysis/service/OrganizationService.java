package com.aizhixin.cloud.dataanalysis.analysis.service;

import com.aizhixin.cloud.dataanalysis.analysis.dto.OrganizationDTO;
import org.apache.commons.lang.StringUtils;
import org.hibernate.SQLQuery;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.*;

/**
 * @author: Created by jianwei.wu
 * @E-mail: wujianwei@aizhixin.com
 * @Date: 2018-05-06
 */
@Service
public class OrganizationService {

    @Autowired
    private EntityManager em;

    public Map<String,Object> getCollege(Long orgId){
        Map<String,Object> result = new HashMap<>();
        try {
            List<OrganizationDTO> orgList = getCollegeList(orgId, null);
            result.put("success", true);
            result.put("data", orgList);
            return result;
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", "获取学院列表失败！");
            return result;
        }
    }

    private String setToString(Set<String> codes) {
        StringBuilder sb = new StringBuilder();
        boolean first = true;
        for (String c : codes) {
            if(first) {
                first = false;
            } else {
                sb.append(",");
            }
            sb.append("'").append(c).append("'");
        }
        return sb.toString();
    }

    public List<OrganizationDTO> getCollegeList(Long orgId, Set<String> codes) {
        List<OrganizationDTO> orgList = new ArrayList<>();
        StringBuilder sql = new StringBuilder("SELECT  COMPANY_NUMBER as code, COMPANY_NAME as name, SIMPLE_NAME as simple FROM t_department WHERE 1=1");
        if (null != orgId) {
            sql.append(" AND ORG_ID = " + orgId + "");
        }
        if (null != codes && !codes.isEmpty()) {
            sql.append(" and COMPANY_NUMBER in (" + setToString(codes) + ") ");
        }
        if (null == codes || codes.isEmpty()) {
            sql.append(" AND COMPANY_NAME LIKE '%学院%'");
        }
        Query sq = em.createNativeQuery(sql.toString());
        sq.unwrap(SQLQuery.class).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
        List<Object> res = sq.getResultList();
        for (Object obj : res) {
            Map row = (Map) obj;
            OrganizationDTO org = new OrganizationDTO();
            if (null != row.get("code")) {
                org.setCode(row.get("code").toString());
                if (null != row.get("name")) {
                    org.setName(row.get("name").toString());
                }
                if (null != row.get("simple")) {
                    org.setSimple(row.get("simple").toString());
                }
                if (null != org.getSimple() && org.getSimple().length() > 0) {
                    org.setName(org.getSimple());
                }
                orgList.add(org);
            }
        }
        return orgList;
    }

    public Map<String,Object> getProfession(Long orgId,String code){
        Map<String,Object> result = new HashMap<>();
        try {
            List<OrganizationDTO> orgList = getProfessionList(orgId, code, null);
            result.put("success", true);
            result.put("data", orgList);
            return result;
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", "获取专业列表失败！");
            return result;
        }
    }


    public List<OrganizationDTO> getProfessionList(Long orgId, String code, Set<String> codes) {
        List<OrganizationDTO> orgList = new ArrayList<>();
        Map<String, Object> condition = new HashMap<>();
        StringBuilder sql = new StringBuilder("SELECT  CODE as code, NAME as name FROM t_profession WHERE 1=1");
        if (null != orgId) {
            sql.append(" and ORG_ID = :orgId");
            condition.put("orgId", orgId);
        }
        if (!StringUtils.isBlank(code)) {
            sql.append(" and COMPANY_NUMBER = :code");
            condition.put("code", code);
        }

        if (null != codes && !codes.isEmpty()) {
            sql.append(" and CODE in (" + setToString(codes) + ") ");
        }
        Query sq = em.createNativeQuery(sql.toString());
        sq.unwrap(SQLQuery.class).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
        for (Map.Entry<String, Object> e : condition.entrySet()) {
            sq.setParameter(e.getKey(), e.getValue());
        }
        List<Object> res = sq.getResultList();
        for (Object obj : res) {
            Map row = (Map) obj;
            OrganizationDTO org = new OrganizationDTO();
            if (null != row.get("code")) {
                org.setCode(row.get("code").toString());
                if (null != row.get("name")) {
                    org.setName(row.get("name").toString());
                }
                orgList.add(org);
            }
        }
        return orgList;
    }

    public Map<String,Object> getClass(Long orgId, String ccode, String pcode){
        Map<String,Object> result = new HashMap<>();
        Map<String, Object> condition = new HashMap<>();
        List<OrganizationDTO> orgList = new ArrayList<>();
        try {
            orgList = getClassList (orgId, ccode, pcode, null);
            result.put("success", true);
            result.put("data", orgList);
            return result;
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", "获取班级列表失败！");
            return result;
        }
    }


    public List<OrganizationDTO> getClassList(Long orgId, String ccode, String pcode, Set<String> codes) {
        Map<String, Object> condition = new HashMap<>();
        List<OrganizationDTO> orgList = new ArrayList<>();
        StringBuilder sql = new StringBuilder("SELECT  CLASS_NUMBER as code, NAME as name FROM t_class WHERE 1=1");
        if (null != orgId) {
            sql.append(" and ORG_ID = :orgId");
            condition.put("orgId", orgId);
        }
        if (!StringUtils.isBlank(ccode)) {
            sql.append(" and COLLEGE_CODE = :ccode");
            condition.put("ccode", ccode);
        }
        if (!StringUtils.isBlank(pcode)) {
            sql.append(" and PROFESSION_CODE = :pcode");
            condition.put("pcode", pcode);
        }

        if (null != codes && !codes.isEmpty()) {
            sql.append(" and NAME in (" + setToString(codes) + ") ");
        }
        Query sq = em.createNativeQuery(sql.toString());
        sq.unwrap(SQLQuery.class).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
        for (Map.Entry<String, Object> e : condition.entrySet()) {
            sq.setParameter(e.getKey(), e.getValue());
        }
        List<Object> res = sq.getResultList();
        for (Object obj : res) {
            Map row = (Map) obj;
            OrganizationDTO org = new OrganizationDTO();
            if (null != row.get("code")) {
                org.setCode(row.get("code").toString());
                if (null != row.get("name")) {
                    org.setName(row.get("name").toString());
                }
                orgList.add(org);
            }
        }
        return orgList;
    }

    public Map<String,Object> getGrade(Long orgId){
        Map<String,Object> result = new HashMap<>();
        Map<String, Object> condition = new HashMap<>();
        List<OrganizationDTO> orgList = new ArrayList<>();
        try {
            StringBuilder sql = new StringBuilder("SELECT GRADE as name FROM t_student_status WHERE 1=1");
            if (null != orgId) {
                sql.append(" and ORG_ID = :orgId");
                condition.put("orgId", orgId);
            }
            sql.append(" GROUP BY GRADE");
            Query sq = em.createNativeQuery(sql.toString());
            sq.unwrap(SQLQuery.class).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
            for (Map.Entry<String, Object> e : condition.entrySet()) {
                sq.setParameter(e.getKey(), e.getValue());
            }
            List<Object> res = sq.getResultList();
            for (Object obj : res) {
                Map row = (Map) obj;
                OrganizationDTO org = new OrganizationDTO();
                if (null != row.get("name")) {
                    org.setCode(row.get("name").toString());
                    orgList.add(org);
                }
            }
            result.put("success", true);
            result.put("data", orgList);
            return result;
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", "获取年级列表失败！");
            return result;
        }
    }








}
