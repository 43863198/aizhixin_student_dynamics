package com.aizhixin.cloud.dataanalysis.analysis.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.ToString;

@ApiModel(description="辅修、二学位数据统计")
@Data
@ToString
public class MinorSecondDegreeVO {
    @ApiModelProperty(value = "部门名称")
    private String bmmc;
    @ApiModelProperty(value = "本部门辅修人数")
    private int bmfxs;
    @ApiModelProperty(value = "本部门二学位人数")
    private int bmexws;
    @ApiModelProperty(value = "外部门辅修本部门人数")
    private int wbmfxs;
    @ApiModelProperty(value = "外部门修本部门二学位人数")
    private int wbmexws;

}
