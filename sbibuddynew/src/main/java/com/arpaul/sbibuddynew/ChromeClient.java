package com.arpaul.sbibuddynew;

import android.webkit.WebChromeClient;
import android.webkit.WebView;

/**
 * Created by ARPaul on 13-08-2016.
 */
public class ChromeClient extends WebChromeClient {

    @Override
    public void onProgressChanged(WebView view, int newProgress) {
        super.onProgressChanged(view, newProgress);
    }
}
