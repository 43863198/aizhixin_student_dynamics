package com.aizhixin.cloud.datainstall.ftp.utils;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.archivers.zip.ZipArchiveInputStream;
import org.apache.commons.compress.archivers.zip.ZipArchiveOutputStream;
import org.apache.commons.compress.utils.IOUtils;

import java.io.*;
import java.nio.file.Files;

@Slf4j
public class ZipUtil {
    public static void unzip(String fileName, String targetDir) {
        try {
            File outDir = new File(targetDir);
            if (!outDir.exists()) {
                outDir.mkdirs();
            } else {
                File[] files = outDir.listFiles();
                if (files != null && files.length > 0) {
                    for (File file : files) {
                        file.delete();
                    }
                }
            }
            File file = new File(fileName);
            ZipArchiveInputStream input = new ZipArchiveInputStream(new BufferedInputStream(new FileInputStream(file), 1024));
            ArchiveEntry entry = null;
            while ((entry = input.getNextEntry()) != null) {
                if (!input.canReadEntryData(entry)) {
                    continue;
                }
                File f = new File(targetDir, entry.getName());
                if (entry.isDirectory()) {
                    if (!f.isDirectory() && !f.mkdirs()) {
                        throw new IOException("failed to create directory " + f);
                    }
                } else {
                    File parent = f.getParentFile();
                    if (!parent.isDirectory() && !parent.mkdirs()) {
                        throw new IOException("failed to create directory " + parent);
                    }
                    try (OutputStream o = Files.newOutputStream(f.toPath())) {
                        IOUtils.copy(input, o);
                    }
                }
            }
        } catch (Exception e) {
            log.warn("Exception", e);
        }
    }

    public static void zip(String srcFilePath, String outFile) {
        try {
            File srcFile = new File(srcFilePath);
            ZipArchiveOutputStream zipOutput = new ZipArchiveOutputStream(new File(outFile));
            File[] filesToArchive;
            if (srcFile.isDirectory()) {
                filesToArchive = srcFile.listFiles();
            } else {
                filesToArchive = new File[]{srcFile};
            }
            if (filesToArchive != null) {
                for (File f : filesToArchive) {
                    ArchiveEntry entry = zipOutput.createArchiveEntry(f, f.getName());
                    zipOutput.putArchiveEntry(entry);
                    if (f.isFile()) {
                        try (InputStream i = Files.newInputStream(f.toPath())) {
                            IOUtils.copy(i, zipOutput);
                        }
                    }
                    zipOutput.closeArchiveEntry();
                }
            }
            zipOutput.finish();
        } catch (Exception e) {
            log.warn("Exception", e);
        }
    }
}
