package com.aizhixin.cloud.dataanalysis.studentRegister.service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.aizhixin.cloud.dataanalysis.studentRegister.common.CommonException;
import com.aizhixin.cloud.dataanalysis.studentRegister.common.ErrorCode;
import com.aizhixin.cloud.dataanalysis.studentRegister.common.ExcelUtil;
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

	/**
	 * 将Cell类型设置为字符串类型
	 * 
	 * @param row
	 * @param n
	 */
	private void setCellStringType(Row row, int n) {
		for (int i = 0; i < n; i++) {
			if (null != row.getCell(i)) {
				row.getCell(i).setCellType(CellType.STRING);
			}
		}
	}

	/**
	 * 获取字符串类型的Cell的值
	 * 
	 * @param row
	 * @param i
	 * @return
	 */
	private String getCellStringValue(Row row, int i) {
		if (null != row.getCell(i)) {
			String t = row.getCell(i).getStringCellValue();
			if (null != t) {
				t = t.trim();
			}
			return t;
		}
		return null;
	}

	public List<StudentRegisterDomain> readStudentRegisterFromInputStream(MultipartFile file) {
		List<StudentRegisterDomain> list = new ArrayList<>();
		ExcelUtil util = new ExcelUtil(file);
		Sheet sheet = util.getSheet("新生报到2017");
		if (null == sheet) {// 如果没有此sheet页标签，读取第1个标签的内容
			sheet = util.getSheet(0);
		}
		if (null != sheet) {
			Iterator<Row> rows = sheet.rowIterator();
			int line = 1;
			while (rows.hasNext()) {
				Row row = rows.next();
				if (1 == line) {
					line++;
					continue;// 跳过第一行
				}
				setCellStringType(row, 73);// 一行共有73个列值
				String jobNum = getCellStringValue(row, 5);
				Long userId = null;
				String userName = getCellStringValue(row, 64);
				Long classId = null;
				String className = getCellStringValue(row, 0);
				Long collegeId = null;
				String collegeName = null;
				Long professionalId = null;
				String professionalName = null;
				String grade = getCellStringValue(row, 4);
				String schoolYear = null;
				String actualRegisterDate = null;
				int isregister = 1;
				list.add(new StudentRegisterDomain(line, jobNum, userId, userName, classId, className, collegeId,
						collegeName, professionalId, professionalName, grade, isregister, actualRegisterDate,
						schoolYear));
				line++;
			}
		}
		return list;
	}

	public void importData(Long orgId, MultipartFile file, Date registerDate) throws ParseException {
		List<StudentRegisterDomain> excelDatas = readStudentRegisterFromInputStream(file);
		if (null == excelDatas || excelDatas.size() <= 0) {
			throw new CommonException(ErrorCode.ID_IS_REQUIRED, "没有读取到任何数据");
		}
		List<StudentRegister> stuRegisterList = new ArrayList<StudentRegister>();
		for (StudentRegisterDomain data : excelDatas) {
			StudentRegister studentRegister = new StudentRegister();
			studentRegister.setOrgId(orgId);
			studentRegister.setGrade(data.getGrade());
			studentRegister.setJobNum(data.getJobNum());
			studentRegister.setUserId(data.getUserId());
			studentRegister.setUserName(data.getUserName());
			studentRegister.setClassId(data.getClassId());
			studentRegister.setClassName(data.getClassName());
			studentRegister.setCollegeId(data.getCollegeId());
			studentRegister.setCollegeName(data.getCollegeName());
			studentRegister.setProfessionalId(data.getProfessionalId());
			studentRegister.setProfessionalName(data.getProfessionalName());
			studentRegister.setIsregister(data.getIsregister());
			if (null != data.getActualRegisterDate()) {
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");  
				Date actualRegisterDate = sdf.parse(data.getActualRegisterDate()); 
				studentRegister.setActualRegisterDate(actualRegisterDate);
			}
			studentRegister.setRegisterDate(registerDate);
			studentRegister.setSchoolYear(data.getSchoolYear());
			stuRegisterList.add(studentRegister);
		}
		respository.save(stuRegisterList);
	}
}
