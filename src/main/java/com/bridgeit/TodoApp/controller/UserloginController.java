package com.bridgeit.TodoApp.controller;

import java.security.NoSuchAlgorithmException;
import java.util.Map;

import javax.mail.Session;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.bridgeit.TodoApp.DTO.Tokens;
import com.bridgeit.TodoApp.DTO.User;
import com.bridgeit.TodoApp.Service.TokenService;
import com.bridgeit.TodoApp.Service.UserService;
import com.bridgeit.TodoApp.json.Response;
import com.bridgeit.TodoApp.util.EmailUtil;
import com.bridgeit.TodoApp.util.OtpUtil;
import com.bridgeit.TodoApp.util.PasswordEncryptionUsingHashing;
import com.bridgeit.TodoApp.util.TokenManupulation;

@RestController
public class UserloginController {

	@Autowired
	UserService userService;

	@Autowired
	TokenService tokenService;

	@Autowired
	TokenManupulation tokenManupulation;

	private static final Logger logger = Logger.getLogger("loginFile");
	private static final Logger logger1 = Logger.getRootLogger();

	@PostMapping(value = "/userlogin")
	public ResponseEntity<Tokens> userLogin(@RequestBody Map<String, String> reqParam, HttpServletRequest request,
			HttpServletResponse response) throws NoSuchAlgorithmException {

		Tokens tokens = null;
		try {
			logger.warn("toDoLogin is executed!");
			logger1.info("rootlogger demo...");
			System.out.println("************");
			String email = reqParam.get("email");
			String password = reqParam.get("password");
			System.out.println(email);
			System.out.println(password);
			String encriptedpwd = PasswordEncryptionUsingHashing.passwordEncryption(password);
			User user = userService.userLogin(email, encriptedpwd);

			if (user != null) {

				logger.info("validation successfull...");

				tokens = tokenManupulation.generateTokens();

				// save user field of token class which is mapping
				tokens.setGetUser(user);
				tokenService.saveToken(tokens);

				// we dont need to send user as a response only token is
				// required

				// tokens.setGetUser(null);
				return new ResponseEntity<Tokens>(tokens, HttpStatus.OK);
			}
			return new ResponseEntity<Tokens>(tokens, HttpStatus.NOT_FOUND);
		} catch (Exception e) {
			logger.info("validation not successfull...");
			System.out.println(e.getMessage());
			e.printStackTrace();
			return new ResponseEntity<Tokens>(tokens, HttpStatus.SERVICE_UNAVAILABLE);
		}
	}

	@RequestMapping(value = "/app/logout", method = RequestMethod.POST)
	public ResponseEntity<Void> logoutUser(HttpServletRequest request) {

		String accessToken = request.getHeader("accessToken");
		/*
		 * String accessTokentodelete = accessToken.substring(1,
		 * accessToken.length() - 1);
		 */
		boolean result = false;
		try {
			result = tokenService.logoutUser(accessToken);
			if (result == true) {
				return new ResponseEntity<Void>(HttpStatus.OK);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new ResponseEntity<Void>(HttpStatus.NOT_FOUND);
	}

	@RequestMapping(value = "/forgotpwdApi", method = RequestMethod.POST)
	public ResponseEntity<Response> forgotpwdApi(@RequestBody User user) {
		Response resp = new Response();
		User userOfResetPwd;
		try {
			userOfResetPwd = userService.getUserByEmail(user.getEmail());
			if (userOfResetPwd != null) {
				String otp = OtpUtil.OTP();

				final String fromEmail = "bridgelabzsolutions@gmail.com";
				final String password = "bridgelabz";
				final String toEmail = user.getEmail();
				Session session = EmailUtil.emailparameters(fromEmail, password);
				EmailUtil.sendEmail(session, toEmail, "Your ToDoApp OTP is :"+otp,
						"Dear User, \n\n We received a request to reset your Todo password.\n\n You enter the following code to reset your Password: \n\n "
								+ otp);
				resp.setMessage("otp send to mail successfully");
				resp.setStatus(5);
				return new ResponseEntity<Response>(resp,HttpStatus.OK);
			}
			else {
				resp.setMessage("otp not send,email does not exist");
				resp.setStatus(-5);
				return new ResponseEntity<Response>(resp,HttpStatus.OK);
			}
		} catch (Exception e) {
			e.printStackTrace();
			resp.setMessage("otp not send,server error in catch");
			resp.setStatus(-55);
			return new ResponseEntity<Response>(resp,HttpStatus.OK);
		}
	}

	@RequestMapping(value = "/validateOtp", method = RequestMethod.POST)
	public ResponseEntity<Response> validateOtp(@RequestBody Map<String, Object> getOtp) {
		String otp1=(String) getOtp.get("otp");
		System.out.println("inside validateOtp " + otp1);
		Response resp = new Response();

		boolean isValidOtp = OtpUtil.validateOtp(otp1);
		System.out.println(isValidOtp);
		if (isValidOtp == true) {
			resp.setMessage("otp correct");
			resp.setStatus(6);
			return new ResponseEntity<Response>(resp, HttpStatus.OK);
		}
		resp.setMessage("otp incorrect");
		resp.setStatus(-6);
		return new ResponseEntity<Response>(resp, HttpStatus.OK);
	}
	
	@RequestMapping(value = "/resetNewPassword", method = RequestMethod.POST)
	public ResponseEntity<Response> resetNewPassword(@RequestBody Map<String, Object> resetPWD) {
		String pass1=(String) resetPWD.get("password1");
		String pass2=(String) resetPWD.get("password2");
		System.out.println("pass1 " + pass1);
		System.out.println("pass2 " + pass2);
		return null;
	}
	
}
