package com.alibaba.otter.canal.parse.driver.mysql.socket;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.nio.channels.ClosedByInterruptException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 使用BIO进行dump
 *
 * @author chuanyi
 */
public class BioSocketChannel implements SocketChannel {

    private static final Logger logger = LoggerFactory.getLogger(BioSocketChannel.class);
	static final int     DEFAULT_CONNECT_TIMEOUT = 10 * 1000;
    static final int     SO_TIMEOUT              = 1000;
    private Socket       socket;
    private InputStream  input;
    private OutputStream output;

    BioSocketChannel(Socket socket) throws IOException{
        this.socket = socket;
        this.input = new BufferedInputStream(socket.getInputStream(), 16384);
        this.output = socket.getOutputStream();
    }

    @Override
	public void write(byte[]... buf) throws IOException {
        OutputStream output = this.output;
        if (output != null) {
            for (byte[] bs : buf) {
                output.write(bs);
            }
        } else {
            throw new SocketException("Socket already closed.");
        }
    }

    @Override
	public byte[] read(int readSize) throws IOException {
        InputStream input = this.input;
        byte[] data = new byte[readSize];
        int remain = readSize;
        if (input == null) {
            throw new SocketException("Socket already closed.");
        }
        while (remain > 0) {
            try {
                int read = input.read(data, readSize - remain, remain);
                if (read > -1) {
                    remain -= read;
                } else {
                    throw new IOException("EOF encountered.");
                }
            } catch (SocketTimeoutException te) {
                logger.error(te.getMessage(), te);
				if (Thread.interrupted()) {
                    throw new ClosedByInterruptException();
                }
            }
        }
        return data;
    }

    @Override
	public byte[] read(int readSize, int timeout) throws IOException {
        InputStream input = this.input;
        byte[] data = new byte[readSize];
        int remain = readSize;
        int accTimeout = 0;
        if (input == null) {
            throw new SocketException("Socket already closed.");
        }
        while (remain > 0 && accTimeout < timeout) {
            try {
                int read = input.read(data, readSize - remain, remain);
                if (read > -1) {
                    remain -= read;
                } else {
                    throw new IOException("EOF encountered.");
                }
            } catch (SocketTimeoutException te) {
                logger.error(te.getMessage(), te);
				if (Thread.interrupted()) {
                    throw new ClosedByInterruptException();
                }
                accTimeout += SO_TIMEOUT;
            }
        }
        if (remain > 0 && accTimeout >= timeout) {
            throw new SocketTimeoutException(new StringBuilder().append("Timeout occurred, failed to read total ").append(readSize).append(" bytes in ").append(timeout).append(" milliseconds, actual read only ")
					.append(readSize - remain).append(" bytes").toString());
        }
        return data;
    }

    @Override
    public void read(byte[] data, int off, int len, int timeout) throws IOException {
        InputStream input = this.input;
        int accTimeout = 0;
        if (input == null) {
            throw new SocketException("Socket already closed.");
        }

        int n = 0;
        while (n < len && accTimeout < timeout) {
            try {
                int read = input.read(data, off + n, len - n);
                if (read > -1) {
                    n += read;
                } else {
                    throw new IOException("EOF encountered.");
                }
            } catch (SocketTimeoutException te) {
                logger.error(te.getMessage(), te);
				if (Thread.interrupted()) {
                    throw new ClosedByInterruptException();
                }
                accTimeout += SO_TIMEOUT;
            }
        }

        if (n < len && accTimeout >= timeout) {
            throw new SocketTimeoutException(new StringBuilder().append("Timeout occurred, failed to read total ").append(len).append(" bytes in ").append(timeout).append(" milliseconds, actual read only ").append(n)
					.append(" bytes").toString());
        }
    }

    @Override
	public boolean isConnected() {
        Socket socket = this.socket;
        if (socket != null) {
            return socket.isConnected();
        }
        return false;
    }

    @Override
	public SocketAddress getRemoteSocketAddress() {
        Socket socket = this.socket;
        if (socket != null) {
            return socket.getRemoteSocketAddress();
        }

        return null;
    }

    @Override
	public SocketAddress getLocalSocketAddress() {
        Socket socket = this.socket;
        if (socket != null) {
            return socket.getLocalSocketAddress();
        }

        return null;
    }

    @Override
	public void close() {
        Socket socket = this.socket;
        if (socket != null) {
            try {
                socket.shutdownInput();
            } catch (IOException e) {
				logger.error(e.getMessage(), e);
                // Ignore, could not do anymore
            }
            try {
                socket.shutdownOutput();
            } catch (IOException e) {
				logger.error(e.getMessage(), e);
                // Ignore, could not do anymore
            }
            try {
                socket.close();
            } catch (IOException e) {
				logger.error(e.getMessage(), e);
                // Ignore, could not do anymore
            }
        }
        this.input = null;
        this.output = null;
        this.socket = null;
    }

}
