package com.aizhixin.cloud.dataanalysis.alertinformation.domain;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Email;

import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
public class AccountDTO implements java.io.Serializable{
	
	

	/**
	 * 
	 */
	private static final long serialVersionUID = -1683528815261114626L;

	/**
	 * 修改：用户账号信息后台获取，前台传人参数可以为空
	 */
	@ApiModelProperty(value = "id")
	private Long id;

	@ApiModelProperty(value = "用户名称")
	private String name;

	@ApiModelProperty(value = "账号")
	private String login;
	
	@ApiModelProperty(value = "账号")
	private Boolean activated;

	@Email
	@ApiModelProperty(value = "邮箱", required = false)
	@Size(min = 5, max = 100)
	private String mail;
	
	private String gender;
	
	private String orgLogo;
	
	private String orgPtLogo;
	
	private Long orgId;
	
	private String workNo;
	
	private String mailActivatedTime;
	
	private String orgCode;
	
	private Long collegeId;
	
	private Boolean mailActivated;
	
	private String userGroup;
	
	private String orgLptLogo;
	
	private Date createDate;
	
	private String orgName;
	
	private String orgDomainName;
	
	private Boolean phoneActivated;
	
	private String avatar;
	
	private String collegeName;
	
	private String phone;
	
	private String phoneActivatedTime;
	
	private Long classId;
	
	private String className;
	
	private Long majorId;
	
	private String majorName;
	
	private List<String> roleNames = new ArrayList<String>();
	
}