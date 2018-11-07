package com.aizhixin.cloud.dataanalysis.dd.rollcall.dto;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@ApiModel
@ToString
@NoArgsConstructor
public class TeacherForRollcallDTO {
    @ApiModelProperty(value = "老师ID")
    @Getter @Setter private Long teacherId;
    @ApiModelProperty(value = "老师名称")
    @Getter  @Setter private String teacherName;
    @ApiModelProperty(value = "老师工号")
    @Getter  @Setter private String teacherWorkNo;
    @ApiModelProperty(value = "学院名称")
    @Getter  @Setter private String collegeName;
    @ApiModelProperty(value = "班级id列表")
    @Getter  @Setter private String classes;

    public TeacherForRollcallDTO (Long teacherId, String teacherName, String teacherWorkNo, String collegeName, String classes) {
        this.teacherId = teacherId;
        this.teacherName = teacherName;
        this.teacherWorkNo = teacherWorkNo;
        this.collegeName = collegeName;
        this.classes = classes;
    }
}
