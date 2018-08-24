package com.aizhixin.cloud.dataanalysis.analysis.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;

@ApiModel(description="辅修专业二学位信息")
@Entity(name = "T_FXEXWXX")
@NoArgsConstructor
@ToString
public class MinorSecondDegreeInfo {
    @ApiModelProperty(value = "ID")
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "ID")
    @Getter
    @Setter
    private Long id;

    @ApiModelProperty(value = "学校代码")
    @Column(name = "XXDM")
    @Getter @Setter private Long xxdm;
    @ApiModelProperty(value = "学号")
    @Column(name = "XH")
    @Getter @Setter private String xh;
    @ApiModelProperty(value = "姓名")
    @Column(name = "XM")
    @Getter @Setter private String xm;
    @ApiModelProperty(value = "专业号")
    @Column(name = "ZYH")
    @Getter @Setter private String zyh;
    @ApiModelProperty(value = "专业名称")
    @Column(name = "ZYMC")
    @Getter @Setter private String zymc;
    @ApiModelProperty(value = "院系所号")
    @Column(name = "YXSH")
    @Getter @Setter private String yxsh;
    @ApiModelProperty(value = "院系名称")
    @Column(name = "YXMC")
    @Getter @Setter private String yxmc;
    @ApiModelProperty(value = "辅修专业号")
    @Column(name = "FXZYH")
    @Getter @Setter private String fxzyh;
    @ApiModelProperty(value = "辅修专业名称")
    @Column(name = "FXZYMC")
    @Getter @Setter private String fxzyhmc;
    @ApiModelProperty(value = "辅修院系所号")
    @Column(name = "FXYXSH")
    @Getter @Setter private String fxyxsh;
    @ApiModelProperty(value = "辅修院系名称")
    @Column(name = "FXYXMC")
    @Getter @Setter private String fxyxmc;
    @ApiModelProperty(value = "二学位专业号")
    @Column(name = "EXWZYH")
    @Getter @Setter private String exwzyh;
    @ApiModelProperty(value = "二学位专业名称")
    @Column(name = "EXWZYMC")
    @Getter @Setter private String exwzymc;
    @ApiModelProperty(value = "二学位院系所号")
    @Column(name = "EXWYXSH")
    @Getter @Setter private String exwyxsh;
    @ApiModelProperty(value = "二学位院系名称")
    @Column(name = "EXWYXMC")
    @Getter @Setter private String exwyxmc;
}
