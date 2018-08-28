package com.aizhixin.cloud.dataanalysis.etl.score.service;

import com.aizhixin.cloud.dataanalysis.zb.app.entity.StudentSemesterScoreIndex;
import com.aizhixin.cloud.dataanalysis.zb.app.mananger.StudentSemesterScoreIndexManager;
import com.aizhixin.cloud.dataanalysis.etl.score.dto.StudentScorezbDTO;
import com.aizhixin.cloud.dataanalysis.etl.score.dto.StudentSemesterScoreIndexDTO;
import com.aizhixin.cloud.dataanalysis.etl.cet.dto.XnXqDTO;
import com.aizhixin.cloud.dataanalysis.etl.score.manager.StandardScoreSemesterManager;
import com.aizhixin.cloud.dataanalysis.etl.score.manager.StudentScoreIndexManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 学生学习成绩学期指标
 */
@Component
@Slf4j
public class StandardScoreSemesterIndexService {
    @Autowired
    private StandardScoreSemesterManager standardScoreSemesterManager;
    @Autowired
    private StudentSemesterScoreIndexManager studentSemesterScoreIndexManager;
    @Autowired
    private StudentScoreIndexManager studentScoreIndexManager;

    @Async
    public void oneSemesterStudentScoreIndex(String xxdm, String xn, String xq) {
        standardScoreSemesterManager.deleteXnXqIndex(xxdm, xn, xq);

        Map<String, StudentSemesterScoreIndexDTO> zbMap = new HashMap<>();
        List<StudentSemesterScoreIndexDTO> zbList = standardScoreSemesterManager.queryStudentSemsterIndex(xxdm, xn, xq);
        for (StudentSemesterScoreIndexDTO d : zbList) {
            zbMap.put(d.getXh(), d);
        }

        List<StudentSemesterScoreIndex> data = standardScoreSemesterManager.queryStudentSemsterGpaIndex(xxdm, xn, xq);
        for (StudentSemesterScoreIndex d : data) {
            d.setXxdm(xxdm);
            d.setXn(xn);
            d.setXqm(xq);
            StudentSemesterScoreIndexDTO zb = zbMap.get(d.getXh());
            if (null != zb) {
                d.setCkkcs(zb.getCkkcs());
                d.setBjgkcs(zb.getBjgkcs());
                d.setBjgzxf(zb.getBjgzxf());
            } else {
                System.out.println("No zb---------" + d.toString());
            }
        }
        if (!data.isEmpty()) {
            studentSemesterScoreIndexManager.save(data);
        }
    }

    @Async
    public void allSemesterStudentScoreIndex(String xxdm) {
        List<XnXqDTO> list = standardScoreSemesterManager.queryAllYearAndSemester(xxdm);
        for (XnXqDTO xnxq : list) {
            oneSemesterStudentScoreIndex(xxdm, xnxq.getXn(), xnxq.getXq());
        }
    }

     @Async
    public void schoolStudentScoreIndex(Long orgId) {
        List<StudentScorezbDTO> cache = new ArrayList<>();
        List<XnXqDTO> xnList = studentScoreIndexManager.queryStudentScoreXnXq(StudentScoreIndexManager.SQL_DC_XN_XQ, orgId.toString());
        for (XnXqDTO d : xnList) {
            List<StudentScorezbDTO> rs = studentScoreIndexManager.queryDczb(StudentScoreIndexManager.SQL_DC_COLLEGE, orgId.toString(), d.getXn(), d.getXq());
            if (null != rs && !rs.isEmpty()) {
                cache.addAll(rs);
            }
            rs = studentScoreIndexManager.queryDczb(StudentScoreIndexManager.SQL_DC_PROFESSIONAL, orgId.toString(), d.getXn(), d.getXq());
            if (null != rs && !rs.isEmpty()) {
                cache.addAll(rs);
            }
        }
        if (!cache.isEmpty()) {
            studentScoreIndexManager.saveStudentScoreIndex(cache, orgId.toString());
        }
    }
}
