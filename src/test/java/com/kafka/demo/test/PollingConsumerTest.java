package com.kafka.demo.test;

import com.kafka.demo.config.ConfigConst;
import com.kafka.demo.consumer.MessageConsumer;
import com.kafka.demo.consumer.MessageHandler;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class PollingConsumerTest extends BaseTestCase {

    @Autowired
    private MessageConsumer messageConsumer;

    @Test
    public void consumerMessageTest() {
        messageConsumer.consumeMessage(new Handler(), ConfigConst.topic);
    }

    static class Handler implements MessageHandler {
        public void handle(String token, String msg) {
            System.out.println(token + " ," + msg);
        }
    }
}
