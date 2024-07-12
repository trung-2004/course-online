package com.example.course_service.dto.response.item_online;

import com.example.course_service.dto.response.question_item.QuestionItemDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ItemOnlineDetail {
    private String id;
    private String title;
    private String courseSlug;
    private String content;
    private Integer itemType;
    private Integer orderTop;
    private String pathUrl;
    private boolean status;
    private List<QuestionItemDTO> questionItemDTOList;
    private Timestamp createdDate;
    private Timestamp modifiedDate;
    private String createdBy;
    private String modifiedBy;
}
