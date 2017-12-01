package com.aizhixin.cloud.dataanalysis.analysis.respository;

import com.aizhixin.cloud.dataanalysis.analysis.dto.SchoolProfileDTO;
import com.aizhixin.cloud.dataanalysis.analysis.entity.SchoolStatistics;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

/**
 * @author: Created by jianwei.wu
 * @E-mail: wujianwei@aizhixin.com
 * @Date: 2017-11-29
 */
public interface SchoolStatisticsRespository extends JpaRepository<SchoolStatistics, String> {
    //SchoolProfileDTO(Long allStudent, Long allTeacher, Long allInstructor, Long inSchoolStudent, Long outSchoolStudent, Long readyGraduation)
    @Query("select new com.aizhixin.cloud.dataanalysis.analysis.dto.SchoolProfileDTO(sum(a.studentNumber),sum(a.teacherNumber),sum(a.InstructorNumber),0,sum(a.readyGraduation)) from #{#entityName} a where a.orgId = :orgId ")
    SchoolProfileDTO getSchoolPersonStatistics(@Param(value = "orgId")Long orgId);
}

