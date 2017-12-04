/**
 * 
 */
package com.aizhixin.cloud.dataanalysis.studentRegister.controller;

import java.util.HashMap;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import com.aizhixin.cloud.dataanalysis.rollCall.job.RollCallJob;
import com.aizhixin.cloud.dataanalysis.score.job.ScoreJob;
import com.aizhixin.cloud.dataanalysis.studentRegister.job.StudentRegisterJob;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

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


	@RequestMapping(value = "/rollcallcountjob", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(httpMethod = "GET", value = "签到定统计数据时任务", response = Void.class, notes = "签到定统计数据时任务<br><br><b>@author zhengning</b>")
	public ResponseEntity<Map<String, Object>> rollCallCountJob() {

		rollCallJob.rollCallCountJob();

		Map<String, Object> result = new HashMap<String, Object>();
		return new ResponseEntity<Map<String, Object>>(result, HttpStatus.OK);
	}

	@RequestMapping(value = "/rollcalljob", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(httpMethod = "GET", value = "签到预警定时任务", response = Void.class, notes = "签到预警定时任务<br><br><b>@author zhengning</b>")
	public ResponseEntity<Map<String, Object>> rollCallJob() {

		rollCallJob.rollCallJob();

		Map<String, Object> result = new HashMap<String, Object>();
		return new ResponseEntity<Map<String, Object>>(result, HttpStatus.OK);
	}

	@RequestMapping(value = "/sturegisterjob", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(httpMethod = "GET", value = "新生报到预警定时任务", response = Void.class, notes = "新生报到预警定时任务<br><br><b>@author zhengning</b>")
	public ResponseEntity<Map<String, Object>> stuRegisterJob() {

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
	public ResponseEntity<Map<String, Object>> totalScoreJob() {
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
	public ResponseEntity<Map<String, Object>> makeUpScoreJob() {
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
	public ResponseEntity<Map<String, Object>> scoreFluctuateJob() {
		scoreJob.scoreFluctuateJob();
		Map<String, Object> result = new HashMap<String, Object>();
		return new ResponseEntity<Map<String, Object>>(result, HttpStatus.OK);
	}

	@RequestMapping(value = "/attendabnormaljob", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(httpMethod = "GET", value = "修学异常预警定时任务", response = Void.class, notes = "总评成绩统计定时任务执行后,执行修学异常预警定时任务<br><br><b>@author zhengning</b>")
	public ResponseEntity<Map<String, Object>> attendAbnormalJob() {
		scoreJob.attendAbnormalJob();
		Map<String, Object> result = new HashMap<String, Object>();
		return new ResponseEntity<Map<String, Object>>(result, HttpStatus.OK);
	}

	
	@RequestMapping(value = "/cet4job", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(httpMethod = "GET", value = "英语4级预警定时任务", response = Void.class, notes = "总评成绩统计定时任务执行后,执行英语4级预警定时任务<br><br><b>@author zhengning</b>")
	public ResponseEntity<Map<String, Object>> cet4Job() {
		scoreJob.cet4ScoreJob();
		Map<String, Object> result = new HashMap<String, Object>();
		return new ResponseEntity<Map<String, Object>>(result, HttpStatus.OK);
	}
}