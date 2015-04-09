package com.born2go.shared;

import java.io.Serializable;

public class Poster implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	Long userID;
	String userName;
	String userImg;
	
	public Poster() {
		super();
	}

	public Long getUserID() {
		return userID;
	}

	public void setUserID(Long userID) {
		this.userID = userID;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getUserImg() {
		return userImg;
	}

	public void setUserImg(String userImg) {
		this.userImg = userImg;
	}
	
}
