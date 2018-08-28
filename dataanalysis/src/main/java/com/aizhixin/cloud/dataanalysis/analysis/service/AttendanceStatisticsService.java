package com.aizhixin.cloud.dataanalysis.analysis.service;

import com.aizhixin.cloud.dataanalysis.analysis.constant.AttendanceResult;
import com.aizhixin.cloud.dataanalysis.analysis.domain.AttendanceStatisticsCourseDomain;
import com.aizhixin.cloud.dataanalysis.analysis.domain.AttendanceStatisticsDomain;
import com.aizhixin.cloud.dataanalysis.analysis.domain.AttendanceStatisticsTeacherDomain;
import com.aizhixin.cloud.dataanalysis.analysis.domain.AttendanceStatisticsUnitDomain;
import com.aizhixin.cloud.dataanalysis.analysis.dto.OrganizationDTO;
import com.aizhixin.cloud.dataanalysis.analysis.entity.AttendanceStatistics;
import com.aizhixin.cloud.dataanalysis.analysis.respository.AttendanceStatisticsRepository;
import com.aizhixin.cloud.dataanalysis.analysis.vo.AttendanceStatisticsVO;
import com.aizhixin.cloud.dataanalysis.common.PageData;
import com.aizhixin.cloud.dataanalysis.common.core.PageUtil;
import com.aizhixin.cloud.dataanalysis.setup.service.GenerateWarningInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class AttendanceStatisticsService {
    @Autowired
    private AttendanceStatisticsRepository attendanceStatisticsRepository;
    @Autowired
    private GenerateWarningInfoService generateWarningInfoService;
    @Autowired
    private OrganizationService organizationService;
    private static final String SERVER = "7";
    private static final String THIRTY = "30";
    private static final String ONE = "1";

    /**
     * 按单位统计
     */
    public AttendanceStatisticsVO statisticsByUnit(Long xxdm, String yxsh, String zyh, String dateRange) {
        try {
            Map dateMap = getDate(xxdm, dateRange);
            String teachYear = dateMap.get("teachYear").toString();
            String semester = dateMap.get("semester").toString();
            String start = dateMap.get("start").toString();
            String end = dateMap.get("end").toString();
            Long total;
            Long actual;
            Long leave;
            Long absentee;
            Double avg;

            AttendanceStatisticsVO attendanceStatisticsVO = new AttendanceStatisticsVO();
            AttendanceStatisticsDomain attendanceStatisticsDomain = new AttendanceStatisticsDomain();
            List<AttendanceStatisticsUnitDomain> list = new ArrayList<>();
            if (StringUtils.isEmpty(yxsh) && StringUtils.isEmpty(zyh)) {
                total = attendanceStatisticsRepository.countByXxdmAndXnAndXqmAndKqrqBetweenOrderByYxsh(xxdm, teachYear, semester, start, end);
                actual = attendanceStatisticsRepository.countByXxdmAndXnAndXqmAndKqjgAndKqrqBetweenOrderByYxsh(xxdm, teachYear, semester, AttendanceResult.ARRIVED, start, end);
                leave = attendanceStatisticsRepository.countByXxdmAndXnAndXqmAndKqjgAndKqrqBetweenOrderByYxsh(xxdm, teachYear, semester, AttendanceResult.LEAVE, start, end);
                absentee = attendanceStatisticsRepository.countByXxdmAndXnAndXqmAndKqjgAndKqrqBetweenOrderByYxsh(xxdm, teachYear, semester, AttendanceResult.ABSENTEE, start, end);
                avg = (double) ((total.intValue() - actual.intValue() - leave.intValue() - absentee.intValue()) / total.intValue());
                //学校各院系列表
                Map yxMap = organizationService.getCollege(xxdm);
                List<OrganizationDTO> yxList = (List) yxMap.get("data");
                for (OrganizationDTO temp : yxList) {
                    AttendanceStatisticsUnitDomain attendanceStatisticsUnitDomain = new AttendanceStatisticsUnitDomain();
                    String code = temp.getCode();
                    attendanceStatisticsUnitDomain.setUnitCode(code);
                    attendanceStatisticsUnitDomain.setUnitName(StringUtils.isEmpty(temp.getSimple()) ? temp.getName() : temp.getSimple());
                    Map collegeMap = getAttendanceByCollege(xxdm, code, teachYear, semester, start, end);
                    Long unittotal = Long.parseLong(collegeMap.get("total").toString());
                    Long unitactual = Long.parseLong(collegeMap.get("actual").toString());
                    Long unitleave = Long.parseLong(collegeMap.get("leave").toString());
                    Long unitabsentee = Long.parseLong(collegeMap.get("absentee").toString());
                    Double unitavg = Double.parseDouble(collegeMap.get("avg").toString());
                    attendanceStatisticsUnitDomain.setTotal(unittotal.intValue());
                    attendanceStatisticsUnitDomain.setActual(unitactual.intValue());
                    attendanceStatisticsUnitDomain.setLeave(unitleave.intValue());
                    attendanceStatisticsUnitDomain.setAbsentee(unitabsentee.intValue());
                    attendanceStatisticsUnitDomain.setAvg(unitavg);
                    list.add(attendanceStatisticsUnitDomain);
                }
            } else if (StringUtils.isEmpty(zyh)) {
                Map collegeMap = getAttendanceByCollege(xxdm, yxsh, teachYear, semester, start, end);
                total = Long.parseLong(collegeMap.get("total").toString());
                actual = Long.parseLong(collegeMap.get("actual").toString());
                leave = Long.parseLong(collegeMap.get("leave").toString());
                absentee = Long.parseLong(collegeMap.get("absentee").toString());
                avg = Double.parseDouble(collegeMap.get("avg").toString());
                //本部门各专业列表
                Map zyMap = organizationService.getProfession(xxdm, yxsh);
                List<OrganizationDTO> zyList = (List) zyMap.get("data");
                for (OrganizationDTO temp : zyList) {
                    AttendanceStatisticsUnitDomain attendanceStatisticsUnitDomain = new AttendanceStatisticsUnitDomain();
                    String code = temp.getCode();
                    attendanceStatisticsUnitDomain.setUnitCode(code);
                    attendanceStatisticsUnitDomain.setUnitName(temp.getName());
                    Map professionMap = getAttendaneByProfession(xxdm, yxsh, code, teachYear, semester, start, end);
                    Long unittotal = Long.parseLong(professionMap.get("total").toString());
                    Long unitactual = Long.parseLong(professionMap.get("actual").toString());
                    Long unitleave = Long.parseLong(professionMap.get("leave").toString());
                    Long unitabsentee = Long.parseLong(professionMap.get("absentee").toString());
                    Double unitavg = Double.parseDouble(professionMap.get("avg").toString());
                    attendanceStatisticsUnitDomain.setTotal(unittotal.intValue());
                    attendanceStatisticsUnitDomain.setActual(unitactual.intValue());
                    attendanceStatisticsUnitDomain.setLeave(unitleave.intValue());
                    attendanceStatisticsUnitDomain.setAbsentee(unitabsentee.intValue());
                    attendanceStatisticsUnitDomain.setAvg(unitavg);
                    list.add(attendanceStatisticsUnitDomain);
                }
            } else {
                Map collegeMap = getAttendaneByProfession(xxdm, yxsh, zyh, teachYear, semester, start, end);
                total = Long.parseLong(collegeMap.get("total").toString());
                actual = Long.parseLong(collegeMap.get("actual").toString());
                leave = Long.parseLong(collegeMap.get("leave").toString());
                absentee = Long.parseLong(collegeMap.get("absentee").toString());
                avg = Double.parseDouble(collegeMap.get("avg").toString());
                //本专业各班级列表
                Map zyMap = organizationService.getClass(xxdm, yxsh, zyh);
                List<OrganizationDTO> bjList = (List) zyMap.get("data");
                for (OrganizationDTO temp : bjList) {
                    AttendanceStatisticsUnitDomain attendanceStatisticsUnitDomain = new AttendanceStatisticsUnitDomain();
                    String code = temp.getCode();
                    attendanceStatisticsUnitDomain.setUnitCode(code);
                    attendanceStatisticsUnitDomain.setUnitName(temp.getName());
                    Map classMap = getAttendaneByClass(xxdm, yxsh, zyh, code, teachYear, semester, start, end);
                    Long unittotal = Long.parseLong(classMap.get("total").toString());
                    Long unitactual = Long.parseLong(classMap.get("actual").toString());
                    Long unitleave = Long.parseLong(classMap.get("leave").toString());
                    Long unitabsentee = Long.parseLong(classMap.get("absentee").toString());
                    Double unitavg = Double.parseDouble(classMap.get("avg").toString());
                    attendanceStatisticsUnitDomain.setTotal(unittotal.intValue());
                    attendanceStatisticsUnitDomain.setActual(unitactual.intValue());
                    attendanceStatisticsUnitDomain.setLeave(unitleave.intValue());
                    attendanceStatisticsUnitDomain.setAbsentee(unitabsentee.intValue());
                    attendanceStatisticsUnitDomain.setAvg(unitavg);
                    list.add(attendanceStatisticsUnitDomain);
                }
            }
            attendanceStatisticsDomain.setTotal(total.intValue());
            attendanceStatisticsDomain.setActual(actual.intValue());
            attendanceStatisticsDomain.setLeave(leave.intValue());
            attendanceStatisticsDomain.setAbsentee(absentee.intValue());
            attendanceStatisticsDomain.setAvg(avg);

            attendanceStatisticsVO.setAttendanceStatisticsDomain(attendanceStatisticsDomain);
            attendanceStatisticsVO.setAttendanceStatisticsUnitDomainList(list);
            return attendanceStatisticsVO;
        } catch (Exception e) {
            e.printStackTrace();
            return new AttendanceStatisticsVO();
        }
    }

    /**
     * 按课程统计
     */
    public PageData<AttendanceStatisticsCourseDomain> statisticsByCourse(Long xxdm, String kchOrkcmc, String dateRange, Integer pageNumber, Integer pageSize) {
        try {
            Map dateMap = getDate(xxdm, dateRange);
            String teachYear = dateMap.get("teachYear").toString();
            String semester = dateMap.get("semester").toString();
            String start = dateMap.get("start").toString();
            String end = dateMap.get("end").toString();

            PageData<AttendanceStatisticsCourseDomain> p = new PageData<>();
            List<AttendanceStatistics> list;
            List<AttendanceStatisticsCourseDomain> resultLsit = new ArrayList<>();
            if (null == pageNumber || pageNumber < 1) {
                pageNumber = 1;
            }
            if (null == pageSize) {
                pageSize = 20;
            }
            Pageable pageable = new PageRequest(pageNumber - 1, pageSize);
            if (StringUtils.isEmpty(kchOrkcmc)) {
                list = attendanceStatisticsRepository.findByXxdmAndXnAndXqmAndKqrqBetween(xxdm, teachYear, semester, start, end, pageable);
                for (AttendanceStatistics temp : list) {
                    AttendanceStatisticsCourseDomain attendanceStatisticsCourseDomain = new AttendanceStatisticsCourseDomain();
                    String kch = temp.getKch();
                    String kcmc = temp.getKcmc();
                    List<AttendanceStatistics> listBykch = attendanceStatisticsRepository.findByXxdmAndXnAndXqmAndKcmcAndKqrqBetween(xxdm, teachYear, semester, kch, start, end);
                    int total;
                    int arrived = 0;
                    int late = 0;
                    int leave = 0;
                    int absentee = 0;
                    int leave_early = 0;
                    double avg;

                    total = listBykch.size();
                    for (AttendanceStatistics attendanceStatistics : listBykch) {
                        if (temp.getKqjg().equals(AttendanceResult.ARRIVED)) {
                            arrived++;
                        } else if (attendanceStatistics.getKqjg().equals(AttendanceResult.LATE)) {
                            late++;
                        } else if (attendanceStatistics.getKqjg().equals(AttendanceResult.LEAVE)) {
                            leave++;
                        } else if (attendanceStatistics.getKqjg().equals(AttendanceResult.ABSENTEE)) {
                            absentee++;
                        } else if (attendanceStatistics.getKqjg().equals(AttendanceResult.LEAVE_EARLY)) {
                            leave_early++;
                        }
                    }
                    avg = (double) ((total - arrived - leave - absentee) / total);
                    attendanceStatisticsCourseDomain.setKch(kch);
                    attendanceStatisticsCourseDomain.setKcmc(kcmc);
                    attendanceStatisticsCourseDomain.setSkls(temp.getJsxm());
                    attendanceStatisticsCourseDomain.setTotal(total);
                    attendanceStatisticsCourseDomain.setArrived(arrived);
                    attendanceStatisticsCourseDomain.setLate(late);
                    attendanceStatisticsCourseDomain.setLeave(leave);
                    attendanceStatisticsCourseDomain.setAbsentee(absentee);
                    attendanceStatisticsCourseDomain.setLeave_early(leave_early);
                    attendanceStatisticsCourseDomain.setAvg(avg);

                    resultLsit.add(attendanceStatisticsCourseDomain);

                }
            } else {
                if (kchOrkcmc.matches("[0-9]{1,}")) {
                    list = attendanceStatisticsRepository.findByXxdmAndXnAndXqmAndKchAndKqrqBetween(xxdm, teachYear, semester, kchOrkcmc, start, end);

                } else {
                    list = attendanceStatisticsRepository.findByXxdmAndXnAndXqmAndKcmcAndKqrqBetween(xxdm, teachYear, semester, kchOrkcmc, start, end);
                }
                int total;
                int arrived = 0;
                int late = 0;
                int leave = 0;
                int absentee = 0;
                int leave_early = 0;
                double avg;

                total = list.size();
                for (AttendanceStatistics temp : list) {
                    if (temp.getKqjg().equals(AttendanceResult.ARRIVED)) {
                        arrived++;
                    } else if (temp.getKqjg().equals(AttendanceResult.LATE)) {
                        late++;
                    } else if (temp.getKqjg().equals(AttendanceResult.LEAVE)) {
                        leave++;
                    } else if (temp.getKqjg().equals(AttendanceResult.ABSENTEE)) {
                        absentee++;
                    } else if (temp.getKqjg().equals(AttendanceResult.LEAVE_EARLY)) {
                        leave_early++;
                    }
                }
                avg = (double) ((total - arrived - leave - absentee) / total);
                AttendanceStatistics temp = list.get(0);
                AttendanceStatisticsCourseDomain attendanceStatisticsCourseDomain = new AttendanceStatisticsCourseDomain();
                attendanceStatisticsCourseDomain.setKch(temp.getKch());
                attendanceStatisticsCourseDomain.setKcmc(temp.getKcmc());
                attendanceStatisticsCourseDomain.setSkls(temp.getJsxm());
                attendanceStatisticsCourseDomain.setTotal(total);
                attendanceStatisticsCourseDomain.setArrived(arrived);
                attendanceStatisticsCourseDomain.setLate(late);
                attendanceStatisticsCourseDomain.setLeave(leave);
                attendanceStatisticsCourseDomain.setAbsentee(absentee);
                attendanceStatisticsCourseDomain.setLeave_early(leave_early);
                attendanceStatisticsCourseDomain.setAvg(avg);

                resultLsit.add(attendanceStatisticsCourseDomain);

            }


            p.setData(resultLsit);
            p.getPage().setPageNumber(pageNumber);
            p.getPage().setPageSize(pageSize);
            p.getPage().setTotalElements((long) resultLsit.size());
            p.getPage().setTotalPages(PageUtil.cacalatePagesize((long) resultLsit.size(), p.getPage().getPageSize()));
            return p;
        } catch (Exception e) {
            e.printStackTrace();
            return new PageData<>();
        }
    }

    /**
     * 按教师统计
     */
    public PageData<AttendanceStatisticsTeacherDomain> statisticsByTeacher(Long xxdm, String jsghOrjsmc, String dateRange, Integer pageNumber, Integer pageSize) {
        try {
            Map dateMap = getDate(xxdm, dateRange);
            String teachYear = dateMap.get("teachYear").toString();
            String semester = dateMap.get("semester").toString();
            String start = dateMap.get("start").toString();
            String end = dateMap.get("end").toString();

            PageData<AttendanceStatisticsTeacherDomain> p = new PageData<>();
            List<AttendanceStatistics> list;
            List<AttendanceStatisticsTeacherDomain> resultLsit = new ArrayList<>();
            if (null == pageNumber || pageNumber < 1) {
                pageNumber = 1;
            }
            if (null == pageSize) {
                pageSize = 20;
            }

            Pageable pageable = new PageRequest(pageNumber - 1, pageSize);
            if (StringUtils.isEmpty(jsghOrjsmc)) {
                list = attendanceStatisticsRepository.findByXxdmAndXnAndXqmAndKqrqBetween(xxdm, teachYear, semester, start, end, pageable);
                for (AttendanceStatistics temp : list) {
                    AttendanceStatisticsTeacherDomain attendanceStatisticsTeacherDomain = new AttendanceStatisticsTeacherDomain();
                    String jsgh = temp.getJsgh();
                    String jsxm = temp.getJsxm();
                    List<AttendanceStatistics> listByjsgh = attendanceStatisticsRepository.findByXxdmAndXnAndXqmAndKcmcAndKqrqBetween(xxdm, teachYear, semester, jsgh, start, end);
                    int total;
                    int arrived = 0;
                    int late = 0;
                    int leave = 0;
                    int absentee = 0;
                    int leave_early = 0;
                    double avg;

                    total = listByjsgh.size();
                    for (AttendanceStatistics attendanceStatistics : listByjsgh) {
                        if (temp.getKqjg().equals(AttendanceResult.ARRIVED)) {
                            arrived++;
                        } else if (attendanceStatistics.getKqjg().equals(AttendanceResult.LATE)) {
                            late++;
                        } else if (attendanceStatistics.getKqjg().equals(AttendanceResult.LEAVE)) {
                            leave++;
                        } else if (attendanceStatistics.getKqjg().equals(AttendanceResult.ABSENTEE)) {
                            absentee++;
                        } else if (attendanceStatistics.getKqjg().equals(AttendanceResult.LEAVE_EARLY)) {
                            leave_early++;
                        }
                    }
                    avg = (double) ((total - arrived - leave - absentee) / total);
                    attendanceStatisticsTeacherDomain.setJsgh(jsgh);
                    attendanceStatisticsTeacherDomain.setSkls(jsxm);
                    attendanceStatisticsTeacherDomain.setYxsh(temp.getDwh());
                    attendanceStatisticsTeacherDomain.setYxmc(temp.getDwmc());
                    attendanceStatisticsTeacherDomain.setTotal(total);
                    attendanceStatisticsTeacherDomain.setArrived(arrived);
                    attendanceStatisticsTeacherDomain.setLate(late);
                    attendanceStatisticsTeacherDomain.setLeave(leave);
                    attendanceStatisticsTeacherDomain.setAbsentee(absentee);
                    attendanceStatisticsTeacherDomain.setLeave_early(leave_early);
                    attendanceStatisticsTeacherDomain.setAvg(avg);

                    resultLsit.add(attendanceStatisticsTeacherDomain);

                }
            } else {
                if (jsghOrjsmc.matches("[0-9]{1,}")) {
                    list = attendanceStatisticsRepository.findByXxdmAndXnAndXqmAndJsghAndKqrqBetween(xxdm, teachYear, semester, jsghOrjsmc, start, end);

                } else {
                    list = attendanceStatisticsRepository.findByXxdmAndXnAndXqmAndJsxmAndKqrqBetween(xxdm, teachYear, semester, jsghOrjsmc, start, end);
                }
                int total;
                int arrived = 0;
                int late = 0;
                int leave = 0;
                int absentee = 0;
                int leave_early = 0;
                double avg;

                total = list.size();
                for (AttendanceStatistics temp : list) {
                    if (temp.getKqjg().equals(AttendanceResult.ARRIVED)) {
                        arrived++;
                    } else if (temp.getKqjg().equals(AttendanceResult.LATE)) {
                        late++;
                    } else if (temp.getKqjg().equals(AttendanceResult.LEAVE)) {
                        leave++;
                    } else if (temp.getKqjg().equals(AttendanceResult.ABSENTEE)) {
                        absentee++;
                    } else if (temp.getKqjg().equals(AttendanceResult.LEAVE_EARLY)) {
                        leave_early++;
                    }
                }
                avg = (double) ((total - arrived - leave - absentee) / total);
                AttendanceStatistics temp = list.get(0);
                AttendanceStatisticsTeacherDomain attendanceStatisticsTeacherDomain = new AttendanceStatisticsTeacherDomain();
                attendanceStatisticsTeacherDomain.setJsgh(temp.getJsgh());
                attendanceStatisticsTeacherDomain.setSkls(temp.getJsxm());
                attendanceStatisticsTeacherDomain.setYxsh(temp.getDwh());
                attendanceStatisticsTeacherDomain.setYxmc(temp.getDwmc());
                attendanceStatisticsTeacherDomain.setTotal(total);
                attendanceStatisticsTeacherDomain.setArrived(arrived);
                attendanceStatisticsTeacherDomain.setLate(late);
                attendanceStatisticsTeacherDomain.setLeave(leave);
                attendanceStatisticsTeacherDomain.setAbsentee(absentee);
                attendanceStatisticsTeacherDomain.setLeave_early(leave_early);
                attendanceStatisticsTeacherDomain.setAvg(avg);

                resultLsit.add(attendanceStatisticsTeacherDomain);

            }
            p.setData(resultLsit);
            p.getPage().setPageNumber(pageNumber);
            p.getPage().setPageSize(pageSize);
            p.getPage().setTotalElements((long) resultLsit.size());
            p.getPage().setTotalPages(PageUtil.cacalatePagesize((long) resultLsit.size(), p.getPage().getPageSize()));
            return p;
        } catch (Exception e) {
            e.printStackTrace();
            return new PageData<>();
        }
    }

    /**
     * 获取过去第几天的日期
     */
    public String getPastDate(int past) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_YEAR, calendar.get(Calendar.DAY_OF_YEAR) - past);
        Date today = calendar.getTime();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        return format.format(today);
    }

    /**
     * 获取各院系考勤
     */
    public Map getAttendanceByCollege(Long xxdm, String yxsh, String teachYear, String semester, String start, String end) {
        //应出勤人次
        Long total = attendanceStatisticsRepository.countByXxdmAndYxshAndXnAndXqmAndKqrqBetweenOrderByZyh(xxdm, yxsh, teachYear, semester, start, end);
        //实到人次
        Long actual = attendanceStatisticsRepository.countByXxdmAndYxshAndXnAndXqmAndKqjgAndKqrqBetweenOrderByZyh(xxdm, yxsh, teachYear, semester, AttendanceResult.ARRIVED, start, end);
        //请假人次
        Long leave = attendanceStatisticsRepository.countByXxdmAndYxshAndXnAndXqmAndKqjgAndKqrqBetweenOrderByZyh(xxdm, yxsh, teachYear, semester, AttendanceResult.LEAVE, start, end);
        //旷课人次
        Long absentee = attendanceStatisticsRepository.countByXxdmAndYxshAndXnAndXqmAndKqjgAndKqrqBetweenOrderByZyh(xxdm, yxsh, teachYear, semester, AttendanceResult.ABSENTEE, start, end);
        //平均到课率
        Double avg = (double) ((total.intValue() - actual.intValue() - leave.intValue() - absentee.intValue()) / total.intValue());

        Map<String, Object> map = new HashMap<>();
        map.put("total", total);
        map.put("actual", actual);
        map.put("leave", leave);
        map.put("absentee", absentee);
        map.put("avg", avg);
        return map;
    }

    /**
     * 获取各专业考勤
     */
    public Map getAttendaneByProfession(Long xxdm, String yxsh, String zyh, String teachYear, String semester, String start, String end) {
        //应出勤人次
        Long total = attendanceStatisticsRepository.countByXxdmAndYxshAndZyhAndXnAndXqmAndKqrqBetweenOrderByBjmc(xxdm, yxsh, zyh, teachYear, semester, start, end);
        //实到人次
        Long actual = attendanceStatisticsRepository.countByXxdmAndYxshAndZyhAndXnAndXqmAndKqjgAndKqrqBetweenOrderByBjmc(xxdm, yxsh, zyh, teachYear, semester, AttendanceResult.ARRIVED, start, end);
        //请假人次
        Long leave = attendanceStatisticsRepository.countByXxdmAndYxshAndZyhAndXnAndXqmAndKqjgAndKqrqBetweenOrderByBjmc(xxdm, yxsh, zyh, teachYear, semester, AttendanceResult.LEAVE, start, end);
        //旷课人次
        Long absentee = attendanceStatisticsRepository.countByXxdmAndYxshAndZyhAndXnAndXqmAndKqjgAndKqrqBetweenOrderByBjmc(xxdm, yxsh, zyh, teachYear, semester, AttendanceResult.ABSENTEE, start, end);
        //平均到课率
        Double avg = (double) ((total.intValue() - actual.intValue() - leave.intValue() - absentee.intValue()) / total.intValue());

        Map<String, Object> map = new HashMap<>();
        map.put("total", total);
        map.put("actual", actual);
        map.put("leave", leave);
        map.put("absentee", absentee);
        map.put("avg", avg);
        return map;
    }

    /**
     * 获取各班级考勤
     */
    public Map getAttendaneByClass(Long xxdm, String yxsh, String zyh, String bjmc, String teachYear, String semester, String start, String end) {
        //应出勤人次
        Long total = attendanceStatisticsRepository.countByXxdmAndYxshAndZyhAndBjmcAndXnAndXqmAndKqrqBetween(xxdm, yxsh, zyh, bjmc, teachYear, semester, start, end);
        //实到人次
        Long actual = attendanceStatisticsRepository.countByXxdmAndYxshAndZyhAndBjmcAndXnAndXqmAndKqjgAndKqrqBetween(xxdm, yxsh, zyh, bjmc, teachYear, semester, AttendanceResult.ARRIVED, start, end);
        //请假人次
        Long leave = attendanceStatisticsRepository.countByXxdmAndYxshAndZyhAndBjmcAndXnAndXqmAndKqjgAndKqrqBetween(xxdm, yxsh, zyh, bjmc, teachYear, semester, AttendanceResult.LEAVE, start, end);
        //旷课人次
        Long absentee = attendanceStatisticsRepository.countByXxdmAndYxshAndZyhAndBjmcAndXnAndXqmAndKqjgAndKqrqBetween(xxdm, yxsh, zyh, bjmc, teachYear, semester, AttendanceResult.ABSENTEE, start, end);
        //平均到课率
        Double avg = (double) ((total.intValue() - actual.intValue() - leave.intValue() - absentee.intValue()) / total.intValue());

        Map<String, Object> map = new HashMap<>();
        map.put("total", total);
        map.put("actual", actual);
        map.put("leave", leave);
        map.put("absentee", absentee);
        map.put("avg", avg);
        return map;
    }


    /**
     * 查询当前时间所属学年学期，并且格式化时间区间
     */
    public Map getDate(Long xxdm, String dateRange) {
        Calendar c = Calendar.getInstance();
        // 当前年份
        int year = c.get(Calendar.YEAR);
        // 当前月份
        int month = c.get(Calendar.MONTH) + 1;
        //当前日期
        int day = c.get(Calendar.DAY_OF_MONTH);
        String end = year + "-" + month + "-" + day;
        String start;
        //查询当前时间所属学年学期
        Map map = generateWarningInfoService.getXQAndXN(xxdm, end);
        String teachYear = map.get("teacherYear").toString();
        String semester = map.get("semester").toString();
        if ("秋".equals(semester)) {
            semester = "1";
            teachYear = teachYear + "-" + (Integer.parseInt(teachYear) + 1);
        } else {
            semester = "2";
            teachYear = (Integer.parseInt(teachYear) - 1) + "-" + teachYear;
        }
        if (SERVER.equals(dateRange)) {
            start = getPastDate(7);
        } else if (THIRTY.equals(dateRange)) {
            start = getPastDate(30);
        } else if (ONE.equals(dateRange)) {
            start = getPastDate(1);
        } else {
            String[] array = dateRange.split("\\~");
            start = array[0];
            end = array[1];
        }

        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("teachYear", teachYear);
        resultMap.put("semester", semester);
        resultMap.put("start", start);
        resultMap.put("end", end);
        return resultMap;
    }
}
