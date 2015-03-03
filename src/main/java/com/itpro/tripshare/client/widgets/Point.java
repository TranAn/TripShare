package com.itpro.tripshare.client.widgets;

import java.io.Serializable;

import com.google.maps.gwt.client.LatLng;

public class Point implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Double x;
	private Double y;

	public Point() {
		super();
	}
	
	public Point(LatLng l) {
		x = l.lat();
		y = l.lng();
	}

	public Double getX() {
		return x;
	}

	public void setX(Double x) {
		this.x = x;
	}

	public Double getY() {
		return y;
	}

	public void setY(Double y) {
		this.y = y;
	}

	public LatLng toLatLng() {
		LatLng l = LatLng.create(x, y);
		return l;
	}

}
