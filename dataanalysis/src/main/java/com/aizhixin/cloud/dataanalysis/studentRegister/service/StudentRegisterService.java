package com.aizhixin.cloud.dataanalysis.studentRegister.service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.aizhixin.cloud.dataanalysis.studentRegister.common.CommonException;
import com.aizhixin.cloud.dataanalysis.studentRegister.common.ErrorCode;
import com.aizhixin.cloud.dataanalysis.studentRegister.common.ExcelBasedataHelper;
import com.aizhixin.cloud.dataanalysis.studentRegister.common.ExcelUtil;
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

	@Autowired
	private StudentRegisterMongoRespository respository;
	
	@Autowired
	private ExcelBasedataHelper basedataHelper;


	public void importData(Long orgId, MultipartFile studentInfoFile,MultipartFile dataBaseFile, Date registerDate) throws ParseException {
		//获取基础数据源
		List<StudentRegisterDomain> dataBases = basedataHelper.readStudentRegisterFromInputStream(dataBaseFile);
		if (null == dataBases || dataBases.size() <= 0) {
			throw new CommonException(ErrorCode.ID_IS_REQUIRED, "没有读取到任何数据");
		}
		//获取学生信息
		List<StudentInfoDomain> studentInfos = basedataHelper.readStudentInfoFromInputStream(studentInfoFile);
		if (null == studentInfos || studentInfos.size() <= 0) {
			throw new CommonException(ErrorCode.ID_IS_REQUIRED, "没有读取到任何数据");
		}
		
		Map<String, List<StudentRegisterDomain>> maps = new HashMap<>();
		Set<String> jobNums = new HashSet<>();
		for (StudentRegisterDomain data : dataBases) {
			List<StudentRegisterDomain> list = maps.get(data.getJobNum());
			if (null == list) {
				list = new ArrayList<>();
				maps.put(data.getJobNum(), list);
			}
			jobNums.add(data.getJobNum());
		}
		
		Map<String, List<StudentInfoDomain>> maps1= new HashMap<>();
		Set<String> jobNums1 = new HashSet<>();
		for (StudentInfoDomain data : studentInfos) {
			List<StudentInfoDomain> list = maps1.get(data.getJobNum());
			if (null == list) {
				list = new ArrayList<>();
				maps1.put(data.getJobNum(), list);
			}
			jobNums1.add(data.getJobNum());
		}
		Set<String> keySet = maps.keySet();
		List<StudentRegister> stuRegisterList = new ArrayList<>();
		for (String key : keySet) {
			if (maps1.containsKey(key)) {
				
				for (StudentRegisterDomain data : dataBases) {
					StudentRegister studentRegister = new StudentRegister();
					studentRegister.setIsregister(data.getIsregister());
					studentRegister.setGrade(data.getGrade());
					if (null != data.getActualRegisterDate()) {
						SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");  
						Date actualRegisterDate = sdf.parse(data.getActualRegisterDate()); 
						studentRegister.setActualRegisterDate(actualRegisterDate);
					}
					studentRegister.setRegisterDate(registerDate);
					studentRegister.setSchoolYear(data.getSchoolYear());
					stuRegisterList.add(studentRegister);
				}
			}
		}
		respository.save(stuRegisterList);
	}
}
