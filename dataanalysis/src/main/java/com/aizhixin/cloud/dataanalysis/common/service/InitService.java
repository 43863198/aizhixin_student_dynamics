package com.aizhixin.cloud.dataanalysis.common.service;

import com.aizhixin.cloud.dataanalysis.alertinformation.entity.AttachmentInformation;
import com.aizhixin.cloud.dataanalysis.alertinformation.entity.OperationRecord;
import com.aizhixin.cloud.dataanalysis.alertinformation.entity.WarningInformation;
import com.aizhixin.cloud.dataanalysis.alertinformation.repository.AlertWarningInformationRepository;
import com.aizhixin.cloud.dataanalysis.alertinformation.repository.AttachmentInfoRepository;
import com.aizhixin.cloud.dataanalysis.alertinformation.repository.OperationRecordRepository;
import com.aizhixin.cloud.dataanalysis.analysis.entity.*;
import com.aizhixin.cloud.dataanalysis.analysis.respository.*;
import com.aizhixin.cloud.dataanalysis.common.core.DataValidity;
import com.aizhixin.cloud.dataanalysis.monitor.entity.AbnormalAttendanceStatistics;
import com.aizhixin.cloud.dataanalysis.monitor.entity.AbnormalTeachingStatistics;
import com.aizhixin.cloud.dataanalysis.monitor.entity.TeachingScheduleStatistics;
import com.aizhixin.cloud.dataanalysis.monitor.respository.AbnormalAttendanceStatisticsRespository;
import com.aizhixin.cloud.dataanalysis.monitor.respository.AbnormalTeachingStatisticsRespository;
import com.aizhixin.cloud.dataanalysis.monitor.respository.TeachingScheduleStatisticsRespository;
import com.aizhixin.cloud.dataanalysis.setup.entity.ProcessingMode;
import com.aizhixin.cloud.dataanalysis.setup.entity.WarningType;
import com.aizhixin.cloud.dataanalysis.setup.respository.ProcessingModeRespository;
import com.aizhixin.cloud.dataanalysis.setup.respository.WarningTypeRespository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
@Transactional
public class InitService {
    private Logger log= LoggerFactory.getLogger(InitService.class);
    @Autowired
    private AbnormalAttendanceStatisticsRespository abnormalAttendanceStatisticsRespository;
    @Autowired
    private AbnormalTeachingStatisticsRespository abnormalTeachingStatisticsRespository;
    @Autowired
    private AlertWarningInformationRepository alertWarningInformationRepository;
    @Autowired
    private CetScoreStatisticsRespository cetScoreStatisticsRespository;
    @Autowired
    private CourseEvaluateRespository courseEvaluateRespository;

    @Autowired
    private PracticeStaticsRespository practiceStaticsRespository;

    @Autowired
    private ProcessingModeRespository processingModeRespository;

    @Autowired
    private SchoolStatisticsRespository schoolStatisticsRespository;
    @Autowired
    private SchoolYearTermResposotory schoolYearTermResposotory;
    @Autowired
    private  TeacherEvaluateRespository teacherEvaluateRespository;
    @Autowired
    private TeachingScheduleStatisticsRespository teachingScheduleStatisticsRespository;
    @Autowired
    private  TeachingScoreDetailsRespository teachingScoreDetailsRespository;
    @Autowired
    private  TeachingScoreStatisticsRespository teachingScoreStatisticsRespository;
    @Autowired
    private WarningTypeRespository warningTypeRespository;

    private Map<Long,Long> mapData1=new HashMap<>();

    private Map<Long,Long> mapData2=new HashMap<>();

    public void initData(){
        mapData1.put(1710L,1870L);
        mapData1.put(1712L,1872L);
        mapData1.put(1714L,1874L);
        mapData1.put(1712L,1872L);
        mapData1.put(1716L,1876L);
        mapData1.put(1718L,1878L);
        mapData1.put(1720L,1880L);
        mapData1.put(1722L,1882L);
        mapData1.put(1724L,1884L);
        mapData1.put(1748L,1886L);
        mapData1.put(1750L,1888L);
        mapData1.put(1752L,1890L);
        mapData1.put(1754L,1892L);
        mapData1.put(1758L,1894L);
        mapData1.put(1760L,1896L);


        mapData2.put(1710L,1898L);
        mapData2.put(1712L,1900L);
        mapData2.put(1714L,1902L);
        mapData2.put(1716L,1904L);
        mapData2.put(1718L,1906L);
        mapData2.put(1720L,1908L);
        mapData2.put(1722L,1910L);
        mapData2.put(1724L,1912L);
        mapData2.put(1748L,1914L);
        mapData2.put(1750L,1916L);
        mapData2.put(1752L,1918L);
        mapData2.put(1754L,1920L);
        mapData2.put(1758L,1922L);
        mapData2.put(1760L,1924L);





    }


    public void initOrgId(Long kId,Long vId,Map<Long,Long> mapData){
        abnormalAttendanceStatisticsRespository.deleteByOrgId(vId);
        List<AbnormalAttendanceStatistics> abnormalAttendanceStatisticsList=abnormalAttendanceStatisticsRespository.findByOrgIdAndDeleteFlag(kId, DataValidity.VALID.getIntValue());
        if (null!=abnormalAttendanceStatisticsList&&0<abnormalAttendanceStatisticsList.size()){
            for (AbnormalAttendanceStatistics abnormalAttendanceStatistics:
                    abnormalAttendanceStatisticsList ) {
                AbnormalAttendanceStatistics abnormalAttendanceStatisticss=new AbnormalAttendanceStatistics();
                BeanUtils.copyProperties(abnormalAttendanceStatistics,abnormalAttendanceStatisticss);
                abnormalAttendanceStatisticss.setId(UUID.randomUUID().toString());
                abnormalAttendanceStatisticss.setOrgId(vId);
                abnormalAttendanceStatisticsRespository.save(abnormalAttendanceStatisticss);
            }
        }
        abnormalTeachingStatisticsRespository.deleteByOrgId(vId);
       List<AbnormalTeachingStatistics> abnormalTeachingStatisticsList= abnormalTeachingStatisticsRespository.findByOrgIdAndDeleteFlag(kId,DataValidity.VALID.getIntValue());
       if (null!=abnormalTeachingStatisticsList&&0<abnormalTeachingStatisticsList.size()){
           for (AbnormalTeachingStatistics abnormalTeachingStatistics:abnormalTeachingStatisticsList){
               AbnormalTeachingStatistics abnormalTeachingStatistics1=new AbnormalTeachingStatistics();
               BeanUtils.copyProperties(abnormalTeachingStatistics,abnormalTeachingStatistics1);
               abnormalTeachingStatistics1.setId(UUID.randomUUID().toString());
               abnormalTeachingStatistics1.setOrgId(vId);
               abnormalTeachingStatisticsRespository.save(abnormalTeachingStatistics1);
           }
       }

        cetScoreStatisticsRespository.deleteByOrgId(vId);
        List<CetScoreStatistics> cetScoreStatisticsList=cetScoreStatisticsRespository.findByOrgIdAndDeleteFlag(kId,DataValidity.VALID.getIntValue());
        if (null!=cetScoreStatisticsList&&0<cetScoreStatisticsList.size())
        for (CetScoreStatistics cetScoreStatistics:cetScoreStatisticsList){
            CetScoreStatistics cetScoreStatistics1=new CetScoreStatistics();
            BeanUtils.copyProperties(cetScoreStatistics,cetScoreStatistics1);
            cetScoreStatistics1.setOrgId(vId);
            Long collegeId=mapData.get(cetScoreStatistics1.getCollegeId());
            cetScoreStatistics1.setCollegeId(collegeId);
            cetScoreStatistics1.setId(UUID.randomUUID().toString());
            cetScoreStatisticsRespository.save(cetScoreStatistics1);
        }

        courseEvaluateRespository.deleteByOrgId(vId);
        List<CourseEvaluate> courseEvaluateList=courseEvaluateRespository.findByOrgIdAndDeleteFlag(kId,DataValidity.VALID.getIntValue());
        if (null!=courseEvaluateList&&0<courseEvaluateList.size())
        for (CourseEvaluate courseEvaluate:courseEvaluateList){
            CourseEvaluate courseEvaluate1=new CourseEvaluate();
            BeanUtils.copyProperties(courseEvaluate,courseEvaluate1);
            courseEvaluate1.setOrgId(vId);
            Long collegeId=mapData.get(courseEvaluate1.getCollegeId());
            courseEvaluate1.setCollegeId(collegeId);
            courseEvaluate1.setId(UUID.randomUUID().toString());
            courseEvaluateRespository.save(courseEvaluate1);
        }

        practiceStaticsRespository.deleteByOrgId(vId);
        List<PracticeStatistics> practiceStatisticsList=practiceStaticsRespository.findByOrgIdAndDeleteFlag(kId,DataValidity.VALID.getIntValue());
        if (null!=practiceStatisticsList&&0<practiceStatisticsList.size())
            for (PracticeStatistics practiceStatistics:practiceStatisticsList){
            PracticeStatistics practiceStatistics1=new PracticeStatistics();
            BeanUtils.copyProperties(practiceStatistics,practiceStatistics1);
                practiceStatistics1.setOrgId(vId);
               Long collegeId=mapData.get(practiceStatistics1.getCollegeId());
                practiceStatistics1.setCollegeId(collegeId);
                practiceStatistics1.setId(UUID.randomUUID().toString());
               practiceStaticsRespository.save(practiceStatistics1);
            }

        processingModeRespository.deleteByOrgId(vId);
        List<ProcessingMode> processingModeList=processingModeRespository.findByOrgIdAndDeleteFlag(kId,DataValidity.VALID.getIntValue());
        if (null!=processingModeList&&0<processingModeList.size()){
               for (ProcessingMode processingMode:processingModeList){
                   ProcessingMode processingMode1=new ProcessingMode();
                   BeanUtils.copyProperties(processingMode,processingMode1);
                   processingMode1.setOrgId(vId);
                   processingMode1.setId(UUID.randomUUID().toString());
                   processingModeRespository.save(processingMode1);
               }
        }

        schoolStatisticsRespository.deleteByOrgId(vId);
        List<SchoolStatistics> schoolStatisticsList=schoolStatisticsRespository.findByOrgIdAndDeleteFlag(kId,DataValidity.VALID.getIntValue());
        if (null!=schoolStatisticsList&&0<schoolStatisticsList.size()){
            for (SchoolStatistics schoolStatistics:schoolStatisticsList){
                SchoolStatistics schoolStatistics1=new SchoolStatistics();
                BeanUtils.copyProperties(schoolStatistics,schoolStatistics1);
                schoolStatistics1.setId(UUID.randomUUID().toString());
                schoolStatistics1.setOrgId(vId);
                Long collegeId=mapData.get(schoolStatistics1.getCollegeId());
                schoolStatistics1.setCollegeId(collegeId);
                schoolStatisticsRespository.save(schoolStatistics1);
            }
        }

        schoolYearTermResposotory.deleteByOrgId(vId);
       List<SchoolYearTerm> schoolYearTermList=schoolYearTermResposotory.findByOrgIdAndDeleteFlag(kId,DataValidity.VALID.getIntValue());
       if (null!=schoolYearTermList&&0<schoolYearTermList.size()){
           for (SchoolYearTerm schoolYearTerm:schoolYearTermList){
               SchoolYearTerm schoolYearTerm1=new SchoolYearTerm();
               BeanUtils.copyProperties(schoolYearTerm,schoolYearTerm1);
               schoolYearTerm1.setOrgId(vId);
               schoolYearTerm1.setId(UUID.randomUUID().toString());
               schoolYearTermResposotory.save(schoolYearTerm1);
           }
       }

        teacherEvaluateRespository.deleteByOrgId(vId);
        List<TeacherEvaluate> teacherEvaluateList=teacherEvaluateRespository.findByOrgIdAndDeleteFlag(kId,DataValidity.VALID.getIntValue());
        if (null!=teacherEvaluateList&&0<teacherEvaluateList.size()){
            for (TeacherEvaluate teacherEvaluate:teacherEvaluateList){
                TeacherEvaluate teacherEvaluate1=new TeacherEvaluate();
                BeanUtils.copyProperties(teacherEvaluate,teacherEvaluate1);
                teacherEvaluate1.setOrgId(vId);
                Long collegeId=mapData.get(teacherEvaluate1.getCollegeId());
                teacherEvaluate1.setCollegeId(collegeId);
                teacherEvaluate1.setId(UUID.randomUUID().toString());
                teacherEvaluateRespository.save(teacherEvaluate1);
            }
        }

        teachingScheduleStatisticsRespository.deleteByOrgId(vId);
        List<TeachingScheduleStatistics> teachingScheduleStatisticsList=teachingScheduleStatisticsRespository.findByOrgIdAndDeleteFlag(kId,DataValidity.VALID.getIntValue());
        if (null!=teachingScheduleStatisticsList&&0<teachingScheduleStatisticsList.size()){
            for (TeachingScheduleStatistics teachingScheduleStatistics:teachingScheduleStatisticsList){
                TeachingScheduleStatistics teachingScheduleStatistics1=new TeachingScheduleStatistics();
                BeanUtils.copyProperties(teachingScheduleStatistics,teachingScheduleStatistics1);
                teachingScheduleStatistics1.setOrgId(vId);
                teachingScheduleStatistics1.setId(UUID.randomUUID().toString());
                teachingScheduleStatisticsRespository.save(teachingScheduleStatistics1);
            }
        }
        teachingScoreDetailsRespository.deleteByOrgId(vId);
        List<TeachingScoreDetails>  teachingScoreDetailsList=teachingScoreDetailsRespository.findByOrgIdAndDeleteFlag(kId,DataValidity.VALID.getIntValue());
        if (null!=teachingScoreDetailsList&&0<teachingScoreDetailsList.size()){
             for (TeachingScoreDetails teachingScoreDetails:teachingScoreDetailsList){
                 TeachingScoreDetails teachingScoreDetails1=new TeachingScoreDetails();
                 BeanUtils.copyProperties(teachingScoreDetails,teachingScoreDetails1);
                 teachingScoreDetails1.setOrgId(vId);
                 Long collegeId=mapData.get(teachingScoreDetails1.getCollegeId());
                 teachingScoreDetails1.setCollegeId(collegeId);
                 teachingScoreDetails1.setId(UUID.randomUUID().toString());
                 teachingScoreDetailsRespository.save(teachingScoreDetails1);
             }
        }

        teachingScoreStatisticsRespository.deleteByOrgId(vId);
        List<TeachingScoreStatistics> teachingScoreStatisticsList=teachingScoreStatisticsRespository.findByOrgIdAndDeleteFlag(kId,DataValidity.VALID.getIntValue());
        if (null!=teachingScoreStatisticsList&&0<teachingScoreStatisticsList.size()){
            for (TeachingScoreStatistics teachingScoreStatistics:teachingScoreStatisticsList){
                TeachingScoreStatistics teachingScoreStatistics1=new TeachingScoreStatistics();
                BeanUtils.copyProperties(teachingScoreStatistics,teachingScoreStatistics1);
                teachingScoreStatistics1.setOrgId(vId);
                teachingScoreStatistics1.setId(UUID.randomUUID().toString());
                Long collegeId=mapData.get(teachingScoreStatistics1.getCollegeId());
                teachingScoreStatistics1.setCollegeId(collegeId);
                teachingScoreStatisticsRespository.save(teachingScoreStatistics1);
            }
        }

        alertWarningInformationRepository.deleteByOrgId(vId);
       List<WarningInformation> warningInformationList= alertWarningInformationRepository.findByOrgIdAndDeleteFlag(kId,DataValidity.VALID.getIntValue());
       if (null!=warningInformationList&&0<warningInformationList.size()){
           for (WarningInformation warningInformation:warningInformationList){
               WarningInformation warningInformation1=new WarningInformation();
               BeanUtils.copyProperties(warningInformation,warningInformation1);
               warningInformation1.setOrgId(vId);
               Long collegeId=mapData.get(warningInformation1.getCollogeId());
               warningInformation1.setCollogeId(collegeId);
               warningInformation1.setId(UUID.randomUUID().toString());
               alertWarningInformationRepository.save(warningInformation1);
           }
       }

        warningTypeRespository.deleteByOrgId(vId);
        List<WarningType> warningTypeList=warningTypeRespository.findByOrgIdAndDeleteFlag(kId,DataValidity.VALID.getIntValue());
        if (null!=warningTypeList&&0<warningTypeList.size()){
            for (WarningType warningType:warningTypeList){
                WarningType warningType1=new WarningType();
                BeanUtils.copyProperties(warningType,warningType1);
                warningType1.setOrgId(vId);
                warningType1.setId(UUID.randomUUID().toString());
                warningTypeRespository.save(warningType1);
            }
        }
    }


    public  void start(){
        log.info("数据初始开始------");
        initData();
        initOrgId(138L,81L,mapData1);
        initOrgId(138L,85L,mapData2);
        log.info("数据初始结束------");
    }
}
