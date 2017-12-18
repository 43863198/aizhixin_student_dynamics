package com.aizhixin.cloud.dataanalysis.monitor.entity;

import com.aizhixin.cloud.dataanalysis.common.entity.AbstractEntity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "T_Abnormal_Teaching_Statistics")
@ToString
public class AbnormalTeachingStatistics extends AbstractEntity {
	/*
	 * 组织id
	 */
	@Column(name = "ORG_ID")
	@Getter
	@Setter
	private Long orgId;
	/*
	 *上课迟到教师人数
	 */
	@Column(name = "LATE_NUM")
	@Getter
	@Setter
	private Integer lateNum;
	/*
	 *下课早退教师人数
	 */
	@Column(name = "LEAVE_EARLY_NUM")
	@Getter
	@Setter
	private Integer leaveEarlyNum;
	/*
	 *有教学任务教师人数
	 */
	@Column(name = "TEACHING_NUM")
	@Getter
	@Setter
	private Integer teachingNum;
	/*
	 * 调停课教师人次
	 */
	@Column(name = "STOP_CLASS_NUM")
	@Getter
	@Setter
	private Integer stopClassNum;
	/*
	 *更换主讲教师人次
	 */
	@Column(name = "CHANGE_LECTURER_NUM")
	@Getter
	@Setter
	private Integer changeLecturerNum;
	/*
	 * 统计时间
	 */
	@Column(name = "STATISTICAL_TIME")
	@Getter
	@Setter
	private String statisticalTime;

}
