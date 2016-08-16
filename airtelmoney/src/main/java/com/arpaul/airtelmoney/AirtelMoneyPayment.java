package com.arpaul.airtelmoney;

import android.content.Context;

import com.arpaul.utilitieslib.CalendarUtils;
import com.arpaul.utilitieslib.LogUtils;

import java.security.MessageDigest;
import java.util.Random;

/**
 * Created by Aritra on 12-08-2016.
 */
public class AirtelMoneyPayment {

    public static final String MERCHANTID   = "25649258";//Test merchant id
    public static final String SALTID       = "c3110acb";
    public static final String CURRENCY     = "INR";
    private final String TEST_URL           = "https://sit.airtelmoney.in/oneClick/signIn?REQUEST=ECOMM_SIGNON";
    private final String PRODUCTION_URL     = "https://ecom.airtelmoney.in/oneClick/signIn?REQUEST=ECOMM_SIGNON";

    public static final String URL_SUCCESS  = "http://revvit.fnpplus.com/control/storePayUResponse";
    public static final String URL_FAILURE  = "http://revvit.fnpplus.com/control/storePayUResponse";

    private Context context;
    private SERVICE_TYPE type;

    private String email, cell, trxAmount;

    public AirtelMoneyPayment(Context context, SERVICE_TYPE type, String email, String cell, String trxAmount) {
        this.context = context;
        this.type = type;

        this.email = email;
        this.cell = cell;
        this.trxAmount = trxAmount;
    }

    public String transactionConfig(){
        String url = "";
        if(type == SERVICE_TYPE.TYPE_TEST)
            url = TEST_URL;//for test environment
        else
            url = PRODUCTION_URL;

        Random randomGenerator = new Random();
        String orderId = "TRX" + (1 + randomGenerator.nextInt(2)) * 10000 + randomGenerator.nextInt(10000);

        String date = CalendarUtils.getDateinPattern(WebServiceConstant.DATE_PATTERN);

        String hash = generateHash(MERCHANTID, orderId, trxAmount, date, SALTID);
        String param = ParamBuilder.createTransactionParam(MERCHANTID, orderId, URL_SUCCESS, URL_FAILURE, trxAmount, date, CURRENCY, "FNP", cell, email, hash);

        LogUtils.infoLog("transactionConfig", param);
        WebServiceResponse response = new RestServiceCalls(url, param, WEBSERVICE_TYPE.POST).getData();

        if(response != null && response.getResponseCode() == WebServiceResponse.ResponseType.SUCCESS){
            return response.getResponseMessage();
        } else
            return null;
    }

    private String generateHash(String mid, String txn_ref_no, String amount, String date, String salt){
        StringBuffer hexString = new StringBuffer();
        try {
            String text = mid + "#" + txn_ref_no + "#" + amount + "#" + date + "#" + salt;

            MessageDigest md = MessageDigest.getInstance("SHA-512");
            md.update(text.getBytes());

            byte byteData[] = md.digest();

            //convert the byte to hex format method 1
            for (int i = 0; i < byteData.length; i++) {
                hexString.append(Integer.toString((byteData[i] & 0xff) + 0x100, 16).substring(1));
            }

            System.out.println("Hex format : " + hexString.toString());

            /*//convert the byte to hex format method 2

            for (int i=0;i < byteData.length; i++) {
                String hex = Integer.toHexString(0xff & byteData[i]);
                if (hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }*/
        } catch (Exception ex){
            ex.printStackTrace();
        } finally {
            return hexString.toString();
        }
    }
    public enum SERVICE_TYPE {
        TYPE_TEST,
        TYPE_PRODUCTION
    }
}
