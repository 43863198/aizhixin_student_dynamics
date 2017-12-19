package com.aizhixin.cloud.dataanalysis.monitor.respository;

import java.util.List;

import com.aizhixin.cloud.dataanalysis.monitor.entity.AbnormalTeachingStatistics;

import org.springframework.data.jpa.repository.JpaRepository;


public interface AbnormalTeachingStatisticsRespository extends JpaRepository<AbnormalTeachingStatistics, String> {

	List<AbnormalTeachingStatistics> findFirst1ByOrgIdOrderByCreatedDateDesc(Long orgId);
	
	AbnormalTeachingStatistics findOneByOrgIdAndStatisticalTime(Long orgId,String statisticalTime);
}

