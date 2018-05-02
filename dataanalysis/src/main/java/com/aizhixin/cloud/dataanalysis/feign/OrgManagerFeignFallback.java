package com.aizhixin.cloud.dataanalysis.feign;

import org.springframework.stereotype.Component;

/**
 * @author: Created by jianwei.wu
 * @E-mail: wujianwei@aizhixin.com
 * @Date: 2018-04-16
 */
@Component
public class OrgManagerFeignFallback implements OrgManagerFeignService{
    @Override
    public String getSemester(Long id) {
        return null;
    }
}