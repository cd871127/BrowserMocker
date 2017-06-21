package com.anthony.browsermocker.mocker;

import org.junit.Test;

import java.net.MalformedURLException;
import java.util.ArrayList;

/**
 * Created by chend on 2017/6/16.
 */
public class SimpleBrowserMockerTest {
    @Test
    public void t() throws MalformedURLException {

        ArrayList<String> a = new ArrayList<>();
        for (int i = 0; i != 10; ++i) {
            a.add(String.valueOf(i));
        }

//        a.forEach((i)-> {return new Integer(i);});
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


