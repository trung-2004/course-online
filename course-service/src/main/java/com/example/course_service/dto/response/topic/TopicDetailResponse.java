package com.example.course_service.dto.response.topic;


import com.example.course_service.dto.response.item_online.ItemOnlineDetail;
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
public class TopicDetailResponse {
    private String id;
    private String name;
    private Integer orderTop;
    private List<ItemOnlineDetail> itemOnlineDetailList;
    private List<TestDTO> testOnlineResponseDTOList;
    private Timestamp createdDate;
    private Timestamp modifiedDate;
    private String createdBy;
    private String modifiedBy;
}
