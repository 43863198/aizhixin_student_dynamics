package com.aizhixin.cloud.dataanalysis.etl.score.controller;


import com.aizhixin.cloud.dataanalysis.bz.service.StandardScoreService;
import com.aizhixin.cloud.dataanalysis.etl.score.service.StandardScoreSemesterIndexService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/etl/score")
@Api(description = "学生成绩ETL程序")
public class EtlScoreController {

    @Autowired
    private StandardScoreService standardScoreService;
    @Autowired
    private StandardScoreSemesterIndexService standardScoreSemesterIndexService;


    @PutMapping(value = "/etl", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(httpMethod = "PUT", value = "学生成绩数据库对数据库标准数据清洗ETL", response = Void.class, notes = "学生成绩数据库对数据库标准数据清洗ETL<br><br><b>@author zhen.pan</b>")
    public void etlScore(@ApiParam(value = "orgId 机构id" , required = true) @RequestParam(value = "orgId") Long orgId) {
        standardScoreService.etlDB2DB(orgId);
    }

    @PutMapping(value = "/etlxnxq", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(httpMethod = "PUT", value = "学生成绩数据库对数据库标准数据清洗ETL", response = Void.class, notes = "学生成绩数据库对数据库标准数据清洗ETL<br><br><b>@author zhen.pan</b>")
    public void etlScore(@ApiParam(value = "orgId 机构id" , required = true) @RequestParam(value = "orgId") Long orgId,
                         @ApiParam(value = "xn 学年" , required = true) @RequestParam(value = "xn") String xn,
                         @ApiParam(value = "xq 学期" , required = true) @RequestParam(value = "xq") String xq) {
        standardScoreService.etlDB2DB(orgId, xn, xq);
    }

    @PutMapping(value = "/index", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(httpMethod = "PUT", value = "学生成绩指标的计算", response = Void.class, notes = "学生成绩指标的计算<br><br><b>@author zhen.pan</b>")
    public void score(@ApiParam(value = "orgId 机构id" , required = true) @RequestParam(value = "orgId") Long orgId) {
        standardScoreSemesterIndexService.schoolStudentScoreIndex(orgId);
    }

    @PutMapping(value = "/index/semester", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(httpMethod = "PUT", value = "学生成绩学期对应数据学生指标的生成", response = Void.class, notes = "学生成绩学期对应数据学生指标的生成<br><br><b>@author zhen.pan</b>")
    public void score(@ApiParam(value = "xxdm 机构代码" , required = true) @RequestParam(value = "xxdm") String xxdm,
                      @ApiParam(value = "xn 学年" , required = true) @RequestParam(value = "xn") String xn,
                      @ApiParam(value = "xq 学期" , required = true) @RequestParam(value = "xq") String xq) {
        standardScoreSemesterIndexService.oneSemesterStudentScoreIndex(xxdm, xn, xq);
    }

    @PutMapping(value = "/index/all", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(httpMethod = "PUT", value = "学生成绩所有学期对应数据学生指标的生成", response = Void.class, notes = "学生成绩所有学期对应数据学生指标的生成<br><br><b>@author zhen.pan</b>")
    public void scoreAll(@ApiParam(value = "xxdm 机构代码" , required = true) @RequestParam(value = "xxdm") String xxdm) {
        standardScoreSemesterIndexService.allSemesterStudentScoreIndex(xxdm);
    }
}
