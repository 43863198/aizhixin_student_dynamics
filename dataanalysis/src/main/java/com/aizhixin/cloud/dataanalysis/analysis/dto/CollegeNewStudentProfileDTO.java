package com.aizhixin.cloud.dataanalysis.analysis.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author: Created by jianwei.wu
 * @E-mail: wujianwei@aizhixin.com
 * @Date: 2017-11-30
 */
@ApiModel(description="学院新生概况信息")
@Data
public class CollegeNewStudentProfileDTO {
    @ApiModelProperty(value = "学院id", required = false)
    private Long collegeId;
    @ApiModelProperty(value = "学院名称", required = false)
    private String collegeName;
    @ApiModelProperty(value = "新生总数量", required = false)
    private int studentNumber;
    @ApiModelProperty(value = "已报到人数", required = false)
    private int alreadyReport;
    @ApiModelProperty(value = "未报到人数", required = false)
    private int unreported;
    @ApiModelProperty(value = "已报到人数占比", required = false)
    private double Proportion;
    @ApiModelProperty(value = "完成缴费人数", required = false)
    private int alreadyPay;
    @ApiModelProperty(value = "走绿色通道人数", required = false)
    private int convenienceChannel;


}
