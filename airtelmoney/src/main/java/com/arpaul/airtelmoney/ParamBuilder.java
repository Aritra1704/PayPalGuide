package com.arpaul.airtelmoney;


import android.net.Uri;

/**
 * Created by Aritra on 05-08-2016.
 */
public class ParamBuilder {

    public static String createTransactionParam(String mid, String txn_ref_no, String successUrl, String failureUrl, String amount, String date,
                                         String currency, String endmid, String contact, String email, String hash){
        StringBuilder sbTransaction = new StringBuilder();

        sbTransaction.append("&MID=").append(mid);
        sbTransaction.append("&TXN_REF_NO=").append(txn_ref_no);
        sbTransaction.append("&SU=").append(successUrl);
        sbTransaction.append("&FU=").append(failureUrl);
        sbTransaction.append("&AMT=").append(amount);
        sbTransaction.append("&DATE=").append(date);
        sbTransaction.append("&CUR=").append(currency);
        sbTransaction.append("&END_MID=").append(endmid);
        sbTransaction.append("&CUST_MOBILE=").append(contact);
        sbTransaction.append("&CUST_EMAIL=").append(email);
        sbTransaction.append("&HASH=").append(hash);

        return sbTransaction.toString();
    }

    public static String createAirtelMoneyParam(String url, String request, String mid, String txn_ref_no, String successUrl, String failureUrl,
                                        String amount, String date, String currency, String endmid, String contact, String email, String hash){
        Uri.Builder builder = Uri.parse(url).buildUpon();
        builder.appendQueryParameter("REQUEST", request);
        builder.appendQueryParameter("MID", mid);
        builder.appendQueryParameter("TXN_REF_NO", txn_ref_no);
        builder.appendQueryParameter("SU", successUrl);
        builder.appendQueryParameter("FU", failureUrl);
        builder.appendQueryParameter("AMT", amount);
        builder.appendQueryParameter("DATE", date);
        builder.appendQueryParameter("CUR", currency);
        builder.appendQueryParameter("END_MID", endmid);
        builder.appendQueryParameter("CUST_MOBILE", contact);
        builder.appendQueryParameter("CUST_EMAIL", email);
        builder.appendQueryParameter("HASH", hash);

        builder.build();
        return builder.toString();
    }
}
