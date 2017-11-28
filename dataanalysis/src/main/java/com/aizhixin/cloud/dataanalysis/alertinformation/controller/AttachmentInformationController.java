package com.aizhixin.cloud.dataanalysis.alertinformation.controller;

import com.aizhixin.cloud.dataanalysis.alertinformation.domain.AttachmentDomain;
import com.aizhixin.cloud.dataanalysis.alertinformation.domain.DealDomain;
import com.aizhixin.cloud.dataanalysis.alertinformation.dto.WarningDetailsDTO;
import com.aizhixin.cloud.dataanalysis.alertinformation.service.AttachmentInfomationService;
import com.aizhixin.cloud.dataanalysis.common.PageData;
import com.aizhixin.cloud.dataanalysis.common.core.PageUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * @author: Created by jianwei.wu
 * @E-mail: wujianwei@aizhixin.com
 * @Date: 2017-11-28
 */
@RestController
@RequestMapping("/v1/attachmentinformation")
@Api(description = "附件信息API")
public class AttachmentInformationController {
    @Autowired
    private AttachmentInfomationService attachmentInfomationService;


    @GetMapping(value = "/getlist", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(httpMethod = "GET", value = "附件信息列表", response = Void.class, notes = "附件信息列表<br><br><b>@author jianwei.wu</b>")
    public Page<AttachmentDomain> getAttachmentInfomationList(
            @ApiParam(value = "orgId 机构id" , required = true) @RequestParam(value = "orgId", required = true) Long orgId,
            @ApiParam(value = "pageNumber 第几页") @RequestParam(value = "pageNumber", required = false) Integer pageNumber,
            @ApiParam(value = "pageSize 每页数据的数目") @RequestParam(value = "pageSize", required = false) Integer pageSize) {
        return attachmentInfomationService.getAttachmentInfomationList(PageUtil.createNoErrorPageRequest(pageNumber, pageSize),orgId);
    }

    @PostMapping(value = "/add", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(httpMethod = "POST", value = "添加附件信息", response = Void.class, notes = "添加附件信息<br><br><b>@author jianwei.wu</b>")
    public Map<String,Object> addAttachmentInfomation(
            @ApiParam(value = "<b>必填:、</b><br>orgId:机构id<br><b>" +
                    "</b><br>fileName:文件名称;" +
                    "<br>fileUrl:文件地址;<br>")
            @RequestBody AttachmentDomain attachmentDomain) {
        return attachmentInfomationService.addAttachmentInfomation(attachmentDomain);
    }

    @PostMapping(value = "/delete", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(httpMethod = "PUT", value = "删除附件信息", response = Void.class, notes = "删除附件信息<br><br><b>@author jianwei.wu</b>")
    public Map<String,Object> deleteAttachmentInfomation(
            @ApiParam(value = "id 附件id" , required = true) @RequestParam(value = "id", required = true) String id) {
        return attachmentInfomationService.deleteAttachmentInfomation(id);
    }

}
