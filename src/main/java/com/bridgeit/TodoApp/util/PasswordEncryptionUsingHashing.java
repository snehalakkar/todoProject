package com.bridgeit.TodoApp.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class PasswordEncryptionUsingHashing {
	public static String passwordEncryption(String password) throws NoSuchAlgorithmException {
		System.out.println("pwd :" + password);
		MessageDigest md = MessageDigest.getInstance("SHA-256");
		md.update(password.getBytes());
		
		byte byteData[] = md.digest();

		// convert the byte to hex format
		StringBuffer hexString = new StringBuffer();

		for (int i = 0; i < byteData.length; i++) {
			String hex = Integer.toHexString(0xff & byteData[i]);
			if (hex.length() == 1)
				hexString.append('0');
			hexString.append(hex);
		}
		System.out.println("Hex format : " + hexString.toString());

		return hexString.toString();

	}
}
