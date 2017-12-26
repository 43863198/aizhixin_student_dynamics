package com.aizhixin.cloud.dataanalysis.monitor.entity;

import com.aizhixin.cloud.dataanalysis.common.entity.AbstractEntity;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "T_Abnormal_Attendance_Statistics")
@ToString
public class AbnormalAttendanceStatistics extends AbstractEntity {
	/*
	 * 组织id
	 */
	@Column(name = "ORG_ID")
	@Getter
	@Setter
	private Long orgId;
	/*
	 * 迟到人数
	 */
	@Column(name = "LATE_NUM")
	@Getter
	@Setter
	private Integer lateNum =0;
	/*
	 * 早退人数
	 */
	@Column(name = "LEAVE_EARLY_NUM")
	@Getter
	@Setter
	private Integer leaveEarlyNum =0;
	/*
	 * 旷课人数
	 */
	@Column(name = "ABSENTEEISM_NUM")
	@Getter
	@Setter
	private Integer absenteeismNum =0;
	/*
	 * 请假人数
	 */
	@Column(name = "LEAVE_NUM")
	@Getter
	@Setter
	private Integer leaveNum =0;
	/*
	 * 统计时间
	 */
	@Column(name = "STATISTICAL_TIME")
	@Getter
	@Setter
	private String statisticalTime;
}
