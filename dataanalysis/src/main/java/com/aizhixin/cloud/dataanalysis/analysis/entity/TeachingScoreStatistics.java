package com.aizhixin.cloud.dataanalysis.analysis.entity;

import com.aizhixin.cloud.dataanalysis.common.entity.AbstractEntity;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "T_TEACHING_SCORE_STATISTICS")
@ToString
public class TeachingScoreStatistics extends AbstractEntity {
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
    @Column(name = "COLLOEGE_NAME")
    @Getter @Setter private String colloegeName;
    /*
    * 学院id
    */
    @Column(name = "COLLOEGE_ID")
    @Getter @Setter private Long colloegeId;
    /**
     * 学生人数
     */
    @Column(name = "STUDENT_NUM")
    @Getter @Setter private int studentNum;
    /**
     * 不及格人数
     */
    @Column(name = "FAIL_PASS_STU_NUM")
    @Getter @Setter private int failPassStuNum;
    /*
     * 开设课程数量
     */
    @Column(name = "COURSES_NUMBER")
    @Getter @Setter private int coursesNum;
    /**
     *平均GPA
     */
    @Column(name = "AVG_GPA")
    @Getter @Setter private float avgGPA;
    /**
     *课程平均分
     */
    @Column(name = "AVG_SCORE")
    @Getter @Setter private float avgScore;
    /*
     * 学期名称
     */
    @Column(name = "SEMESTER_NAME")
    @Getter @Setter private String semesterName;
    /*
    * 学期
    */
    @Column(name = "SEMESTER")
    @Getter @Setter private int semester;
    /*
    * 学年
    */
    @Column(name = "TEACHER_YEAR")
    @Getter @Setter private String teacherYear;
    /*
   * 数据状态
   */
    @Column(name = "STATE")
    @Getter @Setter private int state;
}
