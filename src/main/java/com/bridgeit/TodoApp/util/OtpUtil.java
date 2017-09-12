package com.bridgeit.TodoApp.util;

public class OtpUtil {
	public static String otp;

	public static String OTP() {
		int randomPin = (int) (Math.random() * 9000) + 1000;
		otp = String.valueOf(randomPin);
		return otp;
	}

	public static boolean validateOtp(String enterOtp) {
		System.out.println(enterOtp);
		System.out.println(otp);
		if(enterOtp.equalsIgnoreCase(otp)){
			return true;
		}
		return false;
	}
}
