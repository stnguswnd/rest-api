package com.example.restapi.service;

import com.example.restapi.dto.request.TodoCreateRequest;
import com.example.restapi.dto.request.TodoUpdateRequest;
import com.example.restapi.dto.response.TodoResponse;

import java.util.List;

public interface TodoService {
    TodoResponse create(TodoCreateRequest request, String username);
    List<TodoResponse> findAll();
    TodoResponse findById(Long id);
    void delete(Long id);
    TodoResponse update(Long id, TodoUpdateRequest request);
}
