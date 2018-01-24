package com.qinlei.retrofitutils;


import com.qinlei.retrofitutils.service.HttpService;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.scalars.ScalarsConverterFactory;

/**
 * Created by qinlei
 * Created on 2018/1/12
 * Created description : 初始化
 */

public class HttpCreator {
    protected static String baseUrl;
    protected static OkHttpClient okHttpClient = null;

    public static void init(String baseUrl, OkHttpClient okHttpClient) {
        if (baseUrl==null||"".equals(baseUrl)) {
            throw new RuntimeException("baseUrl is null");
        }
        HttpCreator.baseUrl = baseUrl;
        HttpCreator.okHttpClient = okHttpClient;
    }

    /**
     * 构建全局Retrofit客户端
     */
    private static final class RetrofitHolder {
        private static final String BASE_URL = baseUrl;
        private static final Retrofit RETROFIT_CLIENT = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(okHttpClient)
                .addConverterFactory(ScalarsConverterFactory.create())
                .build();
    }

    private static final class RestServiceHolder {
        private static final HttpService REST_SERVICE =
                RetrofitHolder.RETROFIT_CLIENT.create(HttpService.class);
    }

    public static HttpService getRestService() {
        return RestServiceHolder.REST_SERVICE;
    }
}
