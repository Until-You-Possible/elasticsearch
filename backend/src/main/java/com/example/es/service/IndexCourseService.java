package com.example.es.service;

import com.alibaba.fastjson.JSONObject;
import com.example.es.util.CommonUtil;
import com.example.es.util.request.RestTemplateUtil;

import java.util.*;
import java.util.stream.Collectors;

public class IndexCourseService {

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

}

/*
 Stream.of()
 Stream()

 对于Arrays.Stream()的方法可以接受一个数组并产生一个流
 flatMap的方法可以把流中的每一个值转换成另一个流，然后链接起来成为一个新的流。


*/


