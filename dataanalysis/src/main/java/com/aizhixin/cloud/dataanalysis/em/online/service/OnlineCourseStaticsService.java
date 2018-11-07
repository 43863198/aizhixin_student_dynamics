package com.aizhixin.cloud.dataanalysis.em.online.service;

import com.aizhixin.cloud.dataanalysis.em.online.manager.OnlineCourseStaticsManager;
import com.aizhixin.cloud.dataanalysis.em.online.vo.ScreenOnlineCourseVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class OnlineCourseStaticsService {
    @Autowired
    private OnlineCourseStaticsManager onlineCourseStaticsManager;

    public ScreenOnlineCourseVO testOnlineCourse(Long orgId, Long collegeId) {
        return onlineCourseStaticsManager.queryOnlineCouseLastest(orgId, collegeId);
    }
}
