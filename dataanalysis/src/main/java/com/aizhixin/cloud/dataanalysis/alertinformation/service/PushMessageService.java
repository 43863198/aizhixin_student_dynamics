package com.aizhixin.cloud.dataanalysis.alertinformation.service;

import com.aizhixin.cloud.dataanalysis.alertinformation.domain.PushMessageDTO;
import com.aizhixin.cloud.dataanalysis.alertinformation.domain.PushMessageStatusDTO;

import com.aizhixin.cloud.dataanalysis.alertinformation.entity.WarningInformation;
import com.aizhixin.cloud.dataanalysis.alertinformation.repository.PushMessageQuery;
import com.aizhixin.cloud.dataanalysis.alertinformation.repository.PushMessageRepository;
import com.aizhixin.cloud.dataanalysis.common.PageDomain;
import com.aizhixin.cloud.dataanalysis.common.constant.WarningLevelConstant;
import com.aizhixin.cloud.dataanalysis.common.constant.WarningTypeChangeConstant;
import com.aizhixin.cloud.dataanalysis.common.constant.WarningTypeConstant;
import com.aizhixin.cloud.dataanalysis.common.core.ApiReturnConstants;
import com.aizhixin.cloud.dataanalysis.common.core.DataValidity;
import com.aizhixin.cloud.dataanalysis.common.core.PushMessageConstants;
import com.aizhixin.cloud.dataanalysis.common.util.DateUtil;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.collections.map.HashedMap;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.aizhixin.cloud.dataanalysis.alertinformation.entity.PushMessage;

import javax.persistence.Column;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;


/**
 * Service class for managing users.
 */
@Service
@Transactional
public class PushMessageService {

    @Autowired
    private PushMessageRepository pushMessageRepository;

    @Autowired
    private PushMessageQuery pushMessageQuery;

    public Map<String, Object> getMessageByModuleAndFunctionAndUserId(Pageable pageable,
                                                           String module, String function, Long id) {
    	Map<String, Object> r = new HashedMap();
    	PageDomain pageDomain = new PageDomain();
        Page<PushMessage> page = null;
        
        try {
            page = pushMessageRepository
                    .findAllByModuleAndFunctionAndUserIdAndDeleteFlag(module,
                            function, id, DataValidity.VALID.getIntValue(),
                            pageable);
        } catch (Exception e) {
            e.printStackTrace();
        }

        pageDomain.setPageNumber(pageable.getPageNumber());
        pageDomain.setPageSize(pageable.getPageSize());
        pageDomain.setTotalElements(page.getTotalElements());
        pageDomain.setTotalPages(page.getTotalPages());
        List<PushMessage> pmList = page.getContent();
        List results = new ArrayList();
        PushMessageDTO pmDto = null;
        for (PushMessage pushMessage : pmList) {
            pmDto = new PushMessageDTO();
            BeanUtils.copyProperties(pushMessage, pmDto);
            pmDto.setPushTime(DateUtil.format(pushMessage.getPushTime()));
            results.add(pmDto);
        }

        readMessage(module, function, id);
        r.put(ApiReturnConstants.PAGE, pageDomain);
		r.put(ApiReturnConstants.DATA, results);
		return r;
    }

    public List<PushMessageStatusDTO> getMessageStatus(String module,
                                                       String function, Long id) {
        return pushMessageQuery.queryMessageState(module, function, id);
    }

    public PushMessage createPushMessage(String businessContent,
                                         String content, String function, String module, String title,
                                         Long userId,String studentName,String warningType,String warningLevel) {
        PushMessage message = new PushMessage();
        message.setBusinessContent(businessContent);
        message.setContent(content);
        message.setFunction(function);
        message.setHaveRead(Boolean.FALSE);
        message.setModule(module);
        message.setPushTime(new Date());
        message.setDeleteFlag(DataValidity.VALID.getIntValue());
        message.setTitle(title);
        message.setUserId(userId);
        message.setStudentName(studentName);
        message.setWarningLevel(warningLevel);
        message.setWarningType(warningType);
        pushMessageRepository.save(message);
        return message;
    }
    public void createPushMessage(List<WarningInformation> warningInformationList) {
        List<PushMessage> result=new ArrayList<PushMessage>();
        for(WarningInformation warningInformation:warningInformationList){
            PushMessage message = new PushMessage();
            message.setBusinessContent(warningInformation.getId());
            message.setContent(warningInformation.getWarningCondition());
            message.setFunction(PushMessageConstants.FUNCTION_WARNING_NOTICE);
            message.setHaveRead(Boolean.FALSE);
            message.setModule(PushMessageConstants.MODULE_TASK);
            message.setPushTime(new Date());
            message.setDeleteFlag(DataValidity.VALID.getIntValue());
            message.setTitle(PushMessageConstants.MESSAGE_TITLE);
            message.setUserId(warningInformation.getDefendantId());
            message.setStudentName(warningInformation.getName());
            message.setWarningLevel(WarningLevelConstant.getName(warningInformation.getWarningLevel()));
            message.setWarningType(WarningTypeChangeConstant.valueOf(warningInformation.getWarningType()).getName());
           // message.setWarningType(WarningTypeConstant.);
            result.add(message);
        }

        pushMessageRepository.save(result);
        //return message;
    }
    public List<PushMessage> createPushMessage(String businessContent,
            String content, String function, String module, String title,
            List<Long> userIds) {
    	List<PushMessage> messageList = new ArrayList<PushMessage>();
    	for(Long userId : userIds){
			PushMessage message = new PushMessage();
			message.setBusinessContent(businessContent);
			message.setContent(content);
			message.setFunction(function);
			message.setHaveRead(Boolean.FALSE);
			message.setModule(module);
			message.setPushTime(new Date());
			message.setDeleteFlag(DataValidity.VALID.getIntValue());
			message.setTitle(title);
			message.setUserId(userId);
			messageList.add(message);
    	}
    	messageList = pushMessageRepository.save(messageList);
		return messageList;
	}

    public void readMessage(String module, String function, Long userId) {
        List<PushMessage> list = pushMessageRepository
                .findAllByModuleAndFunctionAndUserIdAndHaveRead(module,
                        function, userId, Boolean.FALSE);
        for (PushMessage pushMessage : list) {
            pushMessage.setHaveRead(Boolean.TRUE);
        }
        pushMessageRepository.save(list);
    }

    public int delete(Long id) {
        return pushMessageRepository.deleteMessage(id,
                DataValidity.INVALID.getIntValue());
    }
}
