package com.book.village.server.domain.request.controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RequestController {

    @GetMapping
    public String getRequest() {
        return "hello world!";
    }
}
