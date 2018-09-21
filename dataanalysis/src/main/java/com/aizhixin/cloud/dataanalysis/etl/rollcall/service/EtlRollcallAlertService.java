//package com.aizhixin.cloud.dataanalysis.etl.rollcall.service;
//
//import com.aizhixin.cloud.dataanalysis.common.util.DateUtil;
//import com.aizhixin.cloud.dataanalysis.etl.rollcall.manager.EtlRollcallAlertManager;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.scheduling.annotation.Async;
//import org.springframework.stereotype.Component;
//
//import java.util.ArrayList;
//import java.util.Date;
//import java.util.List;
//
//@Component
//@Slf4j
//public class EtlRollcallAlertService {
//
//    @Autowired
//    private EtlRollcallAlertManager etlRollcallAlertManager;
//
//    /**
//     * 计算最近3天学生预警
//     */
//    @Async
//    public void calLastest3StudentRollAlert(Long orgId, Date start, Date end, Double dkl) {
//        Date cur = new Date();
//        Date today = new Date();
//        today =  DateUtil.getZerotime(today);
//        if (null == start) {
//            start = DateUtil.afterNDay(cur, -1);
//            start =  DateUtil.getZerotime(start);
//        } else {
//            start = DateUtil.getZerotime(start);
//        }
//        if (null == end) {
//            end =  DateUtil.getZerotime(cur);
//        } else {
//            end = DateUtil.afterNDay(end, 1);
//            end = DateUtil.getZerotime(end);
//        }
//        if (end.after(today)) {
//            end = today;
//        }
//        if(null == dkl) {
//            dkl = 0.6;
//        }
//        List<Long> orgs = new ArrayList<>();//计算合适的orgId
//        if (null == orgId || orgId <= 0) {
//            Date s = DateUtil.afterNDay(start, -2);
//            List<Long> os = etlRollcallAlertManager.queryOrgIds(DateUtil.getZerotime(s), end);
//            if (null != os) {
//                orgs.addAll(os);
//            }
//        } else {
//            orgs.add(orgId);
//        }
//        while (start.before(end)){
//            for (Long o : orgs) {
//                log.info("Cal Lastest3 student rollcall orgId:{}, Date:{}", o, DateUtil.formatShort(start));
//                etlRollcallAlertManager.calStudentLastest3RollcallAlert(o, start, dkl);
//            }
//            start = DateUtil.afterNDay(start, 1);
//        }
//    }
//}
