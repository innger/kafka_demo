package com.aluen.test;

import kafka.javaapi.producer.Producer;
import kafka.producer.ProducerConfig;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.Properties;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

public class KafkaProduceTest extends BaseTestCase {
    String topic = "POLLING";
    @Autowired
    TopicPollingService topicPollingService;
    @Autowired
    TopicPollingService topicPollingService1;

    private String dev_brokerList = "10.218.145.191:9092,10.218.145.190:9092,10.218.145.189:9092";
    private Producer<String, String> producer;

    @Before
    public void initKafkaProducer() {
        Properties props = new Properties();
        props.put("metadata.broker.list", dev_brokerList);
        props.put("serializer.class", "kafka.serializer.StringEncoder");
        props.put("request.required.acks", "1");
        props.put("producer.type", "async");
        props.put("batch.num.messages", "200");
        ProducerConfig producerConfig = new ProducerConfig(props);
        producer = new Producer<String, String>(producerConfig);
    }

    // 单个生产者线程，多个kafka producer
    @Test
    public void testSeqMultiKakfaProducer() throws InterruptedException {
        for (int pollingNum = 1; pollingNum <= 10; pollingNum++) {
            System.out.println("pollingNum=" + pollingNum);
            final TopicPollingService tp = new TopicPollingService(pollingNum);
            for (int test = 1; test <= 10; test++) {
                long start1 = System.currentTimeMillis();
                for (int i = 0; i < 10; i++)
                    tp.sendMessage("2", "234");

                long end1 = System.currentTimeMillis();
                System.out.println(((end1 - start1)));
                System.out.println("==============");
            }
            TimeUnit.SECONDS.sleep(1);
        }
    }

    // 单个生产者线程，单个kafka producer
    @Test
    public void testSeqSingleProducer() throws InterruptedException {
        for (int test = 1; test <= 10; test++) {
            long start2 = System.currentTimeMillis();
            for (int i = 0; i < 100000; i++)
                topicPollingService.sendMessageByOneProducer("1", "aakak");

            long end2 = System.currentTimeMillis();
            System.out.println(((end2 - start2)));
            System.out.println("==============");
        }
        TimeUnit.MINUTES.sleep(1);
    }


    @Test
    public void parallelMultiKakfaProducerTest() throws InterruptedException {
        for (int pollingNum = 1; pollingNum <= 10; pollingNum++) {
            System.out.println("pollingNum=" + pollingNum);
            final TopicPollingService tp = new TopicPollingService(pollingNum);
            for (int test = 1; test <= 10; test++) {
                //==============
                long start1 = System.currentTimeMillis();
                System.out.println("start1:" + start1);
                final CountDownLatch latch = new CountDownLatch(10);
                ArrayList<Thread> workers = new ArrayList<>();
                for (int tid = 0; tid < 10; tid++) {
                    Thread worker = new Thread(new Runnable() {
                        @Override
                        public void run() {
                            for (int mid = 0; mid < 10000; mid++) {
                                tp.sendMessage("2", "234");
                            }
                            latch.countDown();
                        }
                    }, "multi-producer-worker" + tid);
                    workers.add(worker);
                    worker.start();
                }
                latch.await();
                for (Thread t : workers) t.join();

                long end1 = System.currentTimeMillis();
                System.out.println("end1:" + end1);
                System.out.println(((end1 - start1)));

                System.out.println("==============");
            }

        }
        TimeUnit.MINUTES.sleep(1);
    }

    @Test
    public void parallelSingleKakfaProducerTest() throws InterruptedException {

        long start2 = System.currentTimeMillis();
        System.out.println("start2="+start2);
        final CountDownLatch latch1 = new CountDownLatch(10);
        ArrayList<Thread> workers1 = new ArrayList<>();

        for (int tid = 0; tid < 10; tid++) {
            Thread worker = new Thread(new Runnable() {
                @Override
                public void run() {
                    for (int mid = 0; mid < 10; mid++) {
                        topicPollingService.sendMessageByOneProducer("1", "adfasdf");
                    }
                    latch1.countDown();
                }
            }, "single-producer-worker" + tid);
            workers1.add(worker);
            worker.start();
        }
        latch1.await();
        for (Thread t : workers1) t.join();

        long end2 = System.currentTimeMillis();
        System.out.println("end2="+end2);
        System.out.println(((end2 - start2)));

        System.out.println("==============");
//        TimeUnit.SECONDS.sleep(5);

    }


    @Test
    public void Test() throws InterruptedException {
        for (int pollingNum = 10; pollingNum <= 10; pollingNum++) {
            System.out.println("pollingNum=" + pollingNum);
            for (int test = 1; test <= 10; test++) {
                //==============
                long start1 = System.currentTimeMillis();
                System.out.println("start1:" + start1);
                final CountDownLatch latch = new CountDownLatch(10);
                ArrayList<Thread> workers = new ArrayList<>();
                for (int tid = 0; tid < 10; tid++) {
                    Thread worker = new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                TimeUnit.SECONDS.sleep(2);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                            latch.countDown();
                        }
                    }, "multi-producer-worker" + tid);
                    workers.add(worker);
                    worker.start();
                }
                latch.await();
                for (Thread t : workers) t.join();

                long end1 = System.currentTimeMillis();
                System.out.println("end1:" + end1);
                System.out.println(((end1 - start1)));

                System.out.println("==============");
                TimeUnit.SECONDS.sleep(5);
//                Thread.sleep(5000);
//                Thread.currentThread().sleep(5000);
            }

        }
        TimeUnit.MINUTES.sleep(1);
    }

}

