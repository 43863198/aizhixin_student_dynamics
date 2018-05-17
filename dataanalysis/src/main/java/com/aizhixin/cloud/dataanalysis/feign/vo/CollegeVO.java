/**
 * 
 */
package com.aizhixin.cloud.dataanalysis.feign.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * @author zhen.pan
 *
 */
@ApiModel(description="学院信息")
@NoArgsConstructor
@ToString
public class CollegeVO implements java.io.Serializable {
	@ApiModelProperty(value = "学院ID")
	@Getter @Setter private Long id;

	@ApiModelProperty(value = "学院名称")
	@Getter @Setter private String name;

	@ApiModelProperty(value = "学院编码")
	@Getter @Setter private String code;

	@ApiModelProperty(value = "组织机构ID")
	@Getter @Setter private Long orgId;

	@ApiModelProperty(value = "用户ID")
	@Getter @Setter private Long userId;

	@ApiModelProperty(value = "创建日期")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	@Getter @Setter private Date createdDate;

}
