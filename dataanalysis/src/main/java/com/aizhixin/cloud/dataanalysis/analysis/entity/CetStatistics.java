package com.aizhixin.cloud.dataanalysis.analysis.entity;

import com.aizhixin.cloud.dataanalysis.common.constant.DataValidity;
import com.aizhixin.cloud.dataanalysis.common.entity.AbstractEntity;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * @author: Created by jianwei.wu
 * @E-mail: wujianwei@aizhixin.com
 * @Date: 2018-05-07
 */
@Entity
@Table(name = "T_CET_SCORE_STATISTICS")
@ToString
public class CetStatistics implements java.io.Serializable {
    @ApiModelProperty(value = "ID")
    @Id
    @Column(name = "ID")
    @GeneratedValue(generator = "jpa-uuid")
    @GenericGenerator(name="jpa-uuid",strategy="org.hibernate.id.UUIDGenerator")
    @Getter @Setter protected String id;

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
   * 学院名称
   */
    @Column(name = "COLLEGE_NAME")
    @Getter @Setter
    private String collegeName;
    /*
    * 学院代码
    */
    @Column(name = "COLLEGE_CODE")
    @Getter @Setter
    private String collegeCode;
    /*
  * 专业名称
  */
    @Column(name = "PROFESSION_NAME")
    @Getter @Setter
    private String professionName;
    /*
    * 专业代码
    */
    @Column(name = "PROFESSION_CODE")
    @Getter @Setter
    private String professionCode;
    /*
    * 班级名称
     */
    @Column(name = "CLASS_NAME")
    @Getter @Setter
    private String className;
    /*
    * 班号
    */
    @Column(name = "CLASS_CODE")
    @Getter @Setter
    private String classCode;
    /*
     * 年级
     */
    @Column(name = "GRADE")
    @Getter @Setter
    private String grade;
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
     * 性别
     */
    @Column(name = "SEX")
    @Getter @Setter
    private String sex;

    @ApiModelProperty(value = "是否删除标志")
    @Column(name = "DELETE_FLAG")
    @Getter @Setter protected Integer deleteFlag = DataValidity.VALID.getState();

}
