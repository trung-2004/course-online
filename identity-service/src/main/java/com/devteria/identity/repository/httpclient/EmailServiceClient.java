package com.devteria.identity.repository.httpclient;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "email-service", url = "http://localhost:8083")
public interface EmailServiceClient {
    @GetMapping("/email/mail/sent")
    String getSentMail();

    @PostMapping("/email/mail/sent")
    String postSentMail(@RequestParam("name") String name);
}
