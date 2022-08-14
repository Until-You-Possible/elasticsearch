package com.example.es.service;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch._types.mapping.Property;
import co.elastic.clients.elasticsearch.core.ExistsRequest;
import co.elastic.clients.elasticsearch.indices.CreateIndexRequest;
import co.elastic.clients.elasticsearch.indices.CreateIndexResponse;
import co.elastic.clients.elasticsearch.indices.DeleteIndexResponse;
import co.elastic.clients.elasticsearch.indices.GetIndexResponse;
import co.elastic.clients.transport.endpoints.BooleanResponse;
import com.alibaba.fastjson.JSONObject;
import com.example.es.client.ElasticSearchClient;
import com.example.es.core.Constants;
import com.example.es.util.readSetting.ReadJsonFile;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import static com.example.es.core.Constants.COURSE_SETTING_JSON;

public class ElasticSearchService {

    private ElasticsearchClient elasticsearchClient;

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

    // 指定mapping创建index
    public void createIndexWithMapping() {
        // 获取相关mapping
        String getSetting = getReadJsonFile().readJsonSetting(COURSE_SETTING_JSON);
        CreateIndexRequest request = new CreateIndexRequest("ddd");
//        CreateIndexResponse createIndexResponse = getElasticSearchClient().indices().create(createRequest ->
//                        createRequest.mappings(JSONObject.parseObject(getSetting))
//                );
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

    }

}
