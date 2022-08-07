package com.example.es.client;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.json.jackson.JacksonJsonpMapper;
import co.elastic.clients.transport.ElasticsearchTransport;
import co.elastic.clients.transport.rest_client.RestClientTransport;
import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;

import java.lang.ref.Cleaner;

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

    public Boolean existIndex() {
        return createClient().exists(s -> {

        });
    }
}
