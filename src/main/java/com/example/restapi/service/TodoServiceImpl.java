package com.example.restapi.service;

import com.example.restapi.dto.request.TodoCreateRequest;
import com.example.restapi.dto.request.TodoUpdateRequest;
import com.example.restapi.dto.response.TodoResponse;
import com.example.restapi.entity.Todo;
import com.example.restapi.exception.CustomException;
import com.example.restapi.exception.ErrorCode;
import com.example.restapi.repository.TodoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

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

    @Override
    public List<TodoResponse> findAll() {
        return todoRepository.findAll().stream()
                .map(TodoResponse::from)
                .toList();
    }

    @Override
    public TodoResponse findById(Long id) {
        Todo todo = todoRepository.findById(id)
                .orElseThrow(() -> new CustomException(ErrorCode.TODO_NOT_FOUND));
        return TodoResponse.from(todo);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        if (!todoRepository.existsById(id)) {
            throw new CustomException(ErrorCode.TODO_NOT_FOUND);
        }
        todoRepository.deleteById(id);
    }

    @Override
    @Transactional
    public TodoResponse update(Long id, TodoUpdateRequest request) {
        Todo todo = todoRepository.findById(id)
                .orElseThrow(() -> new CustomException(ErrorCode.TODO_NOT_FOUND));

        todo.update(request.getTitle(), request.getContent());
        return TodoResponse.from(todo);

    }

}
