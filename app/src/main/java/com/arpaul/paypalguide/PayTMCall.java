package com.arpaul.paypalguide;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.paytm.pgsdk.PaytmMerchant;
import com.paytm.pgsdk.PaytmOrder;
import com.paytm.pgsdk.PaytmPGService;
import com.paytm.pgsdk.PaytmPaymentTransactionCallback;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 * Created by Aritra on 26-07-2016.
 */
public class PayTMCall implements PaytmPaymentTransactionCallback {

    private Context context;
    private SERVICE_TYPE type;
    private int randomInt = 0;
    private PaytmPGService Service = null;

    public PayTMCall (Context context, SERVICE_TYPE type){
        this.context = context;
        this.type = type;

        initialize();
    }
    public void initialize(){

        Random randomGenerator = new Random();
//        randomInt = randomGenerator.nextInt(1000);

        String orderId = "ORDER" + (1 + randomGenerator.nextInt(2)) * 10000 + randomGenerator.nextInt(10000);

        //Getting the Service Instance. PaytmPGService.getStagingService()  will return the Service pointing to Staging Environment and PaytmPGService.getProductionService() will return the Service pointing to Production Environment.
        if(type == SERVICE_TYPE.TYPE_STAGING)
            Service = PaytmPGService.getStagingService();
        else
            Service = PaytmPGService.getProductionService();

        /*PaytmMerchant constructor takes two parameters
            1) Checksum generation url
            2) Checksum verification url
            Merchant should replace the below values with his values*/
        //Create new Merchant Object having all merchant configuration.
//        PaytmMerchant Merchant = new PaytmMerchant( "http://hostname/<checksum-gerneration-URL>", " http://hostname/<checksum-verification-URL>");
//        PaytmMerchant Merchant = new PaytmMerchant( "https://pguat.paytm.com/merchant-chksum/ChecksumGenerator", "https://pguat.paytm.com/merchant-chksum/ValidateChksum");
        PaytmMerchant Merchant = new PaytmMerchant(
                "https://pguat.paytm.com/paytmchecksum/paytmCheckSumGenerator.jsp",
                "https://pguat.paytm.com/paytmchecksum/paytmCheckSumVerify.jsp");

        //Create new order Object having all order information.
        Map<String, String> paramMap = new HashMap<String, String>();
        paramMap.put("REQUEST_TYPE", "DEFAULT");
        paramMap.put("ORDER_ID", orderId);
        paramMap.put("MID", "klbGlV59135347348753");
        paramMap.put("CUST_ID","CUST110");
        paramMap.put("CHANNEL_ID", "WAP");
        paramMap.put("INDUSTRY_TYPE_ID", "Retail");
        paramMap.put("WEBSITE", "paytm");
        paramMap.put("TXN_AMOUNT", "1.0");
        paramMap.put("THEME ", "merchant");

        PaytmOrder Order = new PaytmOrder(paramMap);

        //Set PaytmOrder and PaytmMerchant objects. Call this method and set both objects before starting transaction.
        Service.initialize(Order, Merchant, null);

//Start the Payment Transaction. Before starting the transaction ensure that initialize method is called.
//        Service.startPaymentTransaction(context, true, true, this);
        Service.startPaymentTransaction(context, false, false, this);
    }


    @Override
    public void someUIErrorOccurred(String inErrorMessage)
    {
        // Some UI Error Occurred in Payment Gateway Activity.
        // This may be due to initialization of views in Payment Gateway Activity or may be due to initialization of webview.
        // Error Message details the error occurred.
        Log.e("Transaction","someUIErrorOccurred :" + inErrorMessage);
    }

    @Override
    public void onTransactionSuccess(Bundle inResponse)
    {
        // After successful transaction this method gets called.
        // Response bundle contains the merchant response parameters.
        Log.e("Transaction"," Success :" + inResponse.toString());
        Toast.makeText(context, "Payment Transaction is successful ", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onTransactionFailure(String inErrorMessage, Bundle  inResponse)
    {
        // This method gets called if transaction failed.
        // Here in this case transaction is completed, but with a failure.
        // Error Message describes the reason for failure.
        // Response bundle contains the merchant response parameters.
        Log.d("Transaction", "Payment Transaction Failed " + inErrorMessage);
        Toast.makeText(context, "Payment Transaction Failed ", Toast.LENGTH_LONG).show();
    }

    @Override
    public void networkNotAvailable()
    {
        // If network is not available, then this method gets called.
        Log.e("Transaction"," Network unavailable");
    }

    @Override
    public void clientAuthenticationFailed(String inErrorMessage)
    {
        // This method gets called if client authentication failed.
        // Failure may be due to following reasons
        //      1. Server error or downtime.
        //      2. Server unable to generate checksum or checksum response is
        //         not in proper format.
        //      3. Server failed to authenticate that client. That is value of
        //         payt_STATUS is 2.
        // Error Message describes the reason for failure.
        Log.e("Transaction"," AuthenticationFailed :" + inErrorMessage);
    }

    @Override
    public void onErrorLoadingWebPage(int iniErrorCode, String inErrorMessage, String inFailingURL)
    {
        // This page gets called if some error occurred while loading some URL in Webview.
        // Error Code and Error Message describes the error.
        // Failing URL is the URL that failed to load.

        Log.e("Transaction","errorLoadingWebPage :" + iniErrorCode + "\n" + inErrorMessage + "\n" + inFailingURL);
    }

    @Override
    public void onBackPressedCancelTransaction() {

    }
}
