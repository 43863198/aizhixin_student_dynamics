package com.aizhixin.cloud.dataanalysis.dd.rollcall.service;


import com.aizhixin.cloud.dataanalysis.common.RoleTool;
import com.aizhixin.cloud.dataanalysis.common.dto.OrgCollegeIdDTO;
import com.aizhixin.cloud.dataanalysis.dd.rollcall.manager.QuestionnaireManager;
import com.aizhixin.cloud.dataanalysis.dd.rollcall.vo.QuestionnaireScreenVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class QuestionnaireService {
    @Autowired
    private QuestionnaireManager questionnaireManager;

    public List<QuestionnaireScreenVO> findStudentQuestion(Long orgId, Long collegeId, String roles) {
        OrgCollegeIdDTO dto = RoleTool.orgAndCollegeResetByRole(orgId, collegeId, roles);
        return  questionnaireManager.findStudentQuestion(dto.getOrgId(), dto.getCollegeId());
    }
}
