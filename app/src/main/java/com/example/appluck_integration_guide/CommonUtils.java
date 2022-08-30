package com.example.appluck_integration_guide;

import android.content.Context;
import android.util.Log;

import com.google.android.gms.ads.identifier.AdvertisingIdClient;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;

import org.apache.commons.lang3.StringUtils;

import java.io.IOException;

public class CommonUtils {

    //广告位链接
    private final static String SLOT_URL = "https://aios.soinluck.com/scene?sk=q842c2e079a1b32c8&lzdid={gaid}";

    public static String url = "";

    public static String slotUrlWithGaid;

    //获取 GAID
    public static String getGAID(Context context) {
        String gaid = "";
        AdvertisingIdClient.Info adInfo = null;
        try {
            adInfo = AdvertisingIdClient.getAdvertisingIdInfo(context);
        } catch (IOException e) {
            // Unrecoverable error connecting to Google Play services (e.g.,
            // the old version of the service doesn't support getting AdvertisingId).
            Log.e("getGAID", "IOException");
        } catch (GooglePlayServicesNotAvailableException e) {
            // Google Play services is not available entirely.
            Log.e("getGAID", "GooglePlayServicesNotAvailableException");
        } catch (Exception e) {
            Log.e("getGAID", "Exception:" + e.toString());
            // Encountered a recoverable error connecting to Google Play services.
        }
        if (adInfo != null) {
            gaid = adInfo.getId();
            Log.w("getGAID", "gaid:" + gaid);
        }
        return gaid;
    }

    public static String getUrl(Context context) {
        String gaid = getGAID(context);
        return StringUtils.replace(SLOT_URL, "{gaid}", gaid);
    }

    public static String setUrl(Context context,String u) {
        String gaid = getGAID(context);
        String replace = StringUtils.replace(u, "{gaid}", gaid);
        return url = replace;
    }
    public static void setSlotUrlWithGaid(String slotUrlWithGaid) {
        CommonUtils.slotUrlWithGaid = slotUrlWithGaid;
    }
}
