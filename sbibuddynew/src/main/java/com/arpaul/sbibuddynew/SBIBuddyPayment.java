package com.arpaul.sbibuddynew;

import android.util.Base64;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.security.Key;
import java.util.Random;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import javax.net.ssl.HttpsURLConnection;

/**
 * Created by ARPaul on 05-08-2016.
 */
public class SBIBuddyPayment {

    public final String EXTERNALTRANSACTIONID = "externalTransactionId=";
    public final String ORDERID_PARAM = "&orderId=";
    public final String AMOUNT_PARAM = "&amount=";
    public final String CURRENCY_PARAM = "&currency=INR";

    public static final String TRANSACTIONID_PARAM = "transactionId";
    public static final String MERCHANTID_PARAM = "merchantId";

    public static final String MERCHANTID = "528001601";
    public static final String ENCODING_KEY = "+VSvfXcO9Ygtxr7iyXZ5vQ==";

    public static final String URL_PAYNOW = "https://buddyuat.sbi.co.in/mmgw-tls/merchant/page/paynow";

    private String transformation = "AES";
    private String utf8 = "UTF-8";

    public SBIBuddyPayment(){

    }

    public String paynow1WebView(float amount) {
        String jsonData = "";
        try{
            byte[] secretKey = new byte[]{0x01};

            Random randomGenerator = new Random();
            String orderId = "EAK" + (1 + randomGenerator.nextInt(2)) * 10000 + randomGenerator.nextInt(10000);
            String externalTransactionId = "SANTC" + (1 + randomGenerator.nextInt(2)) * 10000 + randomGenerator.nextInt(10000);

            secretKey = ENCODING_KEY.getBytes(utf8);
            secretKey = Base64.decode(secretKey, Base64.DEFAULT);

            String data = EXTERNALTRANSACTIONID + externalTransactionId +
                    ORDERID_PARAM + orderId +
                    AMOUNT_PARAM + amount +
                    CURRENCY_PARAM +
                    "&callbackUrl=http%3A%2F%2F54.169.180.254%2Ftvh_revamp%2Fbookyourhome%2Fsbibuddyresponse" +
                    "&backUrl=http%3A%2F%2F54.169.180.254%2Ftvh_revamp%2Fbookyourhome%2Fsbibuddyresponse";

            SecretKeySpec keySep = createKeySpec(secretKey);
            String encryptedData = encrypt(data, keySep);
            System.out.println("Encrypted Val =" + encryptedData);


            jsonData = MERCHANTID_PARAM + "=" + MERCHANTID + "&encryptedData=" + URLEncoder.encode(encryptedData, "UTF-8");

        }catch(Exception e) {
            e.printStackTrace();
        } finally {
            return jsonData;
        }
    }

    public void paynow(String amount) {

        try{
            byte[] secretKey = new byte[]{0x01};

            Random randomGenerator = new Random();
            String orderId = "EAK" + (1 + randomGenerator.nextInt(2)) * 10000 + randomGenerator.nextInt(10000);
            String externalTransactionId = "SANTC" + (1 + randomGenerator.nextInt(2)) * 10000 + randomGenerator.nextInt(10000);

            secretKey = ENCODING_KEY.getBytes(utf8);
            secretKey = Base64.decode(secretKey, Base64.DEFAULT);

            String payLoad = EXTERNALTRANSACTIONID + URLEncoder.encode(externalTransactionId, utf8) +
                    ORDERID_PARAM + URLEncoder.encode(orderId, utf8) +
                    AMOUNT_PARAM + URLEncoder.encode(amount, utf8) +
                    CURRENCY_PARAM +
                    "&callbackUrl=http%3A%2F%2F54.169.180.254%2Ftvh_revamp%2Fbookyourhome%2Fsbibuddyresponse" +
                    "&backUrl=http%3A%2F%2F54.169.180.254%2Ftvh_revamp%2Fbookyourhome%2Fsbibuddyresponse";

            SecretKeySpec keySep = createKeySpec(secretKey);
            String encryptedData = encrypt(payLoad, keySep);

            String body = MERCHANTID_PARAM + "=" + MERCHANTID + "&encryptedData=" + URLEncoder.encode(encryptedData, utf8);

            String entireResponse = postJSON(URL_PAYNOW, body);
            System.out.println("entireResponse:"+ entireResponse);

            if (entireResponse.contains("encryptedData=")) {
                String encryptedResponse = entireResponse.substring(entireResponse.indexOf("encryptedData=") + "encryptedData=".length());
                encryptedResponse = URLDecoder.decode(encryptedResponse, utf8);
                System.out.println("encryptedResponse:"+ encryptedResponse);

                byte[] decodedResponse = Base64.decode(encryptedResponse.getBytes(utf8), Base64.DEFAULT);

//                byte[] decryptedResponse = decrypt(encryptionKey, decodedResponse);
                byte[] decryptedResponse = decrypt(keySep, decodedResponse);
                String response = new String(decryptedResponse, utf8);
                System.out.println("Decoded Response:" + response);

            }
        } catch(Exception e) {
            e.printStackTrace();
        } finally {
        }
    }

    public void makeRefund(String transactionId, String amount)  throws Exception {
        // here you can change your parameters:

        //String orderId = "EAK34283S796695";
        transactionId = "527726566";
        amount = "4.00";
//        encodedKey ="jw6CGR29ps19rKGhTGBvZQ==";

        // String payLoad = ORDERID_PARAM + "=" + URLEncoder.encode(orderId, "UTF-8") + "&" + AMOUNT_PARAM + "=" + URLEncoder.encode(amount, "UTF-8");
        String payLoad = TRANSACTIONID_PARAM + "=" + URLEncoder.encode(transactionId, "UTF-8") + AMOUNT_PARAM + URLEncoder.encode(amount, "UTF-8");

        byte[] decodedKey = Base64.decode(ENCODING_KEY.getBytes(utf8), Base64.DEFAULT);
        Key encryptionKey = new SecretKeySpec(decodedKey, "AES");

        byte[] utf8Bytes = payLoad.getBytes(utf8);
        byte[] encryptedBody = encrypt(encryptionKey, utf8Bytes);
        String encryptedData = Base64.encodeToString(encryptedBody, Base64.DEFAULT);

        String body = MERCHANTID_PARAM + "=" + MERCHANTID + "&encryptedData=" + URLEncoder.encode(encryptedData, "UTF-8");

        String entireResponse = send("https://buddyuat.sbi.co.in/mmgw-tls/merchant/api/refund", body);
        System.out.println("entireResponse:"+ entireResponse);

        if (entireResponse.contains("encryptedData=")) {
            String encryptedResponse = entireResponse.substring(entireResponse.indexOf("encryptedData=") + "encryptedData=".length());
            encryptedResponse = URLDecoder.decode(encryptedResponse, "UTF-8");
            System.out.println("encryptedResponse:"+ encryptedResponse);

            byte[] decodedResponse = Base64.decode(encryptedResponse.getBytes(utf8), Base64.DEFAULT);

            byte[] decryptedResponse = decrypt(encryptionKey, decodedResponse);
            String response = new String(decryptedResponse, "UTF-8");
            System.out.println("Decoded Response:" + response);

        }
    }

    public void checkStatus(String orderId, String transactionId)  throws Exception {
        // change the order id and transaction id accordingly:
        orderId = "EAK38423365456";
        transactionId = "527726566";

//        encodedKey ="jw6CGR29ps19rKGhTGBvZQ==";

        String payLoad = TRANSACTIONID_PARAM + "=" + URLEncoder.encode(transactionId, "UTF-8") + ORDERID_PARAM + URLEncoder.encode(orderId, "UTF-8");


        byte[] decodedKey = Base64.decode(ENCODING_KEY.getBytes(utf8), Base64.DEFAULT);
        Key encryptionKey = new SecretKeySpec(decodedKey, "AES");

        byte[] utf8Bytes = payLoad.getBytes("UTF-8");
        byte[] encryptedBody = encrypt(encryptionKey, utf8Bytes);

        String encryptedData = Base64.encodeToString(encryptedBody, Base64.DEFAULT);

        System.out.println("encryptedData   "+encryptedData);
        //String body = MERCHANTID_PARAM + "=" + merchantId + "&encryptedData=" + encryptedData;
        String body = MERCHANTID_PARAM + "=" + MERCHANTID + "&encryptedData=" + URLEncoder.encode(encryptedData, "UTF-8");

        String entireResponse = send("https://buddyuat.sbi.co.in/mmgw-tls/merchant/api/status", body);
        //https://buddyuat.sbi.co.in/mmgw-tls/merchant/page/paynow

        System.out.println("entireResponse:"+ entireResponse);

        if (entireResponse.contains("encryptedData=")) {
            String encryptedResponse = entireResponse.substring(entireResponse.indexOf("encryptedData=") + "encryptedData=".length());
            encryptedResponse = URLDecoder.decode(encryptedResponse, "UTF-8");
            System.out.println("encryptedResponse:"+ encryptedResponse);

            byte[] decodedResponse = Base64.decode(encryptedResponse.getBytes(utf8), Base64.DEFAULT);

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
            String base64 = Base64.encodeToString(encValue, Base64.DEFAULT);

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

    private String postJSON(String URL, String params) {
        HttpURLConnection httpClient = null;
        StringBuilder sb = null;
        BufferedReader reader = null;
        try {
            URL url = new URL(URL);
            httpClient = (HttpURLConnection) url.openConnection();
            httpClient.setDoInput(true);
            httpClient.setDoOutput(true);
            httpClient.setRequestMethod("POST");
            httpClient.setRequestProperty("Content-length", "application/json");
            httpClient.setRequestProperty("Accept", "application/json");
            httpClient.setUseCaches(false);
            httpClient.setAllowUserInteraction(false);
            httpClient.setConnectTimeout(10000);
            httpClient.setReadTimeout(10000);

            //set headers and method
            Writer writer = new BufferedWriter(new OutputStreamWriter(httpClient.getOutputStream(), utf8));
            writer.write(params);
            writer.flush();
            writer.close();

            httpClient.connect();

            int status = httpClient.getResponseCode();
            InputStream stream;
            switch (status) {
                case 200 :
                    stream = httpClient.getInputStream();

                    break;
                default:
                    stream = httpClient.getErrorStream();
            }

            BufferedReader br = new BufferedReader(new InputStreamReader(stream));
            sb = new StringBuilder();
            String line;
            while((line = br.readLine()) != null) {
                sb.append(line + "\n");
            }
            br.close();

        } catch(MalformedURLException ex) {
            ex.printStackTrace();
        } catch(IOException ex) {
            ex.printStackTrace();
        } catch(Exception ex) {
            ex.printStackTrace();
        } finally {
            if(httpClient != null) {
                httpClient.disconnect();
            }

            return sb.toString();
        }
    }
}
