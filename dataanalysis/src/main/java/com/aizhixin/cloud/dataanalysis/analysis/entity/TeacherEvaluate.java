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
    private Float avgScore;
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
