package com.anthony.browsermocker.mocker;

import com.anthony.browsermocker.processor.HttpResponseProcessor;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.impl.client.CloseableHttpClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URL;
import java.util.ArrayList;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Created by chend on 2017/6/16.
 */
public class MultiThreadBrowserMocker<T> extends SimpleBrowserMocker<T> {

    private Logger logger = LoggerFactory.getLogger(getClass());

    private ExecutorService es;

    private MockerThread<T> mockerThread;

    private int threadCount = 1;


    MultiThreadBrowserMocker(CloseableHttpClient httpClient, HttpResponseProcessor<T> processor, int threadCount) {
        super(httpClient, processor);
        this.threadCount = threadCount;
    }


    public T get(ArrayList<URL> urlList) {
        es = Executors.newFixedThreadPool(threadCount);
        ArrayList<HttpRequestBase> taskList = new ArrayList<>();
//        taskList.add(urlList.forEach((k)->new HttpGet(k.toString())));
        for (URL url : urlList) {
            taskList.add(new HttpGet(url.toString()));
        }
        MockerThread<String> thread = new MockerThread<>(taskList);
        es.submit(thread);
        es.shutdown();

        return null;
    }

    public static <T> MultiThreadBrowserMockerBuilder<T> builder() {
        return new MultiThreadBrowserMocker.MultiThreadBrowserMockerBuilder<>();
    }

    public static class MultiThreadBrowserMockerBuilder<K> extends SimpleBrowserMockerBuilder<K> {
        private int threadCount = 1;

        @Override
        public MultiThreadBrowserMocker<K> build() {
            return new MultiThreadBrowserMocker<>(getHttpClient(), this.processor, threadCount);
        }

        public BrowserMockerBuilder<K> setThreadCount(int threadCount) {
            this.threadCount = threadCount;
            return this;
        }
    }

    private class MockerThread<V> extends SimpleBrowserMocker<V> implements Callable<V> {

        private LinkedBlockingQueue<HttpRequestBase> taskList;

        public MockerThread(ArrayList<HttpRequestBase> taskList) {
            this.taskList = new LinkedBlockingQueue<>(taskList);
        }

        @Override
        public V call() throws Exception {
            HttpRequestBase task;
            while ((task = taskList.poll()) != null) {
                System.out.println(execute(task));
            }
            return null;
//            return execute(httpRequestBase);
        }
    }

}
