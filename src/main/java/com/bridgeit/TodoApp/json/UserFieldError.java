package com.bridgeit.TodoApp.json;

import java.util.List;

import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;

public class UserFieldError extends Response{
	
	private List<ObjectError> list;

	public List<ObjectError> getList() {
		return list;
	}

	public void setList(List<ObjectError> list) {
		this.list = list;
	}

}
