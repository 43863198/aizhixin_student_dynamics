package com.aizhixin.cloud.dataanalysis.alertinformation.service;

import com.aizhixin.cloud.dataanalysis.alertinformation.entity.OperationRecord;
import com.aizhixin.cloud.dataanalysis.alertinformation.repository.OperationRecordRepository;
import com.aizhixin.cloud.dataanalysis.common.constant.DataValidity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

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

    public String save(OperationRecord operationRecord){
         return operationRecordRepository.save(operationRecord).getId();
    }

    public List<OperationRecord> getOperationRecordByWInfoId(String warningInformationId){
        return operationRecordRepository.getOperationRecordByWInfoId(warningInformationId, DataValidity.VALID.getState());
    }





}
