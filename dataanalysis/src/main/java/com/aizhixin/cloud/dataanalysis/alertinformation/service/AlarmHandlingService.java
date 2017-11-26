package com.aizhixin.cloud.dataanalysis.alertinformation.service;

import com.aizhixin.cloud.dataanalysis.alertinformation.domain.AttachmentDomain;
import com.aizhixin.cloud.dataanalysis.alertinformation.domain.DealResultDomain;
import com.aizhixin.cloud.dataanalysis.alertinformation.domain.SubmitDealDomain;
import com.aizhixin.cloud.dataanalysis.alertinformation.entity.AttachmentInformation;
import com.aizhixin.cloud.dataanalysis.alertinformation.entity.OperationRecord;
import com.aizhixin.cloud.dataanalysis.alertinformation.entity.WarningInformation;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @author: Created by jianwei.wu
 * @E-mail: wujianwei@aizhixin.com
 * @Date: 2017-11-25
 */
@Component
@Transactional
public class AlarmHandlingService {
    @Autowired
    private OperaionRecordService operaionRecordService;
    @Autowired
    private AttachmentInfomationService attachmentInfomationService;
    @Autowired
    private AlertWarningInformationService alertWarningInformationService;

    public Map<String, Object> addProcessing(SubmitDealDomain submitDealDomain) {
        Map<String, Object> result = new HashMap<>();
        try {
            WarningInformation warningInformation = alertWarningInformationService.getOneById(submitDealDomain.getWarningInformationId());
            OperationRecord operationRecord = new OperationRecord();
            operationRecord.setOrgId(warningInformation.getOrgId());
            operationRecord.setWarningState(30);
            operationRecord.setOperationTime(new Date());
            operationRecord.setOperationType(submitDealDomain.getDealType() + "");
            operationRecord.setProposal(submitDealDomain.getDealInfo());
            operaionRecordService.save(operationRecord);
            for (AttachmentDomain d : submitDealDomain.getAttachmentDomain()) {
                AttachmentInformation attachmentInformation = new AttachmentInformation();
                attachmentInformation.setOrgId(warningInformation.getOrgId());
                attachmentInformation.setAttachmentName(d.getFileName());
                attachmentInformation.setAttachmentPath(d.getFileUrl());
                attachmentInfomationService.save(attachmentInformation);
            }
        } catch (Exception e) {
            e.printStackTrace();
            result.put("success",false);
            result.put("message","处理信息保存异常！");
        }
        result.put("success",true);
        result.put("message","处理信息保存成功！");
        return result;
    }


    public Map<String, Object> updateProcessing(SubmitDealDomain submitDealDomain) {
        Map<String, Object> result = new HashMap<>();
        try {
            WarningInformation warningInformation = alertWarningInformationService.getOneById(submitDealDomain.getWarningInformationId());
            if(null!=submitDealDomain.getDealId()) {
                OperationRecord operationRecord = operaionRecordService.getOneById(submitDealDomain.getDealId());
                operationRecord.setOrgId(warningInformation.getOrgId());
                operationRecord.setWarningState(30);
                operationRecord.setOperationTime(new Date());
                operationRecord.setOperationType(submitDealDomain.getDealType() + "");
                operationRecord.setProposal(submitDealDomain.getDealInfo());
                operaionRecordService.save(operationRecord);
                for (AttachmentDomain d : submitDealDomain.getAttachmentDomain()) {
                    if(null!=d.getId()) {
                        AttachmentInformation attachmentInformation = attachmentInfomationService.getOneById(d.getId());
                        attachmentInformation.setOrgId(warningInformation.getOrgId());
                        attachmentInformation.setAttachmentName(d.getFileName());
                        attachmentInformation.setAttachmentPath(d.getFileUrl());
                        attachmentInfomationService.save(attachmentInformation);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            result.put("success",false);
            result.put("message","处理信息更新异常！");
        }
        result.put("success",true);
        result.put("message", "处理信息修改成功！");
        return result;
    }

    public Map<String, Object> processing(DealResultDomain dealResultDomain) {
        Map<String, Object> result = new HashMap<>();
        try {
            WarningInformation warningInformation = alertWarningInformationService.getOneById(dealResultDomain.getWarningInformationId());
            if(null!=warningInformation) {
                alertWarningInformationService.save(warningInformation);
            }
            } catch (Exception e) {
                e.printStackTrace();
                result.put("success",false);
                result.put("message","保存处理结果异常！");
            }
            result.put("success",true);
            result.put("message", "保存处理结果成功！");
            return result;
        }


}
