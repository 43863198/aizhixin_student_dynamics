package com.aizhixin.cloud.dataanalysis.analysis.service;

import com.aizhixin.cloud.dataanalysis.analysis.entity.MinorSecondDegreeInfo;
import com.aizhixin.cloud.dataanalysis.analysis.respository.MinorSecondDegreeRepository;
import com.aizhixin.cloud.dataanalysis.analysis.vo.MinorSecondDegreeVO;
import com.aizhixin.cloud.dataanalysis.analysis.vo.OverviewVO;
import com.aizhixin.cloud.dataanalysis.common.PageData;
import com.aizhixin.cloud.dataanalysis.setup.service.GenerateWarningInfoService;
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
import java.util.*;

@Service
public class MinorSecondDegreeService {
    @Autowired
    private MinorSecondDegreeRepository minorSecondDegreeRepository;
    @Autowired
    private GenerateWarningInfoService generateWarningInfoService;
    @Autowired
    private EntityManager em;


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

            Calendar c = Calendar.getInstance();
            // 当前年份
            int year = c.get(Calendar.YEAR);
            // 当前月份
            int month = c.get(Calendar.MONTH) + 1;
            //当前日期
            int day = c.get(Calendar.DAY_OF_MONTH);
            String date = year + "-" + month + "-" + day;
            //查询当前时间所属学年学期
            Map map = generateWarningInfoService.getXQAndXN(xxdm, date);


            String teachYear = map.get("teacherYear").toString();
            String semester = map.get("semester").toString();


            if ("秋".equals(semester)) {
                semester = "1";
                teachYear = teachYear + "-" + (Integer.parseInt(teachYear) + 1);
            } else {
                semester = "2";
                teachYear = (Integer.parseInt(teachYear) - 1) + "-" + teachYear;
            }

            minorSecondDegreeInfo.setXn(teachYear);
            minorSecondDegreeInfo.setXqm(semester);

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
        try {
            if (!StringUtils.isEmpty(collegeCode)) {
                //本部门各专业辅修统计
                StringBuilder bmfxsql = new StringBuilder("select tp.name as name,tp.CODE as code,count(tf.ZYH) as total FROM (select name,CODE from t_profession where COMPANY_NUMBER = :collegeCode) as tp LEFT JOIN t_fxexwxx as tf " +
                        "on tp.CODE = tf.ZYH and tf.FXYXSH IS NOT NULL and tf.XXDM = :xxdm group by tf.ZYH");

                //外部门辅修本部门各专业统计
                StringBuilder wbmfxsql = new StringBuilder("select tp.name as name,tp.CODE as code,count(tf.FXZYH) as total FROM (select name,CODE from t_profession where COMPANY_NUMBER = :collegeCode) as tp LEFT JOIN t_fxexwxx as tf " +
                        "on tp.CODE = tf.FXZYH and tf.FXYXSH IS NOT NULL and tf.XXDM = :xxdm group by tf.FXZYH");

                //本部门各专业修二学位统计
                StringBuilder bmexwsql = new StringBuilder("select tp.name as name,tp.CODE as code,count(tf.ZYH) as total FROM (select name,CODE from t_profession where COMPANY_NUMBER = :collegeCode) as tp LEFT JOIN t_fxexwxx as tf " +
                        "on tp.CODE = tf.ZYH and tf.EXWYXSH IS NOT NULL and tf.XXDM = :xxdm group by tf.ZYH");

                //外部门修本部门各专业二学位统计
                StringBuilder wbmexwsql = new StringBuilder("select tp.name as name,tp.CODE as code,count(tf.EXWZYH) as total FROM (select name,CODE from t_profession where COMPANY_NUMBER = :collegeCode) as tp LEFT JOIN t_fxexwxx as tf " +
                        "on tp.CODE = tf.EXWZYH and tf.EXWYXSH IS NOT NULL and tf.XXDM = :xxdm group by tf.EXWZYH");

                Query bmfx = em.createNativeQuery(bmfxsql.toString());
                Query wbmfx = em.createNativeQuery(wbmfxsql.toString());
                Query bmexw = em.createNativeQuery(bmexwsql.toString());
                Query wbmexw = em.createNativeQuery(wbmexwsql.toString());
                bmfx.unwrap(SQLQuery.class).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
                wbmfx.unwrap(SQLQuery.class).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
                bmexw.unwrap(SQLQuery.class).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
                wbmexw.unwrap(SQLQuery.class).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);

                bmfx.setParameter("collegeCode", collegeCode);
                wbmfx.setParameter("collegeCode", collegeCode);
                bmexw.setParameter("collegeCode", collegeCode);
                wbmexw.setParameter("collegeCode", collegeCode);

                bmfx.setParameter("xxdm", xxdm);
                wbmfx.setParameter("xxdm", xxdm);
                bmexw.setParameter("xxdm", xxdm);
                wbmexw.setParameter("xxdm", xxdm);


                List<Object> bmfxres = bmfx.getResultList();
                if (null != bmfxres) {
                    for (Object obj : bmfxres) {
                        Map d = (Map) obj;
                        MinorSecondDegreeVO minorSecondDegreeVO = new MinorSecondDegreeVO();
                        if (null != d.get("total")) {
                            minorSecondDegreeVO.setBmfxs(Integer.valueOf(d.get("total").toString()));
                        }
                        if (null != d.get("name")) {
                            minorSecondDegreeVO.setBmmc(d.get("name").toString());
                        }
                        if (null != d.get("code")) {
                            minorSecondDegreeVO.setBmCode(d.get("code").toString());
                        }
                        list.add(minorSecondDegreeVO);
                    }
                }
                List<Object> wbmfxres = wbmfx.getResultList();
                if (null != wbmfxres) {
                    for (Object obj : wbmfxres) {
                        Map d = (Map) obj;
                        for (MinorSecondDegreeVO mv : list) {
                            if (mv.getBmmc().equals(d.get("name").toString())) {
                                mv.setWbmfxs(Integer.valueOf(d.get("total").toString()));
                            }
                        }
                    }
                }
                List<Object> bmexwres = bmexw.getResultList();
                if (null != bmexwres) {
                    for (Object obj : bmexwres) {
                        Map d = (Map) obj;
                        for (MinorSecondDegreeVO mv : list) {
                            if (mv.getBmmc().equals(d.get("name").toString())) {
                                mv.setBmexws(Integer.valueOf(d.get("total").toString()));
                            }
                        }
                    }
                }
                List<Object> wbmexwres = wbmexw.getResultList();
                if (null != wbmexwres) {
                    for (Object obj : wbmexwres) {
                        Map d = (Map) obj;
                        for (MinorSecondDegreeVO mv : list) {
                            if (mv.getBmmc().equals(d.get("name").toString())) {
                                mv.setWbmexws(Integer.valueOf(d.get("total").toString()));
                            }
                        }
                    }
                }
            } else {

                List<Map<String, Object>> fxlist = minorSecondDegreeRepository.findByXxdmFxCount(xxdm);
                List<Map<String, Object>> exwlist = minorSecondDegreeRepository.findByXxdmEXWCount(xxdm);
                for (Map map : fxlist) {
                    MinorSecondDegreeVO minorSecondDegreeVO = new MinorSecondDegreeVO();
                    minorSecondDegreeVO.setBmmc(map.get("name").toString());
                    minorSecondDegreeVO.setBmCode(map.get("code").toString());
                    minorSecondDegreeVO.setBmfxs(Integer.valueOf(map.get("total").toString()));
                    list.add(minorSecondDegreeVO);
                }
                for (Map map2 : exwlist) {
                    for (MinorSecondDegreeVO mv : list) {
                        if (mv.getBmmc().equals(map2.get("name").toString())) {
                            mv.setBmexws(Integer.valueOf(map2.get("total").toString()));
                        }
                    }

                }
                List<Map<String, Object>> wfxlist = new ArrayList<>();
                List<Map<String, Object>> wexwlist = new ArrayList<>();
                for (MinorSecondDegreeVO mv : list) {
                    String yxmc = mv.getBmmc();
                    wfxlist = minorSecondDegreeRepository.findByXxdmAndWbFxCount(xxdm, yxmc);
                    wexwlist = minorSecondDegreeRepository.findByXxdmAndWbExwCount(xxdm, yxmc);
                    for (Map map : wfxlist) {
                        for (MinorSecondDegreeVO mv1 : list) {
                            if (mv1.getBmmc().equals(map.get("name").toString())) {
                                mv1.setWbmfxs(Integer.valueOf(map.get("total").toString()));
                            }
                        }
                    }
                    for (Map map2 : wexwlist) {
                        for (MinorSecondDegreeVO mv2 : list) {
                            if (mv2.getBmmc().equals(map2.get("name").toString())) {
                                mv2.setWbmexws(Integer.valueOf(map2.get("total").toString()));
                            }
                        }

                    }
                }
            }

            return list;
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    public List<OverviewVO> overview(Long orgId, String collegeCode) {
        Long fxcount = 0L;
        Long exwcount = 0L;

        List<OverviewVO> list = new ArrayList<>();
        Map<String, Object> condition = new HashMap<>();
        try {
            StringBuilder sql = new StringBuilder("select count(xh) as total from t_xsjbxx where CURDATE() BETWEEN RXNY and YBYNY and DQZT NOT IN ('02','04','16')");

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
}
