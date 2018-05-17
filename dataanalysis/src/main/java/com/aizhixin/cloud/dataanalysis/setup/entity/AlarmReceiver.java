package com.aizhixin.cloud.dataanalysis.setup.entity;

import com.aizhixin.cloud.dataanalysis.common.core.DataValidity;
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
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.util.Date;

/**
 * 告警信息接收人信息
 */
@ApiModel(description="告警信息接收人信息")
@Entity(name = "T_ALARM_RECEIVER")
@NoArgsConstructor
@ToString
public class AlarmReceiver {

    @ApiModelProperty(value = "ID")
    @Id
    @GeneratedValue(generator = "jpa-uuid")
    @GenericGenerator(name = "jpa-uuid", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(name = "ID")
    @Getter @Setter private String id;

    @ApiModelProperty(value = "学校ID")
    @Column(name = "ORG_ID")
    @Getter @Setter private Long orgId;

    @ApiModelProperty(value = "学院ID")
    @Column(name = "COLLEGE_ID")
    @Getter @Setter private Long collegeId;

    @ApiModelProperty(value = "学院名称")
    @Column(name = "COLLEGE_NAME")
    @Getter @Setter private String collegeName;

    @ApiModelProperty(value = "老师ID")
    @Column(name = "TEACHER_ID")
    @Getter @Setter private Long teacherId;

    @ApiModelProperty(value = "老师名称")
    @Column(name = "TEACHER_NAME")
    @Getter @Setter private String teacherName;

    @ApiModelProperty(value = "老师工号")
    @Column(name = "TEACHER_JOB_NUMBER")
    @Getter @Setter private String teacherJobNumber;

    @ApiModelProperty(value = "老师手机号码")
    @Column(name = "TEACHER_PHONE")
    @Getter @Setter private String teacherPhone;

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

    @ApiModelProperty(value = "最后一次修改人")
    @LastModifiedBy
    @Column(name = "LAST_MODIFIED_BY")
    @Getter @Setter private String lastModifiedBy;

    @ApiModelProperty(value = "最后一次修改时间")
    @LastModifiedDate
    @Column(name = "LAST_MODIFIED_DATE")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @Temporal(TemporalType.TIMESTAMP)
    @Getter @Setter private Date lastModifiedDate = new Date();

    @ApiModelProperty(value = "是否删除标志")
    @Column(name = "DELETE_FLAG")
    @Getter @Setter private Integer deleteFlag = DataValidity.VALID.getIntValue();
}
