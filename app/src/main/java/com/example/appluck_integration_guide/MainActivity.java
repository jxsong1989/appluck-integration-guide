package com.example.appluck_integration_guide;

import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;

import com.spin.ok.gp.OkSpin;
import com.spin.ok.gp.utils.Error;

import org.apache.commons.lang3.StringUtils;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private final static String pid = "8947";

    private final static String key = "BtghmH6IBjnaWmjMzyJhVPGZqFXF6qx4";

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
        try {
            getPackageManager().getPackageInfo("123123",0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        List<ApplicationInfo> installedApplications = getPackageManager().getInstalledApplications(0);
        Log.e("installedApplications-----------------------","");
        installedApplications.forEach(e->{
            System.err.println(e.name);
        });
        Log.e("installedApplications-----------------------","");

        System.err.println();
        List<PackageInfo> installedPackages = getPackageManager().getInstalledPackages(0);
        Log.e("installedPackages-----------------------","");
        installedPackages.forEach(e->{
            System.err.println(e.packageName);
        });
        Log.e("installedPackages-----------------------","");
        OkSpin.setListener(new OkSpin.SpinListener() {

            @Override
            public void onInitSuccess() {
                //初始化成功
                Log.d("OkSpin", "onInitSuccess");
                OkSpin.loadIcon(pid);
            }

            @Override
            public void onInitFailed(Error error) {
                //初始化失败
                Log.d("OkSpin", "onInitFailed: " + error);
            }

            @Override
            public void onIconReady(String placement) {
                //Icon广告位Ready
                Log.d("OkSpin", "onIconReady: " + placement);
                if (OkSpin.isIconReady(pid)) {
                    LinearLayout mLinearLayout =  findViewById(R.id.linear_layout);
                    View iconView = OkSpin.showIcon(pid);

                    if (iconView != null && mLinearLayout.indexOfChild(iconView) == -1){
                        iconView.setLayoutParams(new ViewGroup.LayoutParams(200, 200));
                        mLinearLayout.addView(iconView);
                    }
                }
            }

            public void onIconLoadFailed(String placement, Error error) {
                //Icon广告位加载失败
                Log.e("OkSpin", "onIconLoadFailed: " + error);
            }

            public void onIconShowFailed(String placementId, Error error) {
                //Icon广告位展示失败
                Log.d("OkSpin", "onIconShowFailed:" + placementId);
            }

            @Override
            public void onIconClick(String placement) {
                //Icon广告位被点击
                Log.d("OkSpin", "onIconClick: " + placement);
            }

            @Override
            public void onInteractiveOpen(String placement) {
                //互动页面被打开
                Log.d("OkSpin", "onInteractiveOpen: "+placement);
            }

            public void onInteractiveOpenFailed(String placementId, Error error) {
                //互动页面打开失败
                Log.d("OkSpin", "onInteractiveOpenFailed: " + placementId);
            }

            @Override
            public void onInteractiveClose(String placement) {
                //互动页面被关闭
                Log.d("OkSpin", "onInteractiveClose: " + placement);
            }

            @Override
            public void onOfferWallOpen(String placementId) {
                //积分墙页面被打开
                Log.d("OkSpin", "onOfferWallOpen: " + placementId);
            }

            public void onOfferWallOpenFailed(String placementId, Error error) {
                //积分墙页面打开失败
                Log.d("OkSpin", "onOfferWallOpenFailed: " + placementId);
            }

            @Override
            public void onOfferWallClose(String placementId) {
                //积分墙页面关闭
                Log.d("OkSpin", "onOfferWallClose: " + placementId);
            }

            /**
             * v2.3.0 增加
             * GSpace页面打开
             */
            @Override
            public void onGSpaceOpen(String placementId) {
                Log.d("OkSpin", "onGSpaceOpen: " + placementId);
            }

            /**
             * v2.3.0 增加
             * GSpace页面打开失败
             */
            public void onGSpaceOpenFailed(String placementId, Error error) {
                Log.d("OkSpin", "onGSpaceOpenFailed: " + placementId);
            }

            /**
             * v2.3.0 增加
             * GSpace页面关闭
             */
            @Override
            public void onGSpaceClose(String placementId) {
                Log.d("OkSpin", "onGSpaceClose: " + placementId);
            }

            /**
             * v2.0.3 增加
             * 用户交互行为回调
             * INTERACTIVE_PLAY         互动页面互动
             * INTERACTIVE_CLICK        互动页面点击广告
             * OFFER_WALL_SHOW_DETAIL   积分墙页面展示Offer详情
             * OFFER_WALL_GET_TASK      积分墙页面领取Offer
             */
            @Override
            public void onUserInteraction(String placementId, String interaction) {
                Log.d("OKSpin", "onUserInteraction, placementId: " + placementId + ", interaction: " + interaction);
            }
        });
        if (!OkSpin.isInit()) {
            OkSpin.initSDK(key);
        }
    }
}