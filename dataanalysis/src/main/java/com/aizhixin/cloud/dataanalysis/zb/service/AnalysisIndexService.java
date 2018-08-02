package com.aizhixin.cloud.dataanalysis.zb.service;

import com.aizhixin.cloud.dataanalysis.zb.dto.*;
import com.aizhixin.cloud.dataanalysis.zb.manager.AnalysisIndexManager;
import com.aizhixin.cloud.dataanalysis.zb.manager.IndexAnalysisGradeManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 英语等级指标分析计算逻辑
 */
@Component
@Slf4j
public class AnalysisIndexService {
    @Autowired
    private AnalysisIndexManager analysisIndexManager;
    @Autowired
    private IndexAnalysisGradeManager indexAnalysisGradeManager;

    @Async
    public void schoolBaseIndex(Long orgId) {
        List<AnalysisBasezbDTO> cache = new ArrayList<>();
        //校历
        Map<String, Long> yearAllRs = new HashMap<>();
        List<SchoolCalendarDTO> schoolCalendarList = analysisIndexManager.querySchoolCalendar(AnalysisIndexManager.SQL_SCHOOL_CALENDAR, orgId);

        /*********************************************************单次*******************************************************************************/
        //总人数
        for (SchoolCalendarDTO c : schoolCalendarList) {
            long zxrs = analysisIndexManager.queryAllZxrs(AnalysisIndexManager.SQL_SCHOOL_RS, orgId, c.getJsrq());
            yearAllRs.put(c.getXn() + "-" + c.getXq(), zxrs);
        }
        List<AnalysisBasezbDTO> list = analysisIndexManager.queryJczb(AnalysisIndexManager.SQL_DC_SCHOOL, orgId);//总指标
        for (AnalysisBasezbDTO d : list) {
            d.setXxdm(orgId.toString());
            d.setDhlj("1");
            Long zxrs = yearAllRs.get(d.getXn() + "-" + d.getXq());
            if (null != zxrs) {
                d.setZxrs(zxrs);
            }
            cache.add(d);
        }


        //学院人数
        indexAndZxrs(cache, schoolCalendarList, AnalysisIndexManager.SQL_DC_COLLEGE, AnalysisIndexManager.SQL_COLLEGE_RS, orgId);

        //专业人数
        indexAndZxrs(cache, schoolCalendarList, AnalysisIndexManager.SQL_DC_PROFESSIONAL, AnalysisIndexManager.SQL_PROFESSIONAL_RS, orgId);

        //班级人数
        indexAndZxrs(cache, schoolCalendarList, AnalysisIndexManager.SQL_DC_CLASSES, AnalysisIndexManager.SQL_CLASSES_RS, orgId);

        /*********************************************************************累计************************************************************************************/
        //总人数
        for (SchoolCalendarDTO c : schoolCalendarList) {
//            List<AnalysisBasezbDTO>
            list = analysisIndexManager.queryLjJczb(AnalysisIndexManager.SQL_LJ_SCHOOL, orgId, c.getJsrq());//总指标
            for (AnalysisBasezbDTO d : list) {
                d.setXxdm(orgId.toString());
                d.setDhlj("2");
                Long zxrs = yearAllRs.get(d.getXn() + "-" + d.getXq());
                if (null != zxrs) {
                    d.setZxrs(zxrs);
                } else {
                    System.out.println("-------------------------No rs:" + d.toString());
                }
                cache.add(d);
            }
        }
        //学院
        ljIndexAndZxrs(cache, schoolCalendarList, AnalysisIndexManager.SQL_LJ_COLLEGE, AnalysisIndexManager.SQL_COLLEGE_RS, orgId);
        //专业
        ljIndexAndZxrs(cache, schoolCalendarList, AnalysisIndexManager.SQL_LJ_PROFESSIONAL, AnalysisIndexManager.SQL_PROFESSIONAL_RS, orgId);
        //班级
        ljIndexAndZxrs(cache, schoolCalendarList, AnalysisIndexManager.SQL_LJ_CLASSES, AnalysisIndexManager.SQL_CLASSES_RS, orgId);

        log.info("start save data.count:{}", cache.size());
        System.out.println("---------------------------------------------------------------------------------------------------");
        analysisIndexManager.saveJczb(cache);
    }

    private void indexAndZxrs(List<AnalysisBasezbDTO> cache, List<SchoolCalendarDTO> schoolCalendarList, String sbSQL, String rsSQL, Long orgId) {
        Map<String, Long> rsMap = new HashMap<>();
        for (SchoolCalendarDTO c : schoolCalendarList) {
            List<ZxrsDTO> rsList = analysisIndexManager.querySubZxrs(rsSQL, orgId, c.getJsrq());//人数
            for (ZxrsDTO rs : rsList) {
                rsMap.put(c.getXn() + "-" + c.getXq() + "-" + rs.getBh(), rs.getZxrs());
            }
        }
        List<AnalysisBasezbDTO> list = analysisIndexManager.queryJczb(sbSQL, orgId);//基础指标
        for (AnalysisBasezbDTO d : list) {
            d.setXxdm(orgId.toString());
            d.setDhlj("1");
            Long zxrs = rsMap.get(d.getXn() + "-" + d.getXq() + "-" + d.getBh());
            if (null != zxrs) {
                d.setZxrs(zxrs);
            } else {
                System.out.println("---------------------No rs:" + d.toString());
            }
            cache.add(d);
        }
    }

    private void ljIndexAndZxrs(List<AnalysisBasezbDTO> cache, List<SchoolCalendarDTO> schoolCalendarList, String sbSQL, String rsSQL, Long orgId) {
        Map<String, Long> rsMap = new HashMap<>();
        for (SchoolCalendarDTO c : schoolCalendarList) {
            List<ZxrsDTO> rsList = analysisIndexManager.querySubZxrs(rsSQL, orgId, c.getJsrq());//人数
            for (ZxrsDTO rs : rsList) {
                rsMap.put(c.getXn() + "-" + c.getXq() + "-" + rs.getBh(), rs.getZxrs());
            }
        }
        for (SchoolCalendarDTO c : schoolCalendarList) {
            List<AnalysisBasezbDTO> list = analysisIndexManager.queryLjJczb(sbSQL, orgId, c.getJsrq());//基础指标
            for (AnalysisBasezbDTO d : list) {
                d.setXxdm(orgId.toString());
                d.setDhlj("2");
                Long zxrs = rsMap.get(d.getXn() + "-" + d.getXq() + "-" + d.getBh());
                if (null != zxrs) {
                    d.setZxrs(zxrs);
                } else {
                    System.out.println("---------------------No rs:" + d.toString());
                }
                cache.add(d);
            }
        }
    }


    @Async
    public void schoolNjIndex(Long orgId) {
        List<NjAnalysiszbDTO> cache = new ArrayList<>();
        //校历
        Map<String, Long> yearAllRs = new HashMap<>();
        List<SchoolCalendarDTO> schoolCalendarList = analysisIndexManager.querySchoolCalendar(AnalysisIndexManager.SQL_SCHOOL_CALENDAR, orgId);

        /*********************************************************单次*******************************************************************************/
        //总人数
        log.info("start process dc school....");
        dcIndexNj(cache, schoolCalendarList, IndexAnalysisGradeManager.SQL_DC_NJ_SCHOOL, IndexAnalysisGradeManager.SQL_SCHOOL_RS, orgId);
        //学院人数
        log.info("start process dc college....");
        dcIndexNj(cache, schoolCalendarList, IndexAnalysisGradeManager.SQL_DC_NJ_COLLEGE, IndexAnalysisGradeManager.SQL_COLLEGE_RS, orgId);

        //专业人数
        log.info("start process dc profesional....");
        dcIndexNj(cache, schoolCalendarList, IndexAnalysisGradeManager.SQL_DC_NJ_PROFESSIONAL, IndexAnalysisGradeManager.SQL_PROFESSIONAL_RS, orgId);

        //班级人数
        log.info("start process dc classe....");
        dcIndexNj(cache, schoolCalendarList, IndexAnalysisGradeManager.SQL_DC_NJ_CLASSES, IndexAnalysisGradeManager.SQL_CLASSES_RS, orgId);

        /*********************************************************************累计************************************************************************************/
        //总人数
        log.info("start process lj school....");
        ljIndexNj(cache, schoolCalendarList, IndexAnalysisGradeManager.SQL_LJ_NJ_SCHOOL, IndexAnalysisGradeManager.SQL_SCHOOL_RS, orgId);
        //学院
        log.info("start process lj college....");
        ljIndexNj(cache, schoolCalendarList, IndexAnalysisGradeManager.SQL_LJ_NJ_COLLEGE, IndexAnalysisGradeManager.SQL_COLLEGE_RS, orgId);
        //专业
        log.info("start process lj professional....");
        ljIndexNj(cache, schoolCalendarList, IndexAnalysisGradeManager.SQL_LJ_NJ_PROFESSIONAL, IndexAnalysisGradeManager.SQL_PROFESSIONAL_RS, orgId);
        //班级
        log.info("start process lj classes....");
        ljIndexNj(cache, schoolCalendarList, IndexAnalysisGradeManager.SQL_LJ_NJ_CLASSES, IndexAnalysisGradeManager.SQL_CLASSES_RS, orgId);

        log.info("start save data.count:{}", cache.size());
        System.out.println("---------------------------------------------------------------------------------------------------");
        indexAnalysisGradeManager.saveNjzb(cache);
    }
    private void dcIndexNj(List<NjAnalysiszbDTO> cache, List<SchoolCalendarDTO> schoolCalendarList, String sbSQL, String rsSQL, Long orgId) {
        Map<String, Long> rsMap = new HashMap<>();
        for (SchoolCalendarDTO c : schoolCalendarList) {
            List<NjZxrsDTO> njRsList = indexAnalysisGradeManager.querySubZxrs(rsSQL, orgId, c.getJsrq());
            for (NjZxrsDTO d : njRsList) {
                if (null == d.getBh()) {
                    rsMap.put(c.getXn() + "-" + c.getXq() + "-" + d.getNj(), d.getZxrs());
                } else {
                    rsMap.put(c.getXn() + "-" + c.getXq() + "-" + d.getBh() + "-" + d.getNj(), d.getZxrs());
                }
            }
        }
        List<NjAnalysiszbDTO> list = indexAnalysisGradeManager.queryNjDczb(sbSQL, orgId);//总指标
        for (NjAnalysiszbDTO d : list) {
            d.setXxdm(orgId.toString());
            d.setDhlj("1");
            Long zxrs = null;
            if (null == d.getPbh()) {
                zxrs = rsMap.get(d.getXn() + "-" + d.getXq() + "-" + d.getNj());
            } else {
                zxrs = rsMap.get(d.getXn() + "-" + d.getXq() + "-" + d.getBh() + "-" + d.getNj());
            }
            if (null != zxrs) {
                d.setZxrs(zxrs);
            } else {
                System.out.println("-------------------------No rs:" + d.toString());
            }
//            System.out.println(d.toString());
            cache.add(d);
        }
    }

    private void ljIndexNj(List<NjAnalysiszbDTO> cache, List<SchoolCalendarDTO> schoolCalendarList, String sbSQL, String rsSQL, Long orgId) {
        Map<String, Long> rsMap = new HashMap<>();
        for (SchoolCalendarDTO c : schoolCalendarList) {
            List<NjZxrsDTO> rsList = indexAnalysisGradeManager.querySubZxrs(rsSQL, orgId, c.getJsrq());//人数
            for (NjZxrsDTO d : rsList) {
                if (null == d.getBh()) {
                    rsMap.put(c.getXn() + "-" + c.getXq() + "-" + d.getNj(), d.getZxrs());
                } else {
                    rsMap.put(c.getXn() + "-" + c.getXq() + "-" + d.getBh() + "-" + d.getNj(), d.getZxrs());
                }
            }
        }
        for (SchoolCalendarDTO c : schoolCalendarList) {
            List<NjAnalysiszbDTO> list = indexAnalysisGradeManager.queryNjLjzb(sbSQL, orgId, c.getJsrq());//基础指标
            for (NjAnalysiszbDTO d : list) {
                d.setXxdm(orgId.toString());
                d.setDhlj("2");
                Long zxrs = null;
                if (null == d.getPbh()) {
                    zxrs = rsMap.get(d.getXn() + "-" + d.getXq() + "-" + d.getNj());
                } else {
                    zxrs = rsMap.get(d.getXn() + "-" + d.getXq() + "-" + d.getBh() + "-" + d.getNj());
                }
                if (null != zxrs) {
                    d.setZxrs(zxrs);
                } else {
                    System.out.println("---------------------No rs:" + d.toString());
                }
                cache.add(d);
            }
        }
    }
}
