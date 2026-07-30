package com.example.MyExpenseTracker.kafka;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class BudgetAlertConsumer {

    @KafkaListener(topics = "budget-alerts", groupId = "budget-alert-group")
    public void consumeAlert(String message) {
        System.out.println("Budget Alert Received: " + message);
        // In real app → send email, push notification etc.
    }
}