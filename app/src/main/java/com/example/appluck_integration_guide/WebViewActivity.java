package com.example.appluck_integration_guide;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.webkit.WebViewClientCompat;

import org.apache.commons.lang3.StringUtils;

public class WebViewActivity extends AppCompatActivity {
    private WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view);
        webView = findViewById(R.id.webview);
        webView.setWebViewClient(new WebViewClientCompat() {
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
        });
        //下载处理
        webView.setDownloadListener((url, userAgent, contentDisposition, mimetype, contentLength) -> {
            Uri uri = Uri.parse(url);
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            startActivity(intent);
        });
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true); // 允许javascript执行
        webSettings.setDomStorageEnabled(true);// 打开本地缓存提供JS调用,至关重要，开启DOM缓存，开启LocalStorage存储
        webView.loadUrl(CommonUtils.slotUrlWithGaid);
    }
}