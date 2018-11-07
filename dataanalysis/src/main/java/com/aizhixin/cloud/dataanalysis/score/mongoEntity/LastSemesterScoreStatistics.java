package com.aizhixin.cloud.dataanalysis.score.mongoEntity;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Set;

/**
 * @author: Created by jianwei.wu
 * @E-mail: wujianwei@aizhixin.com
 * @Date: 2018-06-22
 */
@Document(collection="LastSemesterScoreStatistics")
@Data
public class LastSemesterScoreStatistics {
    @Id
    private String id;

    /**
     * 机构id
     */
    @Indexed
    private Long orgId;
    /**
     * 工号/学号
     */
    private String jobNum;
    /**
     * 姓名
     */
    private String userName;
    /**
     * 移动电话
     */
    private String phone;

    /**
     * 照片
     */
    private String userPhoto;

    /**
     * 班号
     */
    private String classCode;
    /**
     * 班级名称
     */
    private String className;

    /**
     * 专业code
     */
    private String professionalCode;

    /**
     * 专业名称
     */
    private String professionalName;

    /**
     * 学院code
     */
    private String collegeCode;

    /**
     * 学院名称
     */
    private String collegeName;
    /**
     * 年级
     */
    private String grade;

    @ApiModelProperty(value = "学期", required = false)
    private String semester;

    @ApiModelProperty(value = "学年", required = false)
    private String teachYear;

    @ApiModelProperty(value = "上学期", required = false)
    private String lastSemester;

    @ApiModelProperty(value = "上学年", required = false)
    private String lastSchoolYear;

    /**
     * 用户手机号
     */
    private  String userPhone;

    /**
     * 数据源
     */
    private String dataSource;

    /**
     * 不及格数据源
     */
    private String dataSource2;

    /**
     * 不及格必修课程数
     */
    private int failRequiredCourseNum;

    /**
     * 补考后不及格必修课程数
     */
    private int makeUpFailRequiredCourseNum;

    /**
     * 不及格必修课程号
     */
    private Set<String> scheduleCodeList;

    /**
     * 不及格必修课程学分总计
     */
    private float requireCreditCount;

}
