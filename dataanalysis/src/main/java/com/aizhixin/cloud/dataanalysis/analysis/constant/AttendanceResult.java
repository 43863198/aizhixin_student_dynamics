package com.aizhixin.cloud.dataanalysis.analysis.constant;

public class AttendanceResult {
    /**
     * 考勤结果
     * 1:已到
     * 2:旷课
     * 3:迟到
     * 4:请假
     * 5:早退
     * 6:已提交
     * 7:未提交
     * 8:超出设定范围
     * 9:取消本次考勤
     */

    public static final String ARRIVED = "1";
    public static final String ABSENTEE = "2";
    public static final String LATE = "3";
    public static final String LEAVE = "4";
    public static final String LEAVE_EARLY = "5";
    public static final String SUBMITTED = "6";
    public static final String UNSUBMITTED = "7";
    public static final String OUTRANGE = "8";
    public static final String CANCEL = "9";
}
