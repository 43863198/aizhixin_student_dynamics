package com.aizhixin.cloud.dataanalysis.setup.respository;


import com.aizhixin.cloud.dataanalysis.setup.entity.ProcessingMode;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ProcessingModeRespository extends PagingAndSortingRepository<ProcessingMode, String> {
    @Query("select ast from #{#entityName} ast where ast.deleteFlag = :deleteFlag and ast.warningType = :warningTypeId and ast.orgId = :orgId")
    List<ProcessingMode> getProcessingModeBywarningTypeId(@Param("orgId")Long orgId, @Param("warningTypeId")String warningTypeId, @Param("deleteFlag")int deleteFlag);


}
