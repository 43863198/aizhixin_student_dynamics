package com.aizhixin.cloud.dataanalysis.zb.manager;

import com.aizhixin.cloud.dataanalysis.analysis.vo.CetDetailVO;
import com.aizhixin.cloud.dataanalysis.common.PageData;
import com.aizhixin.cloud.dataanalysis.zb.app.domain.NjStatisticalInfoDomain;
import com.aizhixin.cloud.dataanalysis.zb.app.domain.TotalInfoDomain;
import com.aizhixin.cloud.dataanalysis.zb.app.vo.AvgVo;
import com.aizhixin.cloud.dataanalysis.zb.app.vo.CurrentStatisticsVO;
import com.aizhixin.cloud.dataanalysis.zb.app.vo.EnglishLevelBigScreenVO;
import com.aizhixin.cloud.dataanalysis.zb.app.vo.OrganizationStatisticsVO;
import org.hibernate.SQLQuery;
import org.hibernate.id.IncrementGenerator;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@Transactional
public class IndexAnalysisAppManager {

    private final static String SQL_DJ_DP_TGL = "SELECT XN,XQM,KSLX,ZXRS, CKRC,TGRC FROM t_zb_djksjc WHERE XXDM=? and BH=? and DHLJ=? AND KSLX=?  AND XN = (SELECT max(XN) FROM t_zb_djksjc WHERE KSLX=?) ORDER BY XQM DESC LIMIT 1";

    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Autowired
    private EntityManager em;


    @Transactional(readOnly = true)
    public List<EnglishLevelBigScreenVO> getNewLevelTestBigScreenPass(Long orgId) {
        List<EnglishLevelBigScreenVO> rsList = new ArrayList<>();
        List<EnglishLevelBigScreenVO> list = jdbcTemplate.query(SQL_DJ_DP_TGL, new Object[]{orgId.toString(), orgId.toString(), "1", "3", "3"}, new int[]{Types.VARCHAR, Types.VARCHAR, Types.VARCHAR, Types.VARCHAR, Types.VARCHAR}, new RowMapper<EnglishLevelBigScreenVO>() {
            public EnglishLevelBigScreenVO mapRow(ResultSet rs, int rowNum) throws SQLException {
                return new EnglishLevelBigScreenVO(rs.getString("XN"), rs.getString("XQM"),
                        rs.getString("KSLX"), rs.getLong("ZXRS"),
                        rs.getLong("CKRC"), rs.getLong("TGRC"));
            }
        });
        if (null != list && list.size() > 0) {
            rsList.add(list.get(0));
        }

        list = jdbcTemplate.query(SQL_DJ_DP_TGL, new Object[]{orgId.toString(), orgId.toString(), "1", "4", "4"}, new int[]{Types.VARCHAR, Types.VARCHAR, Types.VARCHAR, Types.VARCHAR, Types.VARCHAR}, new RowMapper<EnglishLevelBigScreenVO>() {
            public EnglishLevelBigScreenVO mapRow(ResultSet rs, int rowNum) throws SQLException {
                return new EnglishLevelBigScreenVO(rs.getString("XN"), rs.getString("XQM"),
                        rs.getString("KSLX"), rs.getLong("ZXRS"),
                        rs.getLong("CKRC"), rs.getLong("TGRC"));
            }
        });
        if (null != list && list.size() > 0) {
            rsList.add(list.get(0));
        }

        list = jdbcTemplate.query(SQL_DJ_DP_TGL, new Object[]{orgId.toString(), orgId.toString(), "1", "6", "6"}, new int[]{Types.VARCHAR, Types.VARCHAR, Types.VARCHAR, Types.VARCHAR, Types.VARCHAR}, new RowMapper<EnglishLevelBigScreenVO>() {
            public EnglishLevelBigScreenVO mapRow(ResultSet rs, int rowNum) throws SQLException {
                return new EnglishLevelBigScreenVO(rs.getString("XN"), rs.getString("XQM"),
                        rs.getString("KSLX"), rs.getLong("ZXRS"),
                        rs.getLong("CKRC"), rs.getLong("TGRC"));
            }
        });
        if (null != list && list.size() > 0) {
            rsList.add(list.get(0));
        }
        return rsList;
    }

    @Transactional(readOnly = true)
    public Map<String, Object> cetSingleDataStatistics(Long orgId,String cetType, String teacherYear, String semester, String collegeCode, String professionCode,String className) {
        Map<String, Object> result = new HashMap<>();
        Map<String, Object> condition = new HashMap<>();
        try {
            String XN = teacherYear + "-" + (Integer.valueOf(teacherYear) + 1);
            StringBuilder sql = new StringBuilder("SELECT ZXRS as total,CKRC as joinTotal,TGRC as passTotal,ZF as totalScore,GF as maxScore FROM t_zb_djksjc where 1=1");
            sql.append(" and DHLJ = '2' ");
            sql.append(" and KSLX= :cetType ");
            sql.append(" and XN IN ('" + XN + "') and XQM = :semester ");
            condition.put("cetType", cetType);
            condition.put("semester", semester);

            if (null == collegeCode && null == professionCode && null == className) {
                sql.append(" and BH = :orgId ");
                condition.put("orgId", orgId);
            } else if (null == professionCode && null == className) {
                sql.append(" and BH = :collegeCode and P_BH = :orgId ");
                condition.put("collegeCode", collegeCode);
                condition.put("orgId", orgId);
            } else if (null == className) {
                sql.append(" and BH = :professionCode and P_BH = :collegeCode ");
                condition.put("professionCode", professionCode);
                condition.put("collegeCode", collegeCode);
            } else {
                sql.append(" and BH = :className and P_BH = :professionCode ");
                condition.put("className", className);
                condition.put("professionCode", professionCode);
            }

            Query sq = em.createNativeQuery(sql.toString());
            sq.unwrap(SQLQuery.class).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
            for (Map.Entry<String, Object> e : condition.entrySet()) {
                sq.setParameter(e.getKey(), e.getValue());
            }
            Map res = (Map) sq.getSingleResult();
            CurrentStatisticsVO data = new CurrentStatisticsVO();
            if (null != res.get("total")) {
                data.setTotal(Integer.valueOf(res.get("total").toString()));
            }
            if (null != res.get("joinTotal")) {
                data.setJoinTotal(Integer.valueOf(res.get("joinTotal").toString()));
            }
            if (null != res.get("passTotal")) {
                data.setPass(Integer.valueOf(res.get("passTotal").toString()));
            }
            //通过人员的成绩均值为 总分/通过人数
            if (null != res.get("totalScore") && null != res.get("joinTotal")) {
                data.setAvg(new BigDecimal(res.get("totalScore").toString()).divide((new BigDecimal(res.get("joinTotal").toString())), 2, BigDecimal.ROUND_HALF_DOWN) + "");
            }
            //通过率为：通过人数/在校人数
            if (data.getTotal() > 0) {
                data.setRate(new DecimalFormat("0.00").format((double) data.getPass() * 100 / data.getTotal()));
            }
            if(null != res.get("maxScore")){
                data.setMax(res.get("maxScore").toString());
            }
            result.put("success", true);
            result.put("data", data);
            return result;

        } catch (Exception e) {
            e.printStackTrace();
            result.put("success", false);
            result.put("message", "单次考试统计分析---数据统计失败！");
            return result;
        }
    }

    @Transactional(readOnly = true)
    public Map<String, Object> currentStatistics(Long orgId, String cetType, String collegeCode, String professionCode, String className) {
        Map<String, Object> result = new HashMap<>();
        Map<String, Object> condition = new HashMap<>();
        try {
            StringBuilder sql = new StringBuilder("SELECT ZXRS as total,CKRC as joinTotal,TGRC as passTotal,ZF as totalScore FROM t_zb_djksjc where 1=1");
            sql.append(" and DHLJ = '1'");
            sql.append(" and KSLX='" + cetType + "'");
            sql.append(" and XN = (SELECT max(XN) FROM t_zb_djksjc WHERE KSLX='" + cetType + "')");

            if (null == collegeCode && null == professionCode && null == className) {
                sql.append(" and BH = :orgId");
                condition.put("orgId", orgId);
            } else if (null == professionCode && null == className) {
                sql.append(" and BH = :collegeCode");
                sql.append(" and P_BH = :orgId ");
                condition.put("collegeCode", collegeCode);
                condition.put("orgId", orgId);
            } else if (null == className) {
                sql.append(" and BH = :professionCode");
                sql.append(" and P_BH = :collegeCode");
                condition.put("professionCode", professionCode);
                condition.put("collegeCode", collegeCode);
            } else {
                sql.append(" and BH = :className");
                sql.append(" and P_BH = :professionCode");
                condition.put("className", className);
                condition.put("professionCode", professionCode);
            }
            Query sq = em.createNativeQuery(sql.toString());
            sq.unwrap(SQLQuery.class).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
            for (Map.Entry<String, Object> e : condition.entrySet()) {
                sq.setParameter(e.getKey(), e.getValue());
            }
            Map res = (Map) sq.getSingleResult();
            CurrentStatisticsVO data = new CurrentStatisticsVO();
            if (null != res.get("total")) {
                data.setTotal(Integer.valueOf(res.get("total").toString()));
            }
            if (null != res.get("joinTotal")) {
                data.setJoinTotal(Integer.valueOf(res.get("joinTotal").toString()));
            }
            if (null != res.get("passTotal")) {
                data.setPass(Integer.valueOf(res.get("passTotal").toString()));
            }
            //通过人员的成绩均值为 总分/通过人数
            if (null != res.get("totalScore") && null != res.get("joinTotal")) {
                data.setAvg(new BigDecimal(res.get("totalScore").toString()).divide((new BigDecimal(res.get("joinTotal").toString())), 2, BigDecimal.ROUND_HALF_DOWN) + "");
            }
            //通过率为：通过人数/在校人数
            if (data.getTotal() > 0) {
                data.setRate(new DecimalFormat("0.00").format((double) data.getPass() * 100 / data.getTotal()));
            }
            result.put("success", true);
            result.put("data", data);
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            result.put("success", false);
            result.put("message", "等级考试当前状况---数据统计失败！");
            return result;
        }
    }

    @Transactional(readOnly = true)
    public Map<String, Object> organizationStatistics(Long orgId, String cetType, String collegeCode, String professionCode, String className) {
        Map<String, Object> result = new HashMap<>();
        Map<String, Object> condition = new HashMap<>();
        List<OrganizationStatisticsVO> csnpList = new ArrayList<>();
        try {
            StringBuilder sql = new StringBuilder("select tzbj.ZXRS as total,tzbj.CKRC as joinTotal,tzbj.TGRC as pass");
            StringBuilder sexSql = new StringBuilder("select SUM(tzbj.NRC) as manTotal,SUM(tzbj.VRC) as womanTotal, SUM(tzbj.NTGRC) as manPass,SUM(tzbj.VTGRC) as womanPass");
            StringBuilder njSql = new StringBuilder("select tzbj.NJ as nj,SUM(tzbj.ZXRS) as total,SUM(tzbj.CKRC ) as joinTotal,SUM(tzbj.TGRC) as passTotal");

            if (null == collegeCode && null == professionCode && null == className) {
                sql.append(",tt.COMPANY_NAME as name,tt.SIMPLE_NAME as simple_name from t_zb_djksjc as tzbj, t_department as tt where 1=1");
                sexSql.append(" from t_zb_djksjc as tzbj, t_department as tt where 1=1");
                njSql.append(" from t_zb_djksnj as tzbj, t_department as tt where 1=1");
                sql.append(" and tzbj.P_BH = :orgId");
                sql.append(" and tzbj.BH = tt.COMPANY_NUMBER");
                sexSql.append(" and tzbj.P_BH = :orgId");
                sexSql.append(" and tzbj.BH = tt.COMPANY_NUMBER");
                njSql.append(" and tzbj.P_BH = :orgId");
                njSql.append(" and tzbj.BH = tt.COMPANY_NUMBER");
                condition.put("orgId", orgId);
            } else if (null == professionCode && null == className) {
                sql.append(", tt.NAME as name from t_zb_djksjc as tzbj, t_profession as tt where 1=1");
                sexSql.append(" from t_zb_djksjc as tzbj, t_profession as tt where 1=1");
                njSql.append(" from t_zb_djksnj as tzbj, t_profession as tt where 1=1");
                sql.append(" and tzbj.BH = :collegeCode");
                sql.append(" and tzbj.P_BH = :orgId ");
                sql.append(" and tzbj.BH = tt.COMPANY_NUMBER");

                sexSql.append(" and tzbj.BH = :collegeCode");
                sexSql.append(" and tzbj.P_BH = :orgId ");
                sexSql.append(" and tzbj.BH = tt.COMPANY_NUMBER");

                njSql.append(" and tzbj.BH = :collegeCode");
                njSql.append(" and tzbj.P_BH = :orgId ");
                njSql.append(" and tzbj.BH = tt.COMPANY_NUMBER");
                condition.put("collegeCode", collegeCode);
                condition.put("orgId", orgId);
            } else if (null == className) {
                sql.append(", tt.NAME as name from t_zb_djksjc as tzbj, t_class as tt where 1=1");
                sexSql.append(" from t_zb_djksjc as tzbj, t_class as tt where 1=1");
                njSql.append(" from t_zb_djksnj as tzbj, t_class as tt where 1=1");
                sql.append(" and tzbj.BH = :professionCode");
                sql.append(" and tzbj.P_BH = :collegeCode");
                sql.append(" and tzbj.BH = tt.PROFESSION_CODE");

                sexSql.append(" and tzbj.BH = :professionCode");
                sexSql.append(" and tzbj.P_BH = :collegeCode");
                sexSql.append(" and tzbj.BH = tt.PROFESSION_CODE");

                njSql.append(" and tzbj.BH = :professionCode");
                njSql.append(" and tzbj.P_BH = :collegeCode");
                njSql.append(" and tzbj.BH = tt.PROFESSION_CODE");
                condition.put("professionCode", professionCode);
                condition.put("collegeCode", collegeCode);
            } else {
                sql.append(", tt.NAME as name from t_zb_djksjc as tzbj, t_class as tt where 1=1");
                sexSql.append(" from t_zb_djksjc as tzbj, t_class as tt where 1=1");
                njSql.append(" from t_zb_djksnj as tzbj, t_class as tt where 1=1");
                sql.append(" and tzbj.BH = :className");
                sql.append(" and tzbj.P_BH = :professionCode");
                sql.append(" and tzbj.BH = tt.NAME");

                sexSql.append(" and tzbj.BH = :className");
                sexSql.append(" and tzbj.P_BH = :professionCode");
                sexSql.append(" and tzbj.BH = tt.NAME");

                njSql.append(" and tzbj.BH = :className");
                njSql.append(" and tzbj.P_BH = :professionCode");
                njSql.append(" and tzbj.BH = tt.NAME");
                condition.put("className", className);
                condition.put("professionCode", professionCode);
            }

            sql.append(" and XN = (SELECT max(XN) FROM t_zb_djksjc WHERE KSLX='" + cetType + "') and tzbj.DHLJ = '1' and tzbj.KSLX='" + cetType + "'");
            sexSql.append(" and XN = (SELECT max(XN) FROM t_zb_djksjc WHERE KSLX='" + cetType + "') and tzbj.DHLJ = '1' and tzbj.KSLX='" + cetType + "'");
            njSql.append(" and XN = (SELECT max(XN) FROM t_zb_djksjc WHERE KSLX='" + cetType + "') and tzbj.DHLJ = '1' and tzbj.KSLX='" + cetType + "' GROUP BY nj ORDER BY nj");
            Query sq = em.createNativeQuery(sql.toString());
            Query sexSq = em.createNativeQuery(sexSql.toString());
            Query njSq = em.createNativeQuery(njSql.toString());
            sq.unwrap(SQLQuery.class).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
            sexSq.unwrap(SQLQuery.class).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
            njSq.unwrap(SQLQuery.class).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
            for (Map.Entry<String, Object> e : condition.entrySet()) {
                sq.setParameter(e.getKey(), e.getValue());
                sexSq.setParameter(e.getKey(), e.getValue());
                njSq.setParameter(e.getKey(), e.getValue());
            }

            List<Object> res = sq.getResultList();
            if (null != res) {
                for (Object row : res) {
                    Map d = (Map) row;
                    String name = "";
                    if (null != d.get("simple_name")) {
                        name = d.get("simple_name").toString();
                    }
                    if (null != d.get("name") && null == d.get("simple_name")) {
                        name = d.get("name").toString();
                    }

                    if (null != d.get("name")) {
                        OrganizationStatisticsVO csnp = new OrganizationStatisticsVO();
                        csnp.setName(name);
                        if (null != d.get("joinTotal")) {
                            csnp.setJoinTotal(Integer.valueOf(d.get("joinTotal").toString()));
                        }
                        if (null != d.get("total")) {
                            csnp.setTotal(Integer.valueOf(d.get("total").toString()));
                        }
                        if (null != d.get("pass")) {
                            csnp.setPassTotal(Integer.valueOf(d.get("pass").toString()));
                        }
                        csnpList.add(csnp);
                    }
                }
            }
            Map sexRes = (Map) sexSq.getSingleResult();
            Map mapSex = new HashMap();
            if (null != sexRes) {
                if (null != sexRes.get("manTotal")) {
                    mapSex.put("manJoinTotal", Integer.valueOf(sexRes.get("manTotal").toString()));
                }
                if (null != sexRes.get("manPass")) {
                    mapSex.put("manPass", Integer.valueOf(sexRes.get("manPass").toString()));
                }
                if (null != sexRes.get("womanTotal")) {
                    mapSex.put("womanJoinTotal", Integer.valueOf(sexRes.get("womanTotal").toString()));
                }
                if (null != sexRes.get("womanPass")) {
                    mapSex.put("womanPass", Integer.valueOf(sexRes.get("womanPass").toString()));
                }
            }

            List<Object> njRes = njSq.getResultList();
            List<NjStatisticalInfoDomain> list = new ArrayList<>();
            if (null != njRes) {
                for (Object row : njRes) {
                    Map d = (Map) row;
                    NjStatisticalInfoDomain nj = new NjStatisticalInfoDomain();
                    if (null != d.get("nj")) {
                        if (null != d.get("nj")) {
                            nj.setNj(d.get("nj").toString());
                        }
                        if (null != d.get("total")) {
                            nj.setAllTotal(Long.valueOf(d.get("total").toString()));
                        }
                        if (null != d.get("joinTotal")) {
                            nj.setJoinTotal(Long.valueOf(d.get("joinTotal").toString()));
                        }
                        if (null != d.get("passTotal")) {
                            nj.setPassTotal(Long.valueOf(d.get("passTotal").toString()));
                        }
                        list.add(nj);
                    }
                }

            }

            TotalInfoDomain totalInfoDomain = new TotalInfoDomain();
            totalInfoDomain.setList(csnpList);
            totalInfoDomain.setMap(mapSex);
            totalInfoDomain.setNjList(list);
            result.put("data", totalInfoDomain);

        } catch (Exception e) {
            e.printStackTrace();
            result.put("success", false);
            result.put("message", "英语考试人数分布--数据分析失败！");
            return result;
        }
        return result;
    }

    public Map<String, Object> OverYearsPassRate(Long orgId, String collegeCode, String professionCode, String cetType, String className) {
        Map<String, Object> result = new HashMap<>();
        Map<String, Object> condition = new HashMap<>();
        try {


        } catch (Exception e) {
            e.printStackTrace();
            result.put("success", false);
            result.put("message", "英语考试单次数据分析---数据统计失败！");
            return result;
        }
        return null;
    }

    public PageData<CetDetailVO> getDetailList(Long orgId, String cetType, String collegeCode, String professionCode, String className, String nj, String isPass, Integer scoreSeg, Integer pageNumber, Integer pageSize) {
        Map<String, Object> result = new HashMap<>();
        Map<String, Object> condition = new HashMap<>();
        PageData<CetDetailVO> p = new PageData<>();
        try {


        } catch (Exception e) {
            e.printStackTrace();
            result.put("success", false);
            result.put("message", "英语考试查看数据详情---数据统计失败！");
            return p;
        }

        return p;
    }

    @Transactional(readOnly = true)
    public Map<String, Object> avg(Long orgId, String cetType, String collegeCode, String professionCode, String className) {
        Map<String, Object> result = new HashMap<>();
        Map<String, Object> condition = new HashMap<>();
        try {
            StringBuilder sql = new StringBuilder("SELECT ZXRS as total,CKRC as joinTotal,TGRC as passTotal,ZF as totalScore");
            StringBuilder sexSql = new StringBuilder("SELECT SUM(NRC) as manTotal,SUM(NZF) as manScore,SUM(VRC) as womanTotal,SUM(VZF) as womanScore,SUM(NTGRC) as manPass,SUM(VTGRC) as womanPass");
            StringBuilder njSql = new StringBuilder("select tzbj.NJ as nj,SUM(tzbj.ZXRS) as total,SUM(tzbj.CKRC ) as joinTotal,SUM(tzbj.TGRC) as passTotal,tzbj.ZF as totalScore");


            if (null == collegeCode && null == professionCode && null == className) {
                sql.append(",tt.COMPANY_NAME as name,tt.SIMPLE_NAME as simple_name from t_zb_djksjc as tzbj, t_department as tt where 1=1");
                sql.append(" and tzbj.P_BH = :orgId");
                sql.append(" and tzbj.BH = tt.COMPANY_NUMBER");

                sexSql.append(" from t_zb_djksjc as tzbj, t_department as tt where 1=1");
                sexSql.append(" and tzbj.P_BH = :orgId");
                sexSql.append(" and tzbj.BH = tt.COMPANY_NUMBER");

                njSql.append(" from t_zb_djksnj as tzbj, t_department as tt where 1=1");
                njSql.append(" and tzbj.P_BH = :orgId");
                njSql.append(" and tzbj.BH = tt.COMPANY_NUMBER");
                condition.put("orgId", orgId);
            } else if (null == professionCode && null == className) {
                sql.append(", tt.NAME as name from t_zb_djksjc as tzbj, t_profession as tt where 1=1");
                sql.append(" and tzbj.BH = :collegeCode");
                sql.append(" and tzbj.P_BH = :orgId ");
                sql.append(" and tzbj.BH = tt.COMPANY_NUMBER");

                sexSql.append(" from t_zb_djksjc as tzbj, t_profession as tt where 1=1");
                sexSql.append(" and tzbj.BH = :collegeCode");
                sexSql.append(" and tzbj.P_BH = :orgId ");
                sexSql.append(" and tzbj.BH = tt.COMPANY_NUMBER");

                njSql.append(" from t_zb_djksnj as tzbj, t_profession as tt where 1=1");
                njSql.append(" and tzbj.BH = :collegeCode");
                njSql.append(" and tzbj.P_BH = :orgId ");
                njSql.append(" and tzbj.BH = tt.COMPANY_NUMBER");

                condition.put("collegeCode", collegeCode);
                condition.put("orgId", orgId);
            } else if (null == className) {
                sql.append(", tt.NAME as name from t_zb_djksjc as tzbj, t_class as tt where 1=1");
                sql.append(" and tzbj.BH = :professionCode");
                sql.append(" and tzbj.P_BH = :collegeCode");
                sql.append(" and tzbj.BH = tt.PROFESSION_CODE");

                sexSql.append(" from t_zb_djksjc as tzbj, t_class as tt where 1=1");
                sexSql.append(" and tzbj.BH = :professionCode");
                sexSql.append(" and tzbj.P_BH = :collegeCode");
                sexSql.append(" and tzbj.BH = tt.PROFESSION_CODE");

                njSql.append(" from t_zb_djksnj as tzbj, t_class as tt where 1=1");
                njSql.append(" and tzbj.BH = :professionCode");
                njSql.append(" and tzbj.P_BH = :collegeCode");
                njSql.append(" and tzbj.BH = tt.PROFESSION_CODE");

                condition.put("professionCode", professionCode);
                condition.put("collegeCode", collegeCode);
            } else {
                sql.append(", tt.NAME as name from t_zb_djksjc as tzbj, t_class as tt where 1=1");
                sql.append(" and tzbj.BH = :className");
                sql.append(" and tzbj.P_BH = :professionCode");
                sql.append(" and tzbj.BH = tt.NAME");

                sexSql.append(" from t_zb_djksjc as tzbj, t_class as tt where 1=1");
                sexSql.append(" and tzbj.BH = :className");
                sexSql.append(" and tzbj.P_BH = :professionCode");
                sexSql.append(" and tzbj.BH = tt.NAME");

                njSql.append(" from t_zb_djksnj as tzbj, t_class as tt where 1=1");
                njSql.append(" and tzbj.BH = :className");
                njSql.append(" and tzbj.P_BH = :professionCode");
                njSql.append(" and tzbj.BH = tt.NAME");

                condition.put("className", className);
                condition.put("professionCode", professionCode);
            }

            sql.append(" and DHLJ = '1' and KSLX='" + cetType + "'");
            sql.append(" and XN = (SELECT max(XN) FROM t_zb_djksjc WHERE KSLX='" + cetType + "') and tzbj.DHLJ = '1' and tzbj.KSLX='" + cetType + "'");

            sexSql.append(" and DHLJ = '1' and KSLX='" + cetType + "'");
            sexSql.append(" and XN = (SELECT max(XN) FROM t_zb_djksjc WHERE KSLX='" + cetType + "') and tzbj.DHLJ = '1' and tzbj.KSLX='" + cetType + "'");

            njSql.append(" and DHLJ = '1' and KSLX='" + cetType + "'");
            njSql.append(" and XN = (SELECT max(XN) FROM t_zb_djksjc WHERE KSLX='" + cetType + "') and tzbj.DHLJ = '1' and tzbj.KSLX='" + cetType + "' GROUP BY nj ORDER BY nj");


            Query sq = em.createNativeQuery(sql.toString());
            Query sexSq = em.createNativeQuery(sexSql.toString());
            Query njSq = em.createNativeQuery(njSql.toString());
            sq.unwrap(SQLQuery.class).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
            sexSq.unwrap(SQLQuery.class).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
            njSq.unwrap(SQLQuery.class).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
            for (Map.Entry<String, Object> e : condition.entrySet()) {
                sq.setParameter(e.getKey(), e.getValue());
                sexSq.setParameter(e.getKey(), e.getValue());
                njSq.setParameter(e.getKey(), e.getValue());
            }
            List<Object> res = sq.getResultList();
            List<Object> avgDomains = new ArrayList<>();
            if (null != res) {
                for (Object row : res) {
                    Map d = (Map) row;
                    String name = "";
                    if (null != d.get("simple_name")) {
                        name = d.get("simple_name").toString();
                    }
                    if (null != d.get("name") && null == d.get("simple_name")) {
                        name = d.get("name").toString();
                    }

                    if (null != d.get("name")) {
                        AvgVo csnp = new AvgVo();
                        csnp.setName(name);
                        if (null != d.get("joinTotal")) {
                            csnp.setJoinTotal(Long.valueOf(d.get("joinTotal").toString()));
                        }
                        if (null != d.get("total")) {
                            csnp.setTotal(Long.valueOf(d.get("total").toString()));
                        }
                        if (null != d.get("passTotal")) {
                            csnp.setPassTotal(Long.valueOf(d.get("passTotal").toString()));
                        }
                        if (null != d.get("totalScore")) {
                            csnp.setTotalScore(new BigDecimal(d.get("totalScore").toString()));
                        }
                        if (null != d.get("totalScore") && null != d.get("joinTotal") && Integer.valueOf(d.get("joinTotal").toString()) != 0) {
                            csnp.setAvgScore(new BigDecimal(d.get("totalScore").toString()).divide(new BigDecimal(d.get("joinTotal").toString()), 0, BigDecimal.ROUND_HALF_DOWN));
                        }

                        avgDomains.add(csnp);
                    }
                }
            }

            Map sexRes = (Map) sexSq.getSingleResult();
            Map sexDomain = new HashMap();
            if (null != sexRes) {
                if (null != sexRes.get("manTotal")) {
                    sexDomain.put("manJoin", Integer.valueOf(sexRes.get("manTotal").toString()));
                }
                if (null != sexRes.get("manPass")) {
                    sexDomain.put("manPass", Integer.valueOf(sexRes.get("manPass").toString()));
                }
                if (null != sexRes.get("manScore")) {
                    sexDomain.put("manScore", new BigDecimal(sexRes.get("manScore").toString()));
                }
                if (null != sexRes.get("womanTotal")) {
                    sexDomain.put("womanJoin", Integer.valueOf(sexRes.get("womanTotal").toString()));
                }
                if (null != sexRes.get("womanPass")) {
                    sexDomain.put("womanPass", Integer.valueOf(sexRes.get("womanPass").toString()));
                }
                if (null != sexRes.get("womanScore")) {
                    sexDomain.put("womanScore", new BigDecimal(sexRes.get("womanScore").toString()));
                }

                //均值为 总分/总人次
                if (null != sexRes.get("manTotal") && Integer.valueOf(sexRes.get("manTotal").toString()) != 0) {
                    sexDomain.put("manAvg", new BigDecimal(sexRes.get("manScore").toString()).divide(new BigDecimal(sexRes.get("manTotal").toString()),0,BigDecimal.ROUND_DOWN));
                }
                if (null != sexRes.get("womanTotal") && Integer.valueOf(sexRes.get("womanTotal").toString()) != 0) {
                    sexDomain.put("womanAvg", new BigDecimal(sexRes.get("womanScore").toString()).divide(new BigDecimal(sexRes.get("womanTotal").toString()),0,BigDecimal.ROUND_HALF_DOWN));
                }
            }

            List<Object> njRes = njSq.getResultList();
            List<Object> njDomain = new ArrayList<>();
            for (Object row : njRes) {
                Map njMap = new HashMap();
                Map d = (Map) row;
                if (null != d.get("nj")) {
                    njMap.put("nj", d.get("nj"));
                }
                if (null != d.get("total")) {
                    njMap.put("total", d.get("total"));
                }
                if (null != d.get("joinTotal")) {
                    njMap.put("joinTotal", d.get("joinTotal"));
                }
                if (null != d.get("passTotal")) {
                    njMap.put("passTotal", d.get("passTotal"));
                }
                if (null != d.get("totalScore")) {
                    njMap.put("totalScore", d.get("totalScore"));
                }

                if (null != d.get("joinTotal") && Integer.valueOf(d.get("joinTotal").toString()) != 0) {
                    njMap.put("avgScore", new BigDecimal(d.get("totalScore").toString()).divide(new BigDecimal(d.get("joinTotal").toString()), 0, BigDecimal.ROUND_HALF_DOWN));
                }
                njDomain.add(njMap);
            }

            Map<String,Object> data = new HashMap<>();
            data.put("avgDomains",avgDomains);
            data.put("sexDomain",sexDomain);
            data.put("njDomain",njDomain);

            result.put("success", true);
            result.put("data", data);
            return result;

        } catch (Exception e) {
            e.printStackTrace();
            result.put("success", false);
            result.put("message", "成绩均值分布---数据统计失败！");
            return result;
        }
    }

    @Transactional(readOnly = true)
    public Map<String, Object> OverYearsAvgScore(Long orgId, String cetType, String collegeCode, String professionCode, String className) {
        Map<String, Object> result = new HashMap<>();
        Map<String, Object> condition = new HashMap<>();
        try {


        } catch (Exception e) {
            e.printStackTrace();
            result.put("success", false);
            result.put("message", "成绩均值趋势分析---数据统计失败！");
            return result;
        }
        return result;
    }
}
