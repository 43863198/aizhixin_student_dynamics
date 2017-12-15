package com.aizhixin.cloud.dataanalysis.simulate.controller;

import com.aizhixin.cloud.dataanalysis.analysis.dto.CourseEvaluateDTO;
import com.aizhixin.cloud.dataanalysis.analysis.dto.CourseEvaluateDetailDTO;
import com.aizhixin.cloud.dataanalysis.analysis.dto.TeacherEvaluateDTO;
import com.aizhixin.cloud.dataanalysis.analysis.dto.TeacherEvaluateDetailDTO;
import com.aizhixin.cloud.dataanalysis.analysis.entity.CourseEvaluate;
import com.aizhixin.cloud.dataanalysis.analysis.entity.TeacherEvaluate;
import com.aizhixin.cloud.dataanalysis.analysis.service.CourseEvaluateService;
import com.aizhixin.cloud.dataanalysis.analysis.service.TeacherEvaluateService;
import com.aizhixin.cloud.dataanalysis.common.PageData;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

/**
 * @author wangjun
 */
@RestController
@RequestMapping("/v1/generate/evaluate")
@Api(description = "教学评价测试数据API")
public class TestAnalysisDataEvaluateController {
    @Autowired
    private CourseEvaluateService courseEvaluateService;
    @Autowired
    private TeacherEvaluateService teacherEvaluateService;
    /**
     * 课程评价
     * @param orgId
     * @return
     */
    @GetMapping(value = "/generatecourseevaluate", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(httpMethod = "GET", value = "课程评价", response = Void.class, notes = "生成课程评价数据<br><br><b>@author wangjun</b>")
    public ResponseEntity<Map<String, Object>> generateCourseEvaluate(@ApiParam(value = "orgId 机构id") @RequestParam(value = "orgId", required = true) Long orgId,
                                                         @ApiParam(value = "teacherYear 学年)") @RequestParam(value = "teacherYear", required = true) int teacherYear,
                                                         @ApiParam(value = "num teacherYear几年内)") @RequestParam(value = "num", required = true) int num) {
        if (null != orgId && orgId.longValue() > 0L) {
        } else {
            orgId = 1L;
        }
        if(num == 0){
            num = 1;
        }
        List<CourseEvaluate> courseEvaluates=new ArrayList<CourseEvaluate>();
        for(int i=0;i<num;i++){
            CourseEvaluate courseEvaluate1=new CourseEvaluate();

            courseEvaluate1.setId(UUID.randomUUID().toString());
            courseEvaluate1.setOrgId(orgId);
            courseEvaluate1.setTeacherYear(teacherYear-i);
            courseEvaluate1.setCollegeName("机械与控制工程学院");
            courseEvaluate1.setCollegeId(1721L);
            courseEvaluate1.setCourseCode("90090");
            courseEvaluate1.setCourseName("大学物理");
            courseEvaluate1.setChargePersonID("2756");
            courseEvaluate1.setChargePerson("杨端翠");
            courseEvaluate1.setAvgScore(4.2f);
            courseEvaluate1.setCourseOpenDepartment("机械与控制工程学院");
            courseEvaluate1.setCourseOpenID("207");
            courseEvaluate1.setGrade(2017);
            courseEvaluate1.setSemester(1);
            courseEvaluate1.setTeachingClassId("614");;
            courseEvaluate1.setTeachingClassName("自动化05-1");

            courseEvaluates.add(courseEvaluate1);

            CourseEvaluate courseEvaluate11=new CourseEvaluate();

            courseEvaluate11.setId(UUID.randomUUID().toString());
            courseEvaluate11.setOrgId(orgId);
            courseEvaluate11.setTeacherYear(teacherYear-i);
            courseEvaluate11.setCollegeName("机械与控制工程学院");
            courseEvaluate11.setCollegeId(1721L);
            courseEvaluate11.setCourseCode("90090");
            courseEvaluate11.setCourseName("大学物理");
            courseEvaluate11.setChargePersonID("2756");
            courseEvaluate11.setChargePerson("杨端翠");
            courseEvaluate11.setAvgScore(4.2f);
            courseEvaluate11.setCourseOpenDepartment("机械与控制工程学院");
            courseEvaluate11.setCourseOpenID("207");
            courseEvaluate11.setGrade(2017);
            courseEvaluate11.setSemester(2);
            courseEvaluate11.setTeachingClassId("614");;
            courseEvaluate11.setTeachingClassName("自动化05-1");

            courseEvaluates.add(courseEvaluate11);

            CourseEvaluate courseEvaluate2=new CourseEvaluate();

            courseEvaluate2.setId(UUID.randomUUID().toString());
            courseEvaluate2.setOrgId(orgId);
            courseEvaluate2.setTeacherYear(teacherYear-i);
            courseEvaluate2.setCollegeName("计算机学院");
            courseEvaluate2.setCollegeId(1722L);
            courseEvaluate2.setCourseCode("040060");
            courseEvaluate2.setCourseName("面向对象与C++--040060--1");
            courseEvaluate2.setChargePersonID("22");
            courseEvaluate2.setChargePerson("陆二庆");
            courseEvaluate2.setAvgScore(4.8f);
            courseEvaluate2.setCourseOpenDepartment("机械与控制工程学院");
            courseEvaluate2.setCourseOpenID("207");
            courseEvaluate2.setGrade(2017);
            courseEvaluate2.setSemester(1);
            courseEvaluate2.setTeachingClassId("615");;
            courseEvaluate2.setTeachingClassName("高计维04-1");

            courseEvaluates.add(courseEvaluate2);

            CourseEvaluate courseEvaluate22=new CourseEvaluate();

            courseEvaluate22.setId(UUID.randomUUID().toString());
            courseEvaluate22.setOrgId(orgId);
            courseEvaluate22.setTeacherYear(teacherYear-i);
            courseEvaluate22.setCollegeName("计算机学院");
            courseEvaluate22.setCollegeId(1722L);
            courseEvaluate22.setCourseCode("040060");
            courseEvaluate22.setCourseName("面向对象与C++--040060--1");
            courseEvaluate22.setChargePersonID("22");
            courseEvaluate22.setChargePerson("陆二庆");
            courseEvaluate22.setAvgScore(4.1f);
            courseEvaluate22.setCourseOpenDepartment("机械与控制工程学院");
            courseEvaluate22.setCourseOpenID("207");
            courseEvaluate22.setGrade(2017);
            courseEvaluate22.setSemester(2);
            courseEvaluate22.setTeachingClassId("615");;
            courseEvaluate22.setTeachingClassName("高计维04-1");

            courseEvaluates.add(courseEvaluate22);
            CourseEvaluate courseEvaluate3=new CourseEvaluate();

            courseEvaluate3.setId(UUID.randomUUID().toString());
            courseEvaluate3.setOrgId(orgId);
            courseEvaluate3.setTeacherYear(teacherYear-i);
            courseEvaluate3.setCollegeName("管理工程学院");
            courseEvaluate3.setCollegeId(1723L);
            courseEvaluate3.setCourseCode("334600");
            courseEvaluate3.setCourseName("第三方物流管理X--334600--1");
            courseEvaluate3.setChargePersonID("27156");
            courseEvaluate3.setChargePerson("蒋爱华");
            courseEvaluate3.setAvgScore(4.9f);
            courseEvaluate3.setCourseOpenDepartment("管理工程学院");
            courseEvaluate3.setCourseOpenID("20712");
            courseEvaluate3.setGrade(2017);
            courseEvaluate3.setSemester(1);
            courseEvaluate3.setTeachingClassId("616");;
            courseEvaluate3.setTeachingClassName("信管-1");

            courseEvaluates.add(courseEvaluate3);

            CourseEvaluate courseEvaluate33=new CourseEvaluate();

            courseEvaluate33.setId(UUID.randomUUID().toString());
            courseEvaluate33.setOrgId(orgId);
            courseEvaluate33.setTeacherYear(teacherYear-i);
            courseEvaluate33.setCollegeName("人文学院");
            courseEvaluate33.setCollegeId(1723L);
            courseEvaluate33.setCourseCode("510740");
            courseEvaluate33.setCourseName("美学基础--510740--1");
            courseEvaluate33.setChargePersonID("2756");
            courseEvaluate33.setChargePerson("王柳");
            courseEvaluate33.setAvgScore(4.2f);
            courseEvaluate33.setCourseOpenDepartment("人文学院");
            courseEvaluate33.setCourseOpenID("20107");
            courseEvaluate33.setGrade(2017);
            courseEvaluate33.setSemester(2);
            courseEvaluate33.setTeachingClassId("6144");;
            courseEvaluate33.setTeachingClassName("美学-1");

            courseEvaluates.add(courseEvaluate33);


            CourseEvaluate courseEvaluate4=new CourseEvaluate();

            courseEvaluate4.setId(UUID.randomUUID().toString());
            courseEvaluate4.setOrgId(orgId);
            courseEvaluate4.setTeacherYear(teacherYear-i);
            courseEvaluate4.setCollegeName("电信学院");
            courseEvaluate4.setCollegeId(1724L);
            courseEvaluate4.setCourseCode("040080");
            courseEvaluate4.setCourseName("微机原理--040080--1");
            courseEvaluate4.setChargePersonID("27756");
            courseEvaluate4.setChargePerson("王吉林");
            courseEvaluate4.setAvgScore(4.2f);
            courseEvaluate4.setCourseOpenDepartment("电信学院");
            courseEvaluate4.setCourseOpenID("1724");
            courseEvaluate4.setGrade(2017);
            courseEvaluate4.setSemester(1);
            courseEvaluate4.setTeachingClassId("710");;
            courseEvaluate4.setTeachingClassName("通信03-1");

            courseEvaluates.add(courseEvaluate4);

            CourseEvaluate courseEvaluate44=new CourseEvaluate();

            courseEvaluate44.setId(UUID.randomUUID().toString());
            courseEvaluate44.setOrgId(orgId);
            courseEvaluate44.setTeacherYear(teacherYear-i);
            courseEvaluate44.setCollegeName("电信学院");
            courseEvaluate44.setCollegeId(1724L);
            courseEvaluate44.setCourseCode("040080");
            courseEvaluate44.setCourseName("微机原理--040080--1");
            courseEvaluate44.setChargePersonID("27756");
            courseEvaluate44.setChargePerson("王吉林");
            courseEvaluate44.setAvgScore(4.2f);
            courseEvaluate44.setCourseOpenDepartment("电信学院");
            courseEvaluate44.setCourseOpenID("1724");
            courseEvaluate44.setGrade(2017);
            courseEvaluate44.setSemester(1);
            courseEvaluate44.setTeachingClassId("710");;
            courseEvaluate44.setTeachingClassName("通信03-1");

            courseEvaluates.add(courseEvaluate44);

            CourseEvaluate courseEvaluate5=new CourseEvaluate();

            courseEvaluate5.setId(UUID.randomUUID().toString());
            courseEvaluate5.setOrgId(orgId);
            courseEvaluate5.setTeacherYear(teacherYear-i);
            courseEvaluate5.setCollegeName("电子信息技术学院");
            courseEvaluate5.setCollegeId(1725L);
            courseEvaluate5.setCourseCode("510520");
            courseEvaluate5.setCourseName("计算机知识及应用初步--510520--4");
            courseEvaluate5.setChargePersonID("3211");
            courseEvaluate5.setChargePerson("朱继元");
            courseEvaluate5.setAvgScore(4.2f);
            courseEvaluate5.setCourseOpenDepartment("电子信息技术学院");
            courseEvaluate5.setCourseOpenID("1725");
            courseEvaluate5.setGrade(2017);
            courseEvaluate5.setSemester(1);
            courseEvaluate5.setTeachingClassId("614");;
            courseEvaluate5.setTeachingClassName("计算机05-1");

            courseEvaluates.add(courseEvaluate5);

            CourseEvaluate courseEvaluate55=new CourseEvaluate();

            courseEvaluate55.setId(UUID.randomUUID().toString());
            courseEvaluate55.setOrgId(orgId);
            courseEvaluate55.setTeacherYear(teacherYear-i);
            courseEvaluate55.setCollegeName("电子信息技术学院");
            courseEvaluate55.setCollegeId(1725L);
            courseEvaluate55.setCourseCode("510520");
            courseEvaluate55.setCourseName("计算机知识及应用初步--510520--4");
            courseEvaluate55.setChargePersonID("3211");
            courseEvaluate55.setChargePerson("朱继元");
            courseEvaluate55.setAvgScore(4.2f);
            courseEvaluate55.setCourseOpenDepartment("电子信息技术学院");
            courseEvaluate55.setCourseOpenID("1725");
            courseEvaluate55.setGrade(2017);
            courseEvaluate55.setSemester(2);
            courseEvaluate55.setTeachingClassId("614");;
            courseEvaluate55.setTeachingClassName("计算机05-1");

            courseEvaluates.add(courseEvaluate55);

            CourseEvaluate courseEvaluate6=new CourseEvaluate();

            courseEvaluate6.setId(UUID.randomUUID().toString());
            courseEvaluate6.setOrgId(orgId);
            courseEvaluate6.setTeacherYear(teacherYear-i);
            courseEvaluate6.setCollegeName("土木与建筑工程学院");
            courseEvaluate6.setCollegeId(1726L);
            courseEvaluate6.setCourseCode("311860");
            courseEvaluate6.setCourseName("工程监理概论X--311860--1");
            courseEvaluate6.setChargePersonID("275116");
            courseEvaluate6.setChargePerson("刘帅");
            courseEvaluate6.setAvgScore(4.2f);
            courseEvaluate6.setCourseOpenDepartment("土木与建筑工程学院");
            courseEvaluate6.setCourseOpenID("1726");
            courseEvaluate6.setGrade(2017);
            courseEvaluate6.setSemester(1);
            courseEvaluate6.setTeachingClassId("614");;
            courseEvaluate6.setTeachingClassName("资勘04-1");

            courseEvaluates.add(courseEvaluate6);

            CourseEvaluate courseEvaluate66=new CourseEvaluate();

            courseEvaluate66.setId(UUID.randomUUID().toString());
            courseEvaluate66.setOrgId(orgId);
            courseEvaluate66.setTeacherYear(teacherYear-i);
            courseEvaluate66.setCollegeName("土木与建筑工程学院");
            courseEvaluate66.setCollegeId(1726L);
            courseEvaluate66.setCourseCode("311860");
            courseEvaluate66.setCourseName("工程监理概论X--311860--1");
            courseEvaluate66.setChargePersonID("275116");
            courseEvaluate66.setChargePerson("刘帅");
            courseEvaluate66.setAvgScore(4.2f);
            courseEvaluate66.setCourseOpenDepartment("土木与建筑工程学院");
            courseEvaluate66.setCourseOpenID("1726");
            courseEvaluate66.setGrade(2017);
            courseEvaluate66.setSemester(2);
            courseEvaluate66.setTeachingClassId("614");;
            courseEvaluate66.setTeachingClassName("资勘04-1");

            courseEvaluates.add(courseEvaluate66);

            CourseEvaluate courseEvaluate7=new CourseEvaluate();

            courseEvaluate7.setId(UUID.randomUUID().toString());
            courseEvaluate7.setOrgId(orgId);
            courseEvaluate7.setTeacherYear(teacherYear-i);
            courseEvaluate7.setCollegeName("艺术学院");
            courseEvaluate7.setCollegeId(1727L);
            courseEvaluate7.setCourseCode("322732");
            courseEvaluate7.setCourseName("素描2--322732--1");
            courseEvaluate7.setChargePersonID("275216");
            courseEvaluate7.setChargePerson("陈曦");
            courseEvaluate7.setAvgScore(4.9f);
            courseEvaluate7.setCourseOpenDepartment("艺术学院");
            courseEvaluate7.setCourseOpenID("1727");
            courseEvaluate7.setGrade(2017);
            courseEvaluate7.setSemester(1);
            courseEvaluate7.setTeachingClassId("614");;
            courseEvaluate7.setTeachingClassName("艺术视传05-3");

            courseEvaluates.add(courseEvaluate7);

            CourseEvaluate courseEvaluate77=new CourseEvaluate();

            courseEvaluate77.setId(UUID.randomUUID().toString());
            courseEvaluate77.setOrgId(orgId);
            courseEvaluate77.setTeacherYear(teacherYear-i);
            courseEvaluate77.setCollegeName("艺术学院");
            courseEvaluate77.setCollegeId(1727L);
            courseEvaluate77.setCourseCode("322732");
            courseEvaluate77.setCourseName("素描2--322732--1");
            courseEvaluate77.setChargePersonID("275216");
            courseEvaluate77.setChargePerson("陈曦");
            courseEvaluate77.setAvgScore(4.9f);
            courseEvaluate77.setCourseOpenDepartment("艺术学院");
            courseEvaluate77.setCourseOpenID("1727");
            courseEvaluate77.setGrade(2017);
            courseEvaluate77.setSemester(2);
            courseEvaluate77.setTeachingClassId("614");;
            courseEvaluate77.setTeachingClassName("艺术视传05-3");

            courseEvaluates.add(courseEvaluate77);

            CourseEvaluate courseEvaluate8=new CourseEvaluate();

            courseEvaluate8.setId(UUID.randomUUID().toString());
            courseEvaluate8.setOrgId(orgId);
            courseEvaluate8.setTeacherYear(teacherYear-i);
            courseEvaluate8.setCollegeName("外国语学院");
            courseEvaluate8.setCollegeId(1728L);
            courseEvaluate8.setCourseCode("511730");
            courseEvaluate8.setCourseName("中级英语口语--511730--3");
            courseEvaluate8.setChargePersonID("2751216");
            courseEvaluate8.setChargePerson("刘光艳");
            courseEvaluate8.setAvgScore(4.5f);
            courseEvaluate8.setCourseOpenDepartment("外国语学院");
            courseEvaluate8.setCourseOpenID("1728");
            courseEvaluate8.setGrade(2017);
            courseEvaluate8.setSemester(1);
            courseEvaluate8.setTeachingClassId("614121");;
            courseEvaluate8.setTeachingClassName("会计05-2");

            courseEvaluates.add(courseEvaluate8);

            CourseEvaluate courseEvaluate88=new CourseEvaluate();

            courseEvaluate88.setId(UUID.randomUUID().toString());
            courseEvaluate88.setOrgId(orgId);
            courseEvaluate88.setTeacherYear(teacherYear-i);
            courseEvaluate88.setCollegeName("外国语学院");
            courseEvaluate88.setCollegeId(1728L);
            courseEvaluate88.setCourseCode("511730");
            courseEvaluate88.setCourseName("中级英语口语--511730--3");
            courseEvaluate88.setChargePersonID("2751216");
            courseEvaluate88.setChargePerson("刘光艳");
            courseEvaluate88.setAvgScore(4.5f);
            courseEvaluate88.setCourseOpenDepartment("外国语学院");
            courseEvaluate88.setCourseOpenID("1728");
            courseEvaluate88.setGrade(2017);
            courseEvaluate88.setSemester(1);
            courseEvaluate88.setTeachingClassId("614121");;
            courseEvaluate88.setTeachingClassName("会计05-2");

            courseEvaluates.add(courseEvaluate88);
        }
        courseEvaluateService.saveList(courseEvaluates);
        Map<String, Object> result = new HashMap<String, Object>();
        return new ResponseEntity<Map<String, Object>>(result, HttpStatus.OK);
    }
    /**
     * 教师评价
     * @param orgId
     * @return
     */
    @GetMapping(value = "/generateteachervaluate", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(httpMethod = "GET", value = "教师评价", response = Void.class, notes = "生成教师评价数据<br><br><b>@author wangjun</b>")
    public ResponseEntity<Map<String, Object>> generateTeacherEvaluate(@ApiParam(value = "orgId 机构id") @RequestParam(value = "orgId", required = true) Long orgId,
                                                                      @ApiParam(value = "teacherYear 学年)") @RequestParam(value = "teacherYear", required = true) int teacherYear,
                                                                      @ApiParam(value = "num teacherYear几年内)") @RequestParam(value = "num", required = true) int num) {
        if (null != orgId && orgId.longValue() > 0L) {
        } else {
            orgId = 1L;
        }
        if(num == 0){
            num = 1;
        }
        List<TeacherEvaluate> list=new ArrayList<TeacherEvaluate>();
        for(int i=0;i<num;i++){

            TeacherEvaluate teacherEvaluate1=new TeacherEvaluate();

            teacherEvaluate1.setId(UUID.randomUUID().toString());
            teacherEvaluate1.setOrgId(orgId);
            teacherEvaluate1.setTeacherYear(teacherYear-i);
            teacherEvaluate1.setCollegeName("机械与控制工程学院");
            teacherEvaluate1.setCollegeId(1721L);
            teacherEvaluate1.setCourseCode("90090");
            teacherEvaluate1.setCourseName("大学物理");
            teacherEvaluate1.setTeacherId("2756");
            teacherEvaluate1.setTeacherName("杨端翠");
            teacherEvaluate1.setAvgScore(4.2f);
            teacherEvaluate1.setGrade(2017);
            teacherEvaluate1.setSemester(1);
            teacherEvaluate1.setClassId("614");;
            teacherEvaluate1.setClassName("自动化05-1");

            list.add(teacherEvaluate1);

            TeacherEvaluate teacherEvaluate11=new TeacherEvaluate();

            teacherEvaluate11.setId(UUID.randomUUID().toString());
            teacherEvaluate11.setOrgId(orgId);
            teacherEvaluate11.setTeacherYear(teacherYear-i);
            teacherEvaluate11.setCollegeName("机械与控制工程学院");
            teacherEvaluate11.setCollegeId(1721L);
            teacherEvaluate11.setCourseCode("90090");
            teacherEvaluate11.setCourseName("大学物理");
            teacherEvaluate11.setTeacherId("2756");
            teacherEvaluate11.setTeacherName("杨端翠");
            teacherEvaluate11.setAvgScore(4.2f);
            teacherEvaluate11.setGrade(2017);
            teacherEvaluate11.setSemester(2);
            teacherEvaluate11.setClassId("614");;
            teacherEvaluate11.setClassName("自动化05-1");

            list.add(teacherEvaluate11);

            TeacherEvaluate teacherEvaluate2=new TeacherEvaluate();

            teacherEvaluate2.setId(UUID.randomUUID().toString());
            teacherEvaluate2.setOrgId(orgId);
            teacherEvaluate2.setTeacherYear(teacherYear-i);
            teacherEvaluate2.setCollegeName("计算机学院");
            teacherEvaluate2.setCollegeId(1722L);
            teacherEvaluate2.setCourseCode("040060");
            teacherEvaluate2.setCourseName("面向对象与C++--040060--1");
            teacherEvaluate2.setTeacherId("22");
            teacherEvaluate2.setTeacherName("陆二庆");
            teacherEvaluate2.setAvgScore(4.8f);
            teacherEvaluate2.setGrade(2017);
            teacherEvaluate2.setSemester(1);
            teacherEvaluate2.setClassId("615");;
            teacherEvaluate2.setClassName("高计维04-1");

            list.add(teacherEvaluate2);

            TeacherEvaluate teacherEvaluate22=new TeacherEvaluate();

            teacherEvaluate22.setId(UUID.randomUUID().toString());
            teacherEvaluate22.setOrgId(orgId);
            teacherEvaluate22.setTeacherYear(teacherYear-i);
            teacherEvaluate22.setCollegeName("计算机学院");
            teacherEvaluate22.setCollegeId(1722L);
            teacherEvaluate22.setCourseCode("040060");
            teacherEvaluate22.setCourseName("面向对象与C++--040060--1");
            teacherEvaluate22.setTeacherId("22");
            teacherEvaluate22.setTeacherName("陆二庆");
            teacherEvaluate22.setAvgScore(4.1f);
            teacherEvaluate22.setGrade(2017);
            teacherEvaluate22.setSemester(2);
            teacherEvaluate22.setClassId("615");;
            teacherEvaluate22.setClassName("高计维04-1");

            list.add(teacherEvaluate22);
            TeacherEvaluate teacherEvaluate3=new TeacherEvaluate();

            teacherEvaluate3.setId(UUID.randomUUID().toString());
            teacherEvaluate3.setOrgId(orgId);
            teacherEvaluate3.setTeacherYear(teacherYear-i);
            teacherEvaluate3.setCollegeName("管理工程学院");
            teacherEvaluate3.setCollegeId(1723L);
            teacherEvaluate3.setCourseCode("334600");
            teacherEvaluate3.setCourseName("第三方物流管理X--334600--1");
            teacherEvaluate3.setTeacherId("27156");
            teacherEvaluate3.setTeacherName("蒋爱华");
            teacherEvaluate3.setAvgScore(4.9f);
            teacherEvaluate3.setGrade(2017);
            teacherEvaluate3.setSemester(1);
            teacherEvaluate3.setClassId("616");;
            teacherEvaluate3.setClassName("信管-1");

            list.add(teacherEvaluate3);

            TeacherEvaluate teacherEvaluate33=new TeacherEvaluate();

            teacherEvaluate33.setId(UUID.randomUUID().toString());
            teacherEvaluate33.setOrgId(orgId);
            teacherEvaluate33.setTeacherYear(teacherYear-i);
            teacherEvaluate33.setCollegeName("人文学院");
            teacherEvaluate33.setCollegeId(1723L);
            teacherEvaluate33.setCourseCode("510740");
            teacherEvaluate33.setCourseName("美学基础--510740--1");
            teacherEvaluate33.setTeacherId("2756");
            teacherEvaluate33.setTeacherName("王柳");
            teacherEvaluate33.setAvgScore(4.2f);
            teacherEvaluate33.setGrade(2017);
            teacherEvaluate33.setSemester(2);
            teacherEvaluate33.setClassId("6144");;
            teacherEvaluate33.setClassName("美学-1");

            list.add(teacherEvaluate33);


            TeacherEvaluate teacherEvaluate4=new TeacherEvaluate();

            teacherEvaluate4.setId(UUID.randomUUID().toString());
            teacherEvaluate4.setOrgId(orgId);
            teacherEvaluate4.setTeacherYear(teacherYear-i);
            teacherEvaluate4.setCollegeName("电信学院");
            teacherEvaluate4.setCollegeId(1724L);
            teacherEvaluate4.setCourseCode("040080");
            teacherEvaluate4.setCourseName("微机原理--040080--1");
            teacherEvaluate4.setTeacherId("27756");
            teacherEvaluate4.setTeacherName("王吉林");
            teacherEvaluate4.setAvgScore(4.2f);
            teacherEvaluate4.setGrade(2017);
            teacherEvaluate4.setSemester(1);
            teacherEvaluate4.setClassId("710");;
            teacherEvaluate4.setClassName("通信03-1");

            list.add(teacherEvaluate4);

            TeacherEvaluate teacherEvaluate44=new TeacherEvaluate();

            teacherEvaluate44.setId(UUID.randomUUID().toString());
            teacherEvaluate44.setOrgId(orgId);
            teacherEvaluate44.setTeacherYear(teacherYear-i);
            teacherEvaluate44.setCollegeName("电信学院");
            teacherEvaluate44.setCollegeId(1724L);
            teacherEvaluate44.setCourseCode("040080");
            teacherEvaluate44.setCourseName("微机原理--040080--1");
            teacherEvaluate44.setTeacherId("27756");
            teacherEvaluate44.setTeacherName("王吉林");
            teacherEvaluate44.setAvgScore(4.2f);
            teacherEvaluate44.setGrade(2017);
            teacherEvaluate44.setSemester(1);
            teacherEvaluate44.setClassId("710");;
            teacherEvaluate44.setClassName("通信03-1");

            list.add(teacherEvaluate44);

            TeacherEvaluate teacherEvaluate5=new TeacherEvaluate();

            teacherEvaluate5.setId(UUID.randomUUID().toString());
            teacherEvaluate5.setOrgId(orgId);
            teacherEvaluate5.setTeacherYear(teacherYear-i);
            teacherEvaluate5.setCollegeName("电子信息技术学院");
            teacherEvaluate5.setCollegeId(1725L);
            teacherEvaluate5.setCourseCode("510520");
            teacherEvaluate5.setCourseName("计算机知识及应用初步--510520--4");
            teacherEvaluate5.setTeacherId("3211");
            teacherEvaluate5.setTeacherName("朱继元");
            teacherEvaluate5.setAvgScore(4.2f);
            teacherEvaluate5.setGrade(2017);
            teacherEvaluate5.setSemester(1);
            teacherEvaluate5.setClassId("614");;
            teacherEvaluate5.setClassName("计算机05-1");

            list.add(teacherEvaluate5);

            TeacherEvaluate teacherEvaluate55=new TeacherEvaluate();

            teacherEvaluate55.setId(UUID.randomUUID().toString());
            teacherEvaluate55.setOrgId(orgId);
            teacherEvaluate55.setTeacherYear(teacherYear-i);
            teacherEvaluate55.setCollegeName("电子信息技术学院");
            teacherEvaluate55.setCollegeId(1725L);
            teacherEvaluate55.setCourseCode("510520");
            teacherEvaluate55.setCourseName("计算机知识及应用初步--510520--4");
            teacherEvaluate55.setTeacherId("3211");
            teacherEvaluate55.setTeacherName("朱继元");
            teacherEvaluate55.setAvgScore(4.2f);
            teacherEvaluate55.setGrade(2017);
            teacherEvaluate55.setSemester(2);
            teacherEvaluate55.setClassId("614");;
            teacherEvaluate55.setClassName("计算机05-1");

            list.add(teacherEvaluate55);

            TeacherEvaluate teacherEvaluate6=new TeacherEvaluate();

            teacherEvaluate6.setId(UUID.randomUUID().toString());
            teacherEvaluate6.setOrgId(orgId);
            teacherEvaluate6.setTeacherYear(teacherYear-i);
            teacherEvaluate6.setCollegeName("土木与建筑工程学院");
            teacherEvaluate6.setCollegeId(1726L);
            teacherEvaluate6.setCourseCode("311860");
            teacherEvaluate6.setCourseName("工程监理概论X--311860--1");
            teacherEvaluate6.setTeacherId("275116");
            teacherEvaluate6.setTeacherName("刘帅");
            teacherEvaluate6.setAvgScore(4.2f);
            teacherEvaluate6.setGrade(2017);
            teacherEvaluate6.setSemester(1);
            teacherEvaluate6.setClassId("614");;
            teacherEvaluate6.setClassName("资勘04-1");

            list.add(teacherEvaluate6);

            TeacherEvaluate teacherEvaluate66=new TeacherEvaluate();

            teacherEvaluate66.setId(UUID.randomUUID().toString());
            teacherEvaluate66.setOrgId(orgId);
            teacherEvaluate66.setTeacherYear(teacherYear-i);
            teacherEvaluate66.setCollegeName("土木与建筑工程学院");
            teacherEvaluate66.setCollegeId(1726L);
            teacherEvaluate66.setCourseCode("311860");
            teacherEvaluate66.setCourseName("工程监理概论X--311860--1");
            teacherEvaluate66.setTeacherId("275116");
            teacherEvaluate66.setTeacherName("刘帅");
            teacherEvaluate66.setAvgScore(4.2f);
            teacherEvaluate66.setGrade(2017);
            teacherEvaluate66.setSemester(2);
            teacherEvaluate66.setClassId("614");;
            teacherEvaluate66.setClassName("资勘04-1");

            list.add(teacherEvaluate66);

            TeacherEvaluate teacherEvaluate7=new TeacherEvaluate();

            teacherEvaluate7.setId(UUID.randomUUID().toString());
            teacherEvaluate7.setOrgId(orgId);
            teacherEvaluate7.setTeacherYear(teacherYear-i);
            teacherEvaluate7.setCollegeName("艺术学院");
            teacherEvaluate7.setCollegeId(1727L);
            teacherEvaluate7.setCourseCode("322732");
            teacherEvaluate7.setCourseName("素描2--322732--1");
            teacherEvaluate7.setTeacherId("275216");
            teacherEvaluate7.setTeacherName("陈曦");
            teacherEvaluate7.setAvgScore(4.9f);
            teacherEvaluate7.setGrade(2017);
            teacherEvaluate7.setSemester(1);
            teacherEvaluate7.setClassId("614");;
            teacherEvaluate7.setClassName("艺术视传05-3");

            list.add(teacherEvaluate7);

            TeacherEvaluate teacherEvaluate77=new TeacherEvaluate();

            teacherEvaluate77.setId(UUID.randomUUID().toString());
            teacherEvaluate77.setOrgId(orgId);
            teacherEvaluate77.setTeacherYear(teacherYear-i);
            teacherEvaluate77.setCollegeName("艺术学院");
            teacherEvaluate77.setCollegeId(1727L);
            teacherEvaluate77.setCourseCode("322732");
            teacherEvaluate77.setCourseName("素描2--322732--1");
            teacherEvaluate77.setTeacherId("275216");
            teacherEvaluate77.setTeacherName("陈曦");
            teacherEvaluate77.setAvgScore(4.9f);
            teacherEvaluate77.setGrade(2017);
            teacherEvaluate77.setSemester(2);
            teacherEvaluate77.setClassId("614");;
            teacherEvaluate77.setClassName("艺术视传05-3");

            list.add(teacherEvaluate77);

            TeacherEvaluate teacherEvaluate8=new TeacherEvaluate();

            teacherEvaluate8.setId(UUID.randomUUID().toString());
            teacherEvaluate8.setOrgId(orgId);
            teacherEvaluate8.setTeacherYear(teacherYear-i);
            teacherEvaluate8.setCollegeName("外国语学院");
            teacherEvaluate8.setCollegeId(1728L);
            teacherEvaluate8.setCourseCode("511730");
            teacherEvaluate8.setCourseName("中级英语口语--511730--3");
            teacherEvaluate8.setTeacherId("2751216");
            teacherEvaluate8.setTeacherName("刘光艳");
            teacherEvaluate8.setAvgScore(4.5f);
            teacherEvaluate8.setGrade(2017);
            teacherEvaluate8.setSemester(1);
            teacherEvaluate8.setClassId("614121");;
            teacherEvaluate8.setClassName("会计05-2");

            list.add(teacherEvaluate8);

            TeacherEvaluate teacherEvaluate88=new TeacherEvaluate();

            teacherEvaluate88.setId(UUID.randomUUID().toString());
            teacherEvaluate88.setOrgId(orgId);
            teacherEvaluate88.setTeacherYear(teacherYear-i);
            teacherEvaluate88.setCollegeName("外国语学院");
            teacherEvaluate88.setCollegeId(1728L);
            teacherEvaluate88.setCourseCode("511730");
            teacherEvaluate88.setCourseName("中级英语口语--511730--3");
            teacherEvaluate88.setTeacherId("2751216");
            teacherEvaluate88.setTeacherName("刘光艳");
            teacherEvaluate88.setAvgScore(4.5f);
            teacherEvaluate88.setGrade(2017);
            teacherEvaluate88.setSemester(1);
            teacherEvaluate88.setClassId("614121");;
            teacherEvaluate88.setClassName("会计05-2");

            list.add(teacherEvaluate88);

        }
        teacherEvaluateService.saveList(list);
        Map<String, Object> result = new HashMap<String, Object>();
        return new ResponseEntity<Map<String, Object>>(result, HttpStatus.OK);
    }
    /**
     * 增加课程评价
     * @param
     * @return
     */
    @PostMapping(value = "/addcourseevaluate", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(httpMethod = "POST", value = "课程评价", response = Void.class, notes = "增加课程评价数据<br><br><b>@author wangjun</b>")
    public ResponseEntity<Map<String, Object>> addCourseEvaluate(@ApiParam(value = "增加课程评价数据") @RequestBody CourseEvaluate courseEvaluate) {
        Map<String, Object> result = new HashMap<String, Object>();
        try{
            courseEvaluateService.save(courseEvaluate);
        }catch (Exception e){
            e.printStackTrace();
            return new ResponseEntity<Map<String, Object>>(result, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return new ResponseEntity<Map<String, Object>>(result, HttpStatus.OK);
    }
    /**
     * 修改课程评价
     * @param
     * @return
     */
    @PostMapping(value = "/updatecourseevaluate", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(httpMethod = "PUT", value = "课程评价", response = Void.class, notes = "修改课程评价数据<br><br><b>@author wangjun</b>")
    public ResponseEntity<Map<String, Object>> updateCourseEvaluate(@ApiParam(value = "修改课程评价数据") @RequestBody CourseEvaluate courseEvaluate) {
        Map<String, Object> result = new HashMap<String, Object>();
        try{
            courseEvaluateService.update(courseEvaluate);
        }catch (Exception e){
            e.printStackTrace();
            return new ResponseEntity<Map<String, Object>>(result, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return new ResponseEntity<Map<String, Object>>(result, HttpStatus.OK);
    }
    /**
     * 增加教师评价
     * @param
     * @return
     */
    @PostMapping(value = "/addteacherevaluate", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(httpMethod = "POST", value = "教师评价", response = Void.class, notes = "增加教师评价数据<br><br><b>@author wangjun</b>")
    public ResponseEntity<Map<String, Object>> addCourseEvaluate(@ApiParam(value = "增加教师评价数据") @RequestBody TeacherEvaluate teacherEvaluate) {
        Map<String, Object> result = new HashMap<String, Object>();
        try{
            teacherEvaluateService.save(teacherEvaluate);
        }catch (Exception e){
            e.printStackTrace();
            return new ResponseEntity<Map<String, Object>>(result, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return new ResponseEntity<Map<String, Object>>(result, HttpStatus.OK);
    }
    /**
     * 修改教师评价
     * @param
     * @return
     */
    @PostMapping(value = "/updateteacherevaluate", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(httpMethod = "PUT", value = "教师评价", response = Void.class, notes = "修改教师评价数据<br><br><b>@author wangjun</b>")
    public ResponseEntity<Map<String, Object>> updateCourseEvaluate(@ApiParam(value = "修改教师评价数据") @RequestBody TeacherEvaluate teacherEvaluate) {
        Map<String, Object> result = new HashMap<String, Object>();
        try{
            teacherEvaluateService.save(teacherEvaluate);
        }catch (Exception e){
            e.printStackTrace();
            return new ResponseEntity<Map<String, Object>>(result, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return new ResponseEntity<Map<String, Object>>(result, HttpStatus.OK);
    }
}
