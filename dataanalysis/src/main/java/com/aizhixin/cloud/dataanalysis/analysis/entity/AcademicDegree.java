package com.aizhixin.cloud.dataanalysis.analysis.entity;

import com.aizhixin.cloud.dataanalysis.common.constant.DataValidity;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * @author: Created by jianwei.wu
 * @E-mail: wujianwei@aizhixin.com
 * @Date: 2018-04-16
 */
@Entity
@Table(name = "T_ACADEMIC_DEGREE")
@ToString
public class AcademicDegree implements Serializable {
    @ApiModelProperty(value = "ID")
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "ID")
    @Getter
    @Setter
    protected Long id;
    /* 学校id*/
    @Column(name = "ORG_ID") @Getter @Setter private Long orgId;
    /*学号*/
    @Column(name = "JOB_NUMBER")
    @Getter @Setter private String jobNmuber;
    /*学制*/
    @Column(name = "LENGTH_OF_SCHOOLING")
    @Getter @Setter private int lengthOfSchooling;
    /*入学日期*/
    @Column(name = "DATE_OF_ADMISSION")
    @Getter @Setter private String dateOfAdmission;
    /*学习方式码*/
    @Column(name = "LEARNING_STYLE")
    @Getter @Setter private String learningStyle;
    /*专业*/
    @Column(name = "MAJOR")
    @Getter @Setter private String major;
    /*结业日期*/
    @Column(name = "DATE_OF_COMPLETION")
    @Getter @Setter private String dateOfCompletion;
    /*获得学位码*/
    @Column(name = "GET_A_DEGREE_CODE")
    @Getter @Setter private String getADegreeCode;
    /*获得学位日期*/
    @Column(name = "GET_A_DEGREE_DATE")
    @Getter @Setter private Date getADegreeDate;
    /*学位证号*/
    @Column(name = " DEGREE_CERTIFICATE_NUMBER")
    @Getter @Setter private String  degreeCertificateNumber;
    /*毕业证号*/
    @Column(name = "DIPLOMA_NUMBER")
    @Getter @Setter private String diplomaNumber;
    @ApiModelProperty(value = "是否删除标志")
    @Column(name = "DELETE_FLAG")
    @Getter @Setter protected Integer deleteFlag = DataValidity.VALID.getState();

}
