package com.bridgeit.TodoApp.util;

import java.util.Date;
import java.util.concurrent.TimeUnit;

public class GenerateOtp {
	public String otp;
	public Date date;

	public String OTP() {
		date = new Date();
		int randomPin = (int) (Math.random() * 9000) + 1000;
		otp = String.valueOf(randomPin);

		return otp;
	}

	public boolean validateOtp(String enterOtp) {
		long diff = (new Date().getTime()) - date.getTime();
		long difference = TimeUnit.MILLISECONDS.toMinutes(diff);
		System.out.println("difference "+ difference);
		System.out.println("date.gettime() " + date.getTime());
		System.out.println(enterOtp);
		System.out.println(otp);
		if (enterOtp.equalsIgnoreCase(otp) && difference < 1) {
			return true;
		}
		return false;
	}
}
