package com.born2go.shared.embedclass;

import java.io.Serializable;

import com.google.maps.gwt.client.LatLng;

public class Locate implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	String addressName;
	Double latitude;
	Double longitude;
	
	public Locate() {
		super();
	}

	public Locate(String addressName, LatLng latlng) {
		super();
		this.addressName = addressName;
		this.latitude = latlng.lat();
		this.longitude = latlng.lng();
	}

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
	
	public void setLatLng(LatLng latlng) {
		latitude = latlng.lat();
		longitude = latlng.lng();
	}
	
//	public LatLng getLatLng() {
//		LatLng l = LatLng.create(latitude, longitude);
//		return l;
//	}
	
}
