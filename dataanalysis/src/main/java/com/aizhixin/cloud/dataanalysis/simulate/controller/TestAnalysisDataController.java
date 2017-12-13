/**
 * 
 */
package com.aizhixin.cloud.dataanalysis.simulate.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

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

import com.aizhixin.cloud.dataanalysis.analysis.entity.SchoolStatistics;
import com.aizhixin.cloud.dataanalysis.analysis.service.SchoolStatisticsService;
import com.aizhixin.cloud.dataanalysis.studentRegister.mongoEntity.StudentRegister;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

/**
 * @author zhengning
 *
 */
@RestController
@RequestMapping("/v1/test/analysis")
@Api(value = "测试生成模拟数据和调用定时任务操作", description = "生成mongodb模拟数据操作")
public class TestAnalysisDataController {
	private final static Logger log = LoggerFactory
			.getLogger(TestAnalysisDataController.class);

	@Autowired
	private SchoolStatisticsService schoolStatisticsService;


	@RequestMapping(value = "/schoolstatisticsdata", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(httpMethod = "POST", value = "生成MongoDB学生报到数据", response = Void.class, notes = "生成MongoDB学生报到数据<br><br><b>@author zhengning</b>")
	public ResponseEntity<Map<String, Object>> addRegisterData(
			@ApiParam(value = "orgId 机构id") @RequestParam(value = "orgId", required = false) Long orgId,
			@ApiParam(value = "teacherYear 学年)") @RequestParam(value = "teacherYear", required = true) int teacherYear,
			@ApiParam(value = "num teacherYear几年内)") @RequestParam(value = "num", required = false) int num) {
		Map<String, Object> result = new HashMap<String, Object>();
		
		if (null != orgId && orgId.longValue() > 0L) {
		} else {
			orgId = 1L;
		}
		if(num == 0){
			num = 1;
		}
		
		List<SchoolStatistics> schoolStatisticsList = new ArrayList<SchoolStatistics>();
	    for(int i=1;i<= num;i++){
	    	SchoolStatistics schoolStatistics1 = new SchoolStatistics();
	    	schoolStatistics1.setId(UUID.randomUUID().toString());
	    	schoolStatistics1.setOrgId(orgId);
//	    	schoolStatistics1.setTeacherYear(teacherYear);
//	    	schoolStatistics1.setCollegeName(collegeName);
//	    	schoolStatistics1.setCollegeId(collegeId);
	    	
	    }
	

		return new ResponseEntity<Map<String, Object>>(result, HttpStatus.OK);
	}

	
}