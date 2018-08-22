package com.aizhixin.cloud.dataanalysis.etl.study.controller;


import com.aizhixin.cloud.dataanalysis.etl.study.service.EtlStudyExceptionService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/etl/study/exception")
@Api(description = "修读异常ETL程序")
public class EtlStudentExceptionController {

    @Autowired
    private EtlStudyExceptionService etlStudyExceptionService;

    @GetMapping(value = "/clean/repeat/zypyjh", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(httpMethod = "GET", value = "清理重复的专业教学计划数据", response = Void.class, notes = "清理重复的专业教学计划数据<br><br><b>@author zhen.pan</b>")
    public void cleanRepeatZypyjh(@ApiParam(value = "orgId 机构id", required = true) @RequestParam(value = "orgId") Long orgId) {
        etlStudyExceptionService.cleanRepeatZypyjh();
    }
    @GetMapping(value = "/create/student/pyjh", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(httpMethod = "GET", value = "生成学生的培养计划", response = Void.class, notes = "生成学生的培养计划<br><br><b>@author zhen.pan</b>")
    public void createStudentPyjh(
            @ApiParam(value = "xn YYYY-CCCC", required = true) @RequestParam(value = "xn") String xn,
            @ApiParam(value = "xq 第一学期(秋季) 1， 第二学期(春节) 2", required = true) @RequestParam(value = "xq") String xq,
            @ApiParam(value = "orgId 机构id", required = true) @RequestParam(value = "orgId") Long orgId) {
        etlStudyExceptionService.createXnXqStudentPyjh(xn, xq, orgId);
    }
    @GetMapping(value = "/cal/student/pyjh", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(httpMethod = "GET", value = "根据成绩更新学生的培养计划", response = Void.class, notes = "根据成绩更新学生的培养计划<br><br><b>@author zhen.pan</b>")
    public void calStudentPyjh(
            @ApiParam(value = "xxdm 学校代码", required = true) @RequestParam(value = "xxdm") String xxdm,
            @ApiParam(value = "yxsh 学院") @RequestParam(value = "yxsh", required = false) String yxsh,
            @ApiParam(value = "xn YYYY-CCCC培养计划的截止日期", required = true) @RequestParam(value = "xn") String xn) {
        etlStudyExceptionService.calStudentPyjh(xxdm, yxsh, xn);
    }
}
