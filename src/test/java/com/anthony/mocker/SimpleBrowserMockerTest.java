package com.anthony.mocker;

import org.apache.http.Consts;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by chend on 2017/6/16.
 */
public class SimpleBrowserMockerTest {
    @Test
    public void t() {

        List<NameValuePair> params = new ArrayList<>();
        params.add(new BasicNameValuePair("word", "maven"));
        params.add(new BasicNameValuePair("word1", "maven1"));

        try {
            String paramStr= EntityUtils.toString(new UrlEncodedFormEntity(params, Consts.UTF_8));
            System.out.println(paramStr);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}