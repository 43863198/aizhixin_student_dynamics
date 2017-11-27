package com.aizhixin.cloud.dataanalysis.setup.respository;


import java.util.List;
import java.util.Set;

import com.aizhixin.cloud.dataanalysis.setup.entity.ProcessingMode;

import org.springframework.data.repository.PagingAndSortingRepository;

public interface ProcessingModeRespository extends PagingAndSortingRepository<ProcessingMode, String> {
	
	List<ProcessingMode> findAllByAlarmSettingsIdInAndDeleteFlag(Set<String> settingsIds,int deleteFlag);
}
