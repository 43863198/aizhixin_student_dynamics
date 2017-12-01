package com.aizhixin.cloud.dataanalysis.studentRegister.service;

import java.util.*;
import java.util.Map.Entry;

import com.aizhixin.cloud.dataanalysis.common.PageData;
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
import com.aizhixin.cloud.dataanalysis.studentRegister.domain.StudentInfoDomain;
import com.aizhixin.cloud.dataanalysis.studentRegister.domain.StudentRegisterDomain;
import com.aizhixin.cloud.dataanalysis.studentRegister.mongoEntity.StudentRegister;
import com.aizhixin.cloud.dataanalysis.studentRegister.mongoRespository.StudentRegisterMongoRespository;

/**
 * 新生报到导入
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
	private MongoTemplate mongoTemplate;

	@Autowired
	private ExcelBasedataHelper basedataHelper;

	public void importData(MultipartFile studentInfoFile, MultipartFile dataBaseFile, Date registerDate) {
		//获取学生信息
		List<StudentInfoDomain> studentInfos = basedataHelper.readStudentInfoFromInputStream(studentInfoFile);
		if (null == studentInfos || studentInfos.size() <= 0) {
			throw new CommonException(ErrorCode.ID_IS_REQUIRED, "没有读取到任何数据");
		}
		//获取学生基础数据源
		List<StudentRegisterDomain> dataBases = basedataHelper.readStudentRegisterFromInputStream(dataBaseFile);
		if (null == dataBases || dataBases.size() <= 0) {
			throw new CommonException(ErrorCode.ID_IS_REQUIRED, "没有读取到任何数据");
		}
		//学生基础数据存map
		Map<String, StudentRegisterDomain> maps = new HashMap<>();
		for (StudentRegisterDomain data : dataBases) {
			maps.put(data.getJobNum(), data);
		}
		//学生信息存map
		Map<String, StudentInfoDomain> maps1 = new HashMap<>();
		for (StudentInfoDomain data : studentInfos) {
			maps1.put(data.getJobNum(), data);
		}
		List<StudentRegister> stuRegisterList = new ArrayList<>();
		//学生数据key value
		int i = 0;
		for (Entry<String, StudentRegisterDomain> entry : maps.entrySet()) {
			//学生信息key value
			for (Entry<String, StudentInfoDomain> entry1 : maps1.entrySet()) {
				try {
					if (entry.getKey().equals(entry1.getKey())) {
						StudentRegister studentRegister = new StudentRegister();
						studentRegister.setOrgId(entry1.getValue().getOrgId());
						studentRegister.setJobNum(entry.getValue().getJobNum());
						if (entry.getValue().getActualRegisterDate() != null) {
							Integer date = Integer.valueOf(entry.getValue().getActualRegisterDate());
							if (date > 0) {
								Calendar c = new GregorianCalendar(1900, 0, -1);
								Date d = c.getTime();
								Date _d = DateUtils.addDays(d, date + 1);  //42605是距离1900年1月1日的天数
								studentRegister.setActualRegisterDate(_d);
							}
						}
						studentRegister.setIsRegister(entry.getValue().getIsRegister());
						studentRegister.setClassId(entry1.getValue().getClassId());
						studentRegister.setClassName(entry1.getValue().getClassName());
						studentRegister.setGrade(entry.getValue().getGrade());
						studentRegister.setCollegeId(entry1.getValue().getCollegeId());
						studentRegister.setCollegeName(entry1.getValue().getCollegeName());
						studentRegister.setProfessionalId(entry1.getValue().getProfessionalId());
						studentRegister.setProfessionalName(entry1.getValue().getProfessionalName());
						studentRegister.setSchoolYear(Integer.parseInt(entry.getValue().getSchoolYear()));
						studentRegister.setUserId(entry1.getValue().getUserId());
						studentRegister.setUserName(entry1.getValue().getUserName());
						studentRegister.setRegisterDate(registerDate);
						studentRegister.setIsPay(entry.getValue().getIsPay());
						studentRegister.setIsGreenChannel(entry.getValue().getIsGreenChannel());
						stuRegisterList.add(studentRegister);
					}
				} catch (Exception e) {
					LOG.info("错误信息行号：" + entry.getValue().getLine() + ",  学号：" + entry.getValue().getJobNum());
					LOG.info("错误信息行号：" + entry1.getValue().getLine() + ",  学号：" + entry1.getValue().getJobNum());
					e.printStackTrace();
				}
			}
			i++;
			if (0 == i % 1000) {
				respository.save(stuRegisterList);
				stuRegisterList.clear();
			}
		}
		if (!stuRegisterList.isEmpty()) {
			respository.save(stuRegisterList);
		}
	}


	public Map<String, Object> getCollegeDetails(Pageable page,Long orgId, String collegeId, String type,String isReport,String isPay) {
		Map<String, Object> result = new HashMap<>();
		PageData<StudentRegister> p = new PageData<>();
		List<StudentRegister> items = new ArrayList<>();
		long total = 0L;
		try {
			//创建排序模板Sort
			Sort sort = new Sort(Sort.Direction.DESC, "id");
			//创建分页模板Pageable
			Pageable pageable = new PageRequest(page.getPageNumber(), page.getPageSize(), sort);
			//创建查询条件对象
			Query query = new Query();
			//条件
			Criteria criteria = Criteria.where("orgId").is(orgId);
			if (null != collegeId) {
				String[] cid = collegeId.split(",");
				Set<Long> collegeIds = new HashSet<>();
				for (String d : cid) {
					collegeIds.add(Long.valueOf(d));
				}
				criteria.and("collegeId").in(collegeId);
			}
			if (null != type) {
				String[] td = type.split(",");
				List tds = new ArrayList<>();
				for (String d : td) {
					tds.add(Integer.valueOf(d));
				}
				criteria.and("education").in(tds);
			}
			if (null != isReport) {
				criteria.and("isRegister").is(Integer.valueOf(isReport));
			}
			if (null != isPay) {
				String[] td = type.split(",");
				for (String d : td) {
					if (d.equals("1")) {
						criteria.and("isPay").is(1);
					}
					if (d.equals("2")) {
						criteria.and("isGreenChannel").is(1);
					}
				}

			}
			query.addCriteria(criteria);
			//mongoTemplate.count计算总数
			 total = mongoTemplate.count(query, StudentRegister.class);
			// mongoTemplate.find 查询结果集
			items = mongoTemplate.find(query.with(pageable), StudentRegister.class);
		}catch (Exception e){
			result.put("success", false);
			result.put("message","获取数据异常！");
		}
        p.getPage().setPageNumber(page.getPageNumber());
		p.getPage().setPageSize(page.getPageSize());
		p.getPage().setTotalElements(total);
		p.setData(items);
		result.put("success", true);
		result.put("data", p);
		return result;
	}


}
