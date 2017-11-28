package com.aizhixin.cloud.dataanalysis.studentRegister.domain;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@ApiModel(description="新生报到excel导入信息")
public class StudentRegisterDomain {
	@ApiModelProperty(value = "line 行号", position=1)
    @Getter @Setter private Integer line;
	@ApiModelProperty(value = "orgId 学校ID", position=2)
	@Getter @Setter private Long orgId;
    @ApiModelProperty(value = "jobNum 学号（工号）", position=3)
    @Getter @Setter private String jobNum;
    @ApiModelProperty(value = "userId 用户ID", position=4)
    @Getter @Setter private Long userId;
    @ApiModelProperty(value = "userName 用户名称", position=5)
    @Getter @Setter private String userName;
    @ApiModelProperty(value = "classId 班级ID", position=6)
    @Getter @Setter private Long classId;
    @ApiModelProperty(value = "className 班级名称", position=7)
    @Getter @Setter private String className;
    @ApiModelProperty(value = "collegeId 学院ID", position=8)
    @Getter @Setter private Long collegeId;
    @ApiModelProperty(value = "collegeName 学院名称", position=9)
    @Getter @Setter private String collegeName;
    @ApiModelProperty(value = "professionalId 专业ID", position=10)
    @Getter @Setter private Long professionalId;
    @ApiModelProperty(value = "professionalName 专业名称", position=11)
    @Getter @Setter private String professionalName;
    @ApiModelProperty(value = "grade 年级", position=9)
    @Getter @Setter private String grade;
    @ApiModelProperty(value = "isregister 是否报到 0--已报到，1--未报到", position=12)
    @Getter @Setter private int isregister;
    @ApiModelProperty(value = "actualRegisterDate 报到日期", position=13)
    @Getter @Setter private String actualRegisterDate;
    @ApiModelProperty(value = "schoolYear 学年学期", position=14)
    @Getter @Setter private String schoolYear;
    @ApiModelProperty(value = "id ID", position=19)
    @Getter @Setter private Long id;
    @ApiModelProperty(value = "msg 消息获取错误描述", position=20)
    @Getter @Setter private String msg;
	public StudentRegisterDomain() {}
	public StudentRegisterDomain(Integer line, String jobNum, Long userId, String userName, Long classId,
			String className, Long collegeId, String collegeName, Long professionalId, String professionalName,
			String grade, int isregister, String actualRegisterDate, String schoolYear) {
		this.line = line;
		this.jobNum = jobNum;
		this.userId = userId;
		this.userName = userName;
		this.classId = classId;
		this.className = className;
		this.collegeId = collegeId;
		this.collegeName = collegeName;
		this.professionalId = professionalId;
		this.professionalName = professionalName;
		this.grade = grade;
		this.isregister = isregister;
		this.actualRegisterDate = actualRegisterDate;
		this.schoolYear = schoolYear;
	}
}
