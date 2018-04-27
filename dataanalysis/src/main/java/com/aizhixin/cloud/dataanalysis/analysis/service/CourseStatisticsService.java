package com.aizhixin.cloud.dataanalysis.analysis.service;

import com.aizhixin.cloud.dataanalysis.alertinformation.dto.WarningDetailsDTO;
import com.aizhixin.cloud.dataanalysis.analysis.vo.ClassTodayVO;
import com.aizhixin.cloud.dataanalysis.analysis.vo.CurriculumTableDetailsVO;
import com.aizhixin.cloud.dataanalysis.analysis.vo.ExamArrangeVO;
import com.aizhixin.cloud.dataanalysis.common.PageData;
import com.aizhixin.cloud.dataanalysis.common.core.PageUtil;
import com.aizhixin.cloud.dataanalysis.feign.OrgManagerFeignService;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang.StringUtils;
import org.hibernate.SQLQuery;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author: Created by jianwei.wu
 * @E-mail: wujianwei@aizhixin.com
 * @Date: 2018-04-16
 */
@Service
public class CourseStatisticsService {
    @Autowired
    private EntityManager em;

    public Map<String,Object> getClassToday(Long orgId) {

        Map<String, Object> result = new HashMap<>();
        Map<String, Object> condition = new HashMap<>();
        List<ClassTodayVO> classTodayVOList = new ArrayList<>();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        int day = 0;
        long weeks = 0;
        String start = "";
        try {
            if(null!=orgId) {
                StringBuilder sql = new StringBuilder("SELECT START_TIME as start FROM t_school_calendar WHERE ORG_ID ="+orgId+" order by START_TIME desc limit 1");
                Query sq = em.createNativeQuery(sql.toString());
                Object time = sq.getSingleResult();
                if(null!=time){
                    start = time.toString();
                }
            }

            if(!StringUtils.isBlank(start)){
                Date endDate = df.parse(df.format(new Date()));
                Date startDate = df.parse(start);
                //实例化起始和结束Calendar对象
                Calendar startCalendar = Calendar.getInstance();
                Calendar endCalendar = Calendar.getInstance();
                //分别设置Calendar对象的时间
                startCalendar.setTime(startDate);
                endCalendar.setTime(endDate);

                //定义起始日期和结束日期分别属于第几周
                int startWeek = startCalendar.get(Calendar.WEEK_OF_YEAR);
                int endWeek = endCalendar.get(Calendar.WEEK_OF_YEAR);

                //拿到起始日期是星期几
                int startDayOfWeek = startCalendar.get(Calendar.DAY_OF_WEEK);
                if(startDayOfWeek == 1)    {
                    startDayOfWeek = 7;
                    startWeek--;
                }else startDayOfWeek--;

                //拿到结束日期是星期几
                int endDayOfWeek = endCalendar.get(Calendar.DAY_OF_WEEK);
                if(endDayOfWeek == 1) {
                    endDayOfWeek = 7;
                    endWeek--;
                }else endDayOfWeek--;

                //计算相差的周数
                weeks = endWeek - startWeek+1;
//              weeks = (date.getTime() - date1.getTime()) / (7 * 24 * 60 * 60 * 1000);
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(new Date());
                day = calendar.get(Calendar.DAY_OF_WEEK);
                day=day-1;
                if(day<=0){
                    day=7;
                }
            }
            StringBuilder sql1 = new StringBuilder("SELECT count(tc.COURSE_NAME) as count FROM t_curriculum_schedule cs  LEFT JOIN t_teaching_class tc ON cs.TEACHING_CLASS_NUMBER = tc.TEACHING_CLASS_NUMBER where 1 <= (IFNULL(cs.START_PERIOD,0) + IFNULL(cs.PERIOD_NUM,0)-1)");
            StringBuilder sql2 = new StringBuilder("SELECT count(tc.COURSE_NAME) as count FROM t_curriculum_schedule cs  LEFT JOIN t_teaching_class tc ON cs.TEACHING_CLASS_NUMBER = tc.TEACHING_CLASS_NUMBER where 2 <= (IFNULL(cs.START_PERIOD,0) + IFNULL(cs.PERIOD_NUM,0)-1)");
            StringBuilder sql3 = new StringBuilder("SELECT count(tc.COURSE_NAME) as count FROM t_curriculum_schedule cs  LEFT JOIN t_teaching_class tc ON cs.TEACHING_CLASS_NUMBER = tc.TEACHING_CLASS_NUMBER where 3 <= (IFNULL(cs.START_PERIOD,0) + IFNULL(cs.PERIOD_NUM,0)-1)");
            StringBuilder sql4 = new StringBuilder("SELECT count(tc.COURSE_NAME) as count FROM t_curriculum_schedule cs  LEFT JOIN t_teaching_class tc ON cs.TEACHING_CLASS_NUMBER = tc.TEACHING_CLASS_NUMBER where 4 <= (IFNULL(cs.START_PERIOD,0) + IFNULL(cs.PERIOD_NUM,0)-1)");
            StringBuilder sql5 = new StringBuilder("SELECT count(tc.COURSE_NAME) as count FROM t_curriculum_schedule cs  LEFT JOIN t_teaching_class tc ON cs.TEACHING_CLASS_NUMBER = tc.TEACHING_CLASS_NUMBER where 5 <= (IFNULL(cs.START_PERIOD,0) + IFNULL(cs.PERIOD_NUM,0)-1)");
            StringBuilder sql6 = new StringBuilder("SELECT count(tc.COURSE_NAME) as count FROM t_curriculum_schedule cs  LEFT JOIN t_teaching_class tc ON cs.TEACHING_CLASS_NUMBER = tc.TEACHING_CLASS_NUMBER where 6 <= (IFNULL(cs.START_PERIOD,0) + IFNULL(cs.PERIOD_NUM,0)-1)");
            StringBuilder sql7 = new StringBuilder("SELECT count(tc.COURSE_NAME) as count FROM t_curriculum_schedule cs  LEFT JOIN t_teaching_class tc ON cs.TEACHING_CLASS_NUMBER = tc.TEACHING_CLASS_NUMBER where 7 <= (IFNULL(cs.START_PERIOD,0) + IFNULL(cs.PERIOD_NUM,0)-1)");
            StringBuilder sql8 = new StringBuilder("SELECT count(tc.COURSE_NAME) as count FROM t_curriculum_schedule cs  LEFT JOIN t_teaching_class tc ON cs.TEACHING_CLASS_NUMBER = tc.TEACHING_CLASS_NUMBER where 8 <= (IFNULL(cs.START_PERIOD,0) + IFNULL(cs.PERIOD_NUM,0)-1)");
            StringBuilder sql9 = new StringBuilder("SELECT count(tc.COURSE_NAME) as count FROM t_curriculum_schedule cs  LEFT JOIN t_teaching_class tc ON cs.TEACHING_CLASS_NUMBER = tc.TEACHING_CLASS_NUMBER where 9 <= (IFNULL(cs.START_PERIOD,0) + IFNULL(cs.PERIOD_NUM,0)-1)");
            StringBuilder sql10 = new StringBuilder("SELECT count(tc.COURSE_NAME) as count FROM t_curriculum_schedule cs  LEFT JOIN t_teaching_class tc ON cs.TEACHING_CLASS_NUMBER = tc.TEACHING_CLASS_NUMBER where 10 <= (IFNULL(cs.START_PERIOD,0) + IFNULL(cs.PERIOD_NUM,0)-1)");
            StringBuilder sql11 = new StringBuilder("SELECT count(tc.COURSE_NAME) as count FROM t_curriculum_schedule cs  LEFT JOIN t_teaching_class tc ON cs.TEACHING_CLASS_NUMBER = tc.TEACHING_CLASS_NUMBER where 11 <= (IFNULL(cs.START_PERIOD,0) + IFNULL(cs.PERIOD_NUM,0)-1)");
            StringBuilder sql12 = new StringBuilder("SELECT count(tc.COURSE_NAME) as count FROM t_curriculum_schedule cs  LEFT JOIN t_teaching_class tc ON cs.TEACHING_CLASS_NUMBER = tc.TEACHING_CLASS_NUMBER where 12 <= (IFNULL(cs.START_PERIOD,0) + IFNULL(cs.PERIOD_NUM,0)-1)");
            if(null!=orgId){
                sql1.append(" AND cs.ORG_ID = :orgId");
                sql2.append(" AND cs.ORG_ID = :orgId");
                sql3.append(" AND cs.ORG_ID = :orgId");
                sql4.append(" AND cs.ORG_ID = :orgId");
                sql5.append(" AND cs.ORG_ID = :orgId");
                sql6.append(" AND cs.ORG_ID = :orgId");
                sql7.append(" AND cs.ORG_ID = :orgId");
                sql8.append(" AND cs.ORG_ID = :orgId");
                sql9.append(" AND cs.ORG_ID = :orgId");
                sql10.append(" AND cs.ORG_ID = :orgId");
                sql11.append(" AND cs.ORG_ID = :orgId");
                sql12.append(" AND cs.ORG_ID = :orgId");
                condition.put("orgId", orgId);
            }
            if(weeks!=0) {
               sql1.append(" AND cs.START_WEEK <= :startweeks");
               sql1.append(" AND cs.END_WEEK >= :endweeks");
               sql2.append(" AND cs.START_WEEK <= :startweeks");
            sql2.append(" AND cs.END_WEEK >= :endweeks");
            sql3.append(" AND cs.START_WEEK <= :startweeks");
            sql3.append(" AND cs.END_WEEK >= :endweeks");
            sql4.append(" AND cs.START_WEEK <= :startweeks");
            sql4.append(" AND cs.END_WEEK >= :endweeks");
            sql5.append(" AND cs.START_WEEK <= :startweeks");
            sql5.append(" AND cs.END_WEEK >= :endweeks");
            sql6.append(" AND cs.START_WEEK <= :startweeks");
            sql6.append(" AND cs.END_WEEK >= :endweeks");
            sql7.append(" AND cs.START_WEEK <= :startweeks");
            sql7.append(" AND cs.END_WEEK >= :endweeks");
            sql8.append(" AND cs.START_WEEK <= :startweeks");
            sql8.append(" AND cs.END_WEEK >= :endweeks");
            sql9.append(" AND cs.START_WEEK <= :startweeks");
            sql9.append(" AND cs.END_WEEK >= :endweeks");
            sql10.append(" AND cs.START_WEEK <= :startweeks");
            sql10.append(" AND cs.END_WEEK >= :endweeks");
            sql11.append(" AND cs.START_WEEK <= :startweeks");
            sql11.append(" AND cs.END_WEEK >= :endweeks");
            sql12.append(" AND cs.START_WEEK <= :startweeks");
            sql12.append(" AND cs.END_WEEK >= :endweeks");
            condition.put("startweeks", weeks);
            condition.put("endweeks", weeks);
           }
           if(day!=0) {
            sql1.append(" AND cs.DAY_OF_THE_WEEK = :day");
            sql2.append(" AND cs.DAY_OF_THE_WEEK = :day");
            sql3.append(" AND cs.DAY_OF_THE_WEEK = :day");
            sql4.append(" AND cs.DAY_OF_THE_WEEK = :day");
            sql5.append(" AND cs.DAY_OF_THE_WEEK = :day");
            sql6.append(" AND cs.DAY_OF_THE_WEEK = :day");
            sql7.append(" AND cs.DAY_OF_THE_WEEK = :day");
            sql8.append(" AND cs.DAY_OF_THE_WEEK = :day");
            sql9.append(" AND cs.DAY_OF_THE_WEEK = :day");
            sql10.append(" AND cs.DAY_OF_THE_WEEK = :day");
            sql11.append(" AND cs.DAY_OF_THE_WEEK = :day");
            sql12.append(" AND cs.DAY_OF_THE_WEEK = :day");
            condition.put("day", day);
           }
            Query sq1 = em.createNativeQuery(sql1.toString());
            Query sq2 = em.createNativeQuery(sql2.toString());
            Query sq3 = em.createNativeQuery(sql3.toString());
            Query sq4 = em.createNativeQuery(sql4.toString());
            Query sq5 = em.createNativeQuery(sql5.toString());
            Query sq6 = em.createNativeQuery(sql6.toString());
            Query sq7 = em.createNativeQuery(sql7.toString());
            Query sq8 = em.createNativeQuery(sql8.toString());
            Query sq9 = em.createNativeQuery(sql9.toString());
            Query sq10 = em.createNativeQuery(sql10.toString());
            Query sq11 = em.createNativeQuery(sql11.toString());
            Query sq12 = em.createNativeQuery(sql12.toString());
            for (Map.Entry<String, Object> e : condition.entrySet()) {
                sq1.setParameter(e.getKey(), e.getValue());
                sq2.setParameter(e.getKey(), e.getValue());
                sq3.setParameter(e.getKey(), e.getValue());
                sq4.setParameter(e.getKey(), e.getValue());
                sq5.setParameter(e.getKey(), e.getValue());
                sq6.setParameter(e.getKey(), e.getValue());
                sq7.setParameter(e.getKey(), e.getValue());
                sq8.setParameter(e.getKey(), e.getValue());
                sq9.setParameter(e.getKey(), e.getValue());
                sq10.setParameter(e.getKey(), e.getValue());
                sq11.setParameter(e.getKey(), e.getValue());
                sq12.setParameter(e.getKey(), e.getValue());
            }
            ClassTodayVO ct1 = new ClassTodayVO();
            int count1 = Integer.valueOf(sq1.getSingleResult().toString());
            ct1.setPeriod(1);
            ct1.setCourseCount(count1);
            classTodayVOList.add(ct1);
            ClassTodayVO ct2 = new ClassTodayVO();
            int count2 = Integer.valueOf(sq2.getSingleResult().toString());
            ct2.setPeriod(2);
            ct2.setCourseCount(count2);
            classTodayVOList.add(ct2);
            ClassTodayVO ct3 = new ClassTodayVO();
            int count3 = Integer.valueOf(sq3.getSingleResult().toString());
            ct3.setPeriod(3);
            ct3.setCourseCount(count3);
            classTodayVOList.add(ct3);
            ClassTodayVO ct4 = new ClassTodayVO();
            int count4 = Integer.valueOf(sq4.getSingleResult().toString());
            ct4.setPeriod(4);
            ct4.setCourseCount(count4);
            classTodayVOList.add(ct4);
            ClassTodayVO ct5 = new ClassTodayVO();
            int count5 = Integer.valueOf(sq5.getSingleResult().toString());
            ct5.setPeriod(5);
            ct5.setCourseCount(count5);
            classTodayVOList.add(ct5);
            ClassTodayVO ct6 = new ClassTodayVO();
            int count6 = Integer.valueOf(sq6.getSingleResult().toString());
            ct6.setPeriod(6);
            ct6.setCourseCount(count6);
            classTodayVOList.add(ct6);
            ClassTodayVO ct7 = new ClassTodayVO();
            int count7 = Integer.valueOf(sq7.getSingleResult().toString());
            ct7.setPeriod(7);
            ct7.setCourseCount(count7);
            classTodayVOList.add(ct7);
            ClassTodayVO ct8 = new ClassTodayVO();
            int count8 = Integer.valueOf(sq8.getSingleResult().toString());
            ct8.setPeriod(8);
            ct8.setCourseCount(count8);
            classTodayVOList.add(ct8);
            ClassTodayVO ct9 = new ClassTodayVO();
            int count9 = Integer.valueOf(sq9.getSingleResult().toString());
            ct9.setPeriod(9);
            ct9.setCourseCount(count9);
            classTodayVOList.add(ct9);
            ClassTodayVO ct10 = new ClassTodayVO();
            int count10 = Integer.valueOf(sq10.getSingleResult().toString());
            ct10.setPeriod(10);
            ct10.setCourseCount(count10);
            classTodayVOList.add(ct10);
            ClassTodayVO ct11 = new ClassTodayVO();
            int count11 = Integer.valueOf(sq11.getSingleResult().toString());
            ct11.setPeriod(11);
            ct11.setCourseCount(count1);
            classTodayVOList.add(ct11);
            ClassTodayVO ct12 = new ClassTodayVO();
            int count12 = Integer.valueOf(sq12.getSingleResult().toString());
            ct12.setPeriod(12);
            ct12.setCourseCount(count12);
            classTodayVOList.add(ct12);
            result.put("success",true);
            result.put("data",classTodayVOList);
            return result;
        } catch (ParseException e) {
            e.printStackTrace();
            result.put("success", false);
            result.put("message","今日课程统计失败！");
            return result;
        }
    }

    public PageData<CurriculumTableDetailsVO> getTodayDetail(Long orgId, Integer period,Integer pageNumber, Integer pageSize) {
        PageData<CurriculumTableDetailsVO> p = new PageData<>();
        List<CurriculumTableDetailsVO> dataList = new ArrayList<>();
        Map<String, Object> condition = new HashMap<>();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        int day = 0;
        long weeks = 0;
        String start = "";
        try {
            if(null!=orgId) {
                StringBuilder sql = new StringBuilder("SELECT START_TIME as start FROM t_school_calendar WHERE ORG_ID ="+orgId+" order by START_TIME desc limit 1");
                Query sq = em.createNativeQuery(sql.toString());
                Object time = sq.getSingleResult();
                if(null!=time){
                    start = time.toString();
                }
            }
            if(!StringUtils.isBlank(start)){
                Date endDate = df.parse(df.format(new Date()));
                Date startDate = df.parse(start);
                //实例化起始和结束Calendar对象
                Calendar startCalendar = Calendar.getInstance();
                Calendar endCalendar = Calendar.getInstance();
                //分别设置Calendar对象的时间
                startCalendar.setTime(startDate);
                endCalendar.setTime(endDate);

                //定义起始日期和结束日期分别属于第几周
                int startWeek = startCalendar.get(Calendar.WEEK_OF_YEAR);
                int endWeek = endCalendar.get(Calendar.WEEK_OF_YEAR);

                //拿到起始日期是星期几
                int startDayOfWeek = startCalendar.get(Calendar.DAY_OF_WEEK);
                if(startDayOfWeek == 1)    {
                    startDayOfWeek = 7;
                    startWeek--;
                }else startDayOfWeek--;

                //拿到结束日期是星期几
                int endDayOfWeek = endCalendar.get(Calendar.DAY_OF_WEEK);
                if(endDayOfWeek == 1) {
                    endDayOfWeek = 7;
                    endWeek--;
                }else endDayOfWeek--;

                //计算相差的周数
                weeks = endWeek - startWeek+1;
//              weeks = (date.getTime() - date1.getTime()) / (7 * 24 * 60 * 60 * 1000);
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(new Date());
                day = calendar.get(Calendar.DAY_OF_WEEK);
                day=day-1;
                if(day<=0){
                    day=7;
                }
            }
            StringBuilder sql = new StringBuilder("SELECT m.START_PERIOD AS sp,m.PERIOD_NUM AS pn, m.TEACHER_NAME AS tn,m.TEACHING_CLASS_NAME AS tcn," +
                    "cr.TEACHING_BUILDING_NUMBER AS tbn,cr.CLASSROOM_NAME AS crn,d.COMPANY_NAME AS cn ");
            sql.append("FROM(SELECT cs.START_PERIOD,cs.PERIOD_NUM,ct.TEACHER_NAME,ct.TEACHING_CLASS_NAME, ct.PLACE,ct.SET_UP_UNIT FROM(SELECT " +
                    "START_PERIOD,PERIOD_NUM,TEACHING_CLASS_NAME FROM t_curriculum_schedule WHERE 1 = 1");
            StringBuilder cql = new StringBuilder("SELECT  count(cs.TEACHING_CLASS_NAME) as count FROM (SELECT DISTINCT TEACHING_CLASS_NAME FROM t_curriculum_schedule WHERE 1 = 1 ");
            if(null!=orgId){
                sql.append(" AND ORG_ID = :orgId");
                cql.append(" AND ORG_ID = :orgId");
                condition.put("orgId", orgId);
            }
            if(weeks!=0) {
                sql.append(" AND START_WEEK <= :startweeks");
                sql.append(" AND END_WEEK >= :endweeks");
                cql.append(" AND START_WEEK <= :startweeks");
                cql.append(" AND END_WEEK >= :endweeks");
                condition.put("startweeks", weeks);
                condition.put("endweeks", weeks);
            }
            if(day!=0) {
                sql.append(" AND DAY_OF_THE_WEEK = :day");
                cql.append(" AND DAY_OF_THE_WEEK = :day");
                condition.put("day", day);
            }
            if(null!=period){
                sql.append(" AND (IFNULL(START_PERIOD,0) + IFNULL(PERIOD_NUM,0)-1) >= :period");
                cql.append(" AND (IFNULL(START_PERIOD,0) + IFNULL(PERIOD_NUM,0)-1) >= :period");
                condition.put("period", period);
            }
            sql.append("  GROUP BY TEACHING_CLASS_NAME) cs LEFT JOIN t_course_timetable ct ON cs.TEACHING_CLASS_NAME = ct.TEACHING_CLASS_NAME  GROUP BY cs.TEACHING_CLASS_NAME) m");
            sql.append(" LEFT JOIN t_class_room cr ON cr.CLASSROOM_NAME = m.PLACE LEFT " +
                    "JOIN t_department d ON m.SET_UP_UNIT = d.COMPANY_NUMBER");
            cql.append(" ) cs");
            Query sq = em.createNativeQuery(sql.toString());
            Query cq = em.createNativeQuery(cql.toString());
            for (Map.Entry<String, Object> e : condition.entrySet()) {
                sq.setParameter(e.getKey(), e.getValue());
                cq.setParameter(e.getKey(), e.getValue());
            }
            sq.unwrap(SQLQuery.class).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
            Long count = Long.valueOf(String.valueOf(cq.getSingleResult()));
            if(pageNumber<1){
                pageNumber =1;
            }
            if(null==pageSize){
                pageSize = 20;
            }
            sq.setFirstResult((pageNumber-1) * pageSize);
            sq.setMaxResults(pageSize);
            List<Object> res = sq.getResultList();
            for (Object obj : res) {
                Map row = (Map) obj;
                CurriculumTableDetailsVO cd = new CurriculumTableDetailsVO();
                if(null!=row.get("tcn")){
                    cd.setTeachingClassName(row.get("tcn").toString());
                }
                if(null!=row.get("cn")){
                    cd.setCourseUnit(row.get("cn").toString());
                }
                if(null!=row.get("tn")){
                    cd.setTeacherName(row.get("tn").toString());
                }
                if(null!=row.get("sp")){
                    cd.setStartSection(Integer.valueOf(row.get("sp").toString()));
                }
                if(null!=row.get("pn")){
                    cd.setEndSection(Integer.valueOf(row.get("pn").toString()) + cd.getStartSection());
                }
                if(null!=row.get("tbn")){
                    cd.setTeachingBuilding(row.get("tbn").toString());
                }
                if(null!=row.get("crn")){
                    cd.setClassRoom(row.get("tn").toString());
                }
                dataList.add(cd);
            }
            p.setData(dataList);
            p.getPage().setPageNumber(pageNumber);
            p.getPage().setPageSize(pageSize);
            p.getPage().setTotalElements(count);
            p.getPage().setTotalPages(PageUtil.cacalatePagesize(count, p.getPage().getPageSize()));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return p;
    }

}
