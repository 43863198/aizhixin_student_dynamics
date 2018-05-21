package com.aizhixin.cloud.dataanalysis.setup.respository;

import com.aizhixin.cloud.dataanalysis.setup.entity.Rule;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

/**
 * @author: Created by jianwei.wu
 * @E-mail: wujianwei@aizhixin.com
 * @Date: 2018-05-17
 */
public interface RuleRespository  extends PagingAndSortingRepository<Rule, String> {

     List<Rule> findByOrgIdAndDeleteFlag(Long orgId,Integer deleteFlag);

     List<Rule> findByOrgIdAndName(Long orgId,String name);

}
