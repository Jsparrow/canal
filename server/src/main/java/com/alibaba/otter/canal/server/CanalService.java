package com.alibaba.otter.canal.server;

import java.util.concurrent.TimeUnit;

import com.alibaba.otter.canal.protocol.ClientIdentity;
import com.alibaba.otter.canal.protocol.Message;
import com.alibaba.otter.canal.server.exception.CanalServerException;

public interface CanalService {

    void subscribe(ClientIdentity clientIdentity);

    void unsubscribe(ClientIdentity clientIdentity);

    Message get(ClientIdentity clientIdentity, int batchSize);

    Message get(ClientIdentity clientIdentity, int batchSize, Long timeout, TimeUnit unit);

    Message getWithoutAck(ClientIdentity clientIdentity, int batchSize);

    Message getWithoutAck(ClientIdentity clientIdentity, int batchSize, Long timeout, TimeUnit unit);

    void ack(ClientIdentity clientIdentity, long batchId);

    void rollback(ClientIdentity clientIdentity);

    void rollback(ClientIdentity clientIdentity, Long batchId);
}
