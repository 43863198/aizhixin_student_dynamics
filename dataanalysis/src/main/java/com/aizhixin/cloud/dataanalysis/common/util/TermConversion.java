package com.aizhixin.cloud.dataanalysis.common.util;

import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * @author: Created by jianwei.wu
 * @E-mail: wujianwei@aizhixin.com
 * @Date: 2018-01-09
 */

@Component
public class TermConversion {

    /**
     * 学年学期转春秋季
     * @param schoolYear
     * @param semester
     * @param change 往后几个学期
     * @return
     */
    public Map<String,Object> getSemester(int schoolYear, int semester, int change) {
        Map<String,Object> result = new HashMap<>();
        int resultYear = 0;
        int resultSemester = 0;
        int changes = semester - change ;
        int add = Math.abs(changes);
        if (add > 0) {
            int yearChange = 0;
            if(changes<0) {
                 yearChange = (int) Math.ceil((float) add / 2);
            }
            int seme = change % 2;
            if (seme == 0) {
                resultSemester = semester;
            } else {
                if (semester == 1) {
                    resultSemester = 2;
                } else {
                    resultSemester = 1;
                }
                if (yearChange == 0) {
                    resultYear = schoolYear;
                } else {
                    resultYear = schoolYear - yearChange;
                }
            }
        }
        if (add == 0) {
            if (semester == 1) {
                resultSemester = 2;
            } else {
                resultSemester = 2;
            }
            resultYear = schoolYear-1;
        }
        result.put("schoolYear",resultYear);
        result.put("semester",getSemester(resultSemester));
        return result;
    }

    public String getSemester(int semester){
        if(semester==1){
            return "春季";
        }else if(semester==2){
            return "秋季";
        }else {
            return "";
        }
    }

}
