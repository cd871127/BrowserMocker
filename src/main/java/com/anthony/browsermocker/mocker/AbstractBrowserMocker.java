package com.anthony.browsermocker.mocker;

import com.anthony.browsermocker.processor.HttpResponseProcessor;
import org.apache.http.NameValuePair;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HttpContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by chend on 2017/6/19.
 */
public abstract class AbstractBrowserMocker<T> implements BasicBrowserMocker<T> {
    private Logger logger= LoggerFactory.getLogger(getClass());
    protected CloseableHttpClient httpClient;
    protected HttpResponseProcessor<T> processor;
    protected Map<String,String> headers=null;

    protected AbstractBrowserMocker() {
    }

    protected AbstractBrowserMocker(CloseableHttpClient httpClient) {
        this.httpClient = httpClient;
    }

    protected AbstractBrowserMocker(CloseableHttpClient httpClient, HttpResponseProcessor<T> processor,Map<String,String> headers) {
        this.httpClient = httpClient;
        this.processor = processor;
        this.headers = headers;
    }

    protected T execute(HttpRequestBase httpRequestBase) {
        HttpContext context = HttpClientContext.create();
        CloseableHttpResponse response;
        T result = null;
        try {
            response = httpClient.execute(httpRequestBase, context);
            Map<String, Object> param = new HashMap<>();
            param.put("httpRequestBase", httpRequestBase);
            if (httpRequestBase instanceof HttpPost)
                param.put("method", "post");
            else if (httpRequestBase instanceof HttpGet)
                param.put("method", "get");
            result = processor.process(response, param);
            response.close();
        } catch (IOException e) {
            if(e instanceof SocketTimeoutException)
                logger.warn(httpRequestBase.getURI().toString() +" time out");
            else
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
