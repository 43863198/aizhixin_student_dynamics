package com.aizhixin.cloud.dataanalysis.notice.repository;


import com.aizhixin.cloud.dataanalysis.notice.entity.NotificationRecord;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface NotificationRecordRepository extends JpaRepository<NotificationRecord, String> {
    List<NotificationRecord> findByOrgIdAndReceiverCodeAndRsAndLastAccessTimeIsNull(Long orgId, String receiverCode, Integer rs);
}
