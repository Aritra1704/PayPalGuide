package com.arpaul.paypalguide.activity;

import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.arpaul.airtelmoney.AirtelMoneyPayment;
import com.arpaul.paypalguide.R;
import com.arpaul.paypalguide.paymentService.RestCall;
import com.arpaul.sbibuddynew.ChromeClient;
import com.arpaul.sbibuddynew.JavaScriptInterface;
import com.arpaul.sbibuddynew.MyBrowser;
import com.arpaul.sbibuddynew.SBIBuddyPayment;

import java.security.MessageDigest;

/**
 * Created by Aritra on 12-08-2016.
 */
public class AirtelMoneyActivity extends AppCompatActivity {

    private WebView wvAirtelMoney;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sbibuddy);

        wvAirtelMoney = (WebView) findViewById(R.id.wvSBIBuddy);

        /*new Thread(new Runnable() {
            @Override
            public void run()
            {
                final String url = new AirtelMoneyPayment(AirtelMoneyActivity.this, AirtelMoneyPayment.SERVICE_TYPE.TYPE_TEST, "test@test.com", "", "11").transactionConfig();

                final String responce = new RestCall().makeRestCallAndGetResponse(AirtelMoneyPayment.TEST_URL, "POST",url);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Log.e("responce",responce);
                        wvAirtelMoney.loadUrl(responce);

//                        wvAirtelMoney.getSettings().setJavaScriptEnabled(true);
//                        wvAirtelMoney.setWebViewClient(new MyBrowser());
//                        wvAirtelMoney.setWebChromeClient(new ChromeClient());
//                        wvAirtelMoney.addJavascriptInterface(new JavaScriptInterface(AirtelMoneyActivity.this), "HtmlViewer");
//
//                        wvAirtelMoney.loadDataWithBaseURL("", responce, "text/html", "UTF-8", "");
                    }
                });
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Log.e("responce",url);
                        wvAirtelMoney.getSettings().setJavaScriptEnabled(true);
                        wvAirtelMoney.loadDataWithBaseURL("", url, "text/html", "UTF-8", "");
                    }
                });

            }
        }).start();*/


        String url = new AirtelMoneyPayment(AirtelMoneyActivity.this, AirtelMoneyPayment.SERVICE_TYPE.TYPE_TEST, "test@test.com", "", "11").getAirtelMoneyParam();
        wvAirtelMoney.clearCache(false);
        wvAirtelMoney.getSettings().setJavaScriptEnabled(true);
//        wvAirtelMoney.setWebViewClient(new MyWebViewClient());
        wvAirtelMoney.setWebViewClient(new MyBrowser());
        wvAirtelMoney.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        wvAirtelMoney.getSettings().setSupportZoom(true);
        wvAirtelMoney.getSettings().setBuiltInZoomControls(true);
        wvAirtelMoney.getSettings().setDomStorageEnabled(true);
        wvAirtelMoney.canGoBack();
        wvAirtelMoney.canGoForward();
        wvAirtelMoney.clearCache(false);
        wvAirtelMoney.getSettings().setAllowFileAccess(true);
        wvAirtelMoney.loadUrl(url);

    }

    public class MyWebViewClient extends WebViewClient {

        public MyWebViewClient() {
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
//            HashMap<String, String> headers = new HashMap<String, String>();
//            headers.put("Referer", "https://secure.revvit.fnpplus.com");
//            view.loadUrl(url, headers);
//            Log.e("url", "url");
//            return true;
        }

        @Override
        public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
            Log.e("error", error.toString());
            super.onReceivedError(view, request, error);
        }
    }
}
