package com.anthony.browsermocker.mocker;

import com.anthony.browsermocker.processor.SimpleResponseProcessor;
import org.junit.Test;

import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by chend on 2017/6/16.
 */
public class SimpleBrowserMockerTest {
    @Test
    public void t() throws MalformedURLException {


        MultiThreadBrowserMocker<String> s = (MultiThreadBrowserMocker<String>) MultiThreadBrowserMocker.<String>builder().setProcessor(new SimpleResponseProcessor()).build();
        String ss = null;
        for (int i = 0; i != 10; ++i) {
            ss = s.get(new URL("http://www.baidu.com"));
        }
        System.out.println(ss);
    }
//    static class B extends A
//    {
//
//    }
}

class A {
    public A getThis() {
        return this;
    }
}

class B extends A {
    public B getThis() {
        return this;
    }
}


