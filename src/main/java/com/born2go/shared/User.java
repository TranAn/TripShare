package com.born2go.shared;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.googlecode.objectify.annotation.Cache;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;

@SuppressWarnings("serial")
@Entity(name = "User")
@Cache
public class User implements Serializable {

	@Id
	String id;
	
	List<Long> myTrips = new ArrayList<Long>();
	
	String userName;
	Date joinDate;

	public User() {
		super();
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public Date getJoinDate() {
		return joinDate;
	}

	public void setJoinDate(Date joinDate) {
		this.joinDate = joinDate;
	}

	public List<Long> getMyTrips() {
		return myTrips;
	}

	public void setMyTrips(List<Long> myTrips) {
		this.myTrips = myTrips;
	}

}
