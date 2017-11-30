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
 * @Date: 2017-11-29
 */
@Entity
@Table(name = "T_SCHOOL_STATISTICS")
@ToString
public class SchoolStatistics extends AbstractEntity {
    /*
    * 学院名称
    */
    @Column(name = "COLLOEGE_NAME")
    @Getter @Setter private String colloegeName;
    /*
    * 学院id
    */
    @Column(name = "COLLOEGE_ID")
    @Getter @Setter private Long colloegeId;
    /*
     * 新生人数
     */
    @Column(name = "NEW_STUDENTS_COUNT")
    @Getter @Setter private int newStudentsCount;
    /*
     * 已报到人数
     */
    @Column(name = "ALREADY_REPORT")
    @Getter @Setter private int alreadyReport;

    /*
     * 已完成缴费人数
     */
    @Column(name = "ALREADY_PAY")
    @Getter @Setter private int alreadyPay;

    /*
    * 便利通道人数
    */
    @Column(name = "CONVENIENCE_CHANNEL")
    @Getter @Setter private int convenienceChannel;

    /*
     * 教师人数
     */
    @Column(name = "TEACHER_NUMBER")
    @Getter @Setter private int teacherNumber;

    /*
     * 学生人数
     */
    @Column(name = "STUDENT_NUMBER")
    @Getter @Setter private int studentNumber;

    /*
     * 辅导员人数
     */
    @Column(name = "INSTRUCTOR_NUMBER")
    @Getter @Setter private int InstructorNumber;

    /*
     * 准毕业人数
     */
    @Column(name = "READY_GRADUATION")
    @Getter @Setter private int readyGraduation;

    /*
     * 学年
     */
    @Column(name = "TEACHER_YEAR")
    @Getter @Setter private String teacherYear;

}
