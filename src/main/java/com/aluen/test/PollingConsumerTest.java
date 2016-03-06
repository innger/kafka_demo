package com.aluen.test;

public class PollingConsumerTest {

    public static void main(String[] args) {
        new MessageConsumer().consumeMessage(new Handler(), "POLLING");
    }

    static class Handler implements MessageHandler {
        public void handle(String token, String msg) {
            System.out.println(token + " ," + msg);
        }
    }
}
