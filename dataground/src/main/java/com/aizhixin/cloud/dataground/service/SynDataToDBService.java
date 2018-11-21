package com.aizhixin.cloud.dataground.service;

import com.aizhixin.cloud.dataground.commons.FileBaseUtils;
import com.aizhixin.cloud.dataground.commons.ReadZipFileTemplate;
import com.aizhixin.cloud.dataground.commons.ZXFTPClient;
import com.aizhixin.cloud.dataground.config.Config;
import com.aizhixin.cloud.dataground.manager.JdbcManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;

/**
 * 拉取FTP相应目录的Zip文件，覆盖到对应数据库表的数据
 */
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

    /**
     * 从ftp上下载并且处理数据的压缩文件
     * 下载成功以后，执行数据库表的覆盖操作
     */
    public void downloadFtpFileAndProcess(String fileName) {
        if (StringUtils.isEmpty(fileName) || !fileName.endsWith(".zip")) {
            log.info("File name ({}) is not end with .zip.");
            return;
        }
        String baseDownloadDir = config.getFtpDownDir();
        FileBaseUtils.validateAndCreateDir(baseDownloadDir);//validate local download dir
        File f = zxftpClient.downloadFile(fileName, baseDownloadDir, true);//下载并删除ftp文件
        if (null != f) {
            log.info("Download file to local[{}], size({})", f, f.length());
            readAndProcessZipFile(f);//处理下载成功的zip文件
            //移动文件到已处理目录
            FileBaseUtils.validateAndCreateDir(config.getFtpCompleteDir());
            File fdes = new File(config.getFtpCompleteDir(), fileName);
            FileBaseUtils.move(f, fdes);
        } else {
            log.info("Download file({}) fail", fileName);
        }
    }

    /**
     * 读取zip文件的数据，覆盖数据对应表的数据
     */
    public void readAndProcessZipFile(File zipFile) {
        new ReadZipFileTemplate(zipFile) {
            public  void doZipEntryFile(String fileName, BufferedReader br) throws IOException {
                log.info("Start process file({})", fileName);
                //根据文件名称做一些额外的处理策略
                int p = fileName.indexOf(".");
                String tableName = null;
                if (p > 0) {
                    tableName = fileName.substring(0, p);
                }
                if (null != tableName) {
                    log.info("TRUNCATE table ({}) data", tableName);
                    jdbcManager.execute("TRUNCATE " + tableName);
                }
                int sqlCount = 0;
                StringBuilder lineStr = new StringBuilder();
                String line = null;
                while ((line = br.readLine()) != null) {
                    if(line.trim().length() < 1) {
                        continue;
                    }
                    lineStr.append(line);
                    if (line.endsWith(");")) {
                        line = lineStr.substring(0, lineStr.length() - 1);
                        if(!line.startsWith("INSERT INTO")) {//简单的攻击判断
                            int length = line.length();
                            if (length > 200) {
                                length = 200;
                            }
                            log.warn("SQL ATTACK : {}", line.substring(0, length));
                            break;
                        }
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
