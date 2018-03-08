package com.qinlei.retrofitutils;


import com.qinlei.retrofitutils.call.RequestCall;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.WeakHashMap;

import okhttp3.RequestBody;

/**
 * Created by qinlei
 * Created on 2018/1/12
 * Created description : 网络请求的构建
 */

public class HttpBuilder {
    private Object tag;
    private String requestUrl;
    private WeakHashMap<String, Object> params = new WeakHashMap<>();
    private String content;
    private List<FileUpload> uploads = new ArrayList<>();
    private String mediaType;

    public HttpBuilder tag(Object tag) {
        this.tag = tag;
        return this;
    }

    public HttpBuilder url(String url) {
        this.requestUrl = url;
        return this;
    }

    public HttpBuilder addParams(String key, Object value) {
        params.put(key, value);
        return this;
    }

    public HttpBuilder addFiles(String key, File value) {
        uploads.add(new FileUpload(key, value));
        return this;
    }

    public HttpBuilder addBody(String jsonBody) {
        content = jsonBody;
        return this;
    }

    public HttpBuilder addBody(String otherBody, String mediaType) {
        content = otherBody;
        this.mediaType = mediaType;
        return this;
    }

    public RequestCall build() {
        RetrofitUtils httpClient = new RetrofitUtils(tag, requestUrl, params, content, uploads, mediaType);
        return httpClient.build();
    }
}
