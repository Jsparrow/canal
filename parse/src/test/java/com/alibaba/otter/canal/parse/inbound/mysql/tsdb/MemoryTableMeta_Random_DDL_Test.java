package com.alibaba.otter.canal.parse.inbound.mysql.tsdb;

import java.io.File;
import java.io.FileInputStream;
import java.net.URL;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.alibaba.fastsql.sql.repository.Schema;
import com.alibaba.otter.canal.parse.inbound.TableMeta;
import com.google.common.collect.Lists;

/**
 * @author agapple 2017年8月1日 下午7:15:54
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/tsdb/h2-tsdb.xml" })
public class MemoryTableMeta_Random_DDL_Test {

    @Test
    public void test_database() throws Throwable {
        URL url = Thread.currentThread().getContextClassLoader().getResource("dummy.txt");
        File dummyFile = new File(url.getFile());
        int number = 39;
        for (int i = 1; i <= number; i++) {
            File sourceFile = new File(dummyFile.getParent() + "/ddl/table", new StringBuilder().append("test_").append(i).append(".sql").toString());
            String sourceSql = StringUtils.join(IOUtils.readLines(new FileInputStream(sourceFile)), "\n");
            MemoryTableMeta source = new MemoryTableMeta();
            source.apply(null, "test", sourceSql, null);

            File targetFile = new File(dummyFile.getParent() + "/ddl/table", new StringBuilder().append("mysql_").append(i).append(".sql").toString());
            String targetSql = StringUtils.join(IOUtils.readLines(new FileInputStream(targetFile)), "\n");
            MemoryTableMeta target = new MemoryTableMeta();
            target.apply(null, "test", targetSql, null);

            compareTableMeta(source, target);
        }
    }

    @Test
    public void test_table() throws Throwable {
        URL url = Thread.currentThread().getContextClassLoader().getResource("dummy.txt");
        File dummyFile = new File(url.getFile());
        int number = 80;
        for (int i = 1; i <= number; i++) {
            try {
                File sourceFile = new File(dummyFile.getParent() + "/ddl/alter", new StringBuilder().append("test_").append(i).append(".sql").toString());
                String sourceSql = StringUtils.join(IOUtils.readLines(new FileInputStream(sourceFile)), "\n");
                MemoryTableMeta source = new MemoryTableMeta();
                source.apply(null, "test", sourceSql, null);

                File targetFile = new File(dummyFile.getParent() + "/ddl/alter", new StringBuilder().append("mysql_").append(i).append(".sql").toString());
                String targetSql = StringUtils.join(IOUtils.readLines(new FileInputStream(targetFile)), "\n");
                MemoryTableMeta target = new MemoryTableMeta();
                target.apply(null, "test", targetSql, null);

                compareTableMeta(source, target);
            } catch (Throwable e) {
                Assert.fail(new StringBuilder().append("case : ").append(i).append(" failed by : ").append(e.getMessage()).toString());
            }
        }
    }

    private void compareTableMeta(MemoryTableMeta source, MemoryTableMeta target) {
        List<String> tableNames = Lists.newArrayList();
        source.getRepository().getSchemas().forEach(schema -> tableNames.addAll(schema.showTables()));

        tableNames.forEach(table -> {
            TableMeta sourceMeta = source.find("test", table);
            TableMeta targetMeta = target.find("test", table);
            boolean result = DatabaseTableMeta.compareTableMeta(sourceMeta, targetMeta);
            if (!result) {
                Assert.fail(new StringBuilder().append(sourceMeta.toString()).append(" vs ").append(targetMeta.toString()).toString());
            }
        });
    }
}
