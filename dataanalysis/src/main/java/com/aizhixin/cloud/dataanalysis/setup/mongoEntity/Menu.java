package com.aizhixin.cloud.dataanalysis.setup.mongoEntity;


import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.util.*;

@Document(collection="Menu")
@NoArgsConstructor
@ToString
public class Menu {

    @ApiModelProperty(value = "ID，写入时最好不出现")
    @Id
    @Getter @Setter private String id;
    @ApiModelProperty(value = "机构ID")
    @Indexed
    @Getter @Setter private Long orgId;

    @ApiModelProperty(value = "角色列表")
    @Indexed
    @Getter @Setter private List<String> roles = new ArrayList<>();

    @ApiModelProperty(value = "标签")
    @Indexed
    @Getter @Setter private String tag;

    @ApiModelProperty(value = "数据内容")
    @Getter @Setter private Map<String, ?> settings = new HashMap<>();

    @ApiModelProperty(value = "最后修改时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @Temporal(TemporalType.TIMESTAMP)
    @Getter @Setter private Date update_date = new Date();

    @ApiModelProperty(value = "创建时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @Temporal(TemporalType.TIMESTAMP)
    @Getter @Setter private Date create_date = new Date();

    @ApiModelProperty(value = "创建人")
    @Getter @Setter private String create_by;

    @ApiModelProperty(value = "最后修改人")
    @Getter @Setter private String update_by;
}
