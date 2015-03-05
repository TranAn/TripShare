package com.itpro.tripshare.shared;

import java.io.Serializable;

public class Locate implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	String addressName;
	Double latitude;
	Double longitude;

	public String getAddressName() {
		return addressName;
	}

	public void setAddressName(String addressName) {
		this.addressName = addressName;
	}

	public Double getLatitude() {
		return latitude;
	}

	public void setLatitude(Double latitude) {
		this.latitude = latitude;
	}

	public Double getLongitude() {
		return longitude;
	}

	public void setLongitude(Double longitude) {
		this.longitude = longitude;
	}
}
