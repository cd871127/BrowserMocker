package com.anthony.browsermocker.mocker;

import com.anthony.browsermocker.processor.HttpResponseProcessor;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.impl.client.CloseableHttpClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URL;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;

/**
 * Created by chend on 2017/6/16.
 */
public class MultiThreadBrowserMocker<T> extends SimpleBrowserMocker<T> {

    private Logger logger = LoggerFactory.getLogger(getClass());

    private ExecutorService es;

    private MockerThread<T> mockerThread;


    MultiThreadBrowserMocker() {
    }

    MultiThreadBrowserMocker(CloseableHttpClient httpClient) {
        super(httpClient);
    }

    MultiThreadBrowserMocker(CloseableHttpClient httpClient, HttpResponseProcessor<T> processor) {
        super(httpClient, processor);
    }

    @Override
    public T get(URL url) {
        MockerThread<T> mockerThread = new MockerThread<>(new HttpGet(url.toString()));
        T res = null;
        try {
            res = mockerThread.call();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return res;
    }

    public static <T> MultiThreadBrowserMockerBuilder<T> builder() {
        return new MultiThreadBrowserMocker.MultiThreadBrowserMockerBuilder<>();
    }

    public static class MultiThreadBrowserMockerBuilder<K> extends SimpleBrowserMockerBuilder<K> {
        @Override
        public MultiThreadBrowserMocker<K> build() {
            return new MultiThreadBrowserMocker<>(getHttpClient(), this.processor);
        }
    }

    private class MockerThread<V> extends SimpleBrowserMocker<V> implements Callable<V> {

        private HttpRequestBase httpRequestBase;

        public MockerThread(HttpRequestBase httpRequestBase) {
            this.httpRequestBase = httpRequestBase;
        }

        @Override
        public V call() throws Exception {
            return execute(httpRequestBase);
        }
    }

}
