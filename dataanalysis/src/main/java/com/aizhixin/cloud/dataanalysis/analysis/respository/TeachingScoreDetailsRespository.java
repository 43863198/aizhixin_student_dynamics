package com.aizhixin.cloud.dataanalysis.analysis.respository;

import com.aizhixin.cloud.dataanalysis.analysis.dto.TeachingScoreStatisticsDTO;
import com.aizhixin.cloud.dataanalysis.analysis.entity.SchoolStatistics;
import com.aizhixin.cloud.dataanalysis.analysis.entity.TeachingScoreDetails;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * @author: Created by jianwei.wu
 * @E-mail: wujianwei@aizhixin.com
 * @Date: 2017-12-14
 */
public interface TeachingScoreDetailsRespository extends JpaRepository<TeachingScoreDetails,String> {
    //物理删除
    @Modifying
    @Query("delete from #{#entityName} a where a.orgId = :orgId")
    void deleteByOrgId(@Param(value = "orgId") Long orgId);


}