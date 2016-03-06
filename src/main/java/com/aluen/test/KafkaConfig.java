package com.aluen.test;

import kafka.consumer.ConsumerConfig;
import kafka.producer.ProducerConfig;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.Properties;

@Component
@Qualifier("kafkaConfig")
public class KafkaConfig {
    private String brokerList = "10.218.145.191:9092,10.218.145.190:9092,10.218.145.189:9092";
    private String zkConnect = "10.218.145.191:2181,10.218.145.190:2181,10.218.145.189:2181";
    private String groupId = "pns_group";
    private String producerType = "async";
    private String batchNum = "200";

    private ConsumerConfig consumerConfig;
    private ProducerConfig producerConfig;

    public ConsumerConfig consumerConfig() {
        return consumerConfig;
    }

    public ProducerConfig producerConfig() {
        return producerConfig;
    }

    public KafkaConfig() {
        Properties props = new Properties();
        props.put("zookeeper.connect", zkConnect);
        props.put("group.id", groupId);
        props.put("zookeeper.session.timeout.ms", "6000");
        props.put("zookeeper.sync.time.ms", "6000");
        props.put("auto.commit.interval.ms", "2000");
        this.consumerConfig = new ConsumerConfig(props);

        props = new Properties();
        props.put("metadata.broker.list", brokerList);
        props.put("serializer.class", "kafka.serializer.StringEncoder");
        props.put("partitioner.class", "com.aluen.test.SimplePartitioner");
        props.put("request.required.acks", "1");
        props.put("producer.type", producerType);
        props.put("batch.num.messages", batchNum);
        this.producerConfig = new ProducerConfig(props);
    }
}
