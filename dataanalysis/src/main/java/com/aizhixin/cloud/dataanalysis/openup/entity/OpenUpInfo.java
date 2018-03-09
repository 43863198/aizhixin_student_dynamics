package com.aizhixin.cloud.dataanalysis.openup.entity;


import com.aizhixin.cloud.dataanalysis.common.entity.AbstractEntity;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;

@Data
@Entity(name = "T_OPEN_UP_INFO")
public class OpenUpInfo extends AbstractEntity {
    @Column(name = "ORG_ID")
    private Long orgId;
    @Column(name = "ORG_CODE")
    private String orgCode;
}
