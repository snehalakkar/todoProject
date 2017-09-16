package com.bridgeit.TodoApp.DTO;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class FacebookProfile {

	private String id;
	private String name;
	private String email;
	private UserProfile image;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public UserProfile getImage() {
		return image;
	}

	public void setImage(UserProfile image) {
		this.image = image;
	}

	@Override
	public String toString() {
		return "FaceBookProfile [id=" + id + ", name=" + name + ", email=" + email + ", image=" + image + "]";
	}
}