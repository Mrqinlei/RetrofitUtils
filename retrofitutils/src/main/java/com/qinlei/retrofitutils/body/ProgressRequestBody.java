package com.qinlei.retrofitutils.body;

import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;

import java.io.IOException;
import java.lang.ref.WeakReference;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import okio.Buffer;
import okio.BufferedSink;
import okio.ForwardingSink;
import okio.Okio;
import okio.Sink;

/**
 * Created by qinlei
 * Created on 2018/1/16
 * Created description : 处理文件上传回调
 */

public class ProgressRequestBody extends RequestBody {
    //实际的待包装请求体
    private RequestBody requestBody;
    //进度回调接口
    protected ProgressRequestListener progressListener;
    //包装完成的BufferedSink
    private BufferedSink bufferedSink;
    protected static int MAX_PROGRESS = 100;
    private int UPLOAD = 111111;
    private Handler myHandler;

    /**
     * 构造函数，赋值
     *
     * @param requestBody      待包装的请求体
     * @param progressListener 回调接口
     */
    public ProgressRequestBody(RequestBody requestBody, ProgressRequestListener progressListener) {
        this.requestBody = requestBody;
        this.progressListener = progressListener;
        myHandler = new MyHandler(this);
    }

    /**
     * 重写调用实际的响应体的contentType
     *
     * @return MediaType
     */
    @Override
    public MediaType contentType() {
        return requestBody.contentType();
    }

    /**
     * 重写调用实际的响应体的contentLength
     *
     * @return contentLength
     * @throws IOException 异常
     */
    @Override
    public long contentLength() throws IOException {
        return requestBody.contentLength();
    }

    /**
     * 重写进行写入
     *
     * @param sink BufferedSink
     * @throws IOException 异常
     */
    @Override
    public void writeTo(@NonNull BufferedSink sink) throws IOException {
        if (bufferedSink == null) {
            //包装
            bufferedSink = Okio.buffer(sink(sink));

        }
        //写入
        requestBody.writeTo(bufferedSink);
        //必须调用flush，否则最后一部分数据可能不会被写入
        bufferedSink.flush();


    }

    /**
     * 写入，回调进度接口
     *
     * @param sink Sink
     * @return Sink
     */
    private Sink sink(Sink sink) {
        return new ForwardingSink(sink) {
            //当前写入字节数
            long bytesWritten = 0L;
            //总字节长度，避免多次调用contentLength()方法
            long contentLength = 0L;

            @Override
            public void write(@NonNull Buffer source, long byteCount) throws IOException {
                super.write(source, byteCount);
                if (contentLength == 0) {
                    //获得contentLength的值，后续不再调用
                    contentLength = contentLength();
                }
                //增加当前写入的字节数
                bytesWritten += byteCount;
                //回调
                if (progressListener != null) {
                    Message message = new Message();
                    message.what = UPLOAD;
                    message.obj = (int) (bytesWritten * MAX_PROGRESS / contentLength);
                    myHandler.sendMessage(message);
                }
            }
        };
    }

    static class MyHandler extends Handler {
        private WeakReference<ProgressRequestBody> mProgressRequestBody;
        private int progress;

        public MyHandler(ProgressRequestBody mProgressRequestBody) {
            this.mProgressRequestBody = new WeakReference<ProgressRequestBody>(mProgressRequestBody);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (progress != (int) msg.obj) {
                ProgressRequestBody progressRequestBody = mProgressRequestBody.get();
                progressRequestBody.progressListener.onRequestProgress((int) msg.obj, MAX_PROGRESS);
                progress = (int) msg.obj;
            }
        }
    }

    public interface ProgressRequestListener {
        void onRequestProgress(int progress, int progressMax);
    }
}