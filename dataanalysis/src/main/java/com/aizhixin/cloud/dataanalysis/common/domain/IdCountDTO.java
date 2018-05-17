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
@ApiModel(description="id和count信息")
@ToString
@NoArgsConstructor
public class IdCountDTO {
	@ApiModelProperty(value = "ID")
	@Getter @Setter private Long id;

	@ApiModelProperty(value = "统计值")
	@Getter @Setter private long count;

	public IdCountDTO(Long id, long count) {
		this.id = id;
		this.count = count;
	}
}
