package com.aizhixin.cloud.dataanalysis.analysis.service;

import com.aizhixin.cloud.dataanalysis.analysis.dto.CodeNameCountDTO;
import com.aizhixin.cloud.dataanalysis.analysis.dto.OrganizationDTO;
import com.aizhixin.cloud.dataanalysis.analysis.entity.MinorSecondDegreeInfo;
import com.aizhixin.cloud.dataanalysis.analysis.respository.MinorSecondDegreeRepository;
import com.aizhixin.cloud.dataanalysis.analysis.vo.ExwYxsTop10VO;
import com.aizhixin.cloud.dataanalysis.analysis.vo.MinorSecondDegreeVO;
import com.aizhixin.cloud.dataanalysis.analysis.vo.OverviewVO;
import com.aizhixin.cloud.dataanalysis.bz.manager.XsjbxxManager;
import com.aizhixin.cloud.dataanalysis.common.PageData;
import org.hibernate.SQLQuery;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class MinorSecondDegreeService {
    @Autowired
    private MinorSecondDegreeRepository minorSecondDegreeRepository;
//    @Autowired
//    private GenerateWarningInfoService generateWarningInfoService;
    @Autowired
    private EntityManager em;
    @Autowired
    private OrganizationService organizationService;
    @Autowired
    private XsjbxxManager xsjbxxManager;


    public PageData<MinorSecondDegreeInfo> list(Long xxdm, String collegeCode, String professionCode, Integer pageNumber, Integer pageSize) {

        PageData<MinorSecondDegreeInfo> p = new PageData<>();
        Page<MinorSecondDegreeInfo> pageData;

        try {
            if (null == pageNumber || pageNumber < 1) {
                pageNumber = 1;
            }
            if (null == pageSize) {
                pageSize = 20;
            }

            MinorSecondDegreeInfo minorSecondDegreeInfo = new MinorSecondDegreeInfo();
            minorSecondDegreeInfo.setXxdm(xxdm);
            if (!StringUtils.isEmpty(collegeCode)) {
                minorSecondDegreeInfo.setYxsh(collegeCode);
            }
            if (!StringUtils.isEmpty(professionCode)) {
                minorSecondDegreeInfo.setZyh(professionCode);
            }

            //查询当前时间所属学年学期
//            Map map = getDate(xxdm);
//
//            String teachYear = map.get("teachYear").toString();
//            String semester = map.get("semester").toString();

//            minorSecondDegreeInfo.setXn(teachYear);
//            minorSecondDegreeInfo.setXqm(semester);

            Example<MinorSecondDegreeInfo> example = Example.of(minorSecondDegreeInfo);
            Pageable pageable = new PageRequest(pageNumber - 1, pageSize);
            pageData = minorSecondDegreeRepository.findAll(example, pageable);

            List<MinorSecondDegreeInfo> list = pageData.getContent();

            p.setData(list);
            p.getPage().setPageNumber(pageNumber);
            p.getPage().setPageSize(pageSize);
            p.getPage().setTotalElements(pageData.getTotalElements());
            p.getPage().setTotalPages(pageData.getTotalPages());
            return p;
        } catch (Exception e) {
            e.printStackTrace();
            return new PageData<>();
        }
    }

    public List<MinorSecondDegreeVO> statistics(Long xxdm, String collegeCode) {
        List<MinorSecondDegreeVO> list = new ArrayList<>();
        //查询当前时间所属学年学期
//        Map map = getDate(xxdm);

//        String teachYear = map.get("teachYear").toString();
//        String semester = map.get("semester").toString();


        try {
            if (!StringUtils.isEmpty(collegeCode)) {

                //本部门各专业列表
                Map zyMap = organizationService.getProfession(xxdm,collegeCode);
                List<OrganizationDTO> zyList = (List)zyMap.get("data");


                for (OrganizationDTO temp : zyList){
                    MinorSecondDegreeVO minorSecondDegreeVO = new MinorSecondDegreeVO();
                    minorSecondDegreeVO.setBmmc(temp.getName());
                    String code = temp.getCode();
                    minorSecondDegreeVO.setBmCode(code);
                    //本专业辅修统计
                   int fxCount =  minorSecondDegreeRepository.countByXxdmAndYxshAndZyhAndFxzyhIsNotNull(xxdm,collegeCode,code);
                    //本专业二学位统计
                    int exwCount = minorSecondDegreeRepository.countByXxdmAndYxshAndZyhAndExwyxshIsNotNull(xxdm,collegeCode,code);
                    //外部门辅修本专业统计
                    int wbmfxCount = minorSecondDegreeRepository.countByXxdmAndYxshWBFX(xxdm,collegeCode,code);
                    //外部门修本专业二学位统计
                    int wbmexwCount = minorSecondDegreeRepository.countByXxdmAndYxshWBEXW(xxdm,collegeCode,code);

                    if(fxCount ==0 && exwCount == 0 && wbmfxCount == 0 && wbmexwCount ==0 ){
                        continue;
                    }
                    minorSecondDegreeVO.setBmfxs(fxCount);
                    minorSecondDegreeVO.setBmexws(exwCount);
                    minorSecondDegreeVO.setWbmfxs(wbmfxCount);
                    minorSecondDegreeVO.setWbmexws(wbmexwCount);

                    list.add(minorSecondDegreeVO);
                }
            } else {
                //学校各院系列表
                Map yxMap = organizationService.getCollege(xxdm);
                List<OrganizationDTO> yxList = (List)yxMap.get("data");
                for(OrganizationDTO temp : yxList){
                    MinorSecondDegreeVO minorSecondDegreeVO = new MinorSecondDegreeVO();
                    String code = temp.getCode();
                    long fxCount = minorSecondDegreeRepository.countByXxdmAndYxshAndFxyxshIsNotNull(xxdm,code);
                    long exwCount = minorSecondDegreeRepository.countByXxdmAndYxshAndExwyxshIsNotNull(xxdm,code);
                    long wbmfxCount = minorSecondDegreeRepository.countByXxdmAndYxshWbFxCount(xxdm,code);
                    long wbmexwCount = minorSecondDegreeRepository.countByXxdmAndYxshWbExwCount(xxdm,code);
                    if(fxCount == 0 && exwCount == 0 && wbmfxCount == 0 && wbmexwCount ==0 ){
                        continue;
                    }
                    minorSecondDegreeVO.setBmmc(StringUtils.isEmpty(temp.getSimple())?temp.getName():temp.getSimple());
                    minorSecondDegreeVO.setBmCode(code);
                    minorSecondDegreeVO.setBmfxs((int)fxCount);
                    minorSecondDegreeVO.setBmexws((int)exwCount);
                    minorSecondDegreeVO.setWbmfxs((int)wbmfxCount);
                    minorSecondDegreeVO.setWbmexws((int)wbmexwCount);

                    list.add(minorSecondDegreeVO);
                }
            }
            return list;
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    public List<OverviewVO> overview(Long orgId, String collegeCode) {
        Long fxcount;
        Long exwcount;

        //查询当前时间所属学年学期
//        Map datemap = getDate(orgId);

//        String teachYear = datemap.get("teachYear").toString();
//        String semester = datemap.get("semester").toString();

        Date d = new Date();
        SimpleDateFormat f = new SimpleDateFormat("yyyyMM");
        List<OverviewVO> list = new ArrayList<>();
        Map<String, Object> condition = new HashMap<>();
        try {
            StringBuilder sql = new StringBuilder("select count(xh) as total from t_xsjbxx where   RXNY <= '" + f.format(d) + "' and CURDATE() <= YBYNY and DQZT NOT IN ('02','04','16')");

            if (StringUtils.isEmpty(collegeCode)) {
                sql.append(" and XXID=:orgId ");
                condition.put("orgId", orgId);
                fxcount = minorSecondDegreeRepository.countByXxdmAndFxyxshIsNotNull(orgId);
                exwcount = minorSecondDegreeRepository.countByXxdmAndExwyxshIsNotNull(orgId);
            } else {
                sql.append(" and YXSH=:collegeCode ");
                condition.put("collegeCode", collegeCode);
                fxcount = minorSecondDegreeRepository.countByXxdmAndYxshAndFxyxshIsNotNull(orgId,collegeCode);
                exwcount = minorSecondDegreeRepository.countByXxdmAndYxshAndExwyxshIsNotNull(orgId,collegeCode);
            }

            Query sq = em.createNativeQuery(sql.toString());
            for (Map.Entry<String,Object> e : condition.entrySet()){
                sq.setParameter(e.getKey(),e.getValue());
            }
            sq.unwrap(SQLQuery.class).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
            List<Object> res = sq.getResultList();
            int total = 0;
            if(null != res){
                Map map = (Map)res.get(0);
                total = Integer.parseInt(map.get("total").toString());
            }

            OverviewVO overviewVO =new OverviewVO();
            overviewVO.setFxtotal(fxcount.intValue());
            overviewVO.setExwtotal(exwcount.intValue());
            overviewVO.setTotal(total);
            list.add(overviewVO);
            return list;
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }

    }

    /**
     * 查询当前时间所属学年学期，并且格式化时间区间
     */
//    public Map getDate(Long xxdm) {
//        Calendar c = Calendar.getInstance();
//        // 当前年份
//        int year = c.get(Calendar.YEAR);
//        // 当前月份
//        int month = c.get(Calendar.MONTH) + 1;
//        //当前日期
//        int day = c.get(Calendar.DAY_OF_MONTH);
//        String end = year + "-" + month + "-" + day;
//        //查询当前时间所属学年学期
//        Map map = generateWarningInfoService.getXQAndXN(xxdm, end);
//        String teachYear = map.get("teacherYear").toString();
//        String semester = map.get("semester").toString();
//        if ("秋".equals(semester)) {
//            semester = "1";
//            teachYear = teachYear + "-" + (Integer.parseInt(teachYear) + 1);
//        } else {
//            semester = "2";
//            teachYear = (Integer.parseInt(teachYear) - 1) + "-" + teachYear;
//        }
//
//        Map<String, Object> resultMap = new HashMap<>();
//        resultMap.put("teachYear", teachYear);
//        resultMap.put("semester", semester);
//        return resultMap;
//    }

    public List<ExwYxsTop10VO> queryExw(Long orgId) {
        List<CodeNameCountDTO> rsList = xsjbxxManager.queryYxsZxrs(orgId);
        List<ExwYxsTop10VO> voList = new ArrayList<>();
        Map<String, ExwYxsTop10VO> map = new HashMap<>();
        for (CodeNameCountDTO v : rsList) {
            ExwYxsTop10VO e = new ExwYxsTop10VO(v.getCode(), v.getName(), v.getCount());
            voList.add(e);
            map.put(e.getYxsh(), e);
        }
        List<CodeNameCountDTO> ectList = minorSecondDegreeRepository.countYxsExwByXxdm(orgId);
        for (CodeNameCountDTO v : ectList) {
            ExwYxsTop10VO vo = map.get(v.getCode());
            if (null != vo) {
                vo.setExwrs(v.getCount());
                if (0 != v.getCount()) {
                    vo.setBl(v.getCount() * 100.0 / vo.getZxrs());
                }
            }
        }

        voList = voList.stream().sorted((ExwYxsTop10VO o1, ExwYxsTop10VO o2) -> {
            if (null == o1) {
                return 1;
            }
            if (null == o2) {
                return -1;
            }
            if (o2.getBl() > o1.getBl()) {
                return 1;
            } else if (o2.getBl() < o1.getBl()) {
                return  -1;
            } else {
                return 0;
            }
        }).limit(10).collect(Collectors.toList());
        return voList;
    }
}
