package com.qinlei.retrofitutils.call;

import android.os.AsyncTask;


import com.qinlei.retrofitutils.callback.BaseCallback;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by qinlei
 * Created on 2018/1/16
 * Created description : retrofit2.Call 的封装，将 parseNetworkResponse（）交由子线程处理
 */

public class RequestCall {
    private Call call;
    private BaseCallback mBaseCallback;

    public BaseCallback getBaseCallback() {
        return mBaseCallback;
    }

    public RequestCall(Call call) {
        this.call = call;
    }

    public void execute(BaseCallback baseCallback) {
        if (baseCallback == null) {
            baseCallback = BaseCallback.CALLBACK_DEFAULT;
        }
        mBaseCallback = baseCallback;
        mBaseCallback.onBefore(call);
        call.enqueue(new Callback() {
            @Override
            public void onResponse(final Call call, final Response response) {
                try {
                    if (mBaseCallback.validateReponse(response)) {
                        //将请求成功的部分交由子线程处理
                        new Task(call, mBaseCallback, response).execute();
                    } else {
                        mBaseCallback.onError(call, new Throwable("request failed , reponse's code is : " + response.code()));
                        mBaseCallback.onAfter(call);
                    }

                } catch (Throwable throwable) {
                    mBaseCallback.onError(call, throwable);
                    mBaseCallback.onAfter(call);
                }
            }

            @Override
            public void onFailure(Call call, Throwable t) {
                if (call.isCanceled()) {
                    mBaseCallback.onError(call, new Throwable("Canceled!"));
                } else {
                    mBaseCallback.onError(call, t);
                }

                mBaseCallback.onAfter(call);
            }
        });
    }

    public void cancel() {
        if (call != null && !call.isCanceled()) {
            call.cancel();
        }
    }

    static class Task extends AsyncTask<Void, Integer, Object> {

        private Call call;
        private BaseCallback baseCallback;
        private Response response;

        public Task(Call call, BaseCallback baseCallback, Response response) {
            this.call = call;
            this.baseCallback = baseCallback;
            this.response = response;
        }

        @Override
        protected Object doInBackground(Void... voids) {
            try {
                return baseCallback.parseNetworkResponse(response);
            } catch (Throwable throwable) {
                return throwable;
            }
        }

        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);
            if (o instanceof Throwable) {
                baseCallback.onError(call, (Throwable) o);
            } else {
                baseCallback.onResponse(call, o);
            }
            baseCallback.onAfter(call);
        }
    }
}
