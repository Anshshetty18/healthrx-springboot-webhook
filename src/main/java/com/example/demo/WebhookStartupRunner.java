package com.example.demo;

import org.springframework.boot.CommandLineRunner;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class WebhookStartupRunner implements CommandLineRunner {

    @Override
    public void run(String... args) throws Exception {
        System.out.println("🚀 Application started... Sending POST request to generate webhook");

        String url = "https://bfhldevapigw.healthrx.co.in/hiring/generateWebhook/JAVA";

        // your details
        GenerateWebhookRequest requestBody = new GenerateWebhookRequest(
                "Ansh T Shetty",
                "PES2UG22CS080",
                "pes2ug22cs080@pesu.pes.edu"
        );

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<GenerateWebhookRequest> entity = new HttpEntity<>(requestBody, headers);

        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<GenerateWebhookResponse> response =
                restTemplate.exchange(url, HttpMethod.POST, entity, GenerateWebhookResponse.class);

        if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
            GenerateWebhookResponse resp = response.getBody();
            String webhookUrl = resp.getWebhook();
            String accessToken = resp.getAccessToken();

            System.out.println("✅ Webhook URL: " + webhookUrl);
            System.out.println("✅ Access Token: " + accessToken);

            // determine even/odd
            String regNo = requestBody.getRegNo();
            String numericPart = regNo.replaceAll("[^0-9]", "");
            int lastTwo = 0;
            if (numericPart.length() >= 2) {
                lastTwo = Integer.parseInt(numericPart.substring(numericPart.length() - 2));
            } else if (numericPart.length() == 1) {
                lastTwo = Integer.parseInt(numericPart);
            }
            boolean isEven = (lastTwo % 2 == 0);
            System.out.println("➡️ Last two digits: " + lastTwo + " (even? " + isEven + ")");

            // SQL for Question 2 (Even)
            String finalSQL =
                    "SELECT e1.EMP_ID, e1.FIRST_NAME, e1.LAST_NAME, d.DEPARTMENT_NAME, " +
                    "COUNT(e2.EMP_ID) AS YOUNGER_EMPLOYEES_COUNT " +
                    "FROM EMPLOYEE e1 " +
                    "JOIN DEPARTMENT d ON e1.DEPARTMENT = d.DEPARTMENT_ID " +
                    "LEFT JOIN EMPLOYEE e2 ON e1.DEPARTMENT = e2.DEPARTMENT AND e2.DOB > e1.DOB " +
                    "GROUP BY e1.EMP_ID, e1.FIRST_NAME, e1.LAST_NAME, d.DEPARTMENT_NAME " +
                    "ORDER BY e1.EMP_ID DESC;";

                            // ✅ Prepare and send final SQL to webhook
                RestTemplate submitTemplate = new RestTemplate();

                HttpHeaders authHeaders = new HttpHeaders();
                authHeaders.setContentType(MediaType.APPLICATION_JSON);
                authHeaders.set("Authorization", "Bearer " + accessToken);

                String jsonBody = "{\"finalQuery\": \"" + finalSQL.replace("\"", "\\\"") + "\"}";
                HttpEntity<String> requestEntity = new HttpEntity<>(jsonBody, authHeaders);

                System.out.println("📤 Sending final query to webhook...");
                System.out.println("➡️ Token: " + accessToken.substring(0, 20) + "..."); // show partial token

            try {
                    ResponseEntity<String> submitResponse = submitTemplate.exchange(
                            webhookUrl,
                            HttpMethod.POST,
                            requestEntity,
                            String.class
                    );

                    System.out.println("📤 Submission Status: " + submitResponse.getStatusCode());
                    System.out.println("📤 Submission Response: " + submitResponse.getBody());
                } catch (Exception e) {
                    System.err.println("❌ Submission failed: " + e.getMessage());
            }
        }
    }
}
