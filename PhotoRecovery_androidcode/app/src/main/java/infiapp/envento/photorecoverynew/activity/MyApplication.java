package infiapp.envento.photorecoverynew.activity;

import android.app.Application;

import com.recovery.photodeleted.data.R;
import infiapp.envento.photorecoverynew.ads.AppOpenManager;
import com.google.android.gms.ads.MobileAds;

public class MyApplication extends Application {

    private static MyApplication mInstance;
    AppOpenManager appOpenManager;

    public static boolean isAdmobInitialized = false;

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;

        // Initialize MobileAds asynchronously on the main thread
        MobileAds.initialize(this, initializationStatus -> {
            isAdmobInitialized = true;
            // Load AppOpenManager safely only after MobileAds initialization completes
            appOpenManager = new AppOpenManager(MyApplication.this, getString(R.string.admob_open_ads));
        });

    }

    public static MyApplication getInstance() {
        return mInstance;
    }

}