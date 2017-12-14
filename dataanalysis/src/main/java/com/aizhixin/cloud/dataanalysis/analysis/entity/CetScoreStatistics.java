package com.aizhixin.cloud.dataanalysis.analysis.entity;

import com.aizhixin.cloud.dataanalysis.common.entity.AbstractEntity;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "T_CET_STATISTICS")
@ToString
public class CetScoreStatistics  extends AbstractEntity {
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
    /*
    * 四级参加人数
    */
    @Column(name = "CET_FORE_JOIN_NUM")
    @Getter @Setter private Integer cetForeJoinNum;
    /*
    * 四级通过人数
    */
    @Column(name = "CET_FORE_PASS_NUM")
    @Getter @Setter private Integer cetForePassNum;
    /*
    * 六级参加人数
    */
    @Column(name = "CET_SIX_JOIN_NUM")
    @Getter @Setter private Integer cetSixJoinNum;
    /*
    * 六级通过人数
    */
    @Column(name = "CET_SIX_PASS_NUM")
<<<<<<< HEAD
    @Getter @Setter private Integer cetSixPassNum;
=======
    @Getter @Setter private Long cetSixPassNum;

>>>>>>> afb1d0e1143a5e9bf128bf98a98f0fbfb39ad76f
    /*
    * 学期
    */
    @Column(name = "SEMESTER")
<<<<<<< HEAD
    @Getter @Setter private Integer semester;
=======
    @Getter @Setter private int semester;

>>>>>>> afb1d0e1143a5e9bf128bf98a98f0fbfb39ad76f
    /*
     * 学年
     */
    @Column(name = "TEACHER_YEAR")
<<<<<<< HEAD
    @Getter @Setter private String teacherYear;
    /*
    * 统计类型
    */
    @Column(name = "STATISTICS_TYPE")
    @Getter @Setter private Integer statisticsType;
    /*
  * 年级
  */
    @Column(name = "GRADE")
    @Getter @Setter private Integer grade;
=======
    @Getter @Setter private int teacherYear;
    /**
     * 年级
     */
    @Column(name = "GRADE")
    @Getter @Setter private int grade;
>>>>>>> afb1d0e1143a5e9bf128bf98a98f0fbfb39ad76f
}
