package com.aizhixin.cloud.dataanalysis.analysis.constant;

/**
 * @author: Created by jianwei.wu
 * @E-mail: wujianwei@aizhixin.com
 * @Date: 2017-11-30
 */

public enum TrendType {
        NEW_STUDENTS_COUNT("录取数",1),
        ALREADY_REPORT("报到数",2),
        ALREADY_PAY("缴费人数",3),
        CONVENIENCE_CHANNEL("绿色通道人数",4),
        UNREPORT("未报到数",5),
        PAY_PROPORTION("缴费人占比",6),
        REPORT_PROPORTION("报到率",7),
        CHANNEL_PROPORTION("绿色通道人数占比",8);

        // 成员变量
        private String name;
        private int index;

        // 构造方法
        private TrendType(String name, int index) {
            this.name = name;
            this.index = index;
        }

        // 获取类型名称方法
        public static String getName(int index) {
            for (TrendType c : TrendType.values()) {
                if (c.getIndex() == index) {
                    return c.name;
                }
            }
            return null;
        }

        // 获取类型方法
        public static String getType(int index) {
            for (TrendType c : TrendType.values()) {
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
//  class  test{
//    public static void main(String[] args) {
//        System.out.println(TrendType.getName(1));
//        System.out.println(TrendType.ALREADY_PAY.getIndex());
//        System.out.println(TrendType.ALREADY_PAY.getName());
//        for(Object ss :TrendType.values()) {
//            System.out.println(ss);
//        }
//        System.out.println("@@@@@@@@@@");
//        System.out.println(TrendType.getType(1));
//
//    }
//}


