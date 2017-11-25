package com.aizhixin.cloud.dataanalysis.alertinformation.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author: Created by jianwei.wu
 * @E-mail: wujianwei@aizhixin.com
 * @Date: 2017-11-24
 */
@ApiModel(description="预警类型")
@Data
public class WarningTypeDTO {
    @ApiModelProperty(value = "id")
    private String id;
    @ApiModelProperty(value = "机构id")
    private Long orgId;
    @ApiModelProperty(value = "告警类型")
    private String warningType;
    @ApiModelProperty(value = "告警名称")
    private String warningName;
    @ApiModelProperty(value = "开启或关闭")
    private int setupCloseFlag;
    @ApiModelProperty(value = "包含等级数")
    private int inclusionNumber;

}
