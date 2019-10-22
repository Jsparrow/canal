package com.alibaba.otter.canal.sink.stub;

import java.util.List;
import java.util.concurrent.TimeUnit;

import com.alibaba.otter.canal.protocol.position.Position;
import com.alibaba.otter.canal.store.CanalEventStore;
import com.alibaba.otter.canal.store.CanalStoreException;
import com.alibaba.otter.canal.store.model.Event;
import com.alibaba.otter.canal.store.model.Events;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DummyEventStore implements CanalEventStore<Event> {

    private static final Logger logger = LoggerFactory.getLogger(DummyEventStore.class);

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
        logger.info("time:" + data.getExecuteTime());
    }

    @Override
	public boolean put(Event data, long timeout, TimeUnit unit) throws InterruptedException {
        logger.info("time:" + data.getExecuteTime());
        return true;
    }

    @Override
	public boolean tryPut(Event data) {
        logger.info("time:" + data.getExecuteTime());
        return true;
    }

    @Override
	public void put(List<Event> datas) throws InterruptedException {
        Event data = datas.get(0);
        logger.info("time:" + data.getExecuteTime());
    }

    @Override
	public boolean put(List<Event> datas, long timeout, TimeUnit unit) throws InterruptedException {
        Event data = datas.get(0);
        logger.info("time:" + data.getExecuteTime());
        return true;
    }

    @Override
	public boolean tryPut(List<Event> datas) {
        Event data = datas.get(0);
        logger.info("time:" + data.getExecuteTime());
        return true;
    }

}
