package com.aluen.test;

import kafka.producer.Partitioner;
import kafka.utils.VerifiableProperties;

import java.util.Random;

public class SimplePartitioner implements Partitioner {
    public SimplePartitioner(VerifiableProperties props) {
    }

    @Override
    public int partition(Object key, int numPartitions) {
        int partition = 0;
        if (key != null) {
            partition = Math.abs(key.hashCode()) % numPartitions;
        } else {
            Random random = new Random();
            partition = random.nextInt(numPartitions);
        }
        return partition;
    }
}