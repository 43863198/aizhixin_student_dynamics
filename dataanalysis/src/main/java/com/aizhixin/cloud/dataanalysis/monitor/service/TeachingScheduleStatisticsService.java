package com.aizhixin.cloud.dataanalysis.monitor.service;

import java.util.List;

import com.aizhixin.cloud.dataanalysis.common.util.DateUtil;
import com.aizhixin.cloud.dataanalysis.monitor.domain.TeachingScheduleDomain;
import com.aizhixin.cloud.dataanalysis.monitor.entity.AbnormalTeachingStatistics;
import com.aizhixin.cloud.dataanalysis.monitor.entity.TeachingScheduleStatistics;
import com.aizhixin.cloud.dataanalysis.monitor.respository.TeachingScheduleStatisticsRespository;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;


@Component
@Transactional
public class TeachingScheduleStatisticsService {
    @Autowired
    private TeachingScheduleStatisticsRespository teachingScheduleStatisticsRespository;
 
    public void save(TeachingScheduleStatistics teachingScheduleStatistics){
    	teachingScheduleStatisticsRespository.save(teachingScheduleStatistics);
    }
    
    public TeachingScheduleStatistics findById(String id){
    	return teachingScheduleStatisticsRespository.findOne(id);
    }
    
    public TeachingScheduleDomain findByOrgId(Long orgId){
    	
    	TeachingScheduleDomain domain = new TeachingScheduleDomain();
    	
    	String todayStr = DateUtil.getCurrentTime(DateUtil.FORMAT_SHORT);
    	TeachingScheduleStatistics teachingScheduleStatistics = teachingScheduleStatisticsRespository.findOneByOrgIdAndStatisticalTime(orgId,todayStr);
    	if(null != teachingScheduleStatistics){
    		BeanUtils.copyProperties(teachingScheduleStatistics, domain);
    	}
    	return domain;
    }
}
