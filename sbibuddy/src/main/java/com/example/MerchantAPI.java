package com.example;


import java.nio.charset.StandardCharsets;
import java.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;


public class MerchantApi {
						
	private String transformation = "AES";
	private String utf8 = "UTF-8";
	public SecretKeySpec createKeySpec(byte[] secretKey) {
		return new SecretKeySpec(secretKey, transformation);
	}

	public String encrypt(String data, SecretKeySpec key) {
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

	 public static void main(String arg[]){
		 try{
		 byte[] secretKey = new byte[]{0x01};

		 String keyVal= "jw6CGR29ps19rKGhTGBvZQ==";
		 secretKey = keyVal.getBytes();
		 secretKey = Base64.getDecoder().decode(secretKey);
//		 String text = new String(secretKey, "UTF-8");

		// String data = "externalTransactionId=3111124112279&orderId=4167211441117c6&amount=1.00&currency=INR&description=Waapo-Sbibuddy&callbackUrl=http%3A%2F%2waapo%2FTest%2Ftest.jsp&backUrl=http%3A%2F%2F122.182.6.216%2Freceive%2F135%2FAUTH%2Fservlet%2FBankRespReceive";
		 String data = "externalTransactionId=SANTC6456163084680&orderId=EAK3842336S460&amount=8.00&currency=INR&callbackUrl=http%3A%2F%2F54.169.180.254%2Ftvh_revamp%2Fbookyourhome%2Fsbibuddyresponse&backUrl=http%3A%2F%2F54.169.180.254%2Ftvh_revamp%2Fbookyourhome%2Fsbibuddyresponse";
		 MerchantApi obj = new MerchantApi();
		 SecretKeySpec keySep = obj.createKeySpec(secretKey);
		 String enVal = obj.encrypt(data, keySep);
		 System.out.println("Encrypted Val =" + enVal);

		 }catch(Exception e)
		 {
			e.printStackTrace();
		 }
	 }
}


