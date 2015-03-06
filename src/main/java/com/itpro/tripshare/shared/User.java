package com.itpro.tripshare.shared;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Index;
import com.googlecode.objectify.annotation.Unindex;

@SuppressWarnings("serial")
@Entity(name = "User")
@Unindex
public class User implements Serializable {
	@Index
	@Id
	Long id;
	@Index
	List<Long> myTrip = new ArrayList<Long>();
	@Index
	String accessToken;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public List<Long> getMyTrip() {
		return myTrip;
	}

	public void setMyTrip(List<Long> myTrip) {
		this.myTrip = myTrip;
	}

	public String getAccessToken() {
		return accessToken;
	}

	public void setAccessToken(String accessToken) {
		this.accessToken = accessToken;
	}
}