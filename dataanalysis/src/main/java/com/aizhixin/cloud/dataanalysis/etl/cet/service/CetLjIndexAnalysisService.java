package com.aizhixin.cloud.dataanalysis.etl.cet.service;

import com.aizhixin.cloud.dataanalysis.etl.cet.dto.AnalysisBasezbDTO;
import com.aizhixin.cloud.dataanalysis.etl.cet.dto.SchoolCalendarDTO;
import com.aizhixin.cloud.dataanalysis.etl.cet.dto.ZxrsDTO;
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
public class CetLjIndexAnalysisService {
    @Autowired
    private CetLjIndexAnalysisManager cetLjIndexAnalysisManager;

    @Async
    public void calLjHaveTest(Long orgId) {
        List<AnalysisBasezbDTO> cache = new ArrayList<>();
        cetLjIndexAnalysisManager.deleteHistory(CetLjIndexAnalysisManager.SQL_DELETE_ALL_NOT_NEW_SHOOL_LJ, orgId.toString());//删除除了最新以外的数据

        List<SchoolCalendarDTO> schoolCalendarList = cetLjIndexAnalysisManager.queryCetXnxqAndDate(orgId.toString());//查询包含有等级考试数据的学年学期
        /*********************************************************************累计************************************************************************************/
        Map<String, ZxrsDTO> rsMap = new HashMap<>();
        //全校
        rsMap.clear();
        for (SchoolCalendarDTO  c : schoolCalendarList) {//查询在校人数
            cetLjIndexAnalysisManager.haveCalendarRs(rsMap, CetLjIndexAnalysisManager.SQL_SCHOOL_RS, orgId, c);
        }
        log.info("Find zxrs school data count:{}", rsMap.size());
        for (SchoolCalendarDTO  c : schoolCalendarList) {
            ljIndexAndZxrs(cache, c, CetLjIndexAnalysisManager.SQL_LJ_SCHOOL,  orgId, rsMap);
        }

        //学院
        rsMap.clear();
        for (SchoolCalendarDTO  c : schoolCalendarList) {//查询在校人数
            cetLjIndexAnalysisManager.haveCalendarRs(rsMap, CetLjIndexAnalysisManager.SQL_COLLEGE_RS, orgId, c);
        }
        log.info("Find zxrs college data count:{}", rsMap.size());
        for (SchoolCalendarDTO  c : schoolCalendarList) {
            ljIndexAndZxrs(cache, c, CetLjIndexAnalysisManager.SQL_LJ_COLLEGE,  orgId, rsMap);
        }

        //专业
        rsMap.clear();
        for (SchoolCalendarDTO  c : schoolCalendarList) {//查询在校人数
            cetLjIndexAnalysisManager.haveCalendarRs(rsMap, CetLjIndexAnalysisManager.SQL_PROFESSIONAL_RS, orgId, c);
        }
        log.info("Find zxrs professional data count:{}", rsMap.size());
        for (SchoolCalendarDTO  c : schoolCalendarList) {
            ljIndexAndZxrs(cache, c, CetLjIndexAnalysisManager.SQL_LJ_PROFESSIONAL, orgId, rsMap);
        }

        //班级
        rsMap.clear();
        for (SchoolCalendarDTO  c : schoolCalendarList) {//查询在校人数
            cetLjIndexAnalysisManager.haveCalendarRs(rsMap, CetLjIndexAnalysisManager.SQL_CLASSES_RS, orgId, c);
        }
        log.info("Find zxrs classes data count:{}", rsMap.size());
        for (SchoolCalendarDTO  c : schoolCalendarList) {
            ljIndexAndZxrs(cache, c, CetLjIndexAnalysisManager.SQL_LJ_CLASSES, orgId, rsMap);
        }

        System.out.println("---------------------------------------------------------------------------------------------------");
        log.info("start save data.count:{}", cache.size());
        cetLjIndexAnalysisManager.saveJczb(cache);
    }

    private void ljIndexAndZxrs(List<AnalysisBasezbDTO> cache, SchoolCalendarDTO c, String sbSQL, Long orgId, Map<String, ZxrsDTO> rsMap) {
        List<AnalysisBasezbDTO> list = cetLjIndexAnalysisManager.queryLjJczb(sbSQL, orgId, c.getKsrq(), c.getJsrq());//基础指标
        for (AnalysisBasezbDTO d : list) {
            d.setXxdm(orgId.toString());
            d.setDhlj("2");
            d.setXn(c.getXn());
            d.setXq(c.getXq());
            ZxrsDTO zxrs = rsMap.get(c.getXn() + "-" + c.getXq() + "-" + d.getBh());
            if (null != zxrs) {
                d.setZxrs(zxrs.getZxrs());
                d.setNzxrs(zxrs.getNzxrs());
                d.setVzxrs(zxrs.getVzxrs());
            } else {
                System.out.println("---------------------No rs:" + d.toString());
            }
            cache.add(d);
        }
    }


    @Async
    public void calLjNewest(Long orgId) {
        List<AnalysisBasezbDTO> cache = new ArrayList<>();
        cetLjIndexAnalysisManager.deleteHistory(CetLjIndexAnalysisManager.SQL_DELETE_ALL_NEW_SHOOL_LJ, orgId.toString());

        Date current = new Date ();
        Map<String, ZxrsDTO> rsMap = new HashMap<>();

        //全校
        noDateRs(rsMap, CetLjIndexAnalysisManager.SQL_SCHOOL_RS, orgId, current);
        ljIndexAndZxrs(cache, CetLjIndexAnalysisManager.SQL_LJ_SCHOOL, orgId, rsMap, current);
        //学院
        noDateRs(rsMap, CetLjIndexAnalysisManager.SQL_COLLEGE_RS, orgId, current);
        ljIndexAndZxrs(cache, CetLjIndexAnalysisManager.SQL_LJ_COLLEGE, orgId, rsMap, current);
        //专业
        noDateRs(rsMap, CetLjIndexAnalysisManager.SQL_PROFESSIONAL_RS, orgId, current);
        ljIndexAndZxrs(cache, CetLjIndexAnalysisManager.SQL_LJ_PROFESSIONAL, orgId, rsMap, current);
        //班级
        noDateRs(rsMap, CetLjIndexAnalysisManager.SQL_CLASSES_RS, orgId, current);
        ljIndexAndZxrs(cache, CetLjIndexAnalysisManager.SQL_LJ_CLASSES, orgId, rsMap, current);

        System.out.println("---------------------------------------------------------------------------------------------------");
        log.info("start save data.count:{}", cache.size());
        cetLjIndexAnalysisManager.saveJczb(cache);
    }

    private void noDateRs(Map<String, ZxrsDTO> rsMap, String rsSql, Long orgId, Date current) {
        rsMap.clear();
        Map<String, List<ZxrsDTO>> dwRsMap = new HashMap<>();
        List<ZxrsDTO> rsList = cetLjIndexAnalysisManager.querySubZxrs(rsSql, orgId, current, current);
        for (ZxrsDTO d : rsList) {
            List<ZxrsDTO> list = dwRsMap.get(d.getBh());
            if (null == list) {
                list = new ArrayList<>();
                dwRsMap.put(d.getBh(), list);
            }
            list.add(d);
        }
        for (Map.Entry<String, List<ZxrsDTO>> e : dwRsMap.entrySet()) {
            ZxrsDTO d = rsMap.get(e.getKey());
            if (null == d) {
                d = new ZxrsDTO();
                rsMap.put(e.getKey(), d);
            }
            for(ZxrsDTO r : e.getValue()) {
                d.setZxrs(d.getZxrs() + r.getZxrs());
                if ("男".equals(r.getXb())) {
                    d.setNzxrs(r.getZxrs());
                } else if ("女".equals(r.getXb())) {
                    d.setVzxrs(r.getZxrs());
                }
            }
        }
    }
    private void ljIndexAndZxrs(List<AnalysisBasezbDTO> cache, String sbSQL, Long orgId, Map<String, ZxrsDTO> rsMap, Date current) {
        List<AnalysisBasezbDTO> list = cetLjIndexAnalysisManager.queryLjJczb(sbSQL, orgId, current, current);//基础指标
        for (AnalysisBasezbDTO d : list) {
            d.setXxdm(orgId.toString());
            d.setDhlj("2");
            ZxrsDTO rs = rsMap.get(d.getBh());
            if (null != rs) {
                d.setZxrs(rs.getZxrs());
                d.setNzxrs(rs.getNzxrs());
                d.setVzxrs(rs.getVzxrs());
            } else {
                System.out.println("---------------------No rs:" + d.toString());
            }
            cache.add(d);
        }
    }
}
