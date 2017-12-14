/**
 * 
 */
package com.aizhixin.cloud.dataanalysis.simulate.controller;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.aizhixin.cloud.dataanalysis.alertinformation.service.AlertWarningInformationService;
import com.aizhixin.cloud.dataanalysis.analysis.job.SchoolStatisticsJob;
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
	private StudentRegisterJob studentRegisterJob;
	@Autowired
	private RollCallJob rollCallJob;
	@Autowired
	private ScoreJob scoreJob;
	@Autowired
	private SchoolStatisticsJob schoolStatisticsJob;
	@Autowired
	private AlertWarningInformationService alertWarningInformationService;


	@RequestMapping(value = "/rollcallcountjob", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(httpMethod = "GET", value = "签到定统计数据时任务", response = Void.class, notes = "签到定统计数据时任务<br><br><b>@author zhengning</b>")
	public ResponseEntity<Map<String, Object>> rollCallCountJob() {

		rollCallJob.rollCallCountJob();

		Map<String, Object> result = new HashMap<String, Object>();
		return new ResponseEntity<Map<String, Object>>(result, HttpStatus.OK);
	}

	@RequestMapping(value = "/rollcalljob", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(httpMethod = "GET", value = "签到预警定时任务", response = Void.class, notes = "签到预警定时任务<br><br><b>@author zhengning</b>")
	public ResponseEntity<Map<String, Object>> rollCallJob(
			
			@ApiParam(value = "orgId 机构id") @RequestParam(value = "orgId", required = false) Long orgId
			) {
        if(null == orgId){
        	orgId = 218L;
        }
		alertWarningInformationService.logicDeleteByOrgIdAndWarnType(WarningTypeConstant.Absenteeism.toString(), orgId);
		rollCallJob.rollCallJob();

		Map<String, Object> result = new HashMap<String, Object>();
		return new ResponseEntity<Map<String, Object>>(result, HttpStatus.OK);
	}

	@RequestMapping(value = "/sturegisterjob", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(httpMethod = "GET", value = "新生报到预警定时任务", response = Void.class, notes = "新生报到预警定时任务<br><br><b>@author zhengning</b>")
	public ResponseEntity<Map<String, Object>> stuRegisterJob(
			@ApiParam(value = "orgId 机构id") @RequestParam(value = "orgId", required = false) Long orgId
			) {
        if(null == orgId){
        	orgId = 218L;
        }
		alertWarningInformationService.logicDeleteByOrgIdAndWarnType(WarningTypeConstant.Register.toString(), orgId);
		studentRegisterJob.studenteRegisterJob();

		Map<String, Object> result = new HashMap<String, Object>();
		return new ResponseEntity<Map<String, Object>>(result, HttpStatus.OK);
	}

	@RequestMapping(value = "/totalscorecountjob", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(httpMethod = "GET", value = "总评成绩统计定时任务", response = Void.class, notes = "新生报到预警定时任务<br><br><b>@author zhengning</b>")
	public ResponseEntity<Map<String, Object>> totalScoreCountJob() {
		scoreJob.totalScoreCountJob();

		Map<String, Object> result = new HashMap<String, Object>();
		return new ResponseEntity<Map<String, Object>>(result, HttpStatus.OK);
	}

	@RequestMapping(value = "/totalscorejob", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(httpMethod = "GET", value = "总评成绩预警定时任务", response = Void.class, notes = "总评成绩预警定时任务<br><br><b>@author zhengning</b>")
	public ResponseEntity<Map<String, Object>> totalScoreJob(
			@ApiParam(value = "orgId 机构id") @RequestParam(value = "orgId", required = false) Long orgId
			) {
        if(null == orgId){
        	orgId = 218L;
        }
		alertWarningInformationService.logicDeleteByOrgIdAndWarnType(WarningTypeConstant.TotalAchievement.toString(), orgId);
		scoreJob.totalScoreJob();
		Map<String, Object> result = new HashMap<String, Object>();
		return new ResponseEntity<Map<String, Object>>(result, HttpStatus.OK);
	}

	@RequestMapping(value = "/makeupscorecountjob", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(httpMethod = "GET", value = "补考成绩统计定时任务", response = Void.class, notes = "补考成绩统计定时任务<br><br><b>@author zhengning</b>")
	public ResponseEntity<Map<String, Object>> makeUpScoreCountJob() {
		scoreJob.makeUpScoreCountJob();
		;
		Map<String, Object> result = new HashMap<String, Object>();
		return new ResponseEntity<Map<String, Object>>(result, HttpStatus.OK);
	}

	@RequestMapping(value = "/makeupscorejob", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(httpMethod = "GET", value = "补考成绩预警定时任务", response = Void.class, notes = "补考成绩预警定时任务<br><br><b>@author zhengning</b>")
	public ResponseEntity<Map<String, Object>> makeUpScoreJob(
			@ApiParam(value = "orgId 机构id") @RequestParam(value = "orgId", required = false) Long orgId
			) {
        if(null == orgId){
        	orgId = 218L;
        }
		alertWarningInformationService.logicDeleteByOrgIdAndWarnType(WarningTypeConstant.SupplementAchievement.toString(), orgId);
		scoreJob.makeUpScoreJob();
		Map<String, Object> result = new HashMap<String, Object>();
		return new ResponseEntity<Map<String, Object>>(result, HttpStatus.OK);
	}

	@RequestMapping(value = "/scorefluctuatecountJob", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(httpMethod = "GET", value = "成绩波动数据统计定时任务", response = Void.class, notes = "成绩波动数据统计定时任务<br><br><b>@author zhengning</b>")
	public ResponseEntity<Map<String, Object>> scoreFluctuateCountJob() {
		scoreJob.scoreFluctuateCountJob();
		Map<String, Object> result = new HashMap<String, Object>();
		return new ResponseEntity<Map<String, Object>>(result, HttpStatus.OK);
	}

	@RequestMapping(value = "/scorefluctuatejob", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(httpMethod = "GET", value = "成绩波动预警定时任务", response = Void.class, notes = "成绩波动预警定时任务<br><br><b>@author zhengning</b>")
	public ResponseEntity<Map<String, Object>> scoreFluctuateJob(
			@ApiParam(value = "orgId 机构id") @RequestParam(value = "orgId", required = false) Long orgId
			) {
        if(null == orgId){
        	orgId = 218L;
        }
		alertWarningInformationService.logicDeleteByOrgIdAndWarnType(WarningTypeConstant.PerformanceFluctuation.toString(), orgId);
		scoreJob.scoreFluctuateJob();
		Map<String, Object> result = new HashMap<String, Object>();
		return new ResponseEntity<Map<String, Object>>(result, HttpStatus.OK);
	}

	@RequestMapping(value = "/attendabnormaljob", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(httpMethod = "GET", value = "修学异常预警定时任务", response = Void.class, notes = "总评成绩统计定时任务执行后,执行修学异常预警定时任务<br><br><b>@author zhengning</b>")
	public ResponseEntity<Map<String, Object>> attendAbnormalJob(
			@ApiParam(value = "orgId 机构id") @RequestParam(value = "orgId", required = false) Long orgId
			) {
        if(null == orgId){
        	orgId = 218L;
        }
		alertWarningInformationService.logicDeleteByOrgIdAndWarnType(WarningTypeConstant.AttendAbnormal.toString(), orgId);
		scoreJob.attendAbnormalJob();
		Map<String, Object> result = new HashMap<String, Object>();
		return new ResponseEntity<Map<String, Object>>(result, HttpStatus.OK);
	}

	
	@RequestMapping(value = "/cet4job", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(httpMethod = "GET", value = "英语4级预警定时任务", response = Void.class, notes = "总评成绩统计定时任务执行后,执行英语4级预警定时任务<br><br><b>@author zhengning</b>")
	public ResponseEntity<Map<String, Object>> cet4Job(
			@ApiParam(value = "orgId 机构id") @RequestParam(value = "orgId", required = false) Long orgId
			) {
        if(null == orgId){
        	orgId = 218L;
        }
		alertWarningInformationService.logicDeleteByOrgIdAndWarnType(WarningTypeConstant.Cet.toString(), orgId);
		scoreJob.cet4ScoreJob();
		Map<String, Object> result = new HashMap<String, Object>();
		return new ResponseEntity<Map<String, Object>>(result, HttpStatus.OK);
	}
	
	
	@RequestMapping(value = "/schoolcountjob", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(httpMethod = "GET", value = "学校信息统计定时任务", response = Void.class, notes = "学校信息统计定时任务<br><br><b>@author zhengning</b>")
	public ResponseEntity<Map<String, Object>> schoolCountJob() {
		schoolStatisticsJob.schoolStatisticsJob();
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
}