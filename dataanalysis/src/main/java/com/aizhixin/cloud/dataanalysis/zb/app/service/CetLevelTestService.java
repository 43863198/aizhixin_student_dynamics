package com.aizhixin.cloud.dataanalysis.zb.app.service;

import com.aizhixin.cloud.dataanalysis.zb.app.vo.EnglishLevelBigScreenVO;
import com.aizhixin.cloud.dataanalysis.zb.manager.IndexAnalysisAppManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * 英语等级考试指标处理逻辑
 */
@Component
public class CetLevelTestService {
    @Autowired
    private IndexAnalysisAppManager indexAnalysisAppManager;
//    @Autowired
//    private EntityManager em;

    public List<EnglishLevelBigScreenVO> getCetLevelBigScreenPass(Long orgId) {
        if (null == orgId || orgId <= 0) {
            return new ArrayList<>();
        }
        return indexAnalysisAppManager.getNewLevelTestBigScreenPassForOld(orgId);
    }

//    public Map<String, Object> cetSingleDataStatistics(Long orgId, String cetType, String teacherYear, String semester, String collegeCode, String professionCode, String className) {
//        if ("3".equals(cetType)) {
//            if (StringUtils.isBlank(collegeCode)) {
//                collegeCode = "215";
//            }
//        }
//        return indexAnalysisAppManager.cetSingleDataStatistics(orgId, cetType, teacherYear, semester, collegeCode, professionCode, className);
//    }
//
//    public Map<String, Object> cetSingleDataAvgScoure(Long orgId, String cetType, String teacherYear, String semester, String collegeCode, String professionCode, String className) {
//        if ("3".equals(cetType)) {
//            if (StringUtils.isBlank(collegeCode)) {
//                collegeCode = "215";
//            }
//        }
//        return indexAnalysisAppManager.cetSingleDataAvgScoure(orgId, cetType, teacherYear, semester, collegeCode, professionCode, className);
//    }
//
//    public Map<String, Object> cetSingleDataNumberOfPeople(Long orgId, String cetType, String teacherYear, String semester, String collegeCode, String professionCode, String className) {
//        if ("3".equals(cetType)) {
//            if (StringUtils.isBlank(collegeCode)) {
//                collegeCode = "215";
//            }
//        }
//        return indexAnalysisAppManager.cetSingleDataNumberOfPeople(orgId, cetType, teacherYear, semester, collegeCode, professionCode, className);
//    }
//
//
//
//    public Map<String, Object> currentStatistics(Long orgId, String cetType, String collegeCode, String professionCode, String className) {
//        if ("3".equals(cetType)) {
//            if (StringUtils.isBlank(collegeCode)) {
//                collegeCode = "215";
//            }
//        }
//        return indexAnalysisAppManager.currentStatistics(orgId, cetType, collegeCode, professionCode, className);
//    }
//
//    public Map<String, Object> organizationStatistics(Long orgId, String cetType, String collegeCode, String professionCode, String className) {
//        if ("3".equals(cetType)) {
//            if (StringUtils.isBlank(collegeCode)) {
//                collegeCode = "215";
//            }
//        }
//        return indexAnalysisAppManager.organizationStatistics(orgId, cetType, collegeCode, professionCode, className);
//    }
//
//    public Map<String, Object> OverYearsPassRate(Long orgId, String cetType, String collegeCode, String professionCode, String className) {
//        if ("3".equals(cetType)) {
//            if (StringUtils.isBlank(collegeCode)) {
//                collegeCode = "215";
//            }
//        }
//        return indexAnalysisAppManager.OverYearsPassRate(orgId, cetType, collegeCode, professionCode, className);
//    }
//
//    public PageData<CetDetailVO> getDetailList(Long orgId, String cetType, String collegeCode, String professionCode, String className, String nj, String isPass, Integer scoreSeg, Integer pageNumber, Integer pageSize) {
//        return indexAnalysisAppManager.getDetailList(orgId, cetType, collegeCode, professionCode, className, nj, isPass, scoreSeg, pageNumber, pageSize);
//    }
//
//    public Map<String, Object> avg(Long orgId, String cetType, String collegeCode, String professionCode, String className) {
//        if ("3".equals(cetType)) {
//            if (StringUtils.isBlank(collegeCode)) {
//                collegeCode = "215";
//            }
//        }
//        return indexAnalysisAppManager.avg(orgId, cetType, collegeCode, professionCode, className);
//    }
//
//    public Map<String, Object> OverYearsAvgScore(Long orgId, String cetType, String collegeCode, String professionCode, String className) {
//        return indexAnalysisAppManager.OverYearsAvgScore(orgId, cetType, collegeCode, professionCode, className);
//    }


}
