package com.bridgeit.TodoApp.controller;

import java.security.NoSuchAlgorithmException;
import java.util.Properties;
import java.util.UUID;

import javax.mail.Authenticator;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import com.bridgeit.TodoApp.DTO.User;
import com.bridgeit.TodoApp.Service.UserService;
import com.bridgeit.TodoApp.json.Response;
import com.bridgeit.TodoApp.json.UserFieldError;
import com.bridgeit.TodoApp.util.EmailUtil;
import com.bridgeit.TodoApp.util.PasswordEncryptionUsingHashing;
import com.bridgeit.TodoApp.validation.ServerSideValidation;

@RestController
public class UserRegController {

	@Autowired
	ServerSideValidation serverSideValidation;

	@Autowired
	UserService userService;

	private static final Logger logger = Logger.getLogger("regFile");
	private static final Logger logger1 = Logger.getRootLogger();

	@RequestMapping(value = "/registerUser1", method = RequestMethod.POST)
	public ResponseEntity<Response> registerUseri(@RequestBody User user, BindingResult result, HttpServletRequest req)
			throws NoSuchAlgorithmException // Response
	{
		UserFieldError err = null;
		logger1.info("rootlogger demo...");

		serverSideValidation.validate(user, result);

		String encriptedpwd = PasswordEncryptionUsingHashing.passwordEncryption(user.getPassword());
		user.setPassword(encriptedpwd);

		if (result.hasErrors()) {
			logger.info("binding result error,Registration fails ");
			err = new UserFieldError();
			err.setStatus(-1);
			err.setMessage("binding result error");
			return new ResponseEntity<Response>(err, HttpStatus.NOT_ACCEPTABLE);
		}

		try {
			// just to remove multiple spaces between words
			String trimfullName = user.getFullName().replaceAll("( +)", " ").trim();
			user.setFullName(trimfullName);
			System.out.println("ch email :" + user.getEmail());
			String acc_token = UUID.randomUUID().toString().replaceAll("-", "");
			user.setEmailToken(acc_token);
			userService.registerUser(user);
			logger.info("user registered successfully... ");

			System.out.println("SimpleEmail Start");

			// String
			// url="http://localhost:8080/TodoApp/activateStatuscode?id="+user.getUserId()+"&acc_token="+acc_token+"?method=post";
			String url = "http://localhost:8080/TodoApp/activateStatuscode/" + user.getUserId() + "/" + acc_token;
			final String fromEmail = "bridgelabzsolutions@gmail.com"; // requires
																		// valid
																		// gmail
																		// id
			final String password = "bridgelabz"; // correct password for gmail
													// id
			final String toEmail = user.getEmail(); // can be any email id

			System.out.println("TLSEmail Start");
			Properties props = new Properties();
			props.put("mail.smtp.host", "smtp.gmail.com"); // SMTP Host
			props.put("mail.smtp.port", "587"); // TLS Port
			props.put("mail.smtp.auth", "true"); // enable authentication
			props.put("mail.smtp.starttls.enable", "true"); // enable STARTTLS

			// create Authenticator object to pass in Session.getInstance
			// argument
			Authenticator auth = new Authenticator() {
				// override the getPasswordAuthentication method
				protected PasswordAuthentication getPasswordAuthentication() {
					return new PasswordAuthentication(fromEmail, password);
				}
			};
			Session session = Session.getInstance(props, auth);

			EmailUtil.sendEmail(session, toEmail, "Confirm your account on TodoApp",
					"Thanks for signing up with TodoApp! You must follow this link to activate your account: \n\n "
							+ url);

		} catch (Exception e) {
			logger.error("sorry, some error occured while saving obj in DB,user not registered ", e);
			err = new UserFieldError();
			err.setStatus(2);
			err.setMessage(e.getMessage());
			return new ResponseEntity<Response>(err, HttpStatus.SERVICE_UNAVAILABLE);

		}
		err = new UserFieldError();
		err.setStatus(1);
		err.setMessage("working fine");
		return new ResponseEntity<Response>(err, HttpStatus.OK);
	}

	@RequestMapping(value = "/app/updateUserProfile",method=RequestMethod.POST, produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<Void> updateUserProfile(@RequestBody User user) {
		System.out.println("u " + user);
		userService.updateUserProfile(user);
		return new ResponseEntity<Void>(HttpStatus.OK);
	}

	@DeleteMapping(value = "/deleteUser/{id}")
	public ResponseEntity<Boolean> deleteUser(@PathVariable("id") int userId, @RequestBody User user) {
		user.setUserId(userId);
		int id = userService.deleteUser(userId, user);
		if (id == 0) {
			return new ResponseEntity<Boolean>(false, HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<Boolean>(true, HttpStatus.OK);
	}
}
