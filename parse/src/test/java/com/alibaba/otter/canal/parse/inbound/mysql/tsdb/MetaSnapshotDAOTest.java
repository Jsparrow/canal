package com.alibaba.otter.canal.parse.inbound.mysql.tsdb;

import javax.annotation.Resource;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.alibaba.otter.canal.parse.inbound.mysql.tsdb.dao.MetaSnapshotDAO;
import com.alibaba.otter.canal.parse.inbound.mysql.tsdb.dao.MetaSnapshotDO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by wanshao Date: 2017/9/20 Time: 下午5:00
 **/
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/tsdb/h2-tsdb.xml" })
public class MetaSnapshotDAOTest {

    private static final Logger logger = LoggerFactory.getLogger(MetaSnapshotDAOTest.class);
	@Resource
    MetaSnapshotDAO metaSnapshotDAO;

    @Ignore
    @Test
    public void testSimple() {
        MetaSnapshotDO metaSnapshotDO = new MetaSnapshotDO();
        metaSnapshotDO.setDestination("test");
        metaSnapshotDO.setBinlogFile("000001");
        metaSnapshotDO.setBinlogOffest(4L);
        metaSnapshotDO.setBinlogMasterId("1");
        metaSnapshotDO.setBinlogTimestamp(System.currentTimeMillis() - 7300 * 1000);
        metaSnapshotDO.setData("test");
        metaSnapshotDAO.insert(metaSnapshotDO);

        MetaSnapshotDO snapshotDO = metaSnapshotDAO.findByTimestamp("test", System.currentTimeMillis());
        logger.info(String.valueOf(snapshotDO));

        int count = metaSnapshotDAO.deleteByTimestamp("test", 7200);
        logger.info(String.valueOf(count));
    }

}
