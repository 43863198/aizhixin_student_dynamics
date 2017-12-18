package com.aizhixin.cloud.dataanalysis.analysis.respository;

import com.aizhixin.cloud.dataanalysis.analysis.dto.PracticeStaticsDTO;
import com.aizhixin.cloud.dataanalysis.analysis.entity.PracticeStatistics;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Map;

/**
 * @author: Created by jun.wang
 * @E-mail: wangjun@aizhixin.com
 * @Date: 2017-11-30
 */
public interface PracticeStaticsRespository extends JpaRepository<PracticeStatistics, String> {
    @Query("select new com.aizhixin.cloud.dataanalysis.analysis.dto.PracticeStaticsDTO(sum(a.practiceStudentNum),sum(a.practiceCompanyNum),sum(a.taskNum),sum(a.taskPassNum)) from #{#entityName} a where a.orgId = :orgId and a.deleteFlag=0 and a.teacherYear=:teacherYear and a.semester=:semester")
    PracticeStaticsDTO getPracticeStatics(@Param(value = "orgId")Long orgId,@Param(value = "teacherYear")int teacherYear,@Param(value = "semester")int semester);
    @Query("select a from #{#entityName} a where a.orgId = :orgId and a.teacherYear = :teacherYear and a.deleteFlag = :deleteFlag order by a.statisticalTime desc")
    Page<PracticeStatistics>  findPageDataByOrgIdAndTeacherYear(Pageable pageable, @Param(value = "orgId") Long orgId, @Param(value = "teacherYear") Integer teacherYear, @Param(value = "deleteFlag") int deleteFlag);

}
