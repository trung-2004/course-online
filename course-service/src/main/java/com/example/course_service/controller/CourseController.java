package com.example.course_service.controller;

import com.example.course_service.dto.ApiResponse;
import com.example.course_service.dto.request.course.CreateCourse;
import com.example.course_service.dto.request.course.EditCourse;
import com.example.course_service.dto.response.CustomUserDetails;
import com.example.course_service.dto.response.course.CourseDTO;
import com.example.course_service.dto.response.course.CourseDTOTopicId;
import com.example.course_service.dto.response.course.CourseDetail;
import com.example.course_service.dto.response.course.CourseResponse;
import com.example.course_service.entity.Course;
import com.example.course_service.projection.CourseProjection;
import com.example.course_service.projection.CourseProjectionDTO;
import com.example.course_service.service.CourseService;
import com.example.course_service.service.impl.RedisCacheService;
import com.example.course_service.util.CourseSpecification;
import com.example.course_service.util.JwtUtils;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
public class CourseController {
    private final CourseService courseService;
    private final RedisCacheService redisCacheService;
    private final JwtUtils jwtUtils;

    @GetMapping("/any/course-online/getall")
    public ResponseEntity<ApiResponse<Page<CourseDTO>>> getAll(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) Double minPrice,
            @RequestParam(required = false) Double maxPrice,
            @RequestParam(required = false) List<String> categoryIds,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        Specification<Course> spec = Specification
                .where(CourseSpecification.hasName(name)
                        .and(CourseSpecification.hasStatus(1))
                        .and(CourseSpecification.hasCategoryIds(categoryIds))
                        .and(CourseSpecification.hasPriceBetween(minPrice, maxPrice))
                );
        Pageable pageable = PageRequest.of(page, size);
        Page<CourseDTO> coursePage = courseService.findAll(spec, pageable);

        return ResponseEntity.status(HttpStatus.OK).body(
                new ApiResponse<>(200, "Successfully", coursePage)
        );
    }

    @GetMapping("/any/course-online/get-top-6")
    ResponseEntity<ApiResponse<CourseResponse>> getCourseTop6() {
        List<CourseDTO> list = courseService.findCourseTop6();
        return ResponseEntity.status(HttpStatus.OK).body(
                new ApiResponse(200,"Successfully", list)
        );
    }

    @PreAuthorize("hasRole('USER')")
    @GetMapping("/course-online/get-by-student")
    ResponseEntity<ApiResponse<CourseResponse>> getAllByStudent() {
        Jwt jwt = (Jwt) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String id = jwt.getClaimAsString("userId");
        List<CourseResponse> list = courseService.findAllByStudent(id);
        return ResponseEntity.status(HttpStatus.OK).body(
                new ApiResponse(200,"Successfully", list)
        );
    }

    @GetMapping("/any/course-online/get-by-projection-test")
    ResponseEntity<ApiResponse<CourseProjection>> getAllByProjection() {
        List<CourseProjection> list = courseService.findAllByProjection();
        return ResponseEntity.status(HttpStatus.OK).body(
                new ApiResponse(200,"Successfully", list)
        );
    }

    @GetMapping("/any/course-online/get-by-projectiondto-test")
    ResponseEntity<ApiResponse<CourseProjectionDTO>> getAllByProjectionDTO() {
        List<CourseProjectionDTO> list = courseService.findAllByProjectionDTO();
        return ResponseEntity.status(HttpStatus.OK).body(
                new ApiResponse(200,"Successfully", list)
        );
    }

    @GetMapping("/any/course-online/{id}")
    ResponseEntity<ApiResponse<CourseDTOTopicId>> getById(@PathVariable("id") String id) {
        CourseDTOTopicId courseDTO = courseService.findById(id);
        return ResponseEntity.status(HttpStatus.OK).body(
                new ApiResponse<>(200, "Successfully", courseDTO)
        );
    }

    @GetMapping("/any/course-online/detail/{id}")
    ResponseEntity<ApiResponse<CourseDetail>> getDetailId(@PathVariable("id") String id) {
        CourseDetail courseDetail = courseService.getDetail(id);
        return ResponseEntity.status(HttpStatus.OK).body(
                new ApiResponse<>(200, "Successfully", courseDetail)
        );
    }

    @GetMapping("/any/course-online/related/{id}")
    ResponseEntity<ApiResponse<List<CourseDTO>>> listRelated(@PathVariable("id") String id) {
        List<CourseDTO> courseDTOs = courseService.getListRelated(id);
        return ResponseEntity.status(HttpStatus.OK).body(
                new ApiResponse<>(200, "Successfully", courseDTOs)
        );
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/course-online/insert")
    ResponseEntity<ApiResponse<CourseDTO>> createCourse(@RequestBody @Valid CreateCourse model) {
        String userId = jwtUtils.getUserIdFromJwt();
        log.info(userId);
        CourseDTO courseDTO = courseService.create(model);
        return ResponseEntity.status(HttpStatus.OK).body(
                new ApiResponse<>(200, "Successfully", courseDTO)
        );
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/course-online/edit")
    ResponseEntity<ApiResponse<CourseDTO>> editCourse(@RequestBody @Valid EditCourse model) {
        CourseDTO courseDTO = courseService.edit(model);
        return ResponseEntity.status(HttpStatus.OK).body(
                new ApiResponse<>(200, "Successfully", courseDTO)
        );
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/course-online/delete")
    ResponseEntity<ApiResponse<String>> delete(@RequestBody String[] ids) {
        courseService.delete(ids);
        return ResponseEntity.status(HttpStatus.OK).body(
                new ApiResponse<>(200, "Successfully", "")
        );
    }

}
