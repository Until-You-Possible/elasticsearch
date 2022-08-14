package com.example.es.service;

import com.alibaba.fastjson.JSONObject;
import com.example.es.util.CommonUtil;
import com.example.es.util.request.RestTemplateUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class IndexCourseService {

    // 获取所有课程的数据
    // 注意分页问题



    String firstPage = "https://open.sap.com/bridges/moochub/courses?page=1";
    String lastPage = "https://open.sap.com/bridges/moochub/courses?page=3";
    String prevPage = "https://open.sap.com/bridges/moochub/courses?page=2";

    public List<Object> getCourseData() {
        JSONObject firstObject = RestTemplateUtil.doGetByJson(firstPage);
        JSONObject prevObject = RestTemplateUtil.doGetByJson(prevPage);
        JSONObject lastObject = RestTemplateUtil.doGetByJson(lastPage);

        List<Object> list1 = CommonUtil.toParseArray(firstObject);
        List<Object> list2 = CommonUtil.toParseArray(prevObject);
        List<Object> list3 = CommonUtil.toParseArray(lastObject);
        return CommonUtil.mergeArray(list1, list2, list3);
    }

}


