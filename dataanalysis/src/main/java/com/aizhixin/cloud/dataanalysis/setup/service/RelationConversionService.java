package com.aizhixin.cloud.dataanalysis.setup.service;

import lombok.extern.apachecommons.CommonsLog;
import org.springframework.stereotype.Component;

/**
 * @author: Created by jianwei.wu
 * @E-mail: wujianwei@aizhixin.com
 * @Date: 2018-01-04
 */
@Component
public class RelationConversionService {

    public String getRelation(String str){
        String[] relation = {"=",">=",">","<=","<"};
        if(str.equals("＝")){
           return relation[0];
        }else if(str.equals("≥")){
           return relation[1];
        }else if(str.equals("＞")){
            return relation[2];
        }else if(str.equals("≤")){
            return relation[3];
        }else if(str.equals("＜")){
            return relation[4];
        }else {
            return null;
        }
    }



}
