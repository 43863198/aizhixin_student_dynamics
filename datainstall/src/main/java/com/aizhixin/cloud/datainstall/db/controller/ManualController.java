package com.aizhixin.cloud.datainstall.db.controller;


import com.aizhixin.cloud.datainstall.db.service.DataBaseQueryService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/manual")
@Api(description = "手动操作API")
public class ManualController {
    @Autowired
    private DataBaseQueryService dataBaseQueryService;
//    @Autowired
//    private ConfigHelper configHelper;
//
//    @GetMapping(value = "/dbconfig", produces = MediaType.APPLICATION_JSON_VALUE)
//    @ApiOperation(httpMethod = "GET", value = "获取数据库表配置", response = Void.class, notes = "获取数据库表配置<br><br><b>@author panzhen</b>")
//    public List<TableDefine> getDbConfig(
//            @ApiParam(value = "dbConfigDir 表结构配置文件的根目录") @RequestParam(value = "dbConfigDir", required = false) String dbConfigDir) {
//        return configHelper.readTablesConfig(dbConfigDir);
//    }

    @PutMapping(value = "/export", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(httpMethod = "PUT", value = "根据配置导到所有的表数据", response = Void.class, notes = "根据配置导到所有的表数据<br><br><b>@author panzhen</b>")
    public void export() {
        dataBaseQueryService.fromDbConfigReadAndOutJson();
    }
}
