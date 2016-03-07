package com.kafka.demo.test;

import com.kafka.demo.producer.TopicPollingService;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

public class PollingProduceTest extends BaseTestCase {

    @Autowired
    private TopicPollingService topicPollingService;

    @Test
    public void test() throws InterruptedException {
        String str = UUID.randomUUID().toString().replace("-","");
        topicPollingService.sendMessageByOneProducer(str,str);
        //producer 异步发送,直接结束会导致发送不成功
        TimeUnit.SECONDS.sleep(5);
    }
}
