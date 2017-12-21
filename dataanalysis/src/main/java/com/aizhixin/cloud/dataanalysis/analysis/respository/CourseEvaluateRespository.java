package com.aizhixin.cloud.dataanalysis.analysis.respository;

import com.aizhixin.cloud.dataanalysis.analysis.entity.CourseEvaluate;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CourseEvaluateRespository extends JpaRepository<CourseEvaluate,String> {
    void deleteByOrgId(Long orgId);
}
