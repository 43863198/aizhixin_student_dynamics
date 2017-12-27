package com.aizhixin.cloud.dataanalysis.analysis.job;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * @author: Created by jianwei.wu
 * @E-mail: wujianwei@aizhixin.com
 * @Date: 2017-12-27
 */
@Component
public class TeachingScoreStatisticsJob {
    @Autowired
    private TeachingScoreAnalysisJob teachingScoreAnalysisJob;

    public Map<String, Object> teachingScoreStatistics() {
        Map<String, Object> reslut = new HashMap<>();
        teachingScoreAnalysisJob.teachingScoreStatisticsAsync();
        reslut.put("message","教学成绩统计任务开始...");
        return reslut;
    }

}
