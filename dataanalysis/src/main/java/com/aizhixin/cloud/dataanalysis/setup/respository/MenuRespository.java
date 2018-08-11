package com.aizhixin.cloud.dataanalysis.setup.respository;


import com.aizhixin.cloud.dataanalysis.setup.mongoEntity.Menu;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Set;

public interface MenuRespository extends MongoRepository<Menu, String> {
    List<Menu> findByOrgIdAndTag(Long orgId, String tag);
    List<Menu> findByOrgIdAndTagAndRolesIn(Long orgId, String tag, Set<String> rolesSet);
    void deleteById(String id);
}
