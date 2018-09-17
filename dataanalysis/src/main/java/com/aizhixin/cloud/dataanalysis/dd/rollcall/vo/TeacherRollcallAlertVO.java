package com.aizhixin.cloud.dataanalysis.dd.rollcall.vo;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * 老师课程预警
 */
@ApiModel
@ToString
@NoArgsConstructor
public class TeacherRollcallAlertVO {
    @ApiModelProperty(value = "日期")
    @Getter  @Setter private String day;
    @ApiModelProperty(value = "老师ID")
    @Getter  @Setter private Long teacherId;
    @ApiModelProperty(value = "老师工号")
    @Getter  @Setter private String teacherNo;
    @ApiModelProperty(value = "老师姓名")
    @Getter  @Setter private String teacherName;
    @ApiModelProperty(value = "学院")
    @Getter  @Setter private String collegeName;
    @ApiModelProperty(value = "课程到课率")
    @Getter  @Setter private String courseRollList;
    @ApiModelProperty(value = "到课率")
    @Getter  @Setter private Double dkl;

    public TeacherRollcallAlertVO(String day, Long teacherId, Double dkl) {
        this.day = day;
        this.teacherId = teacherId;
        this.dkl = dkl;
    }
}
