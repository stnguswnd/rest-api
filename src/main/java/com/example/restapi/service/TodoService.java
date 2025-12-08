package com.example.restapi.service;

import com.example.restapi.dto.request.TodoCreateRequest;
import com.example.restapi.dto.response.TodoResponse;

import java.util.List;

public interface TodoService {
    TodoResponse create(TodoCreateRequest request);
    List<TodoResponse> findAll();


}
