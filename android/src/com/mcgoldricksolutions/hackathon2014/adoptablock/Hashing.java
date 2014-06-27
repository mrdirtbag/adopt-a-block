package com.mcgoldricksolutions.hackathon2014.adoptablock;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import android.util.Base64;

public class Hashing {
	
	/**
	 * This a very bad key *****
	 */
	private static String key = "This is a secret.";

	/**
	 * our hashing algorithm
	 * 
	 * @param input
	 * @return
	 */
	public static String Hash(String input) {
		Mac mac = null;
		try {
			mac = Mac.getInstance("HmacSHA1");
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		SecretKeySpec secret = new SecretKeySpec(key.getBytes(), mac.getAlgorithm());
		try {
			mac.init(secret);
		} catch (InvalidKeyException e) {
			e.printStackTrace();
		}
		byte[] digest = mac.doFinal(input.getBytes());
		byte[] result = Base64.encode(digest, Base64.DEFAULT);	// TODO Auto-generated method stub
		return result.toString();
	}
}
