package com.kafka.demo.test;

import com.kafka.demo.producer.TopicPollingService;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class PollingProduceTest extends BaseTestCase {

    @Autowired
    private TopicPollingService topicPollingService;

    @Test
    public void test() throws InterruptedException {
        topicPollingService.sendMessage("1","234");
    }
}
