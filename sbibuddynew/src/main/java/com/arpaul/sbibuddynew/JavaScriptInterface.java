package com.arpaul.sbibuddynew;

import android.content.Context;
import android.webkit.JavascriptInterface;

/**
 * Created by Aritra on 05-08-2016.
 */
public class JavaScriptInterface {
    private Context ctx;

    public JavaScriptInterface(Context ctx) {
        this.ctx = ctx;
    }

    @JavascriptInterface
    public void showHTML(String html) {
        System.out.println(html);
    }
}
