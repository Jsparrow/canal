package com.alibaba.otter.canal.admin;

import org.junit.Ignore;
import org.junit.Test;

import com.alibaba.otter.canal.admin.connector.SimpleAdminConnector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Ignore
public class SimpleAdminConnectorTest {

    private static final Logger logger = LoggerFactory.getLogger(SimpleAdminConnectorTest.class);

	@Test
    public void testSimple() {
        SimpleAdminConnector connector = new SimpleAdminConnector("127.0.0.1", 11110, "admin", "admin");
        connector.connect();
        logger.info("check 1 : " + connector.check());
        logger.info("getRunning Before stop 1 : " + connector.getRunningInstances());
        logger.info("stop 1: " + connector.stop());
        logger.info("getRunning After stop 1 : " + connector.getRunningInstances());
        logger.info("check 1 : " + connector.check());
        logger.info("listFile 1 : " + connector.listCanalLog());
        logger.info("getFile 1 : " + connector.canalLog(10));
        connector.disconnect();

        connector.connect();
        logger.info("check 2 : " + connector.check());
        logger.info("get Running Before start : " + connector.getRunningInstances());
        logger.info("start 2 : " + connector.start());
        logger.info("get Running After start : " + connector.getRunningInstances());
        logger.info("check 2 : " + connector.check());
        logger.info("check after before example 2 : " + connector.checkInstance("example"));
        logger.info("stop example 2 : " + connector.stopInstance("example"));
        logger.info("check example 2 : " + connector.checkInstance("example"));
        logger.info("start example 2 : " + connector.startInstance("example"));
        logger.info("check after start example 2 : " + connector.checkInstance("example"));
        logger.info("listFile 2 : " + connector.listCanalLog());
        logger.info("getFile 2 : " + connector.canalLog(10));

        logger.info("listFile 3 : " + connector.listInstanceLog("example"));
        logger.info("getFile 3 : " + connector.instanceLog("example", "example.log", 10));
        connector.disconnect();
    }
}
