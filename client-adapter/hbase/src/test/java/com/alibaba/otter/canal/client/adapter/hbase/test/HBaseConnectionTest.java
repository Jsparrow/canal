package com.alibaba.otter.canal.client.adapter.hbase.test;

import com.alibaba.otter.canal.client.adapter.hbase.support.HbaseTemplate;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HBaseConnectionTest {

    private static final Logger logger = LoggerFactory.getLogger(HBaseConnectionTest.class);

	@Test
    public void test01() {
        Configuration hbaseConfig = HBaseConfiguration.create();
        hbaseConfig.set("hbase.zookeeper.quorum", "127.0.0.1");
        hbaseConfig.set("hbase.zookeeper.property.clientPort", "2181");
        hbaseConfig.set("zookeeper.znode.parent", "/hbase");
        HbaseTemplate hbaseTemplate = new HbaseTemplate(hbaseConfig);
        logger.info(String.valueOf(hbaseTemplate.tableExists("ttt")));
    }
}
