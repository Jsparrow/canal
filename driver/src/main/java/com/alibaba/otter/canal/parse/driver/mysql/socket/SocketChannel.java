package com.alibaba.otter.canal.parse.driver.mysql.socket;

import java.io.IOException;
import java.net.SocketAddress;

/**
 * @author agapple 2018年3月12日 下午10:36:44
 * @since 1.0.26
 */
public interface SocketChannel {

    void write(byte[]... buf) throws IOException;

    byte[] read(int readSize) throws IOException;

    byte[] read(int readSize, int timeout) throws IOException;

    void read(byte[] data, int off, int len, int timeout) throws IOException;

    boolean isConnected();

    SocketAddress getRemoteSocketAddress();

    SocketAddress getLocalSocketAddress();

    void close();
}
