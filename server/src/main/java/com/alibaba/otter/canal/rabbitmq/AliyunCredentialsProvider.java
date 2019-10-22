/**
 * aliyun amqp协议 账号类
 * 暂不支持STS授权情况
 */
package com.alibaba.otter.canal.rabbitmq;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import com.alibaba.mq.amqp.utils.UserUtils;
import com.rabbitmq.client.impl.CredentialsProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AliyunCredentialsProvider implements CredentialsProvider {

    private static final Logger logger = LoggerFactory.getLogger(AliyunCredentialsProvider.class);

	/**
     * Access Key ID
     */
    private final String aliyunAccessKey;

    /**
     * Access Key Secret
     */
    private final String aliyunAccessSecret;

    /**
     * 资源主账号ID
     */
    private final long   resourceOwnerId;

    public AliyunCredentialsProvider(final String accessKey, final String accessSecret, final long resourceOwnerId){
        this.aliyunAccessKey = accessKey;
        this.aliyunAccessSecret = accessSecret;
        this.resourceOwnerId = resourceOwnerId;
    }

    @Override
    public String getUsername() {
        return UserUtils.getUserName(aliyunAccessKey, resourceOwnerId);
    }

    @Override
    public String getPassword() {
        try {
            return UserUtils.getPassord(aliyunAccessSecret);
        } catch (InvalidKeyException | NoSuchAlgorithmException ignored) {
			logger.error(ignored.getMessage(), ignored);
        }
        return null;
    }

}
