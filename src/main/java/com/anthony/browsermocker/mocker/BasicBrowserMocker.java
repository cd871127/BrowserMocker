package com.anthony.browsermocker.mocker;

import java.net.URL;
import java.util.Map;

/**
 * Created by chend on 2017/6/16.
 */
public interface BasicBrowserMocker<T> {

    T get(URL url, Map<String, String> parameters);

    T post(URL url, Map<String, String> parameters);

    T get(URL url);

    T post(URL url);

}
