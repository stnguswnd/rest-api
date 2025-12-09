package com.example.restapi.controller;


import com.example.restapi.dto.request.TodoCreateRequest;
import com.example.restapi.dto.request.TodoUpdateRequest;
import com.example.restapi.dto.response.ApiResponse;
import com.example.restapi.dto.response.TodoResponse;
import com.example.restapi.entity.Todo;
import com.example.restapi.service.TodoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.annotation.AuthenticationPrincipal;

import java.util.List;

@RestController
@RequestMapping("/api/todos")
@RequiredArgsConstructor
public class TodoController {
    private final TodoService todoService;

    @PostMapping
    public ResponseEntity<ApiResponse<TodoResponse>> create(
            @Valid @RequestBody TodoCreateRequest request
    ) {
        TodoResponse response = todoService.create(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(response));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<TodoResponse>>> findAll() {
        List<TodoResponse> responses = todoService.findAll();
        return ResponseEntity.ok(ApiResponse.success(responses));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<TodoResponse>> findById(
            @PathVariable Long id
    ){
        TodoResponse response = todoService.findById(id);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> delete(
        @PathVariable Long id)
    {
        todoService.delete(id);
        return ResponseEntity.ok(ApiResponse.success()); //No 컨텐츠를 ok로 바꾸면서 204 -> 200로 바뀜.
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<TodoResponse>> update(
            @PathVariable Long id,
            @Valid @RequestBody TodoUpdateRequest request)
    {
        TodoResponse response = todoService.update(id, request);
        return ResponseEntity.ok(ApiResponse.success(response));
    }
}
