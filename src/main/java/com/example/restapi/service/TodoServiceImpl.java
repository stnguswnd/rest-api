package com.example.restapi.service;

import com.example.restapi.dto.request.TodoCreateRequest;
import com.example.restapi.dto.response.TodoResponse;
import com.example.restapi.entity.Todo;
import com.example.restapi.repository.TodoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true )
public class TodoServiceImpl implements TodoService {

    private final TodoRepository todoRepository;

    @Override
    @Transactional
    public TodoResponse create(TodoCreateRequest request){
        Todo todo = Todo.builder()
                .title(request.getTitle())
                .content(request.getContent())
                .build();
        Todo saved = todoRepository.save(todo);
        return TodoResponse.from(saved);
    }
}
