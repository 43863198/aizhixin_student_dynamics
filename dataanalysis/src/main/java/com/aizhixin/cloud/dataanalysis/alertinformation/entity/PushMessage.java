package com.aizhixin.cloud.dataanalysis.alertinformation.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import java.util.Date;

@Entity
@Table(name = "T_PUSH_MESSAGE")
public class PushMessage extends AbstractEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1651645736366646053L;

	// 被推送用户ID
	@Column(name = "user_id")
	@Getter
	@Setter
	private Long userId;

	// 推送内容
	@Column(name = "content")
	@Getter
	@Setter
	private String content;

	// 推送标题
	@Column(name = "title")
	@Getter
	@Setter
	private String title;

	// 模块
	@Column(name = "module")
	@Getter
	@Setter
	private String module;

	// 方法
	@Column(name = "function")
	@Getter
	@Setter
	private String function;

	// 是否已读
	@Column(name = "have_read")
	@Getter
	@Setter
	private Boolean haveRead;
	// 预警学生姓名
	@Column(name = "STUDENT_NAME")
	@Getter
	@Setter
	private String studentName;
	// 预警类型
	@Column(name = "WARNING_TYPE")
	@Getter
	@Setter
	private String warningType;
	//预警级别
	@Column(name = "WARNING_LEVEL")
	@Getter
	@Setter
	private String warningLevel;
	// 推送时间
	@NotNull
	@Column(name = "push_time")
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	@JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
	@Getter
	@Setter
	private Date pushTime;

	// 业务内容
	@Column(name = "business_content")
	@Getter
	@Setter
	private String businessContent;
}
