/**
 *
 */
package com.aizhixin.cloud.dataanalysis.simulate.controller;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

import com.aizhixin.cloud.dataanalysis.setup.service.GenerateWarningInfoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.aizhixin.cloud.dataanalysis.alertinformation.service.AlertWarningInformationService;
import com.aizhixin.cloud.dataanalysis.analysis.job.SchoolStatisticsAnalysisJob;
import com.aizhixin.cloud.dataanalysis.common.constant.AlertTypeConstant;
import com.aizhixin.cloud.dataanalysis.common.constant.WarningTypeConstant;
import com.aizhixin.cloud.dataanalysis.rollCall.job.RollCallJob;
import com.aizhixin.cloud.dataanalysis.score.job.ScoreJob;
import com.aizhixin.cloud.dataanalysis.studentRegister.job.StudentRegisterJob;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

/**
 * @author zhengning
 *
 */
@RestController
@RequestMapping("/v1/test/job")
@Api(value = "测试预警定时任务操作", description = "调用预警定时任务操作")
public class TestAlarmJobController {
	private final static Logger log = LoggerFactory
			.getLogger(TestAlarmJobController.class);

	@Autowired
	private RollCallJob rollCallJob;
	@Autowired
	private ScoreJob scoreJob;
	@Autowired
	private SchoolStatisticsAnalysisJob schoolStatisticsJob;
	@Autowired
	private AlertWarningInformationService alertWarningInformationService;
	@Autowired
	private GenerateWarningInfoService generateWarningInfoService;


	@RequestMapping(value = "/rollcallcountjob", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(httpMethod = "GET", value = "签到定统计数据时任务", response = Void.class, notes = "签到定统计数据时任务<br><br><b>@author zhengning</b>")
	public ResponseEntity<Map<String, Object>> rollCallCountJob(
			@ApiParam(value = "teacherYear 学年", required = true) @RequestParam(value = "teacherYear", required = true) Integer teacherYear,
			@ApiParam(value = "semester 学期", required = true) @RequestParam(value = "semester", required = true) Integer semester
	) {
		rollCallJob.rollCallCountJob(teacherYear,semester);

		Map<String, Object> result = new HashMap<String, Object>();
		return new ResponseEntity<Map<String, Object>>(result, HttpStatus.OK);
	}

	@RequestMapping(value = "/totalscorecountjob", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(httpMethod = "GET", value = "总评成绩统计定时任务", response = Void.class, notes = "新生报到预警定时任务<br><br><b>@author zhengning</b>")
	public ResponseEntity<Map<String, Object>> totalScoreCountJob(
			@ApiParam(value = "teacherYear 学年", required = true) @RequestParam(value = "teacherYear", required = true) Integer teacherYear,
			@ApiParam(value = "semester 学期", required = true) @RequestParam(value = "semester", required = true) Integer semester
	) {
		scoreJob.totalScoreCountJob(teacherYear, semester);

		Map<String, Object> result = new HashMap<String, Object>();
		return new ResponseEntity<Map<String, Object>>(result, HttpStatus.OK);
	}


	@RequestMapping(value = "/makeupscorecountjob", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(httpMethod = "GET", value = "补考成绩统计定时任务", response = Void.class, notes = "补考成绩统计定时任务<br><br><b>@author zhengning</b>")
	public ResponseEntity<Map<String, Object>> makeUpScoreCountJob(
			@ApiParam(value = "teacherYear 学年", required = true) @RequestParam(value = "teacherYear", required = true) Integer teacherYear
	) {
		scoreJob.makeUpScoreCountJob(teacherYear);
		;
		Map<String, Object> result = new HashMap<String, Object>();
		return new ResponseEntity<Map<String, Object>>(result, HttpStatus.OK);
	}


	@RequestMapping(value = "/scorefluctuatecountJob", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(httpMethod = "GET", value = "成绩波动数据统计定时任务", response = Void.class, notes = "成绩波动数据统计定时任务<br><br><b>@author zhengning</b>")
	public ResponseEntity<Map<String, Object>> scoreFluctuateCountJob(
			@ApiParam(value = "teacherYear 学年", required = true) @RequestParam(value = "teacherYear", required = true) Integer teacherYear,
			@ApiParam(value = "semester 学期", required = true) @RequestParam(value = "semester", required = true) Integer semester
	) {
		scoreJob.scoreFluctuateCountJob(teacherYear, semester);
		Map<String, Object> result = new HashMap<String, Object>();
		return new ResponseEntity<Map<String, Object>>(result, HttpStatus.OK);
	}
	@RequestMapping(value = "/schoolcountjob", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(httpMethod = "GET", value = "学校信息统计定时任务", response = Void.class, notes = "学校信息统计定时任务<br><br><b>@author zhengning</b>")
	public ResponseEntity<Map<String, Object>> schoolCountJob(
			@ApiParam(value = "teacherYear 学年", required = true) @RequestParam(value = "teacherYear", required = true) Integer teacherYear
	) {
		schoolStatisticsJob.schoolStatisticsJob(teacherYear);
		Map<String, Object> result = new HashMap<String, Object>();
		return new ResponseEntity<Map<String, Object>>(result, HttpStatus.OK);
	}

	@RequestMapping(value = "/processedalert", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(httpMethod = "GET", value = "按预警等级处理预警信息", response = Void.class, notes = "处理预警信息<br><br><b>@author zhengning</b>")
	public ResponseEntity<Map<String, Object>> processedAlert(
			@ApiParam(value = "orgIds 机构id") @RequestParam(value = "orgIds", required = true) HashSet<Long> orgIds,
			@ApiParam(value = "warningLevel 预警等级") @RequestParam(value = "warningLevel", required = true) int warningLevel
			) {
		alertWarningInformationService.updateWarningStateByWarningLevel(AlertTypeConstant.ALERT_PROCESSED, warningLevel, orgIds);
		Map<String, Object> result = new HashMap<String, Object>();
		return new ResponseEntity<Map<String, Object>>(result, HttpStatus.OK);
	}


	/**
	 * 手动生成预警数据
	 *
	 * @param orgId
	 * @param warningType
	 * @return
	 */
	@GetMapping(value = "/testeneratedata", produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(httpMethod = "GET", value = "手动生成预警数据", response = Void.class, notes = "手动生成预警数据<br><br><b>@author jianwei.wu</b>")
	public Map<String, Object> testGenerateData(
			@ApiParam(value = "orgId 机构id", required = true) @RequestParam(value = "orgId", required = true) Long orgId,
			@ApiParam(value = "warningType 预警类型", required = true) @RequestParam(value = "warningType", required = true) String warningType,
            @ApiParam(value = "teacherYear 学年", required = true) @RequestParam(value = "teacherYear", required = true) Integer teacherYear,
            @ApiParam(value = "semester 学期", required = true) @RequestParam(value = "semester", required = true) Integer semester
	) {
		Map<String, Object> result = new HashMap<>();
		generateWarningInfoService.warningInfo(orgId, warningType,teacherYear,semester);
		result.put("success", true);
		result.put("message", "手动生成数据成功!");
		return result;
	}


}