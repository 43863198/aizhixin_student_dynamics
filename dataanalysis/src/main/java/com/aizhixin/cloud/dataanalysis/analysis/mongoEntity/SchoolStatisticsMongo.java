package com.aizhixin.cloud.dataanalysis.analysis.mongoEntity;

import com.aizhixin.cloud.dataanalysis.common.entity.AbstractEntity;
import com.fasterxml.jackson.annotation.JsonFormat;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;

import java.util.Date;


@Document(collection="SchoolStatistics")
@Data
public class SchoolStatisticsMongo extends AbstractEntity {
    /*
     * 组织id
     */
   private Long orgId;
    /*
    * 学院名称
    */
 private String colloegeName;
    /*
    * 学院id
    */
 private Long colloegeId;
    /*
     * 新生人数
     */
 private int newStudentsCount;
    /*
     * 已报到人数
     */
  private int alreadyReport;

    /*
     * 已完成缴费人数
     */
 private int alreadyPay;

    /*
    * 便利通道人数
    */
 private int convenienceChannel;

    /*
     * 教师人数
     */
 private int teacherNumber;

    /*
     * 学生人数
     */
 private int studentNumber;

    /*
     * 辅导员人数
     */
 private int InstructorNumber;

    /*
     * 准毕业人数
     */
 private int readyGraduation;

    /*
     * 学年
     */
 private String teacherYear;

    /*
     * 统计时间
     */
 private Date statisticalTime = new Date();

}
