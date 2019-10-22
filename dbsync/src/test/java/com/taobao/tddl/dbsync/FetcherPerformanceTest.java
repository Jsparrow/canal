package com.taobao.tddl.dbsync;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.util.concurrent.atomic.AtomicLong;

import com.taobao.tddl.dbsync.binlog.DirectLogFetcher;
import com.taobao.tddl.dbsync.binlog.LogEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FetcherPerformanceTest {

    private static final Logger logger = LoggerFactory.getLogger(FetcherPerformanceTest.class);

	public static void main(String args[]) {
        DirectLogFetcher fetcher = new DirectLogFetcher();
        try {
            Class.forName("com.mysql.jdbc.Driver");
            Connection connection = DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306", "root", "hello");
            Statement statement = connection.createStatement();
            statement.execute("SET @master_binlog_checksum='@@global.binlog_checksum'");
            statement.execute(new StringBuilder().append("SET @mariadb_slave_capability='").append(LogEvent.MARIA_SLAVE_CAPABILITY_MINE).append("'").toString());

            fetcher.open(connection, "mysql-bin.000006", 120L, 2);

            AtomicLong sum = new AtomicLong(0);
            long start = System.currentTimeMillis();
            long last = 0;
            long end = 0;

            while (fetcher.fetch()) {
                sum.incrementAndGet();
                long current = sum.get();
                if (current - last >= 100000) {
                    end = System.currentTimeMillis();
                    long tps = ((current - last) * 1000) / (end - start);
                    logger.info(new StringBuilder().append(" total : ").append(sum).append(" , cost : ").append(end - start).append(" , tps : ")
							.append(tps).toString());
                    last = current;
                    start = end;
                }
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        } finally {
            try {
                fetcher.close();
            } catch (IOException e) {
				logger.error(e.getMessage(), e);
            }
        }
    }
}
