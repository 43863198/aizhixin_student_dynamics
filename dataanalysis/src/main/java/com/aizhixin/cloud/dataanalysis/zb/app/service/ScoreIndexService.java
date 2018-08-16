package com.aizhixin.cloud.dataanalysis.zb.app.service;

import com.aizhixin.cloud.dataanalysis.analysis.dto.OrganizationDTO;
import com.aizhixin.cloud.dataanalysis.analysis.service.OrganizationService;
import com.aizhixin.cloud.dataanalysis.zb.app.entity.ScoreIndex;
import com.aizhixin.cloud.dataanalysis.zb.app.mananger.ScoreIndexManager;
import com.aizhixin.cloud.dataanalysis.zb.app.vo.ScoreAllYearIndexVO;
import com.aizhixin.cloud.dataanalysis.zb.app.vo.ScoreAvgJdVO;
import com.aizhixin.cloud.dataanalysis.zb.app.vo.ScoreDwCountVO;
import com.aizhixin.cloud.dataanalysis.zb.app.vo.ScoreSubDwIndexVO;
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
            zb.setBxbjgrc(zb.getBxbjgrc());
            zb.setCkrs(zb.getCkrs());
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
}
