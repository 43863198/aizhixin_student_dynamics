package com.aizhixin.cloud.dataanalysis.rollCall.service;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import org.apache.commons.lang.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.aizhixin.cloud.dataanalysis.common.excelutil.ExcelBasedataHelper;
import com.aizhixin.cloud.dataanalysis.common.exception.CommonException;
import com.aizhixin.cloud.dataanalysis.common.exception.ErrorCode;
import com.aizhixin.cloud.dataanalysis.rollCall.domain.RollCallDomain;
import com.aizhixin.cloud.dataanalysis.rollCall.mongoEntity.RollCall;
import com.aizhixin.cloud.dataanalysis.rollCall.mongoRespository.RollCallMongoRespository;

/**
 * 考勤导入
 * 
 * @author bly
 * @data 2017年11月27日
 */
@Component
@Transactional
public class RollCallService {
	final static private Logger LOG = LoggerFactory.getLogger(RollCallService.class);

	@Autowired
	private RollCallMongoRespository respository;
	
	@Autowired
	private ExcelBasedataHelper basedataHelper;

	public void importData(MultipartFile dataBaseFile) {
		//获取考勤
		List<RollCallDomain> dataBases = basedataHelper.readRollCallFromInputStream(dataBaseFile);
		if (null == dataBases || dataBases.size() <= 0) {
			throw new CommonException(ErrorCode.ID_IS_REQUIRED, "没有读取到任何数据");
		}
		List<RollCall> list = new ArrayList<>();
		int i = 0;
		for (RollCallDomain d : dataBases) {
			try {
				RollCall rollCall = new RollCall();
				rollCall.setOrgId(d.getOrgId());
				rollCall.setUserId(d.getUserId());
				rollCall.setUserName(d.getUserName());
				rollCall.setClassId(d.getClassId());
				rollCall.setClassName(d.getClassName());
				rollCall.setCollegeId(d.getCollegeId());
				rollCall.setCollegeName(d.getCollegeName());
				rollCall.setSchoolYear(d.getSchoolYear());
				rollCall.setJobNum(d.getJobNum());
				rollCall.setProfessionalId(d.getProfessionalId());
				rollCall.setProfessionalName(d.getProfessionalName());
				rollCall.setScheduleId(d.getScheduleId());
//				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//				Date date = sdf.parse(d.getRollCallDate());  
				if (d.getRollCallDate() != null) {
					String date = d.getRollCallDate().substring(0, 5);
    				Integer date1 = Integer.valueOf(date);
    				if (date1 > 0) {
    					Calendar c = new GregorianCalendar(1900,0,-1);  
    					Date d1 = c.getTime();  
    					Date _d = DateUtils.addDays(d1, date1 + 1);  //42605是距离1900年1月1日的天数
    					rollCall.setRollCallDate(_d);
    				}
    			}
				rollCall.setRollCallResult(Integer.parseInt(d.getRollCallResult()));
				rollCall.setSemester(2);
				list.add(rollCall);
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
