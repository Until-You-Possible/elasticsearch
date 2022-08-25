package com.example.es.service;

import com.example.es.util.CommonUtil;
import com.example.es.util.readSetting.ReadLocalFile;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import static com.example.es.core.Constants.*;

@Service
public class IndexDataService {

    public HashMap<String, List<Object>> getIndexJsonArray(String indexName) {
        return CommonUtil.getIndexJsonArray(indexName);
    }

    public HashMap<String, Object> fillIndexProduct() throws IOException {
        List<Object> objectList =  getIndexJsonArray(PRODUCT).get(RESULT);
        return new ElasticSearchService().fillIndexData(objectList, PRODUCT);
    }


    public HashMap<String, Object> fillIndexUser() throws IOException {
        List<Object> objectList =  getIndexJsonArray(USER).get(RESULT);
        return new ElasticSearchService().fillIndexData(objectList, USER);
    }
}
