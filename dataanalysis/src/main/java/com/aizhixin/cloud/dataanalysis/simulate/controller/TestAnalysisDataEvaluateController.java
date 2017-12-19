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
        List<CourseEvaluate> list=new ArrayList<CourseEvaluate>();
        for(int i=0;i<num;i++){
            for(int j=1;j<3;j++){
                int max = 5;
                int min = 2;
                Random random = new Random();
                int s = 0;
                if (i > 0) {
                    s = random.nextInt(max) % (max - min + 1) + min;
                }
            CourseEvaluate courseEvaluate1=new CourseEvaluate();

            courseEvaluate1.setId(UUID.randomUUID().toString());
            courseEvaluate1.setOrgId(orgId);
            courseEvaluate1.setTeacherYear(teacherYear-i);
            courseEvaluate1.setCollegeName("机械与控制工程学院");
            courseEvaluate1.setCollegeId(1722L);
            courseEvaluate1.setCourseCode("90090");
            courseEvaluate1.setCourseName("大学物理");
            courseEvaluate1.setChargePersonID("182504");
            courseEvaluate1.setChargePerson("雷军乐");
            courseEvaluate1.setAvgScore(4.2f+s);
            courseEvaluate1.setCourseOpenDepartment("机械与控制工程学院");
            courseEvaluate1.setCourseOpenID("1722");
            courseEvaluate1.setGrade(2017);
            courseEvaluate1.setSemester(j);
            courseEvaluate1.setTeachingClassId("10384");;
            courseEvaluate1.setTeachingClassName("机械2016-1班");

            list.add(courseEvaluate1);

            CourseEvaluate courseEvaluate2=new CourseEvaluate();

            courseEvaluate2.setId(UUID.randomUUID().toString());
            courseEvaluate2.setOrgId(orgId);
            courseEvaluate2.setTeacherYear(teacherYear-i);
            courseEvaluate2.setCollegeName("机械与控制工程学院");
            courseEvaluate2.setCollegeId(1722L);
            courseEvaluate2.setCourseCode("182504");
            courseEvaluate2.setCourseName("大学物理");
            courseEvaluate2.setChargePersonID("22");
            courseEvaluate2.setChargePerson("雷军乐");
            courseEvaluate2.setAvgScore(4.8f-s);
            courseEvaluate2.setCourseOpenDepartment("机械与控制工程学院");
            courseEvaluate2.setCourseOpenID("207");
            courseEvaluate2.setGrade(2017);
            courseEvaluate2.setSemester(j);
            courseEvaluate2.setTeachingClassId("10386");;
            courseEvaluate2.setTeachingClassName("机械2016-2班");

            list.add(courseEvaluate2);

                if (i > 0) {
                    s = random.nextInt(max) % (max - min + 1) + min;
                }
            CourseEvaluate courseEvaluate3=new CourseEvaluate();

            courseEvaluate3.setId(UUID.randomUUID().toString());
            courseEvaluate3.setOrgId(orgId);
            courseEvaluate3.setTeacherYear(teacherYear-i);
            courseEvaluate3.setCollegeName("外国语学院");
            courseEvaluate3.setCollegeId(1758L);
            courseEvaluate3.setCourseCode("334600");
            courseEvaluate3.setCourseName("日语");
            courseEvaluate3.setChargePersonID("182522");
            courseEvaluate3.setChargePerson("杨艳");
            courseEvaluate3.setAvgScore(4.9f+s);
            courseEvaluate3.setCourseOpenDepartment("外国语学院");
            courseEvaluate3.setCourseOpenID("1758");
            courseEvaluate3.setGrade(2017);
            courseEvaluate3.setSemester(j);
            courseEvaluate3.setTeachingClassId("616");;
            courseEvaluate3.setTeachingClassName("日语2016-1班");

            list.add(courseEvaluate3);




            CourseEvaluate courseEvaluate4=new CourseEvaluate();

            courseEvaluate4.setId(UUID.randomUUID().toString());
            courseEvaluate4.setOrgId(orgId);
            courseEvaluate4.setTeacherYear(teacherYear-i);
            courseEvaluate4.setCollegeName("测绘地理信息学院");
            courseEvaluate4.setCollegeId(1724L);
            courseEvaluate4.setCourseCode("040080");
            courseEvaluate4.setCourseName("测绘");
            courseEvaluate4.setChargePersonID("182508");
            courseEvaluate4.setChargePerson("范冬林");
            courseEvaluate4.setAvgScore(4.2f+s);
            courseEvaluate4.setCourseOpenDepartment("测绘地理信息学院");
            courseEvaluate4.setCourseOpenID("1724");
            courseEvaluate4.setGrade(2017);
            courseEvaluate4.setSemester(j);
            courseEvaluate4.setTeachingClassId("10286");;
            courseEvaluate4.setTeachingClassName("测绘类2016-5班");

            list.add(courseEvaluate4);


            CourseEvaluate courseEvaluate5=new CourseEvaluate();

            courseEvaluate5.setId(UUID.randomUUID().toString());
            courseEvaluate5.setOrgId(orgId);
            courseEvaluate5.setTeacherYear(teacherYear-i);
            courseEvaluate5.setCollegeName("人文社会科学学院");
            courseEvaluate5.setCollegeId(1754L);
            courseEvaluate5.setCourseCode("510520");
            courseEvaluate5.setCourseName("广告学");
            courseEvaluate5.setChargePersonID("182528");
            courseEvaluate5.setChargePerson("程丽");
            courseEvaluate5.setAvgScore(4.2f+s);
            courseEvaluate5.setCourseOpenDepartment("人文社会科学学院");
            courseEvaluate5.setCourseOpenID("1754");
            courseEvaluate5.setGrade(2017);
            courseEvaluate5.setSemester(j);
            courseEvaluate5.setTeachingClassId("10428");;
            courseEvaluate5.setTeachingClassName("广告2016-1班");

            list.add(courseEvaluate5);



            CourseEvaluate courseEvaluate6=new CourseEvaluate();

            courseEvaluate6.setId(UUID.randomUUID().toString());
            courseEvaluate6.setOrgId(orgId);
            courseEvaluate6.setTeacherYear(teacherYear-i);
            courseEvaluate6.setCollegeName("化学与生物工程学院");
            courseEvaluate6.setCollegeId(1720L);
            courseEvaluate6.setCourseCode("311860");
            courseEvaluate6.setCourseName("生物工程");
            courseEvaluate6.setChargePersonID("275116");
            courseEvaluate6.setChargePerson("刘帅");
            courseEvaluate6.setAvgScore(4.2f);
            courseEvaluate6.setCourseOpenDepartment("化学与生物工程学院");
            courseEvaluate6.setCourseOpenID("1720");
            courseEvaluate6.setGrade(2017);
            courseEvaluate6.setSemester(j);
            courseEvaluate6.setTeachingClassId("10360");;
            courseEvaluate6.setTeachingClassName("生物工程2016-1班");

            list.add(courseEvaluate6);

                if (i > 0) {
                    s = random.nextInt(max) % (max - min + 1) + min;
                }

            CourseEvaluate courseEvaluate7=new CourseEvaluate();

            courseEvaluate7.setId(UUID.randomUUID().toString());
            courseEvaluate7.setOrgId(orgId);
            courseEvaluate7.setTeacherYear(teacherYear-i);
            courseEvaluate7.setCollegeName("旅游学院");
            courseEvaluate7.setCollegeId(1752L);
            courseEvaluate7.setCourseCode("322732");
            courseEvaluate7.setCourseName("风景园林");
            courseEvaluate7.setChargePersonID("183974");
            courseEvaluate7.setChargePerson("罗薇");
            courseEvaluate7.setAvgScore(4.9f);
            courseEvaluate7.setCourseOpenDepartment("旅游学院");
            courseEvaluate7.setCourseOpenID("1752");
            courseEvaluate7.setGrade(2017);
            courseEvaluate7.setSemester(j);
            courseEvaluate7.setTeachingClassId("10422");
            courseEvaluate7.setTeachingClassName("风景园林2016-1班");

            list.add(courseEvaluate7);



            CourseEvaluate courseEvaluate8=new CourseEvaluate();

            courseEvaluate8.setId(UUID.randomUUID().toString());
            courseEvaluate8.setOrgId(orgId);
            courseEvaluate8.setTeacherYear(teacherYear-i);
            courseEvaluate8.setCollegeName("信息科学与工程学院");
            courseEvaluate8.setCollegeId(1718L);
            courseEvaluate8.setCourseCode("511730");
            courseEvaluate8.setCourseName("信息科学");
            courseEvaluate8.setChargePersonID("183200");
            courseEvaluate8.setChargePerson("蒋存波");
            courseEvaluate8.setAvgScore(4.5f+s);
            courseEvaluate8.setCourseOpenDepartment("信息科学与工程学院");
            courseEvaluate8.setCourseOpenID("1718");
            courseEvaluate8.setGrade(2017);
            courseEvaluate8.setSemester(j);
            courseEvaluate8.setTeachingClassId("10530");;
            courseEvaluate8.setTeachingClassName("电信类2016-2班");

            list.add(courseEvaluate8);

            CourseEvaluate courseEvaluate9=new CourseEvaluate();

            courseEvaluate9.setId(UUID.randomUUID().toString());
            courseEvaluate9.setOrgId(orgId);
            courseEvaluate9.setTeacherYear(teacherYear-i);
            courseEvaluate9.setCollegeName("管理学院");
            courseEvaluate9.setCollegeId(1750L);
            courseEvaluate9.setCourseCode("511730");
            courseEvaluate9.setCourseName("国际贸易");
            courseEvaluate9.setChargePersonID("183980");
            courseEvaluate9.setChargePerson("李隽波");
            courseEvaluate9.setAvgScore(4.5f);
            courseEvaluate9.setCourseOpenDepartment("管理学院");
            courseEvaluate9.setCourseOpenID("1750");
            courseEvaluate9.setGrade(2017);
            courseEvaluate9.setSemester(j);
            courseEvaluate9.setTeachingClassId("10334");;
            courseEvaluate9.setTeachingClassName("国贸2016-1班");

            list.add(courseEvaluate9);
        }}
        courseEvaluateService.saveList(list);
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
            for(int j=1;j<3;j++){
                int max = 5;
                int min = 2;
                Random random = new Random();
                int s = 0;
                if (i > 0) {
                    s = random.nextInt(max) % (max - min + 1) + min;
                }
                TeacherEvaluate teacherEvaluate1=new TeacherEvaluate();

                teacherEvaluate1.setId(UUID.randomUUID().toString());
                teacherEvaluate1.setOrgId(orgId);
                teacherEvaluate1.setTeacherYear(teacherYear-i);
                teacherEvaluate1.setCollegeName("机械与控制工程学院");
                teacherEvaluate1.setCollegeId(1722L);
                teacherEvaluate1.setCourseCode("90090");
                teacherEvaluate1.setCourseName("大学物理");
                teacherEvaluate1.setTeacherId("182504");
                teacherEvaluate1.setTeacherName("雷军乐");
                teacherEvaluate1.setAvgScore(4.2f+s);
                teacherEvaluate1.setGrade(2017);
                teacherEvaluate1.setSemester(j);
                teacherEvaluate1.setClassId("10384");
                teacherEvaluate1.setClassName("机械2016-1班");

                list.add(teacherEvaluate1);

                TeacherEvaluate teacherEvaluate2=new TeacherEvaluate();

                teacherEvaluate2.setId(UUID.randomUUID().toString());
                teacherEvaluate2.setOrgId(orgId);
                teacherEvaluate2.setTeacherYear(teacherYear-i);
                teacherEvaluate2.setCollegeName("机械与控制工程学院");
                teacherEvaluate2.setCollegeId(1722L);
                teacherEvaluate2.setCourseCode("182504");
                teacherEvaluate2.setCourseName("大学物理");
                teacherEvaluate2.setTeacherId("22");
                teacherEvaluate2.setTeacherName("雷军乐");
                teacherEvaluate2.setAvgScore(4.8f-s);
                teacherEvaluate2.setGrade(2017);
                teacherEvaluate2.setSemester(j);
                teacherEvaluate2.setClassId("10386");;
                teacherEvaluate2.setClassId("机械2016-2班");

                list.add(teacherEvaluate2);

                if (i > 0) {
                    s = random.nextInt(max) % (max - min + 1) + min;
                }
                TeacherEvaluate teacherEvaluate3=new TeacherEvaluate();

                teacherEvaluate3.setId(UUID.randomUUID().toString());
                teacherEvaluate3.setOrgId(orgId);
                teacherEvaluate3.setTeacherYear(teacherYear-i);
                teacherEvaluate3.setCollegeName("外国语学院");
                teacherEvaluate3.setCollegeId(1758L);
                teacherEvaluate3.setCourseCode("334600");
                teacherEvaluate3.setCourseName("日语");
                teacherEvaluate3.setTeacherId("182522");
                teacherEvaluate3.setTeacherName("杨艳");
                teacherEvaluate3.setAvgScore(4.9f+s);
                teacherEvaluate3.setGrade(2017);
                teacherEvaluate3.setSemester(j);
                teacherEvaluate3.setClassId("616");;
                teacherEvaluate3.setClassId("日语2016-1班");

                list.add(teacherEvaluate3);




                TeacherEvaluate teacherEvaluate4=new TeacherEvaluate();

                teacherEvaluate4.setId(UUID.randomUUID().toString());
                teacherEvaluate4.setOrgId(orgId);
                teacherEvaluate4.setTeacherYear(teacherYear-i);
                teacherEvaluate4.setCollegeName("测绘地理信息学院");
                teacherEvaluate4.setCollegeId(1724L);
                teacherEvaluate4.setCourseCode("040080");
                teacherEvaluate4.setCourseName("测绘");
                teacherEvaluate4.setTeacherId("182508");
                teacherEvaluate4.setTeacherName("范冬林");
                teacherEvaluate4.setAvgScore(4.2f+s);
                teacherEvaluate4.setGrade(2017);
                teacherEvaluate4.setSemester(j);
                teacherEvaluate4.setClassId("10286");;
                teacherEvaluate4.setClassId("测绘类2016-5班");

                list.add(teacherEvaluate4);


                TeacherEvaluate teacherEvaluate5=new TeacherEvaluate();

                teacherEvaluate5.setId(UUID.randomUUID().toString());
                teacherEvaluate5.setOrgId(orgId);
                teacherEvaluate5.setTeacherYear(teacherYear-i);
                teacherEvaluate5.setCollegeName("人文社会科学学院");
                teacherEvaluate5.setCollegeId(1754L);
                teacherEvaluate5.setCourseCode("510520");
                teacherEvaluate5.setCourseName("广告学");
                teacherEvaluate5.setTeacherId("182528");
                teacherEvaluate5.setTeacherName("程丽");
                teacherEvaluate5.setAvgScore(4.2f+s);
                teacherEvaluate5.setGrade(2017);
                teacherEvaluate5.setSemester(j);
                teacherEvaluate5.setClassId("10428");;
                teacherEvaluate5.setClassId("广告2016-1班");

                list.add(teacherEvaluate5);



                TeacherEvaluate teacherEvaluate6=new TeacherEvaluate();

                teacherEvaluate6.setId(UUID.randomUUID().toString());
                teacherEvaluate6.setOrgId(orgId);
                teacherEvaluate6.setTeacherYear(teacherYear-i);
                teacherEvaluate6.setCollegeName("化学与生物工程学院");
                teacherEvaluate6.setCollegeId(1720L);
                teacherEvaluate6.setCourseCode("311860");
                teacherEvaluate6.setCourseName("生物工程");
                teacherEvaluate6.setTeacherId("275116");
                teacherEvaluate6.setTeacherName("刘帅");
                teacherEvaluate6.setAvgScore(4.2f);
                teacherEvaluate6.setGrade(2017);
                teacherEvaluate6.setSemester(j);
                teacherEvaluate6.setClassId("10360");;
                teacherEvaluate6.setClassId("生物工程2016-1班");

                list.add(teacherEvaluate6);

                if (i > 0) {
                    s = random.nextInt(max) % (max - min + 1) + min;
                }

                TeacherEvaluate teacherEvaluate7=new TeacherEvaluate();

                teacherEvaluate7.setId(UUID.randomUUID().toString());
                teacherEvaluate7.setOrgId(orgId);
                teacherEvaluate7.setTeacherYear(teacherYear-i);
                teacherEvaluate7.setCollegeName("旅游学院");
                teacherEvaluate7.setCollegeId(1752L);
                teacherEvaluate7.setCourseCode("322732");
                teacherEvaluate7.setCourseName("风景园林");
                teacherEvaluate7.setTeacherId("183974");
                teacherEvaluate7.setTeacherName("罗薇");
                teacherEvaluate7.setAvgScore(4.9f);
                teacherEvaluate7.setGrade(2017);
                teacherEvaluate7.setSemester(j);
                teacherEvaluate7.setClassId("10422");
                teacherEvaluate7.setClassId("风景园林2016-1班");

                list.add(teacherEvaluate7);



                TeacherEvaluate teacherEvaluate8=new TeacherEvaluate();

                teacherEvaluate8.setId(UUID.randomUUID().toString());
                teacherEvaluate8.setOrgId(orgId);
                teacherEvaluate8.setTeacherYear(teacherYear-i);
                teacherEvaluate8.setCollegeName("信息科学与工程学院");
                teacherEvaluate8.setCollegeId(1718L);
                teacherEvaluate8.setCourseCode("511730");
                teacherEvaluate8.setCourseName("信息科学");
                teacherEvaluate8.setTeacherId("183200");
                teacherEvaluate8.setTeacherName("蒋存波");
                teacherEvaluate8.setAvgScore(4.5f+s);
                teacherEvaluate8.setGrade(2017);
                teacherEvaluate8.setSemester(j);
                teacherEvaluate8.setClassId("10530");;
                teacherEvaluate8.setClassId("电信类2016-2班");

                list.add(teacherEvaluate8);

                TeacherEvaluate teacherEvaluate9=new TeacherEvaluate();

                teacherEvaluate9.setId(UUID.randomUUID().toString());
                teacherEvaluate9.setOrgId(orgId);
                teacherEvaluate9.setTeacherYear(teacherYear-i);
                teacherEvaluate9.setCollegeName("管理学院");
                teacherEvaluate9.setCollegeId(1750L);
                teacherEvaluate9.setCourseCode("511730");
                teacherEvaluate9.setCourseName("国际贸易");
                teacherEvaluate9.setTeacherId("183980");
                teacherEvaluate9.setTeacherName("李隽波");
                teacherEvaluate9.setAvgScore(4.5f);
                teacherEvaluate9.setGrade(2017);
                teacherEvaluate9.setSemester(j);
                teacherEvaluate9.setClassId("10334");;
                teacherEvaluate9.setClassId("国贸2016-1班");

                list.add(teacherEvaluate9);
        }}
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
