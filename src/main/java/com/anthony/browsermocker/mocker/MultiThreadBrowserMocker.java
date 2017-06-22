package com.anthony.browsermocker.mocker;

import com.anthony.browsermocker.processor.HttpResponseProcessor;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.impl.client.CloseableHttpClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Created by chend on 2017/6/16.
 */
public class MultiThreadBrowserMocker<T> extends SimpleBrowserMocker<T> {

    private Logger logger = LoggerFactory.getLogger(getClass());

    private ExecutorService es;

    private int threadCount = 1;

    MultiThreadBrowserMocker(CloseableHttpClient httpClient, HttpResponseProcessor<T> processor, int threadCount) {
        super(httpClient, processor);
        this.threadCount = threadCount;
    }

    public Map<String, T> get(Map<String, URL> urls) {
        es = Executors.newFixedThreadPool(threadCount);

        Map<String, HttpRequestBase> taskMap = new HashMap<>();
        for (Map.Entry<String, URL> e : urls.entrySet()) {
            taskMap.put(e.getKey(), new HttpGet(e.getValue().toString()));
        }
        MockerThread mockerThread = new MockerThread(taskMap);
        Future[] futures = new Future[threadCount];

        for (int i = 0; i != threadCount; ++i)
            futures[i] = es.submit(mockerThread);
        es.shutdown();

        while (!es.isTerminated()) {
            try {
                Thread.sleep(200);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        Map<String, T> resMap = new HashMap<>();
        for (Map.Entry<String, T> e : mockerThread.getResList()) {
            resMap.put(e.getKey(), e.getValue());
        }
        return resMap;
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

    private class MockerThread implements Runnable {

        private LinkedBlockingQueue<Map.Entry<String, HttpRequestBase>> taskList;

        private LinkedBlockingQueue<Map.Entry<String, T>> resList;

        public MockerThread(Map<String, HttpRequestBase> taskMap) {
            this.resList = new LinkedBlockingQueue<>();
            this.taskList = new LinkedBlockingQueue<>();
            this.taskList.addAll(taskMap.entrySet());
        }

        public LinkedBlockingQueue<Map.Entry<String, T>> getResList() {
            return resList;
        }

        @Override
        public void run() {
            Map.Entry<String, HttpRequestBase> task;
            Map<String, T> resMap = new HashMap<>();
            while ((task = taskList.poll()) != null) {
                resMap.put(task.getKey(), execute(task.getValue()));
            }
            resList.addAll(resMap.entrySet());
        }
    }

}
