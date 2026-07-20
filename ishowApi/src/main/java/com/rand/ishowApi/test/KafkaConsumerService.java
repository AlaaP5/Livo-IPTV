package com.rand.ishowApi.test;


import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class KafkaConsumerService {

    @KafkaListener(topics = "test-topic", groupId = "${Kafka.groupId}")
    public void consume(Map<String, Object> payload, Acknowledgment ack) {
        System.out.println("Received Dynamic Payload: " + payload);
        System.out.println("User Name: " + payload.get("name"));

        // Manual acknowledgment because of your MANUAL_IMMEDIATE config
        ack.acknowledge();
    }
}
