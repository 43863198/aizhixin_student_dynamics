package com.aizhixin.cloud.dataanalysis.bz.service;

import com.aizhixin.cloud.dataanalysis.analysis.vo.CetDetailVO;
import com.aizhixin.cloud.dataanalysis.bz.entity.CetStandardScore;
import com.aizhixin.cloud.dataanalysis.bz.manager.CetStandardScoreManager;
import com.aizhixin.cloud.dataanalysis.bz.vo.CetTopVo;
import com.aizhixin.cloud.dataanalysis.common.PageData;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
public class CetStandardScoreService {

    @Autowired
    private CetStandardScoreManager cetStandardScoreManager;


    public List<CetStandardScore> getCetScore(Long orgId, String jobNum, Integer pageNumber, Integer pageSize) {

        return cetStandardScoreManager.getCetScore(orgId, jobNum, pageNumber, pageSize);
    }

    public List<CetTopVo> getTop(Long orgId, String cetType, String teacherYear) {
        int index = teacherYear.lastIndexOf("-");
        String t = null;
        String s = null;
        if(index > 0){
             t = teacherYear.substring(0,index);
             s = teacherYear.substring(index+1);
        }
        return cetStandardScoreManager.getTop(orgId, cetType, t,s);
    }

    /**
     * 查看数据详情
     */
    public PageData<CetDetailVO> getDetailList(Long orgId, String cetType, String collegeCode, String professionCode, String className, Integer scoreSeg, Integer pageNumber, Integer pageSize) {
        return cetStandardScoreManager.getDetailList(orgId, cetType, collegeCode, professionCode, className, scoreSeg, pageNumber, pageSize);
    }
}
