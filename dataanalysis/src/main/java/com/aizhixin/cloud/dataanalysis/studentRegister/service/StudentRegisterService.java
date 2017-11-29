package com.aizhixin.cloud.dataanalysis.studentRegister.service;

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

import com.aizhixin.cloud.dataanalysis.studentRegister.common.CommonException;
import com.aizhixin.cloud.dataanalysis.studentRegister.common.ErrorCode;
import com.aizhixin.cloud.dataanalysis.studentRegister.common.ExcelBasedataHelper;
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

	public void importData(MultipartFile studentInfoFile,MultipartFile dataBaseFile, Date registerDate) {
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
		Map<String, StudentInfoDomain> maps1= new HashMap<>();
		for (StudentInfoDomain data : studentInfos) {
			maps1.put(data.getJobNum(), data);
		}
		List<StudentRegister> stuRegisterList = new ArrayList<>();
		//学生数据key value
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
		    					Calendar c = new GregorianCalendar(1900,0,-1);  
		    					Date d = c.getTime();  
		    					Date _d = DateUtils.addDays(d, date + 1);  //42605是距离1900年1月1日的天数
		    					studentRegister.setActualRegisterDate(_d);
		    				}
		    			}
		    			studentRegister.setIsregister(entry.getValue().getIsregister());
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
		    			stuRegisterList.add(studentRegister);
		    		}
				} catch (Exception e) {
					LOG.info("错误信息行号：" + entry.getValue().getLine() + ",  学号：" + entry.getValue().getJobNum());
					LOG.info("错误信息行号：" + entry1.getValue().getLine() + ",  学号：" + entry1.getValue().getJobNum());
					e.printStackTrace();
				}
		    }
		}  
		respository.save(stuRegisterList);
	}
}
