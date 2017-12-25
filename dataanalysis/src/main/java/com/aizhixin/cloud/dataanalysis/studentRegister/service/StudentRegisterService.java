package com.aizhixin.cloud.dataanalysis.studentRegister.service;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Map.Entry;

import com.aizhixin.cloud.dataanalysis.common.PageData;
import com.aizhixin.cloud.dataanalysis.common.domain.ImportDomain;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.aizhixin.cloud.dataanalysis.common.excelutil.ExcelBasedataHelper;
import com.aizhixin.cloud.dataanalysis.common.exception.CommonException;
import com.aizhixin.cloud.dataanalysis.common.exception.ErrorCode;
import com.aizhixin.cloud.dataanalysis.common.util.DateUtil;
import com.aizhixin.cloud.dataanalysis.studentRegister.domain.StudentInfoDomain;
import com.aizhixin.cloud.dataanalysis.studentRegister.domain.StudentRegisterDomain;
import com.aizhixin.cloud.dataanalysis.studentRegister.mongoEntity.StudentRegister;
import com.aizhixin.cloud.dataanalysis.studentRegister.mongoRespository.StudentRegisterMongoRespository;

/**
 * 导入新生报到数据写入Mongo库
 * 
 * @author bly
 * @data 2017年11月27日
 */
@Component
@Transactional
public class StudentRegisterService {
	final static private Logger LOG = LoggerFactory.getLogger(StudentRegisterService.class);

	@Autowired
	private StudentRegisterMongoRespository respository;
	@Autowired
	private ExcelBasedataHelper basedataHelper;
	@Autowired
	private MongoTemplate mongoTemplate;


	public void importData(String studentInfoFile, String dataBaseFile, String importFile) {
		LOG.debug("star.........getStudentInfos..14-16");
		// 获取学生信息
		List<StudentInfoDomain> studentInfos = basedataHelper.readStudentInfoFromInputStream(studentInfoFile);
		if (null == studentInfos || studentInfos.size() <= 0) {
			throw new CommonException(ErrorCode.ID_IS_REQUIRED, "没有读取到任何数据");
		}
		LOG.debug("star.........getDataBases");
		// 获取学生基础数据源
		List<StudentRegisterDomain> dataBases = basedataHelper.readStudentRegisterFromInputStream(dataBaseFile);
		if (null == dataBases || dataBases.size() <= 0) {
			throw new CommonException(ErrorCode.ID_IS_REQUIRED, "没有读取到任何数据");
		}
		LOG.debug("star.........getStudentInfos...replaceFile");
		// 获取新学生基础信息
		List<ImportDomain> importDomains = basedataHelper.readDataBase(importFile);
		if (null == importDomains || importDomains.size() <= 0) {
			throw new CommonException(ErrorCode.ID_IS_REQUIRED, "没有读取到任何数据");
		}
		// 学生基础数据存map
		Map<String, StudentRegisterDomain> maps = new HashMap<>();
		for (StudentRegisterDomain data : dataBases) {
			maps.put(data.getJobNum(), data);
		}
		// 学生信息存map
		Map<String, StudentInfoDomain> maps1 = new HashMap<>();
		for (StudentInfoDomain data : studentInfos) {
			maps1.put(data.getJobNum(), data);
		}
		// 新学生基础信息存map
		Map<String, ImportDomain> map2 = new HashMap<>();
		for (ImportDomain data : importDomains) {
			map2.put(data.getName(), data);
		}
		List<StudentRegister> stuRegisterList = new ArrayList<>();
		int i = 0;
		LOG.debug("star.........executeMethod");
		// 学生信息key value
		for (Entry<String, StudentInfoDomain> entry1 : maps1.entrySet()) {
			// 学生数据key value
			for (Entry<String, StudentRegisterDomain> entry : maps.entrySet()) {
				try {
					if (entry1.getKey().equals(entry.getKey())) {
						StudentRegister studentRegister = new StudentRegister();
						studentRegister.setOrgId(entry1.getValue().getOrgId());
						studentRegister.setJobNum(entry1.getValue().getJobNum());
						if (entry.getValue().getActualRegisterDate() != null) {
							Integer date = Integer.valueOf(entry.getValue().getActualRegisterDate());
							if (date > 0) {
								Calendar c = new GregorianCalendar(1900, 0, -1);
								Date d = c.getTime();
								Date _d = DateUtils.addDays(d, date + 1); // 42605是距离1900年1月1日的天数
								studentRegister.setActualRegisterDate(_d);
							}
						}
						studentRegister.setIsRegister(entry.getValue().getIsRegister());
						for (Entry<String, ImportDomain> entry2 : map2.entrySet()) {
							if (entry2.getKey().equals(entry1.getValue().getClassName())) {
								studentRegister.setClassId(entry2.getValue().getId());
								break;
							}
						}
						studentRegister.setClassName(entry1.getValue().getClassName());
						studentRegister.setGrade(entry.getValue().getGrade());
						for (Entry<String, ImportDomain> entry2 : map2.entrySet()) {
							if (entry2.getKey().equals(entry1.getValue().getCollegeName())) {
								studentRegister.setCollegeId(entry2.getValue().getId());
								break;
							}
						}
						studentRegister.setCollegeName(entry1.getValue().getCollegeName());
						for (Entry<String, ImportDomain> entry2 : map2.entrySet()) {
							if (entry2.getKey().equals(entry1.getValue().getProfessionalName())) {
								studentRegister.setProfessionalId(entry2.getValue().getId());
								break;
							}
						}
						studentRegister.setProfessionalName(entry1.getValue().getProfessionalName());
						studentRegister.setSchoolYear(Integer.parseInt(entry.getValue().getSchoolYear()));
						studentRegister.setUserId(entry1.getValue().getUserId());
						studentRegister.setUserName(entry1.getValue().getUserName());
						SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
						String str = "2014-09-01";
						String str1 = "2015-09-01";
						String str2 = "2016-09-01";
						if (("14").equals(entry1.getValue().getJobNum().substring(1, 3))||
								("13").equals(entry1.getValue().getJobNum().substring(1, 3))) {
							Date date = sdf.parse(str);
							studentRegister.setRegisterDate(date);
						} else if(("15").equals(entry1.getValue().getJobNum().substring(1, 3))){
							Date date = sdf.parse(str1);
							studentRegister.setRegisterDate(date);
						} else{
							Date date = sdf.parse(str2);
							studentRegister.setRegisterDate(date);
						}
						studentRegister.setIsPay(entry.getValue().getIsPay());
						studentRegister.setIsGreenChannel(entry.getValue().getIsGreenChannel());
						studentRegister.setUserPhone(entry1.getValue().getUserPhone());
						stuRegisterList.add(studentRegister);
					}
				} catch (Exception e) {
					LOG.debug("学生报到数据表错误信息行号：" + entry.getValue().getLine() + ",  学号：" + entry.getValue().getJobNum());
					LOG.debug("学生信息表错误信息行号：" + entry1.getValue().getLine() + ",  学号：" + entry1.getValue().getJobNum());
					// e.printStackTrace();
				}
			}
//			i++;
//			if (0 == i % 1000) {
//				respository.save(stuRegisterList);
//				stuRegisterList.clear();
//			}
		}
//		if (!stuRegisterList.isEmpty()) {
//			respository.save(stuRegisterList);
//		}
		respository.save(stuRegisterList);
		System.out.println(stuRegisterList.size());
	}


   /*********************************修改新生注册信息*******************************/
	public Map<String, Object> modifyNewStudentDetails(Long orgId, Integer teacherYear) {
		List<StudentRegister> items = null;
		Map<String, Object> result = new HashMap<>();
		try {
			//创建查询条件对象
			org.springframework.data.mongodb.core.query.Query query = new org.springframework.data.mongodb.core.query.Query();
			//条件
			Criteria criteria = Criteria.where("orgId").is(orgId);
			if(null!=teacherYear){
				criteria.and("schoolYear").is(teacherYear);
			}
			query.addCriteria(criteria);
			//mongoTemplate.count计算总数
			long total = mongoTemplate.count(query, StudentRegister.class);
			// mongoTemplate.find 查询结果集
			items = mongoTemplate.find(query, StudentRegister.class);
			if(null!=items){
				for(StudentRegister sr: items){
					if(null!=sr.getActualRegisterDate()){
						sr.setIsRegister(1);
					}
				}
			}
			respository.save(items);
		}catch (Exception e){
			e.printStackTrace();
			result.put("success", false);
			result.put("message","修改新生数据异常！");
			return result;
		}
		result.put("success", true);
		result.put("message", "修改新生数据成功！");
		return result;
	}

}
