/**
 * 
 */
package com.aizhixin.cloud.dataanalysis.common.domain;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * @author zhen.pan
 *
 */
@ApiModel(description="一般用来承载操作成功或失败原因的视图对象")
@NoArgsConstructor
@ToString
public class MessageVO implements java.io.Serializable {
	@ApiModelProperty(value = "代码含义：成功0，其它错误码")
	@Getter @Setter private Integer code = 0;

	@ApiModelProperty(value = "消息含义：成功SUCCESS，失败原因描述")
	@Getter @Setter private String message = "SUCCESS";

	@ApiModelProperty(value = "业务返回值")
	@Getter @Setter private String data;

	public MessageVO(Integer code, String message) {
		this.code = code;
		this.message = message;
	}
}
