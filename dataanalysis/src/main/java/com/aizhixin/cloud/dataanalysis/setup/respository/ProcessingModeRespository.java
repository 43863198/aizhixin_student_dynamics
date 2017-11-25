package com.aizhixin.cloud.dataanalysis.setup.respository;


import com.aizhixin.cloud.dataanalysis.setup.entity.ProcessingMode;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface ProcessingModeRespository extends PagingAndSortingRepository<ProcessingMode, String> {
	
}
