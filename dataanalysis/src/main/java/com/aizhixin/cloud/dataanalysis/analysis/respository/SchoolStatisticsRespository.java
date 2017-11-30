package com.aizhixin.cloud.dataanalysis.analysis.respository;

import com.aizhixin.cloud.dataanalysis.alertinformation.domain.AttachmentDomain;
import com.aizhixin.cloud.dataanalysis.analysis.entity.SchoolStatistics;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

/**
 * @author: Created by jianwei.wu
 * @E-mail: wujianwei@aizhixin.com
 * @Date: 2017-11-29
 */
public interface SchoolStatisticsRespository extends JpaRepository<SchoolStatistics, String> {

    @Query("select a from #{#entityName} a where a.orgId = :orgId and a.teacherYear = :teacherYear and a.deleteFlag = :deleteFlag order by a.statisticalTime desc")
    Page<SchoolStatistics> findPageDataByOrgIdAndTeacherYear(Pageable pageable, @Param(value = "orgId") Long orgId, @Param(value = "teacherYear") String teacherYear, @Param(value = "deleteFlag") int deleteFlag);

}
