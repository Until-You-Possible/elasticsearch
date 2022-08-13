package com.example.es.controller;
import com.alibaba.fastjson.JSONObject;
import com.example.es.service.ElasticSearchService;
import com.example.es.util.request.HttpClientToInterface;
import com.example.es.util.request.RestTemplateUtil;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.example.es.util.readSetting.ReadAccountMessage;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/test")
public class TestController {

    private ElasticSearchService elasticSearchService;

    private ElasticSearchService getElasticSearchService() {
        if (elasticSearchService == null) {
            elasticSearchService = new ElasticSearchService();
        }
        return elasticSearchService;
    }

    @GetMapping("/index")
    public String index() {
        return "About elasticsearch interface";
    }

    @GetMapping("/exist")
    public HashMap<String, Object> existIndex() throws IOException {
        HashMap<String, Object> rawMap = new HashMap<>();
        String indexName = "test";
        Boolean bool = getElasticSearchService().existIndex(indexName);
        rawMap.put("result",bool);
        rawMap.put("message", indexName + " is existed");
        return rawMap;
    }

    @GetMapping("/deleteIndex")
    public HashMap<String, Object> deleteIndex() throws IOException {
        HashMap<String, Object> rawMap = new HashMap<>();
        String indexName = "system";
        Boolean bool = getElasticSearchService().deleteIndex(indexName);
        rawMap.put("result",bool);
        rawMap.put("message", indexName + "has been deleted");
        return rawMap;
    }

    @GetMapping("/create")
    public Boolean createIndex() throws IOException {
        String indexName = "system2";
        return elasticSearchService.createIndex(indexName);
    }

    @GetMapping("/indexDetail")
    public Map<String, Object> indexDetail() throws IOException {
        String indexName = "system";
        return getElasticSearchService().indexDetail(indexName);
    }

    @GetMapping("/getpath")
    public HashMap<String, String> getPath() throws IOException {

        return new ReadAccountMessage().getAccountInformation();

    }

    @GetMapping("/doget")
    public String getCourse() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("commentId", "13026194071");
        return new HttpClientToInterface().doPost("http://tcc.taobao.com/cc/json/mobile_tel_segment.htm?tel=13026194071", jsonObject);
    }

    @GetMapping("/sp")
    public HashMap<String, Object> sp() {
        HashMap<String, Object> hashMap = new HashMap<>();
        String url = "https://open.sap.com/bridges/moochub/courses";
        hashMap.put("message", RestTemplateUtil.doGetByJson(url));
        return hashMap;
    }
}
