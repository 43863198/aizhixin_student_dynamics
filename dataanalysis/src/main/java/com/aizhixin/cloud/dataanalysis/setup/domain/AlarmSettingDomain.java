package com.aizhixin.cloud.dataanalysis.setup.domain;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @author: Created by jianwei.wu
 * @E-mail: wujianwei@aizhixin.com
 * @Date: 2017-11-27
 */
@ApiModel(description="预警设置信息")
@Data
public class AlarmSettingDomain {

    @ApiModelProperty(value = "预警设置id", required = false)
    private String alarmSettingsId;

    @ApiModelProperty(value = "机构id", required = false)
    private Long orgId;

    @ApiModelProperty(value = "规则", required = false)
    List<AlarmRuleDomain> ruleDomainList;


}
