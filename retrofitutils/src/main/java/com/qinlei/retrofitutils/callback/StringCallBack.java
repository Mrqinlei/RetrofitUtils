package com.qinlei.retrofitutils.callback;

import retrofit2.Response;

/**
 * Created by qinlei
 * Created on 2018/1/15
 * Created description : 基础回调
 */

public abstract class StringCallBack extends BaseCallback<String> {
    @Override
    public String parseNetworkResponse(Response response) throws Throwable {
        return response.body().toString();
    }
}
