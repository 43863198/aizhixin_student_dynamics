package com.aizhixin.cloud.dataanalysis.monitor.service;

import java.util.List;

import com.aizhixin.cloud.dataanalysis.monitor.domain.AbnormalTeachingDomain;
import com.aizhixin.cloud.dataanalysis.monitor.domain.TeachingScheduleDomain;
import com.aizhixin.cloud.dataanalysis.monitor.entity.AbnormalAttendanceStatistics;
import com.aizhixin.cloud.dataanalysis.monitor.entity.AbnormalTeachingStatistics;
import com.aizhixin.cloud.dataanalysis.monitor.entity.TeachingScheduleStatistics;
import com.aizhixin.cloud.dataanalysis.monitor.respository.AbnormalTeachingStatisticsRespository;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;


@Component
@Transactional
public class AbnormalTeachingStatisticsService {
    @Autowired
    private AbnormalTeachingStatisticsRespository abnormalTeachingStatisticsRespository;
 
    public void save(AbnormalTeachingStatistics abnormalTeachingStatistics){
    	abnormalTeachingStatisticsRespository.save(abnormalTeachingStatistics);
    }
    
    public AbnormalTeachingStatistics findById(String id){
    	return abnormalTeachingStatisticsRespository.findOne(id);
    }
    
    public AbnormalTeachingDomain findByOrgId(Long orgId){
    	AbnormalTeachingDomain domain = new AbnormalTeachingDomain();
    	List<AbnormalTeachingStatistics> list = abnormalTeachingStatisticsRespository.findFirst1ByOrgIdOrderByCreatedDateDesc(orgId);
    	if(null != list && list.size() > 0){
    		AbnormalTeachingStatistics abnormalTeachingStatistics = list.get(0);
    		BeanUtils.copyProperties(abnormalTeachingStatistics, domain);
    	}
    	return domain;
    }
}
