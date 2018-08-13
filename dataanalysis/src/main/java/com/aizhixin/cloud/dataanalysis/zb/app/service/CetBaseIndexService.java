package com.aizhixin.cloud.dataanalysis.zb.app.service;

import com.aizhixin.cloud.dataanalysis.analysis.dto.OrganizationDTO;
import com.aizhixin.cloud.dataanalysis.analysis.service.OrganizationService;
import com.aizhixin.cloud.dataanalysis.zb.app.entity.CetBaseIndex;
import com.aizhixin.cloud.dataanalysis.zb.app.mananger.CetBaseIndexManager;
import com.aizhixin.cloud.dataanalysis.zb.app.vo.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.*;

@Component
@Transactional(readOnly = true)
public class CetBaseIndexService {
    @Autowired
    private CetBaseIndexManager cetBaseIndexManager;
    @Autowired
    private OrganizationService organizationService;

    private CetBaseIndex findNewLjOneDwIndex(Long orgId, String cetType, String collegeCode, String professionalCode, String classesCode) {
        CetBaseIndex c = null;
        if (StringUtils.isEmpty(collegeCode) && StringUtils.isEmpty(professionalCode) && StringUtils.isEmpty(classesCode)) {
            c = cetBaseIndexManager.findNewLjAllSchool(orgId.toString(), cetType);
        } else if (!StringUtils.isEmpty(collegeCode) && StringUtils.isEmpty(professionalCode) && StringUtils.isEmpty(classesCode)){
            c = cetBaseIndexManager.findNewDwLj(orgId.toString(), cetType, orgId.toString(), collegeCode);
        } else if (!StringUtils.isEmpty(professionalCode) && !StringUtils.isEmpty(professionalCode) && StringUtils.isEmpty(classesCode)){
            c = cetBaseIndexManager.findNewDwLj(orgId.toString(), cetType, collegeCode, professionalCode);
        } else if (!StringUtils.isEmpty(classesCode) && !StringUtils.isEmpty(classesCode)){
            c = cetBaseIndexManager.findNewDwLj(orgId.toString(), cetType, professionalCode, classesCode);
        }
        return c;
    }

    /**
     * 累计单位统计
     */
    public DwLjCountVO findDwLjCount(Long orgId, String cetType, String collegeCode, String professionalCode, String classesCode) {
        DwLjCountVO v = new DwLjCountVO ();
        if (null == orgId || orgId <= 0) {
            return v;
        }
        CetBaseIndex c = findNewLjOneDwIndex(orgId, cetType, collegeCode, professionalCode, classesCode);
        if (null != c) {
            v.setZxrs(c.getZxrs());
            v.setCkrc(c.getCkrc());
            v.setTgrs(c.getTgrc());
            v.setTgzf(c.getTgzf());
        }
        return v;
    }

    /**
     * 累计子单位人数分布和均值分布
     */
    public List<DwDistributeCountVO> findSubDwLjCount(Long orgId, String cetType, String collegeCode, String professionalCode, String classesCode) {
        List<DwDistributeCountVO> rs = new ArrayList<>();
        if (null == orgId || orgId <= 0) {
            return rs;
        }
        boolean college = false, professinal = false, classes = false;
        List<CetBaseIndex> zbList = null;
        if (StringUtils.isEmpty(collegeCode) && StringUtils.isEmpty(professionalCode) && StringUtils.isEmpty(classesCode)) {
            college = true;
            zbList = cetBaseIndexManager.findSubDwLj(orgId.toString(), cetType, orgId.toString());
        } else if (!StringUtils.isEmpty(collegeCode) && StringUtils.isEmpty(professionalCode) && StringUtils.isEmpty(classesCode)){
            professinal = true;
            zbList = cetBaseIndexManager.findSubDwLj(orgId.toString(), cetType, collegeCode);
        } else if (!StringUtils.isEmpty(professionalCode) && !StringUtils.isEmpty(professionalCode) && StringUtils.isEmpty(classesCode)){
            classes = true;
            zbList = cetBaseIndexManager.findSubDwLj(orgId.toString(), cetType, professionalCode);
        } else if (!StringUtils.isEmpty(classesCode) && !StringUtils.isEmpty(classesCode)){
            classes = true;
            CetBaseIndex c = cetBaseIndexManager.findNewDwLj(orgId.toString(), cetType, professionalCode, classesCode);
            if (null != c) {
                zbList = new ArrayList<>();
                zbList.add(c);
            }
        }
        Set<String> bhSet = new HashSet<>();
        if (null != zbList) {
            for (CetBaseIndex c : zbList) {
                DwDistributeCountVO v = new DwDistributeCountVO ();
                rs.add(v);
                v.setZxrs(c.getZxrs());
                v.setCkrc(c.getCkrc());
                v.setTgrs(c.getTgrc());
                v.setZf(c.getTgzf());
                v.setCode(c.getBh());
                bhSet.add(c.getBh());
            }
            Map<String, OrganizationDTO> orgMap = new HashMap<>();
            if (!bhSet.isEmpty()) {
                List<OrganizationDTO> orgList = null;
                if (college) {
                    orgList = organizationService.getCollegeList(orgId, bhSet);
                }  else if( professinal) {
                    orgList = organizationService.getProfessionList(orgId, null, bhSet);
                } else if (classes) {
                    orgList = organizationService.getClassList(orgId, null, null, bhSet);
                }
                if (null != orgList) {
                    for (OrganizationDTO o : orgList) {
                        orgMap.put(o.getCode(), o);
                    }
                }
            }
            if (!orgMap.isEmpty()) {
                for (DwDistributeCountVO v : rs) {
                    OrganizationDTO o = orgMap.get(v.getCode());
                    if (null != o) {
                        v.setName(o.getName());
                    }
                }
            }
        }
        return rs;
    }

    /**
     * 累计性别人数分布
     */
    public SexRsVO findDwLjSexRsCount(Long orgId, String cetType, String collegeCode, String professionalCode, String classesCode) {
        SexRsVO v = new SexRsVO ();
        BaseIndexRsVO man = new BaseIndexRsVO();
        BaseIndexRsVO woman = new BaseIndexRsVO();
        v.setMan(man);
        v.setWomen(woman);

        if (null == orgId || orgId <= 0) {
            return v;
        }

        CetBaseIndex c = findNewLjOneDwIndex(orgId, cetType, collegeCode, professionalCode, classesCode);
        if (null != c) {
            v.getMan().setCkrc(c.getNrc());
            v.getMan().setTgrs(c.getTgrc());
            v.getMan().setZxrs(c.getNzxrs());

            v.getWomen().setTgrs(c.getVtgrc());
            v.getWomen().setCkrc(c.getVrc());
            v.getWomen().setZxrs(c.getVzxrs());
        }
        return v;
    }

    /**
     * 累计性别均值分布
     */
    public SexAvgVO findDwLjSexAvgCount(Long orgId, String cetType, String collegeCode, String professionalCode, String classesCode) {
        SexAvgVO v = new SexAvgVO ();
        BaseIndexAvgVO man = new BaseIndexAvgVO();
        BaseIndexAvgVO woman = new BaseIndexAvgVO();
        v.setMan(man);
        v.setWomen(woman);

        if (null == orgId || orgId <= 0) {
            return v;
        }

        CetBaseIndex c = findNewLjOneDwIndex(orgId, cetType, collegeCode, professionalCode, classesCode);
        if (null != c) {
            v.getMan().setCkrc(c.getNrc());
            v.getMan().setZf(c.getNzf());
            v.getMan().setZxrs(c.getNzxrs());

            v.getWomen().setCkrc(c.getVtgrc());
            v.getWomen().setZf(c.getVzf());
            v.getWomen().setZxrs(c.getVzxrs());
        }
        return v;
    }
}
