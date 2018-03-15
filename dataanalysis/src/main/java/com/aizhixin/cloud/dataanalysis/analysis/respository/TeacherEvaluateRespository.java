package com.aizhixin.cloud.dataanalysis.analysis.respository;

import com.aizhixin.cloud.dataanalysis.analysis.entity.TeacherEvaluate;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TeacherEvaluateRespository extends JpaRepository<TeacherEvaluate,String> {
    void deleteByOrgId(Long orgId);
    List<TeacherEvaluate> findByOrgIdAndDeleteFlag(Long orgId,Integer deleteFlag);
}
