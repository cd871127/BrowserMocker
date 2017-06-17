package com.anthony.mocker;

import org.apache.http.Consts;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by chend on 2017/6/16.
 */
public class SimpleBrowserMocker implements BasicBrowserMocker {

    private Logger logger = LoggerFactory.getLogger(getClass());
    private CloseableHttpClient httpClient;

    SimpleBrowserMocker(CloseableHttpClient httpClient) {
        this.httpClient = httpClient;
    }

    private void excute(HttpRequestBase httpRequestBase) {
        HttpContext context = HttpClientContext.create();
        CloseableHttpResponse response;
        try {
            response = httpClient.execute(httpRequestBase, context);
            System.out.println(response.getStatusLine());
            InputStreamReader inputStreamReader = new InputStreamReader(response.getEntity().getContent());
//            HttpEntity entity=response.getEntity();
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            String tmp;
            while ((tmp = bufferedReader.readLine()) != null)
                System.out.println(tmp);
            response.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void get(URL url, Map<String, String> parameters) {
        List<NameValuePair> paramList = parameterMapToList(parameters);
        String paramStr = "";
        try {
            if (null != paramList)
                paramStr = EntityUtils.toString(new UrlEncodedFormEntity(paramList, Consts.UTF_8));

        } catch (IOException e) {
            e.printStackTrace();
        }
        HttpGet httpGet = new HttpGet(url.toString() + paramStr);
        excute(httpGet);
    }

    public void get(URL url) {
        get(url, null);
    }


    public void post(URL url, Map<String, String> parameters) {
        HttpPost httpPost = new HttpPost(url.toString());
        List<NameValuePair> paramList = parameterMapToList(parameters);
        if (null != paramList)
            httpPost.setEntity(new UrlEncodedFormEntity(paramList, Consts.UTF_8));
        excute(httpPost);
    }

    public void post(URL url) {
        post(url, null);
    }

    private List<NameValuePair> parameterMapToList(Map<String, String> parameters) {
        if (null == parameters)
            return null;
        List<NameValuePair> list = new ArrayList<>();
        parameters.forEach((k, v) -> list.add(new BasicNameValuePair(k, v)));
        return list;
    }


    public static SimpleBrowserMockerBuilder builer() {
        return new SimpleBrowserMockerBuilder();
    }

    public static class SimpleBrowserMockerBuilder extends BrowserMockerBuilder {
        @Override
        public SimpleBrowserMocker build() {
            return new SimpleBrowserMocker(getHttpClient());
        }
    }
}
