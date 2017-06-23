package com.anthony.browsermocker.processor;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by chend on 2017/6/23.
 */
public abstract class AbstractResponseProcessor<T> implements HttpResponseProcessor<T> {
    @Override
    public T process(CloseableHttpResponse response) {
        return process(response, null);
    }

    protected Map<String, String> requestParam(Map param) {
        if (!("get").equals(param.get("method")))
            return null;
        HttpGet httpGet = (HttpGet) param.get("httpRequestBase");
        String queryStr = httpGet.getURI().getRawQuery();
        if (null == queryStr)
            return null;
        Map<String, String> resMap = new HashMap<>();
        String[] keyAndValues = queryStr.split("&");
        for (String keyAndValue : keyAndValues) {
            String[] singKeyAndValue = keyAndValue.split("=");
            resMap.put(singKeyAndValue[0], singKeyAndValue[1]);
        }
        return resMap;
    }
}
