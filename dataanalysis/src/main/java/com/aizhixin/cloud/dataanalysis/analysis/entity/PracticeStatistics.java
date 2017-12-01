package com.aizhixin.cloud.dataanalysis.analysis.entity;

import com.aizhixin.cloud.dataanalysis.common.entity.AbstractEntity;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "T_PRACTICE_STATISTICS")
@ToString
public class PracticeStatistics extends AbstractEntity {
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
    * 专业名称
    */
    @Column(name = "PROFESSION_NAME")
    @Getter @Setter private String professionName;
    /*
    * 专业id
    */
    @Column(name = "PROFESSION_ID")
    @Getter @Setter private Long professionId;

    /*
    * 班级名称
    */
    @Column(name = "CLASS_NAME")
    @Getter @Setter private String className;
    /*
    * 班级id
    */
    @Column(name = "CLASS_ID")
    @Getter @Setter private Long classId;

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
* 实践人数
*/
    @Column(name = "PRACTICE_STUDENT_NUM")
    @Getter @Setter private Long practiceStudentNum;
    /*
    * 实践企业数
    */
    @Column(name = "PRACTICE_COMPANY_NUM")
    @Getter @Setter private Long practiceCompanyNum;
    /*
* 实践任务数
*/
    @Column(name = "TASK_NUM")
    @Getter @Setter private Long taskNum;
    /*
    * 任务通过数
    */
    @Column(name = "TASK_PASS_NUM")
    @Getter @Setter private Long taskPassNum;
    /*
* 实践任务数
*/
    @Column(name = "STATE")
    @Getter @Setter private int state;
    /*
    * 任务通过数
    */
    @Column(name = "STATISTICS_TYPE")
    @Getter @Setter private int statisticsType;
}
