package com.example.es.util.request;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

public class RestTemplateUtil {

    static RestTemplate restTemplate;

    static {
        SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
        factory.setReadTimeout(5000);
        factory.setConnectTimeout(5000);
        restTemplate = new RestTemplate(factory);
    }

    public static JSONObject doGetByJson(String url) {

        JSONObject jsonObject = new JSONObject();
        ResponseEntity<String> response = null;
        String responseBody = "";

        try {
            response = restTemplate.getForEntity(url, String.class);
            responseBody = new String(Objects.requireNonNull(response.getBody()).getBytes(StandardCharsets.UTF_8));
        } catch (RestClientException restClientException) {
            jsonObject.put("message", restClientException.getMessage());
            return jsonObject;
        }
        jsonObject = JSON.parseObject(responseBody);
        return jsonObject;

    }
}
