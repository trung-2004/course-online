package com.example.email_service.controller;

import com.example.email_service.dto.request.MessageDTO;
import com.example.email_service.service.EmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class SentMailController {
    private final EmailService emailService;
    @GetMapping("/mail/sent")
    public String sentMail(){
        return " ok";
    }

    @PostMapping("/mail/sent")
    public String sentMail2(@RequestParam("name") String name){
        return "ok "+name;
    }

    @PostMapping("/mail/sent/test")
    public String sent(@RequestBody MessageDTO messageDTO) {
        emailService.sendEmail(messageDTO);
        return "ok";
    }

}
