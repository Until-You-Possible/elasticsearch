package com.example.es.client;

import co.elastic.clients.json.jackson.JacksonJsonpMapper;
import co.elastic.clients.transport.ElasticsearchTransport;
import co.elastic.clients.transport.rest_client.RestClientTransport;
import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;

public class ElasticSearchClient {

    // create low level client
    String ip = "localhost";
    Integer port = 9200;
    HttpHost httpHost = new HttpHost(ip, port);
    RestClient httpclient = RestClient.builder(httpHost).build();

    // create the high level client
    ElasticsearchTransport transport = new RestClientTransport(httpclient, new JacksonJsonpMapper());

    // create client
    ElasticSearchClient client = new ElasticSearchClient(transport);

    public ElasticSearchClient(ElasticsearchTransport transport) {
    }
}
