package com.aizhixin.cloud.dataanalysis.alertinformation.domain;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Map;

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

    @ApiModelProperty(value = "处理操作id", required = false)
    private String dealId;
    @ApiModelProperty(value = "状态", required = false)
    private int status;

    @ApiModelProperty(value = "处理类型（辅导员处理10 学院处理 20）和处理信息（处理建议、附件） ", required = false)
    private Map<String,Object> dealTypes;
    

}
