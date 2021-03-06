package com.arpaul.paypalguide.activity;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.RelativeLayout;

import com.arpaul.paypalguide.R;
import com.paypal.android.sdk.payments.PayPalConfiguration;

import java.math.BigDecimal;

public class MainActivity extends AppCompatActivity {

    private boolean _paypalLibraryInit;
    private static final String CONFIG_CLIENT_ID = "Aa7ZDll4t1ihlW0JnBlLoLqrQK3mSmbOtq50U3y9s-SgeraxdHe_XXyyeaDgSK_zAet7VLXlEaLoGai8";
    private static final String CONFIG_ENVIRONMENT = PayPalConfiguration.ENVIRONMENT_SANDBOX;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initLibrary();
    }

    //https://developer.paypal.com/docs/classic/mobile/ht_mpl-itemPayment-Android/
    public void initLibrary() {
    }

    private void showPayPalButton() {

        // Generate the PayPal checkout button and save it for later use
        /*PayPal pp = PayPal.getInstance();
        launchPayPalButton = pp.getCheckoutButton(this, PayPal.BUTTON_278x43, CheckoutButton.TEXT_PAY);

        // The OnClick listener for the checkout button
        launchPayPalButton.setOnClickListener(this);

        // Add the listener to the layout
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams (RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT);
        params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        params.bottomMargin = 10;
        launchPayPalButton.setLayoutParams(params);
        launchPayPalButton.setId(PAYPAL_BUTTON_ID);
        ((RelativeLayout) findViewById(R.id.RelativeLayout01)).addView(launchPayPalButton);
        ((RelativeLayout) findViewById(R.id.RelativeLayout01)).setGravity(Gravity.CENTER_HORIZONTAL);*/
    }

    public void PayPalButtonClick(View arg0) {
        // Create a basic PayPal payment
        /*PayPalPayment payment = new PayPalPayment();

        // Set the currency type
        payment.setCurrencyType("USD");

        // Set the recipient for the payment (can be a phone number)
        payment.setRecipient("ppalav_1285013097_biz@yahoo.com");

        // Set the payment amount, excluding tax and shipping costs
        payment.setSubtotal(new BigDecimal(_theSubtotal));

        // Set the payment type--his can be PAYMENT_TYPE_GOODS,
        // PAYMENT_TYPE_SERVICE, PAYMENT_TYPE_PERSONAL, or PAYMENT_TYPE_NONE
        payment.setPaymentType(PayPal.PAYMENT_TYPE_GOODS);

        // PayPalInvoiceData can contain tax and shipping amounts, and an
        // ArrayList of PayPalInvoiceItem that you can fill out.
        // These are not required for any transaction.
        PayPalInvoiceData invoice = new PayPalInvoiceData();

        // Set the tax amount
        invoice.setTax(new BigDecimal(_taxAmount));*/
    }

    public void PayPalActivityResult(int requestCode, int resultCode, Intent intent) {
        /*switch (resultCode) {
            // The payment succeeded
            case Activity.RESULT_OK:
                String payKey = intent.getStringExtra(PayPalActivity.EXTRA_PAY_KEY);
                this.paymentSucceeded(payKey);
                break;

            // The payment was canceled
            case Activity.RESULT_CANCELED:
                this.paymentCanceled();
                break;

            // The payment failed, get the error from the EXTRA_ERROR_ID and EXTRA_ERROR_MESSAGE
            case PayPalActivity.RESULT_FAILURE:
                String errorID = intent.getStringExtra(PayPalActivity.EXTRA_ERROR_ID);
                String errorMessage = intent.getStringExtra(PayPalActivity.EXTRA_ERROR_MESSAGE);
                this.paymentFailed(errorID, errorMessage);
        }*/
    }
}
