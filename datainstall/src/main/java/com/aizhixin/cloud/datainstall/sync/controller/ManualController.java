package com.aizhixin.cloud.datainstall.sync.controller;

import com.aizhixin.cloud.datainstall.db.service.DataBaseQueryService;
import com.aizhixin.cloud.datainstall.ftp.service.FTPService;
import com.aizhixin.cloud.datainstall.sync.service.DataSyncService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/web/manual")
@Api(description = "手动操作API")
public class ManualController {
    @Autowired
    private DataSyncService syncService;
    @Lazy
    @Autowired
    private FTPService ftpService;
    @Lazy
    @Autowired
    private DataBaseQueryService dataBaseQueryService;

    @PutMapping(value = "/execsync", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(httpMethod = "PUT", value = "执行同步", response = Void.class, notes = "执行同步<br><br><b>@author hsh</b>")
    public void execsync() {
        syncService.syncData();
    }

    @PutMapping(value = "/syncconfig", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(httpMethod = "PUT", value = "下载配置", response = Void.class, notes = "下载配置<br><br><b>@author hsh</b>")
    public void syncconfig() {
        ftpService.downloadConfig();
    }

    @PutMapping(value = "/export", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(httpMethod = "PUT", value = "根据配置导到所有的表数据", response = Void.class, notes = "根据配置导到所有的表数据<br><br><b>@author panzhen</b>")
    public void export() {
        dataBaseQueryService.fromDbConfigReadAndOutJson();
    }

    @PutMapping(value = "/uploadfile", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(httpMethod = "PUT", value = "上传数据", response = Void.class, notes = "上传数据<br><br><b>@author hsh</b>")
    public void uploadfile() {
        ftpService.uploadFile();
    }
}
