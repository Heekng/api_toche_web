package com.heekng.api_toche_web.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

@Slf4j
@RestController
@RequiredArgsConstructor
public class WebRestController {

    private final Environment env;

    @GetMapping("/time")
    public String time() {
        LocalDateTime now = LocalDateTime.now();
        return now.toString();
    }
}
