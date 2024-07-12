package com.example.course_service.repository;

import com.example.course_service.entity.ItemOnline;
import com.example.course_service.entity.ItemOnlineUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ItemOnlineUserRepository extends JpaRepository<ItemOnlineUser, String> {
    ItemOnlineUser findByItemOnlineAndUserId(ItemOnline itemOnline, String id);
}
