package com.arpaul.paypalguide.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.webkit.WebView;

import com.arpaul.airtelmoney.AirtelMoneyPayment;
import com.arpaul.paypalguide.R;
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
                final String url = new AirtelMoneyPayment(AirtelMoneyActivity.this, AirtelMoneyPayment.SERVICE_TYPE.TYPE_TEST, "test@test.com", "9874563210", "10").transactionConfig();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Log.e("responce",url);
                        wvAirtelMoney.getSettings().setJavaScriptEnabled(true);
                        wvAirtelMoney.loadDataWithBaseURL("", url, "text/html", "UTF-8", "");
                    }
                });

            }
        }).start();

    }
}
