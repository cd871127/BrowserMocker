package com.anthony.browsermocker.processor;

import org.apache.http.client.methods.CloseableHttpResponse;

import java.util.Map;

/**
 * Created by chend on 2017/6/18.
 */
public interface HttpResponseProcessor<T> {
    T process(CloseableHttpResponse response);

    T process(CloseableHttpResponse response, Map param);
}
