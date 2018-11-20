package com.aizhixin.cloud.dataground.commons;

import com.aizhixin.cloud.dataground.config.Config;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.*;

@Component
@Slf4j
public class ZXFTPClient {
    @Autowired
    private Config config;

    private FTPClient getClient() {
        FTPClient ftp = new FTPClient();
        try {
            int reply;
            ftp.connect(config.getFtpHost(), config.getFtpPort());
            log.info("Connected to Host({}) on Port ({}) " , config.getFtpHost() , config.getFtpPort());
            reply = ftp.getReplyCode();
            if (!FTPReply.isPositiveCompletion(reply)) {
                ftp.disconnect();
                log.warn("FTP server refused connection.");
            }
            ftp.login(config.getFtpUserName(), config.getFtpPassword());
            ftp.setFileType(FTP.BINARY_FILE_TYPE);
        } catch (Exception e) {
            if (ftp.isConnected()) {
                try {
                    ftp.disconnect();
                } catch (Exception f) {
                    log.warn("{}", f);
                }
            }
            ftp = null;
            log.warn("Could not connect to server.{}", e);
        }
        return ftp;
    }

    private void closeClient(FTPClient ftp) {
        if (ftp != null) {
            try {
                ftp.noop();
                ftp.logout();
            } catch (Exception e) {
                log.warn("{}", e);
            } finally {
                if (ftp.isConnected()) {
                    try {
                        ftp.disconnect();
                    } catch (Exception f) {
                        log.warn("{}", f);
                    }
                }
            }
        }
    }

    public File downloadFile(String fileName, String localDownDir, boolean isDelete) {
        FTPClient ftp = getClient();
        File local = new File(localDownDir, fileName);
        if (ftp != null) {
            try {
                InputStream is = ftp.retrieveFileStream(fileName);
                if (null == is) {//文件不存在
                    return null;
                }
                File dir = new File(localDownDir);
                if (!dir.exists()) {
                    dir.mkdirs();
                }
                OutputStream output = new FileOutputStream(local);
                ftp.retrieveFile(fileName, output);
                output.close();
                log.info("Download ftp file to local[{}] success.", local.toString());
//                FTPFile[] ftpFiles = ftp.listFiles();
//                for (FTPFile ftpFile : ftpFiles) {
//                    File local = new File(config.getFtpDownDir(), ftpFile.getName());
//                    OutputStream output = new FileOutputStream(local);
//                    ftp.retrieveFile(ftpFile.getName(), output);
//                    output.close();
//                    if (isDelete) {
//                        ftp.deleteFile(ftpFile.getName());
//                    }
//                    log.info("Download ftp file to local[{}] success.", local.toString());
//                }
            } catch (IOException e) {
                log.warn("{}", e);
            }
            closeClient(ftp);
        }
        return local;
    }
}
