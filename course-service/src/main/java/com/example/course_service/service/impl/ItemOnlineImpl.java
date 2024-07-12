package com.example.course_service.service.impl;

import com.example.course_service.dto.ApiResponse;
import com.example.course_service.dto.request.item_online.CreateItemOnline;
import com.example.course_service.dto.request.item_online.EditItemOnline;
import com.example.course_service.dto.response.item_online.ItemOnlineDTO;
import com.example.course_service.dto.response.item_online.ItemOnlineDetail;
import com.example.course_service.entity.Course;
import com.example.course_service.entity.ItemOnline;
import com.example.course_service.entity.ItemOnlineUser;
import com.example.course_service.entity.Topic;
import com.example.course_service.exception.AppException;
import com.example.course_service.exception.ErrorCode;
import com.example.course_service.mapper.ItemOnlineMapper;
import com.example.course_service.repository.ItemOnlineRepository;
import com.example.course_service.repository.ItemOnlineUserRepository;
import com.example.course_service.repository.TopicRepository;
import com.example.course_service.repository.httpclient.PaymentClient;
import com.example.course_service.service.ItemOnlineService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ItemOnlineImpl implements ItemOnlineService {
    private final ItemOnlineMapper itemOnlineMapper;
    private final ItemOnlineRepository itemOnlineRepository;
    private final TopicRepository topicRepository;
    private final ItemOnlineUserRepository itemOnlineUserRepository;
    private final PaymentClient paymentClient;

    @Override
    public ItemOnlineDTO create(CreateItemOnline model) {
        Topic topic = topicRepository.findById(model.getTopicId())
                .orElseThrow(() -> new AppException(ErrorCode.COURSE_NOTFOUND));
        ItemOnline itemOnline = ItemOnline.builder()
                .title(model.getTitle())
                .content(model.getContent())
                .itemType(model.getItemType())
                .duration(model.getDuration())
                .pathUrl(model.getPathUrl())
                .orderTop(model.getOrderTop())
                .topic(topic)
                .createdBy("Demo")
                .modifiedBy("Demo")
                .createdDate(new Timestamp(System.currentTimeMillis()))
                .modifiedDate(new Timestamp(System.currentTimeMillis()))
                .build();
        return itemOnlineMapper.toItemOnlineDTO(itemOnlineRepository.save(itemOnline));
    }

    @Override
    public ItemOnlineDTO edit(EditItemOnline model) {
        ItemOnline itemOnline = itemOnlineRepository.findById(model.getId())
                .orElseThrow(() -> new AppException(ErrorCode.ITEMONLINE_NOTFOUND));
        Topic topic = topicRepository.findById(model.getTopicId())
                .orElseThrow(() -> new AppException(ErrorCode.COURSE_NOTFOUND));
        itemOnline.setTitle(model.getTitle());
        itemOnline.setContent(model.getContent());
        itemOnline.setItemType(model.getItemType());
        itemOnline.setDuration(model.getDuration());
        itemOnline.setPathUrl(model.getPathUrl());
        itemOnline.setOrderTop(model.getOrderTop());
        itemOnline.setTopic(topic);
        itemOnline.setModifiedBy("Demo");
        itemOnline.setModifiedDate(new Timestamp(System.currentTimeMillis()));

        return itemOnlineMapper.toItemOnlineDTO(itemOnlineRepository.save(itemOnline));
    }

    @Override
    public void delete(String[] ids) {
        itemOnlineRepository.deleteAllById(List.of(ids));
    }

    @Override
    public ItemOnlineDTO findById(String id) {
        ItemOnline itemOnline = itemOnlineRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.ITEMONLINE_NOTFOUND));
        return itemOnlineMapper.toItemOnlineDTO(itemOnline);
    }

    @Override
    public ItemOnlineDetail getDetailById(String id, String userId) {
        ItemOnline itemOnline = itemOnlineRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.ITEMONLINE_NOTFOUND));
        ItemOnlineUser itemOnlineUser = itemOnlineUserRepository.findByItemOnlineAndUserId(itemOnline, userId);
        if (itemOnlineUser == null) throw new AppException(ErrorCode.ITEMONLINE_NOTFOUND);
        // check buy course?
        ApiResponse<Boolean> checkBuy = paymentClient.checkBuyCourse(itemOnline.getTopic().getCourse().getId());
        if (!checkBuy.getResult()) throw new AppException(ErrorCode.COURSE_NOTFOUND);
        return itemOnlineMapper.toItemOnlineStudentDetail(itemOnline, userId);
    }

    @Override
    public void complete(String id, String idUser) {
        ItemOnline itemOnline = itemOnlineRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.ITEMONLINE_NOTFOUND));
        ItemOnlineUser itemOnlineUser = itemOnlineUserRepository.findByItemOnlineAndUserId(itemOnline, idUser);
        if (itemOnlineUser == null) throw new AppException(ErrorCode.ITEMONLINE_NOTFOUND);
        if (itemOnlineUser.isStatus()){

        } else {
            itemOnlineUser.setStatus(true);
            itemOnlineUserRepository.save(itemOnlineUser);
        }
    }
}
