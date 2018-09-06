package com.aizhixin.cloud.dataanalysis.dd.rollcall.service;


import com.aizhixin.cloud.dataanalysis.common.util.DateUtil;
import com.aizhixin.cloud.dataanalysis.dd.rollcall.manager.RollCallManager;
import com.aizhixin.cloud.dataanalysis.dd.rollcall.vo.SchoolWeekRollcallScreenVO;
import com.aizhixin.cloud.dataanalysis.dd.rollcall.vo.SchoolWeekRollcallScreenZhVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Component
public class RollcallService {

    @Autowired
    private RollCallManager rollCallManager;

    private void cacheLjz(SchoolWeekRollcallScreenVO v, SchoolWeekRollcallScreenVO c) {
        if (null == c.getYdrs()) {
            c.setYdrs(v.getYdrs());
        } else {
            c.setYdrs(c.getYdrs() + v.getYdrs());
        }

        if (null == c.getSdrs()) {
            c.setSdrs(v.getSdrs());
        } else {
            c.setSdrs(c.getSdrs() + v.getSdrs());
        }

        if (null == c.getCdrs()) {
            c.setCdrs(v.getCdrs());
        } else {
            c.setCdrs(c.getCdrs() + v.getCdrs());
        }

        if (null == c.getQjrs()) {
            c.setQjrs(v.getQjrs());
        } else {
            c.setQjrs(c.getQjrs() + v.getQjrs());
        }

        if (null == c.getZtrs()) {
            c.setZtrs(v.getZtrs());
        } else {
            c.setZtrs(c.getKkrs() + v.getZtrs());
        }

        if (null == c.getKkrs()) {
            c.setKkrs(v.getKkrs());
        } else {
            c.setKkrs(c.getKkrs() + v.getKkrs());
        }
    }

    public SchoolWeekRollcallScreenZhVO queryLastestWeekRollcall(Long orgId) {
        SchoolWeekRollcallScreenZhVO zhVo = new SchoolWeekRollcallScreenZhVO ();
        Date current = new Date();
        int curWeek = DateUtil.getDayOfWeek(current);
//        Date monday = DateUtil.getMonday(current);
        NumberFormat nt = NumberFormat.getPercentInstance();
        nt.setMinimumFractionDigits(2);

        Date lastestPreMonday =  DateUtil.getPreMonday(current);
        Date nextday = DateUtil.nextDate(current);
        List<SchoolWeekRollcallScreenVO> list = rollCallManager.queryLastTwoWeekRollcall(orgId, DateUtil.formatShort(lastestPreMonday), DateUtil.formatShort(nextday));
        List<SchoolWeekRollcallScreenVO> rsList = new ArrayList<>();
        zhVo.setDays(rsList);

        SchoolWeekRollcallScreenVO preWeekCache = new SchoolWeekRollcallScreenVO ();
        SchoolWeekRollcallScreenVO curWeekCache = new SchoolWeekRollcallScreenVO ();
        boolean preWeek = true;
        int weekCount = 0;
        for (SchoolWeekRollcallScreenVO v : list) {
            if (null != v.getDay()) {
                Date d = DateUtil.parseShortDate(v.getDay());
                int week = DateUtil.getDayOfWeek(d);
                if (1 == week && 0 == weekCount) {
                    weekCount++;
                } else if (1 == week && 1 == weekCount) {
                    preWeek = false;
                }
                if (preWeek) {
                    cacheLjz(v, preWeekCache);
                } else {
                    if (week <= curWeek) {
                        cacheLjz(v, curWeekCache);
                        rsList.add(v);
                        v.setDayOfWeek("" + week);
                    } else {
                        break;
                    }
                }
            }
        }
        if (null != curWeekCache.getYdrs() && null != curWeekCache.getSdrs()) {
            double tmp = curWeekCache.getSdrs() * 1.0 / curWeekCache.getYdrs();
            zhVo.setDkl(nt.format(tmp));
            if (null != preWeekCache.getYdrs() && null != preWeekCache.getSdrs()) {
                double preTmp = preWeekCache.getSdrs() * 1.0 / preWeekCache.getYdrs();
                zhVo.setQs(nt.format(tmp - preTmp));
            } else {
                zhVo.setQs(zhVo.getDkl());
            }
        }
        return zhVo;
    }
}
