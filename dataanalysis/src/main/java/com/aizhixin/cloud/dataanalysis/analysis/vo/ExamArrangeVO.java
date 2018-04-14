package com.aizhixin.cloud.dataanalysis.analysis.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Column;
import java.sql.Time;

/**
 * @author: Created by jianwei.wu
 * @E-mail: wujianwei@aizhixin.com
 * @Date: 2018-04-14
 */
@ApiModel(description="考试安排")
@Data
@ToString
public class ExamArrangeVO {

    @ApiModelProperty(value = "考试开始时间")
    private Time startTime;
    @ApiModelProperty(value = "考试结束时间")
    private Time endTime;
    @ApiModelProperty(value = "考试科目")
    private String Subject;
    @ApiModelProperty(value = "考场")
    private String room;


}
