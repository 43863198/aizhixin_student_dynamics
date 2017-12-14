package com.aizhixin.cloud.dataanalysis.analysis.entity;

import com.aizhixin.cloud.dataanalysis.common.entity.AbstractEntity;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.mongodb.core.index.Indexed;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * @author: Created by jianwei.wu
 * @E-mail: wujianwei@aizhixin.com
 * @Date: 2017-12-11
 */
@Entity
@Table(name = "T_TEACHING_SCORE_DETAILS")
@ToString
public class TeachingScoreDetails extends AbstractEntity {
    /**
     * 机构id
     */
    @Column(name = "ORG_ID")
    @Getter
    @Setter
    private Long orgId;
    /**
     * 工号/学号
     */
    @Column(name = "JOB_NUM")
    @Getter
    @Setter
    private String jobNum;
    /**
     * 用户id
     */
    @Column(name = "USER_ID")
    @Getter
    @Setter
    private Long userId;
    /**
     * 姓名
     */
    @Column(name = "USER_NAME")
    @Getter
    @Setter
    private String userName;
    /**
     * 班级id
     */
    @Column(name = "CLASS_ID")
    @Getter
    @Setter
    private Long classId;
    /**
     * 班级名称
     */
    @Column(name = "CLASS_NAME")
    @Getter
    @Setter
    private String className;
    /**
     * 专业id
     */
    @Column(name = "PROFESSIONAL_ID")
    @Getter
    @Setter
    private Long professionalId;
    /**
     * 专业名称
     */
    @Column(name = "PROFESSIONAL_NAME")
    @Getter
    @Setter
    private String professionalName;

    /**
     * 学院id
     */
    @Column(name = "COLLEGE_ID")
    @Getter
    @Setter
    private Long collegeId;
    /**
     * 学院名称
     */
    @Column(name = "COLLEGE_NAME")
    @Getter
    @Setter
    private String collegeName;
    /**
     * 学年
     */
    @Column(name = "TEACHER_YEAR")
    @Getter
    @Setter
    private Integer teacherYear;
    /**
     * 学期
     */
    @Column(name = "SEMESTER")
    @Getter
    @Setter
    private Integer semester;
    /**
     * 平均绩点
     */
    @Column(name = "AVERAGE_GPA")
    @Getter @Setter private Float averageGPA;
    /*
    * 参考科目数
    */
    @Column(name = "REFERENCE_SUBJECTS")
    @Getter @Setter private Integer referenceSubjects;
    /*
     * 不及格科目
     */
    @Column(name = "FAILED_SUBJECTS")
    @Getter @Setter private Integer failedSubjects;
    /*
     * 不及格科目学分
     */
    @Column(name = "FAILING_GRADE_CREDITS")
    @Getter @Setter private Float failingGradeCredits;
    /*
     *年级
     */
    @Column(name = "GRADE")
    @Getter @Setter private Integer grade;

}
