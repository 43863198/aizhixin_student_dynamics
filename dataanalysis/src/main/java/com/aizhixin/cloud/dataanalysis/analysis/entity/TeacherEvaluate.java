package com.aizhixin.cloud.dataanalysis.analysis.entity;

import com.aizhixin.cloud.dataanalysis.common.entity.AbstractEntity;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "T_TEACHER_EVALUATE")
@ToString
public class TeacherEvaluate extends AbstractEntity {
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
   * 教师id
   */
    @Column(name = "TEACHER_ID")
    @Getter
    @Setter
    private String teacherId;
    /*
    * 教师名称
    */
    @Column(name = "TEACHER_NAME")
    @Getter
    @Setter
    private String teacherName;
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
    private float avgScore;

    /*
    * 学期名称
    */
    @Column(name = "SEMESTER_NAME")
    @Getter @Setter private String semesterName;
    /*
    * 学期id
    */
    @Column(name = "SEMESTER_ID")
    @Getter @Setter private Long semesterId;
    /*
    * 学年
    */
    @Column(name = "TEACHER_YEAR")
    @Getter @Setter private int teacherYear;
    /*
 * 班级id
 */
    @Column(name = "CLASS_ID")
    @Getter
    @Setter
    private String classId;
    /*
    * 课程名称
    */
    @Column(name = "CLASS_NAME")
    @Getter
    @Setter
    private String className;
}
