package com.aizhixin.cloud.dataanalysis.common.v1.controller;


import com.aizhixin.cloud.dataanalysis.common.service.BaseDataService;
import com.aizhixin.cloud.dataanalysis.feign.vo.CollegeVO;
import com.aizhixin.cloud.dataanalysis.feign.vo.TeacherVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/base")
@Api(description = "基础数据平台基础数据获取API")
public class BaseDataController {
    private BaseDataService baseDataService;
    @Autowired
    public BaseDataController (BaseDataService baseDataService) {
        this.baseDataService = baseDataService;
    }

    @GetMapping(value = "/org/{orgId}/college", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(httpMethod = "GET", value = "查询学校的所有学院信息", response = Void.class, notes = "查询学校的所有学院信息<br><br><b>@author zhen.pan</b>")
    public List<CollegeVO> queryCollege(
            @ApiParam(value = "orgId 学校ID", required = true) @PathVariable Long orgId) {
        return baseDataService.queryAllCollege(orgId);
    }

    @GetMapping(value = "/org/{orgId}/college/{collegeId}/teacher", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(httpMethod = "GET", value = "查询学校的老师信息", response = Void.class, notes = "查询学校的老师信息<br><br><b>@author zhen.pan</b>")
    public List<TeacherVO> queryTeacher(
            @ApiParam(value = "orgId 学校ID", required = true) @PathVariable Long orgId,
            @ApiParam(value = "collegeId 学院ID", required = true) @PathVariable Long collegeId,
            @ApiParam(value = "name 老师姓名或工号", required = true) @RequestParam(value = "name") String name) {
        return baseDataService.queryTeacher(orgId, collegeId, name);
    }
}
