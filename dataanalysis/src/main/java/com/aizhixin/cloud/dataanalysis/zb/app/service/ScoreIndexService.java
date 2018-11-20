package com.aizhixin.cloud.dataanalysis.zb.app.service;

import com.aizhixin.cloud.dataanalysis.analysis.dto.OrganizationDTO;
import com.aizhixin.cloud.dataanalysis.analysis.service.OrganizationService;
import com.aizhixin.cloud.dataanalysis.common.PageData;
import com.aizhixin.cloud.dataanalysis.common.core.PageUtil;
import com.aizhixin.cloud.dataanalysis.zb.app.entity.ScoreIndex;
import com.aizhixin.cloud.dataanalysis.zb.app.entity.StudentSemesterScoreIndex;
import com.aizhixin.cloud.dataanalysis.zb.app.mananger.ScoreIndexManager;
import com.aizhixin.cloud.dataanalysis.zb.app.mananger.StudentSemesterScoreIndexManager;
import com.aizhixin.cloud.dataanalysis.zb.app.vo.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.*;

@Component
@Transactional(readOnly = true)
public class ScoreIndexService {
    @Autowired
    private ScoreIndexManager scoreIndexManager;
    @Autowired
    private OrganizationService organizationService;
    @Autowired
    private StudentSemesterScoreIndexManager studentSemesterScoreIndexManager;

    /**
     * 单位成绩统计
     */
    public ScoreDwCountVO findDwCount(Long orgId, String xnxq, String collegeCode) {
        ScoreDwCountVO v = new ScoreDwCountVO ();
        if (null == orgId || orgId <= 0) {
            return v;
        }
        int p = xnxq.lastIndexOf("-");
        String xn = null, xq = null;
        if (p > 0) {
            xn = xnxq.substring(0, p);
            xq = xnxq.substring(p + 1);
        }
        if (null == xn || null == xq) {
            return v;
        }
        if (StringUtils.isEmpty(collegeCode)) {
            return scoreIndexManager.getDwCountIndex(orgId.toString(), xn, xq);
        } else {
            ScoreIndex zb = scoreIndexManager.findByXxdmAndXnAndXqmAndBh(orgId.toString(), xn, xq, collegeCode);
            if (null == zb) {
                return v;
            }
            v.setKcs(zb.getKcs());
            v.setBxbjgrc(zb.getBxbjgrc());
            v.setCkrs(zb.getCkrs());
            if (null != zb.getCkrc() && 0 != zb.getCkrc()) {
                v.setAvgcj(zb.getCjzf()/zb.getCkrc());
                v.setAvgjd(zb.getJdzf()/zb.getCkrc());
            }
        }
        return v;
    }

    /**
     * 子单位平均绩点
     */
    public List<ScoreAvgJdVO> findSubDwAvgJd(Long orgId, String xnxq, String collegeCode) {
        List<ScoreAvgJdVO> list = new ArrayList<>();
        if (null == orgId || orgId <= 0) {
            return list;
        }
        int p = xnxq.lastIndexOf("-");
        String xn = null, xq = null;
        if (p > 0) {
            xn = xnxq.substring(0, p);
            xq = xnxq.substring(p + 1);
        }
        if (null == xn || null == xq) {
            return list;
        }
        boolean college = false, professional = false;
        List<ScoreIndex> scoreList = null;
        if (StringUtils.isEmpty(collegeCode)) {
            college = true;
            scoreList = scoreIndexManager.findByXxdmAndXnAndXqmAndPbhIsNull(orgId.toString(), xn, xq);
        } else {
            professional = true;
            scoreList = scoreIndexManager.findByXxdmAndXnAndXqmAndPbh(orgId.toString(), xn, xq, collegeCode);
        }
        Set<String> bhSet = new HashSet<>();
        for(ScoreIndex d : scoreList) {
            ScoreAvgJdVO v = new ScoreAvgJdVO();
            v.setCode(d.getBh());
            bhSet.add(d.getBh());
            if (null != d.getCkrc() && 0 != d.getCkrc()) {
                v.setAvgjd(d.getJdzf() / d.getCkrc());
            }
            list.add(v);
        }

        Map<String, OrganizationDTO> orgMap = new HashMap<>();
        List<OrganizationDTO> orgList = null;
        if (college) {
            orgList = organizationService.getCollegeList(orgId, bhSet);
        } else if (professional) {
            orgList = organizationService.getProfessionList(orgId, null, bhSet);
        }
        if (null != orgList) {
            for (OrganizationDTO o : orgList) {
                orgMap.put(o.getCode(), o);
            }
        }
        if (!orgMap.isEmpty()) {
            for (ScoreAvgJdVO v : list) {
                OrganizationDTO o = orgMap.get(v.getCode());
                if (null != o) {
                    v.setName(o.getName());
                }
            }
        }
        return list;
    }


    /**
     * 子单位成绩指标
     */
    public List<ScoreSubDwIndexVO> findSubDwIndex(Long orgId, String xnxq, String collegeCode) {
        List<ScoreSubDwIndexVO> list = new ArrayList<>();
        if (null == orgId || orgId <= 0) {
            return list;
        }
        int p = xnxq.lastIndexOf("-");
        String xn = null, xq = null;
        if (p > 0) {
            xn = xnxq.substring(0, p);
            xq = xnxq.substring(p + 1);
        }
        if (null == xn || null == xq) {
            return list;
        }
        boolean college = false, professional = false;
        List<ScoreIndex> scoreList = null;
        if (StringUtils.isEmpty(collegeCode)) {
            college = true;
            scoreList = scoreIndexManager.findByXxdmAndXnAndXqmAndPbhIsNull(orgId.toString(), xn, xq);
        } else {
            professional = true;
            scoreList = scoreIndexManager.findByXxdmAndXnAndXqmAndPbh(orgId.toString(), xn, xq, collegeCode);
        }
        Set<String> bhSet = new HashSet<>();
        for(ScoreIndex d : scoreList) {
            ScoreSubDwIndexVO v = new ScoreSubDwIndexVO();
            v.setCode(d.getBh());
            bhSet.add(d.getBh());
            if (null != d.getCkrc() && 0 != d.getCkrc()) {
                v.setCkrc(d.getCkrc());
                v.setAvgjd(d.getJdzf() / d.getCkrc());
                v.setAvgcj(d.getCjzf() / d.getCkrc());
            }
            v.setRs(d.getCkrs());
            v.setKcs(d.getKcs());
            v.setBxckrc(d.getBxckrc());
            v.setBxbjgrc(d.getBxbjgrc());
            list.add(v);
        }
        Map<String, OrganizationDTO> orgMap = new HashMap<>();
        List<OrganizationDTO> orgList = null;
        if (college) {
            orgList = organizationService.getCollegeList(orgId, bhSet);
        } else if (professional) {
            orgList = organizationService.getProfessionList(orgId, null, bhSet);
        }
        if (null != orgList) {
            for (OrganizationDTO o : orgList) {
                orgMap.put(o.getCode(), o);
            }
        }
        if (!orgMap.isEmpty()) {
            for (ScoreSubDwIndexVO v : list) {
                OrganizationDTO o = orgMap.get(v.getCode());
                if (null != o) {
                    v.setName(o.getName());
                }
            }
        }
        return list;
    }

    public List<ScoreAllYearIndexVO> findSubDwIndex(Long orgId, String collegeCode) {
        List<ScoreAllYearIndexVO> list = new ArrayList<>();
        if (null == orgId || orgId <= 0) {
            return list;
        }
        boolean college = false, professional = false;
        if (StringUtils.isEmpty(collegeCode)) {
            college = true;
            list =  scoreIndexManager.findByXxdmAllSemsesterIndex(orgId.toString());
        } else {
            professional = true;
            list =  scoreIndexManager.findByXxdmAllSemsesterIndex(orgId.toString(), collegeCode);
        }

        Set<String> bhSet = new HashSet<>();
        for (ScoreAllYearIndexVO v : list) {
            bhSet.add(v.getCode());
        }
        Map<String, OrganizationDTO> orgMap = new HashMap<>();
        List<OrganizationDTO> orgList = null;
        if (college) {
            orgList = organizationService.getCollegeList(orgId, bhSet);
        } else if (professional) {
            orgList = organizationService.getProfessionList(orgId, null, bhSet);
        }
        if (null != orgList) {
            for (OrganizationDTO o : orgList) {
                orgMap.put(o.getCode(), o);
            }
        }
        if (!orgMap.isEmpty()) {
            for (ScoreAllYearIndexVO v : list) {
                OrganizationDTO o = orgMap.get(v.getCode());
                if (null != o) {
                    v.setName(o.getName());
                }
            }
        }
        return list;
    }

    public PageData<StudentSemesterScoreIndexVO> findScoreSemesterDetails(Long orgId, String xnxq, String collegeCode, String professionalCode, String nj, String name, Integer pageNumber, Integer pageSize) {
        PageData<StudentSemesterScoreIndexVO> pd = new PageData<>();
        if (null == orgId || orgId <= 0) {
            return pd;
        }
        int p = xnxq.lastIndexOf("-");
        String xn = null, xq = null;
        if (p > 0) {
            xn = xnxq.substring(0, p);
            xq = xnxq.substring(p + 1);
        }
        if (null == xn || null == xq) {
            return pd;
        }
        PageData<StudentSemesterScoreIndex> page = studentSemesterScoreIndexManager.querySemesterScoreIndex(PageUtil.createNoErrorPageRequest(pageNumber, pageSize), orgId.toString(), xn, xq, collegeCode, professionalCode, nj, name);
//        Page<StudentSemesterScoreIndex> page = null;
//        if (!StringUtils.isEmpty(collegeCode) && StringUtils.isEmpty(professionalCode)) {
//            page = studentSemesterScoreIndexManager.findByXxdmAndXnAndXqmAndYxsh(PageUtil.createNoErrorPageRequest(pageNumber, pageSize), orgId.toString(), xn, xq, collegeCode);
//        } else if (!StringUtils.isEmpty(collegeCode) && !StringUtils.isEmpty(professionalCode)) {
//            page = studentSemesterScoreIndexManager.findByXxdmAndXnAndXqmAndZyh(PageUtil.createNoErrorPageRequest(pageNumber, pageSize), orgId.toString(), xn, xq, collegeCode, professionalCode);
//        }
//        pd.getPage().setPageNumber(page.getNumber() + 1);
//        pd.getPage().setPageSize(page.getSize());
//        pd.getPage().setTotalElements(page.getTotalElements());
//        pd.getPage().setTotalPages(page.getTotalPages());
        pd.setPage(page.getPage());
        List<StudentSemesterScoreIndexVO> data = new ArrayList<>();
        pd.setData(data);

        fillOutData(orgId, page.getData(), data);
        return pd;
    }

    private void fillOutData(Long orgId, List<StudentSemesterScoreIndex> list, List<StudentSemesterScoreIndexVO> data) {
        Set<String> cbhSet = new HashSet<>();
        Set<String> pbhSet = new HashSet<>();
        for (StudentSemesterScoreIndex d : list) {
            StudentSemesterScoreIndexVO v = new StudentSemesterScoreIndexVO ();
            v.setYxsh(d.getYxsh());
            v.setZyh(d.getZyh());
            v.setBjmc(d.getBh());
            v.setXh(d.getXh());
            v.setXm(d.getXm());
            v.setNj(d.getNj());

            v.setCkkcs(d.getCkkcs());
            v.setGpa(d.getGpa());
            v.setBjgkcs(d.getBjgkcs());
            v.setBjgzxf(d.getBjgzxf());

            cbhSet.add(v.getYxsh());
            pbhSet.add(v.getZyh());
            data.add(v);
        }
        Map<String, OrganizationDTO> cMap = new HashMap<>();
        Map<String, OrganizationDTO> pMap = new HashMap<>();

        List<OrganizationDTO> clist = organizationService.getCollegeList(orgId, cbhSet);
        List<OrganizationDTO> plist = organizationService.getProfessionList(orgId, null, pbhSet);

        if (null != clist) {
            for (OrganizationDTO o : clist) {
                cMap.put(o.getCode(), o);
            }
        }
        if (null != clist) {
            for (OrganizationDTO o : plist) {
                pMap.put(o.getCode(), o);
            }
        }

        for (StudentSemesterScoreIndexVO v : data) {
            OrganizationDTO c = cMap.get(v.getYxsh());
            if (null != c) {
                v.setYxsmc(c.getName());
            }
            c = pMap.get(v.getZyh());
            if (null != c) {
                v.setZymc(c.getName());
            }
        }
    }

    public List<ScoreAvgYearsVO> findLast10YearAvgScore(String xxid) {
        return  scoreIndexManager.findLast10YearAvgScore(xxid);
    }
}
