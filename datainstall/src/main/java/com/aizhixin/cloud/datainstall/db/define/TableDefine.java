package com.aizhixin.cloud.datainstall.db.define;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@ApiModel(description="数据库表描述")
@NoArgsConstructor
@ToString
@Slf4j
public class TableDefine {
    @ApiModelProperty(value = "库名称")
    @Getter @Setter private String name;

    @ApiModelProperty(value = "库描述")
    @Getter @Setter private String remark;

    @ApiModelProperty(value = "字段定义")
    @Getter @Setter private List<TableField> fields = new ArrayList<>();

    /**
     *  逐个加入字段定义（缺省是按照加入顺序）
     */
    public void addField(String fieldName, String fieldType) {
        if (null != fieldType) {//转换为大写字母
            fieldType = fieldType.toUpperCase();
        }
        fields.add(new TableField(fieldName, fieldType, fields.size()));
    }

    /**
     * 对字段进行排序
     */
    public void sortField() {
        if (null != fields) {
            Collections.sort(fields);
        }
    }

    /**
     * 获取CSV文件头定义
     */
    public String getCsvHead(char fd) {
        StringBuilder s = new StringBuilder();
        for (int i = 0; i < fields.size(); i++) {
            TableField f = fields.get(i);
            s.append(f.getName());
            if (i < fields.size() - 1) {
                s.append(fd).append(" ");
            }
        }
        return s.toString();
    }

    /**
     * 生成查询SQL
     *  SELECT f1, f2, fxx FROM table_name
     */
    public String getSelectSQL() {
        if (StringUtils.isEmpty(name) || null == fields || fields.isEmpty()) {
            log.warn("Table name is null OR field is Empty.");
            return null;
        }
        StringBuilder s = new StringBuilder("SELECT ");
        s.append(getCsvHead(','));
        s.append(" FROM ").append(name);
        return s.toString();
    }

    public String getSelectSQL(String database) {
        if (StringUtils.isEmpty(name) || null == fields || fields.isEmpty()) {
            log.warn("Table name is null OR field is Empty.");
            return null;
        }
        StringBuilder s = new StringBuilder("SELECT ");
        if (database.equals("ORACLE")) {
            s.append(" rownum rowno, ");
        }
        s.append(getCsvHead(','));
        s.append(" FROM ").append(name);
        return s.toString();
    }
}
