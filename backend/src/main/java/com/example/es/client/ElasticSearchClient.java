package com.example.es.client;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.json.jackson.JacksonJsonpMapper;
import co.elastic.clients.transport.ElasticsearchTransport;
import co.elastic.clients.transport.rest_client.RestClientTransport;
import com.example.es.core.Constants;
import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.elasticsearch.client.RestClient;

import java.util.List;

public class ElasticSearchClient {

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

}

class ElasticSearchOptions {
    public static final String KEY_HOSTS = "hosts";

    public static final String KEY_USERNAME = "username";

    public static final String KEY_PASSWORD = "password";

    public static final String KEY_HOSTNAME = "hostname";

    public static final String KEY_PORT = "port";

    public static final String KEY_SCHEME = "scheme";

    private static final String ELASTICSEARCH = "elasticsearch";

    private final String username;

    private final String password;

    private final List<HttpHost> hosts;

    ElasticSearchOptions(String username, String password, List<HttpHost> hosts) {
        this.username = username;
        this.password = password;
        this.hosts = hosts;
    }

    public List<HttpHost> getHostsList() {
        return this.hosts;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }
}