package com.aizhixin.cloud.dataanalysis.analysis.controller;

import com.aizhixin.cloud.dataanalysis.analysis.dto.CourseEvaluateDTO;
import com.aizhixin.cloud.dataanalysis.analysis.service.CourseEvaluateService;
import com.aizhixin.cloud.dataanalysis.analysis.service.PracticeStatisticsService;
import com.aizhixin.cloud.dataanalysis.analysis.service.TeacherEvaluateService;
import com.aizhixin.cloud.dataanalysis.common.PageData;
import com.aizhixin.cloud.dataanalysis.common.core.PageUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 * @author: Created by jianwei.wu
 * @E-mail: wujianwei@aizhixin.com
 * @Date: 2017-12-04
 */
@RestController
@RequestMapping("/v1/evaluate")
@Api(description = "教学评价API")
public class TeachingEvaluateController {
    @Autowired
    private CourseEvaluateService courseEvaluateService;
    @Autowired
    private TeacherEvaluateService teacherEvaluateService;
    /**
     * 课程评价
     * Integer pageSize, Integer pageNumber
     * @param orgId
     * @return
     */
    @GetMapping(value = "/courseevaluate", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(httpMethod = "GET", value = "课程评价", response = Void.class, notes = "课程评价<br><br><b>@author wangjun</b>")
    public PageData<CourseEvaluateDTO> getCourseEvaluate(@ApiParam(value = "orgId 机构id", required = true) @RequestParam(value = "orgId", required = true) Long orgId,
                                                         @ApiParam(value = "学期id", required = true) @RequestParam(value = "semesterId", required = true) String semesterId,
                                                         @ApiParam(value = "grade 学年" , required = false) @RequestParam(value = "grade", required = false) String grade,
                                                         @ApiParam(value = "课程名称", required = false) @RequestParam(value = "courseName", required = false) String courseName,
                                                         @ApiParam(value = "第几页", required = false) @RequestParam(value = "pageNumber", required = false) Integer pageNumber,
                                                         @ApiParam(value = "每页大小", required = false) @RequestParam(value = "pageSize", required = false) Integer pageSize,
                                                         @ApiParam(value = "排序升序：asc 降序 desc", required = true) @RequestParam(value = "sort", required = false) String sort) {
        return courseEvaluateService.getCourseEvaluate(orgId,semesterId,grade,courseName,sort,pageSize,pageNumber);
    }
    /**
     * 课程评价详情
     *
     * @param orgId
     * @return
     */
    @GetMapping(value = "/courseevaluatedetail", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(httpMethod = "GET", value = "课程评价详情", response = Void.class, notes = "课程评价详情<br><br><b>@author wangjun</b>")
    public Map<String, Object> getCourseEvaluateDetail(@ApiParam(value = "orgId 机构id", required = true) @RequestParam(value = "orgId", required = true) Long orgId,
                                                 @ApiParam(value = "学期id", required = true) @RequestParam(value = "semesterId", required = true) String semesterId,
                                                       @ApiParam(value = "grade 学年" , required = false) @RequestParam(value = "grade", required = false) String grade,
                                                 @ApiParam(value = "课程编号", required = false) @RequestParam(value = "courseCode", required = false) String courseCode,
                                                 @ApiParam(value = "教学班或授课教师名称", required = false) @RequestParam(value = "name", required = false) String name,
                                                 @ApiParam(value = "第几页", required = false) @RequestParam(value = "pageNumber", required = false) Integer pageNumber,
                                                 @ApiParam(value = "每页大小", required = false) @RequestParam(value = "pageSize", required = false) Integer pageSize) {
        return courseEvaluateService.getCourseEvaluateDetail(orgId,semesterId,grade,courseCode,name,pageNumber,pageSize);
    }

    /**
     * 教师评价
     * Integer pageSize, Integer pageNumber
     * @param orgId
     * @return
     */
    @GetMapping(value = "/teacherevaluate", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(httpMethod = "GET", value = "课程评价", response = Void.class, notes = "课程评价<br><br><b>@author wangjun</b>")
    public Map<String, Object> getTeacherEvaluate(@ApiParam(value = "orgId 机构id", required = true) @RequestParam(value = "orgId", required = true) Long orgId,
                                                 @ApiParam(value = "学期id", required = true) @RequestParam(value = "semesterId", required = true) String semesterId,
                                                 @ApiParam(value = "教师id", required = false) @RequestParam(value = "teacherId", required = false) String teacherId,
                                                 @ApiParam(value = "第几页", required = false) @RequestParam(value = "pageNumber", required = false) Integer pageNumber,
                                                 @ApiParam(value = "每页大小", required = false) @RequestParam(value = "pageSize", required = false) Integer pageSize,
                                                 @ApiParam(value = "排序升序：asc 降序 desc", required = true) @RequestParam(value = "sort", required = false) String sort) {
        return teacherEvaluateService.getTeacherEvaluate(orgId,semesterId,teacherId,sort,pageSize,pageNumber);
    }
    /**
     * 教师评价详情
     *
     * @param orgId
     * @return
     */
    @GetMapping(value = "/teacherevaluatedetail", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(httpMethod = "GET", value = "教师评价详情", response = Void.class, notes = "教师评价详情<br><br><b>@author wangjun</b>")
    public Map<String, Object> getTeacherEvaluateDetail(@ApiParam(value = "orgId 机构id", required = true) @RequestParam(value = "orgId", required = true) Long orgId,
                                                       @ApiParam(value = "学期id", required = true) @RequestParam(value = "semesterId", required = true) String semesterId,
                                                       @ApiParam(value = "教师id", required = false) @RequestParam(value = "teacherId", required = false) String teacherId,
                                                       @ApiParam(value = "教师名称", required = false) @RequestParam(value = "teacherName", required = false) String teacherName,
                                                       @ApiParam(value = "第几页", required = false) @RequestParam(value = "pageNumber", required = false) Integer pageNumber,
                                                       @ApiParam(value = "每页大小", required = false) @RequestParam(value = "pageSize", required = false) Integer pageSize) {
        return teacherEvaluateService.getTeacherEvaluateDetail(orgId,semesterId,teacherId,teacherName,pageNumber,pageSize);
    }
}
