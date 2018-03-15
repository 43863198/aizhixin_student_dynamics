package com.aizhixin.cloud.dataanalysis.analysis.respository;

import com.aizhixin.cloud.dataanalysis.analysis.entity.CourseEvaluate;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CourseEvaluateRespository extends JpaRepository<CourseEvaluate,String> {
    void deleteByOrgId(Long orgId);
    List<CourseEvaluate> findByOrgIdAndDeleteFlag(Long orgId,Integer deleteFlag);
}
