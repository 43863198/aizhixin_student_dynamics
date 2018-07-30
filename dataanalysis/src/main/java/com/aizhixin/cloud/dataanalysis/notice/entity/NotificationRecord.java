package com.aizhixin.cloud.dataanalysis.notice.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.util.Date;


@ApiModel(description="通知记录")
@Entity
@Table(name = "T_NOTIFICATION_RECORD")
@ToString
@NoArgsConstructor
public class NotificationRecord {
    @ApiModelProperty(value = "ID")
    @Id
    @GeneratedValue(generator = "jpa-uuid")
    @GenericGenerator(name = "jpa-uuid", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(name = "ID")
    @Getter @Setter private String id;
    @ApiModelProperty(value = "发送时间")
    @CreatedDate
    @Column(name = "SEND_TIME")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @Temporal(TemporalType.TIMESTAMP)
    @Getter @Setter private Date sendTime = new Date();
    @ApiModelProperty(value = "最后一次访问时间")
    @LastModifiedDate
    @Column(name = "LAST_ACCESS_TIME")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @Temporal(TemporalType.TIMESTAMP)
    @Getter @Setter private Date lastAccessTime = new Date();
    @ApiModelProperty(value = "告警类型")
    @Column(name = "ALERT_TYPE")
    @Getter @Setter private String alertType;
    @ApiModelProperty(value = "接收人姓名")
    @Column(name = "RECEIVER_NAME")
    @Getter @Setter private String receiverName;
    @ApiModelProperty(value = "接收人工号")
    @Column(name = "RECEIVER_CODE")
    @Getter @Setter private String receiverCode;
    @ApiModelProperty(value = "接收人电话")
    @Column(name = "RECEIVER_PHONE")
    @Getter @Setter private String receiverPhone;
    @ApiModelProperty(value = "学院编码")
    @Column(name = "COLLEGE_CODE")
    @Getter @Setter private String collegeCode;
    @ApiModelProperty(value = "学院名称")
    @Column(name = "COLLEGE_NAME")
    @Getter @Setter private String collegeName;
    @ApiModelProperty(value = "发送结果（10 成功，20 失败)")
    @Column(name = "RS")
    @Getter @Setter private Integer rs;
    @ApiModelProperty(value = "失败消息")
    @Column(name = "FAIL_MSG")
    @Getter @Setter private String failMsg;
    @ApiModelProperty(value = "消息内容")
    @Column(name = "CONTENT")
    @Getter @Setter private String content;
    @ApiModelProperty(value = "学校")
    @Column(name = "ORG_ID")
    @Getter @Setter private Long orgId;
}
