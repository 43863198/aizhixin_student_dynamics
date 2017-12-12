package com.aizhixin.cloud.dataanalysis.analysis.constant;

/**
 * @author: Created by jianwei.wu
 * @E-mail: wujianwei@aizhixin.com
 * @Date: 2017-12-12
 */
public enum TeachingScoreTrendType {

    STUDENT_NUM("学生人数",1),
    AVG_GPA("平均GPA",2),
    FAIL_PASS_STU_NUM("不及格人数",3),
    AVG_SCORE("课程平均分",4);

    // 成员变量
    private String name;
    private int index;

    // 构造方法
    private TeachingScoreTrendType(String name, int index) {
        this.name = name;
        this.index = index;
    }

    // 获取类型名称方法
    public static String getName(int index) {
        for (TeachingScoreTrendType c : TeachingScoreTrendType.values()) {
            if (c.getIndex() == index) {
                return c.name;
            }
        }
        return null;
    }

    // 获取类型方法
    public static String getType(int index) {
        for (TeachingScoreTrendType c : TeachingScoreTrendType.values()) {
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
