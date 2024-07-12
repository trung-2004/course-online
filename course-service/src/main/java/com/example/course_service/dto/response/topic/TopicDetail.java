package com.example.course_service.dto.response.topic;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TopicDetail {
    private Long id;
    private String name;
    private Integer orderTop;

    //private List<ItemOnlineDTO> itemOnlineDTOList;

    //private List<TestOnlineDTO> testOnlineDTOList;

    private Timestamp createdDate;

    private Timestamp modifiedDate;

    private String createdBy;

    private String modifiedBy;
}
