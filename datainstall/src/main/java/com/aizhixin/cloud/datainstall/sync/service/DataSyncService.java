package com.aizhixin.cloud.datainstall.sync.service;

import com.aizhixin.cloud.datainstall.db.service.DataBaseQueryService;
import com.aizhixin.cloud.datainstall.ftp.service.FTPService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DataSyncService {
    @Autowired
    private FTPService ftpService;
    @Autowired
    private DataBaseQueryService dataBaseQueryService;

    /**
     * 1. 下载配置, 解压
     * 2. 导出sql文件
     * 3. 压缩, 上传
     */
    public void syncData(){
        //1. 下载配置, 解压
        ftpService.downloadConfig();
        //2. 导出sql文件
        dataBaseQueryService.fromDbConfigReadAndOutJson();
        //3. 压缩, 上传
        ftpService.uploadFile();
    }
}
