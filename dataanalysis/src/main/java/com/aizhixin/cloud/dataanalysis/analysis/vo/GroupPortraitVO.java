package com.aizhixin.cloud.dataanalysis.analysis.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.ToString;

import java.util.List;

/**
 * @author: Created by jianwei.wu
 * @E-mail: wujianwei@aizhixin.com
 * @Date: 2018-05-12
 */
@ApiModel(description="群体画像信息")
@Data
@ToString
public class GroupPortraitVO {
    @ApiModelProperty(value = "性别分布")
    private List<ValueVo> sexList;
    @ApiModelProperty(value = "年龄分布")
    private List<ValueVo> ageList;

}
