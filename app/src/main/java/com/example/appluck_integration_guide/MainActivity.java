package com.example.appluck_integration_guide;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button btn_webView = findViewById(R.id.btn_webView);
        Button btn_chrome = findViewById(R.id.btn_chrome);
        if (StringUtils.isBlank(CommonUtils.slotUrlWithGaid)) {
            new Thread(() -> {
                String url = CommonUtils.getUrl(getApplicationContext());
                CommonUtils.setSlotUrlWithGaid(url);
            }).start();
        }
        btn_webView.setOnClickListener(v -> {
            // 第一个为当前类的上下文参数，不能直接使用this，需要使用类名.this
            // 第二个参数为目标文件的反射对象
            Intent intent = new Intent(MainActivity.this, MainActivity2.class);
            // 第一个参数为intent
            startActivity(intent);
        });

        btn_chrome.setOnClickListener(v -> {
            // 第一个为当前类的上下文参数，不能直接使用this，需要使用类名.this
            // 第二个参数为目标文件的反射对象
            Uri uri = Uri.parse(CommonUtils.slotUrlWithGaid);
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            // 第一个参数为intent
            startActivity(intent);
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        WebViewClient webViewClient = new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(@NonNull WebView view, @NonNull WebResourceRequest request) {
                String url = request.getUrl().toString();
                Log.d("url", url);
                //支持google play
                if (StringUtils.startsWith(url, "market:")
                        || StringUtils.startsWith(url, "https://play.google.com/store/")
                        || StringUtils.startsWith(url, "http://play.google.com/store/")) {
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.setData(Uri.parse(url));
                    startActivity(intent);
                    return true;
                }
                return super.shouldOverrideUrlLoading(view, request);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                Log.e("webViewClient.","onPageFinished....");
            }
        };
        DownloadListener downloadListener = new DownloadListener() {
            @Override
            public void onDownloadStart(String url, String userAgent, String contentDisposition, String mimetype, long contentLength) {
                Uri uri = Uri.parse(url);
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
            }
        };
        WebViewUtil.init(getApplicationContext(),webViewClient,downloadListener);
        WebViewUtil.preload(CommonUtils.slotUrlWithGaid);
        Log.e("main.","onStart....");
    }
}