package com.kafka.demo.test;

import com.kafka.demo.config.ConfigConst;
import com.kafka.demo.consumer.MessageConsumer;
import com.kafka.demo.consumer.MessageHandler;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

public class PollingConsumerTest extends BaseTestCase {

    private static Logger logger = LoggerFactory.getLogger(PollingConsumerTest.class);
    @Autowired
    private MessageConsumer messageConsumer;

    @Test
    public void consumerMessageTest() throws InterruptedException {
        messageConsumer.consumeMessage(new Handler(), ConfigConst.topic);

    }

    public static class Handler implements MessageHandler {
        public void handle(String token, String msg) {
            logger.info("Polling consumer message token={} msg={}", token, msg);
        }
    }
}
