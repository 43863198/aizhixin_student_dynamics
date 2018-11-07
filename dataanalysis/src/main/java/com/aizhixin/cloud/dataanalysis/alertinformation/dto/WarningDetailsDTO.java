package com.aizhixin.cloud.dataanalysis.alertinformation.dto;

import com.aizhixin.cloud.dataanalysis.alertinformation.domain.DealDomain;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.util.Date;
import java.util.List;

/**
 * @author: Created by jianwei.wu
 * @E-mail: wujianwei@aizhixin.com
 * @Date: 2017-11-21
 */
@ApiModel(description="预警详情")
@Data
public class    WarningDetailsDTO {
    @ApiModelProperty(value = "id")
    private String id;

    @ApiModelProperty(value = "告警人姓名")
    private String name;

    @ApiModelProperty(value = "学号")
    private String jobNumber;

    @ApiModelProperty(value = "所属学院")
    private String collogeName;

    @ApiModelProperty(value = "所属专业")
    private String professionalName;

    @ApiModelProperty(value = "所属班级")
    private String className;

    @ApiModelProperty(value = "学年")
    private String teacherYear;

    @ApiModelProperty(value = "年级")
    private String nj;
    
    @ApiModelProperty(value = "学期")
    private String semester;
 
    @ApiModelProperty(value = "手机号")
    private String phone;

    @ApiModelProperty(value = "家庭住址")
    private String address;

    @ApiModelProperty(value = "家长联系方式")
    private String parentsContact;

    @ApiModelProperty(value = "预警名称")
    private String warningName;

    @ApiModelProperty(value = "预警产生时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @Temporal(TemporalType.TIMESTAMP)
    protected Date warningTime = new Date();

    @ApiModelProperty(value = "预警等级")
    private int warningLevel;

    @ApiModelProperty(value = "产生预警条件")
    private String warningCondition;

    @ApiModelProperty(value = "预警标准")
    private String warningStandard;

    @ApiModelProperty(value = "数据源")
    private String warningSource;

    @ApiModelProperty(value = "预警状态")
    private int warningState;

    @ApiModelProperty(value = "处理操作", required = false)
    private List<DealDomain> dealDomainList;

    @ApiModelProperty(value = "预警处理时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dealTime = new Date();

    @ApiModelProperty(value = "撤销说明(仅在预警状态是40的时候有用)")
    @Getter @Setter private String cancelComments;
}
