package com.example.course_service.service;

import com.example.course_service.dto.request.item_online.CreateItemOnline;
import com.example.course_service.dto.request.item_online.EditItemOnline;
import com.example.course_service.dto.response.item_online.ItemOnlineDTO;
import com.example.course_service.dto.response.item_online.ItemOnlineDetail;

public interface ItemOnlineService {
    ItemOnlineDTO create(CreateItemOnline model);
    ItemOnlineDTO edit(EditItemOnline model);
    void delete(String[] ids);
    ItemOnlineDTO findById(String id);
    ItemOnlineDetail getDetailById(String id, String userId);
    void complete(String id, String idUser);
}
