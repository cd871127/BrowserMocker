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
        map.put("1", new URL("http://www.google.com"));
        map.put("2", new URL("http://www.163.com"));
        map.put("3", new URL("http://www.sodu.cc"));
        map.put("4", new URL("http://www.sina.com"));
        Map<String, String> map2 = m.get(map);

        System.out.println(map2.get("1"));
        System.out.println(map2.get("2").length());
        System.out.println(map2.get("3").length());
        System.out.println(map2.get("4").length());
    }

//    static class B extends A
//    {
//
//    }
}


