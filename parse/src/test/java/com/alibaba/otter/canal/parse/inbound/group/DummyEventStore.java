package com.alibaba.otter.canal.parse.inbound.group;

import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import com.alibaba.otter.canal.protocol.CanalEntry.EntryType;
import com.alibaba.otter.canal.protocol.position.Position;
import com.alibaba.otter.canal.store.CanalEventStore;
import com.alibaba.otter.canal.store.CanalStoreException;
import com.alibaba.otter.canal.store.model.Event;
import com.alibaba.otter.canal.store.model.Events;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.Collections;

public class DummyEventStore implements CanalEventStore<Event> {

    private static final Logger logger = LoggerFactory.getLogger(DummyEventStore.class);
	private static final String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";
    private static final String messgae     = "{0} [{1}:{2}:{3}]";

    @Override
	public void ack(Position position) {

    }

    @Override
	public void ack(Position position, Long seqId) {

    }

    @Override
	public Events get(Position start, int batchSize) throws InterruptedException {
        return null;
    }

    @Override
	public Events get(Position start, int batchSize, long timeout, TimeUnit unit) throws InterruptedException {
        return null;
    }

    @Override
	public Position getFirstPosition() {
        return null;
    }

    @Override
	public Position getLatestPosition() {
        return null;
    }

    @Override
	public void rollback() {

    }

    @Override
	public Events tryGet(Position start, int batchSize) {
        return null;
    }

    @Override
	public boolean isStart() {
        return false;
    }

    @Override
	public void start() {

    }

    @Override
	public void stop() {

    }

    @Override
	public void cleanAll() {
    }

    @Override
	public void cleanUntil(Position position) {

    }

    @Override
	public void put(Event data) throws InterruptedException {
        put(Collections.singletonList(data));
    }

    @Override
	public boolean put(Event data, long timeout, TimeUnit unit) throws InterruptedException {
        return put(Collections.singletonList(data), timeout, unit);
    }

    @Override
	public boolean tryPut(Event data) {
        return tryPut(Collections.singletonList(data));
    }

    @Override
	public void put(List<Event> datas) throws InterruptedException {
        datas.forEach(data -> {
            Date date = new Date(data.getExecuteTime());
            SimpleDateFormat format = new SimpleDateFormat(DATE_FORMAT);
            if (data.getEntryType() == EntryType.TRANSACTIONBEGIN || data.getEntryType() == EntryType.TRANSACTIONEND) {
                // System.out.println(MessageFormat.format(messgae, new Object[]
                // { Thread.currentThread().getName(),
                // header.getLogfilename(), header.getLogfileoffset(),
                // format.format(date),
                // data.getEntry().getEntryType(), "" }));
                logger.info(String.valueOf(data.getEntryType()));

            } else {
                logger.info(MessageFormat.format(messgae,
                    new Object[] { Thread.currentThread().getName(), data.getJournalName(),
                            String.valueOf(data.getPosition()), format.format(date) }));
            }
        });
    }

    @Override
	public boolean put(List<Event> datas, long timeout, TimeUnit unit) throws InterruptedException {
        datas.forEach(data -> {
            Date date = new Date(data.getExecuteTime());
            SimpleDateFormat format = new SimpleDateFormat(DATE_FORMAT);
            if (data.getEntryType() == EntryType.TRANSACTIONBEGIN || data.getEntryType() == EntryType.TRANSACTIONEND) {
                // System.out.println(MessageFormat.format(messgae, new Object[]
                // { Thread.currentThread().getName(),
                // header.getLogfilename(), header.getLogfileoffset(),
                // format.format(date),
                // data.getEntry().getEntryType(), "" }));
                logger.info(String.valueOf(data.getEntryType()));

            } else {
                logger.info(MessageFormat.format(messgae,
                    new Object[] { Thread.currentThread().getName(), data.getJournalName(),
                            String.valueOf(data.getPosition()), format.format(date) }));
            }
        });
        return true;
    }

    @Override
	public boolean tryPut(List<Event> datas) {
        logger.info("\n>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
        datas.forEach(data -> {

            Date date = new Date(data.getExecuteTime());
            SimpleDateFormat format = new SimpleDateFormat(DATE_FORMAT);
            if (data.getEntryType() == EntryType.TRANSACTIONBEGIN || data.getEntryType() == EntryType.TRANSACTIONEND) {
                // System.out.println(MessageFormat.format(messgae, new Object[]
                // { Thread.currentThread().getName(),
                // header.getLogfilename(), header.getLogfileoffset(),
                // format.format(date),
                // data.getEntry().getEntryType(), "" }));
                logger.info(String.valueOf(data.getEntryType()));

            } else {
                logger.info(MessageFormat.format(messgae,
                    new Object[] { Thread.currentThread().getName(), data.getJournalName(),
                            String.valueOf(data.getPosition()), format.format(date) }));
            }

        });
        logger.info("<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<\n");
        return true;
    }

}
