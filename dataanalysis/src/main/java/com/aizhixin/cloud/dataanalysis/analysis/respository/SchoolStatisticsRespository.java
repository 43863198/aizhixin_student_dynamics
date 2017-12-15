package com.aizhixin.cloud.dataanalysis.analysis.respository;

import com.aizhixin.cloud.dataanalysis.analysis.dto.NewStudentProfileDTO;
import com.aizhixin.cloud.dataanalysis.analysis.dto.SchoolProfileDTO;
import com.aizhixin.cloud.dataanalysis.analysis.entity.SchoolStatistics;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * @author: Created by jianwei.wu
 * @E-mail: wujianwei@aizhixin.com
 * @Date: 2017-11-29
 */
public interface SchoolStatisticsRespository extends JpaRepository<SchoolStatistics, String> {
    @Query("select new com.aizhixin.cloud.dataanalysis.analysis.dto.SchoolProfileDTO(sum(a.studentNumber),sum(a.teacherNumber),sum(a.InstructorNumber),0L,sum(a.readyGraduation)) from #{#entityName} a where a.orgId = :orgId and a.deleteFlag=0 and a.teacherYear=:teacherYear")
    SchoolProfileDTO getSchoolPersonStatistics(@Param(value = "orgId")Long orgId,@Param(value = "teacherYear")String teacherYear);

    @Query("select a from #{#entityName} a where a.orgId = :orgId and a.teacherYear = :teacherYear and a.deleteFlag = :deleteFlag order by a.statisticalTime desc")
    Page<SchoolStatistics> findPageDataByOrgIdAndTeacherYear(Pageable pageable, @Param(value = "orgId") Long orgId, @Param(value = "teacherYear") String teacherYear, @Param(value = "deleteFlag") int deleteFlag);

    @Query("select new com.aizhixin.cloud.dataanalysis.analysis.dto.NewStudentProfileDTO(sum(a.newStudentsCount),sum(a.alreadyReport)) from #{#entityName} a where a.orgId = :orgId and  a.deleteFlag=0 and a.teacherYear =(select max(b.teacherYear) FROM #{#entityName} b where b.orgId = :orgId)")
    NewStudentProfileDTO getNewStudentStatistics(@Param(value = "orgId")Long orgId);

    @Query("select a from #{#entityName} a where a.orgId = :orgId and a.teacherYear = :teacherYear and a.deleteFlag = :deleteFlag order by a.statisticalTime desc")
    List<SchoolStatistics> findDataByOrgIdAndTeacherYear(@Param(value = "orgId") Long orgId, @Param(value = "teacherYear") Integer teacherYear, @Param(value = "deleteFlag") int deleteFlag);


}

