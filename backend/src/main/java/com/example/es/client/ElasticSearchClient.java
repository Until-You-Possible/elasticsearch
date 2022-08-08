package com.example.es.client;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch.core.ExistsRequest;
import co.elastic.clients.elasticsearch.indices.CreateIndexResponse;
import co.elastic.clients.elasticsearch.indices.DeleteIndexResponse;
import co.elastic.clients.elasticsearch.indices.GetIndexRequest;
import co.elastic.clients.json.jackson.JacksonJsonpMapper;
import co.elastic.clients.transport.ElasticsearchTransport;
import co.elastic.clients.transport.endpoints.BooleanResponse;
import co.elastic.clients.transport.rest_client.RestClientTransport;
import org.apache.http.HttpHost;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;

import java.io.IOException;

public class ElasticSearchClient {

    private static final String Address  = "localhost";
    private static final Integer port    = 9200;
    HttpHost httpHost = new HttpHost(Address, port);


    public ElasticsearchClient createClient() {

        // Create the low-level client
        RestClient restClient = RestClient.builder(httpHost).build();

        ElasticsearchTransport transport = new RestClientTransport(
                restClient, new JacksonJsonpMapper());

        return new ElasticsearchClient(transport);
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
            BooleanResponse booleanResponse = createClient().indices()
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
            BooleanResponse response = createClient().exists(request);
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
            DeleteIndexResponse deleteIndexResponse = createClient().indices().delete(d -> d.index(indexName));
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
            CreateIndexResponse createIndexResponse = createClient().indices().create(createRequest -> createRequest.index(indexName));
            result = createIndexResponse.acknowledged();
        } catch (IOException e) {
            result = false;
        }
        // log.info("== {} index 是否被创建成功(不指定mapping的): {}", result);
        return result;
    }

}