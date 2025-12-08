package com.example.restapi.service;

import com.example.restapi.dto.request.TodoCreateRequest;
import com.example.restapi.dto.response.TodoResponse;

public interface TodoService {
    TodoResponse create(TodoCreateRequest request);


}
