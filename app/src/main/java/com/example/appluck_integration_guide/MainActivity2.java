package com.example.appluck_integration_guide;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.webkit.WebViewClientCompat;

import org.apache.commons.lang3.StringUtils;

public class MainActivity2 extends AppCompatActivity {

    private WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        webView = findViewById(R.id.webview);
        webView.setWebViewClient(new WebViewClientCompat(){
            @Override
            public boolean shouldOverrideUrlLoading(@NonNull WebView view, @NonNull WebResourceRequest request) {
                String url = request.getUrl().toString();
                if(StringUtils.startsWith(url,"market:")){
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.setData(Uri.parse(url));
                    startActivity(intent);
                    return true;
                }
                return super.shouldOverrideUrlLoading(view, request);
            }
        });
        webView.loadUrl(CommonUtils.slotUrlWithGaid);
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true); // 允许javascript执行
        webSettings.setDomStorageEnabled(true);// 打开本地缓存提供JS调用,至关重要，开启DOM缓存，开启LocalStorage存储
    }


    @Override
    public void onBackPressed() {
        //拦截系统返回事件
        if(webView.canGoBack()){
            webView.goBack();
            return;
        }
        super.onBackPressed();
    }
}