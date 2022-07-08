package com.example.appluck_integration_guide;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

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
}