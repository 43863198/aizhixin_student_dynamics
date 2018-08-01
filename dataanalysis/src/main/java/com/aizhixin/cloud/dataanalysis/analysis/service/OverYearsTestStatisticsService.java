package com.aizhixin.cloud.dataanalysis.analysis.service;

import com.aizhixin.cloud.dataanalysis.analysis.domain.*;
import com.aizhixin.cloud.dataanalysis.analysis.jdbcTemp.OverYearTestStatisticsJdbc;
import com.aizhixin.cloud.dataanalysis.analysis.vo.CetSingleDataStatisticsVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author xiagen
 * @date 2018/5/11 19:12
 * @description 历年数据分析
 */
@Service
public class OverYearsTestStatisticsService {

    @Autowired
    private CetStatisticAnalysisService cetStatisticAnalysisService;
    @Autowired
    private OverYearTestStatisticsJdbc overYearTestStatisticsJdbc;

    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy");

    public Map<String, Object> OverYearsPassRate(Long orgId,   String collegeCode, String professionCode, String classCode, String cetType,String className) {
        Map<String, Object> rs=new HashMap<>();
        List<SemesterPassRateDomain> semesterPassRateDomains=new ArrayList<>();
        List<SemesterAddRateDomain> semesterAddRateDomains=new ArrayList<>();
        Date date=new Date();
        String endTeacherYear=sdf.format(date);
        Integer c= Integer.parseInt(endTeacherYear);
        List<OverYearsInfoDomain> list=new ArrayList<>();
        for (int i=c-5;i<c;i++){
         Map<String,Object> map= cetStatisticAnalysisService.cetSingleDataStatistics(orgId,i+"","春",collegeCode,professionCode,classCode,className,cetType);
         if (map!=null){
             CetSingleDataStatisticsVO csdsVO = (CetSingleDataStatisticsVO) map.get("data");
             if (null!=csdsVO){
                 SemesterPassRateDomain semesterPassRateDomain=new SemesterPassRateDomain();
                 semesterPassRateDomain.setSemesterInfo(i+"春");
                 semesterPassRateDomain.setPassRate(csdsVO.getPassRate());
                 semesterPassRateDomains.add(semesterPassRateDomain);
                 OverYearsInfoDomain overYearsInfoDomain=new OverYearsInfoDomain();
                 overYearsInfoDomain.setSemesterInfo(i+"春");
                 overYearsInfoDomain.setPassRate(csdsVO.getPassRate());
                 list.add(overYearsInfoDomain);
             }
         }
            Map<String,Object> map2= cetStatisticAnalysisService.cetSingleDataStatistics(orgId,i+"","秋",collegeCode,professionCode,classCode,className,cetType);
            if (map!=null){
                CetSingleDataStatisticsVO csdsVO = (CetSingleDataStatisticsVO) map2.get("data");
                if (null!=csdsVO){
                    SemesterPassRateDomain semesterPassRateDomain=new SemesterPassRateDomain();
                    semesterPassRateDomain.setPassRate(csdsVO.getPassRate());
                    semesterPassRateDomain.setSemesterInfo(i+"秋");
                    semesterPassRateDomains.add(semesterPassRateDomain);
                    OverYearsInfoDomain overYearsInfoDomain=new OverYearsInfoDomain();
                    overYearsInfoDomain.setSemesterInfo(i+"秋");
                    overYearsInfoDomain.setPassRate(csdsVO.getPassRate());
                    list.add(overYearsInfoDomain);
                }
            }
        }
        int i=0;
        for (OverYearsInfoDomain overYearsInfoDomain:list) {
            SemesterAddRateDomain semesterAddRateDomain=new SemesterAddRateDomain();
            semesterAddRateDomain.setSemesterInfo(overYearsInfoDomain.getSemesterInfo());
            if (i==0){
                semesterAddRateDomain.setAddRate(null);
            }else{
                if (overYearsInfoDomain.getPassRate()!=null&&list.get(i-1).getPassRate()!=null) {
                    if (!overYearsInfoDomain.getPassRate().equals("0.00")&&!list.get(i-1).getPassRate().equals("0.00")) {
                        Double a = (Double.valueOf(overYearsInfoDomain.getPassRate()).doubleValue() - Double.valueOf(list.get(i - 1).getPassRate()).doubleValue()) / Double.valueOf(list.get(i - 1).getPassRate()).doubleValue();
                        System.out.println(a);
                        semesterAddRateDomain.setAddRate(new DecimalFormat("0.0000").format(a));
                    }else{
                        semesterAddRateDomain.setAddRate("0");
                    }
                }else {
                    semesterAddRateDomain.setAddRate(null);
                }
            }
            semesterAddRateDomains.add(semesterAddRateDomain);
            i++;
        }

        SemesterRateInfoDomain semesterRateInfoDomain=new SemesterRateInfoDomain();
        semesterRateInfoDomain.setSemesterPassRateDomains(semesterPassRateDomains);
        semesterRateInfoDomain.setSemesterAddRateDomains(semesterAddRateDomains);
        leijiPass(orgId,collegeCode,professionCode,classCode,cetType,semesterRateInfoDomain);
        rs.put("success",true);
        rs.put("data",semesterRateInfoDomain);
       return rs;
    }


    public void leijiPass(Long orgId,   String collegeCode, String professionCode, String classCode, String cetType, SemesterRateInfoDomain semesterRateInfoDomain){
        Date date=new Date();
        String endTeacherYear=sdf.format(date);
        Integer end=Integer.parseInt(endTeacherYear);
        List<SemesterLeiJiRateDomain> semesterLeiJiRateDomainList=new ArrayList<>();
        List<SemesterLeiJiAddRateDomain> semesterLeiJiAddRateDomainList=new ArrayList<>();
        for (int i=end-5;i<end;i++){
           Map<String,Object> map=overYearTestStatisticsJdbc.find(orgId,i+"","春");
           if (null!=map){
               String s =map.get("start_time").toString();
               String e =map.get("end_time").toString();
               if ("三级".equals(cetType)){
                   collegeCode = "215";
               }
              Integer a= overYearTestStatisticsJdbc.countTotal(orgId,s,collegeCode,professionCode,classCode);
              Integer b=overYearTestStatisticsJdbc.countPassTotal(orgId,cetType,collegeCode,professionCode,classCode,s,e);
               SemesterLeiJiRateDomain  semesterLeiJiRateDomain=new SemesterLeiJiRateDomain();
               semesterLeiJiRateDomain.setSemesterInfo(i+"春");
               if (a!=0&&b!=0){
                   Double d=Double.parseDouble(b+"")/Double.parseDouble(a+"");
                   semesterLeiJiRateDomain.setLeijiRate(new DecimalFormat("0.0000").format(d));
               }else if (a!=0){
                   semesterLeiJiRateDomain.setLeijiRate("0");
               }else{
                   semesterLeiJiRateDomain.setLeijiRate(null);
               }
               semesterLeiJiRateDomainList.add(semesterLeiJiRateDomain);
           }

            Map<String,Object> map2=overYearTestStatisticsJdbc.find(orgId,i+"","秋");
            if (null!=map){
                String s =map2.get("start_time").toString();
                String e =map2.get("end_time").toString();
                Integer a= overYearTestStatisticsJdbc.countTotal(orgId,s,collegeCode,professionCode,classCode);
                Integer b=overYearTestStatisticsJdbc.countPassTotal(orgId,cetType,collegeCode,professionCode,classCode,s,e);
                SemesterLeiJiRateDomain  semesterLeiJiRateDomain=new SemesterLeiJiRateDomain();
                semesterLeiJiRateDomain.setSemesterInfo(i+"秋");
                if (a!=0&&b!=0){
                    Double d=Double.parseDouble(b+"")/Double.parseDouble(a+"");
                    semesterLeiJiRateDomain.setLeijiRate(new DecimalFormat("0.0000").format(d));
                }else if (a!=0){
                    semesterLeiJiRateDomain.setLeijiRate("0");
                }else{
                    semesterLeiJiRateDomain.setLeijiRate(null);
                }
                semesterLeiJiRateDomainList.add(semesterLeiJiRateDomain);
            }
            }
        if (!semesterLeiJiRateDomainList.isEmpty()){
            int i=0;
            for (SemesterLeiJiRateDomain semesterLeiJiRateDomain:semesterLeiJiRateDomainList) {
                SemesterLeiJiAddRateDomain semesterLeiJiAddRateDomain=new SemesterLeiJiAddRateDomain();
                semesterLeiJiAddRateDomain.setSemesterInfo(semesterLeiJiRateDomain.getSemesterInfo());
                if (i==0){
                    semesterLeiJiAddRateDomain.setLeijiRate(null);
                }else{
                    if (null!=semesterLeiJiRateDomain.getLeijiRate()&&semesterLeiJiRateDomainList.get(i-1).getLeijiRate()!=null){
                      Double a=Double.parseDouble(semesterLeiJiRateDomain.getLeijiRate());
                      Double b=Double.parseDouble(semesterLeiJiRateDomainList.get(i-1).getLeijiRate());
                      if (a!=0&&b!=0){
                        Double c=(a-b)/b;
                        semesterLeiJiAddRateDomain.setLeijiRate(new DecimalFormat("0.0000").format(c));
                      }else{
                        semesterLeiJiAddRateDomain.setLeijiRate("0");
                      }
                    }else{
                        semesterLeiJiAddRateDomain.setLeijiRate(null);
                    }
                }
                i++;
                semesterLeiJiAddRateDomainList.add(semesterLeiJiAddRateDomain);
            }
        }
        semesterRateInfoDomain.setSemesterLeiJiRateDomainList(semesterLeiJiRateDomainList);
        semesterRateInfoDomain.setSemesterLeiJiAddRateDomainList(semesterLeiJiAddRateDomainList);
    }





    public Map<String, Object> OverYearsAvgScore(Long orgId,   String collegeCode, String professionCode, String classCode, String cetType,String className) {
        Map<String, Object> rs=new HashMap<>();
        Date date=new Date();
        String endTeacherYear=sdf.format(date);
        Integer c= Integer.parseInt(endTeacherYear);
        List<SemesterAvgScoreDomain> list=new ArrayList<>();
        List<SemesterAvgScoreAddRateDomain> semesterAvgScoreAddRateDomains=new ArrayList<>();
        for (int i=c-5;i<c;i++){
            Map<String,Object> map= cetStatisticAnalysisService.cetSingleDataStatistics(orgId,i+"","春",collegeCode,professionCode,classCode,className,cetType);
            if (map!=null) {
                CetSingleDataStatisticsVO csdsVO = (CetSingleDataStatisticsVO) map.get("data");
                if (null!=csdsVO){
                    SemesterAvgScoreDomain semesterAvgScoreDomain=new SemesterAvgScoreDomain();
                    semesterAvgScoreDomain.setSemesterInfo(i+"春");
                    semesterAvgScoreDomain.setScore(csdsVO.getAverage());
                    list.add(semesterAvgScoreDomain);
                }
             }

            Map<String,Object> map2= cetStatisticAnalysisService.cetSingleDataStatistics(orgId,i+"","秋",collegeCode,professionCode,classCode,className,cetType);
            if (map2!=null) {
                CetSingleDataStatisticsVO csdsVO = (CetSingleDataStatisticsVO) map2.get("data");
                if (null!=csdsVO){
                    SemesterAvgScoreDomain semesterAvgScoreDomain=new SemesterAvgScoreDomain();
                    semesterAvgScoreDomain.setSemesterInfo(i+"秋");
                    semesterAvgScoreDomain.setScore(csdsVO.getAverage());
                    list.add(semesterAvgScoreDomain);
                }
            }
        }
        if (!list.isEmpty()){
            int i=0;
            for (SemesterAvgScoreDomain semesterAvgScoreDomain:list) {
                SemesterAvgScoreAddRateDomain semesterAvgScoreAddRateDomain=new SemesterAvgScoreAddRateDomain();
                semesterAvgScoreAddRateDomain.setSemesterInfo(semesterAvgScoreDomain.getSemesterInfo());
                 if (i==0){
                     semesterAvgScoreAddRateDomain.setAvgAddRate(null);
                 }else{
                     if (!StringUtils.isEmpty(semesterAvgScoreDomain.getScore())&&!StringUtils.isEmpty(list.get(i - 1).getScore())) {
                         Double a = Double.parseDouble(semesterAvgScoreDomain.getScore());
                         Double b = Double.parseDouble(list.get(i - 1).getScore());
                         if (a != 0 && b != 0) {
                             Double cc = (a - b) / b;
                             semesterAvgScoreAddRateDomain.setAvgAddRate(new DecimalFormat("0.0000").format(cc));
                         } else {
                             semesterAvgScoreAddRateDomain.setAvgAddRate("0");
                         }
                     }else{
                         semesterAvgScoreAddRateDomain.setAvgAddRate(null);
                     }
                 }
                i++;
                semesterAvgScoreAddRateDomains.add(semesterAvgScoreAddRateDomain);
            }
        }
        SemesterAvgScoreInfoDomain semesterAvgScoreInfoDomain=new SemesterAvgScoreInfoDomain();
        semesterAvgScoreInfoDomain.setSemesterAvgScoreDomains(list);
        semesterAvgScoreInfoDomain.setSemesterAvgScoreAddRateDomains(semesterAvgScoreAddRateDomains);
        rs.put("data",semesterAvgScoreInfoDomain);
        rs.put("success",true);
        return rs;
    }
}
