package com.aizhixin.cloud.dataanalysis.common.excelutil;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import com.aizhixin.cloud.dataanalysis.analysis.domain.PracticeDomain;
import com.aizhixin.cloud.dataanalysis.common.domain.ImportDomain;
import com.aizhixin.cloud.dataanalysis.rollCall.domain.RollCallDomain;
import com.aizhixin.cloud.dataanalysis.score.domain.ScoreDomain;
import com.aizhixin.cloud.dataanalysis.studentRegister.domain.StudentInfoDomain;
import com.aizhixin.cloud.dataanalysis.studentRegister.domain.StudentRegisterDomain;
/**
 * excel导入数据
 * 
 * @author bly
 * @data 2017年12月5日
 */
@Component
public class ExcelBasedataHelper {
	final static private Logger LOG = LoggerFactory.getLogger(ExcelBasedataHelper.class);

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
		Sheet sheet = util.getSheet("new");
		if (null == sheet) {// 如果没有此sheet页标签，读取第1个标签的内容
			sheet = util.getSheet(0);
		}
		if (null != sheet) {
			Iterator<Row> rows = sheet.rowIterator();
			int line = 1;
			String jobNum = null;
			while (rows.hasNext()) {
				Row row = rows.next();
				if (1 == line) {
					line++;
					continue;// 跳过第一行
				}
				try {
					setCellStringType(row, 73);// 一行共有73个列值
					String grade = getCellStringValue(row, 4);
					String schoolYear = getCellStringValue(row, 4);
					jobNum = getCellStringValue(row, 5);
					String actualRegisterDate = getCellStringValue(row, 39);
					int isPay = 0;
					int isGreenChannel = 0;
					if (!"".equals(getCellStringValue(row, 69)) && getCellStringValue(row, 69).length() > 0) {
						isGreenChannel = 1;// 银行卡号
						isPay = 1;
					}
					int isRegister = 0;
					if ("".equals(actualRegisterDate) || actualRegisterDate.length() <= 0) {
						isRegister = 1;
					}
					list.add(new StudentRegisterDomain(line, jobNum, grade, isRegister, actualRegisterDate, schoolYear,
							isPay, isGreenChannel));
				} catch (Exception e) {
					StudentRegisterDomain d = new StudentRegisterDomain();
					d.setLine(line);
					d.setJobNum(jobNum);
					list.add(d);
					LOG.info("错误信息行号：" + line + ",  学号：" + jobNum);
				}
				line++;
			}
		}
		return list;
	}

	public List<StudentInfoDomain> readStudentInfoFromInputStream(MultipartFile file) {
		List<StudentInfoDomain> list = new ArrayList<>();
		ExcelUtil util = new ExcelUtil(file);
		Sheet sheet = util.getSheet("new");
		if (null == sheet) {// 如果没有此sheet页标签，读取第1个标签的内容
			sheet = util.getSheet(0);
		}
		if (null != sheet) {
			Iterator<Row> rows = sheet.rowIterator();
			int line = 1;
			String jobNum = null;
			while (rows.hasNext()) {
				Row row = rows.next();
				if (1 == line) {
					line++;
					continue;// 跳过第一行
				}
				try {
					setCellStringType(row, 11);// 一行共有11个列值
					Long orgId = Long.valueOf(getCellStringValue(row, 0));
					Long userId = Long.valueOf(getCellStringValue(row, 1));
					jobNum = getCellStringValue(row, 2);
					String userName = getCellStringValue(row, 3);
					Long classId = Long.valueOf(getCellStringValue(row, 5));
					String className = getCellStringValue(row, 6);
					Long professionalId = Long.valueOf(getCellStringValue(row, 7));
					String professionalName = getCellStringValue(row, 8);
					Long collegeId = Long.valueOf(getCellStringValue(row, 9));
					String collegeName = getCellStringValue(row, 10);
					String userPhone = getCellStringValue(row, 4);
					list.add(new StudentInfoDomain(line, orgId, jobNum, userId, userName, classId, className,
							professionalId, professionalName, collegeId, collegeName, userPhone));
				} catch (Exception e) {
					StudentInfoDomain d = new StudentInfoDomain();
					d.setLine(line);
					d.setJobNum(jobNum);
					list.add(d);
					LOG.info("错误信息行号：" + line + ",  学号：" + jobNum);
				}
				line++;
			}
		}
		return list;
	}

	public List<ScoreDomain> readStudentScoreFromInputStream(MultipartFile file) {
		List<ScoreDomain> list = new ArrayList<>();
		ExcelUtil util = new ExcelUtil(file);
		Sheet sheet = util.getSheet("new");
		if (null == sheet) {// 如果没有此sheet页标签，读取第1个标签的内容
			sheet = util.getSheet(0);
		}
		if (null != sheet) {
			Iterator<Row> rows = sheet.rowIterator();
			int line = 1;
			String jobNum = null;
			while (rows.hasNext()) {
				Row row = rows.next();
				try {
					setCellStringType(row, 26);// 一行共有25个列值
					Long orgId = Long.valueOf(getCellStringValue(row, 0));
					Long userId = Long.valueOf(getCellStringValue(row, 1));
					jobNum = getCellStringValue(row, 2);
					String userName = getCellStringValue(row, 3);
					Long classId = Long.valueOf(getCellStringValue(row, 6));
					String className = getCellStringValue(row, 7);
					Long professionalId = Long.valueOf(getCellStringValue(row, 9));
					String professionalName = getCellStringValue(row, 10);
					Long collegeId = Long.valueOf(getCellStringValue(row, 11));
					String collegeName = getCellStringValue(row, 12);
					Integer semester = null;
					if (!StringUtils.isEmpty(getCellStringValue(row, 15))) {
						semester = Integer.valueOf(getCellStringValue(row, 15));
					}
					Integer schoolYear = null;
					if (!StringUtils.isEmpty(getCellStringValue(row, 14))) {
						schoolYear = Integer.valueOf(getCellStringValue(row, 14));
					}
					String grade = getCellStringValue(row, 8);
					String scheduleId = getCellStringValue(row, 17);
					String credit = getCellStringValue(row, 18);
					String examTime = getCellStringValue(row, 16);
					String usualScore = getCellStringValue(row, 23);
					String courseType = getCellStringValue(row, 20);
					String totalScore = getCellStringValue(row, 21);
					String gradePoint = getCellStringValue(row, 22);
					String userPhone = getCellStringValue(row, 4);
					list.add(new ScoreDomain(line, orgId, jobNum, userId, userName, classId, className, professionalId,
							professionalName, collegeId, collegeName, userPhone, semester, schoolYear, scheduleId, courseType,
							usualScore, credit, gradePoint, examTime, totalScore, grade));
				} catch (Exception e) {
					ScoreDomain d = new ScoreDomain();
					d.setLine(line);
					d.setJobNum(jobNum);
					list.add(d);
					LOG.info("错误信息行号：" + line + ",  学号：" + jobNum);
				}
				line++;
			}
		}
		return list;
	}

	public List<RollCallDomain> readRollCallFromInputStream(MultipartFile file) {
		List<RollCallDomain> list = new ArrayList<>();
		ExcelUtil util = new ExcelUtil(file);
		Sheet sheet = util.getSheet("new");
		if (null == sheet) {// 如果没有此sheet页标签，读取第1个标签的内容
			sheet = util.getSheet(0);
		}
		if (null != sheet) {
			Iterator<Row> rows = sheet.rowIterator();
			int line = 1;
			String jobNum = null;
			while (rows.hasNext()) {
				Row row = rows.next();
				if (1 == line) {
					line++;
					continue;// 跳过第一行
				}
				try {
					setCellStringType(row, 15);// 一行共有15个列值
					Long orgId = Long.valueOf(getCellStringValue(row, 0));
					Long userId = Long.valueOf(getCellStringValue(row, 1));
					jobNum = getCellStringValue(row, 2);
					String userName = getCellStringValue(row, 3);
					Long classId = Long.valueOf(getCellStringValue(row, 5));
					String className = getCellStringValue(row, 6);
					Long professionalId = Long.valueOf(getCellStringValue(row, 7));
					String professionalName = getCellStringValue(row, 8);
					Long collegeId = Long.valueOf(getCellStringValue(row, 9));
					String collegeName = getCellStringValue(row, 10);
					Long scheduleId = null;
					if (getCellStringValue(row, 11).length() > 0 && !"".equals(getCellStringValue(row, 11))) {
						scheduleId = Long.valueOf(getCellStringValue(row, 11));
					}
					String rollCallDate = getCellStringValue(row, 14);
					String rollCallResult = getCellStringValue(row, 12);
					String userPhone = getCellStringValue(row, 4);
					Integer schoolYear = 2017;
					if (!"".equals(getCellStringValue(row, 13)) && getCellStringValue(row, 13).length() == 4) {
						schoolYear = Integer.valueOf(getCellStringValue(row, 13));
					}
					list.add(new RollCallDomain(line, orgId, jobNum, userId, userName, classId, className,
							professionalId, professionalName, collegeId, collegeName,userPhone, scheduleId, rollCallDate, schoolYear,
							rollCallResult));
				} catch (Exception e) {
					RollCallDomain d = new RollCallDomain();
					d.setLine(line);
					d.setJobNum(jobNum);
					list.add(d);
					LOG.info("错误信息行号：" + line + ",  学号：" + jobNum);
				}
				line++;
			}
		}
		return list;
	}
	
	public List<PracticeDomain> readPracticeFromInputStream(MultipartFile file) {
		List<PracticeDomain> list = new ArrayList<>();
		ExcelUtil util = new ExcelUtil(file);
		Sheet sheet = util.getSheet("new");
		if (null == sheet) {// 如果没有此sheet页标签，读取第1个标签的内容
			sheet = util.getSheet(0);
		}
		if (null != sheet) {
			Iterator<Row> rows = sheet.rowIterator();
			int line = 1;
			String jobNum = null;
			while (rows.hasNext()) {
				Row row = rows.next();
				if (1 == line) {
					line++;
					continue;// 跳过第一行
				}
				try {
					setCellStringType(row, 15);// 一行共有15个列值
					Long orgId = Long.valueOf(getCellStringValue(row, 0));
					Long userId = Long.valueOf(getCellStringValue(row, 1));
					jobNum = getCellStringValue(row, 2);
					String userName = getCellStringValue(row, 3);
					String userPhone = getCellStringValue(row, 4);
					Long classId = Long.valueOf(getCellStringValue(row, 5));
					String className = getCellStringValue(row, 6);
					Long professionalId = Long.valueOf(getCellStringValue(row, 7));
					String professionalName = getCellStringValue(row, 8);
					Long collegeId = Long.valueOf(getCellStringValue(row, 9));
					String collegeName = getCellStringValue(row, 10);
					String companyName = getCellStringValue(row, 11);
					String companyProvince = getCellStringValue(row, 12);
					String companyCity = getCellStringValue(row, 13);
					String reviewResult = getCellStringValue(row, 14);
					Date taskCreatedDate = row.getCell(15).getDateCellValue();
					Integer schoolYear = 2016;
					String grade = "2016";
					list.add(new PracticeDomain(line, orgId, jobNum, userId, userName, classId, className, professionalId, 
							professionalName, collegeId, collegeName, userPhone, /*semester,*/ schoolYear, companyName,
							companyProvince, companyCity, reviewResult, taskCreatedDate, grade));
				} catch (Exception e) {
					PracticeDomain d = new PracticeDomain();
					d.setLine(line);
					d.setJobNum(jobNum);
					list.add(d);
					LOG.info("错误信息行号：" + line + ",  学号：" + jobNum);
				}
				line++;
			}
		}
		return list;
	}

	public List<ImportDomain> readDataBase(MultipartFile file) {
		List<ImportDomain> list = new ArrayList<>();
		ExcelUtil util = new ExcelUtil(file);
		Sheet sheet = util.getSheet("new");
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
				try {
					setCellStringType(row, 2);// 一行共有2个列值
					Long id = Long.valueOf(getCellStringValue(row, 0));
					String name = getCellStringValue(row, 1);
					list.add(new ImportDomain(id, name, line));
				} catch (Exception e) {
					ImportDomain d = new ImportDomain();
					d.setLine(line);
					list.add(d);
					LOG.info("错误信息行号：" + line);
				}
				line++;
			}
		}
		return list;
	}
	
}
