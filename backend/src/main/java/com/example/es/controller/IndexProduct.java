package com.example.es.controller;


import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;

@RestController
@RequestMapping("/index")
public class IndexProduct {

    @GetMapping("/product")
    public String indexProduct() {
        return "123456";
    }

}
