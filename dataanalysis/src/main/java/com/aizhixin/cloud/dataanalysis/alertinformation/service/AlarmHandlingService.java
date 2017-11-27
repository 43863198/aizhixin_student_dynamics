package com.aizhixin.cloud.dataanalysis.alertinformation.service;

import com.aizhixin.cloud.dataanalysis.alertinformation.domain.AttachmentDomain;
import com.aizhixin.cloud.dataanalysis.alertinformation.domain.DealDomain;
import com.aizhixin.cloud.dataanalysis.alertinformation.domain.DealResultDomain;
import com.aizhixin.cloud.dataanalysis.alertinformation.entity.AttachmentInformation;
import com.aizhixin.cloud.dataanalysis.alertinformation.entity.OperationRecord;
import com.aizhixin.cloud.dataanalysis.alertinformation.entity.WarningInformation;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

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

    public Map<String, Object> addProcessing(DealDomain submitDealDomain) {
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
            result.put("success", false);
            result.put("message", "处理信息保存异常！");
        }
        result.put("success", true);
        result.put("message", "处理信息保存成功！");
        return result;
    }


    public Map<String, Object> updateProcessing(DealDomain dealDomain) {
        Map<String, Object> result = new HashMap<>();
        try {
            WarningInformation warningInformation = alertWarningInformationService.getOneById(dealDomain.getWarningInformationId());
            if (null != dealDomain.getDealId()) {
                OperationRecord operationRecord = operaionRecordService.getOneById(dealDomain.getDealId());
                operationRecord.setOrgId(warningInformation.getOrgId());
                operationRecord.setWarningState(30);
                operationRecord.setOperationTime(new Date());
                operationRecord.setOperationType(dealDomain.getDealType() + "");
                operationRecord.setProposal(dealDomain.getDealInfo());
                operaionRecordService.save(operationRecord);
                for (AttachmentDomain d : dealDomain.getAttachmentDomain()) {
                    if (null != d.getId()) {
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
            result.put("success", false);
            result.put("message", "处理信息更新异常！");
        }
        result.put("success", true);
        result.put("message", "处理信息修改成功！");
        return result;
    }

    public Map<String, Object> processing(DealResultDomain dealResultDomain) {
        Map<String, Object> result = new HashMap<>();
        try {
            WarningInformation warningInformation = alertWarningInformationService.getOneById(dealResultDomain.getWarningInformationId());
            if (null != warningInformation) {
                alertWarningInformationService.save(warningInformation);
            }
        } catch (Exception e) {
            e.printStackTrace();
            result.put("success", false);
            result.put("message", "保存处理结果异常！");
        }
        result.put("success", true);
        result.put("message", "保存处理结果成功！");
        return result;
    }

    public List<DealDomain>  getOperationRecordByWInfoId(String warningInformationId){
        List<DealDomain> dealDomainList = new ArrayList<>();
        List<OperationRecord> operationRecordList = operaionRecordService.getOperationRecordByWInfoId(warningInformationId);
        if(null!=operationRecordList&&operationRecordList.size()>0){
            for(OperationRecord or : operationRecordList){
                DealDomain  dealDomain = new DealDomain();
                dealDomain.setWarningInformationId(warningInformationId);
                dealDomain.setDealId(or.getId());
                dealDomain.setDealType(or.getDealType());
                dealDomain.setDealInfo(or.getProposal());
                List<AttachmentDomain> attachmentDomainList = new ArrayList<>();
                List<AttachmentInformation> attachmentInformationList = attachmentInfomationService.getAttachmentInformationByOprId(or.getProcessingModeId());
                if(null!=attachmentInformationList&&attachmentInformationList.size()>0){
                    for(AttachmentInformation ai : attachmentInformationList){
                        AttachmentDomain attachmentDomain = new AttachmentDomain();
                        attachmentDomain.setFileName(ai.getAttachmentName());
                        attachmentDomain.setId(ai.getId());
                        attachmentDomain.setFileUrl(ai.getAttachmentPath());
                        attachmentDomainList.add(attachmentDomain);
                    }
                }
                dealDomain.setAttachmentDomain(attachmentDomainList);
                dealDomainList.add(dealDomain);
            }
        }
        return dealDomainList;
    }

}




