package com.aizhixin.cloud.dataanalysis.zb.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * 学生学期成绩指标
 */
@ApiModel("学生学期成绩指标")
@ToString
@NoArgsConstructor
public class StudentSemesterScoreIndexDTO {
    @ApiModelProperty(value = "XH学号")
    @Getter @Setter private String xh;
    @ApiModelProperty(value = "CKKCS参考课程数")
    @Getter @Setter private Long ckkcs;
    @ApiModelProperty(value = "BJGKCS不及格课程数")
    @Getter @Setter private Long bjgkcs;
    @ApiModelProperty(value = "BJGZXF不及格课程学分总计")
    @Getter @Setter private Double bjgzxf;

    public StudentSemesterScoreIndexDTO(String xh, Long ckkcs, Long bjgkcs, Double bjgzxf) {
        this.xh = xh;
        this.ckkcs = ckkcs;
        this.bjgkcs = bjgkcs;
        this.bjgzxf = bjgzxf;
    }
}
