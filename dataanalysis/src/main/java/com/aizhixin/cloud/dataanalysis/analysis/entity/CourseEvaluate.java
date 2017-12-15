package com.aizhixin.cloud.dataanalysis.analysis.entity;

import com.aizhixin.cloud.dataanalysis.common.entity.AbstractEntity;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "T_COURSE_EVALUATE")
@ToString
public class CourseEvaluate extends AbstractEntity {
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
   * 课程编号
   */
    @Column(name = "COURSE_CODE")
    @Getter
    @Setter
    private String courseCode;
    /*
    * 课程名称
    */
    @Column(name = "COURSE_NAME")
    @Getter
    @Setter
    private String courseName;
    /*
    * 平均分
    */
    @Column(name = "AVG_SCORE")
    @Getter
    @Setter
    private Float avgScore;
    /*
   * 开课单位
   */
    @Column(name = "COURSE_OPEN_DEPARTMENT")
    @Getter
    @Setter
    private String courseOpenDepartment;
    /*
    * 开课单位ID
    *
    */
    @Column(name = "COURSE_OPEN_ID")
    @Getter
    @Setter
    private String courseOpenID;
    /*
    * 开课教师
    *
    */
    @Column(name = "CHARGE_PERSON")
    @Getter
    @Setter
    private String chargePerson;
    /*
    * 开课教师ID
    *
    */
    @Column(name = "CHARGE_PERSON_ID")
    @Getter
    @Setter
    private String chargePersonID;

    /*
      * 教学班id
      */
    @Column(name = "TEACHING_CLASS_ID")
    @Getter
    @Setter
    private String teachingClassId;
    /*
    * 课程名称
    */
    @Column(name = "TEACHING_CLASS_NAME")
    @Getter
    @Setter
    private String teachingClassName;
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
}
