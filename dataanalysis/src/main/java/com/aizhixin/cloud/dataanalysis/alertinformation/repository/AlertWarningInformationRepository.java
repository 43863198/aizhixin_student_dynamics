package com.aizhixin.cloud.dataanalysis.alertinformation.repository;

import com.aizhixin.cloud.dataanalysis.alertinformation.entity.AlertWarningInformation;
import com.aizhixin.cloud.dataanalysis.setup.entity.AlarmSettings;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * @author: Created by jianwei.wu
 * @E-mail: wujianwei@aizhixin.com
 * @Date: 2017-11-14
 */
public interface AlertWarningInformationRepository extends JpaRepository<AlertWarningInformation, String> {

    @org.springframework.data.jpa.repository.Query("select aw from #{#entityName} aw where aw.deleteFlag = :deleteFlag and aw.warningType = :warningType and aw.orgId = :orgId and aw.defendantId = :defendantId")
    List<AlertWarningInformation> getawinfoByDefendantId(@Param("orgId")Long orgId, @Param("warningType")String warningType, @Param("defendantId")Long defendantId, @Param("deleteFlag")int deleteFlag);

    @org.springframework.data.jpa.repository.Query("select aw from #{#entityName} aw where aw.deleteFlag = :deleteFlag and aw.warningType = :warningType and aw.orgId = :orgId")
    List<AlertWarningInformation> getawinfoByOrgIdAndWarningType(@Param("orgId")Long orgId, @Param("warningType")String warningType, @Param("deleteFlag")int deleteFlag);


}
