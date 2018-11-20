package com.aizhixin.cloud.dataground.service;

import com.aizhixin.cloud.dataground.commons.FileBaseUtils;
import com.aizhixin.cloud.dataground.commons.ZXFTPClient;
import com.aizhixin.cloud.dataground.config.Config;
import com.aizhixin.cloud.dataground.manager.JdbcManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;

@Component
@Slf4j
public class SynDataToDBService {
    @Autowired
    private Config config;
    @Autowired
    private ZXFTPClient zxftpClient;
    @Autowired
    JdbcManager jdbcManager;

    public void downloadFtpFile(String fileName) {
        //validate local download dir
        String baseDownloadDir = FileBaseUtils.createDateDirByBase(config.getFtpDownDir());
        File f = zxftpClient.downloadFile(fileName, baseDownloadDir, false);
        if (null != f) {
            log.info("Download file to local[{}], size({})", f, f.length());
            readZipFile(f);
        } else {
            log.info("Download file({}) fail", fileName);
        }
    }

    public void readZipFile(File zipFile) {
        try {
            ZipFile zf = new ZipFile(zipFile);
//            ZipInputStream zip = new ZipInputStream(new FileInputStream(zipFile));
            ZipInputStream zin = new ZipInputStream(new BufferedInputStream(new FileInputStream(zipFile)));
            ZipEntry ze = null;
            while ((ze = zin.getNextEntry()) != null) {
                if (!ze.isDirectory()) {
                    log.info("Output file:{}", ze.getName());
                    long size = ze.getSize();
                    if (size > 0) {
                        BufferedReader br = new BufferedReader(new InputStreamReader(zf.getInputStream(ze)));
                        StringBuilder lineStr = new StringBuilder();
                        String line = null;
                        while ((line = br.readLine()) != null) {
                            lineStr.append(line);
                            if (line.endsWith(");")) {
                                line = lineStr.substring(0, lineStr.length() - 1);
                                if (line.indexOf("#%29%3b#") > 0) {
                                    jdbcManager.execute(line.replaceAll("#%29%3b#", ");\n"));
                                } else {
                                    jdbcManager.execute(line);
                                }
                                lineStr.delete(0, lineStr.length());
                            }
                        }
                        br.close();
                    }
                }
                zin.closeEntry();
            }
            zin.close();
        } catch (IOException e) {
            log.warn("{}", e);
        }
    }
}
