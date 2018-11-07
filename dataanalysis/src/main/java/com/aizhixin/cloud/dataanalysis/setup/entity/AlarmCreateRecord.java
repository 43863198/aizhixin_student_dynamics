package com.aizhixin.cloud.dataanalysis.setup.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.util.Date;

/**
 * 告警生成日志
 */
@ApiModel(description="告警生成日志")
@Entity(name = "T_ALARM_CREATE_RECORD")
@NoArgsConstructor
@ToString
public class AlarmCreateRecord {

    @ApiModelProperty(value = "ID")
    @Id
    @GeneratedValue(generator = "jpa-uuid")
    @GenericGenerator(name = "jpa-uuid", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(name = "ID")
    @Getter @Setter private String id;

    @ApiModelProperty(value = "学校ID")
    @Column(name = "ORG_ID")
    @Getter @Setter private Long orgId;

    @ApiModelProperty(value = "预警类型名称")
    @Column(name = "ALARM_TYPE")
    @Getter @Setter private String alarmType;

    @ApiModelProperty(value = "覆盖红色预警条数")
    @Column(name = "DELETE_RED_NUM")
    @Getter @Setter private Integer deleteRedNum;

    @ApiModelProperty(value = "覆盖橙色预警条数")
    @Column(name = "DELETE_ORG_NUM")
    @Getter @Setter private Integer deleteOrgNum;

    @ApiModelProperty(value = "覆盖黄色预警条数")
    @Column(name = "DELETE_YELLO_NUM")
    @Getter @Setter private Integer deleteYelloNum;

    @ApiModelProperty(value = "新增红色预警条数")
    @Column(name = "ADD_RED_NUM")
    @Getter @Setter private Integer addRedNum;

    @ApiModelProperty(value = "新增橙色预警条数")
    @Column(name = "ADD_ORG_NUM")
    @Getter @Setter private Integer addOrgNum;

    @ApiModelProperty(value = "新增黄色预警条数")
    @Column(name = "ADD_YELLO_NUM")
    @Getter @Setter private Integer addYelloNum;

    @ApiModelProperty(value = "创建人ID")
    @Column(name = "CREATED_ID")
    @Getter @Setter private Long createdId;

    @ApiModelProperty(value = "创建人")
    @CreatedBy
    @Column(name = "CREATED_BY")
    @Getter @Setter private String createdBy;

    @ApiModelProperty(value = "创建时间")
    @CreatedDate
    @Column(name = "CREATED_DATE")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @Temporal(TemporalType.TIMESTAMP)
    @Getter @Setter private Date createdDate = new Date();
}
