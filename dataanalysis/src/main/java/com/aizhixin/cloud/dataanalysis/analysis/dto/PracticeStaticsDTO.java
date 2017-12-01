package com.aizhixin.cloud.dataanalysis.analysis.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * @author: Created by jun.wang
 * @E-mail: wangjun@aizhixin.com
 * @Date: 2017-11-30
 */
@ApiModel(description="实践学情概况统计信息")
@Data
@ToString
@NoArgsConstructor
public class PracticeStaticsDTO {
    @ApiModelProperty(value = "实践人数", required = false)
    private Long practiceStudentNum;
    @ApiModelProperty(value = "实践公司数", required = false)
    private Long practiceCompanyNum;
    @ApiModelProperty(value = "任务数", required = false)
    private Long taskNum;
    @ApiModelProperty(value = "任务数通过", required = false)
    private Long taskPassNum;

    public PracticeStaticsDTO(Long practiceStudentNum, Long practiceCompanyNum, Long taskNum, Long taskPassNum) {
        this.practiceStudentNum = practiceStudentNum;
        this.practiceCompanyNum = practiceCompanyNum;
        this.taskNum = taskNum;
        this.taskPassNum = taskPassNum;
    }
}
