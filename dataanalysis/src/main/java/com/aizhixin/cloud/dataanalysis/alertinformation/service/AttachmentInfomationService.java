package com.aizhixin.cloud.dataanalysis.alertinformation.service;

import com.aizhixin.cloud.dataanalysis.alertinformation.entity.AttachmentInformation;
import com.aizhixin.cloud.dataanalysis.alertinformation.repository.AttachmentInfoRepository;
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
public class AttachmentInfomationService {
    @Autowired
    private AttachmentInfoRepository attachmentInfoRepository;


    public AttachmentInformation getOneById(String id){
        return attachmentInfoRepository.findOne(id);
    }

    public void save(AttachmentInformation attachmentInformation){
        attachmentInfoRepository.save(attachmentInformation);
    }
}
