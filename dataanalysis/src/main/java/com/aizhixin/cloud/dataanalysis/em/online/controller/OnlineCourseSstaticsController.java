package com.aizhixin.cloud.dataanalysis.em.online.controller;


import com.aizhixin.cloud.dataanalysis.em.online.service.OnlineCourseStaticsService;
import com.aizhixin.cloud.dataanalysis.em.online.vo.ScreenOnlineCourseVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/online/org/{orgId}")
@Api(description = "在线课程统计相关API")
public class OnlineCourseSstaticsController {
    @Autowired
    private OnlineCourseStaticsService onlineCourseStaticsService;

    @GetMapping(value = "/course", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(httpMethod = "GET", value = "在线课程统计大屏指标", response = Void.class, notes = "在线课程统计大屏指标<br><br><b>@author zhen.pan</b>")
    public ScreenOnlineCourseVO classescourse(
            @ApiParam(value = "orgId 机构id" , required = true) @PathVariable Long orgId,
            @ApiParam(value = "roleCollgeId 登录用户的学院ID") @RequestParam(value = "roleCollgeId", required = false) Long roleCollgeId,
            @ApiParam(value = "roles 角色，多个角色使用英文逗号分隔", required = true) @RequestParam(value = "roles") String roles
    ) {
         return onlineCourseStaticsService.testOnlineCourse(orgId, roleCollgeId);
    }
}
