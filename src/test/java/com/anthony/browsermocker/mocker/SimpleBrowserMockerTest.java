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

        MultiThreadBrowserMocker<String> m = (MultiThreadBrowserMocker<String>) MultiThreadBrowserMocker.<String>builder().setThreadCount(6).setProcessor(new SimpleResponseProcessor()).build();
        Map<String, URL> map = new HashMap<>();
        map.put("1", new URL("http://www.baidu.com/s?"));
        map.put("2", new URL("http://www.baidu.com"));
        map.put("3", new URL("http://www.sodu.cc"));
        map.put("4", new URL("http://www.sina.com"));
        Map<String, Map<String, String>> map3 = new HashMap<>();

        Map<String, String> map4 = new HashMap<>();
        map4.put("word", "3333");
        map3.put("1", map4);

        Map<String, String> map2 = m.get(map, map3);

        System.out.println(map2.get("1").length());
        System.out.println(map2.get("2").length());
        System.out.println(map2.get("3").length());
        System.out.println(map2.get("4").length());
    }

//    static class B extends A
//    {
//
//    }
}


