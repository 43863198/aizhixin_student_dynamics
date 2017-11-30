package com.aizhixin.cloud.dataanalysis.score.service;

import io.swagger.annotations.ApiModelProperty;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;

import com.aizhixin.cloud.dataanalysis.common.util.DateUtil;
import com.aizhixin.cloud.dataanalysis.score.domain.ScoreDomain;
import com.aizhixin.cloud.dataanalysis.score.mongoEntity.Score;
import com.aizhixin.cloud.dataanalysis.score.mongoRespository.ScoreMongoRespository;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.aizhixin.cloud.dataanalysis.rollCall.domain.RollCallDomain;
import com.aizhixin.cloud.dataanalysis.rollCall.mongoEntity.RollCall;
import com.aizhixin.cloud.dataanalysis.score.domain.ScoreDomain;
import com.aizhixin.cloud.dataanalysis.score.mongoEntity.Score;
import com.aizhixin.cloud.dataanalysis.score.mongoRespository.ScoreMongoRespository;
import com.aizhixin.cloud.dataanalysis.studentRegister.common.CommonException;
import com.aizhixin.cloud.dataanalysis.studentRegister.common.ErrorCode;
import com.aizhixin.cloud.dataanalysis.studentRegister.common.ExcelBasedataHelper;
import com.aizhixin.cloud.dataanalysis.studentRegister.domain.StudentInfoDomain;

/**
 * 成绩导入
 * 
 * @author bly
 * @data 2017年11月29日
 */
@Component
@Transactional
public class ScoreService {
	final static private Logger LOG = LoggerFactory.getLogger(ScoreService.class);

	@Autowired
	private ScoreMongoRespository respository;
	
	@Autowired
	private ExcelBasedataHelper basedataHelper;
	
	public void importData(MultipartFile dataBaseFile) {
		//获取成绩
		List<ScoreDomain> dataBases = basedataHelper.readStudentScoreFromInputStream(dataBaseFile);
		if (null == dataBases || dataBases.size() <= 0) {
			throw new CommonException(ErrorCode.ID_IS_REQUIRED, "没有读取到任何数据");
		}
		List<Score> list = new ArrayList<>();
		int i = 0;
		for (ScoreDomain d : dataBases) {
			try {
				Score score = new Score();
				score.setUserId(d.getUserId());
				score.setUserName(d.getUserName());
				score.setOrgId(d.getOrgId());
				score.setClassId(d.getClassId());
				score.setClassName(d.getClassName());
				score.setCollegeId(d.getCollegeId());
				score.setCollegeName(d.getCollegeName());
				score.setGrade(d.getGrade());
				score.setJobNum(d.getJobNum());
				score.setProfessionalId(d.getProfessionalId());
				score.setProfessionalName(d.getProfessionalName());
				score.setScheduleId(d.getScheduleId());
				score.setCourseType(d.getCourseType());
				score.setUsualScore(d.getUsualScore());
				score.setCredit(d.getCredit());
				score.setGradePoint(d.getGradePoint());
				score.setSchoolYear(Integer.valueOf(d.getSchoolYear()));
				score.setTotalScore(d.getTotalScore());
				if (d.getExamTime() != null) {
    				Integer date = Integer.valueOf(d.getExamTime());
    				if (date > 0) {
    					Calendar c = new GregorianCalendar(1900,0,-1);  
    					Date d1 = c.getTime();  
    					Date _d = DateUtils.addDays(d1, date + 1);  //42605是距离1900年1月1日的天数
    					score.setExamTime(_d);
    				}
    			}
				list.add(score);
			} catch (Exception e) {
				LOG.info("错误信息行号：" + d.getLine() + ",  学号：" + d.getJobNum());
				e.printStackTrace();
			}
			i++;
			if (0 == i % 10000) {
				respository.save(list);
				list.clear();
			}
		}
		if (!list.isEmpty()) {
			respository.save(list);
		}
	}
}
