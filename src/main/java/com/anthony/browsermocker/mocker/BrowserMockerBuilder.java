package com.anthony.browsermocker.mocker;

import com.anthony.browsermocker.processor.HttpResponseProcessor;
import org.apache.http.HttpHost;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.conn.routing.HttpRoute;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;

import java.util.Map;

/**
 * Created by chend on 2017/6/16.
 */
public abstract class BrowserMockerBuilder<T> {
    HttpHost proxy = null;
    private PoolingHttpClientConnectionManager cm;
    int socketTimeout = 10000;
    int connectTimeout = 5000;
    CloseableHttpClient httpClient;
    int retryCount = 0;
    HttpResponseProcessor<T> processor;
    Map<String, String> headers;

    BrowserMockerBuilder() {
        cm = new PoolingHttpClientConnectionManager();
        cm.setMaxTotal(200);
        cm.setDefaultMaxPerRoute(20);
        HttpHost localhost = new HttpHost("locahost", 80);
        cm.setMaxPerRoute(new HttpRoute(localhost), 50);
    }

    public BrowserMockerBuilder<T> setHeaders(Map<String, String> headers) {
        this.headers = headers;
        return this;
    }

    public BrowserMockerBuilder<T> setProcessor(HttpResponseProcessor<T> processor) {
        this.processor = processor;
        return this;
    }

    public BrowserMockerBuilder<T> setHttpClient(CloseableHttpClient httpClient) {
        this.httpClient = httpClient;
        return this;
    }

    public BrowserMockerBuilder<T> setProxy(final String hostname, final int port, final String scheme) {
        return setProxy(hostname, port, scheme, null, null);
    }

    private BrowserMockerBuilder<T> setProxy(final String hostname, final int port, final String scheme, final String username, final String password) {
        this.proxy = new HttpHost(hostname, port, scheme);
        return this;
    }

    public BrowserMockerBuilder<T> setSocketTimeout(final int socketTimeout) {
        this.socketTimeout = socketTimeout;
        return this;
    }

    public BrowserMockerBuilder<T> setConnectTimeout(final int connectTimeout) {
        this.connectTimeout = connectTimeout;
        return this;
    }

    public BrowserMockerBuilder<T> setRetryCount(final int retryCount) {
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
                    .setRetryHandler((exception, executionCount, context) -> executionCount < this.retryCount)
                    .build();
        } else {
            this.httpClient = HttpClients.custom()
                    .setConnectionManager(this.cm)
                    .setDefaultRequestConfig(requestConfig)
                    .build();
        }
        return this.httpClient;
    }

    public abstract BasicBrowserMocker<T> build();
}
