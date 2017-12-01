package com.aizhixin.cloud.dataanalysis.analysis.respository;

import com.aizhixin.cloud.dataanalysis.analysis.dto.PracticeStaticsDTO;
import com.aizhixin.cloud.dataanalysis.analysis.dto.SchoolProfileDTO;
import com.aizhixin.cloud.dataanalysis.analysis.entity.PracticeStatistics;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

/**
 * @author: Created by jun.wang
 * @E-mail: wangjun@aizhixin.com
 * @Date: 2017-11-30
 */
public interface PracticeStaticsRespository extends JpaRepository<PracticeStatistics, String> {
    @Query("select new com.aizhixin.cloud.dataanalysis.analysis.dto.PracticeStaticsDTO(sum(a.practiceStudentNum),sum(a.practiceCompanyNum),sum(a.taskNum),sum(a.taskPassNum)) from #{#entityName} a where a.orgId = :orgId and a.state=0 ")
    PracticeStaticsDTO getPracticeStatics(@Param(value = "orgId")Long orgId);
}
