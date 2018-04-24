package com.aizhixin.cloud.dataanalysis.feign;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.*;

/**
 * @author: Created by jianwei.wu
 * @E-mail: wujianwei@aizhixin.com
 * @Date: 2018-04-16
 */
@FeignClient(name = "org-manager",fallback=OrgManagerFeignFallback.class)
public interface  OrgManagerFeignService {
    @RequestMapping(value = "/v1/semester/get/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public String getSemester(@PathVariable(value = "id") Long id);
}
