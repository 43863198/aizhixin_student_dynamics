package com.aizhixin.cloud.dataanalysis.analysis.controller;

import com.aizhixin.cloud.dataanalysis.analysis.job.TeachingScoreAnalysisJob;
import com.aizhixin.cloud.dataanalysis.analysis.service.CetStatisticAnalysisService;
import com.aizhixin.cloud.dataanalysis.analysis.service.TeachingScoreService;
import com.aizhixin.cloud.dataanalysis.common.core.PageUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * @author: Created by jianwei.wu
 * @E-mail: wujianwei@aizhixin.com
 * @Date: 2017-12-11
 */
@RestController
@RequestMapping("/v1/teachingscore")
@Api(description = "教学成绩统计分析API")
public class TeachingScoreController {
    @Autowired
    private TeachingScoreService teachingScoreService;


    /**
     * 教学成绩———统计
     *
     * @param orgId
     * @return
     */
    @GetMapping(value = "/statistic", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(httpMethod = "GET", value = "教学成绩———统计", response = Void.class, notes = "教学成绩———统计<br><br><b>@author jianwei.wu</b>")
    public Map<String,Object> getStatisticTeachingScore(
            @ApiParam(value = "orgId 机构id" , required = true) @RequestParam(value = "orgId", required = true) Long orgId,
            @ApiParam(value = "collegeCode 学院code", required = false) @RequestParam(value = "collegeCode", required = false) String collegeCode,
            @ApiParam(value = "teacherYear 学年" , required = false) @RequestParam(value = "teacherYear", required = false) String teacherYear,
            @ApiParam(value = "semester 学期" , required = false) @RequestParam(value = "semester", required = false) String semester) {
        return teachingScoreService.getStatistic(orgId, collegeCode, teacherYear, semester);
    }

    /**
     * 教学成绩———趋势分析
     *
     * @param orgId
     * @return
     */
    @GetMapping(value = "/analysis", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(httpMethod = "GET", value = "教学成绩———趋势分析", response = Void.class, notes = "教学成绩———趋势分析<br><br><b>@author jianwei.wu</b>")
    public ResponseEntity<Map<String, Object>> getTeachingScoreTrendAnalysis(
            @ApiParam(value = "orgId 机构id" , required = true) @RequestParam(value = "orgId", required = true) Long orgId,
            @ApiParam(value = "collegeId 学院id", required = false) @RequestParam(value = "collegeId", required = false) Long collegeId,
            @ApiParam(value = "type 分析指标：1:学生人数，2:平均GPA，3:不及格人数，4:课程平均分" , required = true)
            @RequestParam(value = "type", required = true) Integer type
    ) {
        return new ResponseEntity<Map<String, Object>>(teachingScoreService.getTeachingScoreTrendAnalysis(orgId, collegeId,type), HttpStatus.OK);
    }

    /**
     * 教学成绩———详情列表
     *
     * @param orgId
     * @return
     */
    @GetMapping(value = "/detail", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(httpMethod = "GET", value = "教学成绩———详情列表", response = Void.class, notes = "教学成绩———详情列表<br><br><b>@author jianwei.wu</b>")
    public Map<String,Object> getTeachingScoreDetail(
            @ApiParam(value = "teacherYear 学年" , required = false) @RequestParam(value = "teacherYear", required = false) Integer teacherYear,
            @ApiParam(value = "semester 学期" , required = false) @RequestParam(value = "semester", required = false) Integer semester,
            @ApiParam(value = "orgId 机构id" , required = true) @RequestParam(value = "orgId", required = true) Long orgId,
            @ApiParam(value = "collegeId 学院id 注:多个时候用“，”隔开", required = false) @RequestParam(value = "collegeId", required = false) String collegeId,
            @ApiParam(value = "grade 年级 注:多个时候用“，”隔开", required = false) @RequestParam(value = "grade", required = false) String grade,
            @ApiParam(value = "nj 姓名/学号" , required = false) @RequestParam(value = "nj", required = false) String nj,
            @ApiParam(value = "professionId 专业码", required = false) @RequestParam(value = "professionId", required = false) String professionId,
            @ApiParam(value = "pageNumber 第几页") @RequestParam(value = "pageNumber", required = false) Integer pageNumber,
            @ApiParam(value = "pageSize 每页数据的数目") @RequestParam(value = "pageSize", required = false) Integer pageSize) {
        return teachingScoreService.getTeachingScoreDetail(teacherYear,semester,orgId, collegeId, grade, nj,professionId, PageUtil.createNoErrorPageRequest(pageNumber, pageSize));
    }




    /**
     * 手动修改教学成绩详情
     *
     * @param
     * @return
     */
    @GetMapping(value = "/modifydetail", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(httpMethod = "PUT", value = "手动修改教学成绩详情", response = Void.class, notes = "手动修改教学成绩详情<br><br><b>@author jianwei.wu</b>")
    public Map<String,Object> modifyTeachingScoreDetail(
            @ApiParam(value = "id 详情id" , required = true) @RequestParam(value = "id", required = true) String id,
            @ApiParam(value = "teacherYear 学年", required = true) @RequestParam(value = "teacherYear", required = true) Integer teacherYear,
            @ApiParam(value = "semester 学期", required = true) @RequestParam(value = "semester", required = true) Integer semester,
            @ApiParam(value = "grade 年级", required = true) @RequestParam(value = "grade", required = true) Integer grade,
            @ApiParam(value = "averageGPA 平均绩点", required = true) @RequestParam(value = "averageGPA", required = true) Double averageGPA,
            @ApiParam(value = "referenceSubjects 参考科目数", required = true) @RequestParam(value = "referenceSubjects", required = true) Integer referenceSubjects,
            @ApiParam(value = "failedSubjects 不及格科目", required = true) @RequestParam(value = "failedSubjects", required = true) Integer failedSubjects,
            @ApiParam(value = "failingGradeCredits 不及格科目学分", required = true) @RequestParam(value = "failingGradeCredits", required = true) Double failingGradeCredits
    ) {
        return teachingScoreService.modifyTeachingScoreDetail(id,teacherYear,semester,grade,averageGPA,referenceSubjects,failedSubjects,failingGradeCredits);
    }



}
