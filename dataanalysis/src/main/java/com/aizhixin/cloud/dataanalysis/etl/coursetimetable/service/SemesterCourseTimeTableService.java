package com.aizhixin.cloud.dataanalysis.etl.coursetimetable.service;

import com.aizhixin.cloud.dataanalysis.etl.coursetimetable.dto.CourseTimeTableDTO;
import com.aizhixin.cloud.dataanalysis.etl.coursetimetable.dto.CourseTimeTableOutDTO;
import com.aizhixin.cloud.dataanalysis.etl.coursetimetable.manager.SemesterCourseTimeTableManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

@Component
@Slf4j
public class SemesterCourseTimeTableService {

    @Autowired
    private SemesterCourseTimeTableManager semesterCourseTimeTableManager;

    public void etlGuiliPkxx(String xn, String xq) {
        List<CourseTimeTableDTO> dlist = semesterCourseTimeTableManager.queryGuiLiPksj(xn, xq);
        if (null == dlist) {
            log.info("XN:{}, XQ:{} not found any data.", xn, xq);
            return;
        }
        log.info("XN:{}, XQ:{} course time table query count:{}", xn, xq, dlist.size());
        List<CourseTimeTableOutDTO> list = new ArrayList<>();
        String tmp = null;
        for (CourseTimeTableDTO d : dlist) {
            CourseTimeTableOutDTO o = new CourseTimeTableOutDTO ();
            o.setOrgId(d.getOrgId());
            o.setJxbh(d.getPkh() + "-" + d.getJxbh());
            o.setJxbmc(d.getJxbmc());
            if (!StringUtils.isEmpty(d.getSksj())) {//处理上课时间:sksj|3 第3、4节 10:20-12:00|5 第5-8节 14:30-18:00|4 第6节 15:25-16:10|3 第3节-中午2 10:20-14:05
                if(d.getSksj().indexOf("中午") > 0) {//中午和单双周不处理
                    continue;
                }
                int p = d.getSksj().indexOf("第");
                if (p > 0) {
                    tmp = d.getSksj().substring(0, p);
                    tmp = tmp.trim();
                    o.setXqj(new Integer(tmp));

                    tmp = d.getSksj().substring(p + 1);
                    p = tmp.indexOf("节");
                    if (p > 0) {
                        tmp = tmp.substring(0, p);
                        String[] ts = tmp.split("-");
                        if (ts.length >= 2) {
                            o.setDjj(new Integer(ts[0].trim()));
                            o.setCxj(new Integer(ts[0].trim()) - o.getDjj() + 1);
                        } else {
                            ts = tmp.split("、");
                            if (ts.length >= 2) {
                                o.setDjj(new Integer(ts[0].trim()));
                                o.setCxj(new Integer(ts[0].trim()) - o.getDjj() + 1);
                            } else {
                                if (org.apache.commons.lang.StringUtils.isNumeric(tmp)) {
                                    o.setQsz(new Integer(tmp));
                                    o.setCxj(1);
                                } else {
                                    log.warn("Not read data 3:{}", d.getSksj());
                                }
                            }
                        }
                    } else {
                        log.warn("Not read data 2:{}", d.getSksj());
                    }
                } else {
                    log.warn("Not read data 1:{}", d.getSksj());
                }
            } else {
                continue;
            }

            if (!StringUtils.isEmpty(d.getSkzc())) {//处理周次：zc|13-14周|7-12,14-19|1-15单周|2-16双周|第4周|6,10-16双|6-7,10-19|7,11-19单
                String[] zcs = d.getSkzc().split(",");
                int c = 1;
                for (String z : zcs) {
                    if (c > 1) {//多个周
                        CourseTimeTableOutDTO o1 = new CourseTimeTableOutDTO ();
                        list.add(o1);
                        o1.setOrgId(o.getOrgId());
                        o1.setJxbh(o.getJxbh());
                        o1.setJxbmc(o.getJxbmc());
                        o1.setXqj(o.getXqj());
                        o1.setDjj(o.getDjj());
                        o1.setCxj(o.getCxj());
                        oneZc(o1, z);
                        list.add(o1);
                    } else {
                        oneZc(o, z);
                        list.add(o);
                    }
                    c++;
                }
            }
        }

        log.info("XN:{}, XQ:{} course time table out table count:{}", xn, xq, list.size());
        if (!list.isEmpty()) {
            semesterCourseTimeTableManager.writeGuiliPksj(list);
        }
    }

    private void oneZc(CourseTimeTableOutDTO o1, String zc) {
        int p = zc.indexOf("单");
        if (p > 0) {
            zc = zc.substring(0, p);
        }
        p = zc.indexOf("双");
        if (p > 0) {
            zc = zc.substring(0, p);
        }
        p = zc.indexOf("周");
        if (p > 0) {
            zc = zc.substring(0, p);
        }
        p = zc.indexOf("第");
        if (p >= 0) {
            zc = zc.substring(p + "第".length());
        }
        p = zc.indexOf("-");
        if (p > 0) {
            o1.setQsz(new Integer(zc.substring(0, p)));
            o1.setJsz(new Integer(zc.substring(p + 1)));
        } else {
            o1.setQsz(new Integer(zc));
            o1.setJsz(o1.getQsz());
        }
    }
}
