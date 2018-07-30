package com.aizhixin.cloud.dataanalysis.notice.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;


@ApiModel(description="通知记录")
@ToString
@NoArgsConstructor
public class NotificationRecordVO {
    @ApiModelProperty(value = "ID")
    @Getter @Setter private String id;

    @ApiModelProperty(value = "发送时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @Getter @Setter private Date sendTime;

    @ApiModelProperty(value = "最后一次访问时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @Getter @Setter private Date lastAccessTime;

    @ApiModelProperty(value = "接收人姓名")
    @Getter @Setter private String receiverName;
    @ApiModelProperty(value = "接收人工号")
    @Getter @Setter private String receiverCode;
    @ApiModelProperty(value = "接收人电话")
    @Getter @Setter private String receiverPhone;

    @ApiModelProperty(value = "学院名称")
    @Getter @Setter private String collegeName;
    @ApiModelProperty(value = "发送结果（10 成功，20 失败)")
    @Getter @Setter private Integer rs;
    @ApiModelProperty(value = "失败消息")
    @Getter @Setter private String failMsg;
    @ApiModelProperty(value = "消息内容")
    @Getter @Setter private String content;

    public NotificationRecordVO (String id, Date sendTime, Date lastAccessTime, String receiverName, String receiverCode, String receiverPhone, String collegeName, Integer rs, String failMsg, String content) {
        this.id = id;
        this.sendTime = sendTime;
        this.lastAccessTime = lastAccessTime;
        this.receiverName = receiverName;
        this.receiverCode = receiverCode;
        this.receiverPhone = receiverPhone;
        this.collegeName = collegeName;
        this.rs = rs;
        this.failMsg = failMsg;
        this.content = content;
    }
}
