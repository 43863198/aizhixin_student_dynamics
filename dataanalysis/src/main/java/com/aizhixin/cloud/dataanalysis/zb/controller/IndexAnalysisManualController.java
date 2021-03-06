package com.aizhixin.cloud.dataanalysis.zb.controller;

/**
 * 手动统计
 */
//@RestController
//@RequestMapping("/v1/index/manual")
//@Api(description = "英语手动触发")
public class IndexAnalysisManualController {
//    @Autowired
//    private AnalysisIndexService analysisIndexService;
//    @Autowired
//    private CetEtlService cetEtlService;
//    @Autowired
//    private StandardScoreService standardScoreService;
//    @Autowired
//    private CetLjIndexAnalysisService cetLjIndexAnalysisService;
//    @Autowired
//    private CetGradeService cetGradeService;
//    @Autowired
//    private CetDcIndexAnalysisService cetDcIndexAnalysisService;
//    @Autowired
//    private StandardScoreSemesterIndexService standardScoreSemesterIndexService;
//    @Autowired
//    private StudyExceptionIndexService studyExceptionIndexService;
//
//    @GetMapping(value = "/cet/etl", produces = MediaType.APPLICATION_JSON_VALUE)
//    @ApiOperation(httpMethod = "GET", value = "英语等级考试数据库对数据库标准数据清洗ETL", response = Void.class, notes = "英语等级考试数据库对数据库标准数据清洗ETL<br><br><b>@author zhen.pan</b>")
//    public void etlCet(@ApiParam(value = "orgId 机构id" , required = true) @RequestParam(value = "orgId") Long orgId) {
//        cetEtlService.etlDB2DB(orgId);
//    }

//    @GetMapping(value = "/score/etl", produces = MediaType.APPLICATION_JSON_VALUE)
//    @ApiOperation(httpMethod = "GET", value = "学生成绩数据库对数据库标准数据清洗ETL", response = Void.class, notes = "学生成绩数据库对数据库标准数据清洗ETL<br><br><b>@author zhen.pan</b>")
//    public void etlScore(@ApiParam(value = "orgId 机构id" , required = true) @RequestParam(value = "orgId") Long orgId) {
//        standardScoreService.etlDB2DB(orgId);
//    }
//
//    @GetMapping(value = "/score/etlxnxq", produces = MediaType.APPLICATION_JSON_VALUE)
//    @ApiOperation(httpMethod = "GET", value = "学生成绩数据库对数据库标准数据清洗ETL", response = Void.class, notes = "学生成绩数据库对数据库标准数据清洗ETL<br><br><b>@author zhen.pan</b>")
//    public void etlScore(@ApiParam(value = "orgId 机构id" , required = true) @RequestParam(value = "orgId") Long orgId,
//                         @ApiParam(value = "xn 学年" , required = true) @RequestParam(value = "xn") String xn,
//                         @ApiParam(value = "xq 学期" , required = true) @RequestParam(value = "xq") String xq) {
//        standardScoreService.etlDB2DB(orgId, xn, xq);
//    }
//
//    @GetMapping(value = "/cet", produces = MediaType.APPLICATION_JSON_VALUE)
//    @ApiOperation(httpMethod = "GET", value = "英语等级考试基础指标统计", response = Void.class, notes = "英语等级考试基础指标统计<br><br><b>@author zhen.pan</b>")
//    public void cet(@ApiParam(value = "orgId 机构id" , required = true) @RequestParam(value = "orgId") Long orgId) {
//        analysisIndexService.schoolBaseIndex(orgId);
//    }
//
//    @GetMapping(value = "/cet/grade", produces = MediaType.APPLICATION_JSON_VALUE)
//    @ApiOperation(httpMethod = "GET", value = "英语等级考试基础指标年级分布统计", response = Void.class, notes = "英语等级考试基础指标年级分布统计<br><br><b>@author zhen.pan</b>")
//    public void cetGrade(@ApiParam(value = "orgId 机构id" , required = true) @RequestParam(value = "orgId") Long orgId) {
//        analysisIndexService.schoolNjIndex(orgId);
//    }
//
//    @GetMapping(value = "/score", produces = MediaType.APPLICATION_JSON_VALUE)
//    @ApiOperation(httpMethod = "GET", value = "学生成绩指标的计算", response = Void.class, notes = "学生成绩指标的计算<br><br><b>@author zhen.pan</b>")
//    public void score(@ApiParam(value = "orgId 机构id" , required = true) @RequestParam(value = "orgId") Long orgId) {
//        standardScoreSemesterIndexService.schoolStudentScoreIndex(orgId);
//    }
//
//    @GetMapping(value = "/cet/lj", produces = MediaType.APPLICATION_JSON_VALUE)
//    @ApiOperation(httpMethod = "GET", value = "英语等级考试累计基础指标统计", response = Void.class, notes = "英语等级考试累计基础指标统计<br><br><b>@author zhen.pan</b>")
//    public void cetlj(@ApiParam(value = "orgId 机构id" , required = true) @RequestParam(value = "orgId") Long orgId) {
//        cetLjIndexAnalysisService.calLjHaveTest(orgId);
//    }
//
//    @GetMapping(value = "/cet/lj/newest", produces = MediaType.APPLICATION_JSON_VALUE)
//    @ApiOperation(httpMethod = "GET", value = "英语等级考试最新累计基础指标统计", response = Void.class, notes = "英语等级考试最新累计基础指标统计<br><br><b>@author zhen.pan</b>")
//    public void cetljNewest(@ApiParam(value = "orgId 机构id" , required = true) @RequestParam(value = "orgId") Long orgId) {
//        cetLjIndexAnalysisService.calLjNewest(orgId);
//    }
//
//    @GetMapping(value = "/cet/lj/grade/newest", produces = MediaType.APPLICATION_JSON_VALUE)
//    @ApiOperation(httpMethod = "GET", value = "英语等级考试最新累计年级指标统计", response = Void.class, notes = "英语等级考试最新累计年级指标统计<br><br><b>@author zhen.pan</b>")
//    public void cetljGradeNewest(@ApiParam(value = "orgId 机构id" , required = true) @RequestParam(value = "orgId") Long orgId) {
//        cetGradeService.calLjNewest(orgId);
//    }
//
//    @GetMapping(value = "/cet/lj/grade", produces = MediaType.APPLICATION_JSON_VALUE)
//    @ApiOperation(httpMethod = "GET", value = "英语等级考试所有累计年级指标统计", response = Void.class, notes = "英语等级考试所有累计年级指标统计<br><br><b>@author zhen.pan</b>")
//    public void cetljGrade(@ApiParam(value = "orgId 机构id" , required = true) @RequestParam(value = "orgId") Long orgId) {
//        cetGradeService.calLjAll(orgId);
//    }
//
//    @GetMapping(value = "/cet/dc/grade", produces = MediaType.APPLICATION_JSON_VALUE)
//    @ApiOperation(httpMethod = "GET", value = "英语等级考试单次所有年级指标统计", response = Void.class, notes = "英语等级考试单次所有年级指标统计<br><br><b>@author zhen.pan</b>")
//    public void cetDcGrade(@ApiParam(value = "orgId 机构id" , required = true) @RequestParam(value = "orgId") Long orgId) {
//        cetGradeService.calDcAll(orgId);
//    }
//
//    @GetMapping(value = "/cet/dc", produces = MediaType.APPLICATION_JSON_VALUE)
//    @ApiOperation(httpMethod = "GET", value = "英语等级考试单次基础指标统计", response = Void.class, notes = "英语等级考试单次基础指标统计<br><br><b>@author zhen.pan</b>")
//    public void cetDc(@ApiParam(value = "orgId 机构id" , required = true) @RequestParam(value = "orgId") Long orgId) {
//        cetDcIndexAnalysisService.calDcHaveTest(orgId);
//    }

//    @GetMapping(value = "/semesterscore/semester", produces = MediaType.APPLICATION_JSON_VALUE)
//    @ApiOperation(httpMethod = "GET", value = "学生成绩学期对应数据学生指标的生成", response = Void.class, notes = "学生成绩学期对应数据学生指标的生成<br><br><b>@author zhen.pan</b>")
//    public void score(@ApiParam(value = "xxdm 机构代码" , required = true) @RequestParam(value = "xxdm") String xxdm,
//                      @ApiParam(value = "xn 学年" , required = true) @RequestParam(value = "xn") String xn,
//                      @ApiParam(value = "xq 学期" , required = true) @RequestParam(value = "xq") String xq) {
//        standardScoreSemesterIndexService.oneSemesterStudentScoreIndex(xxdm, xn, xq);
//    }
//
//    @GetMapping(value = "/semesterscore/all", produces = MediaType.APPLICATION_JSON_VALUE)
//    @ApiOperation(httpMethod = "GET", value = "学生成绩所有学期对应数据学生指标的生成", response = Void.class, notes = "学生成绩所有学期对应数据学生指标的生成<br><br><b>@author zhen.pan</b>")
//    public void scoreAll(@ApiParam(value = "xxdm 机构代码" , required = true) @RequestParam(value = "xxdm") String xxdm) {
//        standardScoreSemesterIndexService.allSemesterStudentScoreIndex(xxdm);
//    }
//
//    @GetMapping(value = "/studyException", produces = MediaType.APPLICATION_JSON_VALUE)
//    @ApiOperation(httpMethod = "GET", value = "计算当前时间对于学期的修读异常指标", response = Void.class, notes = "计算当前时间对于学期的修读异常指标<br><br><b>@author zhen.pan</b>")
//    public void studyException(@ApiParam(value = "orgId 机构id" , required = true) @RequestParam(value = "orgId") Long orgId) {
//        studyExceptionIndexService.calCurrentDateIndex(orgId);
//    }
}
