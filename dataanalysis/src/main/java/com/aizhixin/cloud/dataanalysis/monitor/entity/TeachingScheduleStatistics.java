package com.aizhixin.cloud.dataanalysis.monitor.entity;

import com.aizhixin.cloud.dataanalysis.common.entity.AbstractEntity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "T_Teaching_Schedule_Statistics")
@ToString
public class TeachingScheduleStatistics extends AbstractEntity {
	/*
	 * 组织id
	 */
	@Column(name = "ORG_ID")
	@Getter
	@Setter
	private Long orgId;
	/*
	 * 1，2节排课数量
	 */
	@Column(name = "COURSE_NUM1")
	@Getter
	@Setter
	private Integer courseNum1;
	/*
	 * 3，4节排课数量
	 */
	@Column(name = "COURSE_NUM3")
	@Getter
	@Setter
	private Integer courseNum3;
	/*
	 * 5，6节排课数量
	 */
	@Column(name = "COURSE_NUM5")
	@Getter
	@Setter
	private Integer courseNum5;
	/*
	 * 7，8节排课数量
	 */
	@Column(name = "COURSE_NUM7")
	@Getter
	@Setter
	private Integer courseNum7;
	/*
	 * 9，10节排课数量
	 */
	@Column(name = "COURSE_NUM9")
	@Getter
	@Setter
	private Integer courseNum9;
	/*
	 * 11，12节排课数量
	 */
	@Column(name = "COURSE_NUM11")
	@Getter
	@Setter
	private Integer courseNum11;
	
	/*
	 * 13，14节排课数量
	 */
	@Column(name = "COURSE_NUM13")
	@Getter
	@Setter
	private Integer courseNum13;
	/*
	 *15，16节排课数量
	 */
	@Column(name = "COURSE_NUM15")
	@Getter
	@Setter
	private Integer courseNum15;
	/*
	 * 17，18节排课数量
	 */
	@Column(name = "COURSE_NUM17")
	@Getter
	@Setter
	private Integer courseNum17;
	/*
	 * 统计时间
	 */
	@Column(name = "STATISTICAL_TIME")
	@Getter
	@Setter
	private String statisticalTime;
}
