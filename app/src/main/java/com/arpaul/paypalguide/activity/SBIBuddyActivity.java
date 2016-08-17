package com.arpaul.paypalguide.activity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;

import com.arpaul.paypalguide.R;
import com.arpaul.sbibuddynew.ChromeClient;
import com.arpaul.sbibuddynew.JavaScriptInterface;
import com.arpaul.sbibuddynew.MyBrowser;
import com.arpaul.sbibuddynew.SBIBuddyPayment;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

/**
 * Created by Aritra on 05-08-2016.
 */
public class SBIBuddyActivity extends AppCompatActivity {

    private WebView wvSBIBuddy;
    private String responce;
    private String urlCreateOrder;
    private Button btnSignUp, btnBack;

//    http://stackoverflow.com/questions/4065312/detect-click-on-html-button-through-javascript-in-android-webview
//    http://stackoverflow.com/questions/20917235/webviews-html-button-click-detection-in-activityjava-code

    @SuppressLint("JavascriptInterface")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sbibuddy);

        wvSBIBuddy = (WebView) findViewById(R.id.wvSBIBuddy);
        btnSignUp = new Button(SBIBuddyActivity.this);
        btnBack = new Button(SBIBuddyActivity.this);

        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        urlCreateOrder = new SBIBuddyPayment().paynowWebView(1);
//        wvSBIBuddy.getSettings().setJavaScriptEnabled(true);
//        wvSBIBuddy.getSettings().setSupportZoom(true);
//        wvSBIBuddy.getSettings().setBuiltInZoomControls(true);
//        wvSBIBuddy.setWebViewClient(new MyBrowser());
//        Map<String, String> extraHeaders = new HashMap<String, String>();
//        extraHeaders.put("Referer", "https://secure.revvit.fnpplus.com");

//        wvSBIBuddy.loadUrl(SBIBuddyPayment.URL_PAYNOW);
//        wvSBIBuddy.postUrl(SBIBuddyPayment.URL_PAYNOW, urlCreateOrder.getBytes());
//        wvSBIBuddy.loadData(urlCreateOrder, "text/html; charset=utf-8", "base64");
        new Thread(new Runnable() {
            @Override
            public void run()
            {
                responce = makeRestCallAndGetResponse(SBIBuddyPayment.URL_PAYNOW, "POST",urlCreateOrder);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Log.e("responce",responce);
                        wvSBIBuddy.getSettings().setJavaScriptEnabled(true);
                        wvSBIBuddy.setWebViewClient(new MyBrowser());
                        wvSBIBuddy.setWebChromeClient(new ChromeClient());
                        wvSBIBuddy.addJavascriptInterface(new JavaScriptInterface(SBIBuddyActivity.this), "HtmlViewer");

                        wvSBIBuddy.addJavascriptInterface(btnSignUp, "signup");
                        wvSBIBuddy.addJavascriptInterface(btnBack, "back");

                        wvSBIBuddy.loadDataWithBaseURL("", responce, "text/html", "UTF-8", "");
                    }
                });

            }
        }).start();

//        wvSBIBuddy.getSettings().setJavaScriptEnabled(true);
//        wvSBIBuddy.getSettings().setSupportZoom(true);
//        wvSBIBuddy.getSettings().setBuiltInZoomControls(true);
//        wvSBIBuddy.setWebViewClient(new MyBrowser());
//        wvSBIBuddy.loadUrl("http://pqrs.abcde.com/facebook");
//        wvSBIBuddy.loadUrl(SBIBuddyPayment.URL_PAYNOW);
//        wvSBIBuddy.loadData(urlCreateOrder, "application/json", null);

//        wvSBIBuddy.loadData(urlCreateOrder, "text/html; charset=utf-8", "base64");

//        wvSBIBuddy.loadDataWithBaseURL(SBIBuddyPayment.URL_PAYNOW,urlCreateOrder,"text/html", "UTF-8", SBIBuddyPayment.ENCODING_KEY);
//        wvSBIBuddy.addJavascriptInterface(new JavaScriptInterface(this), "HtmlViewer");

//        wvSBIBuddy.postUrl(SBIBuddyPayment.URL_PAYNOW, urlCreateOrder.getBytes());


//        Map<String, String> extraHeaders = new HashMap<String, String>();
//        extraHeaders.put("Referer", "https://secure.revvit.fnpplus.comt");
//
//        wvSBIBuddy.loadUrl(SBIBuddyPayment.URL_PAYNOW, extraHeaders);
//        wvSBIBuddy.loadData(urlCreateOrder, "text/html; charset=utf-8", "base64");


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

    public static String makeRestCallAndGetResponse(String strUrl, String method, String parameters) {
        String response;
        TrustManager[] trustAllCerts = new TrustManager[]
                {
                        new X509TrustManager() {
                            public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                                return null;
                            }
                            public void checkClientTrusted(java.security.cert.X509Certificate[] certs, String authType) {}
                            public void checkServerTrusted(java.security.cert.X509Certificate[] certs, String authType) {}
                        }
                };
        try {
            SSLContext sc = SSLContext.getInstance("SSL");
            sc.init(null, trustAllCerts, new java.security.SecureRandom());
            HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
        } catch (Exception e) {
            e.printStackTrace();
        }
        response ="";
        HttpURLConnection connection = null;
        URL url = null;
        BufferedReader reader = null;
        try {
            url = new URL(strUrl);
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod(method);
            if("POST".equals(method))
            {
                connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                connection.setRequestProperty("User-Agent", "mozilla");//
                connection.setRequestProperty("Referer", "https://secure.revvit.fnpplus.com");
            }
            else
            {
                connection.setRequestProperty("User-Agent", "mozilla");
                connection.setRequestProperty("Referer", "https://secure.revvit.fnpplus.com");
            }
            if(!TextUtils.isEmpty(parameters))
            {
                OutputStream os = connection.getOutputStream();
                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
                writer.write(parameters);
                writer.flush();
                writer.close();
                os.close();
            }
            connection.connect();
            InputStream stream = connection.getInputStream();
            reader = new BufferedReader(new InputStreamReader(stream));
            StringBuilder buffer = new StringBuilder();
            String line = "";
            while ((line = reader.readLine())!=null)
            {
                buffer.append(line);
            }
            response = buffer.toString();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally
        {
            if(connection != null){
                connection.disconnect();
            }
            try {
                if(reader !=null) {
                    reader.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            return response;
        }
    }
}
