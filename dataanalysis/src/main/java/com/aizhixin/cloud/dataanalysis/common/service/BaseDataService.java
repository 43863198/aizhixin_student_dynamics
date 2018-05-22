package com.aizhixin.cloud.dataanalysis.common.service;

import com.aizhixin.cloud.dataanalysis.common.PageData;
import com.aizhixin.cloud.dataanalysis.feign.OrgManagerFeignService;
import com.aizhixin.cloud.dataanalysis.feign.vo.CollegeVO;
import com.aizhixin.cloud.dataanalysis.feign.vo.TeacherVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class BaseDataService {
    private OrgManagerFeignService orgManagerFeignService;

    @Autowired
    public BaseDataService (OrgManagerFeignService orgManagerFeignService) {
        this.orgManagerFeignService = orgManagerFeignService;
    }

    public List<CollegeVO> queryAllCollege(Long orgId) {
        PageData<CollegeVO> page =  orgManagerFeignService.queryCollege(orgId, null, 1, Integer.MAX_VALUE);
        if (null != page) {
            return page.getData();
        }
        return new ArrayList<>();
    }

    public List<TeacherVO> queryTeacher(Long orgId, Long collegeId, String name) {
        PageData<TeacherVO> page =  orgManagerFeignService.queryTeacher(orgId, collegeId, name, 1, Integer.MAX_VALUE);
        if (null != page) {
            return page.getData();
        }
        return new ArrayList<>();
    }

}
