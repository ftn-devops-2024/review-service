package com.devops.reviewservice.controller;

import com.devops.reviewservice.service.ReviewService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
public class TestController {

    Logger logger = LoggerFactory.getLogger(ReviewService.class);
    @GetMapping("/test")
    public String test(){
        logger.info("ajde molim te");
        return "Welcome from review-service";
    }
}

