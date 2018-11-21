package com.aizhixin.cloud.dataground.commons;

import lombok.extern.slf4j.Slf4j;

import java.io.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;

@Slf4j
public abstract class ReadZipFileTemplate {

    /**
     * 仅读取文件(有内容的)，忽略目录及目录下子目录及文件
     */
    public ReadZipFileTemplate(File zipFile) {
        ZipFile zf = null;
        ZipInputStream zin = null;
        try {
            zf = new ZipFile(zipFile);
            zin = new ZipInputStream(new BufferedInputStream(new FileInputStream(zipFile)));
            ZipEntry ze = null;
            while ((ze = zin.getNextEntry()) != null) {
                if (!ze.isDirectory()) {
                    long size = ze.getSize();
                    if (size > 0) {
                        BufferedReader br = new BufferedReader(new InputStreamReader(zf.getInputStream(ze)));
                        doZipEntryFile(ze.getName(), br);
                        br.close();
                    } else {
                        log.warn("Ignore processing file:{}, file size:{}", ze.getName(), ze.getSize());
                    }
                }
                zin.closeEntry();
            }
        } catch (IOException e) {
            log.warn("{}", e);
        } finally {
            if (null != zin) {
                try {
                    zin.close();
                } catch (IOException e) {
                    log.warn("{}", e);
                }
            }
            if (null != zf) {
                try {
                    zf.close();
                } catch (IOException e) {
                    log.warn("{}", e);
                }
            }
        }
    }

    public abstract void doZipEntryFile(String fileName, BufferedReader br) throws IOException;
}
