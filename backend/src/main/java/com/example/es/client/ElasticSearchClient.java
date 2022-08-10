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
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestClientBuilder;

import java.io.FileNotFoundException;
import java.util.List;

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
    HttpHost httpHost = new HttpHost(Address, port);


    public ElasticsearchClient getClient() {

        final CredentialsProvider credentialsProvider = new BasicCredentialsProvider();
        UsernamePasswordCredentials credentials = new UsernamePasswordCredentials(Constants.USERNAME, Constants.PASSWORD);
        credentialsProvider.setCredentials(AuthScope.ANY, credentials);

        // Create the low-level client
        RestClient restClient = RestClient.builder(httpHost).build();

        ElasticsearchTransport transport = new RestClientTransport(
                restClient, new JacksonJsonpMapper());

        return new ElasticsearchClient(transport);
    }

    public  RestClientBuilder restClientBuilder() throws FileNotFoundException {
        String username = getReadAccountMessage().getAccountInformation().get("username");
        String password = getReadAccountMessage().getAccountInformation().get("password");
        final CredentialsProvider credentialsProvider = new BasicCredentialsProvider();
        UsernamePasswordCredentials credentials = new UsernamePasswordCredentials(username, password);
        credentialsProvider.setCredentials(AuthScope.ANY, credentials);
    }

}

