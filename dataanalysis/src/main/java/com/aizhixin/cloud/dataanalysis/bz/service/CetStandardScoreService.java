package com.aizhixin.cloud.dataanalysis.bz.service;

import com.aizhixin.cloud.dataanalysis.bz.entity.CetStandardScore;
import com.aizhixin.cloud.dataanalysis.bz.manager.CetStandardScoreManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Slf4j
@Component
public class CetStandardScoreService {

    @Autowired
    private CetStandardScoreManager cetStandardScoreManager;


    public List<CetStandardScore> getCetScore(Long orgId, String jobNum, Integer pageNumber, Integer pageSize) {

        return cetStandardScoreManager.getCetScore(orgId, jobNum, pageNumber, pageSize);
    }

    public Map<String, Object> getTop(Long orgId, String cetType, String teacherYear, String semester) {
        teacherYear = teacherYear + "-" +  (Integer.valueOf(teacherYear) + 1);
        return cetStandardScoreManager.getTop(orgId, cetType, teacherYear, semester);
    }
}
