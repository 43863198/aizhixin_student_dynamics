package com.aizhixin.cloud.dataanalysis.analysis.entity;

import com.aizhixin.cloud.dataanalysis.common.entity.AbstractEntity;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * @author: Created by jianwei.wu
 * @E-mail: wujianwei@aizhixin.com
 * @Date: 2017-12-25
 */
@Entity
@Table(name = "T_SCHOOL_YEAR_TERM")
@ToString
public class SchoolYearTerm extends AbstractEntity {
    /*
     * 组织id
     */
    @Column(name = "ORG_ID")
    @Getter
    @Setter
    private Long orgId;
    /*
    * 学年
    */
    @Column(name = "TEACHER_YEAR")
    @Getter
    @Setter
    private Integer teacherYear;
    /*
     * 学期
     */
    @Column(name = "SEMESTER")
    @Getter
    @Setter
    private Integer semester;
    /*
     * 学情类型（DataType枚举类中的数据类型）
     */
     @Column(name = "DATA_TYPE")
     @Getter
     @Setter
     private String dataType;

}
