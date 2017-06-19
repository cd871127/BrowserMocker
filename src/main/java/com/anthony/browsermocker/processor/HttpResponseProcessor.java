package com.anthony.browsermocker.processor;

import org.apache.http.client.methods.CloseableHttpResponse;

/**
 * Created by chend on 2017/6/18.
 */
public interface HttpResponseProcessor<T> {
    T process(CloseableHttpResponse response);
}
