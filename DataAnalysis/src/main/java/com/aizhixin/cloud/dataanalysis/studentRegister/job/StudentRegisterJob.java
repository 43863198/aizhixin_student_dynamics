package com.aizhixin.cloud.dataanalysis.studentRegister.job;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.aizhixin.cloud.dataanalysis.alertinformation.entity.AlertWarningInformation;
import com.aizhixin.cloud.dataanalysis.alertinformation.repository.AlertWarningInformationRepository;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.aizhixin.cloud.dataanalysis.common.constant.AlertTypeConstant;
import com.aizhixin.cloud.dataanalysis.common.constant.StudentRegisterConstant;
import com.aizhixin.cloud.dataanalysis.common.core.DataValidity;
import com.aizhixin.cloud.dataanalysis.common.util.DateUtil;
import com.aizhixin.cloud.dataanalysis.setup.entity.AlarmParameter;
import com.aizhixin.cloud.dataanalysis.setup.respository.AlarmParameterRespository;
import com.aizhixin.cloud.dataanalysis.studentRegister.mongoEntity.StudentRegister;
import com.aizhixin.cloud.dataanalysis.studentRegister.mongoRespository.StudentRegisterMongoRespository;

@Component
public class StudentRegisterJob {

	private Logger logger = Logger.getLogger(this.getClass());

	@Autowired
	private AlarmParameterRespository alarmParameterRespository;
	@Autowired
	private StudentRegisterMongoRespository stuRegisterMongoRespository;
	@Autowired
	private AlertWarningInformationRepository alertWarningInformationRepository;

	@Scheduled(cron = "0 0/2 * * * ?")
	public void studenteRegister() {

		List<AlarmParameter> alarmParams = alarmParameterRespository
				.findAllByDeleteFlagAndAlarmSettings_SetupCloseFlagAndAlarmSettings_TypeOrderByAlarmSettings_idAscLevelDesc(DataValidity.VALID.getState(),AlertTypeConstant.SETUP_ALERTSETTING,
						AlertTypeConstant.STUDENT_REGISTER);
		if (null != alarmParams && alarmParams.size() > 0) {
			HashMap<Long, ArrayList<AlarmParameter>> alarmMap = new HashMap<Long, ArrayList<AlarmParameter>>();
			ArrayList<AlertWarningInformation> alertInforList = new ArrayList<AlertWarningInformation>();
			//按orgId归类告警等级阀值
			for (AlarmParameter param : alarmParams) {
				Long orgId = param.getAlarmSettings().getOrgId();
				if (null != alarmMap.get(orgId)) {
					ArrayList<AlarmParameter> alarmList = alarmMap.get(orgId);
					alarmList.add(param);
				} else {
					ArrayList<AlarmParameter> alarmList = new ArrayList<AlarmParameter>();
					alarmList.add(param);
					alarmMap.put(orgId, alarmList);
				}
			}
			Iterator iter = alarmMap.entrySet().iterator();
			Date today = new Date();
			while (iter.hasNext()) {
				Map.Entry entry = (Map.Entry) iter.next();
				Long key = (Long)entry.getKey();
				ArrayList<AlarmParameter> val = (ArrayList<AlarmParameter>)entry.getValue();
				//按orgId查询未报到的学生信息
				List<StudentRegister> stuRegisterList = stuRegisterMongoRespository
						.findAllByOrgIdAndIsregister(key,StudentRegisterConstant.UNREGISTER);
				for(StudentRegister studentRegister : stuRegisterList){
					int result = DateUtil.getDaysBetweenDate(studentRegister.getRegisterDate(), today);
					for(AlarmParameter alarmParam : val){
						if(result >= alarmParam.getSetParameter()){
							AlertWarningInformation alertInfor = new AlertWarningInformation();
							alertInfor.setName(studentRegister.getStuName());
							alertInfor.setJobNumber(studentRegister.getJobNum());
							alertInfor.setCollogeName(studentRegister.getCollegeName());
							alertInfor.setTeachingYear(studentRegister.getGrade());
							alertInfor.setClassName(studentRegister.getClassName());
							alertInfor.setWarningTime(new Date());
							alertInforList.add(alertInfor);
							break;
						}else{
							continue;
						}
					}
				}
			}
			
			if(!alertInforList.isEmpty()){
				alertWarningInformationRepository.save(alertInforList);
			}
		}
	}
}
