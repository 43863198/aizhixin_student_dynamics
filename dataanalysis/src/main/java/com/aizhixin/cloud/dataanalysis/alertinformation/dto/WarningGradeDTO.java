package com.aizhixin.cloud.dataanalysis.alertinformation.dto;

import com.aizhixin.cloud.dataanalysis.setup.entity.AlarmRule;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @author: Created by jianwei.wu
 * @E-mail: wujianwei@aizhixin.com
 * @Date: 2017-11-24
 */
@ApiModel(description="预警等级")
@Data
public class WarningGradeDTO {
    @ApiModelProperty(value = "等级")
    private int grade;
    @ApiModelProperty(value = "等级名称")
    private String name;
    @ApiModelProperty(value = "开启状态(10:启用 ;20:关闭；")
    private int setupCloseFlag;
    @ApiModelProperty(value = "规则描述")
    List<String> describeList;
    @ApiModelProperty(value = "预警规则")
    List<AlarmRule> ruleList;
}
