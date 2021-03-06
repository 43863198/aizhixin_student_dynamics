package com.aizhixin.cloud.dataanalysis.analysis.jdbcTemp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.Map;

@Component
public class OverYearTestStatisticsJdbc {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    public Map<String,Object> find(Long orgId, String year, String semester){
        String sql="SELECT tsc.start_time,tsc.end_time FROM `t_school_calendar` AS tsc WHERE tsc.TEACHER_YEAR='"+year+"' AND tsc.SEMESTER='"+semester+"' and tsc.org_id="+orgId;
       return jdbcTemplate.queryForMap(sql);
    }

    public Integer countTotal(Long orgId,String startDate, String collegeCode, String professionCode, String classCode){
        String sql="SELECT COUNT(DISTINCT tx.XH) FROM `t_xsjbxx` AS tx LEFT join `t_cet_score` AS tcs  ON tcs.JOB_NUMBER=tx.XH WHERE tx.RXNY<='"+startDate+"'AND tx.YBYNY>='"+startDate+"' AND tx.DQZT NOT IN ('02','04','16') AND tx.XXID="+orgId+" AND tcs.score>0";
        if (!StringUtils.isEmpty(collegeCode)){
            sql+=" AND tx.`YXSH`='"+collegeCode+"'";
        }
        if (!StringUtils.isEmpty(professionCode)){
            sql+=" AND tx.`ZYH`='"+professionCode+"'";
        }
        if (!StringUtils.isEmpty(classCode)){
            sql+=" AND tx.`BH`='"+classCode+"'";
        }
        return jdbcTemplate.queryForObject(sql,Integer.class);
    }

    public Integer countPassTotal(Long orgId,String type, String collegeCode, String professionCode, String classCode,String startDate,String endDate){
        String sql="SELECT MAX(tcs.score) AS sc FROM `t_xsjbxx` AS tx LEFT JOIN `t_cet_score` AS tcs  ON tcs.JOB_NUMBER=tx.XH WHERE tcs.org_id="+orgId+" and tx.RXNY<='"+startDate+"' AND tx.YBYNY>='"+startDate+"' and tcs.EXAMINATION_DATE >='"+startDate+"'   and tcs.EXAMINATION_DATE<='"+endDate+"'  AND  tcs.`TYPE` LIKE '%大学英语"+type+"%'";
        int passScore = 0;
        if (!StringUtils.isEmpty(collegeCode)){
            sql+=" AND tx.`YXSH`='"+collegeCode+"'";
        }
        if (!StringUtils.isEmpty(professionCode)){
            sql+=" AND tx.`ZYH`='"+professionCode+"'";
        }
        if (!StringUtils.isEmpty(classCode)){
            sql+=" AND tx.`BH`='"+classCode+"'";
        }
        if("三级".equals(type)){
            passScore = 60;
        }else{
            passScore = 425;
        }
        sql+=" GROUP BY tx.xh HAVING MAX(tcs.score)>= '" + passScore + "'";

        String sql2="SELECT COUNT(cc.sc) FROM ("+sql+") AS cc";
        return jdbcTemplate.queryForObject(sql2,Integer.class);
    }


}
