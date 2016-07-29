package com.arpaul.paypalguide;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.arpaul.utilitieslib.StringUtils;
import com.mobikwik.sdk.MobikwikSDK;
import com.mobikwik.sdk.lib.MKTransactionResponse;
import com.paypal.android.sdk.payments.PayPalAuthorization;
import com.paypal.android.sdk.payments.PayPalConfiguration;
import com.paypal.android.sdk.payments.PayPalFuturePaymentActivity;
import com.paypal.android.sdk.payments.PayPalPayment;
import com.paypal.android.sdk.payments.PayPalService;
import com.paypal.android.sdk.payments.PaymentActivity;
import com.paypal.android.sdk.payments.PaymentConfirmation;
import com.paytm.pgsdk.PaytmClientCertificate;
import com.paytm.pgsdk.PaytmOrder;

import org.json.JSONException;

import java.math.BigDecimal;

/**
 * Created by Aritra on 19-07-2016.
 */
public class TestActivity extends AppCompatActivity {

    private static final String CONFIG_CLIENT_ID = "Aa7ZDll4t1ihlW0JnBlLoLqrQK3mSmbOtq50U3y9s-SgeraxdHe_XXyyeaDgSK_zAet7VLXlEaLoGai8";

    private static final String CONFIG_ENVIRONMENT = PayPalConfiguration.ENVIRONMENT_SANDBOX;
    // note that these credentials will differ between live & sandbox
// environments.
    private static final int REQUEST_CODE_PAYMENT = 1;
    private static final int REQUEST_CODE_FUTURE_PAYMENT = 2;
    private static PayPalConfiguration config = new PayPalConfiguration().environment(CONFIG_ENVIRONMENT)
    .clientId(CONFIG_CLIENT_ID)
    .merchantName("Hipster Store")
    .merchantPrivacyPolicyUri(Uri.parse("https://www.example.com/privacy"))
            .merchantUserAgreementUri(Uri.parse("https://www.example.com/legal"));
    PayPalPayment thingToBuy;


    //PayTM
    //http://paywithpaytm.com/developer/paytm_sdk_doc?target=android-configurations

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent intent = new Intent(this, PayPalService.class);
        intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, config);
        startService(intent);
        findViewById(R.id.order).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                thingToBuy = new PayPalPayment(new BigDecimal("1"), "USD",
                        "HeadSet", PayPalPayment.PAYMENT_INTENT_SALE);
                Intent intent = new Intent(TestActivity.this, PaymentActivity.class);
                intent.putExtra(PaymentActivity.EXTRA_PAYMENT, thingToBuy);
                startActivityForResult(intent, REQUEST_CODE_PAYMENT);
            }
        });

        findViewById(R.id.orderPaytm).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new PayTMCall(TestActivity.this, SERVICE_TYPE.TYPE_STAGING);
            }
        });

        findViewById(R.id.orderMobikwik).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new MobikWikCall(TestActivity.this, SERVICE_TYPE.TYPE_STAGING, "aritrarpal@gmail.com", "9030303407", "1");
            }
        });
    }

    public void onFuturePaymentPressed(View pressed) {
        Intent intent = new Intent(TestActivity.this,  PayPalFuturePaymentActivity.class);
        startActivityForResult(intent, REQUEST_CODE_FUTURE_PAYMENT);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE_PAYMENT) {
            if (resultCode == Activity.RESULT_OK) {
                PaymentConfirmation confirm = data
                        .getParcelableExtra(PaymentActivity.EXTRA_RESULT_CONFIRMATION);
                if (confirm != null) {
                    try {
                        System.out.println(confirm.toJSONObject().toString(4));
                        System.out.println(confirm.getPayment().toJSONObject()
                                .toString(4));
                        Toast.makeText(getApplicationContext(), "Order placed",
                                Toast.LENGTH_LONG).show();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            } else if (resultCode == Activity.RESULT_CANCELED) {
                System.out.println("The user canceled.");
            } else if (resultCode == PaymentActivity.RESULT_EXTRAS_INVALID) {
                System.out
                        .println("An invalid Payment or PayPalConfiguration was submitted. Please see the docs.");
            }
        } else if (requestCode == REQUEST_CODE_FUTURE_PAYMENT) {
            if (resultCode == Activity.RESULT_OK) {
                PayPalAuthorization auth = data.getParcelableExtra(PayPalFuturePaymentActivity.EXTRA_RESULT_AUTHORIZATION);
                if (auth != null) {
                    try {
                        Log.i("FuturePaymentExample", auth.toJSONObject()
                                .toString(4));
                        String authorization_code = auth.getAuthorizationCode();
                        Log.i("FuturePaymentExample", authorization_code);
                        sendAuthorizationToServer(auth);
                        Toast.makeText(getApplicationContext(),
                                "Future Payment code received from PayPal",
                                Toast.LENGTH_LONG).show();
                    } catch (JSONException e) {
                        Log.e("FuturePaymentExample",
                                "an extremely unlikely failure occurred: ", e);
                    }
                }
            } else if (resultCode == Activity.RESULT_CANCELED) {
                Log.i("FuturePaymentExample", "The user canceled.");
            } else if (resultCode == PayPalFuturePaymentActivity.RESULT_EXTRAS_INVALID) {
                Log.i("FuturePaymentExample",
                        "Probably the attempt to previously start the PayPalService had an invalid PayPalConfiguration. Please see the docs.");
            }
        } else if (requestCode == MobikWikCall.REQ_CODE ) {
            if (data != null ) {
                MKTransactionResponse response = (MKTransactionResponse)data.getSerializableExtra(MobikwikSDK. EXTRA_TRANSACTION_RESPONSE );
                switch (StringUtils.getInt(response.statusCode)){
                    case MobikWikCall.SUCCESS:
                        Toast.makeText(TestActivity.this, response. statusMessage, Toast.LENGTH_SHORT).show();
                        break;
                    case MobikWikCall.FAILED:
                        Toast.makeText(TestActivity.this, response. statusMessage, Toast.LENGTH_SHORT).show();
                        break;
                    case MobikWikCall.USER_BLOCKED:
                        Toast.makeText(TestActivity.this, response. statusMessage, Toast.LENGTH_SHORT).show();
                        break;
                    case MobikWikCall.INSUFFICIENT_BALANCE:
                        Toast.makeText(TestActivity.this, response. statusMessage, Toast.LENGTH_SHORT).show();
                        break;
                    case MobikWikCall.USER_CANCELLED_TRANSACTION:
                        Toast.makeText(TestActivity.this, response. statusMessage, Toast.LENGTH_SHORT).show();
                        break;
                    case MobikWikCall.DUPLICATE_ORDER_ID:
                        Toast.makeText(TestActivity.this, response. statusMessage, Toast.LENGTH_SHORT).show();
                        break;
                    case MobikWikCall.EMAIL_ID_INVALID:
                        Toast.makeText(TestActivity.this, response. statusMessage, Toast.LENGTH_SHORT).show();
                        break;
                    case MobikWikCall.CELL_INVALID:
                        Toast.makeText(TestActivity.this, response. statusMessage, Toast.LENGTH_SHORT).show();
                        break;
                    case MobikWikCall.EITHER_EMAIL_OR_MOBILE:
                        Toast.makeText(TestActivity.this, response. statusMessage, Toast.LENGTH_SHORT).show();
                        break;
                    case MobikWikCall.AMOUNT_INVALID:
                        Toast.makeText(TestActivity.this, response. statusMessage, Toast.LENGTH_SHORT).show();
                        break;
                    case MobikWikCall.AUTHENTICATION_FAILED:
                        Toast.makeText(TestActivity.this, response. statusMessage, Toast.LENGTH_SHORT).show();
                        break;
                    case MobikWikCall.UNEXPECTED_ERROR:
                        Toast.makeText(TestActivity.this, response. statusMessage, Toast.LENGTH_SHORT).show();
                        break;
                    case MobikWikCall.UNABLE_TO_CONNECT_SERVER:
                        Toast.makeText(TestActivity.this, response. statusMessage, Toast.LENGTH_SHORT).show();
                        break;
                    case MobikWikCall.USER_DOES_NOT_EXIST:
                        Toast.makeText(TestActivity.this, response. statusMessage, Toast.LENGTH_SHORT).show();
                        break;
                }
                System.out.println("CheckoutActivity.onActivityResult() " + response. statusMessage );
                System.out.println("CheckoutActivity.onActivityResult() " + response. statusCode );
            }
        }
    }
    private void sendAuthorizationToServer(PayPalAuthorization authorization) {
    }
    public void onFuturePaymentPurchasePressed(View pressed) {
// Get the Application Correlation ID from the SDK
        String correlationId = PayPalConfiguration.getApplicationCorrelationId(this);
        Log.i("FuturePaymentExample", "Application Correlation ID: "
                + correlationId);
// TODO: Send correlationId and transaction details to your server for
// processing with
// PayPal...
        Toast.makeText(getApplicationContext(),
                "App Correlation ID received from SDK", Toast.LENGTH_LONG)
                .show();
    }
    @Override
    public void onDestroy() {
// Stop service when done
        stopService(new Intent(this, PayPalService.class));
        super.onDestroy();
    }
}
