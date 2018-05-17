package com.aizhixin.cloud.dataanalysis.feign;

import com.aizhixin.cloud.dataanalysis.common.PageData;
import com.aizhixin.cloud.dataanalysis.feign.vo.CollegeVO;
import com.aizhixin.cloud.dataanalysis.feign.vo.TeacherVO;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

/**
 * @author: Created by jianwei.wu
 * @E-mail: wujianwei@aizhixin.com
 * @Date: 2018-04-16
 */
@FeignClient(name = "org-manager")
public interface OrgManagerFeignService {
    @RequestMapping(value = "/v1/semester/get/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    String getSemester(@PathVariable(value = "id") Long id);

    @RequestMapping(value = "/v1/user/get/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    Map getUser(@PathVariable(value = "id") Long id);

    /**
     * 分页查询所有的学院信息
     *
     * @param orgId
     * @param name
     * @param pageNumber
     * @param pageSize
     * @return
     */
    @RequestMapping(value = "/v1/college/list", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    PageData<CollegeVO> queryCollege(
            @RequestParam(value = "orgId") Long orgId,
            @RequestParam(value = "name", required = false) String name,
            @RequestParam(value = "pageNumber", required = false) Integer pageNumber,
            @RequestParam(value = "pageSize", required = false) Integer pageSize);


    /**
     * 分页查询所有的学院信息
     *
     * @param orgId
     * @param name
     * @param pageNumber
     * @param pageSize
     * @return
     */
    @RequestMapping(value = "/v1/teacher/list", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    PageData<TeacherVO> queryTeacher(
            @RequestParam(value = "orgId") Long orgId,
            @RequestParam(value = "collegeId", required = false) Long collegeId,
            @RequestParam(value = "name", required = false) String name,
            @RequestParam(value = "pageNumber", required = false) Integer pageNumber,
            @RequestParam(value = "pageSize", required = false) Integer pageSize);
}
