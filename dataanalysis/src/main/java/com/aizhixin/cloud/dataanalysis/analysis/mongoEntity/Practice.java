package com.aizhixin.cloud.dataanalysis.analysis.mongoEntity;

import java.util.Date;

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
	@Indexed
	private String jobNum;
	
	/**
	 * 用户id
	 */
	private Long userId;
	
	/**
	 * 姓名
	 */
	private String userName;
	
	/**
	 * 照片
	 */
	private String userPhoto;
	
	/**
	 * 班级id
	 */
	private Long classId;
	/**
	 * 班级名称
	 */
	private String className;
	
	/**
	 * 专业id
	 */
	private Long professionalId;
	
	/**
	 * 专业名称
	 */
	private String professionalName;
	
	/**
	 * 学院id
	 */
	private Long collegeId;
	
	/**
	 * 学院名称
	 */
	private String collegeName;
	
	/**
	 * 用户手机号
	 */
	private  String userPhone;
	
	/**
	 * 年级
	 */
	private String grade;
	
	/**
	 * 学期
	 */
	@Indexed
	private int semester;
	
	/**
	 * 学年
	 */
	@Indexed
	private int schoolYear;
	
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
