package com.aluen.test;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class PollingProduceTest extends BaseTestCase {
    String topic = "POLLING";
    @Autowired
    TopicPollingService topicPollingService;

    @Test
    public void tst() throws InterruptedException {
        topicPollingService.sendMessage("1","234");
    }
}
