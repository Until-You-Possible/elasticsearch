package com.example.es.controller;


import co.elastic.clients.elasticsearch.indices.Alias;
import co.elastic.clients.elasticsearch.indices.CreateIndexRequest;
import co.elastic.clients.elasticsearch.indices.CreateIndexResponse;
import com.example.es.client.ElasticSearchClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequestMapping("/test")
public class TestController {

    private ElasticSearchClient elasticSearchClient;

    private ElasticSearchClient getElasticSearchClient() {
        if (elasticSearchClient == null) {
            elasticSearchClient = new ElasticSearchClient();
        }
        return elasticSearchClient;
    }

    @GetMapping("/index")
    public String index() {
        return "About elasticsearch interface";
    }

    @GetMapping("/exist")
    public Boolean existIndex() throws IOException {
        return getElasticSearchClient().createClient().exists(s -> s.index("test").id("P2_J7baYSeWf3paJa7MQgA")).value();
    }

    @GetMapping("/create")
    public Boolean createIndex() {
        return false;
    }

}
