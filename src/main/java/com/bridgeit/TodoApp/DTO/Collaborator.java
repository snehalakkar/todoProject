package com.bridgeit.TodoApp.DTO;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

@Entity
@Table
public class Collaborator implements Serializable{
	@Id
	@GenericGenerator(name = "abc", strategy = "increment")
	@GeneratedValue(generator = "abc")
	private int collaboratorId;
	
	@ManyToOne(optional=false)
	@JoinColumn(name="ownerId")
	private User ownerId;
	
	@ManyToOne(optional=false)
	@JoinColumn(name="shareWithId")
	private User shareWithId;
	
	@ManyToOne(optional=false)
	@JoinColumn(name="todoId")
	private TodoTask todoTask;

	public int getCollaboratorId() {
		return collaboratorId;
	}

	public User getOwnerId() {
		return ownerId;
	}

	public User getShareWithId() {
		return shareWithId;
	}

	public TodoTask getTodoTask() {
		return todoTask;
	}

	public void setCollaboratorId(int collaboratorId) {
		this.collaboratorId = collaboratorId;
	}

	public void setOwnerId(User ownerId) {
		this.ownerId = ownerId;
	}

	public void setShareWithId(User shareWithId) {
		this.shareWithId = shareWithId;
	}

	public void setTodoTask(TodoTask todoTask) {
		this.todoTask = todoTask;
	}

	@Override
	public String toString() {
		return "Collaborator [collaboratorId=" + collaboratorId + ", ownerId=" + ownerId + ", shareWithId="
				+ shareWithId + ", todoTask=" + todoTask + "]";
	}
}
