package com.aizhixin.cloud.datainstall.db.service;

import com.aizhixin.cloud.datainstall.commons.FileUtils;
import com.aizhixin.cloud.datainstall.db.define.ConfigHelper;
import com.aizhixin.cloud.datainstall.db.define.TableDefine;
import com.aizhixin.cloud.datainstall.db.define.TableField;
import com.aizhixin.cloud.datainstall.db.manager.JdbcManager;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.csv.CSVPrinter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.io.*;
import java.util.List;
import java.util.Map;

@Component
@Slf4j
public class DataBaseQueryService {

    @Value("${db.batch.size}")
    private int batchSize = 10000;

    @Value("${db.config.dir}")
    private String configBaseDir;

    @Value("${db.out.dir}")
    private String outDir;

    @Autowired
    private ConfigHelper configHelper;

    @Autowired
    private JdbcManager jdbcManager;

    public void queryTableAndOutJson(TableDefine t) {
        String sql = t.getSelectSQL() + " LIMIT " + batchSize;
        int offset = 0;
        String dir = FileUtils.createDateDirByBase(outDir);
        if (null != dir) {
            File f = new File(dir, t.getName() + ".csv");
//            CSVPrinter csvPrinter = null;
//            CSVFormat csvFormat = CSVFormat.MYSQL.withIgnoreHeaderCase();
            try (BufferedWriter out = new BufferedWriter(new OutputStreamWriter (new FileOutputStream(f), "UTF-8"))) {
//                csvPrinter = new CSVPrinter(out, csvFormat);
                List<Map<String, Object>> lines = jdbcManager.query(sql + " OFFSET " + offset);
                while (lines.size() >= batchSize) {
//                    JsonUtil.encode(out, lines);
//                    outDataToCsv(csvPrinter, t, lines);
                    offset += batchSize;
                    lines = jdbcManager.query(sql + " OFFSET " + offset);
                }
                if (!lines.isEmpty()) {
//                    JsonUtil.encode(out, lines);
//                    outDataToCsv(csvPrinter, t, lines);
                }
            } catch (IOException ioe) {
                log.warn("Write table ({}) to file ({}) fail.", t.getName(), f.toString());
            }
        } else {
            log.warn("Write table ({}) to create dir ({}) fail.", t.getName(), dir);
        }
    }

    private void outDataToCsv(CSVPrinter csvPrinter, TableDefine t, List<Map<String, Object>> lines) throws IOException {
        for (Map<String, Object> line : lines) {
            if (null != t.getFields()) {
                for(TableField f : t.getFields()) {
                    Object o = line.get(f.getName());
                    if (null != o) {
                        csvPrinter.print(o.toString());
                    } else {
                        csvPrinter.print(null);
                    }
                }
                csvPrinter.println();
            }
        }
    }

    @Async
    public void fromDbConfigReadAndOutJson() {
        List<TableDefine> tables = configHelper.readTablesConfig(configBaseDir);
        for (TableDefine t : tables) {
            queryTableAndOutJson(t);
        }
    }
}
