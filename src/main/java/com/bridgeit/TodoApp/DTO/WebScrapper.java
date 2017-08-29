package com.bridgeit.TodoApp.DTO;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

@Entity
@Table
public class WebScrapper implements Serializable{
	
	@Id
	@GenericGenerator(name = "abc", strategy = "increment")
	@GeneratedValue(generator = "abc")
	private int scrapperid;
	
	private String title;
	private String host;
	private String image;
	
	public int getScrapperid() {
		return scrapperid;
	}
	public void setScrapperid(int scrapperid) {
		this.scrapperid = scrapperid;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getHost() {
		return host;
	}
	public void setHost(String host) {
		this.host = host;
	}
	public String getImage() {
		return image;
	}
	public void setImage(String image) {
		this.image = image;
	}
	
	@Override
	public String toString() {
		return "WebScrapper [scrapperid=" + scrapperid + ", title=" + title + ", host=" + host
				+ ", image=" + image + "]";
	}
}
