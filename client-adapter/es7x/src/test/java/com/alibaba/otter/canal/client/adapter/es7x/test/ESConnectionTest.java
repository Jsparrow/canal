package com.alibaba.otter.canal.client.adapter.es7x.test;

import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;

import org.elasticsearch.cluster.metadata.MappingMetaData;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import com.alibaba.otter.canal.client.adapter.es7x.support.ESConnection;
import org.springframework.util.Assert;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Ignore
public class ESConnectionTest {

    private static final Logger logger = LoggerFactory.getLogger(ESConnectionTest.class);
	ESConnection esConnection;

    @Before
    public void init() throws UnknownHostException {
        String[] hosts = new String[] { "127.0.0.1:9200" };
        Map<String, String> properties = new HashMap<>();
        properties.put("cluster.name", "elasticsearch");
        esConnection = new ESConnection(hosts, properties, ESConnection.ESClientMode.REST);
    }

    @Test
    public void test01() {
        MappingMetaData mappingMetaData = esConnection.getMapping("mytest_user");

        Map<String, Object> sourceMap = mappingMetaData.getSourceAsMap();
        Map<String, Object> esMapping = (Map<String, Object>) sourceMap.get("properties");
        esMapping.entrySet().forEach(entry -> {
            Map<String, Object> value = (Map<String, Object>) entry.getValue();
            if (value.containsKey("properties")) {
                logger.info(entry.getKey() + " object");
            } else {
                logger.info(new StringBuilder().append(entry.getKey()).append(" ").append(value.get("type")).toString());
                Assert.notNull(entry.getKey(), "null column name");
                Assert.notNull(value.get("type"), "null column type");
            }
        });
    }
}
