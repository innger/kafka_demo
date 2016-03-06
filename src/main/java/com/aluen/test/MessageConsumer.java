package com.aluen.test;

import kafka.consumer.ConsumerConfig;
import kafka.consumer.KafkaStream;
import kafka.message.MessageAndMetadata;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.charset.Charset;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MessageConsumer {

    private static final Logger logger = LoggerFactory.getLogger(MessageConsumer.class);
    private int numThread = 2;

    private final ExecutorService consumerExecutors;

    private ConsumerConfig consumerConfig;

    public MessageConsumer() {
        this.consumerConfig = consumerConfig();
        this.numThread = 2;
        this.consumerExecutors = Executors.newFixedThreadPool(numThread);
    }

    public void consumeMessage(MessageHandler handle, String topic) {
        Consumer consumer = new Consumer(consumerConfig);
        List<KafkaStream<byte[], byte[]>> streams = consumer.topicStreams(topic, numThread);
        for (final KafkaStream<byte[], byte[]> stream : streams) {
            consumerExecutors.submit(new Client(stream, handle));
        }
    }

    private ConsumerConfig consumerConfig() {
        Properties props = new Properties();
        props.put("zookeeper.connect", "10.218.145.191:2181,10.218.145.190:2181,10.218.145.189:2181");
        props.put("group.id", "pns_group");
        props.put("zookeeper.session.timeout.ms", "6000");
        props.put("zookeeper.sync.time.ms", "6000");
        props.put("auto.commit.interval.ms", "2000");
        return new ConsumerConfig(props);
    }

    public void destroy() throws Exception {
        consumerExecutors.shutdownNow();
    }

    private class Client implements Runnable {
        private KafkaStream<byte[], byte[]> stream;
        private MessageHandler messageHandle;

        public Client(KafkaStream<byte[], byte[]> stream, MessageHandler handle) {
            this.stream = stream;
            this.messageHandle = handle;
        }

        public void run() {
            for (final MessageAndMetadata<byte[], byte[]> msgAndMetadata : stream) {
                try {
                    String message = new String(msgAndMetadata.message(), Charset.forName("UTF-8"));
                    String token = new String(msgAndMetadata.key(), Charset.forName("UTF-8"));
                    messageHandle.handle(token, message);
                } catch (Exception e) {
                    logger.error("message handle error.", e);
                }
            }
        }
    }
}
