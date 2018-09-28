package com.aizhixin.cloud.dataanalysis.dd.rollcall.vo;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * 课程考勤统计
 */
@ApiModel
@ToString
@NoArgsConstructor
public class TeacherRollcallStatisticsVO extends RollcallStatisticsVO {
    @ApiModelProperty(value = "老师ID")
    @Getter  @Setter private Long teacherId;
    @ApiModelProperty(value = "老师名称")
    @Getter  @Setter private String teacherName;
    @ApiModelProperty(value = "老师工号")
    @Getter  @Setter private String teacherWorkNo;
    @ApiModelProperty(value = "学院名称")
    @Getter  @Setter private String collegeName;

    public TeacherRollcallStatisticsVO(Long teacherId, String teacherName, Integer ydrs, Integer sdrs, Integer cdrs, Integer qjrs, Integer kkrs, Integer ztrs, Double dkl) {
        super(ydrs, sdrs, cdrs, qjrs, kkrs, ztrs, dkl);
        this.teacherId = teacherId;
        this.teacherName = teacherName;
    }
}
