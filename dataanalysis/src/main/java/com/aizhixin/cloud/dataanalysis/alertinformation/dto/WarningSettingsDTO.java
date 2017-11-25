package com.aizhixin.cloud.dataanalysis.alertinformation.dto;

import com.aizhixin.cloud.dataanalysis.setup.entity.AlarmRule;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @author: Created by jianwei.wu
 * @E-mail: wujianwei@aizhixin.com
 * @Date: 2017-11-24
 */
@ApiModel(description="预警设置")
@Data
public class WarningSettingsDTO {
    @ApiModelProperty(value = "id")
    private String id;
    @ApiModelProperty(value = "告警类型id")
    private String warningTypeId;
    @ApiModelProperty(value = "告警名称")
    private String warningName;
    @ApiModelProperty(value = "开启或关闭")
    private int setupCloseFlag;
    @ApiModelProperty(value = "包含的等级")
    private List<WarningGradeDTO> gradeDTOList;

}
