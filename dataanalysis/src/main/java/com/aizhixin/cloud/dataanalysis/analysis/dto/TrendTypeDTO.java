package com.aizhixin.cloud.dataanalysis.analysis.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author: Created by jianwei.wu
 * @E-mail: wujianwei@aizhixin.com
 * @Date: 2017-12-01
 */
@ApiModel(description="指标类型")
@Data
public class TrendTypeDTO {
    private int index;
    private String tyep;
    private String typeName;
}
