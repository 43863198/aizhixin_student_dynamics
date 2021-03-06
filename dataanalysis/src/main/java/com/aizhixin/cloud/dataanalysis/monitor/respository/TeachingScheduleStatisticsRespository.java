package com.aizhixin.cloud.dataanalysis.monitor.respository;

import java.util.List;

import com.aizhixin.cloud.dataanalysis.monitor.entity.AbnormalAttendanceStatistics;
import com.aizhixin.cloud.dataanalysis.monitor.entity.AbnormalTeachingStatistics;
import com.aizhixin.cloud.dataanalysis.monitor.entity.TeachingScheduleStatistics;

import org.springframework.data.jpa.repository.JpaRepository;


public interface TeachingScheduleStatisticsRespository extends JpaRepository<TeachingScheduleStatistics, String> {

	TeachingScheduleStatistics findOneByOrgIdAndStatisticalTime(Long orgId,String statisticalTime);
	
	 void deleteByOrgId(Long orgId);

	 List<TeachingScheduleStatistics> findByOrgIdAndDeleteFlag(Long orgId,Integer deleteFlag);
}

