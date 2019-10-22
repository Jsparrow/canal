package com.alibaba.otter.canal.deployer;

import java.text.MessageFormat;

/**
 * 启动常用变量
 *
 * @author jianghang 2012-11-8 下午03:15:55
 * @version 1.0.0
 */
public class CanalConstants {

    public static final String MDC_DESTINATION                      = "destination";
    public static final String ROOT                                 = "canal";
    public static final String CANAL_ID                             = new StringBuilder().append(ROOT).append(".").append("id").toString();
    public static final String CANAL_IP                             = new StringBuilder().append(ROOT).append(".").append("ip").toString();
    public static final String CANAL_REGISTER_IP                    = new StringBuilder().append(ROOT).append(".").append("register.ip").toString();
    public static final String CANAL_PORT                           = new StringBuilder().append(ROOT).append(".").append("port").toString();
    public static final String CANAL_USER                           = new StringBuilder().append(ROOT).append(".").append("user").toString();
    public static final String CANAL_PASSWD                         = new StringBuilder().append(ROOT).append(".").append("passwd").toString();
    public static final String CANAL_METRICS_PULL_PORT              = new StringBuilder().append(ROOT).append(".").append("metrics.pull.port").toString();
    public static final String CANAL_ADMIN_MANAGER                  = new StringBuilder().append(ROOT).append(".").append("admin.manager").toString();
    public static final String CANAL_ADMIN_PORT                     = new StringBuilder().append(ROOT).append(".").append("admin.port").toString();
    public static final String CANAL_ADMIN_USER                     = new StringBuilder().append(ROOT).append(".").append("admin.user").toString();
    public static final String CANAL_ADMIN_PASSWD                   = new StringBuilder().append(ROOT).append(".").append("admin.passwd").toString();
    public static final String CANAL_ADMIN_AUTO_REGISTER            = new StringBuilder().append(ROOT).append(".").append("admin.register.auto").toString();
    public static final String CANAL_ADMIN_AUTO_CLUSTER             = new StringBuilder().append(ROOT).append(".").append("admin.register.cluster").toString();
    public static final String CANAL_ZKSERVERS                      = new StringBuilder().append(ROOT).append(".").append("zkServers").toString();
    public static final String CANAL_WITHOUT_NETTY                  = new StringBuilder().append(ROOT).append(".").append("withoutNetty").toString();

    public static final String CANAL_DESTINATIONS                   = new StringBuilder().append(ROOT).append(".").append("destinations").toString();
    public static final String CANAL_AUTO_SCAN                      = new StringBuilder().append(ROOT).append(".").append("auto.scan").toString();
    public static final String CANAL_AUTO_SCAN_INTERVAL             = new StringBuilder().append(ROOT).append(".").append("auto.scan.interval").toString();
    public static final String CANAL_CONF_DIR                       = new StringBuilder().append(ROOT).append(".").append("conf.dir").toString();
    public static final String CANAL_SERVER_MODE                    = new StringBuilder().append(ROOT).append(".").append("serverMode").toString();

    public static final String CANAL_DESTINATION_SPLIT              = ",";
    public static final String GLOBAL_NAME                          = "global";

    public static final String INSTANCE_MODE_TEMPLATE               = new StringBuilder().append(ROOT).append(".").append("instance.{0}.mode").toString();
    public static final String INSTANCE_LAZY_TEMPLATE               = new StringBuilder().append(ROOT).append(".").append("instance.{0}.lazy").toString();
    public static final String INSTANCE_MANAGER_ADDRESS_TEMPLATE    = new StringBuilder().append(ROOT).append(".").append("instance.{0}.manager.address").toString();
    public static final String INSTANCE_SPRING_XML_TEMPLATE         = new StringBuilder().append(ROOT).append(".").append("instance.{0}.spring.xml").toString();

    public static final String CANAL_DESTINATION_PROPERTY           = ROOT + ".instance.destination";

    public static final String CANAL_SOCKETCHANNEL                  = new StringBuilder().append(ROOT).append(".").append("socketChannel").toString();

    public static final String CANAL_MQ_SERVERS                     = new StringBuilder().append(ROOT).append(".").append("mq.servers").toString();
    public static final String CANAL_MQ_RETRIES                     = new StringBuilder().append(ROOT).append(".").append("mq.retries").toString();
    public static final String CANAL_MQ_BATCHSIZE                   = new StringBuilder().append(ROOT).append(".").append("mq.batchSize").toString();
    public static final String CANAL_MQ_LINGERMS                    = new StringBuilder().append(ROOT).append(".").append("mq.lingerMs").toString();
    public static final String CANAL_MQ_MAXREQUESTSIZE              = new StringBuilder().append(ROOT).append(".").append("mq.maxRequestSize").toString();
    public static final String CANAL_MQ_BUFFERMEMORY                = new StringBuilder().append(ROOT).append(".").append("mq.bufferMemory").toString();
    public static final String CANAL_MQ_CANALBATCHSIZE              = new StringBuilder().append(ROOT).append(".").append("mq.canalBatchSize").toString();
    public static final String CANAL_MQ_CANALGETTIMEOUT             = new StringBuilder().append(ROOT).append(".").append("mq.canalGetTimeout").toString();
    public static final String CANAL_MQ_FLATMESSAGE                 = new StringBuilder().append(ROOT).append(".").append("mq.flatMessage").toString();
    public static final String CANAL_MQ_PARALLELTHREADSIZE          = new StringBuilder().append(ROOT).append(".").append("mq.parallelThreadSize").toString();
    public static final String CANAL_MQ_COMPRESSION_TYPE            = new StringBuilder().append(ROOT).append(".").append("mq.compressionType").toString();
    public static final String CANAL_MQ_ACKS                        = new StringBuilder().append(ROOT).append(".").append("mq.acks").toString();
    public static final String CANAL_MQ_TRANSACTION                 = new StringBuilder().append(ROOT).append(".").append("mq.transaction").toString();
    public static final String CANAL_MQ_PRODUCERGROUP               = new StringBuilder().append(ROOT).append(".").append("mq.producerGroup").toString();
    public static final String CANAL_ALIYUN_ACCESSKEY               = new StringBuilder().append(ROOT).append(".").append("aliyun.accessKey").toString();
    public static final String CANAL_ALIYUN_SECRETKEY               = new StringBuilder().append(ROOT).append(".").append("aliyun.secretKey").toString();
    public static final String CANAL_MQ_PROPERTIES                  = new StringBuilder().append(ROOT).append(".").append("mq.properties").toString();
    public static final String CANAL_MQ_ENABLE_MESSAGE_TRACE        = new StringBuilder().append(ROOT).append(".").append("mq.enableMessageTrace").toString();
    public static final String CANAL_MQ_ACCESS_CHANNEL              = new StringBuilder().append(ROOT).append(".").append("mq.accessChannel").toString();
    public static final String CANAL_MQ_CUSTOMIZED_TRACE_TOPIC      = new StringBuilder().append(ROOT).append(".").append("mq.customizedTraceTopic").toString();
    public static final String CANAL_MQ_NAMESPACE                   = new StringBuilder().append(ROOT).append(".").append("mq.namespace").toString();
    public static final String CANAL_MQ_KAFKA_KERBEROS_ENABLE       = new StringBuilder().append(ROOT).append(".").append("mq.kafka.kerberos.enable").toString();
    public static final String CANAL_MQ_KAFKA_KERBEROS_KRB5FILEPATH = new StringBuilder().append(ROOT).append(".").append("mq.kafka.kerberos.krb5FilePath").toString();
    public static final String CANAL_MQ_KAFKA_KERBEROS_JAASFILEPATH = new StringBuilder().append(ROOT).append(".").append("mq.kafka.kerberos.jaasFilePath").toString();
    public static final String CANAL_MQ_USERNAME                    = new StringBuilder().append(ROOT).append(".").append("mq.username").toString();
    public static final String CANAL_MQ_PASSWORD                    = new StringBuilder().append(ROOT).append(".").append("mq.password").toString();
    public static final String CANAL_MQ_VHOST                       = new StringBuilder().append(ROOT).append(".").append("mq.vhost").toString();
    public static final String CANAL_MQ_ALIYUN_UID                  = new StringBuilder().append(ROOT).append(".").append("mq.aliyunuid").toString();
    public static final String CANAL_MQ_EXCHANGE                    = new StringBuilder().append(ROOT).append(".").append("mq.exchange").toString();
    public static final String CANAL_MQ_DATABASE_HASH               = new StringBuilder().append(ROOT).append(".").append("mq.database.hash").toString();

    public static String getInstanceModeKey(String destination) {
        return MessageFormat.format(INSTANCE_MODE_TEMPLATE, destination);
    }

    public static String getInstanceManagerAddressKey(String destination) {
        return MessageFormat.format(INSTANCE_MANAGER_ADDRESS_TEMPLATE, destination);
    }

    public static String getInstancSpringXmlKey(String destination) {
        return MessageFormat.format(INSTANCE_SPRING_XML_TEMPLATE, destination);
    }

    public static String getInstancLazyKey(String destination) {
        return MessageFormat.format(INSTANCE_LAZY_TEMPLATE, destination);
    }
}
