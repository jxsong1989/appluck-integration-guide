package com.example.appluck_integration_guide;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.appluck_integration_guide.util.WebViewUtil;

public class WebViewPreLoadActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(WebViewUtil.webView);
    }

    @Override
    public void onBackPressed() {
        //拦截系统返回事件
        if (WebViewUtil.webView.canGoBack()) {
            WebViewUtil.webView.goBack();
            return;
        }
        super.onBackPressed();
    }

}