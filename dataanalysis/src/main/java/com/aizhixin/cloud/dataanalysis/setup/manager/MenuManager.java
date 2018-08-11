package com.aizhixin.cloud.dataanalysis.setup.manager;

import com.aizhixin.cloud.dataanalysis.setup.mongoEntity.Menu;
import com.aizhixin.cloud.dataanalysis.setup.respository.MenuRespository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;

/**
 * 菜单DAO对象
 */
@Component
public class MenuManager {
    @Autowired
    private MenuRespository menuRespository;
    public Menu save (Menu entity) {
        return menuRespository.save(entity);
    }

    public Menu findById(String id) {
        return menuRespository.findOne(id);
    }

    public void deleteById(String id) {
        menuRespository.deleteById(id);
    }

    public List<Menu> findByOrgIdAndTag(Long orgId, String tag) {
        return  menuRespository.findByOrgIdAndTag(orgId, tag);
    }

    public List<Menu> findByOrgIdAndTagAndRolesIn(Long orgId, String tag, Set<String> rolesSet) {
        return  menuRespository.findByOrgIdAndTagAndRolesIn(orgId, tag, rolesSet);
    }
}
