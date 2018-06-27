package com.aizhixin.cloud.dataanalysis.analysis.mongoEntity;

import java.util.Date;

import io.swagger.annotations.ApiModelProperty;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;

@Document(collection="Practice")
@Data
public class Practice {

	@Id
	private String id;

	/**
	 * 机构id
	 */
	@Indexed
	private Long orgId;
	/**
	 * 工号/学号
	 */
	private String jobNum;
	/**
	 * 姓名
	 */
	private String userName;

	/**
	 * 照片
	 */
	private String userPhoto;

	/**
	 * 班号
	 */
	private String classCode;
	/**
	 * 班级名称
	 */
	private String className;

	/**
	 * 专业code
	 */
	private String professionalCode;

	/**
	 * 专业名称
	 */
	private String professionalName;

	/**
	 * 学院code
	 */
	private String collegeCode;

	/**
	 * 学院名称
	 */
	private String collegeName;

	/**
	 * 用户手机号
	 */
	private  String userPhone;

	@ApiModelProperty(value = "学期", required = false)
	private String semester;

	@ApiModelProperty(value = "学年", required = false)
	private String teachYear;
	
	/**
	 * 年级
	 */
	private String grade;

	/**
	 * 实践企业名称
	 */
	private String companyName;
	
	/**
	 * 实践企业所在省份
	 */
	private String companyProvince;
	
	/**
	 * 实践企业所在城市
	 */
	private String companyCity;
	
	/**
	 * 实践任务评审结果(pass:已通过,notPass:未通过,backTo:被打回)
	 */
	private String reviewResult;
	
	/**
	 * 实践任务Id
	 */
	private String taskId;
	
	/**
	 * 实践任务创建日期
	 */
	private Date taskCreatedDate;
	
	/**
	 * 备注
	 */
	private String remarks;
	
}
