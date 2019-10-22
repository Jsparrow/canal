package com.alibaba.otter.canal.store.memory.buffer;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang.math.RandomUtils;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.util.CollectionUtils;

import com.alibaba.otter.canal.common.utils.BooleanMutex;
import com.alibaba.otter.canal.protocol.position.Position;
import com.alibaba.otter.canal.store.CanalStoreException;
import com.alibaba.otter.canal.store.memory.MemoryEventStoreWithBuffer;
import com.alibaba.otter.canal.store.model.BatchMode;
import com.alibaba.otter.canal.store.model.Event;
import com.alibaba.otter.canal.store.model.Events;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 多线程的put/get/ack/rollback测试
 *
 * @author jianghang 2012-6-20 下午02:50:36
 * @version 1.0.0
 */
public class MemoryEventStoreMultiThreadTest extends MemoryEventStoreBase {

    private static final Logger logger = LoggerFactory.getLogger(MemoryEventStoreMultiThreadTest.class);
	private ExecutorService            executor = Executors.newFixedThreadPool(2); // 1
                                                                                   // producer
                                                                                   // ,1
                                                                                   // cousmer
    private MemoryEventStoreWithBuffer eventStore;

    @Before
    public void setUp() {
        eventStore = new MemoryEventStoreWithBuffer();
        eventStore.setBufferSize(16 * 16);
        eventStore.setBatchMode(BatchMode.MEMSIZE);
        eventStore.start();
    }

    @After
    public void tearDown() {
        eventStore.stop();
    }
    @Ignore
    @Test
    public void test() {
        CountDownLatch latch = new CountDownLatch(1);
        BooleanMutex mutex = new BooleanMutex(true);
        Producer producer = new Producer(mutex, 10);
        Cosumer cosumer = new Cosumer(latch, 20, 50);

        executor.submit(producer);
        executor.submit(cosumer);

        try {
            Thread.sleep(30 * 1000L);
        } catch (InterruptedException e) {
			logger.error(e.getMessage(), e);
        }

        mutex.set(false);
        try {
            latch.await();
        } catch (InterruptedException e) {
			logger.error(e.getMessage(), e);
        }
        executor.shutdown();

        List<Long> result = cosumer.getResult();

        Long last = -1L;
        for (Long offest : result) {
            Assert.assertTrue(last + 1 == offest);// 取出来的数据一定是递增的
            last = offest;
        }
    }

    class Producer implements Runnable {

        private final Logger logger1 = LoggerFactory.getLogger(Producer.class);
		private BooleanMutex mutex;
        private int          freq;

        public Producer(BooleanMutex mutex, int freq){
            this.mutex = mutex;
            this.freq = freq;
        }

        @Override
		public void run() {
            long offest = 0;
            while (true) {
                try {
                    mutex.get();
                    Thread.sleep(RandomUtils.nextInt(freq));
                } catch (InterruptedException e) {
                    logger1.error(e.getMessage(), e);
					return;
                }
                Event event = buildEvent("1", offest++, 1L);

                try {
                    Thread.sleep(RandomUtils.nextInt(freq));
                } catch (InterruptedException e) {
                    logger1.error(e.getMessage(), e);
					return;
                }
                try {
                    eventStore.put(event);
                } catch (InterruptedException | CanalStoreException e) {
					logger1.error(e.getMessage(), e);
                }
            }
        }
    }

    class Cosumer implements Runnable {

        private final Logger logger1 = LoggerFactory.getLogger(Cosumer.class);
		private CountDownLatch latch;
        private int            freq;
        private int            batchSize;
        private List<Long>     result = new ArrayList<>();

        public Cosumer(CountDownLatch latch, int freq, int batchSize){
            this.latch = latch;
            this.freq = freq;
            this.batchSize = batchSize;
        }

        @Override
		public void run() {
            Position first = eventStore.getFirstPosition();
            while (first == null) {
                try {
                    Thread.sleep(RandomUtils.nextInt(freq));
                } catch (InterruptedException e) {
                    logger1.error(e.getMessage(), e);
					latch.countDown();
                    return;
                }

                first = eventStore.getFirstPosition();
            }

            int ackCount = 0;
            int emptyCount = 0;
            while (emptyCount < 10) {
                try {
                    Thread.sleep(RandomUtils.nextInt(freq));
                } catch (InterruptedException e) {
					logger1.error(e.getMessage(), e);
                }

                try {
                    Events<Event> entrys = eventStore.get(first, batchSize, 1000L, TimeUnit.MILLISECONDS);
                    // Events<Event> entrys = eventStore.tryGet(first,
                    // batchSize);
                    if (!CollectionUtils.isEmpty(entrys.getEvents())) {
                        if (entrys.getEvents().size() != batchSize) {
                            logger1.info(new StringBuilder().append("get size:").append(entrys.getEvents().size()).append(" with not full batchSize:").append(batchSize).toString());
                        }

                        first = entrys.getPositionRange().getEnd();
                        entrys.getEvents().forEach(event -> this.result.add(event.getPosition()));
                        emptyCount = 0;

                        logger1.info(new StringBuilder().append("offest : ").append(entrys.getEvents().get(0).getPosition()).append(" , count :").append(entrys.getEvents().size()).toString());
                        ackCount++;
                        if (ackCount == 1) {
                            eventStore.cleanUntil(entrys.getPositionRange().getEnd());
                            logger1.info("first position : " + eventStore.getFirstPosition());
                            ackCount = 0;
                        }
                    } else {
                        emptyCount++;
                        logger1.info("empty events for " + emptyCount);
                    }
                } catch (Exception e) {
                    logger1.error(e.getMessage(), e);
                }
            }

            latch.countDown();
        }

        public List<Long> getResult() {
            return result;
        }

    }
}
