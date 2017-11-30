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

	/**
     * 功能：判断字符串是否为日期格式
     * 
     * @param str
     * @return
     */
    public static boolean isDate(String strDate) {
        Pattern pattern = Pattern
                .compile("^((\\d{2}(([02468][048])|([13579][26]))[\\-\\/\\s]?((((0?[13578])|(1[02]))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(3[01])))|(((0?[469])|(11))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(30)))|(0?2[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])))))|(\\d{2}(([02468][1235679])|([13579][01345789]))[\\-\\/\\s]?((((0?[13578])|(1[02]))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(3[01])))|(((0?[469])|(11))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(30)))|(0?2[\\-\\/\\s]?((0?[1-9])|(1[0-9])|(2[0-8]))))))(\\s(((0?[0-9])|([1-2][0-3]))\\:([0-5]?[0-9])((\\s)|(\\:([0-5]?[0-9])))))?$");
        Matcher m = pattern.matcher(strDate);
        if (m.matches()) {
            return true;
        } else {
            return false;
        }
    }

	public void importData(MultipartFile studentInfoFile,MultipartFile scoreFile) {
		//获取学生信息
		List<StudentInfoDomain> studentInfos = basedataHelper.readStudentInfoFromInputStream(studentInfoFile);
		if (null == studentInfos || studentInfos.size() <= 0) {
			throw new CommonException(ErrorCode.ID_IS_REQUIRED, "没有读取到任何数据");
		}
		//获取成绩
		List<ScoreDomain> dataBases = basedataHelper.readStudentScoreFromInputStream(scoreFile);
		if (null == dataBases || dataBases.size() <= 0) {
			throw new CommonException(ErrorCode.ID_IS_REQUIRED, "没有读取到任何数据");
		}
		//学生成绩存map
		Map<String, ScoreDomain> maps = new HashMap<>();
		for (ScoreDomain data : dataBases) {
			maps.put(data.getJobNum(), data);
		}
		//学生信息存map
		Map<String, StudentInfoDomain> maps1= new HashMap<>();
		for (StudentInfoDomain data : studentInfos) {
			maps1.put(data.getJobNum(), data);
		}
		List<Score> scores = new ArrayList<>();
		//学生成绩key value
		for (Entry<String, ScoreDomain> entry : maps.entrySet()) {  
		    //学生信息key value
		    for (Entry<String, StudentInfoDomain> entry1 : maps1.entrySet()) {  
		    	try {
		    		if (entry.getKey().equals(entry1.getKey())) {
		    			Score score = new Score();
		    			score.setOrgId(entry1.getValue().getOrgId());
		    			score.setJobNum(entry.getValue().getJobNum());
		    			score.setClassId(entry1.getValue().getClassId());
		    			score.setClassName(entry1.getValue().getClassName());
		    			score.setGrade(entry.getValue().getGrade());
		    			score.setCollegeId(entry1.getValue().getCollegeId());
		    			score.setCollegeName(entry1.getValue().getCollegeName());
		    			score.setProfessionalId(entry1.getValue().getProfessionalId());
		    			score.setProfessionalName(entry1.getValue().getProfessionalName());
		    			score.setSchoolYear(Integer.parseInt(entry.getValue().getSchoolYear()));
		    			score.setUserId(entry1.getValue().getUserId());
		    			score.setUserName(entry1.getValue().getUserName());
		    			if (entry.getValue().getExamTime() != null && entry.getValue().getExamTime().length() == 5) {
		    				Integer date = Integer.valueOf(entry.getValue().getExamTime());
		    				if (date > 0) {
		    					Calendar c = new GregorianCalendar(1900,0,-1);  
		    					Date d = c.getTime();  
		    					Date _d = DateUtils.addDays(d, date + 1);  //42605是距离1900年1月1日的天数
		    					score.setExamTime(_d);
		    				}
		    			}
		    			score.setScheduleId(String.valueOf(entry.getValue().getScheduleId()));
		    			score.setUsualScore(entry.getValue().getUsualScore());
		    			score.setSchoolYear(Integer.parseInt(entry.getValue().getSchoolYear()));
		    			score.setGradePoint(entry.getValue().getGradePoint());
		    			scores.add(score);
		    		}
				} catch (Exception e) {
					LOG.info("错误信息行号：" + entry.getValue().getLine() + ",  学号：" + entry.getValue().getJobNum());
					LOG.info("错误信息行号：" + entry1.getValue().getLine() + ",  学号：" + entry1.getValue().getJobNum());
					e.printStackTrace();
				}
		    }
		}  
		respository.save(scores);
	}
}
