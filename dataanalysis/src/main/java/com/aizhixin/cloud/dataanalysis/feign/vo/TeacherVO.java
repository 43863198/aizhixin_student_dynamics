/**
 * 
 */
package com.aizhixin.cloud.dataanalysis.feign.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * @author zhen.pan
 *
 */
@ApiModel(description="教师信息")
@NoArgsConstructor
@ToString
public class TeacherVO {
	@ApiModelProperty(value = "ID")
	@Getter @Setter private Long id;
	@ApiModelProperty(value = "账号ID")
	@Getter @Setter private Long accountId;
	@ApiModelProperty(value = "姓名")
	@Getter @Setter private String name;
	@ApiModelProperty(value = "姓名电话")
	@Getter @Setter private String phone;
	@ApiModelProperty(value = "邮箱")
	@Getter @Setter private String email;
	@ApiModelProperty(value = "工号")
	@Getter @Setter private String jobNumber;
	@ApiModelProperty(value = "性别")
	@Getter @Setter private String sex;
	@ApiModelProperty(value = "是否班主任")
	@Getter @Setter private Boolean classesManager;
	@ApiModelProperty(value = "学院ID")
	@Getter @Setter private Long collegeId;
	@ApiModelProperty(value = "学院名称")
	@Getter @Setter private String collegeName;
	@ApiModelProperty(value = "学院编码")
	@Getter @Setter private String collegeCode;
	@ApiModelProperty(value = "学校ID")
	@Getter @Setter private Long orgId;
	@ApiModelProperty(value = "创建日期")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	@Getter @Setter private Date createdDate;
	@ApiModelProperty(value = "操作用户ID")
	@Getter @Setter private Long userId;
}
