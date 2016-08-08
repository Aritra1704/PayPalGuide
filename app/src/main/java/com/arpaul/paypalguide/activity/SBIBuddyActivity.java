package com.arpaul.paypalguide.activity;

import android.content.ComponentName;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.webkit.WebView;

import com.arpaul.paypalguide.R;
import com.arpaul.sbibuddynew.MyBrowser;
import com.arpaul.sbibuddynew.SBIBuddyPayment;

import java.util.HashMap;
import java.util.Map;

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

        String urlCreateOrder = new SBIBuddyPayment().paynowWebView(1);

        wvSBIBuddy.getSettings().setJavaScriptEnabled(true);
        wvSBIBuddy.getSettings().setSupportZoom(true);
        wvSBIBuddy.getSettings().setBuiltInZoomControls(true);
        wvSBIBuddy.setWebViewClient(new MyBrowser());
//        wvSBIBuddy.loadUrl("http://pqrs.abcde.com/facebook");
//        wvSBIBuddy.loadUrl(SBIBuddyPayment.URL_PAYNOW);
//        wvSBIBuddy.loadData(urlCreateOrder, "application/json", null);

//        wvSBIBuddy.loadData(urlCreateOrder, "text/html; charset=utf-8", "base64");

//        wvSBIBuddy.loadDataWithBaseURL(SBIBuddyPayment.URL_PAYNOW,urlCreateOrder,"text/html", "UTF-8", SBIBuddyPayment.ENCODING_KEY);
//        wvSBIBuddy.addJavascriptInterface(new MyJavaScriptInterface(this), "HtmlViewer");

//        wvSBIBuddy.postUrl(SBIBuddyPayment.URL_PAYNOW, urlCreateOrder.getBytes());


        Map<String, String> extraHeaders = new HashMap<String, String>();
        extraHeaders.put("Referer", "https://revvit.fnpplus.com/control/makepayment");

        wvSBIBuddy.loadUrl(SBIBuddyPayment.URL_PAYNOW, extraHeaders);
        wvSBIBuddy.loadData(urlCreateOrder, "text/html; charset=utf-8", "base64");


//        Intent intent = new Intent();
//        intent.setComponent(new ComponentName("com.google.android.browser","com.google.android.browser.BrowserActivity"));
//        intent.setAction("android.intent.action.VIEW");
//        intent.addCategory("android.intent.category.BROWSABLE");
//        Uri uri = Uri.parse("http://techblogon.com");
//        intent.setData(uri);
//        try{
////            Intent webPageIntent = new Intent(Intent.ACTION_VIEW);
////            webPageIntent.setData(Uri.parse("http://techblogon.com"));
//            startActivity(webPageIntent);
//        } catch (Exception ex){
//            ex.printStackTrace();
//        }


//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                new SBIBuddyPayment().paynow("1");
//            }
//        }).start();

    }
}
