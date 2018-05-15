package com.aizhixin.cloud.dataanalysis.analysis.entity;

import com.aizhixin.cloud.dataanalysis.common.constant.DataValidity;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Date;

/**
 * @author: Created by jianwei.wu
 * @E-mail: wujianwei@aizhixin.com
 * @Date: 2018-05-09
 */
@Entity
@Table(name = "T_SCORE_STATISTICS")
@ToString
public class ScoreStatistics implements java.io.Serializable {
    @ApiModelProperty(value = "ID")
    @Id
    @Column(name = "ID")
    @GeneratedValue(generator = "jpa-uuid")
    @GenericGenerator(name="jpa-uuid",strategy="org.hibernate.id.UUIDGenerator")
    @Getter
    @Setter
    protected String id;
    /*
     * 组织id
     */
    @Column(name = "ORG_ID")
    @Getter @Setter
    private Long orgId;
    /*
     * 考试日期
     */
    @Column(name = "EXAMINATION_DATE")
    @Getter @Setter
    private Date examDate;
    /*
    * 参加人数
    */
    @Column(name = "JOIN_NUMBER")
    @Getter @Setter
    private Integer joinNumber;
    /*
    * 平均分
    */
    @Column(name = "AVG_SCORE")
    @Getter @Setter
    private Double avgScoure;
    /*
    * 最高分
    */
    @Column(name = "MAX_SCORE")
    @Getter @Setter
    private Double maxScore;
    /*
    * 通过人数
    */
    @Column(name = "PASS_NUMBER")
    @Getter @Setter
    private Integer passNumber;
    /*
    * 成绩类型
    */
    @Column(name = "SCORE_TYPE")
    @Getter @Setter
    private String scoreType;
    /*
     * 统计类型
     */
    @Column(name = "STATISTICS_TYPE")
    @Getter @Setter
    private String statisticsType;
    /*
    * 父级码
    */
    @Column(name = "PARENT_CODE")
    @Getter @Setter
    private String parentCode;
    /*
    *名称码
    */
    @Column(name = "CODE")
    @Getter @Setter
    private String code;
    /*
    *名称码
    */
    @Column(name = "NAME_CODE")
    @Getter @Setter
    private String name;

    @ApiModelProperty(value = "是否删除标志")
    @Column(name = "DELETE_FLAG")
    @Getter @Setter protected Integer deleteFlag = DataValidity.VALID.getState();

    /*
     *名称码
     */
    @Column(name = "CLASS_NAME")
    @Getter @Setter
    private String className;

}






