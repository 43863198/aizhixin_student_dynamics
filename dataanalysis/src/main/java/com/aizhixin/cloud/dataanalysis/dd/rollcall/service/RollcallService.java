package com.aizhixin.cloud.dataanalysis.dd.rollcall.service;


import com.aizhixin.cloud.dataanalysis.common.PageData;
import com.aizhixin.cloud.dataanalysis.common.util.DateUtil;
import com.aizhixin.cloud.dataanalysis.dd.rollcall.manager.RollCallManager;
import com.aizhixin.cloud.dataanalysis.dd.rollcall.vo.*;
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
//    @Autowired
//    private Lastest3RollcallAlertManager lastest3RollcallAlertManager;

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

    /**
     * 模拟数据版
     */
    public SchoolWeekRollcallScreenZhVO queryLastestWeekRollcallDemo(Long orgId) {
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

    /**
     * 真实逻辑版
     */
    public SchoolWeekRollcallScreenZhVO queryLastestWeekRollcall(Long orgId) {
        SchoolWeekRollcallScreenZhVO zhVo = new SchoolWeekRollcallScreenZhVO ();
        Date current = new Date();
        Date monday = DateUtil.getMonday(current);
        NumberFormat nt = NumberFormat.getPercentInstance();
        nt.setMinimumFractionDigits(2);

        Date lastestPreMonday =  DateUtil.getPreMonday(current);
        Date nextday = DateUtil.nextDate(current);

        int arithmetic = rollCallManager.getOrgArithmetic(orgId);
        List<SchoolWeekRollcallScreenVO> preWeek = rollCallManager.findRollcallStatics(orgId, lastestPreMonday, monday);
        List<SchoolWeekRollcallScreenVO> curWeekDataList = rollCallManager.findRollcallStaticsDayAndDay(orgId, monday, nextday);
        zhVo.setDays(curWeekDataList);

        SchoolWeekRollcallScreenVO preWeekData = null;//上周合计值
        SchoolWeekRollcallScreenVO curWeekData = new SchoolWeekRollcallScreenVO ();//本周合计值
        if (null != preWeek && preWeek.size() > 0) {
            preWeekData = preWeek.get(0);
        }
        if (null != curWeekDataList && curWeekDataList.size() > 0) {
            for (SchoolWeekRollcallScreenVO v : curWeekDataList) {
                cacheLjz(v, curWeekData);
            }
        }

        double curWeekDkl = 0.0;
        if (null != curWeekData.getYdrs()) {
            curWeekDkl = rollCallManager.attendanceAccount(curWeekData.getYdrs(), curWeekData.getSdrs(), curWeekData.getCdrs(), curWeekData.getQjrs(), curWeekData.getKkrs(), arithmetic);//本周到课率
            zhVo.setDkl(nt.format(curWeekDkl));
        } else {
            zhVo.setDkl("0.00%");
        }
        if (null != preWeekData) {
            double preWeekDkl = rollCallManager.attendanceAccount(preWeekData.getYdrs(), preWeekData.getSdrs(), preWeekData.getCdrs(), preWeekData.getQjrs(), preWeekData.getKkrs(), arithmetic);//上周到课率
            zhVo.setQs(nt.format(curWeekDkl - preWeekDkl));
        } else {
            zhVo.setQs(nt.format(curWeekDkl));
        }

        return zhVo;
    }

    /**
     * 学校管理平台老师考勤告警查询
     * 需要查询点点和org的数据库，直接查询没有调用接口
     */
    public PageData<TeacherRollcallAlertVO> queryTeacherRollcallAlert (Long orgId, Long collegeId, Long teacherId, Date start, Date end, Double dkl, Integer pageIndex, Integer pageSize) {
        if (null == orgId || orgId <= 0) {
            return new PageData<>();
        }
        return rollCallManager.queryTeacherDkl(orgId, collegeId, teacherId, start, end, dkl, pageIndex, pageSize);
    }

    /**
     * 学校管理平台班级考勤告警查询
     * 需要查询点点和org的数据库，直接查询没有调用接口
     */
    public PageData<ClassesRollcallAlertVO> queryClassesRollcallAlert (Long orgId, Long collegeId, Long teacherId, Date start, Date end, Double dkl, Integer pageIndex, Integer pageSize) {
        if (null == orgId || orgId <= 0) {
            return new PageData<>();
        }
        return rollCallManager.queryClassesDkl(orgId, collegeId, teacherId, start, end, dkl, pageIndex, pageSize);
    }

    /**
     * 学生到课率告警统计
     */
    public PageData<StudentRollcallAlertVO> findStudentRollcallAlert(Long orgId, Long collegeId, String name, Date start, Date end, Integer pageIndex, Integer pageSize) {
        return rollCallManager.findStudentRollcallAlert(orgId, collegeId, name, start, end, pageIndex, pageSize);
    }

    /**
     * 学生缺课告警统计
     */
    public PageData<UnNormalRollcallAlertVO> queryStudentUnNormalRollcall(Long orgId, Long collegeId, String name, Date start, Date end, Double undkl, Integer pageIndex, Integer pageSize) {
        return rollCallManager.queryStudentUnNormalRollcall(orgId, collegeId, name, start, end, undkl, pageIndex, pageSize);
    }

    public UnitRollcallStatisticsDOVO findUnitRollcallStatistics(Long orgId, Long collegeId, Long professionalId, String timeRange) {
        return rollCallManager.findUnitRollcallStatistics(orgId, collegeId, professionalId, timeRange);
    }
}