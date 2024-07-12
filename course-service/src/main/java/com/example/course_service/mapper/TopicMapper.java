package com.example.course_service.mapper;

import com.example.course_service.dto.ApiResponse;
import com.example.course_service.dto.response.item_online.ItemOnlineDetail;
import com.example.course_service.dto.response.topic.TestDTO;
import com.example.course_service.dto.response.topic.TopicDTO;
import com.example.course_service.dto.response.topic.TopicDetailResponse;
import com.example.course_service.entity.ItemOnline;
import com.example.course_service.entity.Topic;
import com.example.course_service.exception.AppException;
import com.example.course_service.exception.ErrorCode;
import com.example.course_service.repository.httpclient.TestClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Component
@RequiredArgsConstructor
public class TopicMapper {
    private final ItemOnlineMapper itemOnlineMapper;
    private final TestClient testClient;
    public TopicDTO toTopicDTO(Topic topic) {
        if (topic == null) {
            return null;
        }

        TopicDTO dto = new TopicDTO();
        dto.setId(topic.getId());
        dto.setName(topic.getName());
        dto.setOrderTop(topic.getOrderTop());
        dto.setCourseId(topic.getCourse().getId());
        dto.setCreatedDate(topic.getCreatedDate());
        dto.setModifiedDate(topic.getModifiedDate());
        dto.setCreatedBy(topic.getCreatedBy());
        dto.setModifiedBy(topic.getModifiedBy());

        return dto;
    }

    public TopicDetailResponse toTopicOnlineAndStudentDetailResponse(Topic model, String userId){
        if (model == null) throw new AppException(ErrorCode.COURSE_NOTFOUND);

        List<ItemOnlineDetail> itemOnlineDTOSDetailList = new ArrayList<>();
        for (ItemOnline itemOnline: model.getItemOnlines()) {
            ItemOnlineDetail itemOnlineDetail = itemOnlineMapper.toItemOnlineStudentDetail(itemOnline, userId);
            itemOnlineDTOSDetailList.add(itemOnlineDetail);
        }

        itemOnlineDTOSDetailList.sort(Comparator.comparingInt(ItemOnlineDetail::getOrderTop));

        List<TestDTO> testDTOS = new ArrayList<>();
        ApiResponse<TestDTO> testDTOApiResponse = testClient.getTest(model.getId());
        testDTOS.add(testDTOApiResponse.getResult());
//        for (TestOnline testOnline: model.getTestOnlines()) {
//            boolean status;
//            TestOnlineStudent testOnlineStudent = testOnlineStudentRepository.findByTestOnlineAndStudentAndStatus(testOnline, student, true);
//            if (testOnlineStudent != null){
//                status = testOnlineStudent.isStatus();
//            }
//            status = false;
//            TestOnlineResponseDTO testOnlineResponseDTO = testOnlineMapper.toTestOnlineResponseDTO(testOnline, status);
//            testOnlineResponseDTOS.add(testOnlineResponseDTO);
//        }


        TopicDetailResponse topicOnlineDetail = TopicDetailResponse.builder()
                .id(model.getId())
                .name(model.getName())
                .orderTop(model.getOrderTop())
                .createdBy(model.getCreatedBy())
                .itemOnlineDetailList(itemOnlineDTOSDetailList)
                .testOnlineResponseDTOList(testDTOS)
                .createdDate(model.getCreatedDate())
                .modifiedBy(model.getModifiedBy())
                .modifiedDate(model.getModifiedDate())
                .build();
        return topicOnlineDetail;
    }
}
