package com.aizhixin.cloud.dataanalysis.etl.rollcall.service;

import com.aizhixin.cloud.dataanalysis.common.util.DateUtil;
import com.aizhixin.cloud.dataanalysis.etl.rollcall.manager.EtlRollcallAlertManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Component
public class EtlRollcallAlertService {

    @Autowired
    private EtlRollcallAlertManager etlRollcallAlertManager;

    /**
     * 计算最近3天学生预警
     */
    @Async
    public void calLastest3StudentRollAlert(Long orgId, Date start, Date end, Double dkl) {
        if (null == start) {
            start = new Date();
        }
        if (null == end) {
            end = DateUtil.afterNDay(start, 1);
        } else {
            end = DateUtil.afterNDay(end, 1);
        }
        if(null == dkl) {
            dkl = 0.6;
        }
        List<Long> orgs = new ArrayList<>();//计算合适的orgId
        if (null == orgId || orgId <= 0) {
            Date s = DateUtil.afterNDay(start, -3);
            List<Long> os = etlRollcallAlertManager.queryOrgIds(DateUtil.getZerotime(s), end);
            if (null != os) {
                orgs.addAll(os);
            }
        } else {
            orgs.add(orgId);
        }
        while (start.before(end)){
            for (Long o : orgs) {
                etlRollcallAlertManager.calStudentLastest3RollcallAlert(o, start, dkl);
            }
            start = DateUtil.afterNDay(start, 1);
        }
    }
}
