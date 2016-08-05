package com.arpaul.paypalguide.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.webkit.WebView;

import com.arpaul.paypalguide.R;
import com.arpaul.sbibuddynew.MyBrowser;
import com.arpaul.sbibuddynew.SBIBuddyPayment;

/**
 * Created by Aritra on 05-08-2016.
 */
public class SBIBuddyActivity extends AppCompatActivity {

    private WebView wvSBIBuddy;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sbibuddy);

        wvSBIBuddy = (WebView) findViewById(R.id.wvSBIBuddy);

//        String urlCreateOrder = new SBIBuddyPayment().paynow(1);
//        wvSBIBuddy.setWebViewClient(new MyBrowser());
//        wvSBIBuddy.getSettings().setLoadsImagesAutomatically(true);
//        wvSBIBuddy.getSettings().setJavaScriptEnabled(true);
//        wvSBIBuddy.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
//
//        wvSBIBuddy.loadUrl(urlCreateOrder);

        new Thread(new Runnable() {
            @Override
            public void run() {
                new SBIBuddyPayment().paynow("1");
            }
        }).start();

    }
}
