package com.aizhixin.cloud.dataanalysis.common.constant;

public enum WarningLevelConstant {
    RedWarning("红色预警",1),
    OrangeWarning("橙色预警",2),
    YellowWarning("黄色预警",3);
    // 成员变量
    private String name;
    private int index;
    // 构造方法
    private WarningLevelConstant(String name, int index) {
        this.name = name;
        this.index = index;
    }

    // 获取类型名称方法
    public static String getName(int index) {
        for (WarningLevelConstant c : WarningLevelConstant.values()) {
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

