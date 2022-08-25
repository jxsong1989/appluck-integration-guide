package com.example.appluck_integration_guide.util;

import android.content.Context;
import android.util.Log;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.webkit.DownloadListener;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class WebViewUtil {

    public static WebView webView;

    public static void init(Context context, WebViewClient webViewClient, DownloadListener downloadListener) {
        if (webView != null) {
            ViewParent parent = webView.getParent();
            if (parent != null && parent instanceof ViewGroup) {
                ((ViewGroup) parent).removeView(webView);
            }
            return;
        }
        webView = new WebView(context);
        webView.setWebViewClient(webViewClient);
        //下载处理
        webView.setDownloadListener(downloadListener);
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true); // 允许javascript执行
        webSettings.setDomStorageEnabled(true);// 打开本地缓存提供JS调用,至关重要，开启DOM缓存，开启LocalStorage存储
        Log.e("WebViewUtil.", "init....");
    }

    public static void preload(String url) {
        webView.loadUrl(url);
        Log.e("WebViewUtil.", "preload....");
    }
}
