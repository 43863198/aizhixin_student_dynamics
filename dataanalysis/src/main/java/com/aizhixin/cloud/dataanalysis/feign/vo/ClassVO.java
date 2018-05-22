package com.aizhixin.cloud.dataanalysis.feign.vo;

import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * @author: Created by jianwei.wu
 * @E-mail: wujianwei@aizhixin.com
 * @Date: 2018-05-22
 */
@ApiModel(description="学院信息")
@NoArgsConstructor
@ToString
@Data
public class ClassVO {
    private Long id;
    private String name;
}
