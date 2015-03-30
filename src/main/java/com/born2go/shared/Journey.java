package com.born2go.shared;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.google.maps.gwt.client.LatLng;

public class Journey implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	List<Locate> locates;
	List<Point> directions = new ArrayList<Point>();

	public List<Locate> getLocates() {
		return locates;
	}

	public void setLocates(List<Locate> locates) {
		this.locates = locates;
	}

	public List<Point> getDirections() {
		return directions;
	}

	public void setDirections(List<Point> directions) {
		this.directions = directions;
	}

	public static class Point implements Serializable{
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		
		Double x;
		Double y;

		public Point() {
			super();
		}
		
		public Point(LatLng latlng) {
			this.x = latlng.lat();
			this.y = latlng.lng();
		}

		public Point(Double x, Double y) {
			this.x = x;
			this.y = y;
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
}
