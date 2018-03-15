package com.aizhixin.cloud.dataanalysis.setup.respository;

import com.aizhixin.cloud.dataanalysis.setup.domain.WarningTypeDomain;
import com.aizhixin.cloud.dataanalysis.setup.entity.WarningType;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Set;

/**
 * @author: Created by jianwei.wu
 * @E-mail: wujianwei@aizhixin.com
 * @Date: 2017-11-24
 */
public interface WarningTypeRespository extends JpaRepository<WarningType, String>  {

    @Query("select tp from #{#entityName} tp where tp.deleteFlag = :deleteFlag ")
    List<WarningType> getAllWarningType( @Param("deleteFlag")int deleteFlag);


    @Query("select tp from #{#entityName} tp where tp.orgId = :orgId and tp.deleteFlag = :deleteFlag ")
    List<WarningType> getWarningTypeByOrgId(@Param("orgId")Long orgId, @Param("deleteFlag")int deleteFlag);

    @Query("select tp from #{#entityName} tp where tp.warningType in :typeList and tp.deleteFlag = :deleteFlag ")
    List<WarningType> getWarningTypeByTypeList(@Param("typeList")Set<String> typeList, @Param("deleteFlag")int deleteFlag);

    @Query("select distinct new com.aizhixin.cloud.dataanalysis.setup.domain.WarningTypeDomain(wt.orgId)  from com.aizhixin.cloud.dataanalysis.setup.entity.WarningType wt" ) 
    List<WarningTypeDomain> getAllOrgId();

    @Modifying
    @Query("delete from #{#entityName} tp where tp.orgId = :orgId ")
    int deleteByOrgId(@Param("orgId")Long orgId);

    WarningType findOneByOrgIdAndWarningType(Long orgId, String warningType);

    List<WarningType> findByOrgIdAndDeleteFlag(Long orgId,Integer deleteFlag);

}
