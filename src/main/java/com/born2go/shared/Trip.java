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
	
	public enum TripStatus {
		ONGOING, COMPLETED;
	}

	@Id
	Long id;
	
	Poster poster;
	List<Poster> companion = new ArrayList<Poster>(); //Member sharing the same trip
	List<Long> destination = new ArrayList<Long>(); //Segments ID list
	List<Long> gallery = new ArrayList<Long>();		//Photos list
	
	String name;
	Date createDate;
	Date departureDate;
	String description;
	Journey journey;
	TripStatus status;
	
	public Trip() {
		super();
	}

	public Journey getJourney() {
		return journey;
	}

	public void setJourney(Journey journey) {
		this.journey = journey;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Poster getPoster() {
		return poster;
	}

	public void setPoster(Poster poster) {
		this.poster = poster;
	}

	public List<Poster> getCompanion() {
		return companion;
	}

	public void setCompanion(List<Poster> companion) {
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

	public TripStatus getStatus() {
		return status;
	}

	public void setStatus(TripStatus status) {
		this.status = status;
	}
	
}
