package com.alibaba.otter.canal.client.adapter.rdb.test;

import java.sql.SQLException;

import com.alibaba.druid.pool.DruidDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TestConstant {

    private static final Logger logger = LoggerFactory.getLogger(TestConstant.class);
	public static final String    jdbcUrl      = "jdbc:mysql://127.0.0.1:3306/mytest?useUnicode=true";
    public static final String    jdbcUser     = "root";
    public static final String    jdbcPassword = "121212";

    public static final DruidDataSource dataSource;

    static {
        dataSource = new DruidDataSource();
        dataSource.setDriverClassName("com.mysql.jdbc.Driver");
        dataSource.setUrl(jdbcUrl);
        dataSource.setUsername(jdbcUser);
        dataSource.setPassword(jdbcPassword);
        dataSource.setInitialSize(1);
        dataSource.setMinIdle(1);
        dataSource.setMaxActive(1);
        dataSource.setMaxWait(60000);
        dataSource.setTimeBetweenEvictionRunsMillis(60000);
        dataSource.setMinEvictableIdleTimeMillis(300000);
        dataSource.setPoolPreparedStatements(false);
        dataSource.setMaxPoolPreparedStatementPerConnectionSize(20);
        dataSource.setValidationQuery("select 1");
        try {
            dataSource.init();
        } catch (SQLException e) {
            logger.error(e.getMessage(), e);
        }
    }

}
