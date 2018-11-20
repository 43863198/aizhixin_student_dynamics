package com.aizhixin.cloud.dataground.service;

import com.aizhixin.cloud.dataground.commons.FileBaseUtils;
import com.aizhixin.cloud.dataground.commons.ReadZipFileTemplate;
import com.aizhixin.cloud.dataground.commons.ZXFTPClient;
import com.aizhixin.cloud.dataground.config.Config;
import com.aizhixin.cloud.dataground.manager.JdbcManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;

@Component
@Slf4j
public class SynDataToDBService {
    private final  static String ESCAPE_SQL_CHAR = "#%29%3b#";//sql需转义字符
    private final  static String ESCAPE_SQL_DEST_CHAR = ");\n";//sql转义目标字符

    @Autowired
    private Config config;
    @Autowired
    private ZXFTPClient zxftpClient;
    @Autowired
    JdbcManager jdbcManager;

    public void downloadFtpFile(String fileName) {
        //validate local download dir
        String baseDownloadDir = FileBaseUtils.createDateDirByBase(config.getFtpDownDir());
        File f = zxftpClient.downloadFile(fileName, baseDownloadDir, true);
        if (null != f) {
            log.info("Download file to local[{}], size({})", f, f.length());
            readAndProcessZipFile(f);
        } else {
            log.info("Download file({}) fail", fileName);
        }
    }

    public void readAndProcessZipFile(File zipFile) {
        new ReadZipFileTemplate(zipFile) {
            public  void doZipEntryFile(String fileName, BufferedReader br) throws IOException {
                log.info("Start process file({})", fileName);
                //根据文件名称做一些额外的处理策略
                int sqlCount = 0;
                StringBuilder lineStr = new StringBuilder();
                String line = null;
                while ((line = br.readLine()) != null) {
                    lineStr.append(line);
                    if (line.endsWith(");")) {
                        line = lineStr.substring(0, lineStr.length() - 1);
                        if (line.indexOf(ESCAPE_SQL_CHAR) > 0) {
                            jdbcManager.execute(line.replaceAll(ESCAPE_SQL_CHAR, ESCAPE_SQL_DEST_CHAR));
                            sqlCount++;
                        } else {
                            jdbcManager.execute(line);
                            sqlCount++;
                        }
                        lineStr.delete(0, lineStr.length());
                        if (0 == sqlCount % 50) {
                            log.info("Process file({}) execute sql count:{}", fileName, sqlCount);//进度显示
                        }
                    }
                }
                log.info("Process file ({}) complete. sql execute count:({})", fileName, sqlCount);
            }
        };
    }
}
