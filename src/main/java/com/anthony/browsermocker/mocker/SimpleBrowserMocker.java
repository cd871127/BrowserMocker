package com.anthony.browsermocker.mocker;

import com.anthony.browsermocker.processor.HttpResponseProcessor;
import org.apache.http.Consts;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Map;

/**
 * Created by chend on 2017/6/16.
 */
public class SimpleBrowserMocker<T> extends AbstractBrowserMocker<T> {

    private Logger logger = LoggerFactory.getLogger(getClass());


    SimpleBrowserMocker() {
    }

    SimpleBrowserMocker(CloseableHttpClient httpClient) {
        super(httpClient);
    }

    SimpleBrowserMocker(CloseableHttpClient httpClient, HttpResponseProcessor<T> processor) {
        super(httpClient, processor);
    }

    @Override
    public T get(URL url, Map<String, String> parameters) {
        List<NameValuePair> paramList = parameterMapToList(parameters);
        String paramStr = "";
        try {
            if (null != paramList)
                paramStr = EntityUtils.toString(new UrlEncodedFormEntity(paramList, Consts.UTF_8));
        } catch (IOException e) {
            e.printStackTrace();
        }
        HttpGet httpGet = new HttpGet(url.toString() + paramStr);
        return execute(httpGet);
    }


    @Override
    public T get(URL url) {
        return get(url, null);
    }

    @Override
    public T post(URL url, Map<String, String> parameters) {
        HttpPost httpPost = new HttpPost(url.toString());
        List<NameValuePair> paramList = parameterMapToList(parameters);
        if (null != paramList)
            httpPost.setEntity(new UrlEncodedFormEntity(paramList, Consts.UTF_8));
        execute(httpPost);
        return null;
    }

    @Override
    public T post(URL url) {
        return post(url, null);
    }

    public static <T> SimpleBrowserMockerBuilder<T> builder() {
        return new SimpleBrowserMockerBuilder<>();
    }

    public static class SimpleBrowserMockerBuilder<T> extends BrowserMockerBuilder<T> {

        public SimpleBrowserMocker<T> build() {
            return new SimpleBrowserMocker<>(getHttpClient(), this.processor);
        }

    }
}
