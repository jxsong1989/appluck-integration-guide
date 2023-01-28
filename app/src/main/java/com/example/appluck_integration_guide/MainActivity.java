package com.example.appluck_integration_guide;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.webkit.DownloadListener;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.appluck_integration_guide.util.WebViewUtil;

import org.apache.commons.lang3.StringUtils;

public class MainActivity extends AppCompatActivity {
    private Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button btn_webView_preLoad = findViewById(R.id.btn_webView_preload);
        Button btn_chrome = findViewById(R.id.btn_chrome);
        Button tbn_webView = findViewById(R.id.btn_webview);
        if (StringUtils.isBlank(CommonUtils.slotUrlWithGaid)) {
            new Thread(() -> {
                String url = CommonUtils.getUrl(getApplicationContext());
                CommonUtils.setSlotUrlWithGaid(url);
                //打开预加载web view
                btn_webView_preLoad.setOnClickListener(v -> {
                    Intent intent = new Intent(MainActivity.this, WebViewPreLoadActivity.class);
                    startActivity(intent);
                });

                //打开web view
                tbn_webView.setOnClickListener(v -> {
                    Intent intent = new Intent(MainActivity.this, WebViewActivity.class);
                    startActivity(intent);
                });

                //打开浏览器
                btn_chrome.setOnClickListener(v -> {
                    Uri uri = Uri.parse(CommonUtils.slotUrlWithGaid);
                    Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                    // 第一个参数为intent
                    startActivity(intent);
                });
                Log.e("main.", "onCreate....");
            }).start();
        }
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        Log.e("main.", "onWindowFocusChanged...." + hasFocus);
        if (hasFocus) {
            handler.post(() -> {
                WebViewClient webViewClient = new WebViewClient() {
                    @Override
                    public boolean shouldOverrideUrlLoading(@NonNull WebView view, @NonNull WebResourceRequest request) {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                            String url = request.getUrl().toString();
                            Log.d("url", url);
                            //支持google play
                            try {
                                //支持google play
                                if (StringUtils.startsWith(url, "market:")
                                        || StringUtils.startsWith(url, "https://play.google.com/store/")
                                        || StringUtils.startsWith(url, "http://play.google.com/store/")) {
                                    Intent intent = new Intent(Intent.ACTION_VIEW);
                                    intent.setData(Uri.parse(url));
                                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                    startActivity(intent);
                                    return true;
                                } else if (!StringUtils.startsWith(url, "http://")
                                        && !StringUtils.startsWith(url, "https://")) {
                                    Intent intent = new Intent(Intent.ACTION_VIEW);
                                    ActivityInfo activityInfo = intent.resolveActivityInfo(getPackageManager(), 0);
                                    if (activityInfo.exported) {
                                        intent.setData(Uri.parse(url));
                                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                        startActivity(intent);
                                    }
                                    return true;
                                }
                            } catch (Throwable e) {
                                return true;
                            }
                        }
                        return super.shouldOverrideUrlLoading(view, request);
                    }

                    @Override
                    public void onPageFinished(WebView view, String url) {
                        super.onPageFinished(view, url);
                        if (view.getProgress() < 100) {
                            return;
                        }
                        Log.e("webViewClient.onPageFinished", view.getProgress() + "  " + url);
                        Button btn_webView = findViewById(R.id.btn_webView_preload);
                        int visibility = btn_webView.getVisibility();
                        Log.e("visibility", visibility + "");
                        //目标页面加载完成后展示组件
                        if (visibility == View.INVISIBLE && StringUtils.contains(url, "/dist/")) {
                            try {
                                Thread.sleep(10);
                                btn_webView.setVisibility(View.VISIBLE);
                            } catch (Exception e) {

                            }
                        }
                    }
                };
                //支持下载事件
                DownloadListener downloadListener = new DownloadListener() {
                    @Override
                    public void onDownloadStart(String url, String userAgent, String contentDisposition, String mimetype, long contentLength) {
                        Uri uri = Uri.parse(url);
                        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                        startActivity(intent);
                    }
                };
                WebViewUtil.init(getApplicationContext(), webViewClient, downloadListener);
                WebViewUtil.preload(CommonUtils.slotUrlWithGaid);
            });
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
    }
}