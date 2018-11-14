package com.aizhixin.cloud.datainstall.commons;

import lombok.extern.slf4j.Slf4j;

import java.io.*;

@Slf4j
public abstract class U8TextFileLineTemplate {
    final static String FILE_CHAR = "UTF-8";
    public U8TextFileLineTemplate(File f) {
        try(BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(f), FILE_CHAR))) {
            String line = null;
            line = br.readLine();
            while (null != line) {
                doLine(line);
                line = br.readLine();
            }
        } catch (IOException ioe) {
            log.warn("{}", ioe);
        }
    }

    public abstract void doLine(String line);
}
