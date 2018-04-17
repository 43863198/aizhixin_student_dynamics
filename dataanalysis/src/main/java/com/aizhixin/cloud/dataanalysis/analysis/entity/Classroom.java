package com.aizhixin.cloud.dataanalysis.analysis.entity;

import com.aizhixin.cloud.dataanalysis.common.constant.DataValidity;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.io.Serializable;

/**
 * @author: Created by jianwei.wu
 * @E-mail: wujianwei@aizhixin.com
 * @Date: 2018-04-16
 */
@Entity
@Table(name = "T_CLASS_ROOM")
@ToString
public class ClassRoom implements Serializable {

    @ApiModelProperty(value = "ID")
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "ID")
    @Getter
    @Setter
    private Long id;
    /* 学校id*/
    @Column(name = "ORG_ID") @Getter @Setter private Long orgId;
    /*校区号*/
    @Column(name = "SCHOOL_AREA_NUMBER")
    @Getter @Setter private String SchoolAreaNumber;
    /*教学楼号*/
    @Column(name = "TEACHING_BUILDING_NUMBER")
    @Getter @Setter private String teachingBuildingNumber;
    /*楼层*/
    @Column(name = "FLOOR")
    @Getter @Setter private int floor;
    /*座位数*/
    @Column(name = "NUMBER_OF_SEATS")
    @Getter @Setter private int numberOfSeats;
    /*教室类型码*/
    @Column(name = "CLASSROOM_TYPE_CODE")
    @Getter @Setter private String classroomTypeCode;
    /*考试座位数*/
    @Column(name = "NUMBER_OF_EXAM_SEATS")
    @Getter @Setter private int NumberOfExamSeats;
    /*教室管理部门*/
    @Column(name = "CLASSROOM_MANAGEMENT_DEPARTMENT")
    @Getter @Setter private String classroomManagementDepartment;
    /*教室号*/
    @Column(name = "CLASSROOM_NUMBER")
    @Getter @Setter private String classroomNumber;
    /*教室名称*/
    @Column(name = "CLASSROOM_NAME")
    @Getter @Setter private String classroomName;
    /*是否可用*/
    @Column(name = "NORMAL")
    @Getter @Setter private String normal;
    @ApiModelProperty(value = "是否删除标志")
    @Column(name = "DELETE_FLAG")
    @Getter @Setter protected Integer deleteFlag = DataValidity.VALID.getState();

}
