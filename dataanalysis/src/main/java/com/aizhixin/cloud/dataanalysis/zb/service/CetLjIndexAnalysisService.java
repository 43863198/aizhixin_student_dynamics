package com.aizhixin.cloud.dataanalysis.zb.service;

import com.aizhixin.cloud.dataanalysis.zb.dto.AnalysisBasezbDTO;
import com.aizhixin.cloud.dataanalysis.zb.dto.SchoolCalendarDTO;
import com.aizhixin.cloud.dataanalysis.zb.dto.ZxrsDTO;
import com.aizhixin.cloud.dataanalysis.zb.manager.CetLjIndexAnalysisManager;
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
    public void schoolBaseIndex(Long orgId) {
        List<AnalysisBasezbDTO> cache = new ArrayList<>();
        cetLjIndexAnalysisManager.deleteHistory(CetLjIndexAnalysisManager.SQL_DELETE_NEW_SHOOL_LJ, orgId.toString());
        //校历
        List<SchoolCalendarDTO> schoolCalendarList = cetLjIndexAnalysisManager.queryCetXnxqAndDate(orgId.toString());//查询包含有等级考试数据的学年学期
        /*********************************************************************累计************************************************************************************/
        Map<String, Long> rsMap = new HashMap<>();

        //学院
        for (SchoolCalendarDTO  c : schoolCalendarList) {//查询在校人数
            List<ZxrsDTO> rsList = cetLjIndexAnalysisManager.querySubZxrs(CetLjIndexAnalysisManager.SQL_COLLEGE_RS, orgId, c.getKsrq(), c.getJsrq());
            for (ZxrsDTO d : rsList) {
                rsMap.put(c.getXn() + "-" + c.getXq() + "-" + d.getBh(), d.getZxrs());
            }
        }
        log.info("Find zxrs college data count:{}", rsMap.size());
        for (SchoolCalendarDTO  c : schoolCalendarList) {
            ljIndexAndZxrs(cache, c, CetLjIndexAnalysisManager.SQL_LJ_COLLEGE,  orgId, rsMap);
        }

        rsMap.clear();
        //专业
        for (SchoolCalendarDTO  c : schoolCalendarList) {//查询在校人数
            List<ZxrsDTO> rsList = cetLjIndexAnalysisManager.querySubZxrs(CetLjIndexAnalysisManager.SQL_PROFESSIONAL_RS, orgId, c.getKsrq(), c.getJsrq());
            for (ZxrsDTO d : rsList) {
                rsMap.put(c.getXn() + "-" + c.getXq() + "-" + d.getBh(), d.getZxrs());
            }
        }
        log.info("Find zxrs professional data count:{}", rsMap.size());
        for (SchoolCalendarDTO  c : schoolCalendarList) {
            ljIndexAndZxrs(cache, c, CetLjIndexAnalysisManager.SQL_LJ_PROFESSIONAL, orgId, rsMap);
        }

        rsMap.clear();
        //班级
        for (SchoolCalendarDTO  c : schoolCalendarList) {//查询在校人数
            List<ZxrsDTO> rsList = cetLjIndexAnalysisManager.querySubZxrs(CetLjIndexAnalysisManager.SQL_CLASSES_RS, orgId, c.getKsrq(), c.getJsrq());
            for (ZxrsDTO d : rsList) {
                rsMap.put(c.getXn() + "-" + c.getXq() + "-" + d.getBh(), d.getZxrs());
            }
        }

        log.info("Find zxrs classes data count:{}", rsMap.size());
        for (SchoolCalendarDTO  c : schoolCalendarList) {
            ljIndexAndZxrs(cache, c, CetLjIndexAnalysisManager.SQL_LJ_CLASSES, orgId, rsMap);
        }

        System.out.println("---------------------------------------------------------------------------------------------------");
        log.info("start save data.count:{}", cache.size());
        cetLjIndexAnalysisManager.saveJczb(cache);
    }

    @Async
    public void calLjNewest(Long orgId) {
        List<AnalysisBasezbDTO> cache = new ArrayList<>();
        cetLjIndexAnalysisManager.deleteHistory(CetLjIndexAnalysisManager.SQL_DELETE_ALL_NEW_SHOOL_LJ, orgId.toString());

        Date current = new Date ();
        Map<String, Long> rsMap = new HashMap<>();

        //全校当前时间统计
        List<AnalysisBasezbDTO> list = cetLjIndexAnalysisManager.queryLjJczb(CetLjIndexAnalysisManager.SQL_LJ_SCHOOL, orgId, new Date());
        Long zxrs = cetLjIndexAnalysisManager.queryAllZxrs(CetLjIndexAnalysisManager.SQL_SCHOOL_RS, orgId, new Date ());
        for (AnalysisBasezbDTO d : list) {
            d.setZxrs(zxrs);
            d.setXxdm(orgId.toString());
            d.setDhlj("2");
            cache.add(d);
        }

        //学院
        noDateRs(rsMap, CetLjIndexAnalysisManager.SQL_COLLEGE_RS, orgId, current);
        ljIndexAndZxrs(cache, CetLjIndexAnalysisManager.SQL_LJ_COLLEGE, orgId, rsMap, current);
        //专业
        noDateRs(rsMap, CetLjIndexAnalysisManager.SQL_PROFESSIONAL_RS, orgId, current);
        ljIndexAndZxrs(cache, CetLjIndexAnalysisManager.SQL_LJ_PROFESSIONAL, orgId, rsMap, current);

        //班级
        noDateRs(rsMap, CetLjIndexAnalysisManager.SQL_CLASSES_RS, orgId, current);
        ljIndexAndZxrs(cache, CetLjIndexAnalysisManager.SQL_LJ_CLASSES, orgId, rsMap, current);
    }

    private void noDateRs(Map<String, Long> rsMap, String rsSql, Long orgId, Date current) {
        rsMap.clear();
        List<ZxrsDTO> rsList = cetLjIndexAnalysisManager.querySubZxrs(rsSql, orgId, current, current);
        for (ZxrsDTO d : rsList) {
            rsMap.put(d.getBh(), d.getZxrs());
        }
    }

    private void ljIndexAndZxrs(List<AnalysisBasezbDTO> cache, SchoolCalendarDTO c, String sbSQL, Long orgId, Map<String, Long> rsMap) {
        List<AnalysisBasezbDTO> list = cetLjIndexAnalysisManager.queryLjJczb(sbSQL, orgId, c.getKsrq(), c.getJsrq());//基础指标
        for (AnalysisBasezbDTO d : list) {
            d.setXxdm(orgId.toString());
            d.setDhlj("2");
            d.setXn(c.getXn());
            d.setXq(c.getXq());
            Long zxrs = rsMap.get(c.getXn() + "-" + c.getXq() + "-" + d.getBh());
            if (null != zxrs) {
                d.setZxrs(zxrs);
            } else {
                System.out.println("---------------------No rs:" + d.toString());
            }
            cache.add(d);
        }
    }

    private void ljIndexAndZxrs(List<AnalysisBasezbDTO> cache, String sbSQL, Long orgId, Map<String, Long> rsMap, Date current) {
        List<AnalysisBasezbDTO> list = cetLjIndexAnalysisManager.queryLjJczb(sbSQL, orgId, current, current);//基础指标
        for (AnalysisBasezbDTO d : list) {
            d.setXxdm(orgId.toString());
            d.setDhlj("2");
            Long zxrs = rsMap.get(d.getBh());
            if (null != zxrs) {
                d.setZxrs(zxrs);
            } else {
                System.out.println("---------------------No rs:" + d.toString());
            }
            cache.add(d);
        }
    }
}
