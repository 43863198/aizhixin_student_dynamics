package com.aizhixin.cloud.dataanalysis.etl.cet.service;

import com.aizhixin.cloud.dataanalysis.etl.cet.dto.AnalysisBasezbDTO;
import com.aizhixin.cloud.dataanalysis.etl.cet.dto.SchoolCalendarDTO;
import com.aizhixin.cloud.dataanalysis.etl.cet.dto.ZxrsDTO;
import com.aizhixin.cloud.dataanalysis.etl.cet.manager.CetDcIndexAnalysisManager;
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
public class CetDcIndexAnalysisService {
    @Autowired
    private CetDcIndexAnalysisManager cetDcIndexAnalysisManager;
    @Autowired
    private CetLjIndexAnalysisManager cetLjIndexAnalysisManager;

    @Async
    public void calDcHaveTest(Long orgId) {
        List<AnalysisBasezbDTO> cache = new ArrayList<>();
        cetDcIndexAnalysisManager.deleteHistory(CetDcIndexAnalysisManager.SQL_DELETE_ALL_SHOOL_DC, orgId.toString());//删除单次统计基础指标数据

        List<SchoolCalendarDTO> schoolCalendarList = cetLjIndexAnalysisManager.queryCetXnxqAndDate(orgId.toString());//查询包含有等级考试数据的学年学期
        Map<String, ZxrsDTO> rsMap = new HashMap<>();
        //全校
        for (SchoolCalendarDTO  c : schoolCalendarList) {//查询在校人数
            cetLjIndexAnalysisManager.haveCalendarRs(rsMap, CetLjIndexAnalysisManager.SQL_SCHOOL_RS, orgId, c);
        }
        dcIndexAndZxrs(cache, CetDcIndexAnalysisManager.SQL_DC_SCHOOL,  orgId.toString(), rsMap);
        log.info("Cal dc data add school count:{}", cache.size());

        //学院
        rsMap.clear();
        for (SchoolCalendarDTO  c : schoolCalendarList) {//查询在校人数
            cetLjIndexAnalysisManager.haveCalendarRs(rsMap, CetLjIndexAnalysisManager.SQL_COLLEGE_RS, orgId, c);
        }
        dcIndexAndZxrs(cache, CetDcIndexAnalysisManager.SQL_DC_COLLEGE,  orgId.toString(), rsMap);
        log.info("Cal dc data add college count:{}", cache.size());

        //专业
        rsMap.clear();
        for (SchoolCalendarDTO  c : schoolCalendarList) {//查询在校人数
            cetLjIndexAnalysisManager.haveCalendarRs(rsMap, CetLjIndexAnalysisManager.SQL_PROFESSIONAL_RS, orgId, c);
        }
        dcIndexAndZxrs(cache, CetDcIndexAnalysisManager.SQL_DC_PROFESSIONAL, orgId.toString(), rsMap);
        log.info("Cal dc data add professional count:{}", cache.size());

        //班级
        rsMap.clear();
        for (SchoolCalendarDTO  c : schoolCalendarList) {//查询在校人数
            cetLjIndexAnalysisManager.haveCalendarRs(rsMap, CetLjIndexAnalysisManager.SQL_CLASSES_RS, orgId, c);
        }
        dcIndexAndZxrs(cache, CetDcIndexAnalysisManager.SQL_DC_CLASSES, orgId.toString(), rsMap);
        log.info("----------------------------------------Cal dc data add classes count:{}", cache.size());
        cetLjIndexAnalysisManager.saveJczb(cache);
    }

    private void dcIndexAndZxrs(List<AnalysisBasezbDTO> cache, String sbSQL, String  xxdm, Map<String, ZxrsDTO> rsMap) {
        List<AnalysisBasezbDTO> list = cetDcIndexAnalysisManager.queryDcJczb(sbSQL, xxdm);//基础指标
        for (AnalysisBasezbDTO d : list) {
            d.setXxdm(xxdm);
            d.setDhlj("1");
            ZxrsDTO zxrs = rsMap.get(d.getXn() + "-" + d.getXq() + "-" + d.getBh());
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
}
