package com.kafka.demo.config;

/**
 * Created by renyulong on 16/3/6.
 */
public interface ConfigConst {

    String brokerList = "10.218.145.191:9092,10.218.145.190:9092,10.218.145.189:9092";
    String zkConnect = "10.218.145.191:2181,10.218.145.190:2181,10.218.145.189:2181";
    String groupId = "pns_group_test";
    String producerType = "async";
    String batchNum = "1";

    String topic = "POLLING";
}
