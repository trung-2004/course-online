package com.example.course_service.repository;

import com.example.course_service.entity.ItemOnline;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ItemOnlineRepository extends JpaRepository<ItemOnline, String> {
}
