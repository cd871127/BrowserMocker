package com.anthony.browsermocker.mocker;

import com.anthony.browsermocker.processor.HttpResponseProcessor;
import org.apache.http.NameValuePair;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HttpContext;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by chend on 2017/6/19.
 */
public abstract class AbstractBrowserMocker<T> implements BasicBrowserMocker<T> {
    protected CloseableHttpClient httpClient;
    protected HttpResponseProcessor<T> processor;

    protected AbstractBrowserMocker() {
    }

    protected AbstractBrowserMocker(CloseableHttpClient httpClient) {
        this.httpClient = httpClient;
    }

    protected AbstractBrowserMocker(CloseableHttpClient httpClient, HttpResponseProcessor<T> processor) {
        this.httpClient = httpClient;
        this.processor = processor;
    }

    protected T execute(HttpRequestBase httpRequestBase) {
        HttpContext context = HttpClientContext.create();
        CloseableHttpResponse response;
        T result = null;
        try {
            response = httpClient.execute(httpRequestBase, context);
            result = processor.process(response);
            response.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    protected List<NameValuePair> parameterMapToList(Map<String, String> parameters) {
        if (null == parameters)
            return null;
        List<NameValuePair> list = new ArrayList<>();
        parameters.forEach((k, v) -> list.add(new BasicNameValuePair(k, v)));
        return list;
    }


}
