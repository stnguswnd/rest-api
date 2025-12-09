# REST API 프로젝트 전체 코드 문서

## 목차
1. [프로젝트 개요](#프로젝트-개요)
2. [기술 스택](#기술-스택)
3. [프로젝트 구조](#프로젝트-구조)
4. [설정 파일](#설정-파일)
5. [전체 코드](#전체-코드)
6. [API 명세](#api-명세)

---

## 프로젝트 개요

Spring Boot를 기반으로 한 RESTful API 프로젝트입니다. Todo 관리 시스템을 구현하며, CRUD(Create, Read, Update, Delete) 기능을 제공합니다.

### 주요 기능
- Todo 생성 (POST)
- Todo 전체 조회 (GET)
- Todo 단건 조회 (GET)
- Todo 수정 (PUT)
- Todo 삭제 (DELETE)

---

## 기술 스택

### Backend Framework
- **Spring Boot 3.5.8**
- **Java 21**
- **Gradle**

### 주요 라이브러리
- **Spring Boot Starter Data JPA** - ORM 및 데이터베이스 연동
- **Spring Boot Starter Web** - RESTful API 개발
- **Spring Boot Starter Validation** - 입력값 검증
- **Lombok** - 보일러플레이트 코드 감소
- **H2 Database** - 인메모리 데이터베이스

---

## 프로젝트 구조

```
rest-api/
├── src/
│   ├── main/
│   │   ├── java/com/example/restapi/
│   │   │   ├── controller/          # REST API 컨트롤러
│   │   │   │   ├── HelloController.java
│   │   │   │   └── TodoController.java
│   │   │   ├── dto/                 # 데이터 전송 객체
│   │   │   │   ├── MessageResponse.java
│   │   │   │   ├── request/
│   │   │   │   │   ├── TodoCreateRequest.java
│   │   │   │   │   └── TodoUpdateRequest.java
│   │   │   │   └── response/
│   │   │   │       ├── ApiResponse.java
│   │   │   │       └── TodoResponse.java
│   │   │   ├── entity/              # JPA 엔티티
│   │   │   │   └── Todo.java
│   │   │   ├── exception/           # 예외 처리
│   │   │   │   ├── CustomException.java
│   │   │   │   ├── ErrorCode.java
│   │   │   │   └── GlobalExceptionHandler.java
│   │   │   ├── repository/          # 데이터 저장소
│   │   │   │   └── TodoRepository.java
│   │   │   ├── service/             # 비즈니스 로직
│   │   │   │   ├── TodoService.java
│   │   │   │   └── TodoServiceImpl.java
│   │   │   └── RestapiApplication.java  # 애플리케이션 진입점
│   │   └── resources/
│   │       └── application.properties    # 애플리케이션 설정
│   └── test/                        # 테스트 코드
├── build.gradle                     # Gradle 빌드 설정
├── settings.gradle                  # Gradle 프로젝트 설정
└── README.md                        # 프로젝트 설명
```

---

## 설정 파일

### build.gradle

```gradle
plugins {
    id 'java'
    id 'org.springframework.boot' version '3.5.8'
    id 'io.spring.dependency-management' version '1.1.7'
}

group = 'com.example'
version = '0.0.1-SNAPSHOT'
description = 'restapi'

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(21)
    }
}

configurations {
    compileOnly {
        extendsFrom annotationProcessor
    }
}

repositories {
    mavenCentral()
}

dependencies {
    implementation 'org.springframework.boot:spring-boot-starter-data-jpa'
    implementation 'org.springframework.boot:spring-boot-starter-validation'
    implementation 'org.springframework.boot:spring-boot-starter-web'
    compileOnly 'org.projectlombok:lombok'
    developmentOnly 'org.springframework.boot:spring-boot-devtools'
    runtimeOnly 'com.h2database:h2'
    annotationProcessor 'org.projectlombok:lombok'
    testImplementation 'org.springframework.boot:spring-boot-starter-test'
    testRuntimeOnly 'org.junit.platform:junit-platform-launcher'
}

tasks.named('test') {
    useJUnitPlatform()
}
```

### settings.gradle

```gradle
rootProject.name = 'restapi'
```

### application.properties

```properties
spring.application.name=restapi

spring.datasource.driver-class-name=org.h2.Driver
spring.datasource.url=jdbc:h2:mem:testdb
spring.datasource.password=
spring.datasource.username=sa

spring.h2.console.path=/h2-console
spring.h2.console.enabled=true

spring.jpa.hibernate.ddl-auto=create
spring.jpa.show-sql=true
```

---

## 전체 코드

### 1. Application (진입점)

#### RestapiApplication.java

```java
package com.example.restapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class RestapiApplication {

    public static void main(String[] args) {
        SpringApplication.run(RestapiApplication.class, args);
    }

}
```

---

### 2. Entity (엔티티)

#### Todo.java

```java
package com.example.restapi.entity;

import com.example.restapi.dto.request.TodoUpdateRequest;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor
public class Todo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    private String content;

    private boolean completed;

    private LocalDateTime createdAt;

    @Builder
    public Todo(String title, String content) {
        this.title = title;
        this.content = content;
        this.completed = false;
        this.createdAt = LocalDateTime.now();
    }

    public void update(String title, String content) {
        this.title = title;
        this.content = content;
    }
}
```

**주요 특징:**
- JPA Entity로 데이터베이스 테이블과 매핑
- ID는 자동 생성 (IDENTITY 전략)
- 생성 시 completed는 자동으로 false로 설정
- createdAt은 생성 시점의 시간으로 자동 설정
- update 메서드로 제목과 내용 수정 가능

---

### 3. Repository (데이터 저장소)

#### TodoRepository.java

```java
package com.example.restapi.repository;

import com.example.restapi.entity.Todo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TodoRepository extends JpaRepository<Todo, Long> {
    //save, findById ...
}
```

**주요 특징:**
- JpaRepository를 상속하여 기본 CRUD 메서드 자동 제공
- save, findById, findAll, deleteById, existsById 등 사용 가능

---

### 4. DTO (Data Transfer Object)

#### 4.1 Request DTO

##### TodoCreateRequest.java

```java
package com.example.restapi.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class TodoCreateRequest {

    @NotBlank(message = "제목은 필수입니다.")
    @Size(max = 100, message ="제목은 100자 이하입니다")
    private String title;

    @Size(max = 500, message="내용은 500자 이하입니다.")
    private String content;
}
```

**검증 규칙:**
- title: 필수 입력, 최대 100자
- content: 선택 입력, 최대 500자

##### TodoUpdateRequest.java

```java
package com.example.restapi.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class TodoUpdateRequest {
    @NotBlank(message = "제목은 필수입니다.")
    @Size(max = 100, message = "제목은 100자 이하입니다.")
    private String title;

    @Size(max = 500, message = "내용은 500자 이하입니다.")
    private String content;
}
```

**검증 규칙:**
- title: 필수 입력, 최대 100자
- content: 선택 입력, 최대 500자

#### 4.2 Response DTO

##### TodoResponse.java

```java
package com.example.restapi.dto.response;


import com.example.restapi.entity.Todo;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Builder
public class TodoResponse {
    private Long id;
    private String title;
    private String content;
    private boolean completed;
    private LocalDateTime createdAt;

    public static TodoResponse from(Todo todo) {
        return TodoResponse.builder()
                .id(todo.getId())
                .title(todo.getTitle())
                .content(todo.getContent())
                .completed(todo.isCompleted())
                .createdAt(todo.getCreatedAt())
                .build();
    }
}
```

**주요 특징:**
- Entity를 DTO로 변환하는 from 메서드 제공
- 클라이언트에게 필요한 정보만 노출

##### ApiResponse.java

```java
package com.example.restapi.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)

public class ApiResponse<T> {
    private boolean success;
    private T data;
    private ErrorDetail error;

    // 성공 응답 (데이터 O)
    public static <T> ApiResponse<T> success(T data) {
        return new ApiResponse<>(true, data, null);
    }

    // 성공 응답 (데이터X) <- 오버로딩 처리함.
    public static <T> ApiResponse<T> success() {
        return new ApiResponse<>(true, null, null);
    }

    // 에러 응답
    public static <T> ApiResponse<T> error(String code, String message) {
        return new ApiResponse<>(false, null, new ErrorDetail(code, message));
    }
    @Getter
    @AllArgsConstructor
    public static class ErrorDetail {
        private String code;
        private String message;
    }
}
```

**주요 특징:**
- 제네릭을 사용한 일관된 API 응답 구조
- success: 성공 여부
- data: 응답 데이터
- error: 에러 정보
- null 필드는 JSON에서 제외됨 (@JsonInclude)

##### MessageResponse.java

```java
package com.example.restapi.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor


public class MessageResponse {
    private String message;
    private int code;
}
```

**주요 특징:**
- 간단한 메시지 응답용 DTO
- HelloController에서 테스트용으로 사용

---

### 5. Service (비즈니스 로직)

#### TodoService.java (인터페이스)

```java
package com.example.restapi.service;

import com.example.restapi.dto.request.TodoCreateRequest;
import com.example.restapi.dto.request.TodoUpdateRequest;
import com.example.restapi.dto.response.TodoResponse;

import java.util.List;

public interface TodoService {
    TodoResponse create(TodoCreateRequest request);
    List<TodoResponse> findAll();
    TodoResponse findById(Long id);
    void delete(Long id);
    TodoResponse update(Long id, TodoUpdateRequest request);
}
```

#### TodoServiceImpl.java (구현체)

```java
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
```

**주요 특징:**
- @Transactional(readOnly = true): 기본적으로 읽기 전용 트랜잭션
- CUD 작업에만 @Transactional 추가하여 쓰기 트랜잭션 적용
- 존재하지 않는 Todo 접근 시 CustomException 발생
- Stream API를 활용한 Entity → DTO 변환

---

### 6. Controller (REST API)

#### TodoController.java

```java
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
```

**주요 특징:**
- 기본 경로: `/api/todos`
- @Valid를 통한 요청 데이터 검증
- ApiResponse로 일관된 응답 형식 제공
- HTTP 상태 코드 적절히 반환 (201 Created, 200 OK)

#### HelloController.java

```java
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
```

**주요 특징:**
- 테스트용 컨트롤러
- 단순 문자열, 객체, 리스트 반환 예제

---

### 7. Exception (예외 처리)

#### ErrorCode.java

```java
package com.example.restapi.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {

    //공통 에러
    INVALID_INPUT(HttpStatus.BAD_REQUEST, "INVALID INPUT", "잘못된 입력값입니다."),
    INTERNAL_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "INTERNAL_ERROR", "서버 오류가 발생했습니다."),

    // Todo 에러
    TODO_NOT_FOUND(HttpStatus.NOT_FOUND, "TODO_NOT_FOUND", "할일을 찾을 수 없습니다.");

    private final HttpStatus status;
    private final String code;
    private final String message;
}
```

**주요 특징:**
- Enum으로 에러 코드 관리
- HTTP 상태 코드, 에러 코드, 에러 메시지를 함께 관리

#### CustomException.java

```java
package com.example.restapi.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;


@Getter
public class CustomException extends RuntimeException{
    private final ErrorCode errorCode;

    public CustomException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }

    public CustomException(ErrorCode errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
    }

}
```

**주요 특징:**
- RuntimeException을 상속한 커스텀 예외
- ErrorCode를 포함하여 예외 정보 관리
- 메시지 커스터마이징 가능한 생성자 제공

#### GlobalExceptionHandler.java

```java
package com.example.restapi.exception;


import com.example.restapi.dto.response.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {


    // CustomException 처리
    @ExceptionHandler(CustomException.class)
    public ResponseEntity<ApiResponse<Void>> handleCustomException(CustomException ex){
        ErrorCode errorCode = ex.getErrorCode();

        return ResponseEntity.status(errorCode.getStatus())
                .body(ApiResponse.error(errorCode.getCode(), errorCode.getMessage()));
    }

    // Validation 예외 처리
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<Void>> handleCustomException(MethodArgumentNotValidException ex){
        // 제목은 필수입니다 , 내용은 500자 이하입니다. 를 응답하는 코드임.
        String message = ex.getBindingResult().getFieldErrors().stream()
                .map(FieldError::getDefaultMessage)
                .collect(Collectors.joining(", "));

        return ResponseEntity.badRequest()
                .body(ApiResponse.error("VALIDATION ERROR", message));
    }



    // 그 외 모든 예외 처리
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<Void>> handleException(Exception ex){
        return ResponseEntity.internalServerError()  //500 에러를 의미함.
                .body(ApiResponse.error("INTERNAL_ERROR","서버 오류가 발생했습니다"));

    }



}
```

**주요 특징:**
- @RestControllerAdvice로 전역 예외 처리
- CustomException: 비즈니스 로직 예외
- MethodArgumentNotValidException: 입력값 검증 실패
- Exception: 그 외 모든 예외를 500 에러로 처리
- 모든 에러는 ApiResponse 형식으로 일관되게 반환

---

## API 명세

### Base URL
```
http://localhost:8080
```

### 1. Todo 생성

**Endpoint**
```
POST /api/todos
```

**Request Body**
```json
{
  "title": "할일 제목",
  "content": "할일 내용"
}
```

**Response (201 Created)**
```json
{
  "success": true,
  "data": {
    "id": 1,
    "title": "할일 제목",
    "content": "할일 내용",
    "completed": false,
    "createdAt": "2025-12-08T15:30:00"
  }
}
```

**Validation Error (400)**
```json
{
  "success": false,
  "error": {
    "code": "VALIDATION ERROR",
    "message": "제목은 필수입니다."
  }
}
```

---

### 2. Todo 전체 조회

**Endpoint**
```
GET /api/todos
```

**Response (200 OK)**
```json
{
  "success": true,
  "data": [
    {
      "id": 1,
      "title": "할일 1",
      "content": "내용 1",
      "completed": false,
      "createdAt": "2025-12-08T15:30:00"
    },
    {
      "id": 2,
      "title": "할일 2",
      "content": "내용 2",
      "completed": false,
      "createdAt": "2025-12-08T15:35:00"
    }
  ]
}
```

---

### 3. Todo 단건 조회

**Endpoint**
```
GET /api/todos/{id}
```

**Path Variable**
- id: Todo ID (Long)

**Response (200 OK)**
```json
{
  "success": true,
  "data": {
    "id": 1,
    "title": "할일 제목",
    "content": "할일 내용",
    "completed": false,
    "createdAt": "2025-12-08T15:30:00"
  }
}
```

**Not Found (404)**
```json
{
  "success": false,
  "error": {
    "code": "TODO_NOT_FOUND",
    "message": "할일을 찾을 수 없습니다."
  }
}
```

---

### 4. Todo 수정

**Endpoint**
```
PUT /api/todos/{id}
```

**Path Variable**
- id: Todo ID (Long)

**Request Body**
```json
{
  "title": "수정된 제목",
  "content": "수정된 내용"
}
```

**Response (200 OK)**
```json
{
  "success": true,
  "data": {
    "id": 1,
    "title": "수정된 제목",
    "content": "수정된 내용",
    "completed": false,
    "createdAt": "2025-12-08T15:30:00"
  }
}
```

**Not Found (404)**
```json
{
  "success": false,
  "error": {
    "code": "TODO_NOT_FOUND",
    "message": "할일을 찾을 수 없습니다."
  }
}
```

---

### 5. Todo 삭제

**Endpoint**
```
DELETE /api/todos/{id}
```

**Path Variable**
- id: Todo ID (Long)

**Response (200 OK)**
```json
{
  "success": true
}
```

**Not Found (404)**
```json
{
  "success": false,
  "error": {
    "code": "TODO_NOT_FOUND",
    "message": "할일을 찾을 수 없습니다."
  }
}
```

---

## 실행 방법

### 1. 프로젝트 빌드

```bash
./gradlew build
```

### 2. 애플리케이션 실행

```bash
./gradlew bootRun
```

또는

```bash
java -jar build/libs/restapi-0.0.1-SNAPSHOT.jar
```

### 3. H2 Console 접속

```
http://localhost:8080/h2-console
```

**접속 정보:**
- JDBC URL: `jdbc:h2:mem:testdb`
- Username: `sa`
- Password: (공백)

---

## 주요 특징 및 설계 패턴

### 1. 계층형 아키텍처 (Layered Architecture)
- **Controller Layer**: HTTP 요청/응답 처리
- **Service Layer**: 비즈니스 로직 처리
- **Repository Layer**: 데이터 액세스
- **Entity Layer**: 도메인 모델

### 2. DTO 패턴
- Request DTO: 클라이언트로부터 받는 데이터
- Response DTO: 클라이언트에게 보내는 데이터
- Entity와 DTO 분리로 계층 간 결합도 감소

### 3. 예외 처리 전략
- 비즈니스 예외: CustomException + ErrorCode
- 검증 예외: @Valid + MethodArgumentNotValidException
- 전역 예외 처리: @RestControllerAdvice

### 4. 트랜잭션 관리
- 읽기 전용 트랜잭션: @Transactional(readOnly = true)
- 쓰기 트랜잭션: @Transactional

### 5. 일관된 API 응답
- ApiResponse 래퍼 클래스로 통일된 응답 형식
- success 필드로 성공/실패 구분
- error 객체로 에러 정보 제공

---

## 개선 가능 사항

1. **인증/인가**: Spring Security를 활용한 보안 기능 추가
2. **페이징**: 대량의 데이터 조회 시 페이징 처리
3. **검색**: Todo 제목/내용 기반 검색 기능
4. **완료 상태 변경**: Todo의 completed 필드를 변경하는 API
5. **정렬**: 생성일, 제목 등으로 정렬 기능
6. **테스트**: 단위 테스트 및 통합 테스트 추가
7. **로깅**: 요청/응답 로깅, 에러 로깅 강화
8. **실제 DB 연동**: H2 대신 MySQL, PostgreSQL 등 사용

---

## 작성일
2025-12-08
