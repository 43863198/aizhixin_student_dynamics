package com.aizhixin.cloud.dataanalysis.monitor.respository;

import java.util.List;

import com.aizhixin.cloud.dataanalysis.monitor.entity.AbnormalAttendanceStatistics;

import org.springframework.data.jpa.repository.JpaRepository;


public interface AbnormalAttendanceStatisticsRespository extends JpaRepository<AbnormalAttendanceStatistics, String> {

	List<AbnormalAttendanceStatistics> findFirst1ByOrgIdOrderByCreatedDateDesc(Long orgId);
	
	AbnormalAttendanceStatistics findOneByOrgIdAndStatisticalTime(Long orgId,String statisticalTime);
	
	 void deleteByOrgId(Long orgId);
}

