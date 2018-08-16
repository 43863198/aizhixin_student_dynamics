package com.aizhixin.cloud.dataanalysis.zb.app.vo;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@ApiModel(description="学生学期成绩指标")
@NoArgsConstructor
@ToString
public class StudentSemesterScoreIndexVO {
    @ApiModelProperty(value = "YXSH院系所号")
    @Getter @Setter private String yxsh;
    @ApiModelProperty(value = "院系所名称")
    @Getter @Setter private String yxsmc;
    @ApiModelProperty(value = "ZYH专业号")
    @Getter @Setter private String zyh;
    @ApiModelProperty(value = "专业名称")
    @Getter @Setter private String zymc;
    @ApiModelProperty(value = "班级")
    @Getter @Setter private String bjmc;
    @ApiModelProperty(value = "XH学号")
    @Getter @Setter private String xh;
    @ApiModelProperty(value = "XM姓名")
    @Getter @Setter private String xm;
    @ApiModelProperty(value = "年级")
    @Getter @Setter private String nj;
    @ApiModelProperty(value = "GPA平均学分绩点")
    @Getter @Setter private Double gpa;
    @ApiModelProperty(value = "CKKCS参考课程数")
    @Getter @Setter private Long ckkcs;
    @ApiModelProperty(value = "BJGKCS不及格课程数")
    @Getter @Setter private Long bjgkcs;
    @ApiModelProperty(value = "BJGZXF不及格课程学分总计")
    @Getter @Setter private Double bjgzxf;
}
