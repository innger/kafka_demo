# kafka入门使用示例

- log很重要,之前没有加log4j,导致日志没打出来,producer其实发消息一直有问题,报java.nio.channels.ClosedChannelException异常
查了一下发现可能是kafka server.properties配置有问题[kafka集群配置](http://blog.csdn.net/itjavaer/article/details/46518591)
>这个错误是config/server.properties的host.name写的不对，可能是前面的“#”没有去掉或是写的主机名称，改成服务器ip地址就可以了，如果改成localhost单机模式不会有问题，但分布式的时候会报下面错误
- 在单元测试中,producer配置async,如果进程提前结束,可能会导致消息发送不成功,可以sleep一下
- consumer消费不到消息,是group.id配置不对,之前是改成pns_group_test可以正常,具体原因不明
- kafka常用命令

```
sh kafka-topics.sh --list --zookeeper 10.218.145.191:2181,10.218.145.190:2181,10.218.145.189:2181

sh kafka-console-consumer.sh --zookeeper 10.218.145.191:2181,10.218.145.190:2181,10.218.145.189:2181 --from-beginning --topic POLLING

sh kafka-console-producer.sh --topic POLLING --broker-list 10.218.145.191:9092,10.218.145.190:9092,10.218.145.189:9092
```
