/**
 * 
 */
package com.aizhixin.cloud.dataanalysis.simulate.controller;

import java.util.*;

import com.aizhixin.cloud.dataanalysis.analysis.job.CetStatisticsAnalysisJob;
import com.aizhixin.cloud.dataanalysis.analysis.job.TeachingScoreStatisticsJob;
import com.aizhixin.cloud.dataanalysis.analysis.service.TeachingScoreService;
import com.aizhixin.cloud.dataanalysis.studentRegister.service.StudentRegisterService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.aizhixin.cloud.dataanalysis.analysis.entity.PracticeStatistics;
import com.aizhixin.cloud.dataanalysis.analysis.job.SchoolStatisticsAnalysisJob;
import com.aizhixin.cloud.dataanalysis.analysis.service.CetStatisticAnalysisService;
import com.aizhixin.cloud.dataanalysis.analysis.service.PracticeStatisticsService;
import com.aizhixin.cloud.dataanalysis.analysis.service.SchoolStatisticsService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

/**
 * @author zhengning
 *
 */
@RestController
@RequestMapping("/v1/test/analysis")
@Api(value = "测试生成模拟数据操作", description = "测试生成模拟数据操作")
public class TestAnalysisDataController {
	private final static Logger log = LoggerFactory
			.getLogger(TestAnalysisDataController.class);

	@Autowired
	private PracticeStatisticsService practiceStatisticsService;
	@Autowired
	private SchoolStatisticsAnalysisJob schoolStatisticsAnalysisJob;
	@Autowired
	private TeachingScoreStatisticsJob teachingScoreStatisticsJob;
	@Autowired
	private StudentRegisterService studentRegisterService;
	@Autowired
	private TeachingScoreService teachingScoreService;

	@GetMapping(value = "/addschoolstatistics", produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(httpMethod = "GET", value = "生成学情分析学校人数统计", response = Void.class, notes = "生成学情分析学校人数统计<br><br><b>@author  jianwei.wu</b>")
	public Map<String, Object> addSchoolStatistics(
	) {
		return schoolStatisticsAnalysisJob.schoolStatistics();
	}

	@GetMapping(value = "/addteachingscorestatistics", produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(httpMethod = "GET", value = "手动添加教学成绩统计", response = Void.class, notes = "手动添加教学成绩统计<br><br><b>@author jianwei.wu</b>")
	public Map<String, Object> addTeachingScoreStatistics(
	) {
		return teachingScoreStatisticsJob.teachingScoreStatistics();
	}
	@GetMapping(value = "/addcetscorestatistics", produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(httpMethod = "GET", value = "手动添加cet成绩统计", response = Void.class, notes = "手动添加cet成绩统计<br><br><b>@author jianwei.wu</b>")
	public Map<String, Object> cetScoreStatistics(
	) {
		return teachingScoreStatisticsJob.cetScoreStatistics();
	}


	@GetMapping(value = "/deleteteachingstatistics", produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(httpMethod = "GET", value = "手动删除教学成绩统计数据", response = Void.class, notes = "手动删除教学成绩统计数据<br><br><b>@author jianwei.wu</b>")
	public void deleteTeachingStatistics(
			@ApiParam(value = "orgId 机构id", required = true) @RequestParam(value = "orgId", required = true) Long orgId,
			@ApiParam(value = "teacherYear 学年", required = true) @RequestParam(value = "teacherYear", required = true) Integer teacherYear,
			@ApiParam(value = "semester 学期", required = true) @RequestParam(value = "semester", required = true) Integer semester
	) {
		 teachingScoreService.deleteScoreStatistics(orgId, teacherYear,semester);
	}

//	@GetMapping(value = "/deleteallteachingstatistics", produces = MediaType.APPLICATION_JSON_VALUE)
//	@ApiOperation(httpMethod = "GET", value = "手动清空教学成绩统计数据", response = Void.class, notes = "手动清空教学成绩统计数据<br><br><b>@author jianwei.wu</b>")
//	public void deleteAllTeachingStatistics(
//			@ApiParam(value = "orgId 机构id", required = true) @RequestParam(value = "orgId", required = true) Long orgId
//	) {
//		teachingScoreService.deleteScoreStatistics(orgId);
//	}

	@GetMapping(value = "/deleteteachingdetail", produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(httpMethod = "GET", value = "手动删除教学成绩详情数据", response = Void.class, notes = "手动删除教学成绩详情数据<br><br><b>@author jianwei.wu</b>")
	public void deleteTeachingDetail(
			@ApiParam(value = "orgId 机构id", required = true) @RequestParam(value = "orgId", required = true) Long orgId,
			@ApiParam(value = "teacherYear 学年", required = true) @RequestParam(value = "teacherYear", required = true) Integer teacherYear,
			@ApiParam(value = "semester 学期", required = true) @RequestParam(value = "semester", required = true) Integer semester
	) {
		teachingScoreService.deleteScoreDeatail(orgId, teacherYear, semester);
	}


	@GetMapping(value = "/modifynewstudentinfo", produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(httpMethod = "GET", value = "手动修改新生报到注册状态", response = Void.class, notes = "手动修改新生报到注册状态<br><br><b>@author jianwei.wu</b>")
	public Map<String, Object> modifyNewStudentInfo(
			@ApiParam(value = "orgId 机构id", required = true) @RequestParam(value = "orgId", required = true) Long orgId,
			@ApiParam(value = "teacherYear 学年", required = true) @RequestParam(value = "teacherYear", required = true) Integer teacherYear
	) {
		return studentRegisterService.modifyNewStudentDetails(orgId, teacherYear);
	}

	@RequestMapping(value = "/addpracticestatistics", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(httpMethod = "POST", value = "生成学情分析学生实践统计数据(生成模拟数据前会清空表数据请谨慎使用)", response = Void.class, notes = "生成学情分析学生实践统计数据<br><br><b>@author zhengning</b>")
	public ResponseEntity<Map<String, Object>> addracticeStatistics(
			@ApiParam(value = "orgId 机构id") @RequestParam(value = "orgId", required = false) Long orgId,
			@ApiParam(value = "teacherYear 学年)") @RequestParam(value = "teacherYear", required = true) int teacherYear,
			@ApiParam(value = "num teacherYear几年内)") @RequestParam(value = "num", required = false) int num) {
		Map<String, Object> result = new HashMap<String, Object>();

		if (null != orgId && orgId.longValue() > 0L) {
		} else {
			orgId = 1L;
		}
		if (num == 0) {
			num = 1;
		}

		List<PracticeStatistics> practiceStatisticsList = new ArrayList<PracticeStatistics>();
		for (int i = 0; i < num; i++) {

			for(int j=1;j<3;j++){
			int max = 25;
			int min = 5;
			Random random = new Random();
			int s = random.nextInt(max) % (max - min + 1) + min;

			PracticeStatistics practiceStatistics1 = new PracticeStatistics();
			practiceStatistics1.setId(UUID.randomUUID().toString());
			practiceStatistics1.setOrgId(orgId);
			practiceStatistics1.setTeacherYear((teacherYear-i));
			practiceStatistics1.setCollegeName("机控学院");
			practiceStatistics1.setCollegeId(1722L);
			practiceStatistics1.setProfessionId(2492L);
			practiceStatistics1.setProfessionName("机械设计制造及其自动化");
			practiceStatistics1.setClassId(10384L);
			practiceStatistics1.setClassName("机械2016-1班");
			practiceStatistics1.setPracticeStudentNum(51L-s);
			practiceStatistics1.setPracticeCompanyNum(1L+i);
			practiceStatistics1.setTaskNum(545L-(s*4));
			practiceStatistics1.setTaskPassNum(530L-(s*4));
			practiceStatistics1.setSemester(j);
//			if(practiceStatistics1.getTeacherYear() == 2017 && practiceStatistics1.getSemester() == 2){
//				continue;
//			}else{
				practiceStatisticsList.add(practiceStatistics1);
//			}

			random = new Random();
			s = random.nextInt(max) % (max - min + 1) + min;

			PracticeStatistics practiceStatistics1a = new PracticeStatistics();
			practiceStatistics1a.setId(UUID.randomUUID().toString());
			practiceStatistics1a.setOrgId(orgId);
			practiceStatistics1a.setTeacherYear((teacherYear-i));
			practiceStatistics1a.setCollegeName("机控学院");
			practiceStatistics1a.setCollegeId(1722L);
			practiceStatistics1a.setProfessionId(2492L);
			practiceStatistics1a.setProfessionName("机械设计制造及其自动化");
			practiceStatistics1a.setClassId(10386L);
			practiceStatistics1a.setClassName("机械2016-2班");
			practiceStatistics1a.setPracticeStudentNum(49L-s);
			practiceStatistics1a.setPracticeCompanyNum(1L+i);
			practiceStatistics1a.setTaskNum(395L-(s*4));
			practiceStatistics1a.setTaskPassNum(370L-(s*4));
			practiceStatistics1a.setSemester(j);
			practiceStatisticsList.add(practiceStatistics1a);

			random = new Random();
			s = random.nextInt(max) % (max - min + 1) + min;

			PracticeStatistics practiceStatistics2a = new PracticeStatistics();
			practiceStatistics2a.setId(UUID.randomUUID().toString());
			practiceStatistics2a.setOrgId(orgId);
			practiceStatistics2a.setTeacherYear(teacherYear-i);
			practiceStatistics2a.setCollegeName("外语学院");
			practiceStatistics2a.setCollegeId(1758L);
			practiceStatistics2a.setProfessionId(2502L);
			practiceStatistics2a.setProfessionName("日语");
			practiceStatistics2a.setClassId(10792L);
			practiceStatistics2a.setClassName("日语2016-1班");
			practiceStatistics2a.setPracticeStudentNum(45L-s);
			practiceStatistics2a.setPracticeCompanyNum(1L+i);
			practiceStatistics2a.setTaskNum(325L-(s*4));
			practiceStatistics2a.setTaskPassNum(320L-(s*4));
			practiceStatistics2a.setSemester(j);
			practiceStatisticsList.add(practiceStatistics2a);

			random = new Random();
			s = random.nextInt(max) % (max - min + 1) + min;

			PracticeStatistics practiceStatistics2 = new PracticeStatistics();
			practiceStatistics2.setId(UUID.randomUUID().toString());
			practiceStatistics2.setOrgId(orgId);
			practiceStatistics2.setTeacherYear(teacherYear-i);
			practiceStatistics2.setCollegeName("外语学院");
			practiceStatistics2.setCollegeId(1758L);
			practiceStatistics2.setProfessionId(2502L);
			practiceStatistics2.setProfessionName("日语");
			practiceStatistics2.setClassId(10794L);
			practiceStatistics2.setClassName("日语2016-2班");
			practiceStatistics2.setPracticeStudentNum(55L-s);
			practiceStatistics2.setPracticeCompanyNum(1L+i);
			practiceStatistics2.setTaskNum(445L-(s*4));
			practiceStatistics2.setTaskPassNum(430L-(s*4));
			practiceStatistics2.setSemester(j);
			practiceStatisticsList.add(practiceStatistics2);

			random = new Random();
			s = random.nextInt(max) % (max - min + 1) + min;
			PracticeStatistics practiceStatistics3 = new PracticeStatistics();
			practiceStatistics3.setId(UUID.randomUUID().toString());
			practiceStatistics3.setOrgId(orgId);
			practiceStatistics3.setTeacherYear(teacherYear - i);
			practiceStatistics3.setCollegeName("测绘学院");
			practiceStatistics3.setCollegeId(1724L);
			practiceStatistics3.setProfessionId(2576L);
			practiceStatistics3.setProfessionName("测绘类");
			practiceStatistics3.setClassId(10286L);
			practiceStatistics3.setClassName("测绘类2016-5班");
			practiceStatistics3.setPracticeStudentNum(49L-s);
			practiceStatistics3.setPracticeCompanyNum(1L+i);
			practiceStatistics3.setTaskNum(485L-(s*4));
			practiceStatistics3.setTaskPassNum(470L-(s*4));
			practiceStatistics3.setSemester(j);
			practiceStatisticsList.add(practiceStatistics3);

			random = new Random();
			s = random.nextInt(max) % (max - min + 1) + min;

			PracticeStatistics practiceStatistics3a = new PracticeStatistics();
			practiceStatistics3a.setId(UUID.randomUUID().toString());
			practiceStatistics3a.setOrgId(orgId);
			practiceStatistics3a.setTeacherYear(teacherYear - i);
			practiceStatistics3a.setCollegeName("测绘学院");
			practiceStatistics3a.setCollegeId(1724L);
			practiceStatistics3a.setProfessionId(2576L);
			practiceStatistics3a.setProfessionName("测绘类");
			practiceStatistics3a.setClassId(10284L);
			practiceStatistics3a.setClassName("测绘类2016-4班");
			practiceStatistics3a.setPracticeStudentNum(48L-s);
			practiceStatistics3a.setPracticeCompanyNum(1L+i);
			practiceStatistics3a.setTaskNum(475L-(s*4));
			practiceStatistics3a.setTaskPassNum(460L-(s*4));
			practiceStatistics3a.setSemester(j);
			practiceStatisticsList.add(practiceStatistics3a);

			random = new Random();
			s = random.nextInt(max) % (max - min + 1) + min;
			PracticeStatistics practiceStatistics4 = new PracticeStatistics();
			practiceStatistics4.setId(UUID.randomUUID().toString());
			practiceStatistics4.setOrgId(orgId);
			practiceStatistics4.setTeacherYear(teacherYear - i);
			practiceStatistics4.setCollegeName("管媒学院");
			practiceStatistics4.setCollegeId(1754L);
			practiceStatistics4.setProfessionId(2530L);
			practiceStatistics4.setProfessionName("广告学");
			practiceStatistics4.setClassId(10428L);
			practiceStatistics4.setClassName("广告2016-1班");
			practiceStatistics4.setPracticeStudentNum(45L-s);
			practiceStatistics4.setPracticeCompanyNum(1L+i);
			practiceStatistics4.setTaskNum(426L-(s*4));
			practiceStatistics4.setTaskPassNum(411L-(s*4));
			practiceStatistics4.setSemester(j);
			practiceStatisticsList.add(practiceStatistics4);

			random = new Random();
			s = random.nextInt(max) % (max - min + 1) + min;
			PracticeStatistics practiceStatistics4a = new PracticeStatistics();
			practiceStatistics4a.setId(UUID.randomUUID().toString());
			practiceStatistics4a.setOrgId(orgId);
			practiceStatistics4a.setTeacherYear(teacherYear - i);
			practiceStatistics4a.setCollegeName("管媒学院");
			practiceStatistics4a.setCollegeId(1754L);
			practiceStatistics4a.setProfessionId(2530L);
			practiceStatistics4a.setProfessionName("广告学");
			practiceStatistics4a.setClassId(10430L);
			practiceStatistics4a.setClassName("广告2016-2班");
			practiceStatistics4a.setPracticeStudentNum(42L-s);
			practiceStatistics4a.setPracticeCompanyNum(1L+i);
			practiceStatistics4a.setTaskNum(423L-(s*4));
			practiceStatistics4a.setTaskPassNum(412L-(s*4));
			practiceStatistics4a.setSemester(j);
			practiceStatisticsList.add(practiceStatistics4a);

			random = new Random();
			s = random.nextInt(max) % (max - min + 1) + min;
			PracticeStatistics practiceStatistics5 = new PracticeStatistics();
			practiceStatistics5.setId(UUID.randomUUID().toString());
			practiceStatistics5.setOrgId(orgId);
			practiceStatistics5.setTeacherYear(teacherYear - i);
			practiceStatistics5.setCollegeName("化生学院");
			practiceStatistics5.setCollegeId(1720L);
			practiceStatistics5.setProfessionId(2480L);
			practiceStatistics5.setProfessionName("生物工程");
			practiceStatistics5.setClassId(10360L);
			practiceStatistics5.setClassName("生物工程2016-1班");
			practiceStatistics5.setPracticeStudentNum(59L-s);
			practiceStatistics5.setPracticeCompanyNum(1L+i);
			practiceStatistics5.setTaskNum(568L-(s*4));
			practiceStatistics5.setTaskPassNum(548L-(s*4));
			practiceStatistics5.setSemester(j);
			practiceStatisticsList.add(practiceStatistics5);

			random = new Random();
			s = random.nextInt(max) % (max - min + 1) + min;
			PracticeStatistics practiceStatistics5a = new PracticeStatistics();
			practiceStatistics5a.setId(UUID.randomUUID().toString());
			practiceStatistics5a.setOrgId(orgId);
			practiceStatistics5a.setTeacherYear(teacherYear - i);
			practiceStatistics5a.setCollegeName("化生学院");
			practiceStatistics5a.setCollegeId(1720L);
			practiceStatistics5a.setProfessionId(2480L);
			practiceStatistics5a.setProfessionName("生物工程");
			practiceStatistics5a.setClassId(10362L);
			practiceStatistics5a.setClassName("生物工程2016-2班");
			practiceStatistics5a.setPracticeStudentNum(58L-s);
			practiceStatistics5a.setPracticeCompanyNum(1L+i);
			practiceStatistics5a.setTaskNum(545L-(s*4));
			practiceStatistics5a.setTaskPassNum(530L-(s*4));
			practiceStatistics5a.setSemester(j);
			practiceStatisticsList.add(practiceStatistics5a);

			random = new Random();
			s = random.nextInt(max) % (max - min + 1) + min;
			PracticeStatistics practiceStatistics6a = new PracticeStatistics();
			practiceStatistics6a.setId(UUID.randomUUID().toString());
			practiceStatistics6a.setOrgId(orgId);
			practiceStatistics6a.setTeacherYear(teacherYear - i);
			practiceStatistics6a.setCollegeName("旅游学院");
			practiceStatistics6a.setCollegeId(1752L);
			practiceStatistics6a.setProfessionId(2554L);
			practiceStatistics6a.setProfessionName("风景园林");
			practiceStatistics6a.setClassId(10422L);
			practiceStatistics6a.setClassName("风景园林2016-1班");
			practiceStatistics6a.setPracticeStudentNum(48L-s);
			practiceStatistics6a.setPracticeCompanyNum(1L+i);
			practiceStatistics6a.setTaskNum(479L-(s*4));
			practiceStatistics6a.setTaskPassNum(463L-(s*4));
			practiceStatistics6a.setSemester(j);
			practiceStatisticsList.add(practiceStatistics6a);

			random = new Random();
			s = random.nextInt(max) % (max - min + 1) + min;
			PracticeStatistics practiceStatistics6 = new PracticeStatistics();
			practiceStatistics6.setId(UUID.randomUUID().toString());
			practiceStatistics6.setOrgId(orgId);
			practiceStatistics6.setTeacherYear(teacherYear - i);
			practiceStatistics6.setCollegeName("旅游学院");
			practiceStatistics6.setCollegeId(1752L);
			practiceStatistics6.setProfessionId(2554L);
			practiceStatistics6.setProfessionName("风景园林");
			practiceStatistics6.setClassId(10424L);
			practiceStatistics6.setClassName("风景园林2016-2班");
			practiceStatistics6.setPracticeStudentNum(49L-s);
			practiceStatistics6.setPracticeCompanyNum(1L+i);
			practiceStatistics6.setTaskNum(489L-(s*4));
			practiceStatistics6.setTaskPassNum(460L-(s*4));
			practiceStatistics6.setSemester(j);
			practiceStatisticsList.add(practiceStatistics6);

			random = new Random();
			s = random.nextInt(max) % (max - min + 1) + min;
			PracticeStatistics practiceStatistics7a = new PracticeStatistics();
			practiceStatistics7a.setId(UUID.randomUUID().toString());
			practiceStatistics7a.setOrgId(orgId);
			practiceStatistics7a.setTeacherYear(teacherYear - i);
			practiceStatistics7a.setCollegeName("信息学院");
			practiceStatistics7a.setCollegeId(1718L);
			practiceStatistics7a.setProfessionId(2380L);
			practiceStatistics7a.setProfessionName("电子信息类");
			practiceStatistics7a.setClassId(10530L);
			practiceStatistics7a.setClassName("电信类2016-2班");
			practiceStatistics7a.setPracticeStudentNum(58L-s);
			practiceStatistics7a.setPracticeCompanyNum(1L+i);
			practiceStatistics7a.setTaskNum(545L-(s*4));
			practiceStatistics7a.setTaskPassNum(510L-(s*4));
			practiceStatistics7a.setSemester(j);
			practiceStatisticsList.add(practiceStatistics7a);

			random = new Random();
			s = random.nextInt(max) % (max - min + 1) + min;
			PracticeStatistics practiceStatistics7 = new PracticeStatistics();
			practiceStatistics7.setId(UUID.randomUUID().toString());
			practiceStatistics7.setOrgId(orgId);
			practiceStatistics7.setTeacherYear(teacherYear - i);
			practiceStatistics7.setCollegeName("信息学院");
			practiceStatistics7.setCollegeId(1718L);
			practiceStatistics7.setProfessionId(2380L);
			practiceStatistics7.setProfessionName("电子信息类");
			practiceStatistics7.setClassId(10532L);
			practiceStatistics7.setClassName("电信类2016-3班");
			practiceStatistics7.setPracticeStudentNum(55L-s);
			practiceStatistics7.setPracticeCompanyNum(1L+i);
			practiceStatistics7.setTaskNum(545L-(s*4));
			practiceStatistics7.setTaskPassNum(517L-(s*4));
			practiceStatistics7.setSemester(j);
			practiceStatisticsList.add(practiceStatistics7);

			random = new Random();
			s = random.nextInt(max) % (max - min + 1) + min;
			PracticeStatistics practiceStatistics8 = new PracticeStatistics();
			practiceStatistics8.setId(UUID.randomUUID().toString());
			practiceStatistics8.setOrgId(orgId);
			practiceStatistics8.setTeacherYear(teacherYear - i);
			practiceStatistics8.setCollegeName("管理学院");
			practiceStatistics8.setCollegeId(1750L);
			practiceStatistics8.setProfessionId(2520L);
			practiceStatistics8.setProfessionName("国际经济与贸易（应用）");
			practiceStatistics8.setClassId(10334L);
			practiceStatistics8.setClassName("国贸2016-1班");
			practiceStatistics8.setPracticeStudentNum(49L-s);
			practiceStatistics8.setPracticeCompanyNum(1L+i);
			practiceStatistics8.setTaskNum(415L-(s*4));
			practiceStatistics8.setTaskPassNum(400L-(s*4));
			practiceStatistics8.setSemester(j);
			practiceStatisticsList.add(practiceStatistics8);

			random = new Random();
			s = random.nextInt(max) % (max - min + 1) + min;
			PracticeStatistics practiceStatistics8a = new PracticeStatistics();
			practiceStatistics8a.setId(UUID.randomUUID().toString());
			practiceStatistics8a.setOrgId(orgId);
			practiceStatistics8a.setTeacherYear(teacherYear - i);
			practiceStatistics8a.setCollegeName("管理学院");
			practiceStatistics8a.setCollegeId(1750L);
			practiceStatistics8a.setProfessionId(2520L);
			practiceStatistics8a.setProfessionName("国际经济与贸易（应用）");
			practiceStatistics8a.setClassId(10336L);
			practiceStatistics8a.setClassName("国贸2016-2班");
			practiceStatistics8a.setPracticeStudentNum(43L-s);
			practiceStatistics8a.setPracticeCompanyNum(1L+i);
			practiceStatistics8a.setTaskNum(452L-(s*4));
			practiceStatistics8a.setTaskPassNum(421L-(s*4));
			practiceStatistics8a.setSemester(j);
			practiceStatisticsList.add(practiceStatistics8a);

			random = new Random();
			s = random.nextInt(max) % (max - min + 1) + min;
			PracticeStatistics practiceStatistics9a = new PracticeStatistics();
			practiceStatistics9a.setId(UUID.randomUUID().toString());
			practiceStatistics9a.setOrgId(orgId);
			practiceStatistics9a.setTeacherYear(teacherYear - i);
			practiceStatistics9a.setCollegeName("材料学院");
			practiceStatistics9a.setCollegeId(1716L);
			practiceStatistics9a.setProfessionId(2404L);
			practiceStatistics9a.setProfessionName("材料类（创新班）");
			practiceStatistics9a.setClassId(10270L);
			practiceStatistics9a.setClassName("材料类2016-6班");
			practiceStatistics9a.setPracticeStudentNum(56L-s);
			practiceStatistics9a.setPracticeCompanyNum(1L+i);
			practiceStatistics9a.setTaskNum(515L-(s*4));
			practiceStatistics9a.setTaskPassNum(510L-(s*4));
			practiceStatistics9a.setSemester(j);
			practiceStatisticsList.add(practiceStatistics9a);

			random = new Random();
			s = random.nextInt(max) % (max - min + 1) + min;
			PracticeStatistics practiceStatistics9 = new PracticeStatistics();
			practiceStatistics9.setId(UUID.randomUUID().toString());
			practiceStatistics9.setOrgId(orgId);
			practiceStatistics9.setTeacherYear(teacherYear - i);
			practiceStatistics9.setCollegeName("材料学院");
			practiceStatistics9.setCollegeId(1716L);
			practiceStatistics9.setProfessionId(2404L);
			practiceStatistics9.setProfessionName("材料类（创新班）");
			practiceStatistics9.setClassId(10272L);
			practiceStatistics9.setClassName("材料类2016-7班");
			practiceStatistics9.setPracticeStudentNum(55L-s);
			practiceStatistics9.setPracticeCompanyNum(1L+i);
			practiceStatistics9.setTaskNum(555L-(s*4));
			practiceStatistics9.setTaskPassNum(528L-(s*4));
			practiceStatistics9.setSemester(j);
			practiceStatisticsList.add(practiceStatistics9);

			random = new Random();
			s = random.nextInt(max) % (max - min + 1) + min;
			PracticeStatistics practiceStatistics10a = new PracticeStatistics();
			practiceStatistics10a.setId(UUID.randomUUID().toString());
			practiceStatistics10a.setOrgId(orgId);
			practiceStatistics10a.setTeacherYear(teacherYear - i);
			practiceStatistics10a.setCollegeName("理学院");
			practiceStatistics10a.setCollegeId(1748L);
			practiceStatistics10a.setProfessionId(2560L);
			practiceStatistics10a.setProfessionName("应用统计学");
			practiceStatistics10a.setClassId(10406L);
			practiceStatistics10a.setClassName("应用统计2016-2班");
			practiceStatistics10a.setPracticeStudentNum(52L-s);
			practiceStatistics10a.setPracticeCompanyNum(1L+i);
			practiceStatistics10a.setTaskNum(563L-(s*4));
			practiceStatistics10a.setTaskPassNum(548L-(s*4));
			practiceStatistics10a.setSemester(j);
			practiceStatisticsList.add(practiceStatistics10a);

			random = new Random();
			s = random.nextInt(max) % (max - min + 1) + min;
			PracticeStatistics practiceStatistics10 = new PracticeStatistics();
			practiceStatistics10.setId(UUID.randomUUID().toString());
			practiceStatistics10.setOrgId(orgId);
			practiceStatistics10.setTeacherYear(teacherYear - i);
			practiceStatistics10.setCollegeName("理学院");
			practiceStatistics10.setCollegeId(1748L);
			practiceStatistics10.setProfessionId(2560L);
			practiceStatistics10.setProfessionName("应用统计学");
			practiceStatistics10.setClassId(10408L);
			practiceStatistics10.setClassName("应用统计2016-3班");
			practiceStatistics10.setPracticeStudentNum(55L-s);
			practiceStatistics10.setPracticeCompanyNum(1L+i);
			practiceStatistics10.setTaskNum(723L-(s*4));
			practiceStatistics10.setTaskPassNum(711L-(s*4));
			practiceStatistics10.setSemester(j);
			practiceStatisticsList.add(practiceStatistics10);

			random = new Random();
			s = random.nextInt(max) % (max - min + 1) + min;
			PracticeStatistics practiceStatistics11a = new PracticeStatistics();
			practiceStatistics11a.setId(UUID.randomUUID().toString());
			practiceStatistics11a.setOrgId(orgId);
			practiceStatistics11a.setTeacherYear(teacherYear - i);
			practiceStatistics11a.setCollegeName("土建学院");
			practiceStatistics11a.setCollegeId(1714L);
			practiceStatistics11a.setProfessionId(2378L);
			practiceStatistics11a.setProfessionName("土木类");
			practiceStatistics11a.setClassId(10448L);
			practiceStatistics11a.setClassName("土木类2016-5班");
			practiceStatistics11a.setPracticeStudentNum(55L-s);
			practiceStatistics11a.setPracticeCompanyNum(1L+i);
			practiceStatistics11a.setTaskNum(619L-(s*4));
			practiceStatistics11a.setTaskPassNum(598L-(s*4));
			practiceStatistics11a.setSemester(j);
			practiceStatisticsList.add(practiceStatistics11a);


			random = new Random();
			s = random.nextInt(max) % (max - min + 1) + min;
			PracticeStatistics practiceStatistics11 = new PracticeStatistics();
			practiceStatistics11.setId(UUID.randomUUID().toString());
			practiceStatistics11.setOrgId(orgId);
			practiceStatistics11.setTeacherYear(teacherYear - i);
			practiceStatistics11.setCollegeName("土建学院");
			practiceStatistics11.setCollegeId(1714L);
			practiceStatistics11.setProfessionId(2378L);
			practiceStatistics11.setProfessionName("土木类");
			practiceStatistics11.setClassId(10450L);
			practiceStatistics11.setClassName("土木类2016-6班");
			practiceStatistics11.setPracticeStudentNum(54L-s);
			practiceStatistics11.setPracticeCompanyNum(1L+i);
			practiceStatistics11.setTaskNum(655L-(s*4));
			practiceStatistics11.setTaskPassNum(621L-(s*4));
			practiceStatistics11.setSemester(j);
			practiceStatisticsList.add(practiceStatistics11);

			random = new Random();
			s = random.nextInt(max) % (max - min + 1) + min;
			PracticeStatistics practiceStatistics12 = new PracticeStatistics();
			practiceStatistics12.setId(UUID.randomUUID().toString());
			practiceStatistics12.setOrgId(orgId);
			practiceStatistics12.setTeacherYear(teacherYear - i);
			practiceStatistics12.setCollegeName("环境学院");
			practiceStatistics12.setCollegeId(1712L);
			practiceStatistics12.setProfessionId(2406L);
			practiceStatistics12.setProfessionName("给排水科学与工程");
			practiceStatistics12.setClassId(10370L);
			practiceStatistics12.setClassName("给排水2016-1班");
			practiceStatistics12.setPracticeStudentNum(54L-s);
			practiceStatistics12.setPracticeCompanyNum(1L+i);
			practiceStatistics12.setTaskNum(543L-(s*4));
			practiceStatistics12.setTaskPassNum(532L-(s*4));
			practiceStatistics12.setSemester(j);
			practiceStatisticsList.add(practiceStatistics12);

			random = new Random();
			s = random.nextInt(max) % (max - min + 1) + min;
			PracticeStatistics practiceStatistics12a = new PracticeStatistics();
			practiceStatistics12a.setId(UUID.randomUUID().toString());
			practiceStatistics12a.setOrgId(orgId);
			practiceStatistics12a.setTeacherYear(teacherYear - i);
			practiceStatistics12a.setCollegeName("环境学院");
			practiceStatistics12a.setCollegeId(1712L);
			practiceStatistics12a.setProfessionId(2406L);
			practiceStatistics12a.setProfessionName("给排水科学与工程");
			practiceStatistics12a.setClassId(10372L);
			practiceStatistics12a.setClassName("给排水2016-2班");
			practiceStatistics12a.setPracticeStudentNum(50L-s);
			practiceStatistics12a.setPracticeCompanyNum(1L+i);
			practiceStatistics12a.setTaskNum(558L-(s*4));
			practiceStatistics12a.setTaskPassNum(526L-(s*4));
			practiceStatistics12a.setSemester(j);
			practiceStatisticsList.add(practiceStatistics12a);

			random = new Random();
			s = random.nextInt(max) % (max - min + 1) + min;
			PracticeStatistics practiceStatistics13a = new PracticeStatistics();
			practiceStatistics13a.setId(UUID.randomUUID().toString());
			practiceStatistics13a.setOrgId(orgId);
			practiceStatistics13a.setTeacherYear(teacherYear - i);
			practiceStatistics13a.setCollegeName("地学院");
			practiceStatistics13a.setCollegeId(1710L);
			practiceStatistics13a.setProfessionId(2564L);
			practiceStatistics13a.setProfessionName("地质类");
			practiceStatistics13a.setClassId(10298L);
			practiceStatistics13a.setClassName("地质类2016-5班");
			practiceStatistics13a.setPracticeStudentNum(46L-s);
			practiceStatistics13a.setPracticeCompanyNum(1L+i);
			practiceStatistics13a.setTaskNum(487L-(s*4));
			practiceStatistics13a.setTaskPassNum(463L-(s*4));
			practiceStatistics13a.setSemester(j);
			practiceStatisticsList.add(practiceStatistics13a);

			random = new Random();
			s = random.nextInt(max) % (max - min + 1) + min;
			PracticeStatistics practiceStatistics13 = new PracticeStatistics();
			practiceStatistics13.setId(UUID.randomUUID().toString());
			practiceStatistics13.setOrgId(orgId);
			practiceStatistics13.setTeacherYear(teacherYear - i);
			practiceStatistics13.setCollegeName("地学院");
			practiceStatistics13.setCollegeId(1710L);
			practiceStatistics13.setProfessionId(2564L);
			practiceStatistics13.setProfessionName("地质类");
			practiceStatistics13.setClassId(10300L);
			practiceStatistics13.setClassName("地质类2016-6班");
			practiceStatistics13.setPracticeStudentNum(42L-s);
			practiceStatistics13.setPracticeCompanyNum(1L+i);
			practiceStatistics13.setTaskNum(499L-(s*4));
			practiceStatistics13.setTaskPassNum(477L-(s*4));
			practiceStatistics13.setSemester(j);
			practiceStatisticsList.add(practiceStatistics13);

			random = new Random();
			s = random.nextInt(max) % (max - min + 1) + min;
			PracticeStatistics practiceStatistics14a = new PracticeStatistics();
			practiceStatistics14a.setId(UUID.randomUUID().toString());
			practiceStatistics14a.setOrgId(orgId);
			practiceStatistics14a.setTeacherYear(teacherYear - i);
			practiceStatistics14a.setCollegeName("艺术学院");
			practiceStatistics14a.setCollegeId(1760L);
			practiceStatistics14a.setProfessionId(2578L);
			practiceStatistics14a.setProfessionName("设计学类");
			practiceStatistics14a.setClassId(10506L);
			practiceStatistics14a.setClassName("艺术类2016-3班");
			practiceStatistics14a.setPracticeStudentNum(55L-s);
			practiceStatistics14a.setPracticeCompanyNum(1L+i);
			practiceStatistics14a.setTaskNum(545L-(s*4));
			practiceStatistics14a.setTaskPassNum(530L-(s*4));
			practiceStatistics14a.setSemester(j);
			practiceStatisticsList.add(practiceStatistics14a);

			random = new Random();
			s = random.nextInt(max) % (max - min + 1) + min;
			PracticeStatistics practiceStatistics14 = new PracticeStatistics();
			practiceStatistics14.setId(UUID.randomUUID().toString());
			practiceStatistics14.setOrgId(orgId);
			practiceStatistics14.setTeacherYear(teacherYear - i);
			practiceStatistics14.setCollegeName("艺术学院");
			practiceStatistics14.setCollegeId(1760L);
			practiceStatistics14.setProfessionId(2578L);
			practiceStatistics14.setProfessionName("设计学类");
			practiceStatistics14.setClassId(10508L);
			practiceStatistics14.setClassName("艺术类2016-4班");
			practiceStatistics14.setPracticeStudentNum(56L-s);
			practiceStatistics14.setPracticeCompanyNum(1L+i);
			practiceStatistics14.setTaskNum(547L-(s*4));
			practiceStatistics14.setTaskPassNum(513L-(s*4));
			practiceStatistics14.setSemester(j);
			practiceStatisticsList.add(practiceStatistics14);
			}

		}

		practiceStatisticsService.deleteAllByOrgId(orgId);
		practiceStatisticsService.saveList(practiceStatisticsList);
		return new ResponseEntity<Map<String, Object>>(result, HttpStatus.OK);
	}

	@RequestMapping(value = "/updatepracticestatistics", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(httpMethod = "PUT", value = "修改学情分析学生实践统计数据", response = Void.class, notes = "修改学情分析学生实践统计数据<br><br><b>@author zhengning</b>")
	public ResponseEntity<Map<String, Object>> updatePracticeStatistics(
			@ApiParam(value = "id 记录id") @RequestParam(value = "id", required = false) String id,
			@ApiParam(value = "practiceStudentNum 实践学生人数)") @RequestParam(value = "practiceStudentNum", required = false) Long practiceStudentNum,
			@ApiParam(value = "practiceCompanyNum 实践公司人数)") @RequestParam(value = "practiceCompanyNum", required = false) Long practiceCompanyNum,
			@ApiParam(value = "taskNum 实践任务数)") @RequestParam(value = "taskNum", required = false) Long taskNum,
			@ApiParam(value = "taskPassNum 实践任务通过数)") @RequestParam(value = "taskPassNum", required = false) Long taskPassNum) {
		Map<String, Object> result = new HashMap<String, Object>();

		PracticeStatistics practiceStatistics = practiceStatisticsService
				.findById(id);
		 if(null != practiceStudentNum){
			if(null != practiceStudentNum && practiceStudentNum > 0){
				practiceStatistics.setPracticeStudentNum(practiceStudentNum);
			}
			if(null != practiceCompanyNum && practiceCompanyNum > 0){
				practiceStatistics.setPracticeCompanyNum(practiceCompanyNum);
			}
			if(null != taskNum && taskNum > 0){
				practiceStatistics.setTaskNum(taskNum);
			}
			if(null != taskPassNum && taskPassNum > 0){
				practiceStatistics.setTaskPassNum(taskPassNum);
			}

		}
		 practiceStatisticsService.save(practiceStatistics);
		return new ResponseEntity<Map<String, Object>>(result, HttpStatus.OK);
	}
}