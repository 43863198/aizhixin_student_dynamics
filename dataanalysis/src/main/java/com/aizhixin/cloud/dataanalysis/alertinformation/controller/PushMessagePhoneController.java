package com.aizhixin.cloud.dataanalysis.alertinformation.controller;

import com.aizhixin.cloud.dataanalysis.alertinformation.domain.PushMessageStatusDTO;
import com.aizhixin.cloud.dataanalysis.alertinformation.service.PushMessageService;
import com.aizhixin.cloud.dataanalysis.common.core.ApiReturnConstants;
import com.aizhixin.cloud.dataanalysis.common.core.PageUtil;
import com.aizhixin.cloud.dataanalysis.common.core.PublicErrorCode;
import com.aizhixin.cloud.dataanalysis.common.core.PushMessageConstants;
import com.aizhixin.cloud.dataanalysis.common.domain.AccountDTO;
import com.aizhixin.cloud.dataanalysis.common.service.AuthUtilService;
import com.aizhixin.cloud.dataanalysis.common.util.DateUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;


@RestController
@RequestMapping("/v1/message")
@Api(value = "消息API", description = "针对消息操作API")
public class PushMessagePhoneController {

    private final Logger log = LoggerFactory.getLogger(PushMessagePhoneController.class);

    @Autowired
    private PushMessageService pushMessageService;
	@Autowired
	private AuthUtilService authUtilService;

    /**
     * 用户获取推送的消息
     *
     * @param module
     * @param function
     * @param offset
     * @param limit
     * @param accessToken
     * @return
     */
    @RequestMapping(value = "/get", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "用户获取推送的消息,并将其置为已读", httpMethod = "GET", response = Void.class, notes = "用户获取推送的消息<br><br> task 实践任务<br> student_remind 学生提醒(rollcall) teacher_notice 上课老师请假通知(leave)  teacher_approval 请假审批通知 (leave) student_notice 请假审批通知(leave) 预警（warning）<br><b>@author 王俊</b>")
    public ResponseEntity<?> get(
            @ApiParam(value = "模块 必填",required = true) @RequestParam(value = "module", required = true) String module,
            @ApiParam(value = "方法 必填",required = true) @RequestParam(value = "function", required = true) String function,
            @ApiParam(value = "用户Id",required = true) @RequestParam(value = "id" ,required = true) Long id,
            @ApiParam(value = "offset 起始页") @RequestParam(value = "offset", required = false) Integer offset,
            @ApiParam(value = "limit 每页的限制数目") @RequestParam(value = "limit", required = false) Integer limit,
            @RequestHeader("Authorization") String accessToken) {

    	Map<String, Object> result = new HashMap<String, Object>();
//		AccountDTO dto = authUtilService.getSsoUserInfo(accessToken);
//		if (null == dto || null == dto.getId()) {
//			result.put(ApiReturnConstants.RESULT, Boolean.FALSE);
//			result.put(ApiReturnConstants.CODE,
//					PublicErrorCode.AUTH_EXCEPTION.getIntValue());
//			result.put(ApiReturnConstants.CAUSE, "未授权");
//			return new ResponseEntity<Map<String, Object>>(result,
//					HttpStatus.UNAUTHORIZED);
//		}

        Sort sort = new Sort(Sort.Direction.DESC, "lastModifiedDate");
        result = pushMessageService
                .getMessageByModuleAndFunctionAndUserId(
                        PageUtil.createNoErrorPageRequestAndSort(offset, limit, sort),
                        module, function, id);
        return new ResponseEntity<Object>(result, HttpStatus.OK);
    }

    /**
     * 用户获取推送的消息的状态
     *
     * @param module
     * @param function
     * @param accessToken
     * @return
     */
    @RequestMapping(value = "/getstatus", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "用户获取推送的消息", httpMethod = "GET", response = Void.class, notes = "用户获取推送的消息<br><br><b>@author 杨立强</b>")
    public ResponseEntity<?> getstatus(
            @ApiParam(value = "模块 选填" ,required = true) @RequestParam(value = "module" ,required = true) String module,
            @ApiParam(value = "方法 选填" ,required = true) @RequestParam(value = "function" ,required = true) String function,
            @ApiParam(value = "用户Id",required = true) @RequestParam(value = "function" ,required = true) Long id,
            @RequestHeader("Authorization") String accessToken) {

    	Map<String, Object> result = new HashMap<String, Object>();
//		AccountDTO dto = authUtilService.getSsoUserInfo(accessToken);
//		if (null == dto || null == dto.getId()) {
//			result.put(ApiReturnConstants.RESULT, Boolean.FALSE);
//			result.put(ApiReturnConstants.CODE,
//					PublicErrorCode.AUTH_EXCEPTION.getIntValue());
//			result.put(ApiReturnConstants.CAUSE, "未授权");
//			return new ResponseEntity<Map<String, Object>>(result,
//					HttpStatus.UNAUTHORIZED);
//		}

		 List<PushMessageStatusDTO> list = new ArrayList<PushMessageStatusDTO>();
         list = pushMessageService
                .getMessageStatus(module, function, id);

//        if (list.size() == 0) {
//        	list.add(getDefaultData(dto.getRoleNames()));
//        }
        result.put(ApiReturnConstants.DATA, list);
        return new ResponseEntity<Object>(result, HttpStatus.OK);
    }

    public PushMessageStatusDTO getDefaultData(List<String> roleNames) {
        PushMessageStatusDTO pmDto = new PushMessageStatusDTO();
        pmDto.setModule(PushMessageConstants.MODULE_TASK);
//        if (roleNames.contains(RoleCode.ROLE_STUDENT)) {
//            pmDto.setFunction(PushMessageConstants.FUNCTION_STUDENT_NOTICE);
//        } else {
//            pmDto.setFunction(PushMessageConstants.FUNCTION_MENTOR_NOTICE);
//        }
        pmDto.setLastPushTime(DateUtil.format(new Date()));
        pmDto.setPushCount(0);
        pmDto.setNotRead(0);
        return pmDto;
    }

    /**
     * 删除消息
     *
     * @param accessToken
     * @param id
     * @return
     */
    @RequestMapping(value = "/delete", method = RequestMethod.DELETE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "删除消息", httpMethod = "DELETE", response = Void.class, notes = "删除消息")
    public ResponseEntity<?> deleteMessage(
            @RequestHeader("Authorization") String accessToken,
            @ApiParam(value = "id 消息id 必填") @RequestParam(value = "id", required = true) Long id) {
    	Map<String, Object> result = new HashMap<String, Object>();
		AccountDTO dto = authUtilService.getSsoUserInfo(accessToken);
		if (null == dto || null == dto.getId()) {
			result.put(ApiReturnConstants.RESULT, Boolean.FALSE);
			result.put(ApiReturnConstants.CODE,
					PublicErrorCode.AUTH_EXCEPTION.getIntValue());
			result.put(ApiReturnConstants.CAUSE, "未授权");
			return new ResponseEntity<Map<String, Object>>(result,
					HttpStatus.UNAUTHORIZED);
		}
            pushMessageService.delete(id);
            return new ResponseEntity<>(HttpStatus.OK);
    }
}
