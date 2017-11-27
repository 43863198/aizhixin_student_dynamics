package com.aizhixin.cloud.dataanalysis.alertinformation.domain;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @author: Created by jianwei.wu
 * @E-mail: wujianwei@aizhixin.com
 * @Date: 2017-11-25
 */
@ApiModel(description="处理预警信息")
@Data
public class DealDomain {

    @ApiModelProperty(value = "预警信息id", required = false)
    private String warningInformationId;

    @ApiModelProperty(value = "处理操作id", required = false)
    private String dealId;

    @ApiModelProperty(value = "处理信息", required = false)
    private String dealInfo;

    @ApiModelProperty(value = "处理类型 辅导员处理10 学院处理 20", required = false)
    private int dealType;

    @ApiModelProperty(value = "附件信息", required = false)
    private List<AttachmentDomain> attachmentDomain;




}
