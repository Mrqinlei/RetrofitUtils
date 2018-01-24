package com.qinlei.retrofitutils.callback;

import android.os.Handler;
import android.os.Message;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import okhttp3.ResponseBody;
import retrofit2.Response;

/**
 * Created by qinlei
 * Created on 2018/1/15
 * Created description : 处理文件下载回调
 */

public abstract class FileCallBack extends BaseCallback<File> {
    private static final int DOWNLOAD = 101010;
    private static final int MAX_PROGRESS = 100;
    private File file;
    private Handler handler = new MyHandler();

    public FileCallBack(File dir, String fileName) {
        if (!dir.exists()) {
            dir.mkdirs();
        }
        file = new File(dir.getAbsolutePath() + "/" + fileName);
    }

    @Override
    public File parseNetworkResponse(final Response response) throws Throwable {
        writeFileToSDCard((ResponseBody) response.body());
        return file;
    }

    private boolean writeFileToSDCard(ResponseBody body) {
        try {
            InputStream inputStream = null;
            OutputStream outputStream = null;
            try {
                byte[] fileReader = new byte[4096];
                long fileSize = body.contentLength();
                long fileSizeDownloaded = 0;
                inputStream = body.byteStream();
                outputStream = new FileOutputStream(file);
                while (true) {
                    int read = inputStream.read(fileReader);
                    if (read == -1) {
                        break;
                    }
                    outputStream.write(fileReader, 0, read);
                    fileSizeDownloaded += read;
                    Message message = new Message();
                    message.what = DOWNLOAD;
                    message.obj = (int) (fileSizeDownloaded * MAX_PROGRESS / fileSize);
                    handler.sendMessage(message);
                }
                outputStream.flush();
                return true;
            } catch (IOException e) {
                return false;
            } finally {
                if (inputStream != null) {
                    inputStream.close();
                }
                if (outputStream != null) {
                    outputStream.close();
                }
            }
        } catch (IOException e) {
            return false;
        }
    }

    class MyHandler extends Handler {
        private int progress;

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (progress != (int) msg.obj) {
                inProgress((int) msg.obj, MAX_PROGRESS);
                progress = (int) msg.obj;
            }
        }
    }
}
