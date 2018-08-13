package com.aizhixin.cloud.dataanalysis.zb.app.service;

import com.aizhixin.cloud.dataanalysis.analysis.dto.OrganizationDTO;
import com.aizhixin.cloud.dataanalysis.analysis.service.OrganizationService;
import com.aizhixin.cloud.dataanalysis.zb.app.entity.CetBaseIndex;
import com.aizhixin.cloud.dataanalysis.zb.app.entity.CetGradeIndex;
import com.aizhixin.cloud.dataanalysis.zb.app.mananger.CetBaseIndexManager;
import com.aizhixin.cloud.dataanalysis.zb.app.mananger.CetGradeIndexManager;
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
    @Autowired
    private CetGradeIndexManager cetGradeIndexManager;

    private CetBaseIndex findNewLjOneDwIndex(Long orgId, String cetType, String collegeCode, String professionalCode, String classesCode) {
        CetBaseIndex c = null;
        if (StringUtils.isEmpty(collegeCode) && StringUtils.isEmpty(professionalCode) && StringUtils.isEmpty(classesCode)) {
            c = cetBaseIndexManager.findNewLjAllSchool(orgId.toString(), cetType);
        } else if (!StringUtils.isEmpty(collegeCode) && StringUtils.isEmpty(professionalCode) && StringUtils.isEmpty(classesCode)){
            c = cetBaseIndexManager.findNewDwLj(orgId.toString(), cetType, orgId.toString(), collegeCode);
        } else if (!StringUtils.isEmpty(collegeCode) && !StringUtils.isEmpty(professionalCode) && StringUtils.isEmpty(classesCode)){
            c = cetBaseIndexManager.findNewDwLj(orgId.toString(), cetType, collegeCode, professionalCode);
        } else if (!StringUtils.isEmpty(professionalCode) && !StringUtils.isEmpty(classesCode)){
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
            v.getMan().setZxrs(c.getNzxrs());
            v.getMan().setCkrc(c.getNrc());
            v.getMan().setTgrs(c.getTgrc());
            v.getMan().setZxrs(c.getNzxrs());

            v.getWomen().setZxrs(c.getVzxrs());
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
            v.getMan().setZxrs(c.getNzxrs());
            v.getMan().setCkrc(c.getNrc());
            v.getMan().setZf(c.getNzf());
            v.getMan().setZxrs(c.getNzxrs());

            v.getWomen().setZxrs(c.getVzxrs());
            v.getWomen().setCkrc(c.getVtgrc());
            v.getWomen().setZf(c.getVzf());
            v.getWomen().setZxrs(c.getVzxrs());
        }
        return v;
    }

    /**
     * 累计最新年级指标基础指标查询请求
     */
    private List<CetGradeIndex> findNewLjGradeIndex(Long orgId, String cetType, String collegeCode, String professionalCode, String classesCode) {
        if (StringUtils.isEmpty(collegeCode) && StringUtils.isEmpty(professionalCode) && StringUtils.isEmpty(classesCode)) {
            return cetGradeIndexManager.findNewLjAllSchool(orgId.toString(), cetType);
        } else if (!StringUtils.isEmpty(collegeCode) && StringUtils.isEmpty(professionalCode) && StringUtils.isEmpty(classesCode)){
            return cetGradeIndexManager.findNewLj(orgId.toString(), cetType, orgId.toString(), collegeCode);
        } else if (!StringUtils.isEmpty(collegeCode) && !StringUtils.isEmpty(professionalCode) && StringUtils.isEmpty(classesCode)){
            return cetGradeIndexManager.findNewLj(orgId.toString(), cetType, collegeCode, professionalCode);
        } else if (!StringUtils.isEmpty(professionalCode) && !StringUtils.isEmpty(classesCode)){
            return cetGradeIndexManager.findNewLj(orgId.toString(), cetType, professionalCode, classesCode);
        }
        return new ArrayList<>();
    }

    /**
     * 累计年级人数指标s
     */
    public List<CetGradeRsVo> findDwLjGradeRsCount (Long orgId, String cetType, String collegeCode, String professionalCode, String classesCode) {
        List<CetGradeRsVo> rs = new ArrayList<>();
        List<CetGradeIndex> list = findNewLjGradeIndex(orgId, cetType, collegeCode, professionalCode, classesCode);
        for(CetGradeIndex g : list) {
            CetGradeRsVo v = new CetGradeRsVo ();
            v.setNj(g.getNj());
            v.setZxrs(g.getZxrs());
            v.setTgrs(g.getTgrc());
            rs.add(v);
        }
        return rs;
    }

    /**
     * 累计年级的均值分布
     */
    public List<CetGradeAvgVo> findDwLjGradeAvgCount (Long orgId, String cetType, String collegeCode, String professionalCode, String classesCode) {
        List<CetGradeAvgVo> rs = new ArrayList<>();
        List<CetGradeIndex> list = findNewLjGradeIndex(orgId, cetType, collegeCode, professionalCode, classesCode);
        for(CetGradeIndex g : list) {
            CetGradeAvgVo v = new CetGradeAvgVo ();
            v.setNj(g.getNj());
            v.setZxrs(g.getZxrs());
            v.setCkrc(g.getCkrc());
            v.setZf(g.getZf());
            rs.add(v);
        }
        return rs;
    }

    private CetBaseIndex findDcBaseIndex(String xn, String xq, Long orgId, String cetType, String collegeCode, String professionalCode, String classesCode) {
        CetBaseIndex c = new CetBaseIndex();
        if (StringUtils.isEmpty(collegeCode) && StringUtils.isEmpty(professionalCode) && StringUtils.isEmpty(classesCode)) {
            c = cetBaseIndexManager.findDcOneDw(xn, xq, orgId.toString(), cetType);
        } else if (!StringUtils.isEmpty(collegeCode) && StringUtils.isEmpty(professionalCode) && StringUtils.isEmpty(classesCode)){
            c = cetBaseIndexManager.findDcOneDw(xn, xq, orgId.toString(), cetType, orgId.toString(), collegeCode);
        } else if (!StringUtils.isEmpty(collegeCode) && !StringUtils.isEmpty(professionalCode) && StringUtils.isEmpty(classesCode)){
            c = cetBaseIndexManager.findDcOneDw(xn, xq, orgId.toString(), cetType, collegeCode, professionalCode);
        } else if (!StringUtils.isEmpty(professionalCode) && !StringUtils.isEmpty(classesCode)){
            c = cetBaseIndexManager.findDcOneDw(xn, xq, orgId.toString(), cetType, professionalCode, classesCode);
        }
        return c;
    }

    public DwDcCountVO findDcDwCount(String xnxq, Long orgId, String cetType, String collegeCode, String professionalCode, String classesCode) {
        int p = xnxq.lastIndexOf("-");
        String xn = null, xq = null;
        if (p > 0) {
            xn = xnxq.substring(0, p);
            xq = xnxq.substring(p + 1);
        }
        if (null == xn || null == xq) {
            return new DwDcCountVO();
        }
        CetBaseIndex c = findDcBaseIndex(xn, xq, orgId, cetType, collegeCode, professionalCode, classesCode);
        DwDcCountVO vo = new DwDcCountVO ();
        vo.setCkrc(c.getCkrc());
        vo.setGf(c.getGf());
        vo.setTgrs(c.getTgrc());
        vo.setZf(c.getZf());
        vo.setZxrs(c.getZxrs());
        return vo;
    }

    public List<DwDistributeCountVO> findSubDwDcCount(String xnxq, Long orgId, String cetType, String collegeCode, String professionalCode, String classesCode) {
        List<DwDistributeCountVO> rsList = new ArrayList<>();
        int p = xnxq.lastIndexOf("-");
        String xn = null, xq = null;
        if (p > 0) {
            xn = xnxq.substring(0, p);
            xq = xnxq.substring(p + 1);
        }
        if (null == xn || null == xq) {
            return rsList;
        }
        boolean college = false, professinal = false, classes = false;
        List<CetBaseIndex> list  = new ArrayList<>();
        if (StringUtils.isEmpty(collegeCode) && StringUtils.isEmpty(professionalCode) && StringUtils.isEmpty(classesCode)) {
            list  =  cetBaseIndexManager.findDcSubDw(xn, xq, orgId.toString(), cetType, orgId.toString());
            college = true;
        } else if (!StringUtils.isEmpty(collegeCode) && StringUtils.isEmpty(professionalCode) && StringUtils.isEmpty(classesCode)){
            list  =  cetBaseIndexManager.findDcSubDw(xn, xq, orgId.toString(), cetType, collegeCode);
            professinal = true;
        } else if (!StringUtils.isEmpty(collegeCode) && !StringUtils.isEmpty(professionalCode) && StringUtils.isEmpty(classesCode)){
            list  =  cetBaseIndexManager.findDcSubDw(xn, xq, orgId.toString(), cetType, professionalCode);
            classes = true;
        } else if (!StringUtils.isEmpty(professionalCode) && !StringUtils.isEmpty(classesCode)){
            list  =  cetBaseIndexManager.findDcSubDw(xn, xq, orgId.toString(), cetType, classesCode);
            classes = true;
        }
        Set<String> bhSet = new HashSet<>();
        for (CetBaseIndex d : list) {
            DwDistributeCountVO v = new DwDistributeCountVO ();
            v.setCode(d.getBh());
            v.setCkrc(d.getCkrc());
            v.setTgrs(d.getTgrc());
            v.setZf(d.getZf());
            v.setZxrs(d.getZxrs());
            bhSet.add(d.getBh());
            rsList.add(v);
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
            for (DwDistributeCountVO v : rsList) {
                OrganizationDTO o = orgMap.get(v.getCode());
                if (null != o) {
                    v.setName(o.getName());
                }
            }
        }
        return rsList;
    }
}
