package com.devteria.identity.service;

import com.devteria.identity.dto.request.DemoRequest;
import com.devteria.identity.repository.httpclient.EmailServiceClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

@Service
public class DemoService {
    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    private WebClient.Builder webClientBuilder;
    @Autowired
    private EmailServiceClient emailServiceClient;
    public String postRest(DemoRequest request) {
        String url = "http://localhost:8083/email/mail/sent";
        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.add("name", request.getName());
        // Send the request and get the response
        String response = restTemplate.postForObject(url, map, String.class);

        return request.getName() + " " + response;
    }

    public String getRest(String request) {
        String url = "http://localhost:8083/email/mail/sent";
        String response = restTemplate.getForObject(url, String.class);
        return request+response;
    }

    public String postWeb(DemoRequest request) {
        String url = "http://localhost:8083/email/mail/sent";

        // Gửi yêu cầu POST và nhận phản hồi
        String response = webClientBuilder.build()
                .post()
                .uri(url)
                .body(BodyInserters.fromFormData("name", request.getName()))
                .retrieve()
                .bodyToMono(String.class)
                .block();

        return request.getName() + " " + response;
    }

    public String getWeb(String request) {
        String url = "http://localhost:8083/email/mail/sent";

        // Gửi yêu cầu POST và nhận phản hồi
        String response = webClientBuilder.build()
                .get()
                .uri(url)
                .retrieve()
                .bodyToMono(String.class)
                .block();

        return request + " " + response;
    }

    public String getfeign(String request) {
        String response = emailServiceClient.getSentMail();
        return request + " " + response;
    }

    public String postfeign(DemoRequest request) {
        String response = emailServiceClient.postSentMail(request.getName());
        return request.getName() + " " + response;
    }
}
