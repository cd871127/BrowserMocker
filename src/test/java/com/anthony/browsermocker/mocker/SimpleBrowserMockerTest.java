package com.anthony.browsermocker.mocker;

import org.junit.Test;

import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by chend on 2017/6/16.
 */
public class SimpleBrowserMockerTest {
    @Test
    public void t() throws MalformedURLException {
        A a = new B();
        B b= ((B) a.getThis());

        SimpleBrowserMocker<String> s= (SimpleBrowserMocker<String>) SimpleBrowserMocker.<String>builder().setHttpClient(null).build();
//        s.get(new URL("http://www.baidu.com"));

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


