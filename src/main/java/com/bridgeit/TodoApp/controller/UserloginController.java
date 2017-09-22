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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.bridgeit.TodoApp.DAO.RedisInterface;
import com.bridgeit.TodoApp.DTO.Tokens;
import com.bridgeit.TodoApp.DTO.User;
import com.bridgeit.TodoApp.Service.RedisService;
import com.bridgeit.TodoApp.Service.TokenService;
import com.bridgeit.TodoApp.Service.UserService;
import com.bridgeit.TodoApp.json.Response;
import com.bridgeit.TodoApp.json.TokenResponse;
import com.bridgeit.TodoApp.util.EmailUtil;
import com.bridgeit.TodoApp.util.GenerateOtp;
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
	
	@Autowired
	RedisService redisService;

	private static final Logger logger = Logger.getLogger("loginFile");
	private static final Logger logger1 = Logger.getRootLogger();

	@RequestMapping(value = "/userlogin", method = RequestMethod.POST)
	public ResponseEntity<TokenResponse> userLogin(@RequestBody Map<String, String> reqParam,
			HttpServletRequest request, HttpServletResponse response) throws NoSuchAlgorithmException {
		TokenResponse tokenResp = new TokenResponse();
		Tokens tokens = null;
		try {
			logger.warn("toDoLogin is executed!");
			logger1.info("rootlogger demo...");
			System.out.println("************");
			String email = reqParam.get("email");
			String password = reqParam.get("password");
			String encriptedpwd = PasswordEncryptionUsingHashing.passwordEncryption(password);
			User user = userService.userLogin(email, encriptedpwd);

			if (user != null) {

				logger.info("validation successfull...");

				tokens = tokenManupulation.generateTokens();

				// save user field of token class which is mapping
				tokens.setGetUser(user);
				tokenService.saveToken(tokens);
				redisService.saveTokens(tokens);

				// we dont need to send user as a response only token is
				// required

				// tokens.setGetUser(null);

				tokenResp.setMessage("user login succ");
				tokenResp.setStatus(8);
				tokenResp.setTokens(tokens);
				return new ResponseEntity<TokenResponse>(tokenResp, HttpStatus.OK);
			}
		} catch (Exception e) {
			logger.info("validation not successfull...");
			e.printStackTrace();
			tokenResp.setMessage("user login unsucc");
			tokenResp.setStatus(-8);
			tokenResp.setTokens(null);
			return new ResponseEntity<TokenResponse>(tokenResp, HttpStatus.OK);
		}
		tokenResp.setMessage("user login unsucc");
		tokenResp.setStatus(-88);
		tokenResp.setTokens(null);
		return new ResponseEntity<TokenResponse>(tokenResp, HttpStatus.OK);
	}

	@RequestMapping(value = "/app/logout", method = RequestMethod.POST)
	public ResponseEntity<Response> logoutUser(HttpServletRequest request) {

		String accessToken = request.getHeader("accessToken");
		Response resp = new Response();
		boolean result = false;
		try {
			result = tokenService.logoutUser(accessToken);
			if (result == true) {
				resp.setStatus(555);
				resp.setMessage("user logout succ ");
				return new ResponseEntity<Response>(resp, HttpStatus.OK);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		resp.setStatus(-555);
		resp.setMessage("user logout error ");
		return new ResponseEntity<Response>(HttpStatus.OK);
	}

	@RequestMapping(value = "/forgotpwdApi", method = RequestMethod.POST)
	public ResponseEntity<Response> forgotpwdApi(@RequestBody User user, HttpServletRequest request) {
		Response resp = new Response();
		User userOfResetPwd;
		try {
			userOfResetPwd = userService.getUserByEmail(user.getEmail());
			if (userOfResetPwd != null) {
				GenerateOtp generateOtp = new GenerateOtp();
				String otp = generateOtp.OTP();
				System.out.println("otputil obj "+generateOtp.toString());
				request.getSession().setAttribute("Otp", generateOtp);

				final String fromEmail = "bridgelabzsolutions@gmail.com";
				final String password = "bridgelabz";
				final String toEmail = user.getEmail();
				Session session = EmailUtil.emailparameters(fromEmail, password);
				EmailUtil.sendEmail(session, toEmail, "Your ToDoApp OTP is :" + otp,
						"Dear User, \n\n We received a request to reset your Todo password.\n\n You enter the following code to reset your Password: \n\n "
								+ otp);
				resp.setMessage("otp send to mail successfully");
				resp.setStatus(5);
				return new ResponseEntity<Response>(resp, HttpStatus.OK);
			} else {
				resp.setMessage("otp not send,email does not exist");
				resp.setStatus(-5);
				return new ResponseEntity<Response>(resp, HttpStatus.OK);
			}
		} catch (Exception e) {
			e.printStackTrace();
			resp.setMessage("otp not send,server error in catch");
			resp.setStatus(-55);
			return new ResponseEntity<Response>(resp, HttpStatus.OK);
		}
	}

	@RequestMapping(value = "/validateOtp", method = RequestMethod.POST)
	public ResponseEntity<Response> validateOtp(@RequestBody Map<String, Object> getOtp, HttpServletRequest request) {
		String otp1 = (String) getOtp.get("otp");
		System.out.println("inside validateOtp " + otp1);
		Response resp = new Response();

		boolean isValidOtp;
		GenerateOtp generateOtp = (GenerateOtp) request.getSession().getAttribute("Otp");
		System.out.println("otpUtilobj "+ generateOtp.toString());
		if (generateOtp == null) {
			resp.setMessage("otp expired");
			resp.setStatus(-12);
			return new ResponseEntity<Response>(resp, HttpStatus.OK);
		}
		try {
			isValidOtp = generateOtp.validateOtp(otp1);
			System.out.println(isValidOtp);
			if (isValidOtp == true) {
				resp.setMessage("otp correct");
				resp.setStatus(6);
				return new ResponseEntity<Response>(resp, HttpStatus.OK);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		resp.setMessage("otp incorrect");
		resp.setStatus(-6);
		return new ResponseEntity<Response>(resp, HttpStatus.OK);
	}

	@RequestMapping(value = "/resetNewPassword", method = RequestMethod.POST)
	public ResponseEntity<Response> resetNewPassword(@RequestBody Map<String, Object> resetPWD) {
		String pass1 = (String) resetPWD.get("password1");
		String pass2 = (String) resetPWD.get("password2");
		String emailId = (String) resetPWD.get("emailId");
		Response resp = new Response();

		try {
			if (pass1.equals(pass2)) {
				User user = userService.getUserByEmail(emailId);
				String encriptedpwd = PasswordEncryptionUsingHashing.passwordEncryption(pass1);
				System.out.println("encriptedpwd " + encriptedpwd);
				int pwdStatus = userService.updateUserPassword(user.getUserId(), encriptedpwd);
				if (pwdStatus == 1) {
					resp.setMessage("pwd reset successfully");
					resp.setStatus(7);
					return new ResponseEntity<Response>(resp, HttpStatus.OK);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		resp.setMessage("pwd not reset");
		resp.setStatus(-7);
		return new ResponseEntity<Response>(resp, HttpStatus.OK);
	}
}
