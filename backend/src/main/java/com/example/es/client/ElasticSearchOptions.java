package com.example.es.client;

import com.example.es.util.readSetting.ReadAccountMessage;
import org.apache.http.HttpHost;

import java.util.List;

public class ElasticSearchOptions {

    ReadAccountMessage readAccountMessage;

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

    private ReadAccountMessage getReadAccountMessage() {
        if (readAccountMessage == null) {
            return readAccountMessage = new ReadAccountMessage();
        }
        return readAccountMessage;
    }

    ElasticSearchOptions(String username, String password, List<HttpHost> hosts) {
        this.username = username;
        this.password = password;
        this.hosts = hosts;
    }

    public List<HttpHost> getHostsList() {
        return this.hosts;
    }

    public String getUsername() {
        return  username;
    }

    public String getPassword() {
        return password;
    }
}
