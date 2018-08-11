package com.aizhixin.cloud.dataanalysis.setup.service;

import com.aizhixin.cloud.dataanalysis.setup.manager.MenuManager;
import com.aizhixin.cloud.dataanalysis.setup.mongoEntity.Menu;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;

/**
 *  菜单操作
 */
@Component
public class MenuService {
    @Autowired
    private MenuManager menuManager;

    public Menu save(Menu menu){
        return menuManager.save(menu);
    }

    public List<Menu> findByOrgIdAndTag(Long orgId, String tag) {
        return menuManager.findByOrgIdAndTag(orgId, tag);
    }

    public List<Menu> findByOrgIdAndTagAndRoles(Long orgId, String tag, Set<String> rolesSet) {
        return menuManager.findByOrgIdAndTagAndRolesIn(orgId, tag, rolesSet);
    }

    public Menu findById(String id) {
        return menuManager.findById(id);
    }

    public Menu update(Menu menu) {
        return menuManager.save(menu);
    }
}
