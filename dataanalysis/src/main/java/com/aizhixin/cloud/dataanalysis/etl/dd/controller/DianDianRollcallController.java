package com.aizhixin.cloud.dataanalysis.etl.study.controller;


import com.aizhixin.cloud.dataanalysis.etl.study.service.EtlStudyExceptionService;
import com.aizhixin.cloud.dataanalysis.etl.study.service.StudyExceptionIndexService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/etl/study/exception")
@Api(description = "修读异常ETL程序")
public class EtlStudyExceptionController {

    @Autowired
    private EtlStudyExceptionService etlStudyExceptionService;
    @Autowired
    private StudyExceptionIndexService studyExceptionIndexService;

    @PutMapping(value = "/clean/repeat/zypyjh", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(httpMethod = "PUT", value = "清理重复的专业教学计划数据", response = Void.class, notes = "清理重复的专业教学计划数据<br><br><b>@author zhen.pan</b>")
    public void cleanRepeatZypyjh(@ApiParam(value = "orgId 机构id", required = true) @RequestParam(value = "orgId") Long orgId) {
        etlStudyExceptionService.cleanRepeatZypyjh();
    }
    @PutMapping(value = "/create/student/pyjh", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(httpMethod = "PUT", value = "生成学生的培养计划", response = Void.class, notes = "生成学生的培养计划<br><br><b>@author zhen.pan</b>")
    public void createStudentPyjh(
            @ApiParam(value = "xn YYYY-CCCC", required = true) @RequestParam(value = "xn") String xn,
            @ApiParam(value = "xq 第一学期(秋季) 1， 第二学期(春节) 2", required = true) @RequestParam(value = "xq") String xq,
            @ApiParam(value = "orgId 机构id", required = true) @RequestParam(value = "orgId") Long orgId) {
        etlStudyExceptionService.createXnXqStudentPyjh(xn, xq, orgId);
    }
    @PutMapping(value = "/cal/student/pyjh", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(httpMethod = "PUT", value = "根据成绩更新学生的培养计划", response = Void.class, notes = "根据成绩更新学生的培养计划<br><br><b>@author zhen.pan</b>")
    public void calStudentPyjh(
            @ApiParam(value = "xxdm 学校代码", required = true) @RequestParam(value = "xxdm") String xxdm,
            @ApiParam(value = "yxsh 学院") @RequestParam(value = "yxsh", required = false) String yxsh,
            @ApiParam(value = "xnxq YYYY-CCCC-xq培养计划的截止学年学期", required = true) @RequestParam(value = "xnxq") String xnxq) {
        etlStudyExceptionService.calStudentPyjh(xxdm, yxsh, xnxq);
    }

    @PutMapping(value = "/index", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(httpMethod = "PUT", value = "计算当前时间对于学期的修读异常指标", response = Void.class, notes = "计算当前时间对于学期的修读异常指标<br><br><b>@author zhen.pan</b>")
    public void studyException(@ApiParam(value = "orgId 机构id" , required = true) @RequestParam(value = "orgId") Long orgId) {
        studyExceptionIndexService.calCurrentDateIndex(orgId);
    }
}
