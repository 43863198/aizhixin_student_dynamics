package com.aizhixin.cloud.dataanalysis.dd.rollcall.entity;


import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.util.Date;

@ApiModel(description="连续3天考勤汇总告警数据")
@Entity(name = "T_ALERT_LASTEST3_ROLLCALL")
@NoArgsConstructor
@ToString
public class Lastest3RollcallAlert {
    @ApiModelProperty(value = "ID")
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "ID")
    @Getter @Setter private Long id;
    @ApiModelProperty(value = "学生ID")
    @Column(name = "STUDENT_ID")
    @Getter  @Setter private Long studentId;
    @ApiModelProperty(value = "学生学号")
    @Column(name = "STUDENT_NO")
    @Getter  @Setter private String studentNo;
    @ApiModelProperty(value = "学生名称")
    @Column(name = "STUDENT_NAME")
    @Getter  @Setter private String studentName;
    @ApiModelProperty(value = "班级ID")
    @Column(name = "CLASSES_ID")
    @Getter  @Setter private Long classesId;
    @ApiModelProperty(value = "班级名称")
    @Column(name = "CLASSES_NAME")
    @Getter  @Setter private String classesName;
    @ApiModelProperty(value = "专业")
    @Column(name = "PROFESSIONAL_NAME")
    @Getter  @Setter private String professionalName;
    @ApiModelProperty(value = "学院ID")
    @Column(name = "COLLEGE_ID")
    @Getter  @Setter private Long collegeId;
    @ApiModelProperty(value = "学院")
    @Column(name = "COLLEGE_NAME")
    @Getter  @Setter private String collegeName;
    @ApiModelProperty(value = "统计时间范围")
    @Column(name = "DATE_RANGE")
    @Getter  @Setter private String dateRange;
    @ApiModelProperty(value = "计算所属日期")
    @Column(name = "CAL_DATE")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    @Temporal(TemporalType.DATE)
    @Getter  @Setter private Date calDate;
    @ApiModelProperty(value = "应到")
    @Column(name = "SHOULD_COUNT")
    @Getter  @Setter private Long shouldCount;
    @ApiModelProperty(value = "实到")
    @Column(name = "NORMAL")
    @Getter  @Setter private Long normal;
    @ApiModelProperty(value = "到课率")
    @Column(name = "DKL")
    @Getter  @Setter private Double dkl;
    @ApiModelProperty(value = "数据创建日期时间")
    @Column(name = "CREATED_TIME")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @Temporal(TemporalType.TIMESTAMP)
    @Getter  @Setter private Date createTime;
    @ApiModelProperty(value = "学校")
    @Column(name = "ORG_ID")
    @Getter  @Setter private Long orgId;
}
