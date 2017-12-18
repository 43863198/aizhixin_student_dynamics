package com.aizhixin.cloud.dataanalysis.analysis.entity;

import com.aizhixin.cloud.dataanalysis.common.entity.AbstractEntity;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "T_PRACTICE_STATISTICS")
@ToString
public class PracticeStatistics extends AbstractEntity {
    /*
     * 组织id
     */
    @Column(name = "ORG_ID")
    @Getter
    @Setter
    private Long orgId;
    /*
   * 学院名称
   */
    @Column(name = "COLLEGE_NAME")
    @Getter @Setter private String collegeName;
    /*
    * 学院id
    */
    @Column(name = "COLLEGE_ID")
    @Getter @Setter private Long collegeId;

    /*
    * 专业名称
    */
    @Column(name = "PROFESSION_NAME")
    @Getter @Setter private String professionName;
    /*
    * 专业id
    */
    @Column(name = "PROFESSION_ID")
    @Getter @Setter private Long professionId;

    /*
    * 班级名称
    */
    @Column(name = "CLASS_NAME")
    @Getter @Setter private String className;
    /*
    * 班级id
    */
    @Column(name = "CLASS_ID")
    @Getter @Setter private Long classId;
    /*
     * 实践人数
     */
    @Column(name = "PRACTICE_STUDENT_NUM")
    @Getter @Setter private Long practiceStudentNum;
    /*
    * 实践企业数
    */
    @Column(name = "PRACTICE_COMPANY_NUM")
    @Getter @Setter private Long practiceCompanyNum;
    /*
     * 实践任务数
     */
    @Column(name = "TASK_NUM")
    @Getter @Setter private Long taskNum;
    /*
    * 任务通过数
    */
    @Column(name = "TASK_PASS_NUM")
    @Getter @Setter private Long taskPassNum;

    /*
    * 统计类型
    */
    @Column(name = "STATISTICS_TYPE")
    @Getter @Setter private String statisticsType;


    /*
  * 学期
  */
    @Column(name = "SEMESTER")
    @Getter @Setter private Integer semester;

    /*
     * 学年
     */
    @Column(name = "TEACHER_YEAR")
    @Getter @Setter private Integer teacherYear;
    /**
     * 年级
     */
    @Column(name = "GRADE")
    @Getter @Setter private Integer grade;
    /*
    * 统计时间
    */
    @ApiModelProperty(value = "统计时间")
    @CreatedDate
    @Column(name = "STATISTICAL_TIME")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @Temporal(TemporalType.TIMESTAMP)
    @Getter @Setter protected Date statisticalTime = new Date();
}
