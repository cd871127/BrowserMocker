package com.anthony.browsermocker.processor;

import org.apache.http.client.methods.CloseableHttpResponse;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Map;

/**
 * Created by chend on 2017/6/19.
 */
public class SimpleResponseProcessor extends AbstractResponseProcessor<String> {

    @Override
    public String process(CloseableHttpResponse response,Map param) {
        StringBuilder stringBuilder = new StringBuilder();
        try {
            InputStream in = response.getEntity().getContent();
            BufferedReader br = new BufferedReader(new InputStreamReader(in));
            String tmp;
            while ((tmp = br.readLine()) != null) {
                stringBuilder.append(tmp).append("\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        String result = stringBuilder.toString();
        return "".equals(result) ? null : result;
    }
}
