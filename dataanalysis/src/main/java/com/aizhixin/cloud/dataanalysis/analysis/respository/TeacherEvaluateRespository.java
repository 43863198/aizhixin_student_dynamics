package com.aizhixin.cloud.dataanalysis.analysis.respository;

import com.aizhixin.cloud.dataanalysis.analysis.entity.TeacherEvaluate;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TeacherEvaluateRespository extends JpaRepository<TeacherEvaluate,String> {
    void deleteByOrgId(Long orgId);
}
