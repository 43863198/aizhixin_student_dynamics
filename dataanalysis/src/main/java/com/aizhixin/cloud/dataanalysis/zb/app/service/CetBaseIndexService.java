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

import java.text.DecimalFormat;
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

    /**
     * 单位累计指标的查询
     */
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
     * 性别人数赋值
     */
    private void sexRsValue(SexRsVO v, CetBaseIndex c) {
        if (null != c) {
            v.getMan().setZxrs(c.getNzxrs());
            v.getMan().setCkrc(c.getNrc());
            v.getMan().setTgrs(c.getNtgrc());

            v.getWomen().setZxrs(c.getVzxrs());
            v.getWomen().setCkrc(c.getVrc());
            v.getWomen().setTgrs(c.getVtgrc());
        }
    }

    /**
     * 性别均值赋值
     */
    private void sexAvgValue(SexAvgVO v, CetBaseIndex c) {
        if (null != c) {
            v.getMan().setZxrs(c.getNzxrs());
            v.getMan().setCkrc(c.getNrc());
            v.getMan().setZf(c.getNzf());

            v.getWomen().setZxrs(c.getVzxrs());
            v.getWomen().setCkrc(c.getVrc());
            v.getWomen().setZf(c.getVzf());
        }
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
     * 单次年级指标基础指标查询请求
     */
    private List<CetGradeIndex> findDcGradeIndex(String xn, String xq, Long orgId, String cetType, String collegeCode, String professionalCode, String classesCode) {
        if (StringUtils.isEmpty(collegeCode) && StringUtils.isEmpty(professionalCode) && StringUtils.isEmpty(classesCode)) {
            return cetGradeIndexManager.findDcAllSchool(xn, xq, orgId.toString(), cetType);
        } else if (!StringUtils.isEmpty(collegeCode) && StringUtils.isEmpty(professionalCode) && StringUtils.isEmpty(classesCode)){
            return cetGradeIndexManager.findDc(xn, xq, orgId.toString(), cetType, orgId.toString(), collegeCode);
        } else if (!StringUtils.isEmpty(collegeCode) && !StringUtils.isEmpty(professionalCode) && StringUtils.isEmpty(classesCode)){
            return cetGradeIndexManager.findDc(xn, xq, orgId.toString(), cetType, collegeCode, professionalCode);
        } else if (!StringUtils.isEmpty(professionalCode) && !StringUtils.isEmpty(classesCode)){
            return cetGradeIndexManager.findDc(xn, xq, orgId.toString(), cetType, professionalCode, classesCode);
        }
        return new ArrayList<>();
    }

    /**
     * 单次单位总值查询
     */
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

    /**
     * 填充学院名称
     */
    private void fillCollegeName(Long orgId, List<DwDistributeCountVO> rsList, boolean college, boolean professinal, boolean classes, Set<String> bhSet) {
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
    }

    /**
     * 填充年级人数
     */
    private void fillGradeRs(List<CetGradeRsVo> rs, List<CetGradeIndex> list) {
        for(CetGradeIndex g : list) {
            CetGradeRsVo v = new CetGradeRsVo ();
            v.setNj(g.getNj());
            v.setZxrs(g.getZxrs());
            v.setTgrs(g.getTgrc());
            v.setCkrc(g.getCkrc());
            rs.add(v);
        }
    }

    /**
     * 填充年级均值
     */
    private void fillGradeAvg(List<CetGradeAvgVo> rs, List<CetGradeIndex> list) {
        for(CetGradeIndex g : list) {
            CetGradeAvgVo v = new CetGradeAvgVo ();
            v.setNj(g.getNj());
            v.setZxrs(g.getZxrs());
            v.setCkrc(g.getCkrc());
            v.setZf(g.getZf());
            rs.add(v);
        }
    }

    /**
     * 历年指标汇总
     */
    private List<CetBaseIndex> findAllYear(Long orgId, String cetType, String collegeCode, String professionalCode, String classesCode, String dhlj) {
        if (StringUtils.isEmpty(collegeCode) && StringUtils.isEmpty(professionalCode) && StringUtils.isEmpty(classesCode)) {
            return cetBaseIndexManager.findAllYearCount(orgId.toString(), cetType, dhlj);
        } else if (!StringUtils.isEmpty(collegeCode) && StringUtils.isEmpty(professionalCode) && StringUtils.isEmpty(classesCode)){
            return cetBaseIndexManager.findAllYearCount(orgId.toString(), cetType, dhlj, orgId.toString(), collegeCode);
        } else if (!StringUtils.isEmpty(collegeCode) && !StringUtils.isEmpty(professionalCode) && StringUtils.isEmpty(classesCode)){
            return cetBaseIndexManager.findAllYearCount(orgId.toString(), cetType, dhlj, collegeCode, professionalCode);
        } else if (!StringUtils.isEmpty(professionalCode) && !StringUtils.isEmpty(classesCode)){
            return cetBaseIndexManager.findAllYearCount(orgId.toString(), cetType, dhlj, professionalCode, classesCode);
        }
        return new ArrayList<>();
    }

    /**
     * 历年人数分布填充
     */
    private void fillAllYearRs(List<BaseIndexYearRsVO> rs, List<CetBaseIndex> list) {
        for (CetBaseIndex d : list) {
            BaseIndexYearRsVO v = new BaseIndexYearRsVO();
            rs.add(v);
            v.setCkrc(d.getCkrc());
            v.setTgrs(d.getTgrc());
            v.setXnxq(d.getXn() + "-" + d.getXqm());
            v.setZxrs(d.getZxrs());
        }
        if (null != rs && !rs.isEmpty()) {
            Collections.sort(rs);
        }
    }

    /**
     * 通过率计算
     */
    private void yearAllRsRate(List<BaseIndexYearRsVO> rs, List<YearPercentageVO> list) {
        Double lastRate = null, rate = null;
        boolean first = true;
        DecimalFormat format = new DecimalFormat("#.00");
        for (BaseIndexYearRsVO v : rs) {
            YearPercentageVO vo = new YearPercentageVO ();
            vo.setXnxq(v.getXnxq());
            if (null != v.getZxrs() && null != v.getTgrs()) {
                rate = v.getTgrs() * 1.0 / v.getZxrs();
                if (null != lastRate) {
                    lastRate = (rate - lastRate) * 100 / lastRate;
                    vo.setRate(format.format(lastRate));
                }
                lastRate = rate;
            }
            if (!first) {
                list.add(vo);
            } else {
                first = false;
            }
        }
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
                v.setZf(c.getZf());
                v.setCode(c.getBh());
                bhSet.add(c.getBh());
            }
            fillCollegeName(orgId, rs, college, professinal, classes, bhSet);
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
        sexRsValue(v, c);
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
        sexAvgValue(v, c);
        return v;
    }

    /**
     * 累计年级人数指标
     */
    public List<CetGradeRsVo> findDwLjGradeRsCount (Long orgId, String cetType, String collegeCode, String professionalCode, String classesCode) {
        List<CetGradeRsVo> rs = new ArrayList<>();
        List<CetGradeIndex> list = findNewLjGradeIndex(orgId, cetType, collegeCode, professionalCode, classesCode);
        fillGradeRs(rs, list);
        return rs;
    }

    /**
     * 累计年级的均值分布
     */
    public List<CetGradeAvgVo> findDwLjGradeAvgCount (Long orgId, String cetType, String collegeCode, String professionalCode, String classesCode) {
        List<CetGradeAvgVo> rs = new ArrayList<>();
        List<CetGradeIndex> list = findNewLjGradeIndex(orgId, cetType, collegeCode, professionalCode, classesCode);
        fillGradeAvg(rs, list);
        return rs;
    }

    /**
     * 单次单位总值查询
     */
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
        if (null != c) {
            vo.setCkrc(c.getCkrc());
            vo.setGf(c.getGf());
            vo.setTgrs(c.getTgrc());
            vo.setZf(c.getZf());
            vo.setZxrs(c.getZxrs());
        }
        return vo;
    }

    /**
     * 单次子单位指标值查询
     */
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
        fillCollegeName(orgId, rsList, college, professinal, classes, bhSet);
        return rsList;
    }

    /**
     * 单次性别均值分布
     */
    public SexAvgVO findDwDcSexAvgCount(String xnxq, Long orgId, String cetType, String collegeCode, String professionalCode, String classesCode) {
        SexAvgVO v = new SexAvgVO ();
        BaseIndexAvgVO man = new BaseIndexAvgVO();
        BaseIndexAvgVO woman = new BaseIndexAvgVO();
        v.setMan(man);
        v.setWomen(woman);
        int p = xnxq.lastIndexOf("-");
        String xn = null, xq = null;
        if (p > 0) {
            xn = xnxq.substring(0, p);
            xq = xnxq.substring(p + 1);
        }
        if (null == xn || null == xq) {
            return v;
        }
        if (null == orgId || orgId <= 0) {
            return v;
        }

        CetBaseIndex c = findDcBaseIndex(xn, xq, orgId, cetType, collegeCode, professionalCode, classesCode);
        sexAvgValue(v, c);
        return v;
    }

    /**
     * 累计性别人数分布
     */
    public SexRsVO findDwDcSexRsCount(String xnxq, Long orgId, String cetType, String collegeCode, String professionalCode, String classesCode) {
        SexRsVO v = new SexRsVO ();
        BaseIndexRsVO man = new BaseIndexRsVO();
        BaseIndexRsVO woman = new BaseIndexRsVO();
        v.setMan(man);
        v.setWomen(woman);

        int p = xnxq.lastIndexOf("-");
        String xn = null, xq = null;
        if (p > 0) {
            xn = xnxq.substring(0, p);
            xq = xnxq.substring(p + 1);
        }
        if (null == xn || null == xq) {
            return v;
        }
        if (null == orgId || orgId <= 0) {
            return v;
        }

        CetBaseIndex c = findDcBaseIndex(xn, xq, orgId, cetType, collegeCode, professionalCode, classesCode);
        sexRsValue(v, c);
        return v;
    }



    /**
     * 单次年级人数指标
     */
    public List<CetGradeRsVo> findDcGradeRsCount (String xnxq, Long orgId, String cetType, String collegeCode, String professionalCode, String classesCode) {
        List<CetGradeRsVo> rs = new ArrayList<>();
        int p = xnxq.lastIndexOf("-");
        String xn = null, xq = null;
        if (p > 0) {
            xn = xnxq.substring(0, p);
            xq = xnxq.substring(p + 1);
        }
        if (null == xn || null == xq) {
            return rs;
        }
        if (null == orgId || orgId <= 0) {
            return rs;
        }
        List<CetGradeIndex> list = findDcGradeIndex(xn, xq, orgId, cetType, collegeCode, professionalCode, classesCode);
        fillGradeRs(rs, list);
        return rs;
    }

    /**
     * 单次年级的均值分布
     */
    public List<CetGradeAvgVo> findDcGradeAvgCount (String xnxq, Long orgId, String cetType, String collegeCode, String professionalCode, String classesCode) {
        List<CetGradeAvgVo> rs = new ArrayList<>();
        int p = xnxq.lastIndexOf("-");
        String xn = null, xq = null;
        if (p > 0) {
            xn = xnxq.substring(0, p);
            xq = xnxq.substring(p + 1);
        }
        if (null == xn || null == xq) {
            return rs;
        }
        if (null == orgId || orgId <= 0) {
            return rs;
        }
        List<CetGradeIndex> list = findDcGradeIndex(xn, xq, orgId, cetType, collegeCode, professionalCode, classesCode);
        fillGradeAvg(rs, list);
        return rs;
    }


    /**
     * 历年单次的人数分布
     */
    public List<BaseIndexYearRsVO> findDcAllYearRsCount (Long orgId, String cetType, String collegeCode, String professionalCode, String classesCode) {
        List<BaseIndexYearRsVO> rs = new ArrayList<>();
        if (null == orgId || orgId <= 0) {
            return rs;
        }
        List<CetBaseIndex> list = findAllYear(orgId, cetType, collegeCode, professionalCode, classesCode, "1");
        fillAllYearRs(rs, list);
        return rs;
    }

    /**
     * 历年累计的人数分布
     */
    public List<BaseIndexYearRsVO> findLjAllYearRsCount (Long orgId, String cetType, String collegeCode, String professionalCode, String classesCode) {
        List<BaseIndexYearRsVO> rs = new ArrayList<>();
        if (null == orgId || orgId <= 0) {
            return rs;
        }
        List<CetBaseIndex> list = findAllYear(orgId, cetType, collegeCode, professionalCode, classesCode, "2");
        fillAllYearRs(rs, list);
        return rs;
    }

    /**
     * 历年单次的均值分布
     */
    public List<BaseIndexYearAvgVO> findDcAllYearAvgCount (Long orgId, String cetType, String collegeCode, String professionalCode, String classesCode) {
        List<BaseIndexYearAvgVO> rs = new ArrayList<>();
        if (null == orgId || orgId <= 0) {
            return rs;
        }
        List<CetBaseIndex> list = findAllYear(orgId, cetType, collegeCode, professionalCode, classesCode, "1");
        for (CetBaseIndex d : list) {
            BaseIndexYearAvgVO v = new BaseIndexYearAvgVO();
            rs.add(v);
            v.setCkrc(d.getCkrc());
            v.setXnxq(d.getXn() + "-" + d.getXqm());
            v.setZxrs(d.getZxrs());
            v.setZf(d.getZf());
        }
        if (null != rs && !rs.isEmpty()) {
            Collections.sort(rs);
        }
        return rs;
    }

    /**
     * 历年单次通过率增长率
     */
    public List<YearPercentageVO> findDcAllYearRsRate (Long orgId, String cetType, String collegeCode, String professionalCode, String classesCode) {
        List<BaseIndexYearRsVO> rs = findDcAllYearRsCount(orgId, cetType, collegeCode, professionalCode, classesCode);
        List<YearPercentageVO> list = new ArrayList<>();
        yearAllRsRate(rs, list);
        return list;
    }


    /**
     * 历年累计通过率增长率
     */
    public List<YearPercentageVO> findLjAllYearRsRate (Long orgId, String cetType, String collegeCode, String professionalCode, String classesCode) {
        List<BaseIndexYearRsVO> rs = findLjAllYearRsCount(orgId, cetType, collegeCode, professionalCode, classesCode);
        List<YearPercentageVO> list = new ArrayList<>();
        yearAllRsRate(rs, list);
        return list;
    }


    /**
     * 历年单次均值增长率
     */
    public List<YearPercentageVO> findDcAllYearAvgRate (Long orgId, String cetType, String collegeCode, String professionalCode, String classesCode) {
        List<BaseIndexYearAvgVO> rs = findDcAllYearAvgCount(orgId, cetType, collegeCode, professionalCode, classesCode);
        List<YearPercentageVO> list = new ArrayList<>();
        Double lastRate = null;
        Double lastAvg = null, avg = null;
        boolean first = true;
        DecimalFormat format = new DecimalFormat("#.00");
        for (BaseIndexYearAvgVO v : rs) {
            YearPercentageVO vo = new YearPercentageVO ();
            vo.setXnxq(v.getXnxq());
            if (null != v.getCkrc() && null != v.getZf()) {
                avg = v.getZf() / v.getCkrc();
                if (null != lastAvg) {
                    lastRate = (avg - lastAvg) * 100 / lastAvg;
                    vo.setRate(format.format(lastRate));
                }
                lastAvg = avg;
            }
            if (!first) {
                list.add(vo);
            } else {
                first = false;
            }
        }
        return list;
    }
}
