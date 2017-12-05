package com.aizhixin.cloud.dataanalysis.common.util;

import lombok.ToString;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.NumberFormat;

/**
 * @author: Created by jianwei.wu
 * @E-mail: wujianwei@aizhixin.com
 * @Date: 2017-11-30
 */
public abstract class  ProportionUtil  {

    public  static String accuracy(double num, double total, int scale){
        DecimalFormat df;
//        df = (DecimalFormat) NumberFormat.getInstance();
        df   = new DecimalFormat("######0.00");
        //可以设置精确几位小数
        df.setMaximumFractionDigits(scale);
        //模式 例如四舍五入
        df.setRoundingMode(RoundingMode.HALF_UP);
        double accuracy_num = num / total * 100;
        return df.format(accuracy_num);
    }
}
