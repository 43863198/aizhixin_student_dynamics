package com.aizhixin.cloud.dataanalysis.alertinformation.repository;

import com.aizhixin.cloud.dataanalysis.alertinformation.entity.OperationRecord;
import com.aizhixin.cloud.dataanalysis.alertinformation.entity.WarningInformation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * @author: Created by jianwei.wu
 * @E-mail: wujianwei@aizhixin.com
 * @Date: 2017-11-25
 */
public interface OperationRecordRepository extends JpaRepository<OperationRecord, String> {

    @Query("select ori from #{#entityName} ori where ori.warningInformationId = :warningInformationId and ori.deleteFlag = :deleteFlag")
    List<OperationRecord> getOperationRecordByWInfoId(@Param("warningInformationId")String warningInformationId, @Param("deleteFlag")int deleteFlag);
    void deleteByOrgId(Long orgId);
}
