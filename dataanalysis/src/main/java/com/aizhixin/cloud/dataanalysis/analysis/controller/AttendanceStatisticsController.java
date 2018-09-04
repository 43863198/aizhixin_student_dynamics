package com.aizhixin.cloud.dataanalysis.analysis.controller;

import com.aizhixin.cloud.dataanalysis.analysis.domain.AttendanceStatisticsCourseDomain;
import com.aizhixin.cloud.dataanalysis.analysis.domain.AttendanceStatisticsTeacherDomain;
import com.aizhixin.cloud.dataanalysis.analysis.domain.AttendanceStatisticsUnitDomain;
import com.aizhixin.cloud.dataanalysis.analysis.service.AttendanceStatisticsService;
import com.aizhixin.cloud.dataanalysis.analysis.vo.AttendanceStatisticsVO;
import com.aizhixin.cloud.dataanalysis.common.PageData;
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

@RestController
@RequestMapping("/v1/attendanceanalysis")
@Api(description = "考勤学情分析")
public class AttendanceStatisticsController {
    @Autowired
    private AttendanceStatisticsService attendanceStatisticsService;

    @GetMapping(value = "/statisticsbyunit", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(httpMethod = "GET", value = "按单位统计考勤数据", response = Void.class, notes = "按单位统计考勤数据<br><br><b>@author dengchao</b>")
    public AttendanceStatisticsVO listByUnit(@ApiParam(value = "orgId 机构id", required = true) @RequestParam(value = "orgId") Long orgId,
                                             @ApiParam(value = "collegeCode 学院code") @RequestParam(value = "collegeCode", required = false) String collegeCode,
                                             @ApiParam(value = "professionCode 专业code") @RequestParam(value = "professionCode", required = false) String professionCode,
                                             @ApiParam(value = "dateRange 时间区间(2015-08-19~2015-09-17:2015年8月19日至2015年9月17日)", required = true) @RequestParam(value = "dateRange") String dateRange) {
        {
            return attendanceStatisticsService.statisticsByUnit(orgId, collegeCode, professionCode, dateRange);
        }

    }

    @GetMapping(value = "/statisticsbycourse", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(httpMethod = "GET", value = "按课程统计考勤数据", response = Void.class, notes = "按课程统计考勤数据<br><br><b>@author dengchao</b>")
    public PageData<AttendanceStatisticsCourseDomain> listByCourse(@ApiParam(value = "orgId 机构id", required = true) @RequestParam(value = "orgId") Long orgId,
                                                                   @ApiParam(value = "kchOrkcmc 课程号或课程名称") @RequestParam(value = "kchOrkcmc", required = false) String kchOrkcmc,
                                                                   @ApiParam(value = "dateRange 时间区间(2015-08-19~2015-09-17:2015年8月19日至2015年9月17日)", required = true) @RequestParam(value = "dateRange") String dateRange,
                                                                   @ApiParam(value = "pageNumber 第几页") @RequestParam(value = "pageNumber", required = false) Integer pageNumber,
                                                                   @ApiParam(value = "pageSize 每页数据的数目") @RequestParam(value = "pageSize", required = false) Integer pageSize) {
        {
            return attendanceStatisticsService.statisticsByCourse(orgId, kchOrkcmc, dateRange, pageNumber, pageSize);
        }

    }

    @GetMapping(value = "/statisticsbyTeacger", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(httpMethod = "GET", value = "按教师统计考勤数据", response = Void.class, notes = "按教师统计考勤数据<br><br><b>@author dengchao</b>")
    public PageData<AttendanceStatisticsTeacherDomain> listByTeacher(@ApiParam(value = "orgId 机构id", required = true) @RequestParam(value = "orgId") Long orgId,
                                                                     @ApiParam(value = "jsghOrjsmc 教师工号或教师姓名") @RequestParam(value = "jsghOrjsmc", required = false) String jsghOrjsmc,
                                                                     @ApiParam(value = "dateRange 时间区间(2015-08-19~2015-09-17:2015年8月19日至2015年9月17日)", required = true) @RequestParam(value = "dateRange") String dateRange,
                                                                     @ApiParam(value = "pageNumber 第几页") @RequestParam(value = "pageNumber", required = false) Integer pageNumber,
                                                                     @ApiParam(value = "pageSize 每页数据的数目") @RequestParam(value = "pageSize", required = false) Integer pageSize) {
        {
            return attendanceStatisticsService.statisticsByTeacher(orgId, jsghOrjsmc, dateRange, pageNumber, pageSize);
        }

    }

    @GetMapping(value = "/exportbyunit", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(httpMethod = "GET", value = "按单位导出考勤数据", response = Void.class, notes = "按单位导出考勤数据<br><br><b>@author dengchao</b>")
    public List<AttendanceStatisticsUnitDomain> exportDateByUnit(@ApiParam(value = "orgId 机构id", required = true) @RequestParam(value = "orgId") Long orgId,
                                                                 @ApiParam(value = "collegeCode 学院code") @RequestParam(value = "collegeCode", required = false) String collegeCode,
                                                                 @ApiParam(value = "professionCode 专业code") @RequestParam(value = "professionCode", required = false) String professionCode,
                                                                 @ApiParam(value = "dateRange 时间区间(2015-08-19~2015-09-17:2015年8月19日至2015年9月17日)", required = true) @RequestParam(value = "dateRange") String dateRange) {
        {
            return attendanceStatisticsService.exportDateByUnit(orgId, collegeCode, professionCode, dateRange);
        }

    }

    @GetMapping(value = "/exportbycourse", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(httpMethod = "GET", value = "按课程导出考勤数据", response = Void.class, notes = "按课程导出考勤数据<br><br><b>@author dengchao</b>")
    public List<AttendanceStatisticsCourseDomain> exportDateByCourse(@ApiParam(value = "orgId 机构id", required = true) @RequestParam(value = "orgId") Long orgId,
                                                                     @ApiParam(value = "kchOrkcmc 课程号或课程名称") @RequestParam(value = "kchOrkcmc", required = false) String kchOrkcmc,
                                                                     @ApiParam(value = "dateRange 时间区间(2015-08-19~2015-09-17:2015年8月19日至2015年9月17日)", required = true) @RequestParam(value = "dateRange") String dateRange) {
        return attendanceStatisticsService.exportDateByCourse(orgId, kchOrkcmc, dateRange);
    }

    @GetMapping(value = "/exportbyteacher", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(httpMethod = "GET", value = "按教师导出考勤数据", response = Void.class, notes = "按教师导出考勤数据<br><br><b>@author dengchao</b>")
    public List<AttendanceStatisticsTeacherDomain> exportByTeacher(@ApiParam(value = "orgId 机构id", required = true) @RequestParam(value = "orgId") Long orgId,
                                                                   @ApiParam(value = "jsghOrjsmc 教师工号或教师姓名") @RequestParam(value = "kchOrkcmc", required = false) String jsghOrjsmc,
                                                                   @ApiParam(value = "dateRange 时间区间(2015-08-19~2015-09-17:2015年8月19日至2015年9月17日)", required = true) @RequestParam(value = "dateRange") String dateRange) {
        return attendanceStatisticsService.exportByTeacher(orgId, jsghOrjsmc, dateRange);
    }

}


