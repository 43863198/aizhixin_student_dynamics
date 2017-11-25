package com.aizhixin.cloud.dataanalysis.alertinformation.service;

import com.aizhixin.cloud.dataanalysis.alertinformation.entity.OperationRecord;
import com.aizhixin.cloud.dataanalysis.alertinformation.repository.OperationRecordRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author: Created by jianwei.wu
 * @E-mail: wujianwei@aizhixin.com
 * @Date: 2017-11-25
 */
@Component
@Transactional
public class OperaionRecordService {
    @Autowired
    private OperationRecordRepository operationRecordRepository;


    public OperationRecord getOneById(String id){
        return operationRecordRepository.findOne(id);
    }

    public void save(OperationRecord operationRecord){
         operationRecordRepository.save(operationRecord);
    }

}
