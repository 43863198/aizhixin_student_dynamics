package com.aizhixin.cloud.dataanalysis.rollCall.service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.aizhixin.cloud.dataanalysis.rollCall.domain.RollCallDomain;
import com.aizhixin.cloud.dataanalysis.rollCall.mongoEntity.RollCall;
import com.aizhixin.cloud.dataanalysis.rollCall.mongoRespository.RollCallMongoRespository;
import com.aizhixin.cloud.dataanalysis.studentRegister.common.CommonException;
import com.aizhixin.cloud.dataanalysis.studentRegister.common.ErrorCode;
import com.aizhixin.cloud.dataanalysis.studentRegister.common.ExcelBasedataHelper;

/**
 * 成绩导入
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

	public void importData(MultipartFile dataBaseFile) {
		//获取成绩
		List<RollCallDomain> dataBases = basedataHelper.readScoreFromInputStream(dataBaseFile);
		if (null == dataBases || dataBases.size() <= 0) {
			throw new CommonException(ErrorCode.ID_IS_REQUIRED, "没有读取到任何数据");
		}
		List<RollCall> list = new ArrayList<>();
		for (RollCallDomain d : dataBases) {
			try {
				RollCall rollCall = new RollCall();
				rollCall.setOrgId(d.getOrgId());
				rollCall.setClassId(d.getClassId());
				rollCall.setClassName(d.getClassName());
				rollCall.setCollegeId(d.getCollegeId());
				rollCall.setCollegeName(d.getCollegeName());
				rollCall.setGrade(d.getGrade());
				rollCall.setJobNum(d.getJobNum());
				rollCall.setProfessionalId(d.getProfessionalId());
				rollCall.setProfessionalName(d.getProfessionalName());
				rollCall.setScheduleId(d.getScheduleId());
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				Date date = sdf.parse(d.getRollCallDate());  
				rollCall.setRollCallDate(date);
				rollCall.setRollCallResult(d.getRollCallResult());
				list.add(rollCall);
			} catch (Exception e) {
				LOG.info("错误信息行号：" + d.getLine() + ",  学号：" + d.getJobNum());
				e.printStackTrace();
			}
		}
		respository.save(list);
	}
}
