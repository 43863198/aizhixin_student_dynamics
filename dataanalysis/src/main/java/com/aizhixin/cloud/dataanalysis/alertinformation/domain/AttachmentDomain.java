package com.aizhixin.cloud.dataanalysis.alertinformation.domain;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author: Created by jianwei.wu
 * @E-mail: wujianwei@aizhixin.com
 * @Date: 2017-11-25
 */
@ApiModel(description="附件信息")
@Data
public class AttachmentDomain {

    @ApiModelProperty(value = "附件id", required = false)
    private String id;

    @ApiModelProperty(value = "机构id", required = false)
    private Long orgId;

    @ApiModelProperty(value = "附件名称", required = false)
    private String fileName;

    @ApiModelProperty(value = "附件地址", required = false)
    private String fileUrl;

    public AttachmentDomain(){
    }

    public AttachmentDomain(String id,String fileName,String fileUrl){
        this.id = id;
        this.fileName = fileName;
        this.fileUrl = fileUrl;

    }
}
