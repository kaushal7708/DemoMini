package com.example.demomini;

public class postDetails {
	String title, name, time, Image;

	public postDetails() {

	}

	public postDetails(String title, String name, String time,String Image) {
		this.title = title;
		this.name = name;
		this.time = time;
		this.Image=Image;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public String getImage() {
		return Image;
	}

	public void setImage(String image) {
		Image = image;
	}
}
