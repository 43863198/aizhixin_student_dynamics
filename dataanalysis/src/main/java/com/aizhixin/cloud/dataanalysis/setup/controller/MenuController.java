package com.aizhixin.cloud.dataanalysis.setup.controller;

import com.aizhixin.cloud.dataanalysis.setup.mongoEntity.Menu;
import com.aizhixin.cloud.dataanalysis.setup.service.MenuService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

/**
 * 菜单
 */
@RestController
@RequestMapping("/v1/base/menu")
@Api(description = "基础数据菜单相关API")
public class MenuController {
    @Autowired
    private MenuService menuService;


    @PostMapping(value = "/save", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(httpMethod = "POST", value = "添加数据", response = Void.class, notes = "添加数据<br><br><b>@author pazhen</b>")
    public  Menu save(@ApiParam(value = "菜单内容", required = true) @RequestBody Menu menu) {
     return menuService.save(menu);
    }

    @GetMapping(value = "/list", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(httpMethod = "GET", value = "查询数据", response = Void.class, notes = "查询数据<br><br><b>@author pazhen</b>")
    public List<Menu> list(@ApiParam(value = "菜单内容", required = true) @RequestParam Long orgId,
                           @ApiParam(value = "菜单内容", required = true) @RequestParam String tag,
                           @ApiParam(value = "菜单内容")@RequestParam(required = false) Set<String> roles) {
        if (null == roles || roles.isEmpty()) {
            return menuService.findByOrgIdAndTag(orgId, tag);
        } else {
            return menuService.findByOrgIdAndTagAndRoles(orgId, tag, roles);
        }
    }

}
