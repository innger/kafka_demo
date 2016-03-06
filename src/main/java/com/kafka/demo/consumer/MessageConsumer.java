package com.kafka.demo.consumer;

import com.kafka.demo.config.KafkaConfig;
import kafka.consumer.KafkaStream;
import kafka.message.MessageAndMetadata;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.nio.charset.Charset;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Service
public class MessageConsumer {

    private static final Logger logger = LoggerFactory.getLogger(MessageConsumer.class);

    private int numThread = 2;

    private ExecutorService consumerExecutors = Executors.newFixedThreadPool(numThread);

    @Autowired
    private KafkaConfig kafkaConfig;

    public MessageConsumer() {
    }

    public void consumeMessage(MessageHandler handle, String topic) {
        Consumer consumer = new Consumer(kafkaConfig.consumerConfig());
        List<KafkaStream<byte[], byte[]>> streams = consumer.topicStreams(topic, numThread);
        for (final KafkaStream<byte[], byte[]> stream : streams) {
            consumerExecutors.submit(new Client(stream, handle));
        }
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