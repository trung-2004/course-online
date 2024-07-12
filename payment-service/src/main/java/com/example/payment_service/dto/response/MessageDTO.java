package com.example.payment_service.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class MessageDTO {
    private String to;
    private String toName;
    private String subject;
    private String content;
}
