package com.prxd.shebangs.app;

import android.app.Application;
import com.prxd.shebangs.R;

import uk.co.chrisjenx.calligraphy.CalligraphyConfig;

public class SheBangsApp extends Application {

    private static SheBangsApp app;

    public static SheBangsApp getInstance() {
        return app;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        app = this;
        //引用第三方字体
        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath("fonts/helvetica.ttf")
                .setFontAttrId(R.attr.fontPath)
                .build()
        );
    }
}
