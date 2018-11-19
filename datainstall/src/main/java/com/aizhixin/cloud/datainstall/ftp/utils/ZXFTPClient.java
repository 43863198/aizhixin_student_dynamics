package com.aizhixin.cloud.datainstall.ftp.utils;

import com.aizhixin.cloud.datainstall.config.Config;
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

    private FTPClient getClient() throws Exception {
        FTPClient ftp = new FTPClient();
        try {
            int reply;
            ftp.connect(config.getFtpHost(), config.getFtpPort());
            System.out.println("Connected to " + config.getFtpHost() + " on " + config.getFtpPort());
            reply = ftp.getReplyCode();
            log.info("FTP reply code: {}", reply);
            if (!FTPReply.isPositiveCompletion(reply)) {
                ftp.disconnect();
                log.warn("FTP server refused connection.");
                ftp = null;
            } else {
                ftp.login(config.getFtpUserName(), config.getFtpPassword());
                log.info("FTP登录成功");
                ftp.setFileType(FTP.BINARY_FILE_TYPE);
            }
        } catch (Exception e) {
            log.warn("Exception", e);
            if (ftp.isConnected()) {
                try {
                    ftp.disconnect();
                } catch (Exception f) {
                }
            }
            throw e;
        }
        return ftp;
    }

    private void closeClient(FTPClient ftp) {
        if (ftp != null) {
            try {
                ftp.noop();
                ftp.logout();
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (ftp.isConnected()) {
                    try {
                        ftp.disconnect();
                    } catch (Exception f) {
                        f.printStackTrace();
                    }
                }
            }
        }
    }

    public String downloadFile(String filename, boolean isDelete) throws Exception {
        FTPClient ftp = getClient();
        String result = "";
        if (ftp != null) {
            try {
                File dir = new File(config.getFtpDownDir());
                if (!dir.exists()) {
                    dir.mkdirs();
                }
                File local = new File(config.getFtpDownDir(), filename);
                OutputStream output = new FileOutputStream(local);
                ftp.retrieveFile(filename, output);
                output.close();
                if (isDelete) {
                    ftp.deleteFile(filename);
                }
                if (local.exists()) {
                    result = local.getAbsolutePath();
                }
            } catch (Exception e) {
                log.warn("Exception", e);
                throw e;
            } finally {
                closeClient(ftp);
            }

        }
        return result;
    }

    public void uploadFile(File file) throws Exception {
        FTPClient ftp = getClient();
        if (ftp != null) {
            try {
                InputStream input = new FileInputStream(file);
                ftp.storeFile(file.getName(), input);
                input.close();
            } catch (Exception e) {
                log.warn("Exception", e);
                throw e;
            } finally {
                closeClient(ftp);
            }
        }
    }
}
