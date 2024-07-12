package com.example.course_service.controller;

import com.example.course_service.dto.ApiResponse;
import com.example.course_service.dto.request.item_online.CreateItemOnline;
import com.example.course_service.dto.request.item_online.EditItemOnline;
import com.example.course_service.dto.response.item_online.ItemOnlineDTO;
import com.example.course_service.dto.response.item_online.ItemOnlineDetail;
import com.example.course_service.service.ItemOnlineService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@Slf4j
public class ItemOnlineController {
    private final ItemOnlineService itemOnlineService;

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/item-online/{id}")
    ResponseEntity<ApiResponse<ItemOnlineDTO>> getById(@PathVariable("id") String id) {
        ItemOnlineDTO itemOnlineDTO = itemOnlineService.findById(id);
        return ResponseEntity.status(HttpStatus.OK).body(
                new ApiResponse<>(200, "Successfully", itemOnlineDTO)
        );
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/item-online/insert")
    ResponseEntity<ApiResponse<ItemOnlineDTO>> createTopic(@RequestBody @Valid CreateItemOnline model) {
        ItemOnlineDTO itemOnlineDTO = itemOnlineService.create(model);
        return ResponseEntity.status(HttpStatus.OK).body(
                new ApiResponse<>(200, "Successfully", itemOnlineDTO)
        );
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/item-online/edit")
    ResponseEntity<ApiResponse<ItemOnlineDTO>> editTopic(@RequestBody @Valid EditItemOnline model) {
        ItemOnlineDTO itemOnlineDTO = itemOnlineService.edit(model);
        return ResponseEntity.status(HttpStatus.OK).body(
                new ApiResponse<>(200, "Successfully", itemOnlineDTO)
        );
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/item-online/delete")
    ResponseEntity<ApiResponse<String>> delete(@RequestBody String[] ids) {
        itemOnlineService.delete(ids);
        return ResponseEntity.status(HttpStatus.OK).body(
                new ApiResponse<>(200, "Successfully", "")
        );
    }

    @GetMapping("/item-online/detail/{id}")
    ResponseEntity<ApiResponse<ItemOnlineDetail>> getDetailById(@PathVariable("id") String id) {
        Jwt jwt = (Jwt) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String idUser = jwt.getClaimAsString("userId");
        ItemOnlineDetail itemOnlineDetail = itemOnlineService.getDetailById(id, idUser);
        return ResponseEntity.status(HttpStatus.OK).body(
                new ApiResponse<>(200, "Successfully", itemOnlineDetail)
        );
    }

    @PutMapping("/item-online/complete/{id}")
    ResponseEntity<ApiResponse> completeItem(@PathVariable("id") String id) {
        Jwt jwt = (Jwt) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String idUser = jwt.getClaimAsString("userId");
        itemOnlineService.complete(id, idUser);
        return ResponseEntity.status(HttpStatus.OK).body(
                new ApiResponse<>(200, "Successfully", "")
        );
    }
}
