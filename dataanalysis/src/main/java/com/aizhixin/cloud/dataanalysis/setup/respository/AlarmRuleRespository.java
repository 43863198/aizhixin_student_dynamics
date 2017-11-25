package com.aizhixin.cloud.dataanalysis.setup.respository;


import java.util.List;

import org.springframework.data.repository.PagingAndSortingRepository;

import com.aizhixin.cloud.dataanalysis.setup.entity.AlarmRule;

public interface AlarmRuleRespository extends PagingAndSortingRepository<AlarmRule, String> {
	
//	List<AlarmRule> findAllByDeleteFlagAndWarningType(int deleteFlag,String warningType);
}
