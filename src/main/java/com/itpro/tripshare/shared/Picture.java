package com.itpro.tripshare.shared;

import java.io.Serializable;

import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;

@SuppressWarnings("serial")
@Entity(name = "Picture")
public class Picture implements Serializable {
	
	@Id
	Long id;
	
	Long onTrip;
	Long onPath;

	String key;
	String serveUrl;

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

	public Long getOnPath() {
		return onPath;
	}

	public void setOnPath(Long onPath) {
		this.onPath = onPath;
	}

	public String getServeUrl() {
		return serveUrl;
	}

	public void setServeUrl(String serveUrl) {
		this.serveUrl = serveUrl;
	}
	
}
