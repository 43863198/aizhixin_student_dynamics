package com.aizhixin.cloud.dataanalysis.setup.respository;


import com.aizhixin.cloud.dataanalysis.setup.entity.AlarmCreateRecord;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AlarmCreateRecordRespository extends JpaRepository<AlarmCreateRecord, String> {
    Page<AlarmCreateRecord> findByOrgIdAndCreatedIdOrderByCreatedDateDesc(Pageable p, Long orgId, Long createdId);

    Page<AlarmCreateRecord> findByOrgIdOrderByCreatedDateDesc(Pageable p, Long orgId);
}
