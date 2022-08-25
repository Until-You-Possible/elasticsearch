package com.example.es.controller;


import com.example.es.service.IndexCourseService;
import com.example.es.service.IndexDataService;
import com.example.es.service.IndexProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.HashMap;

@RestController
@RequestMapping("/index")
public class indexDataController {

    @Autowired
    private IndexDataService indexDataService;

    // product
    @GetMapping("/product")
    public HashMap<String, Object> indexProduct() throws IOException {
        return indexDataService.fillIndexProduct();
    }
    // user
    @GetMapping("/user")
    public HashMap<String, Object> indexUser() throws IOException {
        return indexDataService.fillIndexUser();
    }

}
