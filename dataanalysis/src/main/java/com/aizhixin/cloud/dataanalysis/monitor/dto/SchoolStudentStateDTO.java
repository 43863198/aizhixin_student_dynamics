package com.aizhixin.cloud.dataanalysis.monitor.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@ApiModel(description="大屏监学生状态")
@Data
@ToString
@NoArgsConstructor
public class SchoolStudentStateDTO {
    @ApiModelProperty(value = "全校人数", required = false)
    private Integer allStudent;
    @ApiModelProperty(value = "在校人数", required = false)
    private Integer inSchoolStudent;
    @ApiModelProperty(value = "请假人数", required = false)
    private Integer leaveSchoolStudent;
    @ApiModelProperty(value = "离校人数", required = false)
    private Integer outSchoolStudent;

}
