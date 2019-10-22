package com.alibaba.otter.canal.client.running;

import java.net.InetSocketAddress;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.commons.lang.math.RandomUtils;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import com.alibaba.otter.canal.client.impl.running.ClientRunningData;
import com.alibaba.otter.canal.client.impl.running.ClientRunningListener;
import com.alibaba.otter.canal.client.impl.running.ClientRunningMonitor;
import com.alibaba.otter.canal.common.utils.AddressUtils;
import com.alibaba.otter.canal.common.zookeeper.ZkClientx;
import com.alibaba.otter.canal.common.zookeeper.ZookeeperPathUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
@Ignore
public class ClientRunningTest extends AbstractZkTest {

    private static final Logger logger = LoggerFactory.getLogger(ClientRunningTest.class);
	private ZkClientx zkclientx = new ZkClientx(new StringBuilder().append(cluster1).append(";").append(cluster2).toString());
    private short     clientId  = 1001;

    @Before
    public void setUp() {
        String path = ZookeeperPathUtils.getDestinationPath(destination);
        zkclientx.deleteRecursive(path);

        zkclientx.createPersistent(ZookeeperPathUtils.getClientIdNodePath(this.destination, clientId), true);
    }

    @After
    public void tearDown() {
        String path = ZookeeperPathUtils.getDestinationPath(destination);
        zkclientx.deleteRecursive(path);
    }

    @Test
    public void testOneServer() {
        final CountDownLatch countLatch = new CountDownLatch(2);
        ClientRunningMonitor runningMonitor = buildClientRunning(countLatch, clientId, 2088);
        runningMonitor.start();
        sleep(2000L);
        runningMonitor.stop();
        sleep(2000L);

        if (countLatch.getCount() != 0) {
            Assert.fail();
        }
    }

    @Test
    public void testMultiServer() {
        final CountDownLatch countLatch = new CountDownLatch(30);
        final ClientRunningMonitor runningMonitor1 = buildClientRunning(countLatch, clientId, 2088);
        final ClientRunningMonitor runningMonitor2 = buildClientRunning(countLatch, clientId, 2089);
        final ClientRunningMonitor runningMonitor3 = buildClientRunning(countLatch, clientId, 2090);
        final ExecutorService executor = Executors.newFixedThreadPool(3);
        executor.submit(() -> {
		    for (int i = 0; i < 10; i++) {
		        if (!runningMonitor1.isStart()) {
		            runningMonitor1.start();
		        }
		        sleep(2000L + RandomUtils.nextInt(500));
		        runningMonitor1.stop();
		        sleep(2000L + RandomUtils.nextInt(500));
		    }
		});

        executor.submit(() -> {
		    for (int i = 0; i < 10; i++) {
		        if (!runningMonitor2.isStart()) {
		            runningMonitor2.start();
		        }
		        sleep(2000L + RandomUtils.nextInt(500));
		        runningMonitor2.stop();
		        sleep(2000L + RandomUtils.nextInt(500));
		    }
		});

        executor.submit(() -> {
		    for (int i = 0; i < 10; i++) {
		        if (!runningMonitor3.isStart()) {
		            runningMonitor3.start();
		        }
		        sleep(2000L + RandomUtils.nextInt(500));
		        runningMonitor3.stop();
		        sleep(2000L + RandomUtils.nextInt(500));
		    }
		});

        sleep(30000L);
    }

    private ClientRunningMonitor buildClientRunning(final CountDownLatch countLatch, final short clientId,
                                                    final int port) {
        ClientRunningData clientData = new ClientRunningData();
        clientData.setClientId(clientId);
        clientData.setAddress(AddressUtils.getHostIp());

        ClientRunningMonitor runningMonitor = new ClientRunningMonitor();
        runningMonitor.setDestination(destination);
        runningMonitor.setZkClient(zkclientx);
        runningMonitor.setClientData(clientData);
        runningMonitor.setListener(new ClientRunningListener() {

            @Override
			public InetSocketAddress processActiveEnter() {
                logger.info(String.format("clientId:%s port:%s has start", clientId, port));
                countLatch.countDown();
                return new InetSocketAddress(AddressUtils.getHostIp(), port);
            }

            @Override
			public void processActiveExit() {
                countLatch.countDown();
                logger.info(String.format("clientId:%s port:%s has stop", clientId, port));
            }

        });
        runningMonitor.setDelayTime(1);
        return runningMonitor;
    }
}
