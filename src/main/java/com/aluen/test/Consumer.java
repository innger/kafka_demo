package com.aluen.test;

import kafka.consumer.ConsumerConfig;
import kafka.consumer.KafkaStream;
import kafka.javaapi.consumer.ConsumerConnector;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Consumer {
    private final ConsumerConnector consumer;

    public Consumer(ConsumerConfig zkconfig) {
        this.consumer = kafka.consumer.Consumer
                .createJavaConsumerConnector(zkconfig);
    }

    public List<KafkaStream<byte[], byte[]>> topicStreams(final String topic,final int numThreads) {
        Map<String, Integer> topicCountMap = new HashMap<String, Integer>();
        topicCountMap.put(topic, numThreads);
        Map<String, List<KafkaStream<byte[], byte[]>>> consumerMap = consumer
                .createMessageStreams(topicCountMap);
        return consumerMap.get(topic);
    }
}

