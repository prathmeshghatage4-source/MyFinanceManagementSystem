package com.example.MyExpenseTracker.service;

import com.example.MyExpenseTracker.entity.MyExpense;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;
import java.util.Map;

@Service
public class GeminiService {

    @Value("${gemini.api.key}")
    private String apiKey;

    private final ObjectMapper objectMapper = new ObjectMapper();
    private final HttpClient httpClient = HttpClient.newHttpClient();

    public String generateSpendingSummary(List<MyExpense> expenses) throws Exception {

        // Build expense summary text for Gemini
        StringBuilder expenseText = new StringBuilder();
        expenseText.append("Here are my recent expenses:\n\n");

        double total = 0;
        for (MyExpense e : expenses) {
            expenseText.append(String.format(
                    "- %s | Category: %s | Amount: ₹%.2f | Date: %s | Payment: %s\n",
                    e.getTitle(), e.getCategory(),
                    e.getAmount().doubleValue(),
                    e.getDate(), e.getPaymentMethod()
            ));
            total += e.getAmount().doubleValue();
        }

        expenseText.append(String.format("\nTotal spending: ₹%.2f\n", total));
        expenseText.append("Budget limit: ₹10,000\n");

        // Prompt for Gemini
        String prompt = expenseText + """
            
            Please analyze these expenses and provide:
            1. A brief spending summary (2-3 sentences)
            2. Top 2 spending categories
            3. One practical money-saving tip
            4. Budget status (under/over budget)
            
            Keep the response concise, friendly, and in plain text.
            No markdown, no bullet symbols, just clean readable text.
            """;

        // Build request body
        Map<String, Object> requestBody = Map.of(
                "contents", List.of(
                        Map.of("parts", List.of(
                                Map.of("text", prompt)
                        ))
                )
        );

        String requestJson = objectMapper.writeValueAsString(requestBody);

        // Call Gemini API
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(
                        "https://generativelanguage.googleapis.com/v1beta/models/gemini-2.0-flash:generateContent?key=" + apiKey
                ))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(requestJson))
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() != 200) {
            throw new RuntimeException("Gemini API error: " + response.body());
        }

        // Parse response
        Map<String, Object> responseMap = objectMapper.readValue(response.body(), Map.class);
        List candidates = (List) responseMap.get("candidates");
        Map candidate = (Map) candidates.get(0);
        Map content = (Map) candidate.get("content");
        List parts = (List) content.get("parts");
        Map part = (Map) parts.get(0);

        return (String) part.get("text");
    }
}