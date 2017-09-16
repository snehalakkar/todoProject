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

import com.bridgeit.TodoApp.DTO.FacebookAccessToken;
import com.bridgeit.TodoApp.DTO.FacebookProfile;
import com.bridgeit.TodoApp.DTO.GooglePojo;

@Component
public class FacebookConnection {

	public static final String App_Id = "114643112594248";
	public static final String Secret_Id = "62478a1ff61468b725f78ecf5caa979b";
	public static final String Redirect_URI = "http://localhost:8080/TodoApp/connectFB";

	public String getFacebookAuthURL(String unid) {

		String facebookLoginURL = "";

		try {
			facebookLoginURL = "http://www.facebook.com/dialog/oauth?" + "client_id=" + App_Id + "&redirect_uri="
					+ URLEncoder.encode(Redirect_URI, "UTF-8") + "&state=" + unid + "&response_type=code"
					+ "&scope=public_profile,email";
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		System.out.println("inside google authentication" + facebookLoginURL);
		return facebookLoginURL;
	}

	public String getAccessToken(String authCode) throws UnsupportedEncodingException {
		String fbaccessTokenURL = "https://graph.facebook.com/v2.9/oauth/access_token?" + "client_id=" + App_Id
				+ "&redirect_uri=" + URLEncoder.encode(Redirect_URI, "UTF-8") + "&client_secret=" + Secret_Id + "&code="
				+ authCode;

		ResteasyClient restCall = new ResteasyClientBuilder().build();

		ResteasyWebTarget target = restCall.target(fbaccessTokenURL);

		Form f = new Form();
		f.param("client_id", App_Id);
		f.param("client_secret", Secret_Id);
		f.param("redirect_uri", Redirect_URI);
		f.param("code", authCode);
		f.param("grant_type", "authorization_code");
		// using post request we are sending an data to web service and getting
		// json response back
		Response response = target.request().accept(MediaType.APPLICATION_JSON).post(Entity.form(f));
		System.out.println("resp ::" + response);

		FacebookAccessToken facebookAccessToken = response.readEntity(FacebookAccessToken.class);

		restCall.close();
		return facebookAccessToken.getAccess_token();
	}

	public FacebookProfile getUserProfile(String fbaccessToken) {

		String fbgetUserURL = "https://graph.facebook.com/v2.9/me?access_token=" + fbaccessToken
				+ "&fields=id,name,email,picture";
		System.out.println("fb get user details " + fbgetUserURL);
		ResteasyClient restCall = new ResteasyClientBuilder().build();
		ResteasyWebTarget target = restCall.target(fbgetUserURL);

		String headerAuth = "Bearer " + fbaccessToken;
		Response response = target.request().header("Authorization", headerAuth).accept(MediaType.APPLICATION_JSON)
				.get();
		System.out.println(" response of fb user " + response);
		FacebookProfile profile = response.readEntity(FacebookProfile.class);
		restCall.close();
		return profile;
		
	}
}
