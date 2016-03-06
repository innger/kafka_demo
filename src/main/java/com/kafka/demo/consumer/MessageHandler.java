package com.kafka.demo.consumer;

public interface MessageHandler {
    void handle(String token, String msg);
}
