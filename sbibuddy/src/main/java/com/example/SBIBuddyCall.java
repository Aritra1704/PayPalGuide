package com.example;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.security.Key;
import java.util.Base64;
import java.util.Random;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import javax.net.ssl.HttpsURLConnection;

/**
 * Created by ARPaul on 05-08-2016.
 */
public class SBIBuddyCall {

    public final String ORDERID_PARAM = "orderId=";
    public final String AMOUNT_PARAM = "&amount=";
    public final String CURRENCY_PARAM = "&currency=INR";

    public static final String TRANSACTIONID_PARAM = "transactionId";
    public static final String MERCHANTID_PARAM = "merchantId";

    private String transformation = "AES";
    private String utf8 = "UTF-8";

    public SBIBuddyCall (){

    }

    public void createOrder(float amount) {
        try{
            byte[] secretKey = new byte[]{0x01};

            Random randomGenerator = new Random();
            String orderId = "EAK" + (1 + randomGenerator.nextInt(2)) * 10000 + randomGenerator.nextInt(10000);

            String keyVal= "jw6CGR29ps19rKGhTGBvZQ==";
            secretKey = keyVal.getBytes();
            secretKey = Base64.getDecoder().decode(secretKey);
//		 String text = new String(secretKey, "UTF-8");

            // String data = "externalTransactionId=3111124112279&orderId=4167211441117c6&amount=1.00&currency=INR&description=Waapo-Sbibuddy&callbackUrl=http%3A%2F%2waapo%2FTest%2Ftest.jsp&backUrl=http%3A%2F%2F122.182.6.216%2Freceive%2F135%2FAUTH%2Fservlet%2FBankRespReceive";
            String data = "externalTransactionId=SANTC6456163084680&" +
                    ORDERID_PARAM + orderId +
                    AMOUNT_PARAM + amount +
                    CURRENCY_PARAM +
                    "&callbackUrl=http%3A%2F%2F54.169.180.254%2Ftvh_revamp%2Fbookyourhome%2Fsbibuddyresponse&backUrl=http%3A%2F%2F54.169.180.254%2Ftvh_revamp%2Fbookyourhome%2Fsbibuddyresponse";

            SecretKeySpec keySep = createKeySpec(secretKey);
            String enVal = encrypt(data, keySep);
            System.out.println("Encrypted Val =" + enVal);

        }catch(Exception e)
        {
            e.printStackTrace();
        }
    }

    public void makeRefund(String transactionId, String amount, String merchantId, String encodedKey)  throws Exception {
        // here you can change your parameters:

        //String orderId = "EAK34283S796695";
        transactionId = "527726566";
        amount = "4.00";
        merchantId = "527697601";
        encodedKey ="jw6CGR29ps19rKGhTGBvZQ==";

        // String payLoad = ORDERID_PARAM + "=" + URLEncoder.encode(orderId, "UTF-8") + "&" + AMOUNT_PARAM + "=" + URLEncoder.encode(amount, "UTF-8");
        String payLoad = TRANSACTIONID_PARAM + "=" + URLEncoder.encode(transactionId, "UTF-8") + AMOUNT_PARAM + URLEncoder.encode(amount, "UTF-8");

//		byte[] decodedKey = Base64.decodeBase64(encodedKey.getBytes());
        byte[] decodedKey = Base64.getDecoder().decode(encodedKey.getBytes());
        Key encryptionKey = new SecretKeySpec(decodedKey, "AES");

        byte[] utf8Bytes = payLoad.getBytes("UTF-8");
        byte[] encryptedBody = encrypt(encryptionKey, utf8Bytes);
        String encryptedData = Base64.getEncoder().encodeToString(encryptedBody);
//		String encryptedData = new String(Base64.encodeBase64(encryptedBody));

        String body = MERCHANTID_PARAM + "=" + merchantId + "&encryptedData=" + URLEncoder.encode(encryptedData, "UTF-8");

        String entireResponse = send("https://buddyuat.sbi.co.in/mmgw-tls/merchant/api/refund", body);
        System.out.println("entireResponse:"+ entireResponse);

        if (entireResponse.contains("encryptedData=")) {
            String encryptedResponse = entireResponse.substring(entireResponse.indexOf("encryptedData=") + "encryptedData=".length());
            encryptedResponse = URLDecoder.decode(encryptedResponse, "UTF-8");
            System.out.println("encryptedResponse:"+ encryptedResponse);

            byte[] decodedResponse = Base64.getDecoder().decode(encryptedResponse.getBytes());
//				byte[] decodedResponse = Base64.decodeBase64(encryptedResponse.getBytes());

            byte[] decryptedResponse = decrypt(encryptionKey, decodedResponse);
            String response = new String(decryptedResponse, "UTF-8");
            System.out.println("Decoded Response:" + response);

        }
    }

    public void checkStatus(String orderId, String transactionId, String merchantId, String encodedKey)  throws Exception {
        // change the order id and transaction id accordingly:
        orderId = "EAK38423365456";
        transactionId = "527726566";

        merchantId = "527697601";
        encodedKey ="jw6CGR29ps19rKGhTGBvZQ==";

        String payLoad = TRANSACTIONID_PARAM + "=" + URLEncoder.encode(transactionId, "UTF-8") + "&" + ORDERID_PARAM + URLEncoder.encode(orderId, "UTF-8");


//		byte[] decodedKey = Base64.decodeBase64(encodedKey.getBytes());
        byte[] decodedKey = Base64.getDecoder().decode(encodedKey.getBytes());
        Key encryptionKey = new SecretKeySpec(decodedKey, "AES");

        byte[] utf8Bytes = payLoad.getBytes("UTF-8");
        byte[] encryptedBody = encrypt(encryptionKey, utf8Bytes);

//		String encryptedData = new String(Base64.encodeBase64(encryptedBody));
        String encryptedData = Base64.getEncoder().encodeToString(encryptedBody);

        System.out.println("encryptedData   "+encryptedData);
        //String body = MERCHANTID_PARAM + "=" + merchantId + "&encryptedData=" + encryptedData;
        String body = MERCHANTID_PARAM + "=" + merchantId + "&encryptedData=" + URLEncoder.encode(encryptedData, "UTF-8");

        String entireResponse = send("https://buddyuat.sbi.co.in/mmgw-tls/merchant/api/status", body);
        //https://buddyuat.sbi.co.in/mmgw-tls/merchant/page/paynow

        System.out.println("entireResponse:"+ entireResponse);

        if (entireResponse.contains("encryptedData=")) {
            String encryptedResponse = entireResponse.substring(entireResponse.indexOf("encryptedData=") + "encryptedData=".length());
            encryptedResponse = URLDecoder.decode(encryptedResponse, "UTF-8");
            System.out.println("encryptedResponse:"+ encryptedResponse);

//				byte[] decodedResponse = Base64.decodeBase64(encryptedResponse.getBytes());
            byte[] decodedResponse = Base64.getDecoder().decode(encryptedResponse.getBytes());

            byte[] decryptedResponse = decrypt(encryptionKey, decodedResponse);
            String response = new String(decryptedResponse, "UTF-8");
            System.out.println("Decoded Response---------:" + response);
        }
    }

    private String encrypt(String data, SecretKeySpec key) {
        try {
            Cipher c = Cipher.getInstance(transformation);
            c.init(1, key);

            byte[] encValue = c.doFinal(data.getBytes(utf8));
            String base64 = Base64.getEncoder().encodeToString(encValue);

            return base64;
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private static byte[] encrypt(Key encryptionKey, byte[] data) throws Exception {
        Cipher c = Cipher.getInstance("AES");
        c.init(1, encryptionKey);
        return c.doFinal(data);
    }

    private static byte[] decrypt(Key encryptionKey, byte[] data) throws Exception {
        Cipher c = Cipher.getInstance("AES");
        c.init(2, encryptionKey);
        return c.doFinal(data);
    }

    private static String send(String url, String bodyToSend) throws IOException {
        URL obj = new URL(url);

        HttpsURLConnection con = (HttpsURLConnection) obj.openConnection();

        //add reuqest header
        con.setRequestMethod("POST");
        con.setRequestProperty("User-Agent", "Mozilla/5.0");
        con.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
        con.setRequestProperty("Accept", "application/x-www-form-urlencoded");
        con.setRequestProperty("Referer","http://localhost:8080");

        // Send post request
        con.setDoOutput(true);
        DataOutputStream wr = new DataOutputStream(con.getOutputStream());
        wr.writeBytes(bodyToSend);
        wr.flush();
        wr.close();

        int responseCode = con.getResponseCode();

        System.out.println("Response Code : " + responseCode);
        InputStream stream;
        if (responseCode == 200) {
            stream = con.getInputStream();
        } else {
            stream = con.getErrorStream();
        }

        BufferedReader in = new BufferedReader(
                new InputStreamReader(stream));
        String inputLine;
        StringBuffer response = new StringBuffer();

        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();

        return response.toString();
    }

    private SecretKeySpec createKeySpec(byte[] secretKey) {
        return new SecretKeySpec(secretKey, transformation);
    }
}
