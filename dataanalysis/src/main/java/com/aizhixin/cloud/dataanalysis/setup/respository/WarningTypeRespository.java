package com.aizhixin.cloud.dataanalysis.setup.respository;

import com.aizhixin.cloud.dataanalysis.setup.entity.WarningType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * @author: Created by jianwei.wu
 * @E-mail: wujianwei@aizhixin.com
 * @Date: 2017-11-24
 */
public interface WarningTypeRespository extends JpaRepository<WarningType, String>  {

    @Query("select tp from #{#entityName} tp where tp.orgId = :orgId and tp.deleteFlag = :deleteFlag ")
    List<WarningType> getWarningTypeByOrgId(@Param("orgId")Long orgId, @Param("deleteFlag")int deleteFlag);


}
