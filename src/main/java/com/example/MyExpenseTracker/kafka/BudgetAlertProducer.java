package com.example.MyExpenseTracker.kafka;


import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import static org.springframework.kafka.support.KafkaHeaders.TOPIC;

@Service
public class BudgetAlertProducer {

    private static final String Topic = "Budget-alerts";


    private final KafkaTemplate<String, String> kafkaTemplate;

    public BudgetAlertProducer(KafkaTemplate<String, String> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendAlert(String message) {
        // Send asynchronously - don't wait for result
        kafkaTemplate.send(TOPIC, message)
                .whenComplete((result, ex) -> {
                    if (ex != null) {
                        System.out.println("Kafka unavailable: " + ex.getMessage());
                    } else {
                        System.out.println("Alert sent: " + message);
                    }
                });
    }
}
