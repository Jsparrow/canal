package com.alibaba.otter.canal.parse.driver.mysql;

import java.io.IOException;
import java.net.InetSocketAddress;

import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

import com.alibaba.otter.canal.parse.driver.mysql.packets.server.ResultSetPacket;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Ignore
public class MysqlConnectorTest {

    private static final Logger logger = LoggerFactory.getLogger(MysqlConnectorTest.class);

	@Test
    public void testQuery() {

        MysqlConnector connector = new MysqlConnector(new InetSocketAddress("127.0.0.1", 3306), "xxxxx", "xxxxx");
        try {
            connector.connect();
            MysqlQueryExecutor executor = new MysqlQueryExecutor(connector);
            ResultSetPacket result = executor.query("show variables like '%char%';");
            logger.info(String.valueOf(result));
            result = executor.query("select * from test.test1");
            logger.info(String.valueOf(result));
        } catch (IOException e) {
            Assert.fail(e.getMessage());
        } finally {
            try {
                connector.disconnect();
            } catch (IOException e) {
                Assert.fail(e.getMessage());
            }
        }
    }

    // @Test
    public void testUpdate() {

        MysqlConnector connector = new MysqlConnector(new InetSocketAddress("127.0.0.1", 3306), "xxxxx", "xxxxx");
        try {
            connector.connect();
            MysqlUpdateExecutor executor = new MysqlUpdateExecutor(connector);
            executor.update("insert into test.test2(id,name,score,text_value) values(null,'中文1',10,'中文2')");
        } catch (IOException e) {
            Assert.fail(e.getMessage());
        } finally {
            try {
                connector.disconnect();
            } catch (IOException e) {
                Assert.fail(e.getMessage());
            }
        }
    }
}
