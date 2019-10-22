package com.alibaba.otter.canal.parse.inbound.mysql.tsdb;

import java.io.FileNotFoundException;
import java.io.IOException;

import org.junit.Test;

import com.alibaba.fastsql.sql.repository.SchemaObject;
import com.alibaba.fastsql.sql.repository.SchemaRepository;
import com.alibaba.fastsql.util.JdbcConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author agapple 2018年6月7日 下午5:36:13
 * @since 3.1.9
 */
public class FastsqlSchemaTest {

    private static final Logger logger = LoggerFactory.getLogger(FastsqlSchemaTest.class);

	@Test
    public void testSimple() throws IOException {
        SchemaRepository repository = new SchemaRepository(JdbcConstants.MYSQL);
        String sql = "create table quniya4(name varchar(255) null,value varchar(255) null,id int not null,constraint quniya4_pk primary key (id));"
                     + "alter table quniya4 modify id int not null first;";
        repository.console(sql);

        repository.setDefaultSchema("test");
        SchemaObject table = repository.findTable("quniya4");
        logger.info(table.getStatement().toString());
    }
}
