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
                *//*runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Log.e("responce",url);
                        wvAirtelMoney.getSettings().setJavaScriptEnabled(true);
                        wvAirtelMoney.loadDataWithBaseURL("", url, "text/html", "UTF-8", "");
                    }
                });*//*

            }
        }).start();*/


        wvAirtelMoney.setWebViewClient(new MyWebViewClient());
        wvAirtelMoney.clearCache(false);
        Uri.Builder b = Uri.parse("https://sit.airtelmoney.in/oneClick/signIn").buildUpon();
        b.appendQueryParameter("REQUEST", "ECOMM_SIGNON");
        b.appendQueryParameter("MID", "25649258");
        b.appendQueryParameter("TXN_REF_NO", "TRX200004087");
        b.appendQueryParameter("SU", "http://revvit.fnpplus.com/control/storePayUResponse");
        b.appendQueryParameter("FU", "http://revvit.fnpplus.com/control/storePayUResponse");
        b.appendQueryParameter("AMT", "11");
        b.appendQueryParameter("DATE", "08162016182249");
        b.appendQueryParameter("CUR", "INR");
        b.appendQueryParameter("CUST_EMAIL", "test@test.com");
        StringBuilder e = new StringBuilder();
        try
        {

            MessageDigest messageDigest = MessageDigest.getInstance("SHA-512");
            messageDigest.update("25649258#TRX200004087#11#08162016182249#c3110acb".getBytes());
            byte[] mdbytes = messageDigest.digest();
            byte[] var5 = mdbytes;
            int var6 = mdbytes.length;
            for(int var7 = 0; var7 < var6; ++var7) {
                byte hashByte = var5[var7];
                e.append(Integer.toString((hashByte & 255) + 256, 16).substring(1));
            }
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
        b.appendQueryParameter("HASH", e.toString());
        b.build();
        wvAirtelMoney.getSettings().setJavaScriptEnabled(true);
        wvAirtelMoney.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        wvAirtelMoney.getSettings().setSupportZoom(true);
        wvAirtelMoney.getSettings().setBuiltInZoomControls(true);
        wvAirtelMoney.getSettings().setDomStorageEnabled(true);
        wvAirtelMoney.canGoBack();
        wvAirtelMoney.canGoForward();
        wvAirtelMoney.clearCache(false);
        wvAirtelMoney.getSettings().setAllowFileAccess(true);
        wvAirtelMoney.loadUrl(b.toString());

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
