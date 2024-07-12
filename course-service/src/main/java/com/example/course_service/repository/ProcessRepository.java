package com.example.course_service.repository;

import com.example.course_service.entity.Process;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;

@Repository
public interface ProcessRepository extends JpaRepository<Process, Long> {
    @Query(value = "SELECT last_updated_at FROM process WHERE name = :name", nativeQuery = true)
    Timestamp getLastUpdatedAt(@Param("name") String name);

    @Modifying
    @Transactional
    @Query(value = "UPDATE process p SET p.last_updated_at = :lastUpdatedAt WHERE p.name = :name", nativeQuery = true)
    void updateLastUpdatedAt(@Param("lastUpdatedAt") Timestamp lastUpdatedAt, @Param("name") String name);
}
