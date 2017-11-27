package com.aizhixin.cloud.dataanalysis.alertinformation.repository;

import com.aizhixin.cloud.dataanalysis.alertinformation.entity.AttachmentInformation;
import com.aizhixin.cloud.dataanalysis.alertinformation.entity.OperationRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * @author: Created by jianwei.wu
 * @E-mail: wujianwei@aizhixin.com
 * @Date: 2017-11-25
 */
public interface AttachmentInfoRepository extends JpaRepository<AttachmentInformation, String> {

    @Query("select ai from #{#entityName} ai where ai.operationRecordId = :operationRecordId and ai.deleteFlag = :deleteFlag")
    List<AttachmentInformation> getAttachmentInformationByOprId(@Param("operationRecordId")String operationRecordId, @Param("deleteFlag")int deleteFlag);



}
