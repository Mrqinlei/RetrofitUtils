package com.qinlei.retrofitutilsdemo;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.qinlei.retrofitutils.HttpCreator;
import com.qinlei.retrofitutils.RetrofitUtils;
import com.qinlei.retrofitutils.callback.FileCallBack;
import com.qinlei.retrofitutils.callback.StringCallBack;
import com.qinlei.takephoto.TakePhotoCallback;
import com.qinlei.takephoto.TakePhotoUtils;

import java.io.File;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Call;

public class MainActivity extends AppCompatActivity {

    // BASE_URL 需要 "/" 结尾
    public static final String BASE_URL = "https://easy-mock.com/mock/5a58707f3dcb200788d2995b/huban/";
    // 自定义配置 OkHttpClient
    public static final OkHttpClient OK_HTTP_CLIENT = new OkHttpClient.Builder()
            .connectTimeout(10000L, TimeUnit.MILLISECONDS)
            .readTimeout(10000L, TimeUnit.MILLISECONDS)
            .build();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initRetrofitUtils();
    }

    /**
     * 初始化 RetrofitUtils
     */
    private void initRetrofitUtils() {
        HttpCreator.init(BASE_URL, OK_HTTP_CLIENT);
    }

    private void toast(String msg) {
        Toast.makeText(MainActivity.this, msg, Toast.LENGTH_SHORT).show();
    }

    /**
     * get 请求模拟
     *
     * @param view
     */
    public void get(View view) {
        RetrofitUtils.get()
                .url("query")
                .addParams("name", "bbbb")
                .build()
                .execute(new StringCallBack() {
                    @Override
                    public void onError(Call call, Throwable e) {
                        toast(e.getMessage());
                    }

                    @Override
                    public void onResponse(Call call, String response) {
                        toast(response);
                    }
                });
    }

    /**
     * 模拟 post 请求
     *
     * @param view
     */
    public void post(View view) {
        RetrofitUtils.post()
                .url("postParam")
                .addParams("name", "bbbb")
                .build()
                .execute(new StringCallBack() {
                    @Override
                    public void onError(Call call, Throwable e) {
                        toast(e.getMessage());
                    }

                    @Override
                    public void onResponse(Call call, String response) {
                        toast(response);
                    }
                });
    }

    /**
     * 模拟 postRaw 请求
     *
     * @param view
     */
    public void postRaw(View view) {
        HashMap hashMap = new HashMap();
        hashMap.put("fileName", "qinlei");

        RetrofitUtils.postRaw()
                .url("postRaw")
                .addBody(JSON.toJSONString(hashMap))
                .build()
                .execute(new StringCallBack() {
                    @Override
                    public void onError(Call call, Throwable e) {
                        toast(e.getMessage());
                    }

                    @Override
                    public void onResponse(Call call, String response) {
                        toast(response);
                        Log.d("qinlei", "onResponse: "+response);
                    }
                });

        String xml = "<xml></xml>";
        RetrofitUtils.postRaw()
                .url("https://api.mch.weixin.qq.com/pay/unifiedorder")
                .addBody(xml, "application/xml;charset=UTF-8")
                .build()
                .execute(new StringCallBack() {
                    @Override
                    public void onError(Call call, Throwable e) {
                        toast(e.getMessage());
                    }

                    @Override
                    public void onResponse(Call call, String response) {
                        toast(response);
                        Log.d("qinlei", "onResponse: "+response);
                    }
                });
    }

    /**
     * 模拟 put 请求
     *
     * @param view
     */
    public void put(View view) {
        RetrofitUtils.put()
                .url("putParam")
                .tag(this)
                .addParams("name", "bbbb")
                .build()
                .execute(new StringCallBack() {
                    @Override
                    public void onError(Call call, Throwable e) {
                        toast(e.getMessage());
                    }

                    @Override
                    public void onResponse(Call call, String response) {
                        toast(response);
                    }
                });
    }

    /**
     * 模拟 putRaw 请求
     *
     * @param view
     */
    public void putRaw(View view) {
        HashMap hashMap = new HashMap();
        hashMap.put("fileName", "qinlei");
        RetrofitUtils.putRaw()
                .url("putRaw")
                .addBody(JSON.toJSONString(hashMap))
                .build()
                .execute(new StringCallBack() {
                    @Override
                    public void onError(Call call, Throwable e) {
                        toast(e.getMessage());
                    }

                    @Override
                    public void onResponse(Call call, String response) {
                        toast(response);
                    }
                });
    }

    /**
     * 模拟 delete 请求
     *
     * @param view
     */
    public void delete(View view) {
        toast("没有测试的服务器");
    }

    /**
     * 模拟 upload 请求
     *
     * @param view
     */
    public void upload(View view) {
        TakePhotoUtils.getInstance().with(this).setTakePhotoCallback(new TakePhotoCallback() {
            @Override
            public void takeSuccess(File file) {
                RetrofitUtils.upload()
                        .url("http://192.168.31.236/TruckAlliance/userInterface/v1/uploadBatch")
                        .addFiles("file", file)
                        .addFiles("file", file)
                        .build()
                        .execute(new StringCallBack() {
                            ProgressDialog progressDialog;

                            @Override
                            public void onBefore(Call call) {
                                super.onBefore(call);
                                progressDialog = new ProgressDialog(MainActivity.this);
                                progressDialog.setTitle("上传图片");
                                progressDialog.setMessage("上传图片");
                                progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                                progressDialog.setCancelable(false);
                                progressDialog.setCanceledOnTouchOutside(false);
                                progressDialog.show();
                            }

                            @Override
                            public void inProgress(int progress, int progressMax) {
                                super.inProgress(progress, progressMax);
                                progressDialog.setMax(progressMax);
                                progressDialog.setProgress(progress);
                            }

                            @Override
                            public void onError(Call call, Throwable e) {
                                toast(e.getMessage());
                            }

                            @Override
                            public void onResponse(Call call, String response) {
                                Log.d("qinlei", "onResponse: " + response);
                                toast("上传完成");
                            }

                            @Override
                            public void onAfter(Call call) {
                                super.onAfter(call);
                                progressDialog.dismiss();
                            }
                        });
            }

            @Override
            public void takeFail(String msg) {
                toast("选择相册失败");
            }

            @Override
            public void takeCancel() {
                toast("选择相册取消");

            }
        }).chooseGallery();
    }

    /**
     * 模拟 download 请求
     *
     * @param view
     */
    public void download(View view) {
        //需要请求权限
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            download();
        } else {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    1000);
        }
    }

    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 1000: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    download();
                } else {

                }
                return;
            }
        }
    }

    private void download() {
        RetrofitUtils
                .download()
                .url("http:\\\\zhstatic.zhihu.com\\pkg\\store\\daily\\zhihu-daily-zhihu-2.6.0(744)-release.apk")
                .build()
                .execute(new FileCallBack(getExternalFilesDir(DOWNLOAD_SERVICE), "知乎日报.apk") {
                    ProgressDialog progressDialog;

                    @Override
                    public void onBefore(Call call) {
                        super.onBefore(call);
                        progressDialog = new ProgressDialog(MainActivity.this);
                        progressDialog.setTitle("下载apk");
                        progressDialog.setMessage("下载");
                        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                        progressDialog.setCancelable(false);
                        progressDialog.setCanceledOnTouchOutside(false);
                        progressDialog.show();
                    }

                    @Override
                    public void inProgress(int progress, int progressMax) {
                        super.inProgress(progress, progressMax);
                        progressDialog.setMax(progressMax);
                        progressDialog.setProgress(progress);
                    }

                    @Override
                    public void onError(Call call, Throwable e) {
                        toast(e.getMessage());
                    }

                    @Override
                    public void onResponse(Call call, File response) {
                        toast("下载完成");
                    }

                    @Override
                    public void onAfter(Call call) {
                        super.onAfter(call);
                        progressDialog.dismiss();
                    }
                });
    }
}
