package com.aizhixin.cloud.dataanalysis.analysis.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author: Created by jianwei.wu
 * @E-mail: wujianwei@aizhixin.com
 * @Date: 2017-12-11
 */
@ApiModel(description="学院的教学成绩")
@Data
public class CollegeTeachingAchievementDTO extends TeachingAchievementDTO{
    @ApiModelProperty(value = "名称", required = false)
    private String name;
    @ApiModelProperty(value = "代码", required = false)
    private String code;
}
