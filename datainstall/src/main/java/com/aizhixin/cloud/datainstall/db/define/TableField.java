package com.aizhixin.cloud.datainstall.db.define;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@ApiModel(description="数据库表字段描述")
@NoArgsConstructor
@ToString
public class TableField implements Comparable<TableField> {
    @ApiModelProperty(value = "字段名称")
    @Getter @Setter private String name;
    @ApiModelProperty(value = "字段描述")
    @Getter @Setter private String remark;
    @ApiModelProperty(value = "字段数据类型(会自动转成全大写)")
    @Getter @Setter private String type;
    @ApiModelProperty(value = "排序码（由小到大）")
    @Getter @Setter private int order;

    public TableField (String name, String type) {
        this.name = name;
        this.type = type;
        this.order = 0;
    }

    public TableField (String name, String type, int order) {
        this(name, type);
        this.order = order;
    }

    public int compareTo(TableField f) {
        return this.order - f.getOrder();
    }
}
