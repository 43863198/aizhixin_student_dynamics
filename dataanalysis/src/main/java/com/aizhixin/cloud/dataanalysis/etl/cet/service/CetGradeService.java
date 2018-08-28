package com.aizhixin.cloud.dataanalysis.etl.cet.service;

import com.aizhixin.cloud.dataanalysis.etl.cet.dto.NjAnalysiszbDTO;
import com.aizhixin.cloud.dataanalysis.etl.cet.dto.NjZxrsDTO;
import com.aizhixin.cloud.dataanalysis.etl.cet.dto.SchoolCalendarDTO;
import com.aizhixin.cloud.dataanalysis.etl.cet.manager.CetDcGradeManager;
import com.aizhixin.cloud.dataanalysis.etl.cet.manager.CetLjGradeManager;
import com.aizhixin.cloud.dataanalysis.etl.cet.manager.CetLjIndexAnalysisManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.*;

/**
 * 英语等级指标分析计算逻辑
 */
@Component
@Slf4j
public class CetGradeService {
    @Autowired
    private CetLjGradeManager cetLjGradeManager;
    @Autowired
    private CetLjIndexAnalysisManager cetLjIndexAnalysisManager;
    @Autowired
    private CetDcGradeManager cetDcGradeManager;

    @Async
    public void calLjNewest(Long orgId) {
        List<NjAnalysiszbDTO> cache = new ArrayList<>();
        cetLjGradeManager.deleteHistory(CetLjGradeManager.SQL_DELETE_LJ_NEWEST_NJZB, orgId.toString());

        Date current = new Date ();
        Map<String, NjZxrsDTO> rsMap = new HashMap<>();

        //全校
        rsMap.clear();
        noDateRs(rsMap, CetLjGradeManager.SQL_SCHOOL_RS, orgId, current);
        ljIndexAndZxrs(cache, CetLjGradeManager.SQL_LJ_NJ_SCHOOL, orgId, rsMap, current);
        //学院
        rsMap.clear();
        noDateRs(rsMap, CetLjGradeManager.SQL_COLLEGE_RS, orgId, current);
        ljIndexAndZxrs(cache, CetLjGradeManager.SQL_LJ_NJ_COLLEGE, orgId, rsMap, current);
        //专业
        rsMap.clear();
        noDateRs(rsMap, CetLjGradeManager.SQL_PROFESSIONAL_RS, orgId, current);
        ljIndexAndZxrs(cache, CetLjGradeManager.SQL_LJ_NJ_PROFESSIONAL, orgId, rsMap, current);
        //班级
        rsMap.clear();
        noDateRs(rsMap, CetLjGradeManager.SQL_CLASSES_RS, orgId, current);
        ljIndexAndZxrs(cache, CetLjGradeManager.SQL_LJ_NJ_CLASSES, orgId, rsMap, current);

        System.out.println("---------------------------------------------------------------------------------------------------");
        log.info("start save data.count:{}", cache.size());
        cetLjGradeManager.saveNjzb(cache);
    }

    private void noDateRs(Map<String, NjZxrsDTO> rsMap, String rsSql, Long orgId, Date current) {
        List<NjZxrsDTO> rsList = cetLjGradeManager.querySubZxrs(rsSql, orgId, current, current);
        for (NjZxrsDTO d : rsList) {
            String key = d.getBh() + "-" + d.getNj();
            NjZxrsDTO dd = rsMap.get(key);
            if (null == dd) {
                rsMap.put(key, d);
            }
        }
    }
    private void ljIndexAndZxrs(List<NjAnalysiszbDTO> cache, String sbSQL, Long orgId, Map<String, NjZxrsDTO> rsMap, Date current) {
        List<NjAnalysiszbDTO> list = cetLjGradeManager.queryNjLjzb(sbSQL, orgId, current, current);//基础指标
        for (NjAnalysiszbDTO d : list) {
            d.setXxdm(orgId.toString());
            d.setDhlj("2");
            NjZxrsDTO rs = rsMap.get(d.getBh() + "-" + d.getNj());
            if (null != rs) {
                d.setZxrs(rs.getZxrs());
            } else {
                System.out.println("---------------------No rs:" + d.toString());
            }
            cache.add(d);
        }
    }

    @Async
    public void calLjAll(Long orgId) {
        List<NjAnalysiszbDTO> cache = new ArrayList<>();
        cetLjGradeManager.deleteHistory(CetLjGradeManager.SQL_DELETE_LJ_NJZB, orgId.toString());

        List<SchoolCalendarDTO> calendarList = cetLjIndexAnalysisManager.queryCetXnxqAndDate(orgId.toString());
        Map<String, NjZxrsDTO> rsMap = new HashMap<>();

        //全校
        for(SchoolCalendarDTO c : calendarList) {
            zxrs(rsMap, CetLjGradeManager.SQL_SCHOOL_RS, orgId, c);
        }
        for(SchoolCalendarDTO c : calendarList) {
            ljIndexAndZxrs(cache, CetLjGradeManager.SQL_LJ_NJ_SCHOOL, orgId, rsMap, c);
        }
        log.info("Data is add school:{}", cache.size());
        //学院
        rsMap.clear();
        for(SchoolCalendarDTO c : calendarList) {
            zxrs(rsMap, CetLjGradeManager.SQL_COLLEGE_RS, orgId, c);
        }
        for(SchoolCalendarDTO c : calendarList) {
            ljIndexAndZxrs(cache, CetLjGradeManager.SQL_LJ_NJ_COLLEGE, orgId, rsMap, c);
        }
        log.info("Data is add college:{}", cache.size());
        //专业
        rsMap.clear();
        for(SchoolCalendarDTO c : calendarList) {
            zxrs(rsMap, CetLjGradeManager.SQL_PROFESSIONAL_RS, orgId, c);
        }
        for(SchoolCalendarDTO c : calendarList) {
            ljIndexAndZxrs(cache, CetLjGradeManager.SQL_LJ_NJ_PROFESSIONAL, orgId, rsMap, c);
        }
        log.info("Data is add professional:{}", cache.size());
        //班级
        rsMap.clear();
        for(SchoolCalendarDTO c : calendarList) {
            zxrs(rsMap, CetLjGradeManager.SQL_CLASSES_RS, orgId, c);
        }
        for(SchoolCalendarDTO c : calendarList) {
            ljIndexAndZxrs(cache, CetLjGradeManager.SQL_LJ_NJ_CLASSES, orgId, rsMap, c);
        }
        log.info("Data is add classes:{}", cache.size());

        System.out.println("---------------------------------------------------------------------------------------------------");
        log.info("start save data.count:{}", cache.size());
        cetLjGradeManager.saveNjzb(cache);
    }

    private void zxrs(Map<String, NjZxrsDTO> rsMap, String rsSql, Long orgId, SchoolCalendarDTO c) {
        List<NjZxrsDTO> rsList = cetLjGradeManager.querySubZxrs(rsSql, orgId, c.getKsrq(), c.getJsrq());
        for (NjZxrsDTO d : rsList) {
            String key = c.getXn() + "-" + c.getXq() + "-" + d.getBh() + "-" + d.getNj();
            NjZxrsDTO dd = rsMap.get(key);
            if (null == dd) {
                rsMap.put(key, d);
            }
        }
    }
    private void ljIndexAndZxrs(List<NjAnalysiszbDTO> cache, String sbSQL, Long orgId, Map<String, NjZxrsDTO> rsMap, SchoolCalendarDTO c) {
        List<NjAnalysiszbDTO> list = cetLjGradeManager.queryNjLjzb(sbSQL, orgId, c.getKsrq(), c.getJsrq());//基础指标
        for (NjAnalysiszbDTO d : list) {
            d.setXxdm(orgId.toString());
            d.setDhlj("2");
            d.setXn(c.getXn());
            d.setXq(c.getXq());
            NjZxrsDTO rs = rsMap.get(c.getXn() + "-" + c.getXq() + "-" + d.getBh() + "-" + d.getNj());
            if (null != rs) {
                d.setZxrs(rs.getZxrs());
            } else {
                System.out.println("---------------------No rs:" + d.toString());
            }
            cache.add(d);
        }
    }


    @Async
    public void calDcAll(Long orgId) {
        List<NjAnalysiszbDTO> cache = new ArrayList<>();
        cetLjGradeManager.deleteHistory(CetDcGradeManager.SQL_DELETE_DC_NJZB, orgId.toString());

        List<SchoolCalendarDTO> calendarList = cetLjIndexAnalysisManager.queryCetXnxqAndDate(orgId.toString());
        Map<String, NjZxrsDTO> rsMap = new HashMap<>();

        //全校
        for(SchoolCalendarDTO c : calendarList) {
            zxrs(rsMap, CetLjGradeManager.SQL_SCHOOL_RS, orgId, c);
        }
        dcIndexAndZxrs(cache, CetDcGradeManager.SQL_DC_NJ_SCHOOL, orgId.toString(), rsMap);
        log.info("Data is add school:{}", cache.size());
        //学院
        rsMap.clear();
        for(SchoolCalendarDTO c : calendarList) {
            zxrs(rsMap, CetLjGradeManager.SQL_COLLEGE_RS, orgId, c);
        }
        dcIndexAndZxrs(cache, CetDcGradeManager.SQL_DC_NJ_COLLEGE, orgId.toString(), rsMap);
        log.info("Data is add college:{}", cache.size());
        //专业
        rsMap.clear();
        for(SchoolCalendarDTO c : calendarList) {
            zxrs(rsMap, CetLjGradeManager.SQL_PROFESSIONAL_RS, orgId, c);
        }
        dcIndexAndZxrs(cache, CetDcGradeManager.SQL_DC_NJ_PROFESSIONAL, orgId.toString(), rsMap);
        log.info("Data is add professional:{}", cache.size());
        //班级
        rsMap.clear();
        for(SchoolCalendarDTO c : calendarList) {
            zxrs(rsMap, CetLjGradeManager.SQL_CLASSES_RS, orgId, c);
        }
        dcIndexAndZxrs(cache, CetDcGradeManager.SQL_DC_NJ_CLASSES, orgId.toString(), rsMap);
        log.info("Data is add classes:{}", cache.size());

        System.out.println("---------------------------------------------------------------------------------------------------");
        log.info("start save data.count:{}", cache.size());
        cetLjGradeManager.saveNjzb(cache);
    }

    private void dcIndexAndZxrs(List<NjAnalysiszbDTO> cache, String sbSQL, String xxdm, Map<String, NjZxrsDTO> rsMap) {
        List<NjAnalysiszbDTO> list = cetDcGradeManager.queryNjDczb(sbSQL, xxdm);//基础指标
        for (NjAnalysiszbDTO d : list) {
            d.setXxdm(xxdm);
            d.setDhlj("1");
            NjZxrsDTO rs = rsMap.get(d.getXn() + "-" + d.getXq() + "-" + d.getBh() + "-" + d.getNj());
            if (null != rs) {
                d.setZxrs(rs.getZxrs());
            } else {
                System.out.println("---------------------No rs:" + d.toString());
            }
            cache.add(d);
        }
    }
}
