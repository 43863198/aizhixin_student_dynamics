package com.aizhixin.cloud.dataanalysis.setup.vo;

import com.aizhixin.cloud.dataanalysis.setup.entity.AlarmReceiver;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@ApiModel (description = "告警信息接收人信息")
@ToString
@NoArgsConstructor
public class AlertReceiverVO {
    @ApiModelProperty(value = "ID")
    @Getter @Setter private String id;

    @ApiModelProperty(value = "学院ID")
    @Getter @Setter private Long collegeId;

    @ApiModelProperty(value = "学院名称")
    @Getter @Setter private String collegeName;

    @ApiModelProperty(value = "老师ID")
    @Getter @Setter private Long teacherId;

    @ApiModelProperty(value = "老师名称")
    @Getter @Setter private String teacherName;

    @ApiModelProperty(value = "老师工号")
    @Getter @Setter private String teacherJobNumber;

    @ApiModelProperty(value = "老师手机号码")
    @Getter @Setter private String teacherPhone;

    public AlertReceiverVO (AlarmReceiver alarmReceiver) {
        if (null != alarmReceiver) {
            this.id = alarmReceiver.getId();
            this.collegeId = alarmReceiver.getCollegeId();
            this.collegeName = alarmReceiver.getCollegeName();
            this.teacherId = alarmReceiver.getTeacherId();
            this.teacherJobNumber = alarmReceiver.getTeacherJobNumber();
            this.teacherName = alarmReceiver.getTeacherName();
            this.teacherPhone = alarmReceiver.getTeacherPhone();
        }
    }
}
