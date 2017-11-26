package com.aizhixin.cloud.dataanalysis.alertinformation.domain;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

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

    @ApiModelProperty(value = "处理结果", required = false)
    private String status;

}
