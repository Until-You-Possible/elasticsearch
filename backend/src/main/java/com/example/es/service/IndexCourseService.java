package com.example.es.service;

import com.alibaba.fastjson.JSONObject;
import com.example.es.model.Courses;
import com.example.es.util.CommonUtil;
import com.example.es.util.request.RestTemplateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class IndexCourseService {

    @Autowired
    private ElasticSearchService elasticSearchService;

    // 获取所有课程的数据
    // 注意分页问题

    String baseURL = "https://open.sap.com/bridges/moochub/courses";
    int lastPage = 3;

    public List<Object> getCourseData() {

        ArrayList<List<Object>> objectArrayList = new ArrayList<>();

        for (int i = 1; i <= lastPage; i++) {
            JSONObject pageObject = RestTemplateUtil.doGetByJson(baseURL+ "?page=" + i);
            List<Object> objectList = CommonUtil.toParseArray(pageObject);
            objectArrayList.add(objectList);
        }

        return objectArrayList.stream().flatMap(Collection::stream).collect(Collectors.toList());
    }
    // 写入当前数据
    public HashMap<String, Object> fillIndex() throws IOException {
        String indexName = "system";
        // 获取数据
        List<Object> payload = getCourseData();
        ArrayList<Object> objectArrayList = new ArrayList<>();
        // 解析数据格式
        payload.forEach(o -> objectArrayList.add(Courses.formatterSingleCourse((JSONObject) o)));
        return elasticSearchService.fillIndexData(objectArrayList, indexName);
    }

}

/*
 Stream.of()
 Stream()

 对于Arrays.Stream()的方法可以接受一个数组并产生一个流
 flatMap的方法可以把流中的每一个值转换成另一个流，然后链接起来成为一个新的流。


*/


