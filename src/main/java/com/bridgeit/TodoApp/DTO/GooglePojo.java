package com.bridgeit.TodoApp.DTO;

import java.util.List;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class GooglePojo {

	private String id;
	private List<GmailId> emails;
	private String displayName;
	private GoogleImage image;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public List<GmailId> getEmails() {
		return emails;
	}

	public void setEmails(List<GmailId> emails) {
		this.emails = emails;
	}

	public String getDisplayName() {
		return displayName;
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	public GoogleImage getImage() {
		return image;
	}

	public void setImage(GoogleImage image) {
		this.image = image;
	}

	@Override
	public String toString() {
		return "GooglePojo [id=" + id + ", emails=" + emails + ", displayName=" + displayName + ", image=" + image
				+ "]";
	}
}
