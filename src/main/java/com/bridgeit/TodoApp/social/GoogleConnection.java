package com.bridgeit.TodoApp.social;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Form;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.jboss.resteasy.client.jaxrs.ResteasyClient;
import org.jboss.resteasy.client.jaxrs.ResteasyClientBuilder;
import org.jboss.resteasy.client.jaxrs.ResteasyWebTarget;
import org.springframework.stereotype.Component;

import com.bridgeit.TodoApp.DTO.GooglePojo;
import com.bridgeit.TodoApp.DTO.Tokens;

@Component
public class GoogleConnection {

	public static final String CLIENT_Id = "925345495588-q85m1hq94nqn66u9mv7bpmr1pr95dkj8.apps.googleusercontent.com";
	public static final String Secret_Id = "u-S4qznbFp2DcwZo8Ko3oBYO";
	public static final String Redirect_URI = "http://localhost:8080/TodoApp/connectgoogle";

	// Access token in header
	public String Gmail_GET_USER_URL = "https://www.googleapis.com/plus/v1/people/me";

	public String getGoogleAuthURL(String unid) {

		String googleLoginURL = "";

		try {
			googleLoginURL = "https://accounts.google.com/o/oauth2/auth?client_id=" + CLIENT_Id + "&redirect_uri="
					+ URLEncoder.encode(Redirect_URI, "UTF-8") + "&state=" + unid
					+ "&response_type=code&scope=profile email&approval_prompt=force&access_type=offline";
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		System.out.println("inside google authentication" + googleLoginURL);
		return googleLoginURL;
	}

	public String getAccessToken(String authCode) throws UnsupportedEncodingException {

		String accessTokenURL = "https://accounts.google.com/o/oauth2/token";

		ResteasyClient restCall = new ResteasyClientBuilder().build();

		ResteasyWebTarget target = restCall.target(accessTokenURL);

		Form f = new Form();
		f.param("client_id", CLIENT_Id);
		f.param("client_secret", Secret_Id);
		f.param("redirect_uri", Redirect_URI);
		f.param("code", authCode);
		f.param("grant_type", "authorization_code");
		// using post request we are sending an data to web service and getting
		// json response back
		Response response = target.request().accept(MediaType.APPLICATION_JSON).post(Entity.form(f));

		String accessToken = response.readEntity(String.class);

		restCall.close();
		System.out.println("acc" + accessToken);
		return accessToken;
	}

	public GooglePojo getUserProfile(String accessToken) {

		System.out.println("gmail details " + Gmail_GET_USER_URL);
		ResteasyClient restCall = new ResteasyClientBuilder().build();
		ResteasyWebTarget target = restCall.target(Gmail_GET_USER_URL);

		String headerAuth = "Bearer " + accessToken;
		Response response = target.request().header("Authorization", headerAuth).accept(MediaType.APPLICATION_JSON)
				.get();

		GooglePojo profile = response.readEntity(GooglePojo.class);
		System.out.println("profile " + profile);
		restCall.close();
		System.out.println("profile details" + profile);
		return profile;
	}
}
