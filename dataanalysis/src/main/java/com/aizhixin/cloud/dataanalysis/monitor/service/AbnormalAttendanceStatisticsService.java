package com.aizhixin.cloud.dataanalysis.monitor.service;

import java.util.List;

import com.aizhixin.cloud.dataanalysis.monitor.domain.AbnormalAttendanceDomain;
import com.aizhixin.cloud.dataanalysis.monitor.domain.TeachingScheduleDomain;
import com.aizhixin.cloud.dataanalysis.monitor.entity.AbnormalAttendanceStatistics;
import com.aizhixin.cloud.dataanalysis.monitor.entity.AbnormalTeachingStatistics;
import com.aizhixin.cloud.dataanalysis.monitor.respository.AbnormalAttendanceStatisticsRespository;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;


@Component
@Transactional
public class AbnormalAttendanceStatisticsService {
    @Autowired
    private AbnormalAttendanceStatisticsRespository abnormalAttendanceStatisticsRespository;
 
    public void deleteAllByOrgId(Long orgId){
    	abnormalAttendanceStatisticsRespository.deleteByOrgId(orgId);
    }
    
    public void save(AbnormalAttendanceStatistics abnormalAttendanceStatistics){
    	abnormalAttendanceStatisticsRespository.save(abnormalAttendanceStatistics);
    }
    
    public void saveList(List<AbnormalAttendanceStatistics> abnormalAttendanceStatisticsList){
    	abnormalAttendanceStatisticsRespository.save(abnormalAttendanceStatisticsList);
    }
    
    public AbnormalAttendanceStatistics findById(String id){
    	return abnormalAttendanceStatisticsRespository.findOne(id);
    }
    
    public AbnormalAttendanceStatistics findByOrgIdAndStatisticalTime(Long orgId,String todayStr){
    	return abnormalAttendanceStatisticsRespository.findOneByOrgIdAndStatisticalTime(orgId,todayStr);
    }
    
    public AbnormalAttendanceDomain findByOrgId(Long orgId){
    	
    	AbnormalAttendanceDomain domain = new AbnormalAttendanceDomain();
    	List<AbnormalAttendanceStatistics> list = abnormalAttendanceStatisticsRespository.findFirst1ByOrgIdOrderByCreatedDateDesc(orgId);
    	if(null != list && list.size() > 0){
    		AbnormalAttendanceStatistics abnormalAttendanceStatistics = list.get(0);
    		BeanUtils.copyProperties(abnormalAttendanceStatistics, domain);
    	}
    	return domain;
    }
}
