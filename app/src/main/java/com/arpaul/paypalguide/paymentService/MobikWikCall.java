package com.arpaul.paypalguide.paymentService;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import com.mobikwik.sdk.MobikwikSDK;
import com.mobikwik.sdk.lib.Transaction;
import com.mobikwik.sdk.lib.TransactionConfiguration;
import com.mobikwik.sdk.lib.User;
import com.mobikwik.sdk.lib.payinstrument.PaymentInstrumentType;

import java.util.Random;

/**
 * Created by Aritra on 29-07-2016.
 */
public class MobikWikCall {

    private Context context;
    private SERVICE_TYPE type;

    private final String MID = "MBK9002";
    private final String Merchant_Name = "Demo merchant";
    private final String Secret_Key = "ju6tygh7u7tdg554k098ujd5468o";
    private final String TEST_URL = "https://test.mobikwik.com/wallet";
    public static final int REQ_CODE = 1011;

    public static final int SUCCESS                          = 0;
    public static final int FAILED                           = 1;
    public static final int USER_BLOCKED                     = 20;
    public static final int MERCHANT_BLOCKED                 = 21;
    public static final int MERCHANT_NOT_REGISTERED          = 22;
    public static final int MERCHANT_INACTIVE                = 23;
    public static final int WALLET_TOPUP_FAILED              = 30;
    public static final int INSUFFICIENT_BALANCE             = 33;
    public static final int USER_CANCELLED_TRANSACTION       = 43;
    public static final int DUPLICATE_ORDER_ID               = 50;
    public static final int EMAIL_ID_INVALID                 = 53;
    public static final int AMOUNT_INVALID                   = 54;
    public static final int AUTHENTICATION_FAILED            = 60;
    public static final int CHECKSUM_MISMATCH                = 80;
    public static final int UNEXPECTED_ERROR                 = 99;
    public static final int UNABLE_TO_CONNECT_SERVER         = 98;
    public static final int USER_DOES_NOT_EXIST              = 120;
    public static final int INVALID_OTP                      = 155;
    public static final int CELL_INVALID                     = 156;
    public static final int EITHER_EMAIL_OR_MOBILE           = 181;

    private int stagingType = 0;
    private String email, cell, trxAmount;

    private TransactionConfiguration config;

    public MobikWikCall (Context context, SERVICE_TYPE type, String email, String cell, String trxAmount){
        this.context = context;
        this.type = type;

        this.email = email;
        this.cell = cell;
        this.trxAmount = trxAmount;

        initializeTransactionConfig();

        createTransaction();
    }

    public void initializeTransactionConfig(){

        if(type == SERVICE_TYPE.TYPE_STAGING)
            stagingType = 0;//for test environment
        else
            stagingType = 1;//for live environment

        config = new TransactionConfiguration();
        config.setDebitWallet(true);
        config.setPgResponseUrl( " https://test.mobikwik.com/sdkresponse.jsp " );//need to know
        config.setChecksumUrl( " https://test.mobikwik.com/sdkchecksum.jsp " );//need to know
        config.setMerchantName(Merchant_Name);
        config.setMbkId(MID);
        config.setMode(""+stagingType);
    }

    public void createTransaction(){
        User usr = new User(email, cell);

        Random randomGenerator = new Random();
//        randomInt = randomGenerator.nextInt(1000);

        String orderId = "ORDER" + (1 + randomGenerator.nextInt(2)) * 10000 + randomGenerator.nextInt(10000);

        Transaction newTransaction = Transaction.Factory.newTransaction(usr, orderId, trxAmount, PaymentInstrumentType.MK_WALLET);

        Intent mobikwikIntent = new Intent(context, MobikwikSDK. class);
        mobikwikIntent.putExtra(MobikwikSDK. EXTRA_TRANSACTION_CONFIG , config);
        mobikwikIntent.putExtra(MobikwikSDK. EXTRA_TRANSACTION , newTransaction);
        ((Activity)context).startActivityForResult(mobikwikIntent, REQ_CODE);
    }

    /*@Override
    protected void onActivityResult( int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQ_CODE ) {
            if (data != null ) {
                MKTransactionResponse response = (MKTransactionResponse)data.getSerializableExtra(MobikwikSDK. EXTRA_TRANSACTION_RESPONSE );
                System.out.println("CheckoutActivity.onActivityResult() " + response. statusMessage );
                System.out.println("CheckoutActivity.onActivityResult() " + response. statusCode );
            }
        }
    }*/
}
