package com.bridgeit.TodoApp.DTO;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

@Entity
@Table
public class User implements Serializable {

	@Id
	@GenericGenerator(name = "abc", strategy = "increment")
	@GeneratedValue(generator = "abc")
	private int userId;

	private String fullName;

	private String email;

	private String mobile;

	private String password;
	
	@Lob
	private String profile;

	private boolean statusCode;

	private String emailToken;

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public String getFullName() {
		return fullName;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getProfile() {
		return profile;
	}

	public void setProfile(String profile) {
		this.profile = profile;
	}

	public boolean isStatusCode() {
		return statusCode;
	}

	public void setStatusCode(boolean statusCode) {
		this.statusCode = statusCode;
	}

	public String getEmailToken() {
		return emailToken;
	}

	public void setEmailToken(String emailToken) {
		this.emailToken = emailToken;
	}

	@Override
	public String toString() {
		return "User [userId=" + userId + ", fullName=" + fullName + ", email=" + email + ", mobile=" + mobile
				+ ", password=" + password + ", profile=" + profile + ", statusCode=" + statusCode + ", emailToken="
				+ emailToken + "]";
	}
}
