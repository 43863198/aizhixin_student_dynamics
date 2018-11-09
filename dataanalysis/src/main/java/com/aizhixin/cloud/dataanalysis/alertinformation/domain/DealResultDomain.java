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
@ApiModel(description="处理结果信息")
@Data
public class DealResultDomain {

    @ApiModelProperty(value = "预警信息id", required = false)
    private String warningInformationId;

    @ApiModelProperty(value = "状态")
    private int status;

    @ApiModelProperty(value = "撤销意见，或者学院处理意见")
    private String comments;

    @ApiModelProperty(value = "撤销意见，仅在撤销操作时有用")
    private List<AttachmentDomain> files;
}
