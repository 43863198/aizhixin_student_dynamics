package com.aizhixin.cloud.dataanalysis.analysis.dto;

import com.aizhixin.cloud.dataanalysis.analysis.entity.SchoolStatistics;
import com.aizhixin.cloud.dataanalysis.common.PageData;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.Column;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.util.Date;

/**
 * @author: Created by jianwei.wu
 * @E-mail: wujianwei@aizhixin.com
 * @Date: 2017-11-30
 */
@ApiModel(description="迎新学情概况统计信息")
@Data
public class NewStudentProfileDTO {
    @ApiModelProperty(value = "新生总数量", required = false)
    private int studentNumber;
    @ApiModelProperty(value = "已报到人数", required = false)
    private int alreadyReport;
    @ApiModelProperty(value = "未报到人数", required = false)
    private int unreported;
    @ApiModelProperty(value = "已报到人数占比", required = false)
    private String Proportion;
    @ApiModelProperty(value = "完成缴费人数", required = false)
    private int alreadyPay;
    @ApiModelProperty(value = "走绿色通道人数", required = false)
    private int convenienceChannel;
    @ApiModelProperty(value = "统计时间", required = false)
    @CreatedDate
    @Column(name = "STATISTICAL_TIME")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @Temporal(TemporalType.TIMESTAMP)
    private  Date statisticalTime = new Date();
    @ApiModelProperty(value = "分页查询学院新生概况", required = false)
    PageData<SchoolStatistics> schoolStatisticsPageData;

}
