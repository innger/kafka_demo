package com.kafka.demo.config;

import kafka.consumer.ConsumerConfig;
import kafka.producer.ProducerConfig;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.Properties;

@Component
@Qualifier("kafkaConfig")
public class KafkaConfig {

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
        props.put("zookeeper.connect", ConfigConst.zkConnect);
        props.put("group.id", ConfigConst.groupId);
        props.put("zookeeper.session.timeout.ms", "6000");
        props.put("zookeeper.sync.time.ms", "6000");
        props.put("auto.commit.interval.ms", "2000");
        this.consumerConfig = new ConsumerConfig(props);

        props = new Properties();
        props.put("metadata.broker.list", ConfigConst.brokerList);
        props.put("serializer.class", "kafka.serializer.StringEncoder");
        props.put("partitioner.class", "com.kafka.demo.config.SimplePartitioner");
        props.put("request.required.acks", "1");
        props.put("producer.type", ConfigConst.producerType);
        props.put("batch.num.messages", ConfigConst.batchNum);
        this.producerConfig = new ProducerConfig(props);
    }
}
