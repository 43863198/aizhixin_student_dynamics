package com.aizhixin.cloud.dataground.controller;

import com.aizhixin.cloud.dataground.service.SynDataToDBService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;

@RestController
@RequestMapping("/api/web/manual")
@Api(description = "手动操作API")
public class ManualController {
    @Autowired
    private SynDataToDBService syncData;

    @PutMapping(value = "/download", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(httpMethod = "PUT", value = "执行同步", response = Void.class, notes = "执行同步<br><br><b>@author hsh</b>")
    public void download(@ApiParam(value = "FTP文件名称", required = true) @RequestParam(value = "fileName") String fileName) {
        syncData.downloadFtpFileAndProcess(fileName);
    }

    @PutMapping(value = "/processzip", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(httpMethod = "PUT", value = "处理zip文件sql数据文件", response = Void.class, notes = "处理zip文件sql数据文件<br><br><b>@author hsh</b>")
    public void doProcessZip(@ApiParam(value = "FTP文件名称", required = true) @RequestParam(value = "file") String file) {
        File f = new File(file);
        if (f.exists() && f.isFile()) {
            syncData.readAndProcessZipFile(f);
        }
    }

}
