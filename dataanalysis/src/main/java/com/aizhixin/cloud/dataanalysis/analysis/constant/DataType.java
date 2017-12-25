package com.aizhixin.cloud.dataanalysis.analysis.constant;

/**
 * @author: Created by jianwei.wu
 * @E-mail: wujianwei@aizhixin.com
 * @Date: 2017-12-25
 */
public enum DataType {
    cet("英语四六级",1),
    StudentRegister("学生注册",2),
    TeachingScore("教学成绩",3);

    // 成员变量
    private String name;
    private int index;

    // 构造方法
    private DataType(String name, int index) {
        this.name = name;
        this.index = index;
    }

    // 获取类型名称方法
    public static String getName(int index) {
        for (DataType c : DataType.values()) {
            if (c.getIndex() == index) {
                return c.name;
            }
        }
        return null;
    }

    // 获取类型方法
    public static String getType(int index) {
        for (DataType c : DataType.values()) {
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
