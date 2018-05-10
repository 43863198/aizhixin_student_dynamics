package com.aizhixin.cloud.dataanalysis.analysis.entity;

import com.aizhixin.cloud.dataanalysis.common.constant.DataValidity;
import com.aizhixin.cloud.dataanalysis.common.entity.AbstractEntity;
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
  @Table(name = "T_SCORE_TOP")
  @ToString
  public class ScoreTop implements java.io.Serializable {
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
     * 学号
     */
    @Column(name = "JOB_NUMBER")
    @Getter @Setter
    private String jobNumber;
    /*
     * 姓名
     */
    @Column(name = "NAME")
    @Getter @Setter
    private String name;

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
     * 考试日期
    */
    @Column(name = "EXAMINATION_DATE")
    @Getter @Setter
    private Date examDate;
    /*
    * 最高分
    */
    @Column(name = "MAX_SCORE")
    @Getter @Setter
    private String maxScore;
    /*
    * 成绩类型
    */
    @Column(name = "SCORE_TYPE")
    @Getter @Setter
    private String scoreType;

    @ApiModelProperty(value = "是否删除标志")
    @Column(name = "DELETE_FLAG")
    @Getter @Setter protected Integer deleteFlag = DataValidity.VALID.getState();

}
