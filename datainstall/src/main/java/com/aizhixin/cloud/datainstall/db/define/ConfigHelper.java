package com.aizhixin.cloud.datainstall.db.define;

import com.aizhixin.cloud.datainstall.commons.FileUtils;
import com.aizhixin.cloud.datainstall.commons.U8TextFileLineTemplate;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

@Component
public class ConfigHelper {
    public List<TableDefine> readTablesConfig(String baseDir) {
        List<TableDefine> list = new ArrayList<>();
//        String dir = configBaseDir;
//        if (!StringUtils.isEmpty(baseDir)) {
//            dir = baseDir;
//        }
        List<File> files = FileUtils.getDirFileNames(baseDir);
        for (File f : files) {
            final TableDefine table = new TableDefine ();
            String fileName = f.getName();
            int p = fileName.lastIndexOf(".");
            if (p > 0) {
                fileName = fileName.substring(0, p);
            }
            table.setName(fileName);
            new U8TextFileLineTemplate(f) {
                public void doLine(String line) {
                    String[] fs = line.split(",");
                    if (fs.length >= 2) {
                        table.addField(fs[0], fs[1]);
                    }
                }
            };
            list.add(table);
        }
        return list;
    }
}
