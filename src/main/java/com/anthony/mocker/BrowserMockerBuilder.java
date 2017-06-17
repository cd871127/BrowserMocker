package com.anthony.mocker;

import org.apache.http.HttpHost;
import org.apache.http.client.HttpClient;
import org.apache.http.client.HttpRequestRetryHandler;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.conn.routing.HttpRoute;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.protocol.HttpContext;

import java.io.IOException;

/**
 * Created by chend on 2017/6/16.
 */
public abstract class BrowserMockerBuilder {
    private HttpHost proxy = null;
    private PoolingHttpClientConnectionManager cm;
    private int socketTimeout = 10000;
    private int connectTimeout = 5000;
    private CloseableHttpClient httpClient;
    private int retryCount = 0;

    BrowserMockerBuilder() {
        cm = new PoolingHttpClientConnectionManager();
        cm.setMaxTotal(200);
        cm.setDefaultMaxPerRoute(20);
        HttpHost localhost = new HttpHost("locahost", 80);
        cm.setMaxPerRoute(new HttpRoute(localhost), 50);
    }

    public BrowserMockerBuilder setProxy(final String hostname, final int port, final String scheme) {
        return setProxy(hostname, port, scheme, null, null);
    }

    private BrowserMockerBuilder setProxy(final String hostname, final int port, final String scheme, final String username, final String password) {
        this.proxy = new HttpHost(hostname, port, scheme);
        return this;
    }

    public BrowserMockerBuilder setSocketTimeout(final int socketTimeout) {
        this.socketTimeout = socketTimeout;
        return this;
    }

    public BrowserMockerBuilder setConnectTimeout(final int connectTimeout) {
        this.connectTimeout = connectTimeout;
        return this;
    }

    public BrowserMockerBuilder setRetryCount(final int retryCount) {
        this.retryCount = retryCount;
        return this;
    }

    CloseableHttpClient getHttpClient() {
        RequestConfig requestConfig = RequestConfig.custom()
                .setProxy(this.proxy)
                .setSocketTimeout(this.socketTimeout).setConnectTimeout(this.connectTimeout).build();
        if (this.retryCount != 0) {
            this.httpClient = HttpClients.custom()
                    .setConnectionManager(this.cm)
                    .setDefaultRequestConfig(requestConfig)
                    .setRetryHandler((exception, executionCount, context) -> executionCount <= this.retryCount)
                    .build();
        } else {
            this.httpClient = HttpClients.custom()
                    .setConnectionManager(this.cm)
                    .setDefaultRequestConfig(requestConfig)
                    .build();
        }
        return this.httpClient;
    }


    public abstract BasicBrowserMocker build();
}
