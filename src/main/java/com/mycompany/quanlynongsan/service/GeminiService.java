package com.mycompany.quanlynongsan.service;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class GeminiService {
    private static final String API_KEY = "AIzaSyDUwwNU_Jyxw5VQNVuAHLHvVeMF6KWDXRo"; // Thay bằng API Key thật
    private static final String ENDPOINT = "https://generativelanguage.googleapis.com/v1beta/models/gemini-2.0-flash:generateContent?key=" + API_KEY;

    public String chatWithGemini(String userMessage) {
    try {
        String safeMessage = userMessage.replace("\\", "\\\\").replace("\"", "\\\"");

        String jsonBody = String.format(""
                + "{\n" +
"              \"contents\": [\n" +
"                {\n" +
"                  \"parts\": [\n" +
"                    {\n" +
"                      \"text\": \"%s\"\n" +
"                    }\n" +
"                  ]\n" +
"                }\n" +
"              ]\n" +
"            }", safeMessage);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(ENDPOINT))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(jsonBody))
                .build();

        HttpClient client = HttpClient.newHttpClient();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        return response.body();

    } catch (Exception e) {
        e.printStackTrace();
        return "{\"error\":\"Đã xảy ra lỗi khi gửi yêu cầu.\"}";
    }
}

}
