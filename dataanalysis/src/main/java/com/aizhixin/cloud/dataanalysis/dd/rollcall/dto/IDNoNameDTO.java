package com.aizhixin.cloud.dataanalysis.dd.rollcall.dto;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * 基本信息
 */
@ApiModel
@ToString
@NoArgsConstructor
public class IDNoNameDTO {
    @ApiModelProperty(value = "ID")
    @Getter  @Setter private Long id;
    @ApiModelProperty(value = "工号")
    @Getter  @Setter private String no;
    @ApiModelProperty(value = "老师姓名")
    @Getter  @Setter private String name;

    public IDNoNameDTO(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public IDNoNameDTO(Long id, String name, String no) {
        this(id, name);
        this.no = no;
    }
}
