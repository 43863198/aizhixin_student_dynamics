package com.aizhixin.cloud.dataanalysis.analysis.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 *
 */
@ApiModel(description="英语四六级统计信息")
@ToString
@NoArgsConstructor
public class CodeNameCountDTO {
    @ApiModelProperty(value = "CODE")
    @Getter @Setter private String code;
    @ApiModelProperty(value = "NAME")
    @Getter @Setter private String name;
    @ApiModelProperty(value = "Count")
    @Getter @Setter private long count;

    public CodeNameCountDTO (String code, String name, long count) {
        this.code = code;
        this.name = name;
        this.count = count;
    }
}
