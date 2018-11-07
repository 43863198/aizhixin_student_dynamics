package com.aizhixin.cloud.dataanalysis.dd.rollcall.controller;


import com.aizhixin.cloud.dataanalysis.dd.rollcall.service.QuestionnaireService;
import com.aizhixin.cloud.dataanalysis.dd.rollcall.vo.QuestionnaireScreenVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/questionnaire/org/{orgId}")
@Api(description = "学生参评情况统计相关API")
public class QuestionnaireController {
    @Autowired
    private QuestionnaireService questionnaireService;

    @GetMapping(value = "/screen", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(httpMethod = "GET", value = "大屏学生参评情况统计", response = Void.class, notes = "大屏学生参评情况统计<br><br><b>@author zhen.pan</b>")
    public List<QuestionnaireScreenVO> classescourse(
            @ApiParam(value = "orgId 机构id" , required = true) @PathVariable Long orgId,
            @ApiParam(value = "roleCollgeId 登录用户的学院ID") @RequestParam(value = "roleCollgeId", required = false) Long roleCollgeId,
            @ApiParam(value = "roles 角色，多个角色使用英文逗号分隔", required = true) @RequestParam(value = "roles") String roles
    ) {
        return  questionnaireService.findStudentQuestion(orgId, roleCollgeId, roles);
    }
}
