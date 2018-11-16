package com.aizhixin.cloud.datainstall.ftp.service;

import com.aizhixin.cloud.datainstall.config.Config;
import com.aizhixin.cloud.datainstall.ftp.utils.ZXFTPClient;
import com.aizhixin.cloud.datainstall.ftp.utils.ZipUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.Date;

@Service
public class FTPService {
    @Autowired
    private ZXFTPClient zxftpClient;
    @Autowired
    private Config config;

    public void uploadFile() {
        //默认文件夹
        String currDate = DateFormatUtils.format(new Date(), "yyyyMMdd");
        File file = new File(config.getDbOutDir(), currDate);
        uploadFile(file.getAbsolutePath());
    }

    public void uploadFile(String dirName) {
        //zip
        File zipDir = new File(config.getFtpUpDir());
        if (!zipDir.exists()) {
            zipDir.mkdirs();
        }
        String currDate = DateFormatUtils.format(new Date(), "yyyyMMdd");
        File zipFile = new File(config.getFtpUpDir(), currDate + ".zip") ;
        ZipUtil.zip(dirName, zipFile.getAbsolutePath());
        //upload
        zxftpClient.uploadFile(zipFile);
    }

    public void downloadConfig() {
        String fileName = "syncconfig.zip";
        String absolutePath = zxftpClient.downloadFile(fileName, false);
        if (StringUtils.isNotEmpty(absolutePath)) {
            ZipUtil.unzip(absolutePath, config.getDbConfigDir());
        }
    }
}
