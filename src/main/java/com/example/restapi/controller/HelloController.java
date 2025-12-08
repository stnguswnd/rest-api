package com.example.restapi.controller;


//import org.springframework.stereotype.Controller;

import com.example.restapi.dto.MessageResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

//Controller는 html을 찾아서 반환하는게 목적,
//RestController는 데이터를 반환하는게 목적.
@RestController
public class HelloController {

    @GetMapping("/api/hello")
    public String hello() {
        return "Hello World";
    }

    @GetMapping("/api/message")
    public MessageResponse message() {
        return new MessageResponse("hello", 200);
    }

    @GetMapping("api/messages")
    public List<MessageResponse> messages() {
        return List.of(
                new MessageResponse("hello1", 200),
                new MessageResponse("hello2", 200),
                new MessageResponse("hello3", 200)
        );
    }
}
