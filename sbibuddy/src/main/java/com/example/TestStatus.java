package com.example;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.security.Key;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;
import java.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;


public class TestStatus {

	public static final String TRANSACTIONID_PARAM ="transactionId";
	public static final String ORDERID_PARAM = "orderId";
	
	public static final String  MERCHANTID_PARAM = "merchantId";

	public static void checkStatus()  throws Exception {
		// change the order id and transaction id accordingly:
		String orderId = "EAK38423365456";
		String transactionId = "527726566";

		String merchantId = "527697601";
		String encodedKey ="jw6CGR29ps19rKGhTGBvZQ==";

		String payLoad = TRANSACTIONID_PARAM + "=" + URLEncoder.encode(transactionId, "UTF-8") + "&" + ORDERID_PARAM + "=" + URLEncoder.encode(orderId, "UTF-8");


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

	static {
	    disableSslVerification();
	}

	private static void disableSslVerification() {
	    try
	    {
	        	// Create a trust manager that does not validate certificate chains
				TrustManager[] trustAllCerts = new TrustManager[] {new X509TrustManager() {
					public X509Certificate[] getAcceptedIssuers() {
						return null;
					}
					public void checkClientTrusted(X509Certificate[] certs, String authType) {
					}
					public void checkServerTrusted(X509Certificate[] certs, String authType) {
					}
				}
	        };

	        // Install the all-trusting trust manager
	        SSLContext sc = SSLContext.getInstance("SSL");
	        sc.init(null, trustAllCerts, new java.security.SecureRandom());
	        HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());

	        // Create all-trusting host name verifier
	        HostnameVerifier allHostsValid = new HostnameVerifier() {
	            @SuppressWarnings("unused")
				public boolean verify(String hostname, String session) {
	                return true;
	            }

				public boolean verify(String arg0, SSLSession arg1) {
					return true;
				}
	        };

	        // Install the all-trusting host verifier
	        HttpsURLConnection.setDefaultHostnameVerifier(allHostsValid);
	    } catch (NoSuchAlgorithmException e) {
	        e.printStackTrace();
	    } catch (KeyManagementException e) {
	        e.printStackTrace();
	    }
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

	private static String send(String url, String bodyToSend) throws Exception {
		URL obj = new URL(url);
		
		HttpsURLConnection con = (HttpsURLConnection) obj.openConnection();

		//add reuqest header
		con.setRequestMethod("POST");
		con.setRequestProperty("User-Agent", "Mozilla/5.0");
		con.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
		con.setRequestProperty("Accept", "application/x-www-form-urlencoded");
		con.setRequestProperty("Referer","khushboo");
		
		

		// Send post request
		con.setDoOutput(true);
		DataOutputStream wr = new DataOutputStream(con.getOutputStream());
		wr.writeBytes(bodyToSend);
		wr.flush();
		wr.close();

		int responseCode = con.getResponseCode();
		// System.out.println("\nSending 'POST' request to URL : " + url);
		 //System.out.println("Post parameters : " + urlParameters);
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

}