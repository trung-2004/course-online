package com.example.course_service.mapper;

import com.example.course_service.dto.response.item_online.ItemOnlineDTO;
import com.example.course_service.dto.response.question_item.QuestionItemDTO;
import com.example.course_service.dto.response.item_online.ItemOnlineDetail;
import com.example.course_service.entity.ItemOnline;
import com.example.course_service.entity.ItemOnlineUser;
import com.example.course_service.exception.AppException;
import com.example.course_service.exception.ErrorCode;
import com.example.course_service.repository.ItemOnlineUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class ItemOnlineMapper {
    private final QuestionItemMapper questionItemMapper;
    private final ItemOnlineUserRepository itemOnlineUserRepository;
    public ItemOnlineDTO toItemOnlineDTO(ItemOnline model){
        if (model == null) {
            return null;
        }

        ItemOnlineDTO itemOnlineDTO = ItemOnlineDTO.builder()
                .id(model.getId())
                .title(model.getTitle())
                .content(model.getContent())
                .itemType(model.getItemType())
                .orderTop(model.getOrderTop())
                .pathUrl(model.getPathUrl())
                .createdBy(model.getCreatedBy())
                .createdDate(model.getCreatedDate())
                .modifiedBy(model.getModifiedBy())
                .modifiedDate(model.getModifiedDate())
                .build();
        return itemOnlineDTO;
    }

    public ItemOnlineDetail toItemOnlineStudentDetail(ItemOnline model, String userId){
        if (model == null) throw new AppException(ErrorCode.COURSE_NOTFOUND);

        List<QuestionItemDTO> questionItemOnlineDTOList = model.getQuestionItems().stream().map(questionItemMapper::toQuestionItemDTO).collect(Collectors.toList());

        questionItemOnlineDTOList.sort(Comparator.comparingInt(QuestionItemDTO::getOrderTop));

        ItemOnlineUser itemOnlineUser = itemOnlineUserRepository.findByItemOnlineAndUserId(model, userId);

        ItemOnlineDetail itemOnlineDetail = ItemOnlineDetail.builder()
                .id(model.getId())
                .title(model.getTitle())
                .content(model.getContent())
                .itemType(model.getItemType())
                .orderTop(model.getOrderTop())
                .pathUrl(model.getPathUrl())
                .questionItemDTOList(questionItemOnlineDTOList)
                .createdBy(model.getCreatedBy())
                .createdDate(model.getCreatedDate())
                .modifiedBy(model.getModifiedBy())
                .modifiedDate(model.getModifiedDate())
                .build();

        if (itemOnlineUser != null){
            itemOnlineDetail.setStatus(itemOnlineUser.isStatus());
        }

        return itemOnlineDetail;
    }
}
