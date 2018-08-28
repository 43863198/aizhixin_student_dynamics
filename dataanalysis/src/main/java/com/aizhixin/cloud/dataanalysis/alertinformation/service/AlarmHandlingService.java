package com.aizhixin.cloud.dataanalysis.alertinformation.service;

import com.aizhixin.cloud.dataanalysis.alertinformation.domain.*;
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
            OperationRecord operationRecord = null;
            if (!StringUtils.isBlank(submitDealDomain.getDealId())) {
                operationRecord = operaionRecordService.getOneById(submitDealDomain.getDealId());
            } else {
                operationRecord = new OperationRecord();
            }
            operationRecord.setOrgId(warningInformation.getOrgId());
            operationRecord.setOperationTime(new Date());
            operationRecord.setDealType(submitDealDomain.getDealType());
            operationRecord.setProposal(submitDealDomain.getDealInfo());
            operationRecord.setWarningInformationId(submitDealDomain.getWarningInformationId());
            String id = operaionRecordService.save(operationRecord);
            for (AttachmentDomain d : submitDealDomain.getAttachmentDomain()) {
                AttachmentInformation attachmentInformation = null;
                if (null != d.getId() && !StringUtils.isBlank(d.getId())) {
                    attachmentInformation = attachmentInfomationService.getOneById(d.getId());
                } else {
                    attachmentInformation = new AttachmentInformation();
                }
                attachmentInformation.setOrgId(warningInformation.getOrgId());
                attachmentInformation.setAttachmentName(d.getFileName());
                attachmentInformation.setAttachmentPath(d.getFileUrl());
                attachmentInformation.setOperationRecordId(id);
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
            if (null != dealDomain.getWarningInformationId()) {
                WarningInformation warningInformation = alertWarningInformationService.getOneById(dealDomain.getWarningInformationId());
                warningInformation.setLastModifiedDate(new Date());
                alertWarningInformationService.save(warningInformation);
                if (null != dealDomain.getDealId() && !StringUtils.isBlank(dealDomain.getDealId())) {
                    OperationRecord operationRecord = operaionRecordService.getOneById(dealDomain.getDealId());
                    operationRecord.setOrgId(warningInformation.getOrgId());
                    operationRecord.setOperationTime(new Date());
                    operationRecord.setDealType(dealDomain.getDealType());
                    operationRecord.setProposal(dealDomain.getDealInfo());
                    operaionRecordService.save(operationRecord);
                    List<AttachmentInformation> attachmentInformations = attachmentInfomationService.getAttachmentInformationByOprId(operationRecord.getId());
                    if (null != attachmentInformations) {
                        attachmentInfomationService.deleteAttachmentInformation(attachmentInformations);
                    }
                    for (AttachmentDomain d : dealDomain.getAttachmentDomain()) {
                        AttachmentInformation attachmentInformation = new AttachmentInformation();
                        attachmentInformation.setOrgId(warningInformation.getOrgId());
                        attachmentInformation.setAttachmentName(d.getFileName());
                        attachmentInformation.setAttachmentPath(d.getFileUrl());
                        attachmentInformation.setOperationRecordId(dealDomain.getDealId());
                        attachmentInfomationService.save(attachmentInformation);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            result.put("success", false);
            result.put("message", "处理信息更新异常！");
            return result;
        }
        result.put("success", true);
        result.put("message", "处理信息修改成功！");
        return result;
    }

    public Map<String, Object> batchAllProcessing(BatchAllDealDomain domain) {
        Map<String, Object> result = new HashMap<>();
        try {
            int row = alertWarningInformationService.updateAllAlertInforPage(domain);
            List<Map<String, Object>> idList = alertWarningInformationService.queryAllAlertInforPage(domain);
            for (Map map : idList) {
                if (null != map && null != map.get("ID")) {
                    WarningInformation warningInformation = alertWarningInformationService.getOneById(map.get("ID").toString());
                    warningInformation.setLastModifiedDate(new Date());
                    alertWarningInformationService.save(warningInformation);
                    List<OperationRecord> operationRecordList = operaionRecordService.getOperationRecordByWInfoId(map.get("ID").toString());
                    for (OperationRecord or : operationRecordList) {
                        OperationRecord operationRecord = operaionRecordService.getOneById(or.getId());
                        operationRecord.setOrgId(warningInformation.getOrgId());
                        operationRecord.setOperationTime(new Date());
                        operationRecord.setDealType(domain.getDealType());
                        operationRecord.setProposal(domain.getDealInfo());
                        operaionRecordService.save(operationRecord);

                    }

                }
            }

            if (row > 0) {
                result.put("success", true);
                result.put("message", "保存处理结果成功！");
            }
        } catch (Exception e) {
            e.printStackTrace();
            result.put("success", false);
            result.put("message", "保存处理结果异常！");
        }

        return result;
    }

    public Map<String, Object> batchProcessing(BatchDealDomain batchDealDomain) {
        Map<String, Object> result = new HashMap<>();
        try {
            String[] array = batchDealDomain.getWarningInformationIds();
            for (String str : array) {
                if (null != str) {
                    WarningInformation warningInformation = alertWarningInformationService.getOneById(str);
                    warningInformation.setLastModifiedDate(new Date());
                    alertWarningInformationService.save(warningInformation);
                    if (null != batchDealDomain.getDealId() && !StringUtils.isBlank(batchDealDomain.getDealId())) {
                        OperationRecord operationRecord = operaionRecordService.getOneById(batchDealDomain.getDealId());
                        operationRecord.setOrgId(warningInformation.getOrgId());
                        operationRecord.setOperationTime(new Date());
                        operationRecord.setDealType(batchDealDomain.getDealType());
                        operationRecord.setProposal(batchDealDomain.getDealInfo());
                        operaionRecordService.save(operationRecord);
                    }
                }
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

    public Map<String, Object> processing(DealResultDomain dealResultDomain) {
        Map<String, Object> result = new HashMap<>();
        try {
            WarningInformation warningInformation = alertWarningInformationService.getOneById(dealResultDomain.getWarningInformationId());
            if (null != warningInformation) {
                warningInformation.setWarningState(dealResultDomain.getStatus());
                warningInformation.setLastModifiedDate(new Date());
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

    public List<DealDomain> getOperationRecordByWInfoId(String warningInformationId) {
        List<DealDomain> dealDomainList = new ArrayList<>();
        List<OperationRecord> operationRecordList = operaionRecordService.getOperationRecordByWInfoId(warningInformationId);
        if (null != operationRecordList && operationRecordList.size() > 0) {
            for (OperationRecord or : operationRecordList) {
                DealDomain dealDomain = new DealDomain();
                dealDomain.setWarningInformationId(warningInformationId);
                dealDomain.setDealId(or.getId());
                dealDomain.setDealType(or.getDealType());
                dealDomain.setDealInfo(or.getProposal());
                List<AttachmentDomain> attachmentDomainList = new ArrayList<>();
                List<AttachmentInformation> attachmentInformationList = attachmentInfomationService.getAttachmentInformationByOprId(or.getProcessingModeId());
                if (null != attachmentInformationList && attachmentInformationList.size() > 0) {
                    for (AttachmentInformation ai : attachmentInformationList) {
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




