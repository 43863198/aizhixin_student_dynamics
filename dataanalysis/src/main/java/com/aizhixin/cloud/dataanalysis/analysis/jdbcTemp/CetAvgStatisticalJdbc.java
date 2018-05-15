package com.aizhixin.cloud.dataanalysis.analysis.jdbcTemp;

import com.aizhixin.cloud.dataanalysis.analysis.domain.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.util.List;

@Component
public class CetAvgStatisticalJdbc {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    private final Logger logger= LoggerFactory.getLogger(CetAvgStatisticalJdbc.class);

    public Integer getInt(double number){
        BigDecimal bd=new BigDecimal(number).setScale(0, BigDecimal.ROUND_HALF_UP);
        return Integer.parseInt(bd.toString());
    }
    public List<AvgDomain> avgInfo(Long orgId, String collegeCode, String professionCode, String classCode, String cetType){
        String sql=" SELECT max(tcs.`SCORE`) as sc";
        if (StringUtils.isEmpty(collegeCode)){
            sql+=" ,ts.`YXSMC` as `name` ";
        }else{
            if (!StringUtils.isEmpty(professionCode)){
                if (!StringUtils.isEmpty(classCode)){
                    sql+=" ,ts.`BJMC` as `name` ";
                }else{
                    sql+=" ,ts.`ZYMC` as `name` ";
                }
            }else{
                sql+=" ,ts.`ZYMC` as `name` ";
            }
        }
        sql+= "FROM `t_xsjbxx` AS ts  LEFT JOIN `t_cet_score` AS tcs ON ts.`xh`=tcs.`JOB_NUMBER` WHERE ts.`XXID`="+orgId+" AND  ts.`YBYNY`>CURRENT_TIMESTAMP AND ts.`RXNY`<CURRENT_TIMESTAMP AND tcs.`SCORE`>0 ";
        if (!StringUtils.isEmpty(collegeCode)){
            sql+=" AND ts.`YXSH`='"+collegeCode+"'";
        }
        if (!StringUtils.isEmpty(professionCode)){
            sql+=" AND ts.`ZYH`='"+professionCode+"'";
        }
        if (!StringUtils.isEmpty(classCode)){
            sql+=" AND ts.`BH`='"+classCode+"'";
        }
        sql+=" AND tcs.`TYPE` LIKE '%大学英语"+cetType+"%' GROUP BY tcs.`JOB_NUMBER`";
//        if (StringUtils.isEmpty(collegeCode)){
//            sql+=" GROUP BY ts.`YXSH`";
//        }else{
//            if (!StringUtils.isEmpty(professionCode)){
//                if (!StringUtils.isEmpty(classCode)){
//                    sql+=" GROUP BY ts.`ZYH`";
//                }else{
//                    sql+=" GROUP BY ts.`BH`";
//                }
//            }else{
//                sql+=" GROUP BY ts.`ZYH`";
//            }
//        }

        String sql2="SELECT c.name,ROUND(AVG(c.sc),2) AS sc FROM ("+sql+") AS c GROUP BY c.name";

        logger.info("sql 语句>>>>>> "+sql2);
        RowMapper<AvgDomain> rowMapper= (rs, rowNum) -> {
            AvgDomain avgDomain=new AvgDomain();
            avgDomain.setName(rs.getString("name"));
            avgDomain.setScore(getInt(rs.getDouble("sc")));
            return avgDomain;
        };
        return jdbcTemplate.query(sql2,rowMapper);
    }


    public List<AvgSexDomain> avgXbInfo(Long orgId, String collegeCode, String professionCode, String classCode, String cetType){
        String sql=" SELECT max(tcs.`SCORE`) as sc,ts.`XB` ";
        sql+= "FROM `t_xsjbxx` AS ts  LEFT JOIN `t_cet_score` AS tcs ON ts.`xh`=tcs.`JOB_NUMBER` WHERE ts.`XXID`="+orgId+" AND  ts.`YBYNY`>CURRENT_TIMESTAMP AND ts.`RXNY`<CURRENT_TIMESTAMP AND tcs.`SCORE`> 0";
        if (!StringUtils.isEmpty(collegeCode)){
            sql+=" AND ts.`YXSH`='"+collegeCode+"'";
        }
        if (!StringUtils.isEmpty(professionCode)){
            sql+=" AND ts.`ZYH`='"+professionCode+"'";
        }
        if (!StringUtils.isEmpty(classCode)){
            sql+=" AND ts.`BH`='"+classCode+"'";
        }
        sql+=" AND tcs.`TYPE` LIKE '%大学英语"+cetType+"%' GROUP BY ts.`xh`";

        String sql2="SELECT c.xb,ROUND(AVG(c.sc),2) AS sc FROM ("+sql+") as c GROUP BY c.xb";
        logger.info("sql 语句>>>>>> "+sql2);
        RowMapper<AvgSexDomain> rowMapper= (rs, rowNum) -> {
            AvgSexDomain avgSexDomain=new AvgSexDomain();
            avgSexDomain.setSex(rs.getString("xb"));
            avgSexDomain.setScore(getInt(rs.getDouble("sc")));
            return avgSexDomain;
        };
        return jdbcTemplate.query(sql2,rowMapper);
    }


    public List<AvgAgeDomain> avgAgeInfo(Long orgId, String collegeCode, String professionCode, String classCode, String cetType){
        String sql=" SELECT max(tcs.`SCORE`) as sc,ts.`nl` ";
        sql+= "FROM `t_xsjbxx` AS ts  LEFT JOIN `t_cet_score` AS tcs ON ts.`xh`=tcs.`JOB_NUMBER` WHERE ts.`XXID`="+orgId+" AND  ts.`YBYNY`>CURRENT_TIMESTAMP AND ts.`RXNY`<CURRENT_TIMESTAMP AND tcs.`SCORE`>0  and ts.nl is not null";
        if (!StringUtils.isEmpty(collegeCode)){
            sql+=" AND ts.`YXSH`='"+collegeCode+"'";
        }
        if (!StringUtils.isEmpty(professionCode)){
            sql+=" AND ts.`ZYH`='"+professionCode+"'";
        }
        if (!StringUtils.isEmpty(classCode)){
            sql+=" AND ts.`BH`='"+classCode+"'";
        }
        sql+=" AND tcs.`TYPE` LIKE '%大学英语"+cetType+"%' GROUP BY ts.`xh` ";
        String sql2="SELECT c.nl,ROUND(AVG(c.sc),2) AS sc FROM ("+sql+") as c GROUP BY c.nl";
        logger.info("sql 语句>>>>>> "+sql2);
        RowMapper<AvgAgeDomain> rowMapper= (rs, rowNum) -> {
            AvgAgeDomain avgAgeDomain=new AvgAgeDomain();
            avgAgeDomain.setAge(rs.getInt("nl"));
            avgAgeDomain.setScore(getInt(rs.getDouble("sc")));
            return avgAgeDomain;
        };
        return jdbcTemplate.query(sql2,rowMapper);
    }


    public List<AvgNjDomain> avgNjInfo(Long orgId, String collegeCode, String professionCode, String classCode, String cetType){
        String sql=" SELECT max(tcs.`SCORE`) as sc,ts.`NJ` ";
        sql+= "FROM `t_xsjbxx` AS ts  LEFT JOIN `t_cet_score` AS tcs ON ts.`xh`=tcs.`JOB_NUMBER` WHERE ts.`XXID`="+orgId+" AND  ts.`YBYNY`>CURRENT_TIMESTAMP AND tcs.`SCORE`>0 AND ts.`RXNY`<CURRENT_TIMESTAMP";
        if (!StringUtils.isEmpty(collegeCode)){
            sql+=" AND ts.`YXSH`='"+collegeCode+"'";
        }
        if (!StringUtils.isEmpty(professionCode)){
            sql+=" AND ts.`ZYH`='"+professionCode+"'";
        }
        if (!StringUtils.isEmpty(classCode)){
            sql+=" AND ts.`BH`='"+classCode+"'";
        }
        sql+=" AND tcs.`TYPE` LIKE '%大学英语"+cetType+"%' GROUP BY ts.`xh`";
        String sql2="SELECT c.nj,ROUND(AVG(c.sc),2) AS sc FROM ("+sql+") as c group by c.nj";
        logger.info("sql 语句>>>>>> "+sql2);
        RowMapper<AvgNjDomain> rowMapper= (rs, rowNum) -> {
            AvgNjDomain avgNjDomain=new AvgNjDomain();
            avgNjDomain.setNj(rs.getString("nj"));
            avgNjDomain.setScore(getInt(rs.getDouble("sc")));
            return avgNjDomain;
        };
        return jdbcTemplate.query(sql2,rowMapper);
    }
}
