package com.ebay.park.service.email;

import org.apache.commons.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import java.nio.charset.Charset;
import java.security.GeneralSecurityException;

@Component
public class EmailVerificationHelper {

	@Value("${emailVerification.secret.key}")
	private String emailVerificationSecretKey;



	public String encrypt(String value) {

		byte[] raw = emailVerificationSecretKey.getBytes();
		if (raw.length != 16) {
			throw new IllegalArgumentException("Invalid key size.");
		}

		SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
		try {
			Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
			cipher.init(Cipher.ENCRYPT_MODE, skeySpec, new IvParameterSpec(
					new byte[16]));
			byte[] ciphertext = cipher.doFinal(value.getBytes());
			return Base64.encodeBase64URLSafeString(ciphertext);
		} catch (GeneralSecurityException e) {
			throw new RuntimeException(e);
		}
	}

	public String decrypt(String encrypted) {

		byte[] raw = emailVerificationSecretKey.getBytes();
		if (raw.length != 16) {
			throw new IllegalArgumentException("Invalid key size.");
		}
		SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");

		try {
			Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
			cipher.init(Cipher.DECRYPT_MODE, skeySpec, new IvParameterSpec(
					new byte[16]));

			byte[] decoded = Base64.decodeBase64(encrypted);
			byte[] original = cipher.doFinal(decoded);

			return new String(original, Charset.forName("UTF-8"));

		} catch (GeneralSecurityException e) {
			throw new RuntimeException(e);
		}

	}
}
