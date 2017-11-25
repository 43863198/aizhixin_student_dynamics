package com.aizhixin.cloud.dataanalysis.alertinformation.repository;

import com.aizhixin.cloud.dataanalysis.alertinformation.entity.OperationRecord;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author: Created by jianwei.wu
 * @E-mail: wujianwei@aizhixin.com
 * @Date: 2017-11-25
 */
public interface OperationRecordRepository extends JpaRepository<OperationRecord, String> {
}
