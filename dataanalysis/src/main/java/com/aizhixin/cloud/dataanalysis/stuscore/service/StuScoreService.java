package com.aizhixin.cloud.dataanalysis.stuscore.service;

import com.aizhixin.cloud.dataanalysis.common.PageData;
import com.aizhixin.cloud.dataanalysis.feign.OrgManagerFeignService;
import com.aizhixin.cloud.dataanalysis.stuscore.JdbcTemplate.CetScoreJdbc;
import com.aizhixin.cloud.dataanalysis.stuscore.JdbcTemplate.CourseScoreJdbc;
import com.aizhixin.cloud.dataanalysis.stuscore.domain.CetScoreDomain;
import com.aizhixin.cloud.dataanalysis.stuscore.domain.CourseScoreDomain;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class StuScoreService {
    @Autowired
    private CourseScoreJdbc courseScoreJdbc;
    @Autowired
    private CetScoreJdbc cetScoreJdbc;
    @Autowired
    private OrgManagerFeignService orgManagerFeignService;

    public PageData<CourseScoreDomain> getCourseScore(Long orgId, Long userId, String jobNum, String courseName, Integer pageNumber, Integer pageSize) {
        if (StringUtils.isEmpty(jobNum)) {
            if (userId == null || userId < 1) {
                PageData result = new PageData();
                result.setSuccess(false);
                result.setMessage("userId不能为空!");
                return result;
            } else {
                Map user = orgManagerFeignService.getUser(userId);
                if (user != null && user.get("jobNumber") != null) {
                    jobNum = user.get("jobNumber").toString();
                } else {
                    PageData result = new PageData();
                    result.setSuccess(false);
                    result.setMessage("无用户信息!");
                    return result;
                }
            }
        }
        return courseScoreJdbc.getByStu(orgId, jobNum, courseName, pageNumber, pageSize);
    }

    public PageData<CetScoreDomain> getCetScore(Long orgId, Long userId, String jobNum, Integer pageNumber, Integer pageSize) {
        if (StringUtils.isEmpty(jobNum)) {
            if (userId == null || userId < 1) {
                PageData result = new PageData();
                result.setSuccess(false);
                result.setMessage("userId不能为空!");
                return result;
            } else {
                Map user = orgManagerFeignService.getUser(userId);
                if (user != null && user.get("jobNumber") != null) {
                    jobNum = user.get("jobNumber").toString();
                } else {
                    PageData result = new PageData();
                    result.setSuccess(false);
                    result.setMessage("无用户信息!");
                    return result;
                }
            }
        }
        return cetScoreJdbc.getByStu(orgId, jobNum, pageNumber, pageSize);
    }
}
