package com.arpaul.paypalguide.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.webkit.WebView;

import com.arpaul.paypalguide.R;
import com.arpaul.sbibuddynew.MyBrowser;
import com.arpaul.sbibuddynew.MyJavaScriptInterface;
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

        String urlCreateOrder = new SBIBuddyPayment().paynow1WebView(1);

//        wvSBIBuddy.setWebViewClient(new MyBrowser());
//        wvSBIBuddy.getSettings().setLoadsImagesAutomatically(true);
//        wvSBIBuddy.getSettings().setJavaScriptEnabled(true);
//        wvSBIBuddy.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
//        wvSBIBuddy.loadUrl(urlCreateOrder);


        wvSBIBuddy.getSettings().setJavaScriptEnabled(true);
        wvSBIBuddy.getSettings().setSupportZoom(true);
        wvSBIBuddy.getSettings().setBuiltInZoomControls(true);
        wvSBIBuddy.setWebViewClient(new MyBrowser());
//        wvSBIBuddy.loadUrl("http://pqrs.abcde.com/facebook");
        wvSBIBuddy.loadUrl(SBIBuddyPayment.URL_PAYNOW);
//        wvSBIBuddy.loadData(urlCreateOrder, "application/json", null);

//        wvSBIBuddy.loadData(urlCreateOrder, "text/html; charset=utf-8", "base64");

//        wvSBIBuddy.loadDataWithBaseURL(SBIBuddyPayment.URL_PAYNOW,urlCreateOrder,"text/html", "UTF-8", SBIBuddyPayment.ENCODING_KEY);
//        wvSBIBuddy.addJavascriptInterface(new MyJavaScriptInterface(this), "HtmlViewer");

        /*new Thread(new Runnable() {
            @Override
            public void run() {
                new SBIBuddyPayment().paynow("1");
            }
        }).start();*/

    }
}
