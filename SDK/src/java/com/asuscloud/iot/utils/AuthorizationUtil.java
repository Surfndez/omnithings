package com.asuscloud.iot.utils;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Calendar;
import java.util.UUID;
import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;

public class AuthorizationUtil {
    private static String SIGNATURE_METHOD = "HMAC-SHA1";

    /**
     * Generate authorization header content
     * @param progKey
     * @return
     * @throws Exception
     */
    public static String composeAuthorizationHeader(String progKey){
		if (progKey == null || progKey.trim().length() == 0) {
			return null;
		}

		StringBuilder authorization = new StringBuilder();

		String nonce = UUID.randomUUID().toString().replaceAll("-", "");
		String timestamp = String.valueOf(Calendar.getInstance().getTimeInMillis());
		String signature = null;

		// Step 1, Compose signature string
		StringBuilder signaturePre = new StringBuilder();
		signaturePre.append("nonce=").append(nonce).append("&signature_method=").append(SIGNATURE_METHOD)
				.append("&timestamp=").append(timestamp);

		// Step 2, Doing urlencode before doing hash
		String signatureURLEn = null;
		try {
			signatureURLEn = URLEncoder.encode(signaturePre.toString(), StandardCharsets.UTF_8.name());
		} catch (UnsupportedEncodingException e) {
			signatureURLEn = null;
		}

		if (signatureURLEn == null) {
			return null;
		}

		// Java Only Support HMACSHA1
		String signMethod = SIGNATURE_METHOD.replaceAll("-", "");

		// Step 3, Doing hash signature string by HMAC-SHA1
		SecretKey sk;
		try {
			sk = new SecretKeySpec(progKey.getBytes(StandardCharsets.UTF_8.name()), signMethod);
		} catch (UnsupportedEncodingException e) {
			sk = null;
			e.printStackTrace();
		}
		
		if (sk == null) {
			return null;
		}
		
		Mac m;
		try {
			m = Mac.getInstance(signMethod);
			m.init(sk);
			byte[] mac = m.doFinal(signatureURLEn.getBytes(StandardCharsets.UTF_8.name()));
			signature = URLEncoder.encode(DatatypeConverter.printBase64Binary(mac), StandardCharsets.UTF_8.name());
			
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (InvalidKeyException e) {
			e.printStackTrace();
		} catch (IllegalStateException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		if(signature == null){
			return null;
		}

		// Step 4, Doing base64 encoding & doing urlencode again

		// Final step, Put all parameters to be authorization header string
		authorization.append("signature_method=\"").append(SIGNATURE_METHOD).append("\",").append("timestamp=\"")
				.append(timestamp).append("\",").append("nonce=\"").append(nonce).append("\",").append("signature=\"")
				.append(signature).append("\"");

		return authorization.toString();
     }
}
