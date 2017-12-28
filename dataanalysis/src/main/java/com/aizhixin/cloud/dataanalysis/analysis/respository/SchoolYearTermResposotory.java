package com.aizhixin.cloud.dataanalysis.analysis.respository;

import com.aizhixin.cloud.dataanalysis.analysis.entity.SchoolYearTerm;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SchoolYearTermResposotory extends JpaRepository<SchoolYearTerm,String> {
    void deleteByOrgId(Long orgId);
    void deleteByOrgIdAndDataType(Long orgId,String dataType);

}
