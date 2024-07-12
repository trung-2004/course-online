package com.example.course_service.repository;

import com.example.course_service.entity.Course;
import com.example.course_service.projection.CourseProjection;
import com.example.course_service.projection.CourseProjectionDTO;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

@Repository
public interface CourseRepository extends JpaRepository<Course, String>, JpaSpecificationExecutor<Course> {
    List<Course> findAllByIdIn(List<String> courseIds);

    @Query("SELECT c FROM Course c LEFT JOIN FETCH c.topics WHERE c.id = :id")
    Optional<Course> findByIdWithTopics(@Param("id") String id);

    Optional<Course> findByIdAndStatus(String s, Integer i);

    @Query(value = "SELECT * FROM Course WHERE category_id = :categoryId LIMIT 4 OFFSET 0", nativeQuery = true)
    List<Course> findProductsByCategoryId(@Param("categoryId") String categoryId);

    @Query(value = "SELECT c FROM Course c WHERE c.status = 1 ORDER BY c.id ASC LIMIT :limit OFFSET :offset")
    List<Course> findCoursesWithPagination(@Param("limit") int limit, @Param("offset") int offset);

    @Query("SELECT MAX(c.modifiedDate) FROM Course c")
    Timestamp findMaxUpdatedAt();

    @Query(value = "SELECT * FROM Course where modifiedDate > :modifiedDate", nativeQuery = true)
    List<Course> findAllCourseUpdated(@Param("modifiedDate") Timestamp timestamp);

    @Procedure(procedureName = "GetCourseTop")
    List<Course> getCourseTop6(@Param("limit_course") int i);

//    @Query(value = "CALL GetCourseTop(:limit_course);", nativeQuery = true)
//    List<Course> findCarsAfterYear(@Param("limit_course") Integer limit_course);
    @Query("SELECT c.id FROM Course c WHERE c.categoryId = :categoryId AND c.id <> :currentCourseId")
    List<String> findTop4IdsByCategoryIdAndNotCurrentCourseId(
        @Param("categoryId") String categoryId,
        @Param("currentCourseId") String currentCourseId,
        Pageable pageable);

    // projection
    @Query(value = "select c.id as Id , c.name as Name , c.price as Price , p.name as CategoryName from Course c INNER JOIN Category p ON c.category_id = p.id", nativeQuery = true)
    List<CourseProjection> getCourseAndCategoryNameProjection();

    // projection DTO
    @Query(value = "select new com.example.course_service.projection.CourseProjectionDTO (c.id, c.name, c.price, p.name) from Course c INNER JOIN Category p ON c.categoryId = p.id")
    List<CourseProjectionDTO> getCourseAndCategoryNameProjectionDTO();
}
