package com.itpro.tripshare.shared;

import java.io.Serializable;

import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Index;
import com.googlecode.objectify.annotation.Unindex;

@SuppressWarnings("serial")
@Unindex
@Entity(name = "Picture")
public class Picture implements Serializable {
	@Index
	@Id
	Long id;
	// id trip
	@Index
	Long onTrip;
	// save blob-key
	@Index
	String key;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getOnTrip() {
		return onTrip;
	}

	public void setOnTrip(Long onTrip) {
		this.onTrip = onTrip;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}
}
