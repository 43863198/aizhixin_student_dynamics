package com.aizhixin.cloud.dataanalysis.score.service;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.lang.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import com.aizhixin.cloud.dataanalysis.common.constant.ScoreConstant;
import com.aizhixin.cloud.dataanalysis.common.domain.ImportDomain;
import com.aizhixin.cloud.dataanalysis.common.excelutil.ExcelBasedataHelper;
import com.aizhixin.cloud.dataanalysis.common.exception.CommonException;
import com.aizhixin.cloud.dataanalysis.common.exception.ErrorCode;
import com.aizhixin.cloud.dataanalysis.score.domain.ScoreDomain;
import com.aizhixin.cloud.dataanalysis.score.mongoEntity.Score;
import com.aizhixin.cloud.dataanalysis.score.mongoRespository.ScoreMongoRespository;

/**
 * 导入成绩数据写入Mongo库
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

	public void importData(MultipartFile dataBaseFile, MultipartFile importFile, Long orgId) {
		// 获取成绩
		List<ScoreDomain> dataBases = basedataHelper.readStudentScoreFromInputStream(dataBaseFile);
		if (null == dataBases || dataBases.size() <= 0) {
			throw new CommonException(ErrorCode.ID_IS_REQUIRED, "没有读取到任何数据");
		}
		//获取新学生基础信息
		List<ImportDomain> importDomains = basedataHelper.readDataBase(importFile);
		if (null == importDomains || importDomains.size() <= 0) {
			throw new CommonException(ErrorCode.ID_IS_REQUIRED, "没有读取到任何数据");
		}
		//新学生基础信息存map
		Map<String, ImportDomain> map = new HashMap<>();
		for (ImportDomain data : importDomains) {
			map.put(data.getName(), data);
		}
		List<Score> list = new ArrayList<>();
		int i = 0;
		for (ScoreDomain d : dataBases) {
			try {
				Score score = new Score();
				score.setUserId(d.getUserId());
				score.setUserName(d.getUserName());
				score.setOrgId(orgId);
				for (Entry<String, ImportDomain> entry : map.entrySet()) {
					if (entry.getKey().equals(d.getClassName())) {
						score.setClassId(entry.getValue().getId());
						break;
					}
				}
				score.setClassName(d.getClassName());
				for (Entry<String, ImportDomain> entry : map.entrySet()) {
					if (entry.getKey().equals(d.getCollegeName())) {
						score.setCollegeId(entry.getValue().getId());
						break;
					}
				}
				score.setCollegeName(d.getCollegeName());
				score.setSemester(d.getSemester());
				score.setJobNum(d.getJobNum());
				for (Entry<String, ImportDomain> entry : map.entrySet()) {
					if (entry.getKey().equals(d.getProfessionalName())) {
						score.setProfessionalId(entry.getValue().getId());
						break;
					}
				}
				score.setProfessionalName(d.getProfessionalName());
				score.setScheduleId(d.getScheduleId());
				score.setCourseType(d.getCourseType());
				score.setUsualScore(d.getUsualScore());
				score.setCredit(d.getCredit());
				score.setGradePoint(d.getGradePoint());
				score.setSchoolYear(d.getSchoolYear());
				score.setTotalScore(d.getTotalScore());
				score.setUserPhone(d.getUserPhone());
				if (d.getExamTime() != null) {
					Integer date = Integer.valueOf(d.getExamTime());
					if (date > 0) {
						Calendar c = new GregorianCalendar(1900, 0, -1);
						Date d1 = c.getTime();
						Date _d = DateUtils.addDays(d1, date + 1); // 42605是距离1900年1月1日的天数
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

	public void importData46(MultipartFile dataBaseFile, MultipartFile importFile, Long orgId) {
		// 获取成绩
		List<ScoreDomain> dataBases = basedataHelper.readStudentScoreFromInputStream(dataBaseFile);
		if (null == dataBases || dataBases.size() <= 0) {
			throw new CommonException(ErrorCode.ID_IS_REQUIRED, "没有读取到任何数据");
		}
		//获取新学生基础信息
		List<ImportDomain> importDomains = basedataHelper.readDataBase(importFile);
		if (null == importDomains || importDomains.size() <= 0) {
			throw new CommonException(ErrorCode.ID_IS_REQUIRED, "没有读取到任何数据");
		}
		//新学生基础信息存map
		Map<String, ImportDomain> map = new HashMap<>();
		for (ImportDomain data : importDomains) {
			map.put(data.getName(), data);
		}
		List<Score> list = new ArrayList<>();
		int i = 0;
		for (ScoreDomain d : dataBases) {
			if (!StringUtils.isEmpty(d.getTotalScore()) && Double.valueOf(d.getTotalScore()) + 345.00 < 425.00
					&& !"2017".equals(d.getGrade())) {
				try {
					Score score = new Score();
					score.setUserId(d.getUserId());
					score.setUserName(d.getUserName());
					score.setOrgId(orgId);
					for (Entry<String, ImportDomain> entry : map.entrySet()) {
						if (entry.getKey().equals(d.getClassName())) {
							score.setClassId(entry.getValue().getId());
							break;
						}
					}
					score.setClassName(d.getClassName());
					for (Entry<String, ImportDomain> entry : map.entrySet()) {
						if (entry.getKey().equals(d.getCollegeName())) {
							score.setCollegeId(entry.getValue().getId());
							break;
						}
					}
					score.setCollegeName(d.getCollegeName());
					score.setSemester(d.getSemester());
					score.setJobNum(d.getJobNum());
					for (Entry<String, ImportDomain> entry : map.entrySet()) {
						if (entry.getKey().equals(d.getProfessionalName())) {
							score.setProfessionalId(entry.getValue().getId());
							break;
						}
					}
					score.setProfessionalName(d.getProfessionalName());
					score.setScheduleId(d.getScheduleId());
					score.setCourseType(d.getCourseType());
					score.setUsualScore(d.getUsualScore());
					score.setCredit(d.getCredit());
					score.setGradePoint(d.getGradePoint());
					score.setGrade(d.getGrade());
					score.setSchoolYear(d.getSchoolYear());
					score.setTotalScore(String.valueOf(Double.valueOf(d.getTotalScore()) + 345.00));
					score.setUserPhone(d.getUserPhone());
					score.setExamType(ScoreConstant.EXAM_TYPE_CET4);
					if (d.getExamTime() != null) {
						Integer date = Integer.valueOf(d.getExamTime());
						if (date > 0) {
							Calendar c = new GregorianCalendar(1900, 0, -1);
							Date d1 = c.getTime();
							Date _d = DateUtils.addDays(d1, date + 1); // 42605是距离1900年1月1日的天数
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
		}
		if (!list.isEmpty()) {
			respository.save(list);
		}
	}

}
