package com.kafka.demo.producer;

import com.kafka.demo.config.ConfigConst;
import com.kafka.demo.config.KafkaConfig;
import kafka.javaapi.producer.Producer;
import kafka.producer.KeyedMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
public class BaseProducer {

    private static final Logger logger = LoggerFactory.getLogger(BaseProducer.class);

    private String topic = ConfigConst.topic;

    protected Producer<String, String> producer;

    @Qualifier("kafkaConfig")
    @Autowired
    private KafkaConfig kafkaConfig;

    public BaseProducer() {
    }

    public BaseProducer(String topic, KafkaConfig kafkaConfig) {
        this.topic = topic;
        this.kafkaConfig = kafkaConfig;
        this.producer = new Producer<String, String>(kafkaConfig.producerConfig());
    }

    @PostConstruct
    public void initProducer() {
        this.producer = new Producer<String, String>(kafkaConfig.producerConfig());
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public void send(String message) {
        KeyedMessage<String, String> data = new KeyedMessage<String, String>(topic, message);
        logger.debug("Producer send topic :{} , message :{} ", topic, message);
        this.producer.send(data);
    }

    public <T> void send(String partKey, String message) {
        logger.info("Producer send topic :{}, partKey: {} , message :{} ", topic, partKey, message);
        KeyedMessage<String, String> data = new KeyedMessage<String, String>(topic, partKey, message);
        this.producer.send(data);
    }


    public void shutdown() {
        this.producer.close();
    }

}

