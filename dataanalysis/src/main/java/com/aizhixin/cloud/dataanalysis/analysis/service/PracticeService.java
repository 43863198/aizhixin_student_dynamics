package com.aizhixin.cloud.dataanalysis.analysis.service;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.Map.Entry;
import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.aizhixin.cloud.dataanalysis.analysis.constant.CompanyName;
import com.aizhixin.cloud.dataanalysis.analysis.domain.PracticeDomain;
import com.aizhixin.cloud.dataanalysis.analysis.mongoEntity.Practice;
import com.aizhixin.cloud.dataanalysis.analysis.mongoRespository.PracticeMongoRespository;
import com.aizhixin.cloud.dataanalysis.common.domain.ImportDomain;
import com.aizhixin.cloud.dataanalysis.common.excelutil.ExcelBasedataHelper;
import com.aizhixin.cloud.dataanalysis.common.exception.CommonException;
import com.aizhixin.cloud.dataanalysis.common.exception.ErrorCode;
import com.aizhixin.cloud.dataanalysis.common.util.DateUtil;

/**
 * 导入实践数据写入Mongo库
 * 
 * @author bly
 * @data 2017年12月4日
 */
@Component
@Transactional
public class PracticeService {
	final static private Logger LOG = LoggerFactory.getLogger(PracticeService.class);

	@Autowired
	private PracticeMongoRespository respository;

	@Autowired
	private ExcelBasedataHelper basedataHelper;

	public void importData(String dataBaseFile, String importFile, Long orgId) {
		// 获取实践数据
		List<PracticeDomain> dataBases = basedataHelper.readPracticeFromInputStream(dataBaseFile);
		if (null == dataBases || dataBases.size() <= 0) {
			throw new CommonException(ErrorCode.ID_IS_REQUIRED, "没有读取到任何数据");
		}
		// 获取新学生基础信息
		List<ImportDomain> importDomains = basedataHelper.readDataBase(importFile);
		if (null == importDomains || importDomains.size() <= 0) {
			throw new CommonException(ErrorCode.ID_IS_REQUIRED, "没有读取到任何数据");
		}
		// 新学生基础信息存map
		Map<String, ImportDomain> map = new HashMap<>();
		for (ImportDomain data : importDomains) {
			map.put(data.getName(), data);
		}
		List<Practice> list = new ArrayList<>();
		int i = 0;
		int companyNameMax = 5;
		int companyNameMin = 1;
		int reviewResultMax = 20;
		int reviewResultMin = 1;
		for (PracticeDomain d : dataBases) {
			try {
				// 每个学生循环增加10次实践任务
				for (int j = 0; j < 10; j++) {
					Practice practice = new Practice();
					practice.setOrgId(orgId);
					practice.setUserId(d.getUserId());
					practice.setUserName(d.getUserName());
					for (Entry<String, ImportDomain> entry : map.entrySet()) {
						if (entry.getKey().equals(d.getClassName())) {
							practice.setClassId(entry.getValue().getId());
							break;
						}
					}
					practice.setClassName(d.getClassName());
					for (Entry<String, ImportDomain> entry : map.entrySet()) {
						if (entry.getKey().equals(d.getCollegeName())) {
							practice.setCollegeId(entry.getValue().getId());
							break;
						}
					}
					practice.setCollegeName(d.getCollegeName());
					practice.setSchoolYear(d.getSchoolYear());
					practice.setJobNum(d.getJobNum());
					for (Entry<String, ImportDomain> entry : map.entrySet()) {
						if (entry.getKey().equals(d.getProfessionalName())) {
							practice.setProfessionalId(entry.getValue().getId());
							break;
						}
					}
					practice.setProfessionalName(d.getProfessionalName());
					practice.setUserPhone(d.getUserPhone());
//					if (null != d.getTaskCreatedDate()) {
//						Calendar rightNow = Calendar.getInstance();
//						rightNow.setTime(d.getTaskCreatedDate());
//						rightNow.add(Calendar.DAY_OF_YEAR, 1);// 日期加1天
//						Date taskCreatedDate = rightNow.getTime();
//						practice.setTaskCreatedDate(taskCreatedDate);
//					}
					practice.setTaskCreatedDate(DateUtil.randomDate("2017-10-02", "2017-10-15"));
					// 造数据：公司名称5个，按班级id大小随机取
					if (practice.getClassId() < 9700) {
						practice.setCompanyName(CompanyName.getName(1));
					} else if (practice.getClassId() < 9800) {
						practice.setCompanyName(CompanyName.getName(2));
					} else if (practice.getClassId() < 9900) {
						practice.setCompanyName(CompanyName.getName(3));
					} else if (practice.getClassId() < 10000) {
						practice.setCompanyName(CompanyName.getName(4));
					} else if (practice.getClassId() < 11000) {
						practice.setCompanyName(CompanyName.getName(5));
					}
// -----------------------公司名称5个，按随机1~5的数取公司名称-----------------------------------------------------------------//
// -----------------------审评状态3个，按随机1~100的数取审评状态(pass,notPass,backTo),pass率在80%-100%之间----------------------//
//					Random random = new Random();
//					int count = random.nextInt(companyNameMax) % (companyNameMax - companyNameMin + 1) + companyNameMin;
					Random random1 = new Random();
					int count1 = random1.nextInt(reviewResultMax) % (reviewResultMax - reviewResultMin + 1) + reviewResultMin;
//					switch (count) {
//					case 1:
//						practice.setCompanyName(CompanyName.getName(1));
//						break;
//					case 2:
//						practice.setCompanyName(CompanyName.getName(2));
//						break;
//					case 3:
//						practice.setCompanyName(CompanyName.getName(3));
//						break;
//					case 4:
//						practice.setCompanyName(CompanyName.getName(4));
//						break;
//					default:
//						practice.setCompanyName(CompanyName.getName(5));
//						break;
//					}
					switch (count1) {
					case 1:
						practice.setReviewResult("backTo");
						break;
					case 2:
						practice.setReviewResult("notPass");
						break;
					default:
						practice.setReviewResult("pass");
						break;
					}
					practice.setCompanyProvince(d.getCompanyProvince());
					practice.setCompanyCity(d.getCompanyCity());
					String uuid = UUID.randomUUID().toString().replaceAll("-", "");
					practice.setTaskId(uuid);
					practice.setSemester(2);
					practice.setGrade(d.getGrade());
					list.add(practice);
				}
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
