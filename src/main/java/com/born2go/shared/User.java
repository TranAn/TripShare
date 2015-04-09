package com.born2go.shared;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;

@SuppressWarnings("serial")
@Entity(name = "User")
public class User implements Serializable {

	@Id
	String id;
	
	List<Long> myTrips = new ArrayList<Long>();

	public User() {
		super();
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public List<Long> getMyTrips() {
		return myTrips;
	}

	public void setMyTrips(List<Long> myTrips) {
		this.myTrips = myTrips;
	}

}
