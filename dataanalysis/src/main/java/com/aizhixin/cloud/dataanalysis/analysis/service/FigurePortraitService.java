package com.aizhixin.cloud.dataanalysis.analysis.service;

import com.aizhixin.cloud.dataanalysis.analysis.vo.StudentFigurePortraitVO;
import com.aizhixin.cloud.dataanalysis.analysis.vo.StudentInfoVO;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author: Created by jianwei.wu
 * @E-mail: wujianwei@aizhixin.com
 * @Date: 2018-05-11
 */
@Service
public class FigurePortraitService {

    public Map<String, Object> getStudentFigurePortrait(Long orgId,String collegeNumber,String professionNumber,String classNumber){
        Map<String, Object> result = new HashMap<>();
        Map<String, Object> condition = new HashMap<>();
        StudentFigurePortraitVO studentFigurePortrait = new StudentFigurePortraitVO();
        try {





            result.put("success", true);
            result.put("data", studentFigurePortrait);
            return result;
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", "学生画像---学生列表获取失败！");
            return result;
        }
    }



}
