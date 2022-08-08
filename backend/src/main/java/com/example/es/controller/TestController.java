package com.example.es.controller;


import co.elastic.clients.elasticsearch._types.mapping.KeywordProperty;
import co.elastic.clients.elasticsearch._types.mapping.Property;
import co.elastic.clients.elasticsearch._types.mapping.TextProperty;
import co.elastic.clients.elasticsearch._types.mapping.TypeMapping;
import co.elastic.clients.elasticsearch.indices.CreateIndexRequest;
import co.elastic.clients.elasticsearch.indices.CreateIndexResponse;
import com.example.es.client.ElasticSearchClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

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
    public HashMap<String, Object> existIndex() throws IOException {
        HashMap<String, Object> rawMap = new HashMap<>();
        String indexName = "test";
        Boolean bool = getElasticSearchClient().existIndex(indexName);
        rawMap.put("result",bool);
        rawMap.put("message", indexName + " is existed");
        return rawMap;
    }

    @GetMapping("/deleteIndex")
    public HashMap<String, Object> deleteIndex() throws IOException {
        HashMap<String, Object> rawMap = new HashMap<>();
        String indexName = "system";
        Boolean bool = getElasticSearchClient().deleteIndex(indexName);
        rawMap.put("result",bool);
        rawMap.put("message", indexName + "has been deleted");
        return rawMap;
    }

    @GetMapping("/create")
    public Boolean createIndex() throws IOException {
        String indexName = "system2";
        //定义文档属性
//        Map<String, Property> propertyMap = new HashMap<>();
//        propertyMap.put("id",new Property(new KeywordProperty.Builder().build()));
//        propertyMap.put("path",new Property(new KeywordProperty.Builder().build()));
//        propertyMap.put("url",new Property(new KeywordProperty.Builder().build()));
//        propertyMap.put("md5",new Property(new KeywordProperty.Builder().build()));
//        propertyMap.put("size",new Property(new KeywordProperty.Builder().build()));
//        propertyMap.put("dir",new Property(new KeywordProperty.Builder().build()));
//        propertyMap.put("extendName",new Property(new KeywordProperty.Builder().build()));
//        propertyMap.put("source",new Property(new KeywordProperty.Builder().build()));
//        propertyMap.put("isDelete",new Property(new KeywordProperty.Builder().build()));
//        propertyMap.put("createTime",new Property(new KeywordProperty.Builder().build()));
//        propertyMap.put("updateTime",new Property(new KeywordProperty.Builder().build()));
//        propertyMap.put("content",new Property(new TextProperty.Builder().build()));
//        propertyMap.put("hitTime",new Property(new TextProperty.Builder().build()));
//
//        // 设置索引的文档类型映射
//        TypeMapping typeMapping = new TypeMapping.Builder()
//                .properties(propertyMap
//                .build();
//        CreateIndexRequest request = new CreateIndexRequest.Builder()
//                .index(indexName)
//                .mappings(typeMapping)
//                .build();
//        CreateIndexResponse createIndexResponse = getElasticSearchClient().createClient().indices().create(request);
//        return createIndexResponse.acknowledged();
        return getElasticSearchClient().createIndex(indexName);


    }


}
