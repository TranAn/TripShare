package com.itpro.tripshare.shared;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

public class Journey implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	List<Locate> locate;
	Map<String, List<Point>> directions;

	public List<Locate> getLocate() {
		return locate;
	}

	public void setLocate(List<Locate> locate) {
		this.locate = locate;
	}

	public Map<String, List<Point>> getDirections() {
		return directions;
	}

	public void setDirections(Map<String, List<Point>> directions) {
		this.directions = directions;
	}

	public static class Point {
		Double x;
		Double y;

		public Point() {
			super();
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

	}
}
