package com.aizhixin.cloud.dataanalysis.notice.repository;


import com.aizhixin.cloud.dataanalysis.notice.entity.NotificationRecord;
import org.springframework.data.jpa.repository.JpaRepository;


public interface NotificationRecordRepository extends JpaRepository<NotificationRecord, String> {
}
