package com.bridgeit.TodoApp.validation;

import java.util.regex.Pattern;

import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import com.bridgeit.TodoApp.DTO.User;

public class ServerSideValidation implements Validator {

	public boolean supports(Class<?> clazz) {
		return User.class.equals(clazz);
	}

	public void validate(Object obj, Errors err) {
		ValidationUtils.rejectIfEmpty(err, "fullName", "user.fullName.empty");
		ValidationUtils.rejectIfEmpty(err, "email", "user email empty");
		ValidationUtils.rejectIfEmpty(err, "mobile", "user.mobile.empty");
		ValidationUtils.rejectIfEmpty(err, "password", "user.password.empty");

		User user = (User) obj;

		Pattern pattern = Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);
		if (!(pattern.matcher(user.getEmail()).matches())) {
			err.rejectValue("email", "user.email.invalid");
		}
		
		Pattern pattern1 = Pattern.compile("[^0-9]*", Pattern.CASE_INSENSITIVE);
		if (!(pattern1.matcher(user.getFullName()).matches())) {
			err.rejectValue("fullName", "user.fullName.invalid");
		}
		
		Pattern pattern2 = Pattern.compile("^[0-9]{10}$", Pattern.CASE_INSENSITIVE);
		if (!(pattern2.matcher(user.getMobile()).matches())) {
			err.rejectValue("mobile", "user.mobile.invalid");
		}
		
		Pattern pattern3 = Pattern.compile("((?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%]).{6,20})", Pattern.CASE_INSENSITIVE);
		if (!(pattern3.matcher(user.getPassword()).matches())) {
			err.rejectValue("password", "user.password.invalid");
		}
	}
}
