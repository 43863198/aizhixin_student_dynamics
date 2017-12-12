package com.aizhixin.cloud.dataanalysis.analysis.dto;

import com.aizhixin.cloud.dataanalysis.analysis.entity.PracticeStatistics;
import com.aizhixin.cloud.dataanalysis.analysis.entity.SchoolStatistics;
import com.aizhixin.cloud.dataanalysis.common.PageData;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.Column;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.util.Date;

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
    private Long practiceStudentNum=0l;
    @ApiModelProperty(value = "实践公司数", required = false)
    private Long practiceCompanyNum;
    @ApiModelProperty(value = "任务数", required = false)
    private Long taskNum;
    @ApiModelProperty(value = "任务数通过", required = false)
    private Long taskPassNum;

    /**
     *   map.put("classId",rs.getString("CLASS_ID"));
     map.put("className",rs.getString("CLASS_NAME"));
     map.put("practiceNum",rs.getString("PRACTICE_STUDENT_NUM"));
     map.put("practiceCompanyNum",rs.getString("PRACTICE_COMPANY_NUM"));
     map.put("taskNum",rs.getString("TASK_NUM"));
     map.put("taskPassNum",rs.getString("TASK_PASS_NUM"));
     */
    @ApiModelProperty(value = "班级id", required = false)
    private String classId;
    @ApiModelProperty(value = "班级名称", required = false)
    private String className;

    private int convenienceChannel;
    @ApiModelProperty(value = "统计时间", required = false)
    @CreatedDate
    @Column(name = "STATISTICAL_TIME")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @Temporal(TemporalType.TIMESTAMP)
    private  Date statisticalTime = new Date();
    @ApiModelProperty(value = "分页查询", required = false)
    PageData<PracticeStatistics> practiceStatisticsPageData;
    public PracticeStaticsDTO(Long practiceStudentNum, Long practiceCompanyNum, Long taskNum, Long taskPassNum) {
        this.practiceStudentNum = practiceStudentNum==null?0:practiceStudentNum;
        this.practiceCompanyNum = practiceCompanyNum==null?0:practiceCompanyNum;
        this.taskNum = taskNum==null?0:taskNum;
        this.taskPassNum = taskPassNum==null?0:taskPassNum;
    }
}
