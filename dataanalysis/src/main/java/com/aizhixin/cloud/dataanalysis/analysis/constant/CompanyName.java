package com.aizhixin.cloud.dataanalysis.analysis.constant;

public enum CompanyName {
	BAIDU("百度",1),
	ALI("阿里巴巴",2),
	TX("腾讯",3),
	BMW("宝马",4),
	MB("奔驰",5);

    // 成员变量
    private String name;
    private int index;

    // 构造方法
    private CompanyName(String name, int index) {
        this.name = name;
        this.index = index;
    }

    // 获取类型名称方法
    public static String getName(int index) {
        for (CompanyName c : CompanyName.values()) {
            if (c.getIndex() == index) {
                return c.name;
            }
        }
        return null;
    }

    // 获取类型方法
    public static String getType(int index) {
        for (CompanyName c : CompanyName.values()) {
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

