package com.qinlei.retrofitutils.callback;

import retrofit2.Call;
import retrofit2.Response;

/**
 * Created by qinlei
 * Created on 2018/1/16
 * Created description :
 */

public abstract class BaseCallback<T> {

    public void onBefore(Call call) {

    }

    public void onAfter(Call call) {

    }

    public void inProgress(int progress, int progressMax) {

    }

    public boolean validateReponse(Response response) {
        return response.isSuccessful();
    }

    /**
     * Thread Pool Thread
     *
     * @param response
     */
    public abstract T parseNetworkResponse(Response response) throws Throwable;

    public abstract void onError(Call call, Throwable e);

    public abstract void onResponse(Call call, T response);

    public static BaseCallback CALLBACK_DEFAULT = new BaseCallback() {
        @Override
        public Object parseNetworkResponse(Response response) {
            return null;
        }

        @Override
        public void onError(Call call, Throwable e) {

        }

        @Override
        public void onResponse(Call call, Object response) {

        }
    };
}
