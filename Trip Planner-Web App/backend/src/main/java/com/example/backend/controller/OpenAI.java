package com.example.backend.controller;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

@Component
public class OpenAI {
    @Value("${openai.api.url}")
    private String apiURL;
    @Value("${openai.api.key}")
    private String apiKey;
    @Value("${openai.model}")
    private String model;

    public String generateChatGPTResponse(String userPrompt) {
        try {
            URL url = new URL(apiURL);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Authorization", "Bearer " + apiKey);
            connection.setRequestProperty("Content-Type", "application/json");
            String requestBody = "{\"model\": \"" + model + "\", \"messages\": [{\"role\": \"user\", \"content\": \"" + userPrompt + "\"}]}";
            connection.setDoOutput(true);
            try (OutputStreamWriter writer = new OutputStreamWriter(connection.getOutputStream())) {
                writer.write(requestBody);
                writer.flush();
            }
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
                StringBuilder response = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }
                return getLLMResponse(response.toString());
            }
        } catch (IOException e) {
            throw new RuntimeException("Error interacting with the ChatGPT API: " + e.getMessage(), e);
        }
    }

    private static String getLLMResponse(String response) {
        try {
            JSONObject jsonResponse = new JSONObject(response);
            JSONArray choices = jsonResponse.getJSONArray("choices");
            JSONObject message = choices.getJSONObject(0).getJSONObject("message");
            return message.getString("content");
        } catch (JSONException e) {
            throw new IllegalArgumentException("Unable to extract JSON content from response: " + response, e);
        }
    }

}
