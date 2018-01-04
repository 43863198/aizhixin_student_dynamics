package com.aizhixin.cloud.dataanalysis.common.constant;

/**
 * @author: Created by jianwei.wu
 * @E-mail: wujianwei@aizhixin.com
 * @Date: 2018-01-04
 */
public enum OperationType {
    t_school_statistics("",1),
    t_teaching_score_statistics("教学成绩",3),
    t_teacher_evaluate("教师评价",4),
    t_course_evaluate("课程评价",5),
    t_practice_statistics("实践评价",6);

    // 成员变量
    private String name;
    private int index;

    // 构造方法
    private OperationType(String name, int index) {
        this.name = name;
        this.index = index;
    }

    // 获取类型名称方法
    public static String getName(int index) {
        for (OperationType c : OperationType.values()) {
            if (c.getIndex() == index) {
                return c.name;
            }
        }
        return null;
    }

    // 获取类型方法
    public static String getType(int index) {
        for (OperationType c : OperationType.values()) {
            if (c.getIndex() == index) {
                return c.toString();
            }
        }
        return null;
    }

    // get set 方法
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }
}
