package com.anthony.browsermocker.mocker;

import com.anthony.browsermocker.processor.SimpleResponseProcessor;
import org.junit.Test;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by chend on 2017/6/16.
 */
public class SimpleBrowserMockerTest {
    @Test
    public void t() throws MalformedURLException {
        SimpleBrowserMocker<String> bm=SimpleBrowserMocker.<String>builder().setProcessor(new SimpleResponseProcessor()).build();
        Map<String,String> map=new HashMap<>();
        map.put("word","aaaa");
        System.out.println(bm.get(new URL("http://www.baidu.com/s?"),map));
    }



}
