package com.example.es.client;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.json.jackson.JacksonJsonpMapper;
import co.elastic.clients.transport.ElasticsearchTransport;
import co.elastic.clients.transport.rest_client.RestClientTransport;
import com.example.es.core.Constants;
import com.example.es.util.readSetting.ReadAccountMessage;
import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.nio.client.HttpAsyncClientBuilder;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestClientBuilder;

public class ElasticSearchClient {

    ReadAccountMessage readAccountMessage;

    private  ReadAccountMessage getReadAccountMessage() {
        if (readAccountMessage == null) {
            return readAccountMessage = new ReadAccountMessage();
        }
        return readAccountMessage;
    }

    private static final String Address  = "localhost";
    private static final Integer port    = 9200;


    public ElasticsearchClient getClient() {

//        String username = getUserName();
//        String password = getPassword();
//        final CredentialsProvider credentialsProvider = new BasicCredentialsProvider();
//        UsernamePasswordCredentials credentials = new UsernamePasswordCredentials(username, password);
//        credentialsProvider.setCredentials(AuthScope.ANY, credentials);
//        HttpHost httpHost = new HttpHost(username, port);
//        RestClientBuilder builder = RestClient.builder(httpHost)
//                .setHttpClientConfigCallback(new RestClientBuilder.HttpClientConfigCallback() {
//                    @Override
//                    public HttpAsyncClientBuilder customizeHttpClient(
//                            HttpAsyncClientBuilder httpClientBuilder) {
//                        return httpClientBuilder
//                                .setDefaultCredentialsProvider(credentialsProvider);
//                    }
//                });

         // Create the low-level client
        HttpHost httpHost = new HttpHost(Address, port);
        RestClient restClient = RestClient.builder(httpHost).build();
        ElasticsearchTransport transport = new RestClientTransport(
                restClient, new JacksonJsonpMapper());

        return new ElasticsearchClient(transport);
    }

    public String getUserName() {
        return getReadAccountMessage().getAccountInformation().get(Constants.USERNAME);
    }

    public String getPassword() {
        return getReadAccountMessage().getAccountInformation().get(Constants.PASSWORD);
    }

}

