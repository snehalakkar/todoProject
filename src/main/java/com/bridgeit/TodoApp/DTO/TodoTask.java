package com.bridgeit.TodoApp.DTO;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

@Entity
@Table
public class TodoTask implements Serializable{

	@Id
	@GenericGenerator(name = "abc", strategy = "increment")
	@GeneratedValue(generator = "abc")
	private int todoId;
	
	private String title;
	private String description;

	@ManyToOne(optional=false)
	@JoinColumn(name="userId")
    private User user;	
	
	@OneToOne(fetch=FetchType.EAGER,cascade=CascadeType.ALL)
	@JoinColumn(name="Scrapper_ID",referencedColumnName="scrapperid")
	private WebScrapper webScrapper;
	
	private Date createdDate;
	
	private String color;
	
	private Date reminder;
	
	private boolean archieve;
	
	private boolean trash;
	
	private boolean pin;
	
	@Lob
	@Column(columnDefinition="mediumblob")
	private String image;
	
	public int getTodoId() {
		return todoId;
	}
	public void setTodoId(int todoId) {
		this.todoId = todoId;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}
	public Date getCreatedDate() {
		return createdDate;
	}
	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}
	public String getColor() {
		return color;
	}
	public void setColor(String color) {
		this.color = color;
	}
	public Date getReminder() {
		return reminder;
	}
	public void setReminder(Date reminder) {
		this.reminder = reminder;
	}
	public boolean isArchieve() {
		return archieve;
	}
	public void setArchieve(boolean archieve) {
		this.archieve = archieve;
	}
	public boolean isTrash() {
		return trash;
	}
	public void setTrash(boolean trash) {
		this.trash = trash;
	}
	public boolean isPin() {
		return pin;
	}
	public void setPin(boolean pin) {
		this.pin = pin;
	}
	public WebScrapper getWebScrapper() {
		return webScrapper;
	}
	public void setWebScrapper(WebScrapper webScrapper) {
		this.webScrapper = webScrapper;
	}
	public String getImage() {
		return image;
	}
	public void setImage(String image) {
		this.image = image;
	}
	
	@Override
	public String toString() {
		return "TodoTask [todoId=" + todoId + ", title=" + title + ", description=" + description + ", user=" + user
				+ ", webScrapper=" + webScrapper + ", createdDate=" + createdDate + ", color=" + color + ", reminder="
				+ reminder + ", archieve=" + archieve + ", trash=" + trash + ", pin=" + pin + ", image=" + image + "]";
	}
}
