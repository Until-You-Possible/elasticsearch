package com.example.es.service;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch._types.mapping.KeywordProperty;
import co.elastic.clients.elasticsearch._types.mapping.Property;
import co.elastic.clients.elasticsearch._types.mapping.TextProperty;
import co.elastic.clients.elasticsearch._types.mapping.TypeMapping;
import co.elastic.clients.elasticsearch.core.ExistsRequest;
import co.elastic.clients.elasticsearch.core.bulk.BulkOperation;
import co.elastic.clients.elasticsearch.indices.CreateIndexRequest;
import co.elastic.clients.elasticsearch.indices.CreateIndexResponse;
import co.elastic.clients.elasticsearch.indices.DeleteIndexResponse;
import co.elastic.clients.elasticsearch.indices.GetIndexResponse;
import co.elastic.clients.transport.endpoints.BooleanResponse;
import com.alibaba.fastjson.JSONObject;
import com.example.es.client.ElasticSearchClient;
import com.example.es.core.Constants;
import com.example.es.util.CommonUtil;
import com.example.es.util.readSetting.ReadJsonFile;

import java.io.IOException;
import java.util.*;

import static com.example.es.core.Constants.*;

public class ElasticSearchService {

    private ElasticsearchClient elasticsearchClient;

    private IndexCourseService indexCourseService;


    ReadJsonFile readJsonFile;

    private ElasticsearchClient getElasticSearchClient() {
        if (elasticsearchClient == null) {
            return elasticsearchClient = new ElasticSearchClient().getClient();
        }
        return elasticsearchClient;
    }

    private ReadJsonFile getReadJsonFile() {
        if (readJsonFile == null) {
            return  readJsonFile = new ReadJsonFile();
        }
        return readJsonFile;
    }

    private IndexCourseService getIndexCourseService() {
        return Objects.requireNonNullElseGet(indexCourseService, () -> indexCourseService = new IndexCourseService());
    }

    /**
     * 查看index是否在es中存在
     * @param  indexName, Name of the Index
     * @return true if found, false otherwise
     * @throws IOException, if something happened
     */

    public boolean existIndex(String indexName) throws IOException {
        boolean result;
        try {
            BooleanResponse booleanResponse = getElasticSearchClient().indices()
                    .exists(existRequest -> existRequest.index(indexName));
            result = booleanResponse.value();
        } catch (IOException e) {
            result = false;
        }
        // log.info("== {} 索引是否存在: {}", result);
        return result;
    }

    /**
     * 查看相关doc是否在index中存在
     * @param indexName, Name of the index;
     * @param id, ID of the document
     * @return true if found, false otherwise
     * @throws IOException,  if something happened
     */
    public boolean existDocument(String indexName, String id) throws IOException {
        ExistsRequest request = new ExistsRequest.Builder()
                .index(indexName)
                .id(id)
                .build();
        boolean result;
        try {
            BooleanResponse response = getElasticSearchClient().exists(request);
            result = response.value();
        } catch (IOException e) {
            result = false;
        }
        // log.info("== {} 文档是否存在: {}", result);
        return result;
    }


    /**
     * 删除相关 index
     * @param indexName, Name of th index
     * @return true if found, false otherwise
     * @throws IOException，if something happened
     */
    public boolean deleteIndex(String indexName) throws IOException {
        boolean result;
        try {
            DeleteIndexResponse deleteIndexResponse = getElasticSearchClient().indices().delete(d -> d.index(indexName));
            result = deleteIndexResponse.acknowledged();
        } catch (IOException e) {
             // log.info("delete index message", e.getMessage(), e);
            result = false;
        }
         // log.info("== {} index 是否被删除: {}", result);
        return result;

    }

    /**
     *
     * 创建index (不指定Mapping)
     * @param indexName Name of the index (needed)
     * @return true if created successfully, false otherwise
     * @throws IOException, if something happened
     */
    public boolean createIndex(String indexName) throws IOException {
        boolean result;
        try {
            CreateIndexResponse createIndexResponse = getElasticSearchClient()
                    .indices().create(createRequest -> createRequest.index(indexName));
            result = createIndexResponse.acknowledged();
        } catch (IOException e) {
            result = false;
        }
        // log.info("== {} index 是否被创建成功(不指定mapping的): {}", result);
        return result;
    }

    // 处理mapping
    public Property dealProperties(String name) {
        switch (name) {
            case "keyword":
                return new Property(new KeywordProperty.Builder().build());
            default:
                return  new Property(new TextProperty.Builder().build());
        }
    }

    // 指定mapping创建index
    public Boolean createIndexWithMapping(String indexName) throws IOException {
        // 获取resource下对应的配置
        String getSetting = getReadJsonFile().readJsonSetting(
                CommonUtil.getResourceFilePath(COURSE_SETTING_JSON)
        );
        JSONObject settingObject = JSONObject.parseObject(getSetting);
        // 获取properties
        JSONObject properties = settingObject.getJSONObject(MAPPINGS).getJSONObject(PROPERTIES);
        //定义文档属性
        Map<String, Property> propertyMap = new HashMap<>();

        properties.keySet().forEach(key -> propertyMap.put(key, dealProperties(KEYWORD)));

        // 设置索引的文档类型映射
        TypeMapping typeMapping = new TypeMapping.Builder()
                .properties(propertyMap)
                .build();
        CreateIndexRequest request = new CreateIndexRequest.Builder()
                .index(indexName)
                .mappings(typeMapping)
                .build();
        CreateIndexResponse createIndexResponse = getElasticSearchClient().indices().create(request);
        return createIndexResponse.acknowledged();
    }


    // 查看索引相关的信息
    public HashMap<String, Object> indexDetail(String indexName) throws IOException {
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put(Constants.INDEX_NAME, indexName);
        try {
            GetIndexResponse getIndexResponse = getElasticSearchClient().indices()
                    .get(getRequest -> getRequest.index(indexName));

            Map<String, Property> propertyMap = Objects.requireNonNull(getIndexResponse.get(indexName).mappings()).properties();
            hashMap.put(Constants.MESSAGE, propertyMap.keySet());
        } catch (IOException e) {
            hashMap.put(Constants.MESSAGE, e.getMessage());
        }
        return hashMap;
    }

    // 添加文档
    public void  addNewDocument() {

    }

    //获取文档信息
    public void getDocumentInfo() {

    }

    // 更新文档
    public void updateDocument() {

    }

    // 删除文档
    public void deleteDocument() {

    }

    // 批量插入文档
    public void bulkInsertDocument() {
        // 获取所有数据
        List<Object> list = getIndexCourseService().getCourseData();
        List<BulkOperation> bulkOperationArrayList = new ArrayList<>();
    }

}
