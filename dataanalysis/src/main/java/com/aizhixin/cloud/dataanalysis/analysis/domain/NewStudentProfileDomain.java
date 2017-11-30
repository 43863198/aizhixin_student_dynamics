package com.aizhixin.cloud.dataanalysis.analysis.domain;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @author: Created by jianwei.wu
 * @E-mail: wujianwei@aizhixin.com
 * @Date: 2017-11-30
 */
@ApiModel(description="迎新学情概况统计信息")
@Data
public class NewStudentProfileDomain {
    @ApiModelProperty(value = "新生总数量", required = false)
    private int studentNumber;
    @ApiModelProperty(value = "已报到人数", required = false)
    private int alreadyReport;
    @ApiModelProperty(value = "完成缴费人数", required = false)
    private int alreadyPay;
    @ApiModelProperty(value = "走绿色通道人数", required = false)
    private int convenienceChannel;
}
