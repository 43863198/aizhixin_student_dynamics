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

/**
 * @author: Created by jianwei.wu
 * @E-mail: wujianwei@aizhixin.com
 * @Date: 2017-11-29
 */
@Entity
@Table(name = "T_SCHOOL_STATISTICS")
@ToString
public class SchoolStatistics extends AbstractEntity {
    /*
     * 组织id
     */
    @Column(name = "ORG_ID")
    @Getter @Setter private Long orgId;
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
     * 新生人数
     */
    @Column(name = "NEW_STUDENTS_COUNT")
    @Getter @Setter private Integer newStudentsCount;
    /*
     * 已报到人数
     */
    @Column(name = "ALREADY_REPORT")
    @Getter @Setter private Integer alreadyReport;

    /*
     * 已完成缴费人数
     */
    @Column(name = "ALREADY_PAY")
    @Getter @Setter private Integer alreadyPay;

    /*
    * 便利通道人数
    */
    @Column(name = "CONVENIENCE_CHANNEL")
    @Getter @Setter private Integer convenienceChannel;

    /*
     * 教师人数
     */
    @Column(name = "TEACHER_NUMBER")
    @Getter @Setter private Integer teacherNumber;

    /*
     * 学生人数
     */
    @Column(name = "STUDENT_NUMBER")
    @Getter @Setter private int studentNumber;

    /*
     * 辅导员人数
     */
    @Column(name = "INSTRUCTOR_NUMBER")
    @Getter @Setter private Integer InstructorNumber;

    /*
     * 准毕业人数
     */
    @Column(name = "READY_GRADUATION")
    @Getter @Setter private int readyGraduation;
    /*
     * 学年
     */
    @Column(name = "TEACHER_YEAR")
    @Getter @Setter private Integer teacherYear;
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
    /*
    * 学期
    */
    @Column(name = "SEMESTER")
    @Getter @Setter private Integer semester;

    /*
    * 年级
    */
    @Column(name = "GRADE")
    @Getter @Setter private Integer grade;


}
