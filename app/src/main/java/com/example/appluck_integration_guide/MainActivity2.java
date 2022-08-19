package com.example.appluck_integration_guide;

import android.os.Bundle;
import android.webkit.WebView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.appluck_integration_guide.util.WebViewUtil;

public class MainActivity2 extends AppCompatActivity {

    private WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_main2);
        setContentView(WebViewUtil.webView);
        /*webView = findViewById(R.id.webview);
        webView.setWebViewClient(new WebViewClientCompat() {
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
        });
        //下载处理
        webView.setDownloadListener((url, userAgent, contentDisposition, mimetype, contentLength) -> {
            Uri uri = Uri.parse(url);
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            startActivity(intent);
        });
        webView.loadUrl(CommonUtils.slotUrlWithGaid);
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true); // 允许javascript执行
        webSettings.setDomStorageEnabled(true);// 打开本地缓存提供JS调用,至关重要，开启DOM缓存，开启LocalStorage存储
*/
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