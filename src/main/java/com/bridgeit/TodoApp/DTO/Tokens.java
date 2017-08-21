package com.bridgeit.TodoApp.DTO;

import java.io.Serializable;

import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.hibernate.annotations.GenericGenerator;

@JsonIgnoreProperties(ignoreUnknown=true)
@Entity
@Table
public class Tokens implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GenericGenerator(name = "abc", strategy = "increment")
	@GeneratedValue(generator = "abc")
	private int tokenid;
	
	private String accessToken;
	private String refreshToken;
	private Date createdOn;
	

	@OneToOne(fetch=FetchType.LAZY,cascade=CascadeType.DETACH)
	@JoinColumn(name="USER_ID",referencedColumnName="userId")
	private User getUser;
	
	public int getTokenid() {
		return tokenid;
	}

	public void setTokenid(int tokenid) {
		this.tokenid = tokenid;
	}
	
	public String getAccessToken() {
		return accessToken;
	}

	public void setAccessToken(String accessToken) {
		this.accessToken = accessToken;
	}

	public String getRefreshToken() {
		return refreshToken;
	}

	public void setRefreshToken(String refreshToken) {
		this.refreshToken = refreshToken;
	}

	public Date getCreatedOn() {
		return createdOn;
	}

	public void setCreatedOn(Date createdOn) {
		this.createdOn = createdOn;
	}

	public User getGetUser() {
		return getUser;
	}

	public void setGetUser(User getUser) {
		this.getUser = getUser;
	}

	@Override
	public String toString() {
		return "Tokens [tokenid=" + tokenid + ", accessToken=" + accessToken + ", refreshToken=" + refreshToken
				+ ", createdOn=" + createdOn + ", getUser=" + getUser + "]";
	}
}
