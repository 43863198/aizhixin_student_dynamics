package com.aizhixin.cloud.datainstall.db.service;

import com.aizhixin.cloud.datainstall.commons.FileBaseUtils;
import com.aizhixin.cloud.datainstall.commons.JsonUtil;
import com.aizhixin.cloud.datainstall.config.Config;
import com.aizhixin.cloud.datainstall.db.define.ConfigHelper;
import com.aizhixin.cloud.datainstall.db.define.TableDefine;
import com.aizhixin.cloud.datainstall.db.define.TableField;
import com.aizhixin.cloud.datainstall.db.manager.JdbcManager;
import com.fasterxml.jackson.core.JsonParser;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.compress.utils.CharsetNames;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.text.StringEscapeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.*;
import java.util.List;
import java.util.Map;

@Component
@Slf4j
public class DataBaseQueryService {

    @Autowired
    private Config config;

    @Autowired
    private ConfigHelper configHelper;

    @Autowired
    private JdbcManager jdbcManager;

    public boolean fromDbConfigReadAndOutJson() {
        try {
            cleanOutFiles();
            List<TableDefine> tables = configHelper.readTablesConfig(config.getDbConfigDir());
            for (TableDefine t : tables) {
                queryTableAndOutSql(t);
            }
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    private void queryTableAndOutJson(TableDefine t) {
        if (StringUtils.isNotEmpty(t.getSelectSQL())) {
            try {
                String sql = t.getSelectSQL() + " LIMIT " + config.getDbBatchSize();
                int offset = 0;

                List<Map<String, Object>> lines = jdbcManager.query(sql + " OFFSET " + offset);
                if (!lines.isEmpty()) {
                    outDataToJson(t, lines);
                }
                while (lines.size() >= config.getDbBatchSize()) {
                    offset += config.getDbBatchSize();
                    lines = jdbcManager.query(sql + " OFFSET " + offset);
                    if (!lines.isEmpty()) {
                        outDataToJson(t, lines);
                    }
                }

            } catch (Exception e) {
                log.warn("Exception", e);
            }
        }
    }

    private void outDataToJson(TableDefine t, List<Map<String, Object>> lines) {
        String dir = FileBaseUtils.createDateDirByBase(config.getDbOutDir());
        String json = JsonUtil.encode(lines);
        File outFile = new File(dir, t.getName() + ".json");
        try {
            FileUtils.writeStringToFile(outFile, json, CharsetNames.UTF_8, true);
        } catch (IOException e) {
            log.warn("Exception", e);
        }
    }

    private void queryTableAndOutSql(TableDefine t) {
        if (StringUtils.isNotEmpty(t.getSelectSQL())) {
            try {
                String sql = t.getSelectSQL() + " LIMIT " + config.getDbBatchSize();
                int offset = 0;

                List<Map<String, Object>> lines = jdbcManager.query(sql + " OFFSET " + offset);
                if (!lines.isEmpty()) {
                    outDataToSql(t, lines);
                }
                while (lines.size() >= config.getDbBatchSize()) {
                    offset += config.getDbBatchSize();
                    lines = jdbcManager.query(sql + " OFFSET " + offset);
                    if (!lines.isEmpty()) {
                        outDataToSql(t, lines);
                    }
                }

            } catch (Exception e) {
                log.warn("Exception", e);
            }
        }
    }

    private void outDataToSql(TableDefine t, List<Map<String, Object>> lines) throws Exception {
        try {
            String dir = FileBaseUtils.createDateDirByBase(config.getDbOutDir());
            String values = "";
            for (Map<String, Object> item : lines) {
                if (!values.equals("")) {
                    values += ",";
                }
                String value = "";
                for (TableField field : t.getFields()) {
                    if (!value.equals("")) {
                        value += ",";
                    }
                    Object v = item.get(field.getName());
                    if (v != null) {
                        value += " '" + escapeSql(v.toString()) + "'";
                    } else {
                        value += " null";
                    }
                }
                values += "(" + value + ")";
            }
            String fields = "";
            for (TableField field : t.getFields()) {
                if (!fields.equals("")) {
                    fields += ", ";
                }
                fields += field.getName();
            }
            String sql = "INSERT INTO " + t.getName() + " (" + fields + ") VALUES " + values + ";\n";
            File outFile = new File(dir, t.getName() + ".sql");
            FileUtils.writeStringToFile(outFile, sql, CharsetNames.UTF_8, true);
        } catch (Exception e) {
            log.warn("Exception", e);
            throw e;
        }
    }

    private void cleanOutFiles() throws Exception {
        try {
            String dir = FileBaseUtils.createDateDirByBase(config.getDbOutDir());
            File dirFile = new File(dir);
            if (dirFile.exists()) {
                if (dirFile.isDirectory()) {
                    File[] files = dirFile.listFiles();
                    if (files != null && files.length > 0) {
                        for (File f : files) {
                            f.delete();
                        }
                    }
                }
                dirFile.delete();
            }
        } catch (Exception e) {
            log.warn("Exception", e);
            throw e;
        }
    }

    private String escapeSql(String str) {
        if (str == null) {
            return null;
        }
        str = StringUtils.replace(str, "'", "''");
        str = StringUtils.replace(str, ");\n", "#%29%3b#");
        return str;
    }
}
