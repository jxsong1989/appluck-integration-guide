package com.example.appluck_integration_guide.util;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.util.Log;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.webkit.DownloadListener;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import org.apache.commons.lang3.StringUtils;

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

    public static void openUrl(String url, Context context) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        ActivityInfo activityInfo = intent.resolveActivityInfo(context.getPackageManager(), 0);
        if (activityInfo.exported) {
            intent.setData(Uri.parse(url));
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        }
    }

    public static void openBrowser(String url, Context context) {
        if (isAppInstalled("com.android.chrome", context)) {
            // 创建一个 Intent，指定 ACTION_VIEW 动作和 URL
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
            // 指定要使用的浏览器的包名
            intent.setPackage("com.android.chrome"); // Chrome 浏览器的包名
            // 启动 Chrome 浏览器来处理链接
            context.startActivity(intent);
        } else {
            // 如果没有安装 Chrome 浏览器，使用系统默认浏览器打开链接
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
            // 启动默认浏览器来处理链接
            context.startActivity(intent);
        }
    }

    public static boolean openIntent(String url, Context context) {
        if (StringUtils.startsWith(url, "market:")
                || StringUtils.startsWith(url, "https://play.google.com/store/")
                || StringUtils.startsWith(url, "http://play.google.com/store/")) {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            if (StringUtils.startsWith(url, "market://details?id=")) {
                final String replace = StringUtils.replace(url, "market://details", "https://play.google.com/store/apps/details");
                intent.setData(Uri.parse(replace));
            } else {
                intent.setData(Uri.parse(url));
            }
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
            return true;
        } else if (!StringUtils.startsWith(url, "http://")
                && !StringUtils.startsWith(url, "https://")) {
            openUrl(url, context);
            return true;
        } else if (StringUtils.contains(url, "lz_open_browser=1")) {
            openBrowser(url, context);
            return true;
        }
        return false;
    }

    public static boolean isAppInstalled(String packageName, Context context) {
        if (StringUtils.isEmpty(packageName)) {
            return false;
        }
        try {
            context.getPackageManager().getPackageInfo(packageName, 0);
            return true;
        } catch (PackageManager.NameNotFoundException e) {
            return false;
        }
    }
}
