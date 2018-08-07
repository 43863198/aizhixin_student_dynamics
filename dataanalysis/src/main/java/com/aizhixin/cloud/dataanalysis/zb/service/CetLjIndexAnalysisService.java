package com.aizhixin.cloud.dataanalysis.zb.service;

import com.aizhixin.cloud.dataanalysis.zb.dto.AnalysisBasezbDTO;
import com.aizhixin.cloud.dataanalysis.zb.manager.AnalysisIndexManager;
import com.aizhixin.cloud.dataanalysis.zb.manager.CetLjIndexAnalysisManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 英语等级指标分析计算逻辑
 */
@Component
@Slf4j
public class CetLjIndexAnalysisService {
    @Autowired
    private AnalysisIndexManager analysisIndexManager;
    @Autowired
    private CetLjIndexAnalysisManager cetLjIndexAnalysisManager;

    @Async
    public void schoolBaseIndex(Long orgId) {
        List<AnalysisBasezbDTO> cache = new ArrayList<>();
        cetLjIndexAnalysisManager.deleteHistory(CetLjIndexAnalysisManager.SQL_DELETE_NEW_SHOOL_LJ, orgId.toString());
        List<AnalysisBasezbDTO> list = cetLjIndexAnalysisManager.queryLjJczb(CetLjIndexAnalysisManager.SQL_LJ_SCHOOL, orgId, new Date());
        for (AnalysisBasezbDTO d : list) {
            System.out.println(d.toString());
        }
        //校历
//        Map<String, Long> yearAllRs = new HashMap<>();
//        List<SchoolCalendarDTO> schoolCalendarList = analysisIndexManager.querySchoolCalendar(AnalysisIndexManager.SQL_SCHOOL_CALENDAR, orgId);
//        /*********************************************************************累计************************************************************************************/
//        //总人数
//        for (SchoolCalendarDTO c : schoolCalendarList) {
////            List<AnalysisBasezbDTO>
//            list = analysisIndexManager.queryLjJczb(AnalysisIndexManager.SQL_LJ_SCHOOL, orgId, c.getJsrq());//总指标
//            for (AnalysisBasezbDTO d : list) {
//                d.setXxdm(orgId.toString());
//                d.setDhlj("2");
//                Long zxrs = yearAllRs.get(d.getXn() + "-" + d.getXq());
//                if (null != zxrs) {
//                    d.setZxrs(zxrs);
//                } else {
//                    System.out.println("-------------------------No rs:" + d.toString());
//                }
//                cache.add(d);
//            }
//        }
//        //学院
//        ljIndexAndZxrs(cache, schoolCalendarList, AnalysisIndexManager.SQL_LJ_COLLEGE, AnalysisIndexManager.SQL_COLLEGE_RS, orgId);
//        //专业
//        ljIndexAndZxrs(cache, schoolCalendarList, AnalysisIndexManager.SQL_LJ_PROFESSIONAL, AnalysisIndexManager.SQL_PROFESSIONAL_RS, orgId);
//        //班级
//        ljIndexAndZxrs(cache, schoolCalendarList, AnalysisIndexManager.SQL_LJ_CLASSES, AnalysisIndexManager.SQL_CLASSES_RS, orgId);
//
//        log.info("start save data.count:{}", cache.size());
//        System.out.println("---------------------------------------------------------------------------------------------------");
//        analysisIndexManager.saveJczb(cache);
    }

//    private void indexAndZxrs(List<AnalysisBasezbDTO> cache, List<SchoolCalendarDTO> schoolCalendarList, String sbSQL, String rsSQL, Long orgId) {
//        Map<String, Long> rsMap = new HashMap<>();
//        for (SchoolCalendarDTO c : schoolCalendarList) {
//            List<ZxrsDTO> rsList = analysisIndexManager.querySubZxrs(rsSQL, orgId, c.getJsrq());//人数
//            for (ZxrsDTO rs : rsList) {
//                rsMap.put(c.getXn() + "-" + c.getXq() + "-" + rs.getBh(), rs.getZxrs());
//            }
//        }
//        List<AnalysisBasezbDTO> list = analysisIndexManager.queryDcJczb(sbSQL, orgId);//基础指标
//        for (AnalysisBasezbDTO d : list) {
//            d.setXxdm(orgId.toString());
//            d.setDhlj("1");
//            Long zxrs = rsMap.get(d.getXn() + "-" + d.getXq() + "-" + d.getBh());
//            if (null != zxrs) {
//                d.setZxrs(zxrs);
//            } else {
//                System.out.println("---------------------No rs:" + d.toString());
//            }
//            cache.add(d);
//        }
//    }
//
//    private void ljIndexAndZxrs(List<AnalysisBasezbDTO> cache, List<SchoolCalendarDTO> schoolCalendarList, String sbSQL, String rsSQL, Long orgId) {
//        Map<String, Long> rsMap = new HashMap<>();
//        for (SchoolCalendarDTO c : schoolCalendarList) {
//            List<ZxrsDTO> rsList = analysisIndexManager.querySubZxrs(rsSQL, orgId, c.getJsrq());//人数
//            for (ZxrsDTO rs : rsList) {
//                rsMap.put(c.getXn() + "-" + c.getXq() + "-" + rs.getBh(), rs.getZxrs());
//            }
//        }
//        for (SchoolCalendarDTO c : schoolCalendarList) {
//            List<AnalysisBasezbDTO> list = analysisIndexManager.queryLjJczb(sbSQL, orgId, c.getJsrq());//基础指标
//            for (AnalysisBasezbDTO d : list) {
//                d.setXxdm(orgId.toString());
//                d.setDhlj("2");
//                Long zxrs = rsMap.get(d.getXn() + "-" + d.getXq() + "-" + d.getBh());
//                if (null != zxrs) {
//                    d.setZxrs(zxrs);
//                } else {
//                    System.out.println("---------------------No rs:" + d.toString());
//                }
//                cache.add(d);
//            }
//        }
//    }
}
