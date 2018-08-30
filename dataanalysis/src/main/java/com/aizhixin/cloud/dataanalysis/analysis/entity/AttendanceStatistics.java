package com.aizhixin.cloud.dataanalysis.analysis.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;

@ApiModel(description="考勤学情分析")
@Entity(name = "T_ATTENDANCE")
@NoArgsConstructor
@ToString
public class AttendanceStatistics {
    @ApiModelProperty(value = "ID")
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "ID")
    @Getter
    @Setter
    private Long id;

    @ApiModelProperty(value = "学校代码")
    @Column(name = "XXDM")
    @Getter @Setter private Long xxdm;
    @ApiModelProperty(value = "院系所号")
    @Column(name = "YXSH")
    @Getter @Setter private String yxsh;
    @ApiModelProperty(value = "院系名称")
    @Column(name = "YXMC")
    @Getter @Setter private String yxmc;
    @ApiModelProperty(value = "专业号")
    @Column(name = "ZYH")
    @Getter @Setter private String zyh;
    @ApiModelProperty(value = "专业名称")
    @Column(name = "ZYMC")
    @Getter @Setter private String zymc;
    @ApiModelProperty(value = "学号")
    @Column(name = "XH")
    @Getter @Setter private String xh;
    @ApiModelProperty(value = "姓名")
    @Column(name = "XM")
    @Getter @Setter private String xm;
    @ApiModelProperty(value = "班号")
    @Column(name = "BH")
    @Getter @Setter private String bh;
    @ApiModelProperty(value = "班级名称")
    @Column(name = "BJMC")
    @Getter @Setter private String bjmc;
    @ApiModelProperty(value = "学年")
    @Column(name = "XN")
    @Getter @Setter private String xn;
    @ApiModelProperty(value = "学期码")
    @Column(name = "XQM")
    @Getter @Setter private String xqm;
    @ApiModelProperty(value = "课程开设单位号")
    @Column(name = "SET_UP_UNIT")
    @Getter @Setter private String dwh;
    @ApiModelProperty(value = "课程开设单位名称")
    @Column(name = "UNIT_NAME")
    @Getter @Setter private String dwmc;
    @ApiModelProperty(value = "课程号")
    @Column(name = "COURSE_NUMBER")
    @Getter @Setter private String kch;
    @ApiModelProperty(value = "课程名称")
    @Column(name = "COURSE_NAME")
    @Getter @Setter private String kcmc;
    @ApiModelProperty(value = "教师工号")
    @Column(name = "TEACHER_JOB_NUMBER")
    @Getter @Setter private String jsgh;
    @ApiModelProperty(value = "教师姓名")
    @Column(name = "TEACHER_NAME")
    @Getter @Setter private String jsxm;
    @ApiModelProperty(value = "考勤结果")
    @Column(name = "RESULT")
    @Getter @Setter private String kqjg;
    @ApiModelProperty(value = "考勤日期")
    @Column(name = "KQRQ")
    @Getter @Setter private String kqrq;


}
