package com.aizhixin.cloud.dataanalysis.analysis.dto;

import lombok.Data;

/**
 * @author: Created by jianwei.wu
 * @E-mail: wujianwei@aizhixin.com
 * @Date: 2018-05-06
 */
@Data
public class TeacherYearSemesterDTO {
    private String teacherYear;
    private String semester;
    private String startTime;
    private int week;
    private String endTime;
}
