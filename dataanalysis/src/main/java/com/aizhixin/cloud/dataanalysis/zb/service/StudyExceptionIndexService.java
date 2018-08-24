package com.aizhixin.cloud.dataanalysis.zb.service;

import com.aizhixin.cloud.dataanalysis.zb.app.entity.StudyExceptionIndex;
import com.aizhixin.cloud.dataanalysis.zb.app.mananger.StudyExceptionIndexManager;
import com.aizhixin.cloud.dataanalysis.zb.manager.StudyExceptionIndexZbManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * 修读异常指标计算
 */
@Component
@Slf4j
public class StudyExceptionIndexService {

    @Autowired
    private StudyExceptionIndexZbManager studyExceptionIndexZbManager;
    @Autowired
    private StudyExceptionIndexManager studyExceptionIndexManager;

    @Async
    public void calCurrentDateIndex(Long orgId) {
        Date current = new Date();
        int year, month;
        String xn, xq;
        Calendar c = Calendar.getInstance();
        year = c.get(Calendar.YEAR);
        month = c.get(Calendar.MONTH) + 1;
        if (month >= 9 || month < 3) {
            xq = "1";
        } else {
            xq = "2";
        }
        if (month >= 9) {
            xn = year + "-" + (year + 1);
        } else {
            xn = (year - 1) + "-" + year;
        }

        studyExceptionIndexManager.deleteByXxdmAndXnAndXqm(orgId.toString(), xn, xq);
        log.info("Clear xxdm({}) xn({}), xq({}) studyException index data", orgId, xn, xq);
        List<String> collegesList = studyExceptionIndexZbManager.queryAllYxsh(orgId, current);
        for (String yxsh : collegesList) {
            log.info("start cal yxsh({}) studyException index", yxsh);
            List<StudyExceptionIndex> dataList = studyExceptionIndexZbManager.queryXyxdyczb(orgId, yxsh, current);
            if (null != dataList) {
                for (StudyExceptionIndex d : dataList) {
                    d.setXxdm(orgId.toString());
                    d.setXn(xn);
                    d.setXqm(xq);
                }
                studyExceptionIndexManager.save(dataList);
                log.info("Save count({}) yxsh ({}) studyException index data", dataList.size(), yxsh);
            }
        }

        log.info("complete cal xxdm({}) xn({}), xq({}) studyException index.", orgId, xn, xq);
    }

}
