package com.aizhixin.cloud.dataanalysis.bz.service;

import com.aizhixin.cloud.dataanalysis.bz.dto.CetEtlDTO;
import com.aizhixin.cloud.dataanalysis.bz.entity.CetStandardScore;
import com.aizhixin.cloud.dataanalysis.bz.manager.CetETLFromDBManager;
import com.aizhixin.cloud.dataanalysis.bz.manager.CetStandardScoreManager;
import com.aizhixin.cloud.dataanalysis.common.util.DateUtil;
import com.aizhixin.cloud.dataanalysis.zb.dto.SchoolCalendarDTO;
import com.aizhixin.cloud.dataanalysis.zb.manager.AnalysisIndexManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Slf4j
@Component
public class CetEtlService {
    @Autowired
    private CetETLFromDBManager cetETLFromDBManager;
    @Autowired
    private AnalysisIndexManager analysisIndexManager;
    @Autowired
    private CetStandardScoreManager cetStandardScoreManager;

    @Async
    public void etlDB2DB(Long orgId) {
        List<SchoolCalendarDTO> schoolCalendarList = analysisIndexManager.querySchoolCalendar(AnalysisIndexManager.SQL_SCHOOL_CALENDAR, orgId);
        int pn = 0;
        int pz = 1000;
        List<CetStandardScore> cache = new ArrayList<>();
        log.info("Load data page {}.", pn);
        List<CetEtlDTO> srcDataList = cetETLFromDBManager.queryDcJczb(CetETLFromDBManager.SQL_ETL_DB_SRC, orgId, pn, pz);
        while (null != srcDataList && !srcDataList.isEmpty()) {
            for (CetEtlDTO s : srcDataList) {
                CetStandardScore c = new CetStandardScore();
                cache.add(c);
                c.setXxdm(orgId.toString());
                if (null != s.getKsrq()) {
                    Date ksrq = DateUtil.parseShortDate(s.getKsrq());
                    c.setKsrq(ksrq);
                    for (SchoolCalendarDTO cd : schoolCalendarList) {
                        if (ksrq.after(cd.getKsrq()) && ksrq.before(cd.getJsrq())) {
                            if ("春".equals(cd.getXq())) {
                                c.setXqm("2");
                                c.setXn((Integer.valueOf(cd.getXn()) - 1) + "-" + cd.getXn());
                            } else {
                                c.setXqm("1");
                                c.setXn(cd.getXn() + "-" + (Integer.valueOf(cd.getXn()) + 1));
                            }
                            break;
                        }
                    }
                }
                c.setYxsh(s.getYxsh());
                c.setZyh(s.getZyh());
                c.setBh(s.getBjbm());
                c.setXh(s.getXh());
                c.setXm(s.getXm());
                if ("男".equals(s.getXb())) {
                    c.setXbm("1");
                } else if("女".equals(s.getXb())) {
                    c.setXbm("2");
                }
                if (null != s.getNj() && s.getNj().endsWith("级") && s.getNj().length() >= 4) {
                    c.setNj(s.getNj().substring(0, 4));
                }
                c.setLygbm("156");
                if (null != s.getSfzh() && s.getSfzh().length() >= 15) {
                    c.setLydqm(s.getSfzh().substring(0, 2));
                    c.setLyqxm(s.getSfzh().substring(0, 6));
                }
                c.setCsrq(s.getCsrq());
                if (null != s.getKslx()) {
                    if (s.getKslx().indexOf("四级") > 0) {
                        c.setKslx("4");
                    } else if (s.getKslx().indexOf("六级") > 0) {
                        c.setKslx("6");
                    } else if (s.getKslx().indexOf("三级") > 0) {
                        c.setKslx("3");
                    }
                }
                c.setCj(s.getCj());
                if (null != c.getKslx()) {
                    if ("3".equals(c.getKslx())) {
                        if (s.getCj() >= 60) {
                            c.setTg(1);
                        } else {
                            c.setTg(0);
                        }
                    } else {
                        if (s.getCj() >= 425) {
                            c.setTg(1);
                        } else {
                            c.setTg(0);
                        }
                    }
                }
                c.setCk(1);
            }
            pn++;
            pn *= pz;
            log.info("Load data page {}.", pn);
            srcDataList = cetETLFromDBManager.queryDcJczb(CetETLFromDBManager.SQL_ETL_DB_SRC, orgId, pn, pz);
        }
        if (!cache.isEmpty()) {
            log.info("start save {} data", cache.size());
            cetStandardScoreManager.save(cache);
        }
    }
}
