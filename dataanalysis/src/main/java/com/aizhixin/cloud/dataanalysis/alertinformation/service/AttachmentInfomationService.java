package com.aizhixin.cloud.dataanalysis.alertinformation.service;

import com.aizhixin.cloud.dataanalysis.alertinformation.domain.AttachmentDomain;
import com.aizhixin.cloud.dataanalysis.alertinformation.entity.AttachmentInformation;
import com.aizhixin.cloud.dataanalysis.alertinformation.repository.AttachmentInfoRepository;
import com.aizhixin.cloud.dataanalysis.common.PageData;
import com.aizhixin.cloud.dataanalysis.common.constant.DataValidity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author: Created by jianwei.wu
 * @E-mail: wujianwei@aizhixin.com
 * @Date: 2017-11-25
 */
@Component
@Transactional
public class AttachmentInfomationService {
    @Autowired
    private AttachmentInfoRepository attachmentInfoRepository;


    public AttachmentInformation getOneById(String id){
        return attachmentInfoRepository.findOne(id);
    }

    public void save(AttachmentInformation attachmentInformation){
        attachmentInfoRepository.save(attachmentInformation);
    }

    public void deleteAttachmentInformation(String id){
        attachmentInfoRepository.delete(id);
    }

    public List<AttachmentInformation> getAttachmentInformationByOprId(String operationRecordId){
        return attachmentInfoRepository.getAttachmentInformationByOprId(operationRecordId, DataValidity.VALID.getState());
    }

    public PageData<AttachmentDomain> getAttachmentInfomationList(Pageable pageable, Long orgId){
        PageData<AttachmentDomain> p = new PageData<>();
        Page<AttachmentDomain> data =  attachmentInfoRepository.findPageDataByOrgId(pageable, orgId, DataValidity.VALID.getState());
        if(null!=data){
            p.setData(data.getContent());
            p.getPage().setTotalElements(data.getTotalElements());
        }
        p.getPage().setPageNumber(pageable.getPageNumber());
        p.getPage().setPageSize(pageable.getPageSize());
        return p;
    }

    public Map<String,Object> addAttachmentInfomation(AttachmentDomain attachmentDomain){
        Map<String,Object> result = new HashMap<>();
        try {
            AttachmentInformation attachmentInformation = new AttachmentInformation();
            attachmentInformation.setAttachmentName(attachmentDomain.getFileName());
            attachmentInformation.setAttachmentPath(attachmentDomain.getFileUrl());
            attachmentInformation.setOrgId(attachmentDomain.getOrgId());
            attachmentInformation.setUploadPeopleId(attachmentDomain.getUploadPeopleId());
            attachmentInformation.setUploadPeople(attachmentInformation.getUploadPeople());
            attachmentInfoRepository.save(attachmentInformation);

        }catch (Exception e){
            result.put("success",false);
            result.put("message","附件保存失败!");
            return result;
        }
        result.put("success",true);
        result.put("message","附件保存成功!");
        return result;
    }

    public Map<String,Object> deleteAttachmentInfomation(String id){
        Map<String,Object> result = new HashMap<>();
        try {
            attachmentInfoRepository.delete(id);
        }catch (Exception e){
            result.put("success",false);
            result.put("message","附件删除失败!");
            return result;
        }
        result.put("success",true);
        result.put("message","附件删除成功!");
        return result;
    }


}
