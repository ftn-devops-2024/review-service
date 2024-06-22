package com.devops.reviewservice.controller;

import com.devops.reviewservice.service.ReviewService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
public class TestController {

    @GetMapping("/test")
    public String test(){
        return "Welcome from review-service";
    }
}

