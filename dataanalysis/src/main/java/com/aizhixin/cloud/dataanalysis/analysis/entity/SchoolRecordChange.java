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
 * @Date: 2018-04-17
 */
@Entity
@Table(name = "T_SCHOOL_RECORD_CHANGE")
@ToString
public class SchoolRecordChange implements Serializable {
    @ApiModelProperty(value = "ID")
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "ID")
    @Getter
    @Setter
    protected Long id;
    /* 学校id*/
    @Column(name = "ORG_ID")
    @Getter @Setter private Long orgId;
    /*学生学号*/
    @Column(name = "STRUDENT_JOB_NUMBER")
    @Getter @Setter private String studnetJobNumber;
    /*异动日期*/
    @Column(name = "DATE_OF_CHANGE")
    @Getter @Setter private Date dateOfChange;
    /*异动原因码*/
    @Column(name = "CAUSE_OF_CHANGE_NUMBER")
    @Getter @Setter private String causeOfChangeNumber;
    /*审批日期*/
    @Column(name = "DATE_OF_APPROVAL")
    @Getter @Setter private String dateOfApproval;
    /*审批文号*/
    @Column(name = "APPROVAL_NUMBER")
    @Getter @Setter private String approvalNumber;
    /*异动说明*/
    @Column(name = "CHANGE_DESCRIPTION")
    @Getter @Setter private String changeDescription;
    /*异动类别码*/
    @Column(name = "TYPE_OF_CHANGE_NUMBER")
    @Getter @Setter private String typeOfChangeNumber;
    /*原院系所号*/
    @Column(name = "OLD_COLLEGE_NUMBER")
    @Getter @Setter private String oldCollegeNumber;
    /*原专业码*/
    @Column(name = "OLD_MAJOR_NUMBER")
    @Getter @Setter private String oldMajorNumber;
    /*原班号*/
    @Column(name = "OLD_CLASS_NUMBER")
    @Getter @Setter private String oldClassNumber;
    /*原年级*/
    @Column(name = "OLD_GRADE")
    @Getter @Setter private String oldGrade;
    /*原学制*/
    @Column(name = "OLD_LENGTH_OF_SCHOOLING")
    @Getter @Setter private int oldLengthOfSchooling;
    /*现院系所号*/
    @Column(name = "NEW_COLLEGE_NUMBER")
    @Getter @Setter private String newCollegeNumber;
    /*现专业码*/
    @Column(name = "NEW_MAJOR_NUMBER")
    @Getter @Setter private String newMajorNumber;
    /*现班号*/
    @Column(name = "NEW_CLASS_NUMBER")
    @Getter @Setter private String newClassNumber;
    /*现年级*/
    @Column(name = "NEW_GRADE")
    @Getter @Setter private String newGrade;
    /*现学制*/
    @Column(name = "NEW_LENGTH_OF_SCHOOLING")
    @Getter @Setter private int newLengthOfSchooling;
    /*异动ID*/
    @Column(name = "TRANSACTION_ID")
    @Getter @Setter private String transactionId;
    @ApiModelProperty(value = "是否删除标志")
    @Column(name = "DELETE_FLAG")
    @Getter @Setter protected Integer deleteFlag = DataValidity.VALID.getState();

}
