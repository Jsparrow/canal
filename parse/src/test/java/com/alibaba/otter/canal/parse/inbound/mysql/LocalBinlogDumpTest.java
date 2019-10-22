package com.alibaba.otter.canal.parse.inbound.mysql;

import java.net.InetSocketAddress;
import java.nio.charset.Charset;
import java.util.List;

import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

import com.alibaba.otter.canal.parse.exception.CanalParseException;
import com.alibaba.otter.canal.parse.index.AbstractLogPositionManager;
import com.alibaba.otter.canal.parse.stub.AbstractCanalEventSinkTest;
import com.alibaba.otter.canal.parse.support.AuthenticationInfo;
import com.alibaba.otter.canal.protocol.CanalEntry.Column;
import com.alibaba.otter.canal.protocol.CanalEntry.Entry;
import com.alibaba.otter.canal.protocol.CanalEntry.EntryType;
import com.alibaba.otter.canal.protocol.CanalEntry.EventType;
import com.alibaba.otter.canal.protocol.CanalEntry.RowChange;
import com.alibaba.otter.canal.protocol.CanalEntry.RowData;
import com.alibaba.otter.canal.protocol.position.EntryPosition;
import com.alibaba.otter.canal.protocol.position.LogPosition;
import com.alibaba.otter.canal.sink.exception.CanalSinkException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
@Ignore
public class LocalBinlogDumpTest {

    private static final Logger logger = LoggerFactory.getLogger(LocalBinlogDumpTest.class);

	@Test
    public void testSimple() {
        String directory = "/Users/wanshao/projects/canal/parse/src/test/resources/binlog/tsdb";
        final LocalBinlogEventParser controller = new LocalBinlogEventParser();
        final EntryPosition startPosition = new EntryPosition("mysql-bin.000003", 123L);

        controller.setMasterInfo(new AuthenticationInfo(new InetSocketAddress("127.0.0.1", 3306), "canal", "canal"));
        controller.setConnectionCharset(Charset.forName("UTF-8"));
        controller.setDirectory(directory);
        controller.setMasterPosition(startPosition);
        controller.setEventSink(new AbstractCanalEventSinkTest<List<Entry>>() {

            @Override
			public boolean sink(List<Entry> entrys, InetSocketAddress remoteAddress, String destination)
                                                                                                        throws InterruptedException {

                for (Entry entry : entrys) {
                    if (entry.getEntryType() == EntryType.TRANSACTIONBEGIN
                        || entry.getEntryType() == EntryType.TRANSACTIONEND) {
                        continue;
                    }

                    if (entry.getEntryType() == EntryType.ROWDATA) {
                        RowChange rowChage = null;
                        try {
                            rowChage = RowChange.parseFrom(entry.getStoreValue());
                        } catch (Exception e) {
                            throw new RuntimeException("ERROR ## parser of eromanga-event has an error , data:"
                                                       + entry.toString(), e);
                        }

                        EventType eventType = rowChage.getEventType();
                        logger.info(String.format("================> binlog[%s:%s] , name[%s,%s] , eventType : %s",
                            entry.getHeader().getLogfileName(),
                            entry.getHeader().getLogfileOffset(),
                            entry.getHeader().getSchemaName(),
                            entry.getHeader().getTableName(),
                            eventType));

                        rowChage.getRowDatasList().forEach(rowData -> {
                            if (eventType == EventType.DELETE) {
                                print(rowData.getBeforeColumnsList());
                            } else if (eventType == EventType.INSERT) {
                                print(rowData.getAfterColumnsList());
                            } else {
                                logger.info("-------> before");
                                print(rowData.getBeforeColumnsList());
                                logger.info("-------> after");
                                print(rowData.getAfterColumnsList());
                            }
                        });
                    }
                }

                return true;
            }

        });
        controller.setLogPositionManager(new AbstractLogPositionManager() {

            @Override
            public LogPosition getLatestIndexBy(String destination) {
                return null;
            }

            @Override
            public void persistLogPosition(String destination, LogPosition logPosition) {
                logger.info(String.valueOf(logPosition));
            }
        });

        controller.start();

        try {
            Thread.sleep(100 * 1000L);
        } catch (InterruptedException e) {
            Assert.fail(e.getMessage());
        }
        controller.stop();
    }

    private void print(List<Column> columns) {
        columns.forEach(column -> logger.info(new StringBuilder().append(column.getName()).append(" : ").append(column.getValue()).append("    update=").append(column.getUpdated()).toString()));
    }
}
