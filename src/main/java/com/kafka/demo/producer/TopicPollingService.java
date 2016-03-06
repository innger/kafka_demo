package com.kafka.demo.producer;

import com.kafka.demo.config.ConfigConst;
import com.kafka.demo.config.KafkaConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Random;

@Service
@Scope("prototype")
public class TopicPollingService {

    private static final Logger logger = LoggerFactory.getLogger(TopicPollingService.class);

    private static Random random = new Random();

    private String pollingTopic = ConfigConst.topic;

    //注解producer spring管理
    @Autowired
    private BaseProducer producer;

    private int pollingNum = 2;

    //多个producer 非spring管理
    private BaseProducer[] producers;


    public void init() {
        producers = new BaseProducer[pollingNum];
        KafkaConfig kafkaConfig = new KafkaConfig();
        for (int i = 0; i < pollingNum; i++) {
            //非spring管理,手动new bean
            BaseProducer p = new BaseProducer(pollingTopic, kafkaConfig);
            producers[i] = p;
        }
    }


    public TopicPollingService() {
        init();
    }

    public TopicPollingService(int pollingNum) {
        this.pollingNum = pollingNum;
        init();
    }

    public void sendMessage(String token, String message) {
        try {
            if (!StringUtils.isEmpty(token)) {
                int index = random.nextInt(pollingNum);
                BaseProducer p = producers[index];
                p.send(token, message);
            } else {
                logger.info("can not push this message to kafka for token is null or blank.");
            }
        } catch (Exception e) {
            logger.error("Produce message to topic of polling caught an exception", e);
        }
    }

    public void sendMessageByOneProducer(String token, String message) {
        try {
            if (!StringUtils.isEmpty(token)) {
                producer.send(token, message);
            } else {
                logger.info("can not push this message to kafka for token is null or blank.");
            }
        } catch (Exception e) {
            logger.error("Produce message to topic of polling caught an exception", e);
        }
    }

}
