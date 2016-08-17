package com.arpaul.paypalguide.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.webkit.WebView;

import com.arpaul.airtelmoney.AirtelMoneyPayment;
import com.arpaul.paypalguide.R;
import com.arpaul.paypalguide.paymentService.RestCall;
import com.arpaul.sbibuddynew.ChromeClient;
import com.arpaul.sbibuddynew.JavaScriptInterface;
import com.arpaul.sbibuddynew.MyBrowser;
import com.arpaul.sbibuddynew.SBIBuddyPayment;

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

        new Thread(new Runnable() {
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
                /*runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Log.e("responce",url);
                        wvAirtelMoney.getSettings().setJavaScriptEnabled(true);
                        wvAirtelMoney.loadDataWithBaseURL("", url, "text/html", "UTF-8", "");
                    }
                });*/

            }
        }).start();

    }
}
