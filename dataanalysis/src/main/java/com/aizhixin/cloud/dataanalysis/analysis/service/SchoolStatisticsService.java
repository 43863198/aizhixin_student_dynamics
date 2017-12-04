package com.aizhixin.cloud.dataanalysis.analysis.service;

import com.aizhixin.cloud.dataanalysis.analysis.dto.SchoolProfileDTO;
import com.aizhixin.cloud.dataanalysis.analysis.constant.TrendType;
import com.aizhixin.cloud.dataanalysis.analysis.domain.NewStudentProfileDomain;
import com.aizhixin.cloud.dataanalysis.analysis.dto.NewStudentProfileDTO;
import com.aizhixin.cloud.dataanalysis.analysis.dto.TrendDTO;
import com.aizhixin.cloud.dataanalysis.analysis.dto.TrendTypeDTO;
import com.aizhixin.cloud.dataanalysis.analysis.entity.SchoolStatistics;
import com.aizhixin.cloud.dataanalysis.analysis.respository.SchoolStatisticsRespository;
import com.aizhixin.cloud.dataanalysis.common.PageData;
import com.aizhixin.cloud.dataanalysis.common.constant.DataValidity;
import com.aizhixin.cloud.dataanalysis.common.core.PageUtil;
import com.aizhixin.cloud.dataanalysis.common.util.ProportionUtil;
import liquibase.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author: Created by jianwei.wu
 * @E-mail: wujianwei@aizhixin.com
 * @Date: 2017-11-29
 */
@Component
@Transactional
public class SchoolStatisticsService {
    @Autowired
    private SchoolStatisticsRespository schoolStatisticsRespository;
    @Autowired
    private EntityManager em;

    public Map<String, Object> getStatisticNewstudents(Long orgId, String year, Pageable pageable) {
        Map<String, Object> result = new HashMap<>();
        NewStudentProfileDTO newStudentProfileDTO = new NewStudentProfileDTO();
        PageData<SchoolStatistics> p = new PageData<>();
        Map<String, Object> condition = new HashMap<>();
        Long count = 0L;
        long studentNumber = 0;
        long alreadyReport = 0;
        int alreadyPay = 0;
        int convenienceChannel = 0;
        Date time = new Date();
        try {
            StringBuilder sql = new StringBuilder("SELECT COUNT(1) AS count, SUM(ss.NEW_STUDENTS_COUNT) as sum, SUM(ss.ALREADY_REPORT) as rsum, SUM(ss.ALREADY_PAY) as psum, SUM(ss.CONVENIENCE_CHANNEL) as csum, ss.STATISTICAL_TIME FROM T_SCHOOL_STATISTICS ss WHERE 1 = 1");
            if (null != orgId) {
                sql.append(" and ss.ORG_ID = :orgId");
                condition.put("orgId", orgId);
            }
            if(!StringUtils.isEmpty(year)){
                sql.append(" and ss.TEACHER_YEAR = :teacherYear");
                condition.put("teacherYear", year);
            }
            Query sq = em.createNativeQuery(sql.toString());
            for (Map.Entry<String, Object> e : condition.entrySet()) {
                sq.setParameter(e.getKey(), e.getValue());
            }
            Object res = sq.getSingleResult();
            if (null != res) {
                Object[] d = (Object[]) res;
                if (null != d[0]) {
                    count = Long.valueOf(String.valueOf(d[0]));
                }
                if (null != d[1]) {
                    studentNumber = Integer.valueOf(String.valueOf(d[1]));
                }
                if (null != d[2]) {
                    alreadyReport = Integer.valueOf(String.valueOf(d[2]));
                }
                if (null != d[3]) {
                    alreadyPay = Integer.valueOf(String.valueOf(d[3]));
                }
                if (null != d[4]) {
                    convenienceChannel = Integer.valueOf(String.valueOf(d[4]));
                }
                if(null!=d[5]){
                    time = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(String.valueOf(d[5]));
                }
            }
            newStudentProfileDTO.setStudentNumber(studentNumber);
            newStudentProfileDTO.setAlreadyReport(alreadyReport);
            newStudentProfileDTO.setUnreported(studentNumber - alreadyReport);
            newStudentProfileDTO.setProportion(ProportionUtil.accuracy(alreadyReport * 1.0, studentNumber * 1.0, 2));
            newStudentProfileDTO.setAlreadyPay(alreadyPay);
            newStudentProfileDTO.setConvenienceChannel(convenienceChannel);
            newStudentProfileDTO.setStatisticalTime(time);//后续要改为统计时间
            Page<SchoolStatistics> schoolStatisticsPage = schoolStatisticsRespository.findPageDataByOrgIdAndTeacherYear(pageable, orgId, year, DataValidity.VALID.getState());
            p.setData(schoolStatisticsPage.getContent());
            p.getPage().setTotalElements(count);
            p.getPage().setPageNumber(pageable.getPageNumber());
            p.getPage().setTotalPages(schoolStatisticsPage.getTotalPages());
            p.getPage().setPageSize(pageable.getPageSize());
            newStudentProfileDTO.setSchoolStatisticsPageData(p);
        } catch (Exception e) {
            e.printStackTrace();
            result.put("success", false);
            result.put("message", "获取学校新生统计信息异常！");
        }
        result.put("success", true);
        result.put("data",newStudentProfileDTO);
        return result;
    }

    public Map<String, Object> getTrendType() {
        Map<String, Object> result = new HashMap<>();
        List<TrendTypeDTO> trendTypeDTOList = new ArrayList<>();
        try{
            for(TrendType type : TrendType.values()) {
                TrendTypeDTO trendTypeDTO = new TrendTypeDTO();
                trendTypeDTO.setIndex(type.getIndex());
                trendTypeDTO.setTyep(type.toString());
                trendTypeDTO.setTypeName(type.getName());
                trendTypeDTOList.add(trendTypeDTO);
            }
        }catch (Exception e){
            e.printStackTrace();
            result.put("success", false);
            result.put("message","获取指标类型失败！");
            return result;
        }
        result.put("success",true);
        result.put("data",trendTypeDTOList);
        return result;
    }

    public SchoolProfileDTO getSchoolPersonStatistics(Long orgId) {
        return schoolStatisticsRespository.getSchoolPersonStatistics(orgId);
    }
    public NewStudentProfileDTO getNewStudentStatistics(Long orgId){
        return schoolStatisticsRespository.getNewStudentStatistics(orgId);
    }
    public Map<String, Object> getTrend(Long orgId, Long colloegeId, int typeIndex) {
        Map<String, Object> result = new HashMap<>();
        List<TrendDTO> trendDTOList = new ArrayList<>();
        Map<String, Object> condition = new HashMap<>();
        try {
            String trend = TrendType.getType(typeIndex);
            if(null!=trend) {
                if (trend.equals("NEW_STUDENTS_COUNT") || trend.equals("ALREADY_REPORT") || trend.equals("ALREADY_PAY") || trend.equals("CONVENIENCE_CHANNEL")) {
                    StringBuilder sql = new StringBuilder("SELECT TEACHER_YEAR, SUM(" + trend + ") FROM T_SCHOOL_STATISTICS  WHERE 1 = 1");
                    if (null != orgId) {
                        sql.append(" and ORG_ID = :orgId");
                        condition.put("orgId", orgId);
                    }
                    if (null != colloegeId) {
                        sql.append(" and COLLOEGE_ID = :colloegeId");
                        condition.put("colloegeId", colloegeId);
                    }
                    sql.append(" GROUP BY TEACHER_YEAR");
                    Query sq = em.createNativeQuery(sql.toString());
                    for (Map.Entry<String, Object> e : condition.entrySet()) {
                        sq.setParameter(e.getKey(), e.getValue());
                    }
                    List<Object> res = sq.getResultList();
                    if (null != res && res.size() > 0) {
                        for (Object obj : res) {
                            Object[] d = (Object[]) obj;
                            TrendDTO trendDTO = new TrendDTO();
                            if (null != d[0]) {
                                trendDTO.setYear(String.valueOf(d[0]));
                            }
                            if (null != d[1]) {
                                trendDTO.setValue(String.valueOf(d[1]));
                            }
                            trendDTOList.add(trendDTO);
                        }
                    }
                } else if (trend.equals("UNREPORT")) {
                    StringBuilder sql = new StringBuilder("SELECT TEACHER_YEAR, SUM(NEW_STUDENTS_COUNT), SUM(ALREADY_REPORT) FROM T_SCHOOL_STATISTICS WHERE 1 = 1");
                    if (null != orgId) {
                        sql.append(" and ORG_ID = :orgId");
                        condition.put("orgId", orgId);
                    }
                    if (null != colloegeId) {
                        sql.append(" and COLLOEGE_ID = :colloegeId");
                        condition.put("colloegeId", colloegeId);
                    }
                    sql.append(" GROUP BY TEACHER_YEAR");
                    Query sq = em.createNativeQuery(sql.toString());
                    for (Map.Entry<String, Object> e : condition.entrySet()) {
                        sq.setParameter(e.getKey(), e.getValue());
                    }
                    List<Object> res = sq.getResultList();
                    if (null != res && res.size() > 0) {
                        for (Object obj : res) {
                            Object[] d = (Object[]) obj;
                            TrendDTO trendDTO = new TrendDTO();
                            if (null != d[0]) {
                                trendDTO.setYear(String.valueOf(d[0]));
                            }
                            if (null != d[1] && null != d[2]) {
                                trendDTO.setValue((Integer.valueOf(String.valueOf(d[1])) - Integer.valueOf(String.valueOf(d[2]))) + "");
                            }
                            trendDTOList.add(trendDTO);
                        }
                    }
                } else if (trend.equals("PAY_PROPORTION")) {
                    StringBuilder sql = new StringBuilder("SELECT TEACHER_YEAR, SUM(NEW_STUDENTS_COUNT), SUM(ALREADY_PAY) FROM T_SCHOOL_STATISTICS WHERE 1 = 1");
                    if (null != orgId) {
                        sql.append(" and ORG_ID = :orgId");
                        condition.put("orgId", orgId);
                    }
                    if (null != colloegeId) {
                        sql.append(" and COLLOEGE_ID = :colloegeId");
                        condition.put("colloegeId", colloegeId);
                    }
                    sql.append(" GROUP BY TEACHER_YEAR");
                    Query sq = em.createNativeQuery(sql.toString());
                    for (Map.Entry<String, Object> e : condition.entrySet()) {
                        sq.setParameter(e.getKey(), e.getValue());
                    }
                    List<Object> res = sq.getResultList();
                    if (null != res && res.size() > 0) {
                        for (Object obj : res) {
                            Object[] d = (Object[]) obj;
                            TrendDTO trendDTO = new TrendDTO();
                            if (null != d[0]) {
                                trendDTO.setYear(String.valueOf(d[0]));
                            }
                            if (null != d[1] && null != d[2]) {
                                trendDTO.setValue(ProportionUtil.accuracy(Double.valueOf(String.valueOf(d[2])), Double.valueOf(String.valueOf(d[1])),2));
                            }
                            trendDTOList.add(trendDTO);
                        }
                    }
                } else if (trend.equals("REPORT_PROPORTION")) {
                    StringBuilder sql = new StringBuilder("SELECT TEACHER_YEAR, SUM(NEW_STUDENTS_COUNT), SUM(ALREADY_REPORT) FROM T_SCHOOL_STATISTICS WHERE 1 = 1");
                    if (null != orgId) {
                        sql.append(" and ORG_ID = :orgId");
                        condition.put("orgId", orgId);
                    }
                    if (null != colloegeId) {
                        sql.append(" and COLLOEGE_ID = :colloegeId");
                        condition.put("colloegeId", colloegeId);
                    }
                    sql.append(" GROUP BY TEACHER_YEAR");
                    Query sq = em.createNativeQuery(sql.toString());
                    for (Map.Entry<String, Object> e : condition.entrySet()) {
                        sq.setParameter(e.getKey(), e.getValue());
                    }
                    List<Object> res = sq.getResultList();
                    if (null != res && res.size() > 0) {
                        for (Object obj : res) {
                            Object[] d = (Object[]) obj;
                            TrendDTO trendDTO = new TrendDTO();
                            if (null != d[0]) {
                                trendDTO.setYear(String.valueOf(d[0]));
                            }
                            if (null != d[1] && null != d[2]) {
                                trendDTO.setValue(ProportionUtil.accuracy(Double.valueOf(String.valueOf(d[2])), Double.valueOf(String.valueOf(d[1])),2));
                            }
                            trendDTOList.add(trendDTO);
                        }
                    }
                } else if (trend.equals("CHANNEL_PROPORTION")) {
                    StringBuilder sql = new StringBuilder("SELECT TEACHER_YEAR, SUM(NEW_STUDENTS_COUNT), SUM(CONVENIENCE_CHANNEL) FROM T_SCHOOL_STATISTICS WHERE 1 = 1");
                    if (null != orgId) {
                        sql.append(" and ORG_ID = :orgId");
                        condition.put("orgId", orgId);
                    }
                    if (null != colloegeId) {
                        sql.append(" and COLLOEGE_ID = :colloegeId");
                        condition.put("colloegeId", colloegeId);
                    }
                    sql.append(" GROUP BY TEACHER_YEAR");
                    Query sq = em.createNativeQuery(sql.toString());
                    for (Map.Entry<String, Object> e : condition.entrySet()) {
                        sq.setParameter(e.getKey(), e.getValue());
                    }
                    List<Object> res = sq.getResultList();
                    if (null != res && res.size() > 0) {
                        for (Object obj : res) {
                            Object[] d = (Object[]) obj;
                            TrendDTO trendDTO = new TrendDTO();
                            if (null != d[0]) {
                                trendDTO.setYear(String.valueOf(d[0]));
                            }
                            if (null != d[1] && null != d[2]) {
                                trendDTO.setValue(ProportionUtil.accuracy(Double.valueOf(String.valueOf(d[2])), Double.valueOf(String.valueOf(d[1])),2));
                            }
                            trendDTOList.add(trendDTO);
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            result.put("success",false);
            result.put("message","获取分析数据异常！");
            return result;
        }
        result.put("success",true);
        result.put("data",trendDTOList);
        return result;
    }


}
