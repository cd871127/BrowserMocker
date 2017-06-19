package com.anthony.browsermocker.mocker;

import com.anthony.browsermocker.processor.HttpResponseProcessor;
import org.apache.http.HttpHost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;

/**
 * Created by chend on 2017/6/16.
 */
public class MultiThreadBrowserMocker<T> extends SimpleBrowserMocker<T> implements Callable<T> {

    private Logger logger = LoggerFactory.getLogger(getClass());

    private ExecutorService es;

    private SimpleBrowserMocker<T> simpleBrowserMocker;

    MultiThreadBrowserMocker() {
    }

    MultiThreadBrowserMocker(CloseableHttpClient httpClient) {
        super(httpClient);
    }

    MultiThreadBrowserMocker(CloseableHttpClient httpClient, HttpResponseProcessor<T> processor) {
        super(httpClient, processor);
    }

    @Override
    public T call() throws Exception {

        return null;
    }


    public static <T> MultiThreadBrowserMockerBuilder<T> builder() {
        return new MultiThreadBrowserMocker.MultiThreadBrowserMockerBuilder<>();
    }

    public static class MultiThreadBrowserMockerBuilder<K> extends SimpleBrowserMockerBuilder<K> {
        @Override
        public MultiThreadBrowserMocker<K> build() {
            return new MultiThreadBrowserMocker<>(getHttpClient(), this.processor);
        }

        public MultiThreadBrowserMockerBuilder<K> setProcessor(HttpResponseProcessor<K> processor) {
            this.processor = processor;
            return this;
        }

        public MultiThreadBrowserMockerBuilder<K> setHttpClient(CloseableHttpClient httpClient) {
            this.httpClient = httpClient;
            return this;
        }

        public MultiThreadBrowserMockerBuilder<K> setProxy(final String hostname, final int port, final String scheme) {
            return setProxy(hostname, port, scheme, null, null);
        }

        private MultiThreadBrowserMockerBuilder<K> setProxy(final String hostname, final int port, final String scheme, final String username, final String password) {
            this.proxy = new HttpHost(hostname, port, scheme);
            return this;
        }

        public MultiThreadBrowserMockerBuilder<K> setSocketTimeout(final int socketTimeout) {
            this.socketTimeout = socketTimeout;
            return this;
        }

        public MultiThreadBrowserMockerBuilder<K> setConnectTimeout(final int connectTimeout) {
            this.connectTimeout = connectTimeout;
            return this;
        }

        public MultiThreadBrowserMockerBuilder<K> setRetryCount(final int retryCount) {
            this.retryCount = retryCount;
            return this;
        }
    }


}
