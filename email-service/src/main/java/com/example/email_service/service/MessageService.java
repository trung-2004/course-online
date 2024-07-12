package com.example.email_service.service;

import com.example.email_service.dto.request.MessageDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class MessageService {
    private final EmailService emailService;

    @KafkaListener(id = "notificationGroup", topics = "notification")
    public void listen(MessageDTO messageDTO) {
        try {
            log.info("Received: " + messageDTO.getTo());
            emailService.sendEmail(messageDTO);
        } catch (Exception e) {
            log.error("Error processing itemOnlineUser: {}", messageDTO, e);
            // Optionally, you can rethrow the exception or handle it accordingly
            // throw e;
        }
    }
}
