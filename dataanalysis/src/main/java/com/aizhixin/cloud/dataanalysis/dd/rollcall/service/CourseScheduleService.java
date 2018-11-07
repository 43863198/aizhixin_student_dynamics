package com.aizhixin.cloud.dataanalysis.dd.rollcall.service;


import com.aizhixin.cloud.dataanalysis.common.RoleTool;
import com.aizhixin.cloud.dataanalysis.common.dto.OrgCollegeIdDTO;
import com.aizhixin.cloud.dataanalysis.common.util.DateUtil;
import com.aizhixin.cloud.dataanalysis.dd.rollcall.dto.PeriodDTO;
import com.aizhixin.cloud.dataanalysis.dd.rollcall.manager.CourseScheduleManager;
import com.aizhixin.cloud.dataanalysis.dd.rollcall.manager.PeriodManager;
import com.aizhixin.cloud.dataanalysis.dd.rollcall.vo.ClassesCourseCountVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

@Component
public class CourseScheduleService {
    @Autowired
    private PeriodManager periodManager;

    @Autowired
    private CourseScheduleManager courseScheduleManager;

    public List<ClassesCourseCountVO> getCurrentDayCourseScheduleCount(Long orgId, Long collegeId, String roles) {
        List<PeriodDTO> pList = periodManager.findAllPeroidByOrgId(orgId);
        OrgCollegeIdDTO dto = RoleTool.orgAndCollegeResetByRole(orgId, collegeId, roles);
        return  courseScheduleManager.findAllPeroidClassesCourseCountByOrgIdAndDay(DateUtil.formatShort(new Date()), dto.getOrgId(), dto.getCollegeId(), pList);
    }
}
