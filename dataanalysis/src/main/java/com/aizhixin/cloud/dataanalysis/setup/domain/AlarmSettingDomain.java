package com.aizhixin.cloud.dataanalysis.setup.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.Column;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.util.Date;
import java.util.List;

/**
 * @author: Created by jianwei.wu
 * @E-mail: wujianwei@aizhixin.com
 * @Date: 2017-11-27
 */
@ApiModel(description="预警设置信息")
@Data
public class AlarmSettingDomain {

    @ApiModelProperty(value = "预警类型id", required = false)
    private String warningTypeId;

    @ApiModelProperty(value = "开启或关闭")
    private int setupCloseFlag;

    @ApiModelProperty(value = "设置等级规则信息", required = false)
    List<WarningGradeDomain> warningGradeDomainList;


}
