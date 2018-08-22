package com.aizhixin.cloud.dataanalysis.etl.study.service;

import com.aizhixin.cloud.dataanalysis.etl.study.dto.EtlStudendStudyPlanDTO;
import com.aizhixin.cloud.dataanalysis.etl.study.dto.EtlStudentStudyPlanXdztDTO;
import com.aizhixin.cloud.dataanalysis.etl.study.dto.EtlStudyTeachingPlanDTO;
import com.aizhixin.cloud.dataanalysis.etl.study.manager.EtlStudyExceptionManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
@Slf4j
public class EtlStudyExceptionService {
    @Autowired
    private EtlStudyExceptionManager etlStudyExceptionManager;

    /**
     * 删除重复的专业计划
     */
    @Async
    public void cleanRepeatZypyjh() {
        List<EtlStudyTeachingPlanDTO> repeatList = etlStudyExceptionManager.queryRepeatZypyjh();
        if (null != repeatList) {
            log.info("Query repeat zypyjh data ({})", repeatList.size());
            for (EtlStudyTeachingPlanDTO d : repeatList) {
                List<EtlStudyTeachingPlanDTO> repeatJhhList = etlStudyExceptionManager.queryRepeatZypyjhJHH(d);
                Collections.sort(repeatJhhList, (EtlStudyTeachingPlanDTO c1, EtlStudyTeachingPlanDTO c2) -> (int) (c2.getCt() - c1.getCt()));
                boolean first = true;
                for (EtlStudyTeachingPlanDTO dto : repeatJhhList) {//保留最大的第一条数据，其余删除
                    if (first) {
                        first = false;
                    } else {
                        etlStudyExceptionManager.deleteRepeatData(dto);
                        log.info("Delete repeat data ({})", dto);
                    }
                }
            }
        }
    }

    /**
     * 生成学生学年学期的培养计划
     */
    @Async
    public void createXnXqStudentPyjh(String xn, String xq, Long orgId) {
        List<EtlStudendStudyPlanDTO> zypyjhList = etlStudyExceptionManager.queryXnXqZypyjh(xn, xq, orgId.toString());
        if (null != zypyjhList) {
            log.info("Query zypyjh count({})", zypyjhList.size());
            for (EtlStudendStudyPlanDTO zy : zypyjhList) {
                List<EtlStudendStudyPlanDTO> xspyjhList = new ArrayList<>();
                List<EtlStudendStudyPlanDTO> xskcList = etlStudyExceptionManager.queryXnXqZyhNjCourse(xn, xq, orgId.toString(), zy.getZyh(), zy.getNj());
                if (null != xskcList && !xskcList.isEmpty()) {
                    List<EtlStudendStudyPlanDTO> xsxxList = etlStudyExceptionManager.queryXsxx(zy.getZyh(), zy.getNj() + "级", orgId);
                    if (null != xsxxList && !xsxxList.isEmpty()) {
                        log.info("Query zyh({}), nj({}) course ({}), students ({})", zy.getZyh(), zy.getNj(), xskcList.size(), xsxxList.size());
                        for (EtlStudendStudyPlanDTO x : xsxxList) {
                            for (EtlStudendStudyPlanDTO kc : xskcList) {
                                EtlStudendStudyPlanDTO jh = new EtlStudendStudyPlanDTO();
                                jh.setXn(xn);
                                jh.setXqm(xq);
                                jh.setXxdm(orgId.toString());
                                jh.setNj(zy.getNj());
                                jh.setZyh(zy.getZyh());

                                jh.setXm(x.getXm());
                                jh.setXh(x.getXh());
                                jh.setBjmc(x.getBjmc());
                                jh.setYxsh(x.getYxsh());

                                jh.setKch(kc.getKch());
                                jh.setKcmc(kc.getKcmc());
                                jh.setXf(kc.getXf());

                                xspyjhList.add(jh);
                            }
                        }
                    }
                }
                if (!xspyjhList.isEmpty()) {
                    log.info("Save xspyjh data count ({})", xspyjhList.size());
                    etlStudyExceptionManager.saveXspyjh(xspyjhList);
                }
            }
        }
    }

    @Async
    public void calStudentPyjh(String xxdm, String yxsh, String xn) {
        List<String> xhList = etlStudyExceptionManager.queryXyXspyjhXh(xxdm, yxsh, xn);
        if (null != xhList) {
            log.info("Query student count ({})", xhList.size());
            int i = 0;
            for (String xh : xhList) {
//                if ("3172112111134".equals(xh)) {
//                    System.out.println("--------------xh---------------------------");
//                }
                List<EtlStudentStudyPlanXdztDTO> cjList = etlStudyExceptionManager.queryXskccj(xxdm, xh);
                if(null != cjList && !cjList.isEmpty()) {
                    Map<String, EtlStudentStudyPlanXdztDTO> kchCjMap = new HashMap<>();
                    Set<String> kchs = new HashSet();
                    for (EtlStudentStudyPlanXdztDTO cj : cjList) {
                        kchCjMap.put(cj.getKch(), cj);
                        kchs.add(cj.getKch());
                    }
                    List<EtlStudentStudyPlanXdztDTO> pyjhList = etlStudyExceptionManager.queryXsKcPyjh(xh, kchs, xxdm);
                    if (null != pyjhList) {
                        for (EtlStudentStudyPlanXdztDTO jh : pyjhList) {
                            EtlStudentStudyPlanXdztDTO cj = kchCjMap.get(jh.getKch());
                            if (null != cj) {
                                if (null != cj.getCj() && cj.getCj() >= 60) {
                                    jh.setXdzt(10);//通过
                                    jh.setJd(cj.getJd());
                                    if (null == cj.getJd() || cj.getJd() <= 0) {
                                        log.warn("Student cj data exception: pyjh:({}), cj:({})", jh.toString(), cj.toString());
                                    }
                                    //修改修读状况
                                    etlStudyExceptionManager.updateXsStudyPlan(jh);
                                }
                            }
                        }
                    }
                }
                i++;
                if (i > 0 && 0 == i % 50) {
                    log.info("Cal student study plan count({})", i);
                }
            }
        }
        log.info("Cal student study plan end.");
    }
}
