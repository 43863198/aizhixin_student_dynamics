package com.aizhixin.cloud.dataanalysis.analysis.dto;

import com.aizhixin.cloud.dataanalysis.analysis.entity.SchoolStatistics;
import com.aizhixin.cloud.dataanalysis.common.PageData;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.Column;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.util.Date;
import java.util.List;

/**
 * @author: Created by jianwei.wu
 * @E-mail: wujianwei@aizhixin.com
 * @Date: 2017-11-30
 */
@ApiModel(description="迎新学情概况统计信息")
@Data
@ToString
@NoArgsConstructor
public class
        NewStudentProfileDTO {
    @ApiModelProperty(value = "新生总数量", required = false)
    private Long studentNumber=0l;
    @ApiModelProperty(value = "已报到人数", required = false)
    private Long alreadyReport=0l;
    @ApiModelProperty(value = "未报到人数", required = false)
    private Long unreported=0l;
    @ApiModelProperty(value = "已报到人数占比", required = false)
    private String proportion="0";
    @ApiModelProperty(value = "完成缴费人数", required = false)
    private int alreadyPay=0;
    @ApiModelProperty(value = "学年", required = false)
    private Integer teacherYear;
    @ApiModelProperty(value = "走绿色通道人数", required = false)
    private int convenienceChannel;
    @ApiModelProperty(value = "统计时间", required = false)
    @CreatedDate
    @Column(name = "STATISTICAL_TIME")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @Temporal(TemporalType.TIMESTAMP)
    private  Date statisticalTime = new Date();
    @ApiModelProperty(value = "查询学院新生概况", required = false)
    List<SchoolStatistics> schoolStatisticsListData;

    public NewStudentProfileDTO(Long studentNumber, Long alreadyReport,Integer teacherYear) {
        this.studentNumber = studentNumber==null?0:studentNumber;
        this.alreadyReport = alreadyReport==null?0:alreadyReport;
        this.unreported = this.studentNumber-this.alreadyReport;
        this.teacherYear=teacherYear;
        if(this.studentNumber==0){
            this.proportion="0";
        }else {
            this.proportion = this.alreadyReport/this.studentNumber+"";
        }
    }
}
