package com.aizhixin.cloud.dataanalysis.analysis.entity;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.io.Serializable;

/**
 * @author: Created by jianwei.wu
 * @E-mail: wujianwei@aizhixin.com
 * @Date: 2018-04-25
 */
@Entity
@Table(name = "T_COURSE_TIMETABLE")
@ToString
public class CourseTimetable implements Serializable {
    @ApiModelProperty(value = "ID")
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "ID")
    @Getter
    @Setter
    private Long id;
    /*
     * 学校id
     */
    @Column(name = "ORG_ID")
    @Getter
    @Setter
    private Long orgId;
    /*
    * 排课号
    */
    @Column(name = "COURSE_TABLE_NUMBER")
    @Getter
    @Setter
    private String courseTableNumber;
    /*
     * 教学班编号
     */
    @Column(name = "TEACHING_CLASS_NUMBER")
    @Getter
    @Setter
    private String teachingClassNumber;
    /*
     * 开课学年
     */
    @Column(name = "SCHOOL_YEAR")
    @Getter
    @Setter
    private String schoolYear;
    /*
     * 开课学期码
     */
    @Column(name = "SEMESTER_NUMBER")
    @Getter
    @Setter
    private String SemesterNumber;
    /*
     * 开课时间
     */
    @Column(name = "CURRICULA_TIME")
    @Getter
    @Setter
    private String curriculaTime;
    /*
     * 上课地点
     */
    @Column(name = "PLACE")
    @Getter
    @Setter
    private String place;
    /*
     * 课容量
     */
    @Column(name = "CAPACITY")
    @Getter
    @Setter
    private int capacity;
    /*
     * 教室所在校区号
     */
    @Column(name = "CAMPUS")
    @Getter
    @Setter
    private String campus;
    /*
     * 选课人数限定
     */
    @Column(name = "NUMBER_LIMIT")
    @Getter
    @Setter
    private int numberLimit;
    /*
     * 排课要求
     */
    @Column(name = "REQUIRE")
    @Getter
    @Setter
    private String require;
    /*
     * 教室类型码
     */
    @Column(name = "CLASS_ROOM_TYPE")
    @Getter
    @Setter
    private String classRoomType;
    /*
     * 上课周次
     */
    @Column(name = "WEEKLY")
    @Getter
    @Setter
    private String weekly;
    /*
     * 教师姓名
     */
    @Column(name = "TEACHER_NAME")
    @Getter
    @Setter
    private String teacherName;
    /*
     * 课程性质
     */
    @Column(name = "COURSE_NATURE")
    @Getter
    @Setter
    private String courseNature;
    /*
    * 上课班级号：多个班级“,”分隔
    */
    @Column(name = "CLASSES")
    @Getter
    @Setter
    private String classes;
    /*
    * 上课班级名称
    */
    @Column(name = "CLASS_NAME")
    @Getter
    @Setter
    private String className;
    /*
     * 课程属性码
     */
    @Column(name = "COURSE_PROPERTIES")
    @Getter
    @Setter
    private String courseProperties;
    /*
     * 课程开设单位号
     */
    @Column(name = "SET_UP_UNIT")
    @Getter
    @Setter
    private String setUpUnit;
    /*
     * 教师工号
     */
    @Column(name = "TEACHER_JOB_NUMBER")
    @Getter
    @Setter
    private String teacherJobNumber;
    /*
     * 教学班名称
     */
    @Column(name = "TEACHING_CLASS_NAME")
    @Getter
    @Setter
    private String teachingClassName;

}
