package com.born2go.shared;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.googlecode.objectify.annotation.Cache;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;

@SuppressWarnings("serial")
@Entity(name = "Trip")
@Cache
public class Trip implements Serializable {
	
	public static final int COMPLETED_STATUS = 0;
	public static final int ONGOING_STATUS = 1;
	
	public static final int DEFAULT_THEME = 0;
	public static final int BEACH_DAWN_THEME = 1;
	public static final int BEACH_MORNING_THEME = 2;
	public static final int BEACH_SUNSET_THEME = 3;
	public static final int CITY_THEME = 4;
	public static final int COUNTRY_THEME = 5;
	public static final int FOREST_THEME = 6;
	public static final int FOREST_WATERFALL_THEME = 7;
	public static final int MOUNTAIN_SPRING_THEME = 8;
	public static final int MOUNTAIN_WINTER_THEME = 9;

	@Id
	Long id;
	
	String poster;	//trip owner
	List<String> companion = new ArrayList<String>(); //Member sharing the same trip
	List<Long> destination = new ArrayList<Long>(); //Post ID list
	List<Long> gallery = new ArrayList<Long>();		//Photos list
	
	String name;
	Date createDate;
	Date departureDate;
	String description;
	String journey;
	int status;
	int theme;
	

	public Trip() {
		super();
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getPoster() {
		return poster;
	}

	public void setPoster(String poster) {
		this.poster = poster;
	}

	public List<String> getCompanion() {
		return companion;
	}

	public void setCompanion(List<String> companion) {
		this.companion = companion;
	}

	public List<Long> getDestination() {
		return destination;
	}

	public void setDestination(List<Long> destination) {
		this.destination = destination;
	}

	public List<Long> getGallery() {
		return gallery;
	}

	public void setGallery(List<Long> gallery) {
		this.gallery = gallery;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Date getDepartureDate() {
		return departureDate;
	}

	public void setDepartureDate(Date departureDate) {
		this.departureDate = departureDate;
	}

	public String getJourney() {
		return journey;
	}

	public void setJourney(String journey) {
		this.journey = journey;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public int getTheme() {
		return theme;
	}

	public void setTheme(int theme) {
		this.theme = theme;
	}
	
}
