//package com.aizhixin.cloud.dataanalysis.feign;
//
//import com.aizhixin.cloud.dataanalysis.common.PageData;
//import com.aizhixin.cloud.dataanalysis.feign.vo.CollegeVO;
//import com.aizhixin.cloud.dataanalysis.feign.vo.TeacherVO;
//import org.springframework.cloud.netflix.feign.FeignClient;
//import org.springframework.stereotype.Component;
//import org.springframework.web.bind.annotation.RequestParam;
//
//import java.util.Map;
//
///**
// * @author: Created by jianwei.wu
// * @E-mail: wujianwei@aizhixin.com
// * @Date: 2018-04-16
// */
//@Component
//public class OrgManagerFeignFallback implements OrgManagerFeignService{
//    @Override
//    public String getSemester(Long id) {
//        return null;
//    }
//
//    @Override
//    public Map getUser(Long id) {
//        return null;
//    }
//
//    @Override
//    public String getClassesByTeacher(@RequestParam(value = "teacherId ") Long teacherId) {
//        return null;
//    }
//
//    @Override
//    public String getTeacherIds(@RequestParam(value = "orgId") Long orgId) {
//        return null;
//    }
//
//    @Override
//    public PageData<CollegeVO> queryCollege(
//            @RequestParam(value = "orgId") Long orgId,
//            @RequestParam(value = "name", required = false) String name,
//            @RequestParam(value = "pageNumber", required = false) Integer pageNumber,
//            @RequestParam(value = "pageSize", required = false) Integer pageSize) {
//        return null;
//    }
//
//    @Override
//    public PageData<TeacherVO> queryTeacher(
//            @RequestParam(value = "orgId") Long orgId,
//            @RequestParam(value = "collegeId", required = false) Long collegeId,
//            @RequestParam(value = "name", required = false) String name,
//            @RequestParam(value = "pageNumber", required = false) Integer pageNumber,
//            @RequestParam(value = "pageSize", required = false) Integer pageSize) {
//        return null;
//    }
//}
