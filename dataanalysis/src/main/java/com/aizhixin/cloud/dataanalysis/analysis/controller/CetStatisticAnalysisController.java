package com.aizhixin.cloud.dataanalysis.analysis.controller;

import com.aizhixin.cloud.dataanalysis.analysis.job.CetStatisticsAnalysisJob;
import com.aizhixin.cloud.dataanalysis.analysis.service.CetStatisticAnalysisService;
import com.aizhixin.cloud.dataanalysis.analysis.service.OverYearsTestStatisticsService;
import com.aizhixin.cloud.dataanalysis.common.core.PageUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * @author: Created by jianwei.wu
 * @E-mail: wujianwei@aizhixin.com
 * @Date: 2017-12-07
 */
@RestController
@RequestMapping("/v1/cet")
@Api(description = "英语四六级统计分析API")
public class CetStatisticAnalysisController {
    @Autowired
    private CetStatisticAnalysisService cetStatisticAnalysisService;
    @Autowired
    private CetStatisticsAnalysisJob cetStatisticsAnalysisJob;
    @Autowired
    private OverYearsTestStatisticsService overYearsTestStatisticsService;

    /**
     * cet———统计
     *
     * @param orgId
     * @return
     */
    @GetMapping(value = "/statistic", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(httpMethod = "GET", value = "cet———统计", response = Void.class, notes = "cet———统计<br><br><b>@author jianwei.wu</b>")
    public Map<String,Object> getStatisticCet(
            @ApiParam(value = "orgId 机构id" , required = true) @RequestParam(value = "orgId", required = true) Long orgId,
            @ApiParam(value = "teacherYear 学年" , required = true) @RequestParam(value = "teacherYear", required = true) Integer teacherYear,
            @ApiParam(value = "semester 学期" , required = true) @RequestParam(value = "semester", required = true) Integer semester
    ) {
        return cetStatisticAnalysisService.getStatistic(orgId, teacherYear, semester);
    }

    /**
     * cet———趋势分析
     *
     * @param orgId
     * @return
     */
    @GetMapping(value = "/analysis", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(httpMethod = "GET", value = "cet———趋势分析", response = Void.class, notes = "cet———趋势分析<br><br><b>@author jianwei.wu</b>")
    public ResponseEntity<Map<String, Object>> getCetTrendAnalysis(
            @ApiParam(value = "orgId 机构id" , required = true) @RequestParam(value = "orgId", required = true) Long orgId,
            @ApiParam(value = "collegeId 机构id", required = false) @RequestParam(value = "collegeId", required = false) Long collegeId
//            @ApiParam(value = "type 分析指标：4:四级通过率，6:六级通过率" , required = true
//            @RequestParam(value = "type", required = true) Integer type
    ) {
        return new ResponseEntity<Map<String, Object>>(cetStatisticAnalysisService.getCetTrendAnalysis(orgId, collegeId), HttpStatus.OK);
    }

    /**
     * cet———详情列表
     *
     * @param orgId
     * @return
     */
    @GetMapping(value = "/detail", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(httpMethod = "GET", value = "cet———详情列表", response = Void.class, notes = "cet———详情列表<br><br><b>@author jianwei.wu</b>")
    public Map<String,Object> getCetDetail(
            @ApiParam(value = "teacherYear 学年" , required = true) @RequestParam(value = "teacherYear", required = true) Integer teacherYear,
            @ApiParam(value = "semester 学期" , required = true) @RequestParam(value = "semester", required = true) Integer semester,
            @ApiParam(value = "orgId 机构id" , required = true) @RequestParam(value = "orgId", required = true) Long orgId,
            @ApiParam(value = "collegeId 机构id 注:多个时候用“，”隔开", required = false) @RequestParam(value = "collegeId", required = false) String collegeId,
            @ApiParam(value = "grade 年级 注:多个时候用“，”隔开", required = false) @RequestParam(value = "grade", required = false) String grade,
            @ApiParam(value = "type 成绩类型：4:四级通过率，6:六级通过率" , required = false) @RequestParam(value = "type", required = false) Integer type,
            @ApiParam(value = "nj 姓名/学号" , required = false) @RequestParam(value = "nj", required = false) String nj,
            @ApiParam(value = "pageNumber 第几页") @RequestParam(value = "pageNumber", required = false) Integer pageNumber,
            @ApiParam(value = "pageSize 每页数据的数目") @RequestParam(value = "pageSize", required = false) Integer pageSize) {
        return cetStatisticAnalysisService.getCetDetail(orgId, collegeId,teacherYear,semester, grade, type, nj, PageUtil.createNoErrorPageRequest(pageNumber, pageSize));
    }

    /**
     * 历年四六级均值
     * @param orgId
     * @return
     */
    @GetMapping(value = "/yearavg", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(httpMethod = "GET", value = "历年四六级均值", response = Void.class, notes = "历年四六级均值<br><br><b>@author jianwei.wu</b>")
    public ResponseEntity<Map<String, Object>> getYearAvg(
            @ApiParam(value = "orgId 学校id" , required = true) @RequestParam(value = "orgId", required = true) Long orgId
          ) {
        return new ResponseEntity<Map<String, Object>>(cetStatisticAnalysisService.getYearAvg(orgId),HttpStatus.OK);
    }

    /**
     * 英语数据分析---数据统计
     */
    @GetMapping(value = "/cetstatistic", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(httpMethod = "GET", value = "英语统计", response = Void.class, notes = "英语统计<br><br><b>@author jianwei.wu</b>")
    public void getYearAvg() {
        cetStatisticsAnalysisJob.cetScoreStatistics();
//        cetStatisticsAnalysisJob.cetScoreStatisticsTop();
    }


    /**
     * 英语考试单次数据分析---数据统计
     * @param orgId
     * @param cetType
     * @param teacherYear
     * @param semester
     * @param collegeNumber
     * @param professionNumber
     * @param classNumber
     * @return
     */
    @GetMapping(value = "/cetstatistics", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(httpMethod = "GET", value = "英语考试单次数据分析---数据统计", response = Void.class, notes = "英语考试单次数据分析---数据统计<br><br><b>@author jianwei.wu</b>")
    public ResponseEntity<Map<String, Object>> cetSingleDataStatistics(
            @ApiParam(value = "orgId 学校id" , required = true) @RequestParam(value = "orgId", required = true) Long orgId,
            @ApiParam(value = "cetType 成绩类型： (四级;六级;)" , required = true ) @RequestParam(value = "cetType" , required = true) String cetType,
            @ApiParam(value = "teacherYear 学年 (格式如：2017-01-01)" , required = true ) @RequestParam(value = "teacherYear" , required = true) String teacherYear,
            @ApiParam(value = "semester 学期 (春;秋;)" , required = true) @RequestParam(value = "semester" , required = true) String semester,
            @ApiParam(value = "collegeNumber 学院码", required = false) @RequestParam(value = "collegeNumber", required = false) String collegeNumber,
            @ApiParam(value = "professionNumber 专业码", required = false) @RequestParam(value = "professionNumber", required = false) String professionNumber,
            @ApiParam(value = "classNumber 班号", required = false) @RequestParam(value = "classNumber", required = false) String classNumber,
            @ApiParam(value = "className 班名", required = false) @RequestParam(value = "className", required = false) String className
    ) {
        return new ResponseEntity<Map<String, Object>>(cetStatisticAnalysisService.cetSingleDataStatistics(orgId, teacherYear, semester, collegeNumber, professionNumber, classNumber,className, cetType),HttpStatus.OK);
    }

    /**
     * 英语考试单次数据分析---均值分布---按行政单位
     * @param orgId
     * @param cetType
     * @param teacherYear
     * @param semester
     * @param collegeNumber
     * @param professionNumber
     * @param classNumber
     * @return
     */
    @GetMapping(value = "/avg", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(httpMethod = "GET", value = "英语考试单次数据分析---均值分布---按行政单位", response = Void.class, notes = "英语考试单次数据分析---均值分布---按行政单位<br><br><b>@author jianwei.wu</b>")
    public ResponseEntity<Map<String, Object>> cetSingleDataAvgScoure(
            @ApiParam(value = "orgId 学校id" , required = true) @RequestParam(value = "orgId", required = true) Long orgId,
            @ApiParam(value = "cetType 成绩类型： (四级;六级;)" , required = true ) @RequestParam(value = "cetType" , required = true) String cetType,
            @ApiParam(value = "teacherYear 学年" , required = true ) @RequestParam(value = "teacherYear" , required = true) String teacherYear,
            @ApiParam(value = "semester 学期 (春;秋;)" , required = true) @RequestParam(value = "semester" , required = true) String semester,
            @ApiParam(value = "collegeNumber 学院码", required = false) @RequestParam(value = "collegeNumber", required = false) String collegeNumber,
            @ApiParam(value = "professionNumber 专业码", required = false) @RequestParam(value = "professionNumber", required = false) String professionNumber,
            @ApiParam(value = "classNumber 班号", required = false) @RequestParam(value = "classNumber", required = false) String classNumber,
            @ApiParam(value = "className 班名", required = false) @RequestParam(value = "className", required = false) String className
    ) {
        return new ResponseEntity<Map<String, Object>>(cetStatisticAnalysisService.cetSingleDataAvgScoure(orgId, teacherYear, semester, collegeNumber, professionNumber, classNumber, className, cetType), HttpStatus.OK);
    }

    /**
     * 英语考试单次数据分析---均值分布--按男女
     * @param orgId
     * @param cetType
     * @param teacherYear
     * @param semester
     * @param collegeNumber
     * @param professionNumber
     * @param classNumber
     * @return
     */
    @GetMapping(value = "/sexavg", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(httpMethod = "GET", value = "英语考试单次数据分析---均值分布---按男女", response = Void.class, notes = "英语考试单次数据分析---均值分布---按男女<br><br><b>@author jianwei.wu</b>")
    public ResponseEntity<Map<String, Object>> cetSingleDataSexAvgScoure(
            @ApiParam(value = "orgId 学校id" , required = true) @RequestParam(value = "orgId", required = true) Long orgId,
            @ApiParam(value = "cetType 成绩类型： (四级;六级;)" , required = true ) @RequestParam(value = "cetType" , required = true) String cetType,
            @ApiParam(value = "teacherYear 学年" , required = true ) @RequestParam(value = "teacherYear" , required = true) String teacherYear,
            @ApiParam(value = "semester 学期 (春;秋;)" , required = true) @RequestParam(value = "semester" , required = true) String semester,
            @ApiParam(value = "collegeNumber 学院码", required = false) @RequestParam(value = "collegeNumber", required = false) String collegeNumber,
            @ApiParam(value = "professionNumber 专业码", required = false) @RequestParam(value = "professionNumber", required = false) String professionNumber,
            @ApiParam(value = "classNumber 班号", required = false) @RequestParam(value = "classNumber", required = false) String classNumber,
            @ApiParam(value = "className 班名", required = false) @RequestParam(value = "className", required = false) String className
    ) {
        return new ResponseEntity<Map<String, Object>>(cetStatisticAnalysisService.cetSingleDataSexAvgScoure(orgId, teacherYear, semester, collegeNumber, professionNumber, classNumber, className, cetType), HttpStatus.OK);
    }


    /**
     * 英语考试单次数据分析---均值分布--按年级
     * @param orgId
     * @param cetType
     * @param teacherYear
     * @param semester
     * @param collegeNumber
     * @param professionNumber
     * @param classNumber
     * @return
     */
    @GetMapping(value = "/gradeavg", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(httpMethod = "GET", value = "英语考试单次数据分析---均值分布---按年级", response = Void.class, notes = "英语考试单次数据分析---均值分布---按年级<br><br><b>@author jianwei.wu</b>")
    public ResponseEntity<Map<String, Object>> cetSingleDataGradeAvgScoure(
            @ApiParam(value = "orgId 学校id" , required = true) @RequestParam(value = "orgId", required = true) Long orgId,
            @ApiParam(value = "cetType 成绩类型： (四级;六级;)" , required = true ) @RequestParam(value = "cetType" , required = true) String cetType,
            @ApiParam(value = "teacherYear 学年" , required = true ) @RequestParam(value = "teacherYear" , required = true) String teacherYear,
            @ApiParam(value = "semester 学期 (春;秋;)" , required = true) @RequestParam(value = "semester" , required = true) String semester,
            @ApiParam(value = "collegeNumber 学院码", required = false) @RequestParam(value = "collegeNumber", required = false) String collegeNumber,
            @ApiParam(value = "professionNumber 专业码", required = false) @RequestParam(value = "professionNumber", required = false) String professionNumber,
            @ApiParam(value = "classNumber 班号", required = false) @RequestParam(value = "classNumber", required = false) String classNumber,
            @ApiParam(value = "className 班名", required = false) @RequestParam(value = "className", required = false) String className
    ) {
        return new ResponseEntity<Map<String, Object>>(cetStatisticAnalysisService.cetSingleDataGradeAvgScoure(orgId, teacherYear, semester, collegeNumber, professionNumber, classNumber, className, cetType), HttpStatus.OK);
    }


    /**
     * 英语考试单次数据分析---人数分布--按行政单位
     * @param orgId
     * @param cetType
     * @param teacherYear
     * @param semester
     * @param collegeNumber
     * @param professionNumber
     * @param classNumber
     * @return
     */
    @GetMapping(value = "/numberofpeople", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(httpMethod = "GET", value = "英语考试单次数据分析---人数分布---按行政单位", response = Void.class, notes = "英语考试单次数据分析---人数分布---按行政单位<br><br><b>@author jianwei.wu</b>")
    public ResponseEntity<Map<String, Object>> cetSingleDataNumberOfPeople(
            @ApiParam(value = "orgId 学校id" , required = true) @RequestParam(value = "orgId", required = true) Long orgId,
            @ApiParam(value = "cetType 成绩类型： (四级;六级;)" , required = true ) @RequestParam(value = "cetType" , required = true) String cetType,
            @ApiParam(value = "teacherYear 学年" , required = true ) @RequestParam(value = "teacherYear" , required = true) String teacherYear,
            @ApiParam(value = "semester 学期 (春;秋;)" , required = true) @RequestParam(value = "semester" , required = true) String semester,
            @ApiParam(value = "collegeNumber 学院码", required = false) @RequestParam(value = "collegeNumber", required = false) String collegeNumber,
            @ApiParam(value = "professionNumber 专业码", required = false) @RequestParam(value = "professionNumber", required = false) String professionNumber,
            @ApiParam(value = "classNumber 班号", required = false) @RequestParam(value = "classNumber", required = false) String classNumber,
            @ApiParam(value = "className 班名", required = false) @RequestParam(value = "className", required = false) String className
    ) {
        return new ResponseEntity<Map<String, Object>>(cetStatisticAnalysisService.cetSingleDataNumberOfPeople(orgId, teacherYear, semester, collegeNumber, professionNumber, classNumber, className, cetType), HttpStatus.OK);
    }


    /**
     * 英语考试单次数据分析---人数分布--按性别
     * @param orgId
     * @param cetType
     * @param teacherYear
     * @param semester
     * @param collegeNumber
     * @param professionNumber
     * @param classNumber
     * @param grade
     * @return
     */
    @GetMapping(value = "/sexnumberofpeople", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(httpMethod = "GET", value = "英语考试单次数据分析---人数分布---按性别", response = Void.class, notes = "英语考试单次数据分析---人数分布---按性别<br><br><b>@author jianwei.wu</b>")
    public ResponseEntity<Map<String, Object>> cetSingleDataSexNumberOfPeople(
            @ApiParam(value = "orgId 学校id" , required = true) @RequestParam(value = "orgId", required = true) Long orgId,
            @ApiParam(value = "cetType 成绩类型： (四级;六级;)" , required = true ) @RequestParam(value = "cetType" , required = true) String cetType,
            @ApiParam(value = "teacherYear 学年" , required = true ) @RequestParam(value = "teacherYear" , required = true) String teacherYear,
            @ApiParam(value = "semester 学期 (春;秋;)" , required = true) @RequestParam(value = "semester" , required = true) String semester,
            @ApiParam(value = "collegeNumber 学院码", required = false) @RequestParam(value = "collegeNumber", required = false) String collegeNumber,
            @ApiParam(value = "professionNumber 专业码", required = false) @RequestParam(value = "professionNumber", required = false) String professionNumber,
            @ApiParam(value = "classNumber 班号", required = false) @RequestParam(value = "classNumber", required = false) String classNumber,
            @ApiParam(value = "grade 年级", required = false) @RequestParam(value = "grade", required = false) String grade,
            @ApiParam(value = "className 班名", required = false) @RequestParam(value = "className", required = false) String className
    ) {
        return new ResponseEntity<Map<String, Object>>(cetStatisticAnalysisService.cetSingleDataSexNumberOfPeople(orgId, teacherYear, semester, collegeNumber, professionNumber, classNumber, className, grade, cetType), HttpStatus.OK);
    }

    /**
     * 英语考试单次数据分析---人数分布--按年级
     * @param orgId
     * @param cetType
     * @param teacherYear
     * @param semester
     * @param collegeNumber
     * @param professionNumber
     * @param classNumber
     * @param grade
     * @return
     */
    @GetMapping(value = "/gradenumberofpeople", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(httpMethod = "GET", value = "英语考试单次数据分析---人数分布---按年级", response = Void.class, notes = "英语考试单次数据分析---人数分布---按年级<br><br><b>@author jianwei.wu</b>")
    public ResponseEntity<Map<String, Object>> cetSingleDataGradeNumberOfPeople(
            @ApiParam(value = "orgId 学校id" , required = true) @RequestParam(value = "orgId", required = true) Long orgId,
            @ApiParam(value = "cetType 成绩类型： (四级;六级;)" , required = true ) @RequestParam(value = "cetType" , required = true) String cetType,
            @ApiParam(value = "teacherYear 学年" , required = true ) @RequestParam(value = "teacherYear" , required = true) String teacherYear,
            @ApiParam(value = "semester 学期 (春;秋;)" , required = true) @RequestParam(value = "semester" , required = true) String semester,
            @ApiParam(value = "collegeNumber 学院码", required = false) @RequestParam(value = "collegeNumber", required = false) String collegeNumber,
            @ApiParam(value = "professionNumber 专业码", required = false) @RequestParam(value = "professionNumber", required = false) String professionNumber,
            @ApiParam(value = "classNumber 班号", required = false) @RequestParam(value = "classNumber", required = false) String classNumber,
            @ApiParam(value = "grade 年级", required = false) @RequestParam(value = "grade", required = false) String grade,
            @ApiParam(value = "className 班名", required = false) @RequestParam(value = "className", required = false) String className
    ) {
        return new ResponseEntity<Map<String, Object>>(cetStatisticAnalysisService.cetSingleDataGradeNumberOfPeople(orgId, teacherYear, semester, collegeNumber, professionNumber, classNumber, className, grade, cetType), HttpStatus.OK);
    }


    /**
     * 英语考试单次数据分析---top10
     * @param orgId
     * @param cetType
     * @param teacherYear
     * @param semester
     * @return
     */
    @GetMapping(value = "/top", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(httpMethod = "GET", value = "英语考试单次数据分析---top10", response = Void.class, notes = "英语考试单次数据分析---top10<br><br><b>@author jianwei.wu</b>")
    public ResponseEntity<Map<String, Object>> getTop(
            @ApiParam(value = "orgId 学校id" , required = true) @RequestParam(value = "orgId", required = true) Long orgId,
            @ApiParam(value = "cetType 成绩类型： (四级;六级;)" , required = true ) @RequestParam(value = "cetType" , required = true) String cetType,
            @ApiParam(value = "teacherYear 学年" , required = true ) @RequestParam(value = "teacherYear" , required = true) String teacherYear,
            @ApiParam(value = "semester 学期 (春;秋;)" , required = true) @RequestParam(value = "semester" , required = true) String semester
    ) {
        return new ResponseEntity<Map<String, Object>>(cetStatisticAnalysisService.getTop(orgId, teacherYear, semester, cetType), HttpStatus.OK);
    }

    /**
     * 英语考试当前状况---数据统计
     * @param orgId
     * @param cetType
     * @param collegeNumber
     * @param professionNumber
     * @param classNumber
     * @return
     */

    @GetMapping(value = "/currentstatistics", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(httpMethod = "GET", value = "英语考试当前状况---数据统计", response = Void.class, notes = "英语考试当前状况---数据统计<br><br><b>@author jianwei.wu</b>")
    public ResponseEntity<Map<String, Object>> currentStatistics(
            @ApiParam(value = "orgId 学校id" , required = true) @RequestParam(value = "orgId", required = true) Long orgId,
            @ApiParam(value = "cetType 成绩类型： (四级;六级;)" , required = true ) @RequestParam(value = "cetType" , required = true) String cetType,
            @ApiParam(value = "collegeNumber 学院码", required = false) @RequestParam(value = "collegeNumber", required = false) String collegeNumber,
            @ApiParam(value = "professionNumber 专业码", required = false) @RequestParam(value = "professionNumber", required = false) String professionNumber,
            @ApiParam(value = "classNumber 班号", required = false) @RequestParam(value = "classNumber", required = false) String classNumber
    ) {
        return new ResponseEntity<Map<String, Object>>(cetStatisticAnalysisService.currentStatistics(orgId, collegeNumber, professionNumber, classNumber, cetType), HttpStatus.OK);
    }
    /**
     * 英语考试当前状况---人数分布---按行政班
     * @param orgId
     * @param cetType
     * @param collegeNumber
     * @param professionNumber
     * @param classNumber
     * @return
     */

    @GetMapping(value = "/organizationstatistics", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(httpMethod = "GET", value = "英语考试当前状况---人数分布", response = Void.class, notes = "英语考试当前状况---人数分布<br><br><b>@author jianwei.wu</b>")
    public ResponseEntity<Map<String, Object>> organizationStatistics(
            @ApiParam(value = "orgId 学校id" , required = true) @RequestParam(value = "orgId", required = true) Long orgId,
            @ApiParam(value = "cetType 成绩类型： (四级;六级;)" , required = true ) @RequestParam(value = "cetType" , required = true) String cetType,
            @ApiParam(value = "collegeNumber 学院码", required = false) @RequestParam(value = "collegeNumber", required = false) String collegeNumber,
            @ApiParam(value = "professionNumber 专业码", required = false) @RequestParam(value = "professionNumber", required = false) String professionNumber,
            @ApiParam(value = "classNumber 班号", required = false) @RequestParam(value = "classNumber", required = false) String classNumber
    ) {
        return new ResponseEntity<Map<String, Object>>(cetStatisticAnalysisService.organizationStatistics(orgId, collegeNumber, professionNumber, classNumber, cetType), HttpStatus.OK);
    }
    /**
     * 查看数据详情---数据列表
     * @param orgId
     * @param cetType
     * @param collegeNumber
     * @param professionNumber
     * @param classNumber
     * @return
     */

    @GetMapping(value = "/detaillist", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(httpMethod = "GET", value = "查看数据详情---数据列表", response = Void.class, notes = "查看数据详情---数据列表<br><br><b>@author jianwei.wu</b>")
    public ResponseEntity<Object> getDetailList(
            @ApiParam(value = "orgId 学校id" , required = true) @RequestParam(value = "orgId", required = true) Long orgId,
            @ApiParam(value = "cetType 成绩类型： (四级;六级;)" , required = true ) @RequestParam(value = "cetType" , required = true) String cetType,
            @ApiParam(value = "collegeNumber 学院码", required = false) @RequestParam(value = "collegeNumber", required = false) String collegeNumber,
            @ApiParam(value = "professionNumber 专业码", required = false) @RequestParam(value = "professionNumber", required = false) String professionNumber,
            @ApiParam(value = "classNumber 班号", required = false) @RequestParam(value = "classNumber", required = false) String classNumber,
            @ApiParam(value = "nj 姓名或学号", required = false) @RequestParam(value = "nj", required = false) String nj,
            @ApiParam(value = "teacherYear 学年" , required = true ) @RequestParam(value = "teacherYear" , required = true) String teacherYear,
            @ApiParam(value = "semester 学期 (春;秋;)" , required = true) @RequestParam(value = "semester" , required = true) String semester,
            @ApiParam(value = "isPass 是否通过（1:通过;0:未通过）", required = false) @RequestParam(value = "isPass", required = false) String isPass,
            @ApiParam(value = "pageNumber 第几页") @RequestParam(value = "pageNumber", required = false) Integer pageNumber,
            @ApiParam(value = "pageSize 每页数据的数目") @RequestParam(value = "pageSize", required = false) Integer pageSize){
        return new ResponseEntity<Object>(cetStatisticAnalysisService.getDetailList(orgId, collegeNumber, professionNumber, classNumber, cetType, nj, teacherYear, semester, isPass, pageNumber, pageSize), HttpStatus.OK);
    }



    @GetMapping(value = "/organizationstatistics/avg", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(httpMethod = "GET", value = "英语考试当前状况---均值分布", response = Void.class, notes = "英语考试当前状况---人数分布---按行政班<br><br><b>@author jianwei.wu</b>")
    public ResponseEntity<Map<String, Object>> organizationStatisticsAvg(
            @ApiParam(value = "orgId 学校id" , required = true) @RequestParam(value = "orgId", required = true) Long orgId,
            @ApiParam(value = "cetType 成绩类型： (四级;六级;)" , required = true ) @RequestParam(value = "cetType" , required = true) String cetType,
            @ApiParam(value = "collegeNumber 学院码", required = false) @RequestParam(value = "collegeNumber", required = false) String collegeNumber,
            @ApiParam(value = "professionNumber 专业码", required = false) @RequestParam(value = "professionNumber", required = false) String professionNumber,
            @ApiParam(value = "classNumber 班号", required = false) @RequestParam(value = "classNumber", required = false) String classNumber
    ) {
        return new ResponseEntity<Map<String, Object>>(cetStatisticAnalysisService.avg(orgId, collegeNumber, professionNumber, classNumber, cetType), HttpStatus.OK);
    }



    @GetMapping(value = "/organizationstatistics/over/year", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(httpMethod = "GET", value = "历年数据分析---通过率趋势分析", response = Void.class, notes = "历年数据分析---通过率趋势分析<br><br><b>@author jianwei.wu</b>")
    public ResponseEntity<Map<String, Object>> organizationStatisticsOverYear(
            @ApiParam(value = "orgId 学校id" , required = true) @RequestParam(value = "orgId", required = true) Long orgId,
            @ApiParam(value = "cetType 成绩类型： (四级;六级;)" , required = true ) @RequestParam(value = "cetType" , required = true) String cetType,
            @ApiParam(value = "collegeNumber 学院码", required = false) @RequestParam(value = "collegeNumber", required = false) String collegeNumber,
            @ApiParam(value = "professionNumber 专业码", required = false) @RequestParam(value = "professionNumber", required = false) String professionNumber,
            @ApiParam(value = "classNumber 班号", required = false) @RequestParam(value = "classNumber", required = false) String classNumber,
            @ApiParam(value = "className 班级名称", required = false) @RequestParam(value = "className", required = false) String className
    ) {
        return new ResponseEntity<Map<String, Object>>(overYearsTestStatisticsService.OverYearsPassRate(orgId, collegeNumber, professionNumber, classNumber, cetType, className), HttpStatus.OK);
    }


    @GetMapping(value = "/organizationstatistics/over/year/avg", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(httpMethod = "GET", value = "历年数据分析---成绩均值趋势分析", response = Void.class, notes = "历年数据分析---成绩均值趋势分析<br><br><b>@author jianwei.wu</b>")
    public ResponseEntity<Map<String, Object>> organizationStatisticsOverYearAvg(
            @ApiParam(value = "orgId 学校id" , required = true) @RequestParam(value = "orgId", required = true) Long orgId,
            @ApiParam(value = "cetType 成绩类型： (四级;六级;)" , required = true ) @RequestParam(value = "cetType" , required = true) String cetType,
            @ApiParam(value = "collegeNumber 学院码", required = false) @RequestParam(value = "collegeNumber", required = false) String collegeNumber,
            @ApiParam(value = "professionNumber 专业码", required = false) @RequestParam(value = "professionNumber", required = false) String professionNumber,
            @ApiParam(value = "classNumber 班号", required = false) @RequestParam(value = "classNumber", required = false) String classNumber,
            @ApiParam(value = "className 班级名称", required = false) @RequestParam(value = "className", required = false) String className
    ) {
        return new ResponseEntity<Map<String, Object>>(overYearsTestStatisticsService.OverYearsAvgScore(orgId, collegeNumber, professionNumber, classNumber, cetType, className), HttpStatus.OK);
    }


    /**
     * 英语四六级单次考试统计导出
     * @param orgId
     * @param teacherYear
     * @param semester
     * @return
     */
    @GetMapping(value = "/singlexport", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(httpMethod = "GET", value = "英语四六级单次考试统计导出", response = Void.class, notes = "英语四六级单次考试统计导出<br><br><b>@author jianwei.wu</b>")
    public ResponseEntity<byte[]> cetSingleDataSexNumberOfPeople(
            @ApiParam(value = "orgId 学校id" , required = true) @RequestParam(value = "orgId", required = true) Long orgId,
            @ApiParam(value = "teacherYear 学年" , required = true ) @RequestParam(value = "teacherYear" , required = true) String teacherYear,
            @ApiParam(value = "semester 学期 (春;秋;)" , required = true) @RequestParam(value = "semester" , required = true) String semester
    ) {
        return cetStatisticAnalysisService.cetSingleStatisticsExport(orgId, teacherYear, semester);
    }
















}
