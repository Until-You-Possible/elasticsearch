package com.example.es.service;


import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.example.es.util.CommonUtil;
import com.example.es.util.readSetting.ReadJsonFile;
import jakarta.json.JsonArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static com.example.es.core.Constants.*;

@Service
public class IndexProductService {

    private ReadJsonFile readJsonFile;

    private ReadJsonFile getReadJsonFile() {
        if (readJsonFile == null) {
            return  readJsonFile = new ReadJsonFile();
        }
        return readJsonFile;
    }

    // 获取数据源(目前存放本地的json)
    public HashMap<String, List<Object>> getProductJsonArray() {
        HashMap<String, List<Object>> hashMap = new HashMap<>();
        // 读取json存放的路径
        String resourceConfigPath = getReadJsonFile().readJsonSetting(CommonUtil.getResourceFilePath(PRIVATERESOURCEPATH));
        JSONObject jsonObject = JSONObject.parseObject(resourceConfigPath);
        String productJsonPath = (String) jsonObject.getJSONObject("privateMessage").get(PRODUCT);
        // 读取对应的文件
        JSONObject productJsonFile = JSONObject.parseObject(getReadJsonFile().readJsonSetting(productJsonPath));
        JSONArray  jsonArray = productJsonFile.getJSONObject(HITS).getJSONArray(HITS);
        hashMap.put(RESULT, jsonArray.toJavaList(Object.class));
        return hashMap;
    }

    // 写入es
    public HashMap<String, Object> fillIndexProduct() throws IOException {
        // List<Object>
         List<Object> objectList =  getProductJsonArray().get(RESULT);
         return new ElasticSearchService().fillIndexData(objectList, PRODUCT);
    }
}
