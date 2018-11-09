package com.aizhixin.cloud.dataanalysis.alertinformation.service;

import com.aizhixin.cloud.dataanalysis.alertinformation.domain.*;
import com.aizhixin.cloud.dataanalysis.alertinformation.entity.AttachmentInformation;
import com.aizhixin.cloud.dataanalysis.alertinformation.entity.OperationRecord;
import com.aizhixin.cloud.dataanalysis.alertinformation.entity.WarningInformation;
import com.aizhixin.cloud.dataanalysis.common.constant.AlertTypeConstant;
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
            result.put("dealId", id);
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
//                    operationRecord.setOrgId(warningInformation.getOrgId());
                    operationRecord.setOperationTime(new Date());
//                    operationRecord.setDealType(dealDomain.getDealType());
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
                    result.put("dealId", dealDomain.getDealId());
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
                    if (warningInformation.getWarningState() != 20) {
                        Map<String, String> maps = domain.getDealTypes();
                        if (null != maps) {
                            for (Map.Entry<String, String> e : maps.entrySet()) {
                                operaionRecordService.deleteByWarningInformationIdAndDealType(map.get("ID").toString(), Integer.parseInt(e.getKey()));
                                OperationRecord operationRecord1 = new OperationRecord();
                                operationRecord1.setWarningInformationId(map.get("ID").toString());
                                operationRecord1.setOrgId(warningInformation.getOrgId());
                                operationRecord1.setOperationTime(new Date());
                                operationRecord1.setDealType(Integer.parseInt(e.getKey()));
                                operationRecord1.setProposal(e.getValue());
                                operaionRecordService.save(operationRecord1);
                            }
                        }
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
            String[] idArray = batchDealDomain.getWarningInformationIds();
            for (String id : idArray) {
                WarningInformation warningInformation = alertWarningInformationService.getOneById(id);
                if (warningInformation.getWarningState() != 20) {
                    warningInformation.setLastModifiedDate(new Date());
                    warningInformation.setWarningState(AlertTypeConstant.ALERT_PROCESSED);
                    alertWarningInformationService.save(warningInformation);
                    Map<String, String> map = batchDealDomain.getDealTypes();
                    if (null != map) {
                        for (Map.Entry<String, String> e : map.entrySet()) {
                            //删除历史的处理意见
                            operaionRecordService.deleteByWarningInformationIdAndDealType(id, Integer.parseInt(e.getKey()));
                            OperationRecord operationRecord1 = new OperationRecord();
                            operationRecord1.setWarningInformationId(id);
                            operationRecord1.setOrgId(warningInformation.getOrgId());
                            operationRecord1.setOperationTime(new Date());
                            operationRecord1.setDealType(Integer.parseInt(e.getKey()));
                            operationRecord1.setProposal(e.getValue());
                            operaionRecordService.save(operationRecord1);
                        }
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
//            OperationRecord operationRecord = null;
            if (null != warningInformation) {
                warningInformation.setWarningState(dealResultDomain.getStatus());
                warningInformation.setLastModifiedDate(new Date());
                if (40 == dealResultDomain.getStatus()) {
                    warningInformation.setCancelComments(dealResultDomain.getCancelComments());
                }
                alertWarningInformationService.save(warningInformation);
            }
//            if (!StringUtils.isBlank(dealResultDomain.getDealId())) {
//                operationRecord = operaionRecordService.getOneById(dealResultDomain.getDealId());
//            }
            Map<String, Object> maps = dealResultDomain.getDealTypes();
            if (null != maps) {
                for (Map.Entry<String, Object> e : maps.entrySet()) {
//                    operaionRecordService.deleteByWarningInformationIdAndDealType(dealResultDomain.getWarningInformationId(), Integer.parseInt(e.getKey()));

                    Map map = (Map) e.getValue();
                    OperationRecord operationRecord = null;
                    if (null != map.get("dealId")) {
                        operationRecord = operaionRecordService.getOneById(map.get("dealId").toString());
                    }
                    if (null == operationRecord) {
                        operationRecord = new OperationRecord();
                        operationRecord.setOrgId(warningInformation.getOrgId());
                        operationRecord.setWarningInformationId(dealResultDomain.getWarningInformationId());
                        operationRecord.setDealType(Integer.parseInt(e.getKey()));
                    }
                    operationRecord.setOperationTime(new Date());
                    operationRecord.setProposal(map.get("dealInfo").toString());

                    String id = operaionRecordService.save(operationRecord);

                    //附件直接使用替换的方式
                    List<AttachmentInformation> attachmentInformations = attachmentInfomationService.getAttachmentInformationByOprId(id);
                    if (null != attachmentInformations) {
                        attachmentInfomationService.deleteAttachmentInformation(attachmentInformations);
                    }
                    for (AttachmentDomain d : (List<AttachmentDomain>) map.get("attachmentDomain")) {
                        AttachmentInformation attachmentInformation = new AttachmentInformation();
                        attachmentInformation.setOrgId(warningInformation.getOrgId());
                        attachmentInformation.setAttachmentName(d.getFileName());
                        attachmentInformation.setAttachmentPath(d.getFileUrl());
                        attachmentInformation.setOperationRecordId(id);
                        attachmentInfomationService.save(attachmentInformation);
                    }
                }
            }
//            else {
//                operationRecord.setOrgId(warningInformation.getOrgId());
//                operationRecord.setOperationTime(new Date());
//                operationRecord.setWarningInformationId(dealResultDomain.getWarningInformationId());
//            }
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




